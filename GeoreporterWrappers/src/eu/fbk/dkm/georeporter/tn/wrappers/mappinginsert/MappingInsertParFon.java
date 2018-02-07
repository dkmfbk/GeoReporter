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
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Particella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Titolarita;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperParFon;

public class MappingInsertParFon {

	// lista di elementi PARTICELLA del file WRAPPER
	public static List<Particella> listParticella = WrapperParFon.listParticella;

	private static void LoadFile(File filename, File filenameNote, File filename2) {
		// lettura fai JSON per mappatura
		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		Gson gsonNote = new Gson();
		JsonReader readerNote;

		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			readerNote = new JsonReader(new FileReader(filenameNote));
			MappingTabella dataNote = gsonNote.fromJson(readerNote, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(filename2));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);
			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(data, dataNote, data2);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void associazioneMappingNomeVal(MappingTabella data, MappingTabella dataIntavolazione,
			MappingTabella data2) {
		// ciclo la lista degli elementi PAR
		for (int j = 0; j < listParticella.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			List<Attributo> listAttributi2 = new ArrayList<Attributo>();
			List<Attributo> listChiavi2 = new ArrayList<Attributo>();

			List<Attributo> listIntavolazioneI = new ArrayList<Attributo>();
			List<Attributo> listIntavolazioneF = new ArrayList<Attributo>();

			List<Relazione> listRelPAR = new ArrayList<Relazione>();

			// ciclo per crea la listaCHIAVI e listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listParticella.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listParticella.get(j).getListaValoriChiave().get(0).get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listParticella.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp);
				}

				if ((listParticella.get(j).getValori().get(parts[1]) != null)
						&& (listParticella.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listParticella.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}

			}
			// ciclio per creare IntavolazioneINIZIALE e FINALE
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

				if ((listParticella.get(j).getIntavolazioneIniziale().getValori().get(parts[1]) != null)
						&& (listParticella.get(j).getIntavolazioneIniziale().getValori().get(parts[1])
								.isEmpty() == false)) {
					tmpINI.setValore(listParticella.get(j).getIntavolazioneIniziale().getValori().get(parts[1]));
					listIntavolazioneI.add(tmpINI);
				}

				if ((listParticella.get(j).getIntavolazioneFinale().getValori().get(parts[1]) != null)
						&& (listParticella.get(j).getIntavolazioneFinale().getValori().get(parts[1])
								.isEmpty() == false)) {
					tmpINF.setValore(listParticella.get(j).getIntavolazioneFinale().getValori().get(parts[1]));
					listIntavolazioneF.add(tmpINF);
				}
			}
			// ide cat
			for (int i = 0; i < data2.getAttributi().size(); i++) {
				String string = data2.getAttributi().get(i).getNome();
				String[] parts = string.split("#");
				Attributo tmp2 = new Attributo();
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());

				if ((listParticella.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listParticella.get(j).getListaValoriChiave().get(0).get(parts[1]).isEmpty() == false)) {
					tmp2.setValore(listParticella.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi2.add(tmp2);
				}
				if ((listParticella.get(j).getValori().get(parts[1]) != null)
						&& (listParticella.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp2.setValore(listParticella.get(j).getValori().get(parts[1]));
					listAttributi2.add(tmp2);
				}
			}
			List<Relazione> listRelPartIdeCat = new ArrayList<Relazione>();

			String codamm = listParticella.get(j).getListaValoriChiave().get(0).get("comunecatastale");
			String num = listParticella.get(j).getValori().get("numero");
			String den = listParticella.get(j).getValori().get("denominatore");
			String idepar = listParticella.get(j).getListaValoriChiave().get(0).get("identificativoparticella");

			// Intavolazione INIZIALE
			RigaTabella rigaTPAR = new RigaTabella();

			rigaTPAR.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTPAR.setListaattributi(listAttributi);
			if (!listChiavi.isEmpty()) {
				rigaTPAR.setListachiave(listChiavi);
			}
			rigaTPAR.setUririga("http://dkm.fbk.eu/georeporter#PAR_" + codamm + "_" + idepar);

			RigaTabella rigaTINI = new RigaTabella();
			rigaTINI.setNometabella("http://dkm.fbk.eu/georeporter#" + dataIntavolazione.getIdTabella().getMapping());
			rigaTINI.setListaattributi(listIntavolazioneI);
			String numni = listParticella.get(j).getIntavolazioneIniziale().getValori().get("numerodocumento");
			rigaTINI.setUririga("http://dkm.fbk.eu/georeporter#INT_" + codamm + "_" + idepar + "_" + numni);

			// creare relazione per la intavolazione iniziale con tipo documento / tipo
			// intavolazione
			if (!listParticella.get(j).getIntavolazioneIniziale().getValori().get("tipodocumento").isEmpty()) {
				List<Relazione> listRelNI = new ArrayList<Relazione>();
				Relazione relNITipo = new Relazione();
				relNITipo.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTipoDocumento");
				relNITipo.setUriDomain("http://dkm.fbk.eu/georeporter#INT_" + codamm + "_" + idepar + "_" + numni);
				relNITipo.setUriRange("http://dkm.fbk.eu/georeporter#"
						+ listParticella.get(j).getIntavolazioneIniziale().getValori().get("tipodocumento"));
				listRelNI.add(relNITipo);
				rigaTINI.setListarelazioni(listRelNI);
				insertRiga(rigaTINI);
			}

			// Intavolazione FINALE
			RigaTabella rigaTINF = new RigaTabella();
			rigaTINF.setNometabella("http://dkm.fbk.eu/georeporter#" + dataIntavolazione.getIdTabella().getMapping());
			rigaTINF.setListaattributi(listIntavolazioneI);
			String numnf = listParticella.get(j).getIntavolazioneFinale().getValori().get("numerodocumento");
			rigaTINF.setUririga("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + idepar + "_" + numnf);

			// creare relazione per la intavolazione iniziale con tipo documento / tipo
			// intavolazione
			if (!listParticella.get(j).getIntavolazioneFinale().getValori().get("tipodocumento").isEmpty()) {
				List<Relazione> listRelNF = new ArrayList<Relazione>();
				Relazione relNFTipo = new Relazione();
				relNFTipo.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTipoDocumento");
				// ??
				relNFTipo.setUriDomain("http://dkm.fbk.eu/georeporter#INT_" + codamm + "_" + idepar + "_" + numnf);
				relNFTipo.setUriRange("http://dkm.fbk.eu/georeporter#"
						+ listParticella.get(j).getIntavolazioneFinale().getValori().get("tipodocumento"));
				listRelNF.add(relNFTipo);
				rigaTINF.setListarelazioni(listRelNF);
				insertRiga(rigaTINF);
			}

			// inserisco IntavolazioneIniziale e Finale resisto per entrambe URI e iserisco
			// la
			// relazione nella sua riga TTC
			if (!listParticella.get(j).getIntavolazioneIniziale().getValori().get("tipodocumento").isEmpty()) {
				// String relNIuri = insertRigaReturn(rigaTINI);
				Relazione relNI = new Relazione();
				relNI.setNomerelazione("http://dkm.fbk.eu/georeporter#hasRegistrazioneIniziale");
				relNI.setUriDomain("http://dkm.fbk.eu/georeporter#PAR_" + codamm + "_" + idepar);
				relNI.setUriRange("http://dkm.fbk.eu/georeporter#INT_" + codamm + "_" + idepar + "_" + numni);
				listRelPAR.add(relNI);
			}

			if (!listParticella.get(j).getIntavolazioneFinale().getValori().get("tipodocumento").isEmpty()) {
				// String relNFuri = insertRigaReturn(rigaTINF);
				Relazione relNF = new Relazione();
				relNF.setNomerelazione("http://dkm.fbk.eu/georeporter#hasRegistrazioneFinale");
				relNF.setUriDomain("http://dkm.fbk.eu/georeporter#PAR_" + codamm + "_" + idepar);
				relNF.setUriRange("http://dkm.fbk.eu/georeporter#INT_" + codamm + "_" + idepar + "_" + numnf);
				listRelPAR.add(relNF);
			}
			// relazione tipo particella
			if (!listParticella.get(j).getListaValoriChiave().get(0).get("tipoparticella").isEmpty()) {
				Relazione relParTipo = new Relazione();
				relParTipo.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTipoParticella");
				relParTipo.setUriDomain("http://dkm.fbk.eu/georeporter#PAR_" + codamm + "_" + idepar);
				relParTipo.setUriRange("http://dkm.fbk.eu/georeporter#"
						+ listParticella.get(j).getListaValoriChiave().get(0).get("tipoparticella"));
				listRelPAR.add(relParTipo);
			}

			// setto relazioni
			rigaTPAR.setListarelazioni(listRelPAR);
			// inserisco l'elemento di RIGATABELLA PAr dopo aver inserito INTAVOLAZIONE e
			// creato le relazioni
			insertRiga(rigaTPAR);

			RigaTabella rigaTIdeCat = new RigaTabella();
			rigaTIdeCat.setNometabella("http://dkm.fbk.eu/georeporter#" + data2.getIdTabella().getMapping());
			rigaTIdeCat.setListaattributi(listAttributi2);
			rigaTIdeCat.setListachiave(listChiavi2);
			rigaTIdeCat.setUririga("http://dkm.fbk.eu/georeporter#C" + codamm + "_N" + num + "_D" + den);

			// relazione tra particella fondiaria e ide cat
			Relazione relPartIdeCat = new Relazione();
			relPartIdeCat.setNomerelazione("http://dkm.fbk.eu/georeporter#hasParticellaFondiaria");
			relPartIdeCat.setUriDomain("http://dkm.fbk.eu/georeporter#C" + codamm + "_N" + num + "_D" + den);
			relPartIdeCat.setUriRange("http://dkm.fbk.eu/georeporter#PAR_" + codamm + "_" + idepar);
			listRelPartIdeCat.add(relPartIdeCat);

			rigaTIdeCat.setListarelazioni(listRelPartIdeCat);
			insertRiga(rigaTIdeCat);
			// test inserimenti
		}
	}

	// metodo per l'inserimento dell'elemento pronto dopo il mapping
	public static String insertRigaReturn(RigaTabella riga) {

		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
		String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";

		Gson gson = new Gson();
		String json = gson.toJson(riga);
		// System.out.println(json);

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
			while ((output = responseBuffer.readLine()) != null) {
				 //System.out.println(output);
			}

			httpConnection.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	// metodo che dato UI mi restituisce IDECATASTALE
	public static String getICfromUI(String ui) {

		String result = "C0_N0_D0_S0";
		Client client = Client.create();

		WebResource webResource = client
				.resource("http://localhost:8080/GeoreporterService/servizio/rest/icfromui?ui=" + ui);
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

		// path del file .PAR e del file con gli HEADER inseriti a mano da un utente
		String pathF = "file/TN_file/404_41097.PAR";
		String pathP = "file/TN_header/headerfileparfon.csv";

		// chiamata per l'estrazione degli header per la composizione della lista HEADER
		WrapperParFon.estrazioneHeaderFilePar(pathP);

		// chiamata per l'analisi del file .PAR
		WrapperParFon.letturaFilePar(pathF);

		// chiamata al metodo che accoppia ELEMENTO appena acquisito al NOME che serve
		// per l'inserimento
		// questo grazie ai file di mapping
		LoadFile(new File("file/file_mapping/mappingParticella.json"),
				new File("file/file_mapping/mappingIntavolazione.json"),
				new File("file/file_mapping/mappingIdentificativoCatastale.json"));

	}
	
	
	public static void main(String[] args) {

		// path del file .PAR e del file con gli HEADER inseriti a mano da un utente
		String pathF = "file/TN_file/404_41097.PAR";
		String pathP = "file/TN_header/headerfileparfon.csv";

		// chiamata per l'estrazione degli header per la composizione della lista HEADER
		WrapperParFon.estrazioneHeaderFilePar(pathP);

		// chiamata per l'analisi del file .PAR
		WrapperParFon.letturaFilePar(pathF);

		// chiamata al metodo che accoppia ELEMENTO appena acquisito al NOME che serve
		// per l'inserimento
		// questo grazie ai file di mapping
		LoadFile(new File("file/file_mapping/mappingParticella.json"),
				new File("file/file_mapping/mappingIntavolazione.json"),
				new File("file/file_mapping/mappingIdentificativoCatastale.json"));

	}

}
