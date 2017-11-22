package eu.fbk.dkm.georeporter.tn.wrappers;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Titolarita;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;

public class LetturaTit {

	public static String[] headerTit;

	public static List<Titolarita> listTitolarita = new ArrayList<Titolarita>();

	public static void estrazioneHeaderFileTit(String pathP) {

		try {
			File file = new File(pathP);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();

			if (rigaCorrente != null) {
				headerTit = rigaCorrente.split(";", -1);
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void letturaFileTit(String pathP) {

		try {
			File file = new File(pathP);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();

			while (rigaCorrente != null) {
				Map<String, String> campi = new HashMap<String, String>();
				Map<String, String> valoriChiave = new HashMap<String, String>();
				List<Map<String, String>> listaValoriChiave = new ArrayList<Map<String, String>>();
				Map<String, String> notaIniziale = new HashMap<String, String>();
				Map<String, String> notaFinale = new HashMap<String, String>();

				String[] tmpRiga = rigaCorrente.split("\\|", -1);

				int var = 0;
				if (((tmpRiga.length) - 1) != 35) {
					var = 6;
				}
				for (int i = 0; i < 6; i++) {
					valoriChiave.put(headerTit[i].toLowerCase(), tmpRiga[i]);
					listaValoriChiave.add(valoriChiave);
				}
				for (int i = 6; i < 19 - var; i++) {
					campi.put(headerTit[i + var].toLowerCase(), tmpRiga[i]);
				}
				for (int i = 19 - var; i < 25 - var; i++) {
					notaIniziale.put(headerTit[i + var].toLowerCase(), tmpRiga[i]);
				}
				campi.put(headerTit[25].toLowerCase(), tmpRiga[25]);
				for (int i = 26 - var; i < 31 - var; i++) {
					notaFinale.put(headerTit[i + var].toLowerCase(), tmpRiga[i]);
				}
				for (int i = 32 - var; i < 35 - var; i++) {
					campi.put(headerTit[i + var].toLowerCase(), tmpRiga[i]);
				}

				Titolarita tt = new Titolarita();
				Nota ni = new Nota();
				Nota nf = new Nota();

				tt.setValori(campi);
				tt.setListaValoriChiave(listaValoriChiave);
				ni.setValori(notaIniziale);
				nf.setValori(notaFinale);

				tt.setNotaIniziale(ni);
				tt.setNotaFinale(nf);

				listTitolarita.add(tt);

				rigaCorrente = reader.readLine();
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void LoadFile(File filename, File filenameNote) {

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

				if (listTitolarita.get(j).getNotaIniziale().getValori().get(parts[1]) != null) {
					tmpNI.setValore(listTitolarita.get(j).getNotaIniziale().getValori().get(parts[1]));
					listNoteI.add(tmpNI);
				}

				if (listTitolarita.get(j).getNotaFinale().getValori().get(parts[1]) != null) {
					tmpNF.setValore(listTitolarita.get(j).getNotaFinale().getValori().get(parts[1]));
					listNoteF.add(tmpNF);
				}

			}
			// riga di tipo RIGATABELLA per TIT
			RigaTabella rigaTTIT = new RigaTabella();

			rigaTTIT.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTTIT.setListaattributi(listAttributi);
			rigaTTIT.setListachiave(listChiavi);
			String codamm = listTitolarita.get(j).getListaValoriChiave().get(0).get("codiceamministrativo");
			String ideimm = listTitolarita.get(j).getListaValoriChiave().get(0).get("identificativoimmobile");
			rigaTTIT.setUririga("http://dkm.fbk.eu/georeporter#TIT_" + codamm + "_" + ideimm);

			RigaTabella rigaTNI = new RigaTabella();
			rigaTNI.setNometabella("http://dkm.fbk.eu/georeporter#" + dataNote.getIdTabella().getMapping());
			rigaTNI.setListaattributi(listNoteI);
			String numni = listTitolarita.get(j).getNotaIniziale().getValori().get("numeronota");
			rigaTNI.setUririga("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numni);

			RigaTabella rigaTNF = new RigaTabella();
			rigaTNF.setNometabella("http://dkm.fbk.eu/georeporter#" + dataNote.getIdTabella().getMapping());
			rigaTNF.setListaattributi(listNoteF);
			String numnf = listTitolarita.get(j).getNotaIniziale().getValori().get("numeronota");
			rigaTNF.setUririga("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numnf);

			// inserisco NotaIniziale e Finale resisto per entrambe URI e iserisco la
			// relazione nella sua riga TIT
			List<Relazione> listRelNota = new ArrayList<Relazione>();
			if (!listTitolarita.get(j).getNotaIniziale().getValori().get("progressivonota").isEmpty()) {
				String relNIuri = insertRigaReturn(rigaTNI);
				Relazione relNI = new Relazione();
				relNI.setNomerelazione("http://dkm.fbk.eu/georeporter#hasNotaIniziale");
				relNI.setUriDomain("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numni);
				relNI.setUriRange(relNIuri);
				listRelNota.add(relNI);
			}

			if (!listTitolarita.get(j).getNotaFinale().getValori().get("progressivonota").isEmpty()) {
				String relNFuri = insertRigaReturn(rigaTNF);
				Relazione relNF = new Relazione();
				relNF.setNomerelazione("http://dkm.fbk.eu/georeporter#hasNotaFinale");
				relNF.setUriDomain("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numnf);
				relNF.setUriRange(relNFuri);
				listRelNota.add(relNF);
			}
			rigaTTIT.setListarelazioni(listRelNota);
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

		String pathT = "file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.TIT";
		String pathP = "file/TN_header/headerfiletit.csv";

		estrazioneHeaderFileTit(pathP);

		letturaFileTit(pathT);

		LoadFile(new File("file/file_mapping/mappingTitolarita.json"), new File("file/file_mapping/mappingNota2.json"));

	}

}
