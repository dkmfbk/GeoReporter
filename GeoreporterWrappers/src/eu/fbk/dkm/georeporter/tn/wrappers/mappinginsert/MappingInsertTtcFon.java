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
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Titolarita;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.TitolaritaCompleta;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperTit;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperTtcFon;

public class MappingInsertTtcFon {

	// lista di elementi TITOLARITA del file WRAPPER
	public static List<TitolaritaCompleta> listTitolaritaCompleta = WrapperTtcFon.listTitolaritaCompleta;

	private static void LoadFile(File filename, File filename2) {
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

	public static void associazioneMappingNomeVal(MappingTabella data, MappingTabella dataIntavolazione) {
		// ciclo la lista degli elementi TTC
		for (int j = 0; j < listTitolaritaCompleta.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			List<Attributo> listIntavolazioneI = new ArrayList<Attributo>();
			List<Attributo> listIntavolazioneF = new ArrayList<Attributo>();

			List<Relazione> listRelTTC = new ArrayList<Relazione>();

			// ciclo per crea la listaCHIAVI e listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listTitolaritaCompleta.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listTitolaritaCompleta.get(j).getListaValoriChiave().get(0).get(parts[1])
								.isEmpty() == false)) {
					tmp.setValore(listTitolaritaCompleta.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp);
				}

				if ((listTitolaritaCompleta.get(j).getValori().get(parts[1]) != null)
						&& (listTitolaritaCompleta.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listTitolaritaCompleta.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}

			}
			// ciclio per creare intavolazioneINIZIALE e intavolazioneFINALE
			for (int k = 0; k < dataIntavolazione.getAttributi().size(); k++) {

				String string = dataIntavolazione.getAttributi().get(k).getNome();
				String[] parts = string.split("#");

				Attributo tmpINI = new Attributo();
				Attributo tmpINF = new Attributo();

				tmpINI.setNome(dataIntavolazione.getAttributi().get(k).getNome());
				tmpINI.setMapping(dataIntavolazione.getAttributi().get(k).getMapping());
				tmpINI.setTipo(dataIntavolazione.getAttributi().get(k).getTipo());

				tmpINF.setNome(dataIntavolazione.getAttributi().get(k).getNome());
				tmpINF.setMapping(dataIntavolazione.getAttributi().get(k).getMapping());
				tmpINF.setTipo(dataIntavolazione.getAttributi().get(k).getTipo());

				if ((listTitolaritaCompleta.get(j).getIntavolazioneIniziale().getValori().get(parts[1]) != null)
						&& (listTitolaritaCompleta.get(j).getIntavolazioneIniziale().getValori().get(parts[1])
								.isEmpty() == false)) {
					tmpINI.setValore(
							listTitolaritaCompleta.get(j).getIntavolazioneIniziale().getValori().get(parts[1]));
					listIntavolazioneI.add(tmpINI);
				}

				if ((listTitolaritaCompleta.get(j).getIntavolazioneFinale().getValori().get(parts[1]) != null)
						&& (listTitolaritaCompleta.get(j).getIntavolazioneFinale().getValori().get(parts[1])
								.isEmpty() == false)) {

					// controllo data e sistemare in formato
					tmpINF.setValore(listTitolaritaCompleta.get(j).getIntavolazioneFinale().getValori().get(parts[1]));
					listIntavolazioneF.add(tmpINF);

				}

			}
			// riga di tipo RIGATABELLA per TTC

			// Intavolazione INIZIALE
			RigaTabella rigaTTTC = new RigaTabella();

			rigaTTTC.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTTTC.setListaattributi(listAttributi);
			rigaTTTC.setListachiave(listChiavi);

			String codcat = listTitolaritaCompleta.get(j).getListaValoriChiave().get(0).get("comunecatastale");
			String idetit = listTitolaritaCompleta.get(j).getValori().get("identificativotitolarita");
			// tit o ttc?
			rigaTTTC.setUririga("http://dkm.fbk.eu/georeporter#TIT_" + codcat + "_" + idetit);

			RigaTabella rigaTINI = new RigaTabella();
			rigaTINI.setNometabella("http://dkm.fbk.eu/georeporter#" + dataIntavolazione.getIdTabella().getMapping());
			rigaTINI.setListaattributi(listIntavolazioneI);
			String numni = listTitolaritaCompleta.get(j).getIntavolazioneIniziale().getValori().get("numerodocumento");
			rigaTINI.setUririga("http://dkm.fbk.eu/georeporter#NOT_" + codcat + "_" + idetit + "_" + numni);

			// creare relazione per la intavolazione iniziale con tipo documento
			// ha l'IDE
			if (!listTitolaritaCompleta.get(j).getIntavolazioneIniziale().getValori().get("numerodocumento")
					.isEmpty()) {
				List<Relazione> listRelNI = new ArrayList<Relazione>();
				Relazione relNITipo = new Relazione();
				relNITipo.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTipoDocumento");
				relNITipo.setUriDomain("http://dkm.fbk.eu/georeporter#NOT_" + codcat + "_" + idetit + "_" + numni);
				relNITipo.setUriRange("http://dkm.fbk.eu/georeporter#"
						+ listTitolaritaCompleta.get(j).getIntavolazioneIniziale().getValori().get("tipodocumento"));
				//System.out.println("TDI+"+listTitolaritaCompleta.get(j).getIntavolazioneIniziale().getValori().get("tipodocumento"));
				if(!listTitolaritaCompleta.get(j).getIntavolazioneIniziale().getValori().get("tipodocumento").equals("")) {
					//System.out.println("NI");
					listRelNI.add(relNITipo);
				}
				rigaTINI.setListarelazioni(listRelNI);
			}

			// Intavolazione FINALE
			RigaTabella rigaTINF = new RigaTabella();
			rigaTINF.setNometabella("http://dkm.fbk.eu/georeporter#" + dataIntavolazione.getIdTabella().getMapping());
			rigaTINF.setListaattributi(listIntavolazioneF);
			String numnf = listTitolaritaCompleta.get(j).getIntavolazioneFinale().getValori().get("numerodocumento");
			rigaTINF.setUririga("http://dkm.fbk.eu/georeporter#NOT_" + codcat + "_" + idetit + "_" + numnf);

			// creare relazione per la intavolazione finale con tipo documento
			if (!listTitolaritaCompleta.get(j).getIntavolazioneFinale().getValori().get("numerodocumento").isEmpty()) {
				List<Relazione> listRelNF = new ArrayList<Relazione>();
				Relazione relNFTipo = new Relazione();
				relNFTipo.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTipoDocumento");
				relNFTipo.setUriDomain("http://dkm.fbk.eu/georeporter#NOT_" + codcat + "_" + idetit + "_" + numnf);
				relNFTipo.setUriRange("http://dkm.fbk.eu/georeporter#"
						+ listTitolaritaCompleta.get(j).getIntavolazioneFinale().getValori().get("tipodocumento"));
				//System.out.println("TDF+"+listTitolaritaCompleta.get(j).getIntavolazioneFinale().getValori().get("tipodocumento"));
				if(!listTitolaritaCompleta.get(j).getIntavolazioneFinale().getValori().get("tipodocumento").equals("")) {
				//	System.out.println("NF");
				listRelNF.add(relNFTipo);
				}
				rigaTINF.setListarelazioni(listRelNF);
			}

			// inserisco IntavolazioneIniziale e Finale resisto per entrambe URI e iserisco
			// la
			// relazione nella sua riga TTC
			if (!listTitolaritaCompleta.get(j).getIntavolazioneIniziale().getValori().get("numerodocumento")
					.isEmpty()) {
				String relNIuri = insertRigaReturn(rigaTINI);
				Relazione relNI = new Relazione();
				relNI.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIntavolazioneIniziale");
				relNI.setUriDomain("http://dkm.fbk.eu/georeporter#TIT_" + codcat + "_" + idetit);
				relNI.setUriRange(relNIuri);
				listRelTTC.add(relNI);
			}

			if (!listTitolaritaCompleta.get(j).getIntavolazioneFinale().getValori().get("numerodocumento").isEmpty()) {
				String relNFuri = insertRigaReturn(rigaTINF);
				Relazione relNF = new Relazione();
				relNF.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIntavolazioneFinale");
				relNF.setUriDomain("http://dkm.fbk.eu/georeporter#TIT_" + codcat + "_" + idetit);
				relNF.setUriRange(relNFuri);
				listRelTTC.add(relNF);
			}
			// relazione tipo regime
			if (!listTitolaritaCompleta.get(j).getValori().get("regime").isEmpty()) {
				Relazione relRegimeTipo = new Relazione();
				relRegimeTipo.setNomerelazione("http://dkm.fbk.eu/georeporter#hasRegime");
				relRegimeTipo.setUriDomain("http://dkm.fbk.eu/georeporter#TIT_" + codcat + "_" + idetit);
				relRegimeTipo.setUriRange(
						"http://dkm.fbk.eu/georeporter#" + listTitolaritaCompleta.get(j).getValori().get("regime"));
				listRelTTC.add(relRegimeTipo);
			}

			String codamm = listTitolaritaCompleta.get(j).getListaValoriChiave().get(0).get("comunecatastale");
			
			String idepar = listTitolaritaCompleta.get(j).getListaValoriChiave().get(0).get("identificativoparticella");
			// creare relazione per il IDCatastale
						Relazione relTitIdCat = new Relazione();
						relTitIdCat.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIDCatastale");
						//relTitIdCat.setUriDomain("http://dkm.fbk.eu/georeporter#TIT_" + codamm + "_" + idetit + qnd);
						relTitIdCat.setUriDomain("http://dkm.fbk.eu/georeporter#TIT_" + codcat + "_" + idetit);
					
						String codpar = "PAR_" + codamm + "_" + idepar;
						codpar = codpar.replaceAll("\\s+", "");
						String idecat = getICfromPAR(codpar);
						if (idecat.equals("FAIL")) {
							idecat = "http://dkm.fbk.eu/georeporter#PAR_000000000";
							 System.out.println("Particella non presente: "+codpar);
						}
						relTitIdCat.setUriRange(idecat);
						listRelTTC.add(relTitIdCat);

						// creare relazione per il SOG
				Relazione relTitSOG = new Relazione();
				relTitSOG.setNomerelazione("http://dkm.fbk.eu/georeporter#hasSoggetto");
				relTitSOG.setUriDomain("http://dkm.fbk.eu/georeporter#TIT_" + codcat + "_" + idetit);
				String cod = listTitolaritaCompleta.get(j).getListaValoriChiave().get(0).get("identificativosoggetto");
				cod = cod.replaceAll("\\s+", "");
				String codfis = getSOGfromIDESOG("SOG_"+cod);
						
				if(codfis.equals("FAIL")) {
					codfis="http://dkm.fbk.eu/georeporter#SOG_0000000";
			         System.out.println("Soggetto non presente: "+cod);
				}
				relTitSOG.setUriRange(codfis);
				listRelTTC.add(relTitSOG);
						// setto relazioni
					
			
			
			// setto relazioni
			rigaTTTC.setListarelazioni(listRelTTC);
			// inserisco l'elemento di RIGATABELLA TIT dopo aver inserito NOTE e creato le
			// relazioni
			insertRiga(rigaTTTC);

		}
	}

	// metodo per l'inserimento dell'elemento pronto dopo il mapping
	public static String insertRigaReturn(RigaTabella riga) {

		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
		String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";

		Gson gson = new Gson();
		String json = gson.toJson(riga);
	//	System.out.println(json);

		String output = null;

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

			// String output;
			// System.out.println("Output from Server:\n");
			while ((output = responseBuffer.readLine()) != null) {
				// System.out.println(output);
				// output = responseBuffer.readLine();
				return output;
			}

			httpConnection.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return output;
	}
	// metodo che dato UI mi restituisce IDESOG
	public static String getSOGfromIDESOG(String ui_) {
		String result = "FAIL";
		Client client = Client.create();
		String ui = ui_.replaceAll("\\s+", "");
		WebResource webResource = client.resource(
				"http://localhost:8080/GeoreporterService/servizio/rest/urisoggettodaid?identificativoSoggetto=" + ui);

		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

		// Status 200 is successful.
		if (response.getStatus() != 200) {
			System.out.println("Failed with HTTP Error code: " + response.getStatus());
			String error = response.getEntity(String.class);
			System.out.println("Error: " + error);

			return result;
		}
		return response.getEntity(String.class);

	}

	public static void insertRiga(RigaTabella riga) {

		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
		String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";

		Gson gson = new Gson();
		String json = gson.toJson(riga);

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
			while ((output = responseBuffer.readLine()) != null) {

			}

			httpConnection.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	// metodo che dato UI mi restituisce IDECATASTALE
	public static String getICfromPAR(String par_) {

		String result = "FAIL";
		Client client = Client.create();
String par=par_.replaceAll("\\s+", "");
		WebResource webResource = client
				.resource("http://localhost:8080/GeoreporterService/servizio/rest/icfromupar?par=" + par);
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

		// Status 200 is successful.
		if (response.getStatus() != 200) {
			System.out.println("Failed with HTTP Error code: " + response.getStatus());
			String error = response.getEntity(String.class);
			System.out.println("Error: " + error);

			return result;
		}
		return response.getEntity(String.class);

	}
	public static void run() {
		String pathT = "file/TN_file/404_41097.TTC";
		String pathP = "file/TN_header/headerfilettcfon.csv";

		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		// dal file TTC
		WrapperTtcFon.estrazioneHeaderFileTtcFon(pathP);
		WrapperTtcFon.letturaFileTtcFon(pathT);
		// mapping e insert degli elementi TITOLARITA
		LoadFile(new File("file/file_mapping/mappingTitolarita2.json"),
				new File("file/file_mapping/mappingIntavolazione.json"));

	}
	public static void main(String[] args) {
		String pathT = "file/TN_file/404_41097.TTC";
		String pathP = "file/TN_header/headerfilettcfon.csv";

		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		// dal file TTC
		WrapperTtcFon.estrazioneHeaderFileTtcFon(pathP);
		WrapperTtcFon.letturaFileTtcFon(pathT);
		// mapping e insert degli elementi TITOLARITA
		LoadFile(new File("file/file_mapping/mappingTitolarita2.json"),
				new File("file/file_mapping/mappingIntavolazione.json"));

	}

}
