package eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sun.jersey.api.client.WebResource;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Annotazioni;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Comuni;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.IdentificativiCatastali;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Indirizzo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Riserve;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.tn.wrappers.ControlloValore;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperFab;

public class MappingInsertFabUiNote {

	public static List<UnitaImmobiliare> listUnitaImmobiliari = WrapperFab.listUnitaImmobiliari;

	// metodo che acquisisce i 2 file di tipo json
	public static void LoadFile(File filename, File filenameNote, File filenameI) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gsonNote = new Gson();
		JsonReader readerNote;

		Gson gsonI = new Gson();
		JsonReader readerI;

		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			readerNote = new JsonReader(new FileReader(filenameNote));
			MappingTabella dataNote = gsonNote.fromJson(readerNote, MappingTabella.class);

			readerI = new JsonReader(new FileReader(filenameI));
			MappingTabella dataI = gsonNote.fromJson(readerI, MappingTabella.class);
			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(data, dataNote, dataI);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// accoppiamento valore dell'UI a quello di mapping
	public static void associazioneMappingNomeVal(MappingTabella data, MappingTabella dataNote, MappingTabella dataI) {
		// ciclo la lista degli elementi UI
		for (int j = 0; j < listUnitaImmobiliari.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listAttributiI = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			List<Attributo> listNoteI = new ArrayList<Attributo>();
			List<Attributo> listNoteF = new ArrayList<Attributo>();
			// ciclo per crea la listaCHIAVI e listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				// tmp.setIdDomain(data.getIdTabella().getMapping());
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get(parts[1])
								.isEmpty() == false)) {
					tmp.setValore(listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp);
				}

				if ((listUnitaImmobiliari.get(j).getValori().get(parts[1]) != null)
						&& (listUnitaImmobiliari.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listUnitaImmobiliari.get(j).getValori().get(parts[1]));
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

				if ((listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(parts[1]) != null)
						&& (listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(parts[1])
								.isEmpty() == false)) {
					// controllo tipo nota
					// controllo data e sistemare in formato
					if ((parts[1].equals("datadiefficacia")) || (parts[1].equals("datadiregistrazioneinatti"))) {
						tmpNI.setValore(ControlloValore
								.cambioData(listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(parts[1])));
					} else {
						tmpNI.setValore(listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(parts[1]));
					}
					listNoteI.add(tmpNI);
				}
				if ((listUnitaImmobiliari.get(j).getNotaFinale().getValori().get(parts[1]) != null)
						&& (listUnitaImmobiliari.get(j).getNotaFinale().getValori().get(parts[1]).isEmpty() == false)) {
					// controllo tipo nota

					// controllo data e sistemare in formato
					if ((parts[1].equals("datadiefficacia")) || (parts[1].equals("datadiregistrazioneinatti"))) {
						tmpNF.setValore(ControlloValore
								.cambioData(listUnitaImmobiliari.get(j).getNotaFinale().getValori().get(parts[1])));
					} else {
						tmpNF.setValore(listUnitaImmobiliari.get(j).getNotaFinale().getValori().get(parts[1]));
					}
					listNoteF.add(tmpNF);
				}

			}
			// riga di tipo RIGATABELLA per UI
			RigaTabella rigaTUI = new RigaTabella();
			rigaTUI.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTUI.setListaattributi(listAttributi);
			rigaTUI.setListachiave(listChiavi);
			String codamm = listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get("codiceamministrativo");
			String ideimm = listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get("identificativoimmobile");
			rigaTUI.setUririga("http://dkm.fbk.eu/georeporter#UI_" + codamm + "_" + ideimm);

			// riga di tipo RIGATABELLA per IND
			RigaTabella rigaTIND = new RigaTabella();
			rigaTIND.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			// chiamata funzione gps lat long
			//?? ha senso inserirlo ??
			rigaTIND.setListaattributi(listAttributiI);
			// creo l'indirizzo univoco grazie dalla data d'inserimento
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			long time = cal.getTimeInMillis();
			rigaTIND.setUririga("http://dkm.fbk.eu/georeporter#IND_" + time);

			insertRiga(rigaTIND);

			// NOTA INIZIALE
			RigaTabella rigaTNI = new RigaTabella();
			rigaTNI.setNometabella("http://dkm.fbk.eu/georeporter#" + dataNote.getIdTabella().getMapping());
			rigaTNI.setListaattributi(listNoteI);
			String numni = listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get("numeronota");
			rigaTNI.setUririga("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numni);

			// creare relazione per la nota iniziale con tipo nota
			List<Relazione> listRelNI = new ArrayList<Relazione>();
			Relazione relNITipo = new Relazione();
			relNITipo.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTipoNota");
			relNITipo.setUriDomain("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numni);
			relNITipo.setUriRange("http://dkm.fbk.eu/georeporter#"
					+ listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get("tiponota"));
			listRelNI.add(relNITipo);
			rigaTNI.setListarelazioni(listRelNI);

			// NOTA FINALE
			RigaTabella rigaTNF = new RigaTabella();
			rigaTNF.setNometabella("http://dkm.fbk.eu/georeporter#" + dataNote.getIdTabella().getMapping());
			rigaTNF.setListaattributi(listNoteF);
			String numnf = listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get("numeronota");
			rigaTNF.setUririga("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numnf);

			// creare relazione per la nota finale con tipo nota
			List<Relazione> listRelNF = new ArrayList<Relazione>();
			Relazione relNFTipo = new Relazione();
			relNFTipo.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTipoNota");
			relNFTipo.setUriDomain("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numnf);
			relNFTipo.setUriRange("http://dkm.fbk.eu/georeporter#"
					+ listUnitaImmobiliari.get(j).getNotaFinale().getValori().get("tiponota"));
			listRelNF.add(relNFTipo);
			rigaTNF.setListarelazioni(listRelNF);

			// inserisco NotaIniziale e Finale resisto per entrambe URI e iserisco la
			// relazione nella sua riga UI
			List<Relazione> listRelUI = new ArrayList<Relazione>();
			if (!listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get("progressivonota").isEmpty()) {
				String relNIuri = insertRigaReturn(rigaTNI);
				Relazione relNI = new Relazione();
				relNI.setNomerelazione("http://dkm.fbk.eu/georeporter#hasNotaIniziale");
				relNI.setUriDomain("http://dkm.fbk.eu/georeporter#UI_" + codamm + "_" + ideimm);
				relNI.setUriRange(relNIuri);
				listRelUI.add(relNI);
			}

			if (!listUnitaImmobiliari.get(j).getNotaFinale().getValori().get("progressivonota").isEmpty()) {
				String relNFuri = insertRigaReturn(rigaTNF);
				Relazione relNF = new Relazione();
				relNF.setNomerelazione("http://dkm.fbk.eu/georeporter#hasNotaFinale");
				relNF.setUriDomain("http://dkm.fbk.eu/georeporter#UI_" + codamm + "_" + ideimm);
				relNF.setUriRange(relNFuri);
				listRelUI.add(relNF);
			}
			// rel cin ind
			Relazione relIND = new Relazione();
			relIND.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIndirizzo");
			relIND.setUriDomain("http://dkm.fbk.eu/georeporter#UI_" + codamm + "_" + ideimm);
			relIND.setUriRange("http://dkm.fbk.eu/georeporter#IND_" + time);
			listRelUI.add(relIND);

			// riga uri rel + inserimento
			rigaTUI.setListarelazioni(listRelUI);
			insertRiga(rigaTUI);
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

	public static void main(String[] args) {

	}

}