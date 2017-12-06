package eu.fbk.dkm.georeporter.tn.wrappers;

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
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.tn.wrappers.ControlloValore;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperTit;

public class MappingInsertTit {

	// lista di elementi TITOLARITA del file WRAPPER
	public static List<Titolarita> listTitolarita = WrapperTit.listTitolarita;

	private static void LoadFile(File filename, File filenameNote) {
		// lettura fai JSON per mappatura
		Gson gson = new Gson();
		JsonReader reader;

		Gson gsonNote = new Gson();
		JsonReader readerNote;

		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			readerNote = new JsonReader(new FileReader(filenameNote));
			MappingTabella dataNote = gsonNote.fromJson(readerNote, MappingTabella.class);
			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(data, dataNote);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void associazioneMappingNomeVal(MappingTabella data, MappingTabella dataNote) {
		// ciclo la lista degli elementi TIT
		for (int j = 0; j < listTitolarita.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			List<Attributo> listNoteI = new ArrayList<Attributo>();
			List<Attributo> listNoteF = new ArrayList<Attributo>();

			List<Relazione> listRelTIT = new ArrayList<Relazione>();

			// ciclo per crea la listaCHIAVI e listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listTitolarita.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listTitolarita.get(j).getListaValoriChiave().get(0).get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listTitolarita.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp);
				}

				if ((listTitolarita.get(j).getValori().get(parts[1]) != null)
						&& (listTitolarita.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listTitolarita.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}

			}
			// ciclio per creare notaINIZIALE e notaFINALE
			for (int k = 0; k < dataNote.getAttributi().size(); k++) {

				String string = dataNote.getAttributi().get(k).getNome();
				String[] parts = string.split("#");

				Attributo tmpNI = new Attributo();
				Attributo tmpNF = new Attributo();

				tmpNI.setNome(dataNote.getAttributi().get(k).getNome());
				tmpNI.setMapping(dataNote.getAttributi().get(k).getMapping());
				tmpNI.setTipo(dataNote.getAttributi().get(k).getTipo());

				tmpNF.setNome(dataNote.getAttributi().get(k).getNome());
				tmpNF.setMapping(dataNote.getAttributi().get(k).getMapping());
				tmpNF.setTipo(dataNote.getAttributi().get(k).getTipo());

				if ((listTitolarita.get(j).getNotaIniziale().getValori().get(parts[1]) != null)
						&& (listTitolarita.get(j).getNotaIniziale().getValori().get(parts[1]).isEmpty() == false)) {

					// controllo data e sistemare in formato
					if ((parts[1].equals("datadivalidita")) || (parts[1].equals("datadiregistrazioneinatti"))) {
						tmpNI.setValore(ControlloValore
								.cambioData(listTitolarita.get(j).getNotaIniziale().getValori().get(parts[1])));
					} else {
						tmpNI.setValore(listTitolarita.get(j).getNotaIniziale().getValori().get(parts[1]));
					}
					listNoteI.add(tmpNI);
				}

				if ((listTitolarita.get(j).getNotaFinale().getValori().get(parts[1]) != null)
						&& (listTitolarita.get(j).getNotaFinale().getValori().get(parts[1]).isEmpty() == false)) {

					// controllo data e sistemare in formato
					if ((parts[1].equals("datadivalidita")) || (parts[1].equals("datadiregistrazioneinatti"))) {
						tmpNF.setValore(ControlloValore
								.cambioData(listTitolarita.get(j).getNotaFinale().getValori().get(parts[1])));
					} else {
						tmpNF.setValore(listTitolarita.get(j).getNotaFinale().getValori().get(parts[1]));
					}
					listNoteF.add(tmpNF);

				}

			}
			// riga di tipo RIGATABELLA per TIT

			// NOTA INIZIALE
			RigaTabella rigaTTIT = new RigaTabella();

			rigaTTIT.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTTIT.setListaattributi(listAttributi);
			rigaTTIT.setListachiave(listChiavi);
			String codamm = listTitolarita.get(j).getListaValoriChiave().get(0).get("codiceamministrativo");
			String ideimm = listTitolarita.get(j).getListaValoriChiave().get(0).get("identificativoimmobile");
			String idetit = listTitolarita.get(j).getValori().get("identificativotitolarita");
			rigaTTIT.setUririga("http://dkm.fbk.eu/georeporter#TIT_" + codamm + "_" + idetit);

			RigaTabella rigaTNI = new RigaTabella();
			rigaTNI.setNometabella("http://dkm.fbk.eu/georeporter#" + dataNote.getIdTabella().getMapping());
			rigaTNI.setListaattributi(listNoteI);
			String numni = listTitolarita.get(j).getNotaIniziale().getValori().get("numeronota");
			rigaTNI.setUririga("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + idetit + "_" + numni);

			// creare relazione per la nota iniziale con tipo nota
			if (!listTitolarita.get(j).getNotaIniziale().getValori().get("progressivonota").isEmpty()) {
				List<Relazione> listRelNI = new ArrayList<Relazione>();
				Relazione relNITipo = new Relazione();
				relNITipo.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTipoNota");
				relNITipo.setUriDomain("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + idetit + "_" + numni);
				relNITipo.setUriRange("http://dkm.fbk.eu/georeporter#"
						+ listTitolarita.get(j).getNotaIniziale().getValori().get("tiponota"));
				listRelNI.add(relNITipo);
				rigaTNI.setListarelazioni(listRelNI);
			}

			// NOTA FINALE
			RigaTabella rigaTNF = new RigaTabella();
			rigaTNF.setNometabella("http://dkm.fbk.eu/georeporter#" + dataNote.getIdTabella().getMapping());
			rigaTNF.setListaattributi(listNoteF);
			String numnf = listTitolarita.get(j).getNotaIniziale().getValori().get("numeronota");
			rigaTNF.setUririga("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + idetit + "_" + numnf);

			// creare relazione per la nota finale con tipo nota
			if (!listTitolarita.get(j).getNotaFinale().getValori().get("progressivonota").isEmpty()) {
				List<Relazione> listRelNF = new ArrayList<Relazione>();
				Relazione relNFTipo = new Relazione();
				relNFTipo.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTipoNota");
				relNFTipo.setUriDomain("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + idetit + "_" + numnf);
				relNFTipo.setUriRange("http://dkm.fbk.eu/georeporter#"
						+ listTitolarita.get(j).getNotaFinale().getValori().get("tiponota"));
				listRelNF.add(relNFTipo);
				rigaTNF.setListarelazioni(listRelNF);
			}

			// inserisco NotaIniziale e Finale resisto per entrambe URI e iserisco la
			// relazione nella sua riga TIT
			if (!listTitolarita.get(j).getNotaIniziale().getValori().get("progressivonota").isEmpty()) {
				String relNIuri = insertRigaReturn(rigaTNI);
				Relazione relNI = new Relazione();
				relNI.setNomerelazione("http://dkm.fbk.eu/georeporter#hasNotaIniziale");
				relNI.setUriDomain("http://dkm.fbk.eu/georeporter#TIT_" + codamm + "_" + idetit);
				relNI.setUriRange(relNIuri);
				listRelTIT.add(relNI);
			}

			if (!listTitolarita.get(j).getNotaFinale().getValori().get("progressivonota").isEmpty()) {
				String relNFuri = insertRigaReturn(rigaTNF);
				Relazione relNF = new Relazione();
				relNF.setNomerelazione("http://dkm.fbk.eu/georeporter#hasNotaFinale");
				relNF.setUriDomain("http://dkm.fbk.eu/georeporter#TIT_" + codamm + "_" + idetit);
				relNF.setUriRange(relNFuri);
				listRelTIT.add(relNF);
			}

			// creare relazione per il IDCatastale
			Relazione relTitIdCat = new Relazione();
			relTitIdCat.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIDCatastale");
			relTitIdCat.setUriDomain("http://dkm.fbk.eu/georeporter#TIT_" + codamm + "_" + idetit);
			String codammideimm = "UI_" + codamm + "_" + ideimm;
			String idecat = getICfromUI(codammideimm);
			if(idecat.equals("FAIL")) {
				idecat = "http://dkm.fbk.eu/georeporter#C0_N0_D0_S0";
			}
			relTitIdCat.setUriRange(idecat);
			listRelTIT.add(relTitIdCat);

			// setto relazioni
			rigaTTIT.setListarelazioni(listRelTIT);
			// inserisco l'elemento di RIGATABELLA TIT dopo aver inserito NOTE e creato le
			// relazioni
			insertRiga(rigaTTIT);

		}
	}

	// metodo per l'inserimento dell'elemento pronto dopo il mapping
	public static String insertRigaReturn(RigaTabella riga) {

		String targetURL = "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";

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

		String targetURL = "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";

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

	public static void main(String[] args) {

		String pathT = "file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.TIT";
		String pathP = "file/TN_header/headerfiletit.csv";

		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		// dal file TIT
		WrapperTit.estrazioneHeaderFileTit(pathP);
		WrapperTit.letturaFileTit(pathT);
		// mapping e insert degli elementi TITOLARITA
		LoadFile(new File("file/file_mapping/mappingTitolarita.json"), new File("file/file_mapping/mappingNota2.json"));

	}

}
