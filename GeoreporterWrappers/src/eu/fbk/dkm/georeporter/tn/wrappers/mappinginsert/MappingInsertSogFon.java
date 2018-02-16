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
import java.util.Map;
import java.util.Date;
import java.util.Calendar;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.PersonaFisica;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.PersonaGiuridica;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.ProprietarioproTempore;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperSog;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperSogFon;

public class MappingInsertSogFon {

	private static void LoadFile1(File filename, File filename2, List<PersonaFisica> listPers) {
		// lettura fai JSON per mappatura persona Fisica
		//input json personafisica, soggetto
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
			associazioneMappingNomeVal(data, data2, listPers);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void LoadFile2(File filename, File filename2, List<PersonaGiuridica> listPers) {
		// lettura fai JSON per mappatura persona Giuridica
		//input json personagiuridica, soggetto
		
		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(filename2));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);

			associazioneMappingNomeVal2(data, data2, listPers);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private static void LoadFile3(File filename, File filename2, List<ProprietarioproTempore> listPers) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		try {

			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(filename2));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);

			associazioneMappingNomeVal3(data, data2, listPers);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public static void associazioneMappingNomeVal(MappingTabella data, MappingTabella data2,
			List<PersonaFisica> listPers) {
		//input json perso, soggetto
		// ciclo la lista degli elementi P
		for (int j = 0; j < listPers.size(); j++) {
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

				if ((listPers.get(j).getValori().get(parts[1]) != null)
						&& (listPers.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listPers.get(j).getValori().get(parts[1]));
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

				if ((listPers.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listPers.get(j).getListaValoriChiave().get(0).get(parts[1]).isEmpty() == false)) {
					tmp2.setValore(listPers.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp2);
				}

			}
			String codfis = listPers.get(j).getValori().get("codicefiscale");
			List<Map<String,String>> attributichiave_hm = listPers.get(j). getListaValoriChiave();
			String idsoggetto="";
			for (Map<String, String> map : attributichiave_hm) {
				if (map.containsKey("identificativosoggetto")){
					idsoggetto=map.get("identificativosoggetto");
				}
				
			}
			
			if (codfis.isEmpty()||codfis==null) {
			//	System.out.println("CODICE_FISCALE="+codfis);
			//	System.out.println("idsoggetto="+idsoggetto);
				codfis=idsoggetto;
				
			}
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

	public static void associazioneMappingNomeVal2(MappingTabella data, MappingTabella data2,
			List<PersonaGiuridica> listPers) {
		// ciclo la lista degli elementi PG
		for (int j = 0; j < listPers.size(); j++) {
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

				if ((listPers.get(j).getValori().get(parts[1]) != null)
						&& (listPers.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listPers.get(j).getValori().get(parts[1]));
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

				if ((listPers.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listPers.get(j).getListaValoriChiave().get(0).get(parts[1]).isEmpty() == false)) {
					tmp2.setValore(listPers.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp2);
				}

			}

			// riga di tipo RIGATABELLA per PG
			String codfis = listPers.get(j).getValori().get("codicefiscale");
			
			
			List<Map<String,String>> attributichiave_hm = listPers.get(j). getListaValoriChiave();
			String idsoggetto="";
			for (Map<String, String> map : attributichiave_hm) {
				if (map.containsKey("identificativosoggetto")){
					idsoggetto=map.get("identificativosoggetto");
				}
				
			}
			
			if (codfis==null||codfis.isEmpty()) {
				System.out.println("CODICE_FISCALE="+codfis);
				System.out.println("idsoggetto="+idsoggetto);
				codfis=idsoggetto;
				
			}
			
			
			if (!codfis.isEmpty()) {
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
	
	public static void associazioneMappingNomeVal3(MappingTabella data, MappingTabella data2,
			List<ProprietarioproTempore> listPers) {
		// ciclo la lista degli elementi PPT
		for (int j = 0; j < listPers.size(); j++) {
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

				if ((listPers.get(j).getValori().get(parts[1]) != null)
						&& (listPers.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listPers.get(j).getValori().get(parts[1]));
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

				if ((listPers.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listPers.get(j).getListaValoriChiave().get(0).get(parts[1]).isEmpty() == false)) {
					tmp2.setValore(listPers.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp2);
				}

			}
			// riga di tipo RIGATABELLA per PPT
			RigaTabella rigaTPF = new RigaTabella();
			rigaTPF.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTPF.setListaattributi(listAttributi);
			rigaTPF.setListachiave(listChiavi);
			String codamm = listPers.get(j).getListaValoriChiave().get(0).get("comunecatastale");
			String idesog = listPers.get(j).getListaValoriChiave().get(0).get("identificativosoggetto");
			rigaTPF.setUririga("http://dkm.fbk.eu/georeporter#SOG_" + codamm + "_" + idesog);
			//	System.out.println("CODICE_FISCALE="+codfis);
				System.out.println("idsoggetto="+idesog);
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
		//System.out.println(json);

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

	
	public static void run(){

		// path del file .SOG e del file con gli HEADER inseriti a mano da un utente
		String pathF = "file/TN_file/404_41097.SOG";
		String pathP = "file/TN_header/headerfilesogfon.csv";

		// chiamata per l'estrazione degli header per la composizione della lista HEADER
		WrapperSogFon.estrazioneHeaderFileSogFon(pathP);

		// chiamata per l'analisi del file .SOG FON
		WrapperSogFon.letturaFileSogFon(pathF);

		// mapping e insert degli elementi PF PG
		LoadFile1(new File("file/file_mapping/mappingPersonaFisica.json"),
				new File("file/file_mapping/mappingSoggetto.json"), WrapperSogFon.listPersonaFisicaFon);
		LoadFile2(new File("file/file_mapping/mappingPersonaGiuridica.json"),
				new File("file/file_mapping/mappingSoggetto.json"), WrapperSogFon.listPersonaGiuridicaFon);
		LoadFile3(new File("file/file_mapping/mappingProprietarioProTempore.json"),
				new File("file/file_mapping/mappingSoggetto.json"), WrapperSogFon.listProprietarioproTempore);
	}

	
	
	public static void main(String[] args) {

		// path del file .SOG e del file con gli HEADER inseriti a mano da un utente
		String pathF = "file/TN_file/404_41097.SOG";
		String pathP = "file/TN_header/headerfilesogfon.csv";

		// chiamata per l'estrazione degli header per la composizione della lista HEADER
		WrapperSogFon.estrazioneHeaderFileSogFon(pathP);

		// chiamata per l'analisi del file .SOG FON
		WrapperSogFon.letturaFileSogFon(pathF);

		// mapping e insert degli elementi PF PG
		LoadFile1(new File("file/file_mapping/mappingPersonaFisica.json"),
				new File("file/file_mapping/mappingSoggetto.json"), WrapperSogFon.listPersonaFisicaFon);
		LoadFile2(new File("file/file_mapping/mappingPersonaGiuridica.json"),
				new File("file/file_mapping/mappingSoggetto.json"), WrapperSogFon.listPersonaGiuridicaFon);
		LoadFile3(new File("file/file_mapping/mappingProprietarioProTempore.json"),
				new File("file/file_mapping/mappingSoggetto.json"), WrapperSogFon.listProprietarioproTempore);

	}

}
