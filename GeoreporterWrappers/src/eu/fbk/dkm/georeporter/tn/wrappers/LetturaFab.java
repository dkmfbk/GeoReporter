package eu.fbk.dkm.georeporter.tn.wrappers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Indirizzi;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Riserve;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;

public class LetturaFab {

	// lista degli "header" inseriti dal file esterno HEADER FILE FAB, 6 liste
	// diverse, una per ogni TIPO RECORD
	public static List<String[]> header = new ArrayList<String[]>();
	// liste che raccolgono tutti gli elementi del file FAB divisi per TIPO
	public static List<UnitaImmobiliare> listUnitaImmobiliari = new ArrayList<UnitaImmobiliare>();
	public static List<IdentificativiCatastali> listIdentificativiCatastali = new ArrayList<IdentificativiCatastali>();
	public static List<Indirizzi> listIndirizzi = new ArrayList<Indirizzi>();
	public static List<Comuni> listComuni = new ArrayList<Comuni>();
	public static List<Riserve> listRiserve = new ArrayList<Riserve>();
	public static List<Annotazioni> listAnnotazioni = new ArrayList<Annotazioni>();

	// public static List<Annotazioni> listRigaTabella = new
	// ArrayList<Annotazioni>();
	// public static List<Nota> ListaNote = new ArrayList<Nota>();

	// metodo che analizza il file (che deve esser caricato) degli HEADER e crea la
	// lista dei 6 header
	public static void estrazioneHeaderFileFab(String pathP) {
		String[] tmp = new String[99];

		try {
			File file = new File(pathP);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();

			while (rigaCorrente != null) {

				tmp = rigaCorrente.split(";", -1);
				header.add(tmp);
				rigaCorrente = reader.readLine();

			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void letturaFileFab(String pathP) {

		try {

			File file = new File(pathP);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();

			// ciclo tutti il file, riga per riga fino a fine file
			while (rigaCorrente != null) {
				Map<String, String> campi = new HashMap<String, String>();
				Map<String, String> valoriChiave = new HashMap<String, String>();
				List<Map<String, String>> listaValoriChiave = new ArrayList<Map<String, String>>();
				Map<String, String> notaIniziale = new HashMap<String, String>();
				Map<String, String> notaFinale = new HashMap<String, String>();

				// controllo il campo 6 del file per capire a che categoria corrisponde la riga
				// che sto leggendo
				String[] tmpRiga = rigaCorrente.split("\\|", -1);
				int indice = Integer.parseInt(tmpRiga[5]);

				// se tipo = 1
				if (indice == 1) {
					int var;
					if (((tmpRiga.length) - 1) == 47) {
						var = 0;
					} else {
						var = +1;
					}
					for (int i = 0; i < 6; i++) {
						valoriChiave.put(header.get(indice - 1)[i].toLowerCase(), tmpRiga[i]);
						listaValoriChiave.add(valoriChiave);
					}
					for (int i = 6; i < 13; i++) {
						campi.put(header.get(indice - 1)[i].toLowerCase(), tmpRiga[i]);
					}
					for (int i = 13; i < 31; i++) {
						campi.put(header.get(indice - 1)[i + var].toLowerCase(), tmpRiga[i]);
					}
					for (int i = 31; i < 37; i++) {
						notaIniziale.put(header.get(indice - 1)[i + var].toLowerCase(), tmpRiga[i]);
					}
					for (int i = 37; i < 43; i++) {
						notaFinale.put(header.get(indice - 1)[i + var].toLowerCase(), tmpRiga[i]);
					}
					for (int i = 43; i < 47; i++) {
						campi.put(header.get(indice - 1)[i + var].toLowerCase(), tmpRiga[i]);
					}

					UnitaImmobiliare ui = new UnitaImmobiliare();
					Nota ni = new Nota();
					Nota nf = new Nota();

					ui.setValori(campi);
					ui.setListaValoriChiave(listaValoriChiave);
					ni.setValori(notaIniziale);
					nf.setValori(notaFinale);

					ui.setNotaIniziale(ni);
					ui.setNotaFinale(nf);

					listUnitaImmobiliari.add(ui);

				} else {
					// se != da tipo 1 devo controllare se all'interno della riga che sto leggendo
					// ho delle ripetizioni (esclusi i primi 6 campi chiave)
					// quindi lunghezza riga + grande del numero degli header
					if (((tmpRiga.length)) == (header.get(indice - 1).length)) {
						for (int i = 0; i < header.get(indice - 1).length; i++) {
							campi.put(header.get(indice - 1)[i].toLowerCase(), tmpRiga[i]);
						}
						settareElemento(indice, campi);
					} else {
						int rip = ((tmpRiga.length) - 7) / ((header.get(indice - 1).length) - 7);
						for (int j = 0; j < rip; j++) {
							Map<String, String> campi2 = new HashMap<String, String>();
							for (int i = 0; i < 6; i++) {
								campi2.put(header.get(indice - 1)[i].toLowerCase(), tmpRiga[i]);
							}
							for (int k = 6; k < ((header.get(indice - 1).length)); k++) {
								campi2.put(header.get(indice - 1)[k].toLowerCase(),
										tmpRiga[k + (((header.get(indice - 1).length) - 7) * j)]);
							}
							settareElemento(indice, campi2);
						}
					}

				}

				rigaCorrente = reader.readLine();
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// metodo con cui in base al tipo di elemento che ho appena estratto lo aggiungo
	// alla relativa lista di elementi dello stesso tipo
	public static void settareElemento(int indice, Map<String, String> campi) {

		switch (indice) {
		case 1:
			// UnitaImmobiliare ui = new UnitaImmobiliare(); ui.setValori(campi);
			// listUnitaImmobiliari.add(ui);
			break;
		case 2:
			IdentificativiCatastali ic = new IdentificativiCatastali();
			ic.setValori(campi);
			listIdentificativiCatastali.add(ic);
			break;
		case 3:
			Indirizzi ii = new Indirizzi();
			ii.setValori(campi);
			listIndirizzi.add(ii);
			break;
		case 4:
			Comuni cc = new Comuni();
			cc.setValori(campi);
			listComuni.add(cc);
			break;
		case 5:
			Riserve rr = new Riserve();
			rr.setValori(campi);
			listRiserve.add(rr);
			break;
		case 6:
			Annotazioni aa = new Annotazioni();
			aa.setValori(campi);
			listAnnotazioni.add(aa);
			break;
		default:
			;
			break;
		}

	}

	// metodo che acquisisce i 2 file di tipo json
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

	// accoppiamento valore dell'UI a quello di mapping
	public static void associazioneMappingNomeVal(MappingTabella data, MappingTabella dataNote) {
		// ciclo la lista degli elementi UI
		for (int j = 0; j < listUnitaImmobiliari.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
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

				if (listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get(parts[1]) != null) {
					tmp.setValore(listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp);
				}

				if (listUnitaImmobiliari.get(j).getValori().get(parts[1]) != null) {
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

				// tmpNI.setIdDomain(dataNote.getIdTabella().getMapping());
				tmpNI.setNome(dataNote.getAttributi().get(k).getNome());
				tmpNI.setMapping(dataNote.getAttributi().get(k).getMapping());
				tmpNI.setTipo(dataNote.getAttributi().get(k).getTipo());

				// tmpNF.setIdDomain(dataNote.getIdTabella().getMapping());
				tmpNF.setNome(dataNote.getAttributi().get(k).getNome());
				tmpNF.setMapping(dataNote.getAttributi().get(k).getMapping());
				tmpNF.setTipo(dataNote.getAttributi().get(k).getTipo());

				if (listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(parts[1]) != null) {
					tmpNI.setValore(listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(parts[1]));
					listNoteI.add(tmpNI);
				}
				if (listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(parts[1]) != null) {
					tmpNF.setValore(listUnitaImmobiliari.get(j).getNotaFinale().getValori().get(parts[1]));
					listNoteF.add(tmpNF);
				}

			}
			// riga di tipo RIGATABELLA per UI
			RigaTabella rigaTUI = new RigaTabella();
			rigaTUI.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTUI.setListaattributi(listAttributi);
			rigaTUI.setListachiave(listChiavi);
			String ideamm = listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get("identificativoimmobile");
			String codamm = listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get("codiceamministrativo");
			rigaTUI.setUririga(codamm + ideamm);

			// riga di tipo RIGATABELLA per NI e NF
			Attributo tmpPN = new Attributo();
			/*
			 * tmpPN.setValore(listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get
			 * ("progressivonota")); List<Attributo> listChiaviNP = listChiavi;
			 * tmpPN.setNome(); tmpPN.setMapping(); tmpPN.setTipo();
			 * listChiaviNP.add(tmpPN);
			 */

			RigaTabella rigaTNI = new RigaTabella();
			rigaTNI.setNometabella("http://dkm.fbk.eu/georeporter#" + dataNote.getIdTabella().getMapping());
			rigaTNI.setListaattributi(listNoteI);
			rigaTNI.setListachiave(listChiavi);
			String progni = listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get("progressivonota");
			rigaTNI.setUririga("http://dkm.fbk.eu/georeporter#" + codamm + ideamm + progni);

			RigaTabella rigaTNF = new RigaTabella();
			rigaTNF.setNometabella("http://dkm.fbk.eu/georeporter#" + dataNote.getIdTabella().getMapping());
			rigaTNF.setListaattributi(listNoteF);
			rigaTNF.setListachiave(listChiavi);
			String prognf = listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get("progressivonota");
			rigaTNF.setUririga("http://dkm.fbk.eu/georeporter#" + codamm + ideamm + prognf);

			insertRigaNota(rigaTUI);
			insertRigaNota(rigaTNI);
			insertRigaNota(rigaTNF);
		}

	}

	// metodo per l'inserimento dell'elemento pronto dopo il mapping
	public static void insertRigaNota(RigaTabella riga) {

		String targetURL = "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";

		RigaTabella rec = new RigaTabella();
		rec.setNometabella("http://dkm.fbk.eu/georeporter#UnitaImmobiliare");
		rec.setUririga("http://dkm.fbk.eu/georeporter#aaaaaaa0001234tizsllkg");

		List<Attributo> listachiave = new ArrayList<Attributo>();

		Attributo attr1 = new Attributo();
		attr1.setNome("http://dkm.fbk.eu/georeporter#identificativoimmobile");
		attr1.setMapping("http://dkm.fbk.eu/georeporter#identificativoimmobile");
		attr1.setValore("11111111.23");
		attr1.setTipo("http://www.w3.org/2001/XMLSchema#float");

		Attributo attr2 = new Attributo();
		attr2.setNome("http://dkm.fbk.eu/georeporter#codiceamministrativo");
		attr2.setMapping("http://dkm.fbk.eu/georeporter#codiceamministrativo");

		attr2.setValore("L322");
		attr2.setTipo("http://www.w3.org/2001/XMLSchema#string");

		listachiave.add(attr1);
		listachiave.add(attr2);

		rec.setListachiave(listachiave);

		Gson gson = new Gson();
		String json = gson.toJson(rec);
		System.out.println(json);

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
			System.out.println("Output from Server:\n");
			while ((output = responseBuffer.readLine()) != null) {
				System.out.println(output);
			}

			httpConnection.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

	}

	// 6 metodi per stampare in 6 file differenti di tipo csv tutti gli elementi
	// acquisiti e divisi del file generale di tipo .FAB
	/*
	 * public static void stampaFileFab1() { try { FileWriter writer = new
	 * FileWriter("/home/ameneghini/Desktop/TN_stampa/unitaImmobiliari.csv", false);
	 * 
	 * int var = 0; // if ((() - 1) == 46) { // var = -1; // }
	 * 
	 * for (int k = 0; k < header.get(0).length; k++) {
	 * writer.append(header.get(0)[k]); writer.append(';'); } writer.append('\n');
	 * 
	 * for (int j = 0; j < listUnitaImmobiliari.size(); j++) { for (int i = 0; i <
	 * header.get(0).length; i++) { String key = header.get(0)[i].toLowerCase();
	 * 
	 * if (i < 6) {
	 * writer.append(listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get(
	 * key)); writer.append(';'); } else if ((i > 31 + var) && (i < 37 + var)) {
	 * 
	 * } else if ((i > 37 + var) && (i < 42 + var)) {
	 * 
	 * } else { writer.append(listUnitaImmobiliari.get(j).getValori().get(key));
	 * writer.append(';'); }
	 * 
	 * } writer.append('\n'); }
	 * 
	 * writer.flush(); writer.close(); } catch (IOException e) { } }
	 * 
	 * public static void stampaFileFab2() { try { FileWriter writer = new
	 * FileWriter("/home/ameneghini/Desktop/TN_stampa/identificativiCatastali.csv",
	 * false);
	 * 
	 * for (int k = 0; k < header.get(1).length; k++) {
	 * writer.append(header.get(1)[k]); writer.append(';'); } writer.append('\n');
	 * 
	 * for (int j = 0; j < listIdentificativiCatastali.size(); j++) { for (int i =
	 * 0; i < header.get(1).length; i++) { String key =
	 * header.get(1)[i].toLowerCase();
	 * 
	 * writer.append(listIdentificativiCatastali.get(j).getValori().get(key));
	 * writer.append(';'); } writer.append('\n'); }
	 * 
	 * writer.flush(); writer.close(); } catch (IOException e) { } }
	 * 
	 * public static void stampaFileFab3() { try { FileWriter writer = new
	 * FileWriter("/home/ameneghini/Desktop/TN_stampa/indirizzi.csv", false);
	 * 
	 * for (int k = 0; k < header.get(2).length; k++) {
	 * writer.append(header.get(2)[k]); writer.append(';'); } writer.append('\n');
	 * 
	 * for (int j = 0; j < listIndirizzi.size(); j++) { for (int i = 0; i <
	 * header.get(2).length; i++) { String key = header.get(2)[i].toLowerCase();
	 * 
	 * writer.append(listIndirizzi.get(j).getValori().get(key)); writer.append(';');
	 * } writer.append('\n'); }
	 * 
	 * writer.flush(); writer.close(); } catch (IOException e) { } }
	 * 
	 * public static void stampaFileFab4() { try { FileWriter writer = new
	 * FileWriter("/home/ameneghini/Desktop/TN_stampa/comuni.csv", false);
	 * 
	 * for (int k = 0; k < header.get(3).length; k++) {
	 * writer.append(header.get(3)[k]); writer.append(';'); } writer.append('\n');
	 * 
	 * for (int j = 0; j < listComuni.size(); j++) { for (int i = 0; i <
	 * header.get(3).length; i++) { String key = header.get(3)[i].toLowerCase();
	 * 
	 * writer.append(listComuni.get(j).getValori().get(key)); writer.append(';'); }
	 * writer.append('\n'); }
	 * 
	 * writer.flush(); writer.close(); } catch (IOException e) { } }
	 * 
	 * public static void stampaFileFab5() { try { FileWriter writer = new
	 * FileWriter("/home/ameneghini/Desktop/TN_stampa/riserve.csv", false);
	 * 
	 * for (int k = 0; k < header.get(4).length; k++) {
	 * writer.append(header.get(4)[k]); writer.append(';'); } writer.append('\n');
	 * 
	 * for (int j = 0; j < listRiserve.size(); j++) { for (int i = 0; i <
	 * header.get(4).length; i++) { String key = header.get(4)[i].toLowerCase();
	 * 
	 * writer.append(listRiserve.get(j).getValori().get(key)); writer.append(';'); }
	 * writer.append('\n'); }
	 * 
	 * writer.flush(); writer.close(); } catch (IOException e) { } }
	 * 
	 * public static void stampaFileFab6() { try { FileWriter writer = new
	 * FileWriter("/home/ameneghini/Desktop/TN_stampa/annotazioni.csv", false);
	 * 
	 * for (int k = 0; k < header.get(5).length; k++) {
	 * writer.append(header.get(5)[k]); writer.append(';'); } writer.append('\n');
	 * 
	 * for (int j = 0; j < listAnnotazioni.size(); j++) { for (int i = 0; i <
	 * header.get(5).length; i++) { String key = header.get(5)[i].toLowerCase();
	 * 
	 * writer.append(listAnnotazioni.get(j).getValori().get(key));
	 * writer.append(';'); } writer.append('\n'); }
	 * 
	 * writer.flush(); writer.close(); } catch (IOException e) { } }
	 */

	public static void main(String[] args) {

		// path del file .FAB e del file con gli HEADER inseriti a mano da un utente
		// String pathF =
		// "/home/ameneghini/Desktop/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB";
		String pathF = "/home/ameneghini/Desktop/TN_file/prova.FAB";
		String pathP = "/home/ameneghini/Desktop/TN_header/filefab.csv";

		// chiamata per l'estrazione degli header per la composizione della lista HEADER
		estrazioneHeaderFileFab(pathP);

		// chiamata per l'analisi del file .FAB
		letturaFileFab(pathF);

		// chiamata al metodo che accoppia ELEMENTO appena acquisito al NOME che serve
		// per l'inserimento
		// questo grazie ai file di mapping
		LoadFile(new File("/home/ameneghini/Desktop/mappingUI.json"),
				new File("/home/ameneghini/Desktop/mappingNota.json"));

		// lista chiamate per la stampa dei 6 file divisi
		/*
		 * stampaFileFab1(); stampaFileFab2(); stampaFileFab3(); stampaFileFab4();
		 * stampaFileFab5(); stampaFileFab6();
		 */
	}

}
