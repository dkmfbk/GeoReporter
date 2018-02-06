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

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.AnagraficaComunale;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Famiglia;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Titolarita;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperAnagraficaComunale;

public class MappingInsertAnagraficaComunale {

	public static List<AnagraficaComunale> listAnagraficaComunale = WrapperAnagraficaComunale.listAnagraficaComunale;

	private static void LoadFile(File filename, File filename2, File filenameI) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		Gson gsonI = new Gson();
		JsonReader readerI;

		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(filename2));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);

			readerI = new JsonReader(new FileReader(filenameI));
			MappingTabella dataI = gsonI.fromJson(readerI, MappingTabella.class);

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(data, data2, dataI);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void associazioneMappingNomeVal(MappingTabella data, MappingTabella data2, MappingTabella dataI) {
		// ciclo la lista degli elementi AC
		for (int j = 0; j < listAnagraficaComunale.size(); j++) {

			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			// ciclo per crea la listaCHIAVI e listaATTRIBUTI richiesti dal mapping AC
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listAnagraficaComunale.get(j).getValori().get(parts[1]) != null)
						&& (listAnagraficaComunale.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listAnagraficaComunale.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}

			}

			List<Attributo> listAttributi2 = new ArrayList<Attributo>();
			// ciclo per crea la listaCHIAVI e listaATTRIBUTI richiesti dal mapping PF
			for (int i = 0; i < data2.getAttributi().size(); i++) {

				String string2 = data2.getAttributi().get(i).getNome();
				String[] parts2 = string2.split("#");

				Attributo tmp2 = new Attributo();
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());

				if ((listAnagraficaComunale.get(j).getValori().get(parts2[1]) != null)
						&& (listAnagraficaComunale.get(j).getValori().get(parts2[1]).isEmpty() == false)) {
					tmp2.setValore(listAnagraficaComunale.get(j).getValori().get(parts2[1]));
					listAttributi2.add(tmp2);
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

				if ((listAnagraficaComunale.get(j).getValori().get(partsI[1]) != null)
						&& (listAnagraficaComunale.get(j).getValori().get(partsI[1]).isEmpty() == false)) {
					tmpI.setValore(listAnagraficaComunale.get(j).getValori().get(partsI[1]));
					listAttributiI.add(tmpI);
				}

			}

			// riga di tipo RIGATABELLA per AC
			RigaTabella rigaTAC = new RigaTabella();
			rigaTAC.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTAC.setListaattributi(listAttributi);
			String numindiv = listAnagraficaComunale.get(j).getValori().get("nindiv");
			rigaTAC.setUririga("http://dkm.fbk.eu/georeporter#AC_" + numindiv);

			// relazione AC con FAM
			List<Relazione> listRelAC = new ArrayList<Relazione>();
			Relazione relACFAM = new Relazione();
			relACFAM.setNomerelazione("http://dkm.fbk.eu/georeporter#hasNucleoFamiliare");
			relACFAM.setUriDomain("http://dkm.fbk.eu/georeporter#AC_" + numindiv);
			String fam = listAnagraficaComunale.get(j).getValori().get("fam");
			relACFAM.setUriRange("http://dkm.fbk.eu/georeporter#FAM_" + fam);
			listRelAC.add(relACFAM);
			// rel ind + rel convivente

			String codfis = listAnagraficaComunale.get(j).getValori().get("fisc");
			// controllo codfis
			if (codfis!=null) {
				// riga di tipo RIGATABELLA per PF
				RigaTabella rigaTPF = new RigaTabella();
				rigaTPF.setNometabella("http://dkm.fbk.eu/georeporter#PersonaFisica");
				rigaTPF.setListaattributi(listAttributi2);
				// rigaTPF.setListachiave(listChiavi);

				rigaTPF.setUririga("http://dkm.fbk.eu/georeporter#SOG_" + codfis);

				// relazione PF con AC
				List<Relazione> listRelPFAC = new ArrayList<Relazione>();
				Relazione relPFAC = new Relazione();
				relPFAC.setNomerelazione("http://dkm.fbk.eu/georeporter#hasAnagrafica");
				relPFAC.setUriDomain("http://dkm.fbk.eu/georeporter#SOG_" + codfis);
				relPFAC.setUriRange("http://dkm.fbk.eu/georeporter#AC_" + numindiv);
				listRelPFAC.add(relPFAC);
				rigaTPF.setListarelazioni(listRelPFAC);
				insertRiga(rigaTPF);
			}else {
				
				System.out.println("CodiceFiscaleNullo "+numindiv);
			}

			// relazione INQUI con AC
			String resconv = listAnagraficaComunale.get(j).getValori().get("residenteconviv");
			if (resconv!=null) {
				Relazione relINQUIAC = new Relazione();
				relINQUIAC.setNomerelazione("http://dkm.fbk.eu/georeporter#hasResidenteConvivente");
				relINQUIAC.setUriDomain("http://dkm.fbk.eu/georeporter#SOG_" + resconv);
				relINQUIAC.setUriRange("http://dkm.fbk.eu/georeporter#AC_" + numindiv);
				listRelAC.add(relINQUIAC);
			}

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
			Relazione relIND = new Relazione();
			relIND.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIndirizzo");
			relIND.setUriDomain("http://dkm.fbk.eu/georeporter#AC_" + numindiv);
			relIND.setUriRange("http://dkm.fbk.eu/georeporter#IND_" + time);
			listRelAC.add(relIND);

			rigaTAC.setListarelazioni(listRelAC);

			insertRiga(rigaTAC);

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

public static void run() {
	
	
	
	
	String path = "file/TN_file/DGASBANN.csv";
	// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
	WrapperAnagraficaComunale.estrazioneHeaderFile(path);
	WrapperAnagraficaComunale.LetturaFile(path);
	// mapping e insert
	LoadFile(new File("file/file_mapping/mappingAnagraficaComunale.json"),
			new File("file/file_mapping/mappingPersonaFisica2.json"),
			new File("file/file_mapping/mappingIndirizzo.json"));

}
	
	
	
	
	

	
	public static void main(String[] args) {

		String path = "file/TN_file/DGASBANN.csv";
		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		WrapperAnagraficaComunale.estrazioneHeaderFile(path);
		WrapperAnagraficaComunale.LetturaFile(path);
		// mapping e insert
		LoadFile(new File("file/file_mapping/mappingAnagraficaComunale.json"),
				new File("file/file_mapping/mappingPersonaFisica2.json"),
				new File("file/file_mapping/mappingIndirizzo.json"));

	}

}
