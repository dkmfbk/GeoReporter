package eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.Date;
import java.util.Calendar;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabelle;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.PersonaFisica;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.PersonaGiuridica;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.ProprietarioproTempore;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.ControlloValore;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperSog;

public class MappingInsertSog {

	
	private static List<PersonaFisica> listPersfis =WrapperSog.listPersonaFisica;
	private static List<PersonaGiuridica> listPersgiu= WrapperSog.listPersonaGiuridica; 
	private static List<ProprietarioproTempore> listPersprot= WrapperSog.listProprietarioproTempore;
	 
	
	
	
	public static void LoadFile(File fileMappings) {

		Gson gson = new Gson();
		JsonReader reader;

		MappingTabella mappingSoggetto= new MappingTabella();
		MappingTabella mappingPersonaFisica=new MappingTabella();
		MappingTabella mappingPersonaGiuridica=new MappingTabella();
		MappingTabella mappingProprietarioProtempore=new MappingTabella();

		try {
			reader = new JsonReader(new FileReader(fileMappings));
			MappingTabelle mappings = gson.fromJson(reader, MappingTabelle.class);

			
			List<MappingTabella> listofMappings= mappings.getMappings();

			for (MappingTabella mappingTabella : listofMappings) {
				System.out.println(mappingTabella.getIdTabella().getNome());	
				if (mappingTabella.getIdTabella().getMapping().equals("Soggetto")){
					mappingSoggetto=mappingTabella;
					
				}else if(mappingTabella.getIdTabella().getMapping().equals("PersonaFisica")){
					mappingPersonaFisica=mappingTabella;
					
					
				}else if(mappingTabella.getIdTabella().getMapping().equals("PersonaGiuridica")){
					mappingPersonaGiuridica=mappingTabella;
					
					
				}else if(mappingTabella.getIdTabella().getMapping().equals("ProprietarioProTempore")){
					mappingProprietarioProtempore=mappingTabella;
					
					
				}
			}
			

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(mappingSoggetto,mappingPersonaFisica);
			associazioneMappingNomeVal2(mappingSoggetto,mappingPersonaGiuridica);
			associazioneMappingNomeVal3(mappingSoggetto,mappingProprietarioProtempore);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
	
	
	
	
	
	
	private static void LoadFile1(File filename, File filename2) {
		// lettura fai JSON per mappatura
		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(filename2));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(data, data2);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void LoadFile2(File filename, File filename2) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(filename2));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);

			associazioneMappingNomeVal2(data, data2);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void LoadFile3(File filename, File filename2 ) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		try {

			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(filename2));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);

			associazioneMappingNomeVal3(data, data2);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void associazioneMappingNomeVal(MappingTabella data, MappingTabella data2) {
		// ciclo la lista degli elementi P
		for (int j = 0; j < listPersfis.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			// for (Attributo attributo : listChiavi) {}

			// ciclo per creare listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listPersfis.get(j).getValori().get(parts[1]) != null)
						&& (listPersfis.get(j).getValori().get(parts[1]).isEmpty() == false)) {

					tmp.setValore(listPersfis.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);

				}

			}
			// ciclo per creare listaCHIAVI richiesti dal mapping
			for (int i = 0; i < data2.getAttributi().size(); i++) {

				String string = data2.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp2 = new Attributo();
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());
    
			//	System.out.println(tmp2.getNome());
				if(tmp2.getNome().equals("http://dkm.fbk.eu/georeporter#identificativosoggetto")) {
					System.out.println(tmp2.getNome());
	              tmp2.setMultiplo(true);
	
	
                }
				if ((listPersfis.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listPersfis.get(j).getListaValoriChiave().get(0).get(parts[1]).isEmpty() == false)) {
					tmp2.setValore(listPersfis.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp2);
				}

			}
			String codfis = listPersfis.get(j).getValori().get("codicefiscale");
			String idesog = listPersfis.get(j).getListaValoriChiave().get(0).get("identificativosoggetto");
			if (codfis.isEmpty()) {
				codfis = idesog;
			}
			if ((!codfis.isEmpty()) || (idesog.equals("10000000"))) {
				// inserire log in caso di assenza COD FIS
				// riga di tipo RIGATABELLA per PF
				RigaTabella rigaTPF = new RigaTabella();
				rigaTPF.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
				rigaTPF.setListaattributi(listAttributi);
				rigaTPF.setListachiave(listChiavi);
				rigaTPF.setUririga("http://dkm.fbk.eu/georeporter#SOG_" + codfis);
				// inserimento dell'elemento
				insertRiga(rigaTPF);
			}

		}

	}

	public static void associazioneMappingNomeVal2(MappingTabella data, MappingTabella data2) {
		// ciclo la lista degli elementi PG
		for (int j = 0; j < listPersgiu.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			// ciclo per creare listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listPersgiu.get(j).getValori().get(parts[1]) != null)
						&& (listPersgiu.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listPersgiu.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);

				}

			}
			// ciclo per creare listaCHIAVI richiesti dal mapping
			for (int i = 0; i < data2.getAttributi().size(); i++) {

				String string = data2.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp2 = new Attributo();
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());

				if ((listPersgiu.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listPersgiu.get(j).getListaValoriChiave().get(0).get(parts[1]).isEmpty() == false)) {
					tmp2.setValore(listPersgiu.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp2);
				}

			}
			String codfis = listPersgiu.get(j).getValori().get("codicefiscale");
			if (!codfis.isEmpty()) {
				// riga di tipo RIGATABELLA per P
				RigaTabella rigaTPF = new RigaTabella();
				rigaTPF.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
				rigaTPF.setListaattributi(listAttributi);
				rigaTPF.setListachiave(listChiavi);
				rigaTPF.setUririga("http://dkm.fbk.eu/georeporter#SOG_" + codfis);
				// inserimento dell'elemento
				insertRiga(rigaTPF);
			}

		}

	}

	public static void associazioneMappingNomeVal3(MappingTabella data, MappingTabella data2) {
		// ciclo la lista degli elementi PPT
		for (int j = 0; j < listPersprot.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			// ciclo per creare listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listPersprot.get(j).getValori().get(parts[1]) != null)
						&& (listPersprot.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listPersprot.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}

			}
			// ciclo per creare listaCHIAVI richiesti dal mapping
			for (int i = 0; i < data2.getAttributi().size(); i++) {

				String string = data2.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp2 = new Attributo();
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());

				if ((listPersprot.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listPersprot.get(j).getListaValoriChiave().get(0).get(parts[1]).isEmpty() == false)) {
					tmp2.setValore(listPersprot.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp2);
				}

			}
			// riga di tipo RIGATABELLA per PPT
			RigaTabella rigaTPF = new RigaTabella();
			rigaTPF.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTPF.setListaattributi(listAttributi);
			rigaTPF.setListachiave(listChiavi);
			String codamm = listPersprot.get(j).getListaValoriChiave().get(0).get("codiceamministrativo");
			String idesog = listPersprot.get(j).getListaValoriChiave().get(0).get("identificativosoggetto");
			rigaTPF.setUririga("http://dkm.fbk.eu/georeporter#SOG_" + codamm + "_" + idesog);
			// inserimento dell'elemento
			insertRiga(rigaTPF);
		}

	}

	public static void insertRiga(RigaTabella riga) {

		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
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


	public static void run(String filePath, String fileHeaderPath, String fileMappings) {
		
		
	//	String pathS = "file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.SOG";
	//	String pathP = "file/TN_header/headerfilesog.csv";

		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		// dal file SOG
		WrapperSog.estrazioneHeaderFileSog(fileHeaderPath);
		WrapperSog.letturaFileSog(filePath);
		// mapping e insert degli elementi PF PG PPT
		
		
	
		 LoadFile(new File(fileMappings));
		 
		 
		 
	//	LoadFile1(new File("file/file_mapping/mappingPersonaFisica.json"),
	//			new File("file/file_mapping/mappingSoggetto.json"));

	//	LoadFile2(new File("file/file_mapping/mappingPersonaGiuridica.json"),
	//			new File("file/file_mapping/mappingSoggetto.json"));

	//	LoadFile3(new File("file/file_mapping/mappingProprietarioProTempore.json"),
	//			new File("file/file_mapping/mappingSoggetto.json"));
		
			
		
	}
	
	public static void main(String[] args) {
		// IDR0000115470_TIPOFACSN_CAMML322
		String pathS = "file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.SOG";
		String pathP = "file/TN_header/headerfilesog.csv";

		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		// dal file SOG
		WrapperSog.estrazioneHeaderFileSog(pathP);
		WrapperSog.letturaFileSog(pathS);
		// mapping e insert degli elementi PF PG PPT
		LoadFile1(new File("file/file_mapping/mappingPersonaFisica.json"),
				new File("file/file_mapping/mappingSoggetto.json"));

		LoadFile2(new File("file/file_mapping/mappingPersonaGiuridica.json"),
				new File("file/file_mapping/mappingSoggetto.json"));

		LoadFile3(new File("file/file_mapping/mappingProprietarioProTempore.json"),
				new File("file/file_mapping/mappingSoggetto.json"));

	}

}
