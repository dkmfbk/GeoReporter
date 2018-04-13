package eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Famiglia;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabelle;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Titolarita;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperFamiglia;

public class MappingInsertFamiglia {

	public static String[] header;

	public static List<Famiglia> listFamiglia = WrapperFamiglia.listFamiglia;

	
	
	public static void LoadFile(File fileMappings) {

		Gson gson = new Gson();
		JsonReader reader;

		MappingTabella mappingFamiglia= new MappingTabella();
		MappingTabella mappingIndirizzo=new MappingTabella();

		try {
			reader = new JsonReader(new FileReader(fileMappings));
			MappingTabelle mappings = gson.fromJson(reader, MappingTabelle.class);

			
			List<MappingTabella> listofMappings= mappings.getMappings();

			for (MappingTabella mappingTabella : listofMappings) {
				System.out.println(mappingTabella.getIdTabella().getNome());	
				if (mappingTabella.getIdTabella().getMapping().equals("Famiglia")){
					mappingFamiglia=mappingTabella;
					
				}else if(mappingTabella.getIdTabella().getMapping().equals("Indirizzo")){
					mappingIndirizzo=mappingTabella;
					
					
				}
			}
			

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(mappingFamiglia,mappingIndirizzo);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
	private static void LoadFile_old(File filename, File filenameI) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gsonI = new Gson();
		JsonReader readerI;

		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			readerI = new JsonReader(new FileReader(filenameI));
			MappingTabella dataI = gsonI.fromJson(readerI, MappingTabella.class);

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(data, dataI);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void associazioneMappingNomeVal(MappingTabella data, MappingTabella dataI) {
		// ciclo la lista degli elementi FAM

		for (int j = 0; j < listFamiglia.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			// ciclo per crea la listaCHIAVI e listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listFamiglia.get(j).getValori().get(parts[1]) != null)
						&& (listFamiglia.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listFamiglia.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}

			}
			List<Attributo> listAttributiI = new ArrayList<Attributo>();
			// ciclo per crea la e listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < dataI.getAttributi().size(); i++) {

				String stringI = dataI.getAttributi().get(i).getNome();
				String[] partsI = stringI.split("#");

				Attributo tmpI = new Attributo();
				tmpI.setNome(dataI.getAttributi().get(i).getNome());
				tmpI.setMapping(dataI.getAttributi().get(i).getMapping());
				tmpI.setTipo(dataI.getAttributi().get(i).getTipo());
				 
				 if ("civico".equals(partsI[1])) {
						
		               String civico=listFamiglia.get(j).getValori().get("civico");
		               String barrato=listFamiglia.get(j).getValori().get("barrato");
		               if (!barrato.equals("")) {
		               tmpI.setValore(civico+"/"+barrato);
		               }else {
		            	   tmpI.setValore(civico);
		               }
		               listAttributiI.add(tmpI);
	                  }else 
				
				if ("descrfrazione".equals(partsI[1])) {
						
		               String via=listFamiglia.get(j).getValori().get("descrvia");
		               String frazione=listFamiglia.get(j).getValori().get("descrfrazione");


 if (via.equals("")) {
	 tmpI.setValore(frazione);
}else {
	tmpI.setValore(via+" "+frazione);
}
		               
		               listAttributiI.add(tmpI);
	                  }else 
				if ((listFamiglia.get(j).getValori().get(partsI[1]) != null)
						&& (listFamiglia.get(j).getValori().get(partsI[1]).isEmpty() == false)) {
					tmpI.setValore(listFamiglia.get(j).getValori().get(partsI[1]));
					listAttributiI.add(tmpI);
				}

			}
			//aggiunta codice comune catastale
			Attributo codComune = new Attributo();
			codComune.setNome("http://dkm.fbk.eu/georeporter#codiceComuneCatastale");
			codComune.setMapping("http://dkm.fbk.eu/georeporter#codiceComuneCatastale");
			codComune.setTipo("http://www.w3.org/2001/XMLSchema#string");
			codComune.setValore(WrapperFamiglia.codiceComunecatastale);
			listAttributiI.add(codComune);
			// riga di tipo RIGATABELLA per FAM
			RigaTabella rigaTFAM = new RigaTabella();

			rigaTFAM.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTFAM.setListaattributi(listAttributi);
			String progr = listFamiglia.get(j).getValori().get("progr");
			rigaTFAM.setUririga("http://dkm.fbk.eu/georeporter#FAM_" + progr);

			// riga di tipo RIGATABELLA per IND
			RigaTabella rigaTIND = new RigaTabella();
			rigaTIND.setNometabella("http://dkm.fbk.eu/georeporter#" + dataI.getIdTabella().getMapping());
			rigaTIND.setListaattributi(listAttributiI);
			// creo l'indirizzo univoco grazie dalla data d'inserimento
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			long time = cal.getTimeInMillis();
			rigaTIND.setUririga("http://dkm.fbk.eu/georeporter#IND_" + time);
			insertRiga(rigaTIND);

			// rel cin ind
			List<Relazione> listRelFAM = new ArrayList<Relazione>();
			Relazione relIND = new Relazione();
			relIND.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIndirizzo");
			relIND.setUriDomain("http://dkm.fbk.eu/georeporter#FAM_" + progr);
			relIND.setUriRange("http://dkm.fbk.eu/georeporter#IND_" + time);
			listRelFAM.add(relIND);

			rigaTFAM.setListarelazioni(listRelFAM);

			insertRiga(rigaTFAM);

		}
	}

	public static void insertRiga(RigaTabella riga) {

		//String targetURL = "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
		String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";

		
		Gson gson = new Gson();
		String json = gson.toJson(riga);
		// System.out.println(json);

		try {

			URL targetUrl = new URL(targetURL);

			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("Accept", "application/json");

			String input = json;

			OutputStream outputStream = httpConnection.getOutputStream();
			outputStream.write(input.getBytes());
			outputStream.flush();

			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
			}

			BufferedReader responseBuffer = new BufferedReader(
					new InputStreamReader((httpConnection.getInputStream())));

			String output;
			// System.out.println("Output from Server:\n");
			while ((output = responseBuffer.readLine()) != null) {
				// System.out.println(output);
				// output = responseBuffer.readLine();

			}

			httpConnection.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		// return output;
	}

	
	public static void run(String filePath, String fileMappings,String codicecomunecatastale) {

		//	String path = "file/TN_file/DGASBAN2.csv";
			// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
			WrapperFamiglia.estrazioneHeaderFile(filePath);
			WrapperFamiglia.LetturaFile(filePath);
			WrapperFamiglia.codiceComunecatastale=codicecomunecatastale;
			
			// mapping e insert
			LoadFile(new File("file/file_mapping/mappingFamiglia.json"));
		
		
	}
	
	
	
	
	
	
	
	
	public static void main(String[] args) {

		String path = "file/TN_file/DGASBAN2.csv";
		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		WrapperFamiglia.estrazioneHeaderFile(path);
		WrapperFamiglia.LetturaFile(path);
		WrapperFamiglia.codiceComunecatastale="L322";
		// mapping e insert
		LoadFile(new File("file/file_mapping/mappingFamiglia.json"));

	}

}
