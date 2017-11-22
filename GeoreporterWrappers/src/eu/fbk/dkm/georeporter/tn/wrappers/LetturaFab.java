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
						// ciclio prima i 6 campi chiave poi il resto
						for (int i = 0; i < header.get(indice - 1).length; i++) {
							if (i < 6) {
								valoriChiave.put(header.get(indice - 1)[i].toLowerCase(), tmpRiga[i]);
								listaValoriChiave.add(valoriChiave);
							} else {
								campi.put(header.get(indice - 1)[i].toLowerCase(), tmpRiga[i]);
							}
						}
						settareElemento(indice, campi, listaValoriChiave);
					} else {
						int rip = ((tmpRiga.length) - 7) / ((header.get(indice - 1).length) - 7);
						for (int j = 0; j < rip; j++) {
							Map<String, String> campi2 = new HashMap<String, String>();
							// Map<String, String> valoriChiave2 = new HashMap<String, String>();
							for (int i = 0; i < 6; i++) {
								valoriChiave.put(header.get(indice - 1)[i].toLowerCase(), tmpRiga[i]);
								listaValoriChiave.add(valoriChiave);
							}
							for (int k = 6; k < ((header.get(indice - 1).length)); k++) {
								campi2.put(header.get(indice - 1)[k].toLowerCase(),
										tmpRiga[k + (((header.get(indice - 1).length) - 7) * j)]);
							}
							settareElemento(indice, campi2, listaValoriChiave);
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
	public static void settareElemento(int indice, Map<String, String> campi,
			List<Map<String, String>> listaValoriChiave) {

		switch (indice) {
		case 1:
			// UnitaImmobiliare ui = new UnitaImmobiliare(); ui.setValori(campi);
			// listUnitaImmobiliari.add(ui);
			break;
		case 2:
			IdentificativiCatastali ic = new IdentificativiCatastali();
			ic.setValori(campi);
			ic.setListaValoriChiave(listaValoriChiave);
			listIdentificativiCatastali.add(ic);
			break;
		case 3:
			Indirizzi ii = new Indirizzi();
			ii.setValori(campi);
			ii.setListaValoriChiave(listaValoriChiave);
			listIndirizzi.add(ii);
			break;
		case 4:
			Comuni cc = new Comuni();
			cc.setValori(campi);
			cc.setListaValoriChiave(listaValoriChiave);
			listComuni.add(cc);
			break;
		case 5:
			Riserve rr = new Riserve();
			rr.setValori(campi);
			rr.setListaValoriChiave(listaValoriChiave);
			listRiserve.add(rr);
			break;
		case 6:
			Annotazioni aa = new Annotazioni();
			aa.setValori(campi);
			aa.setListaValoriChiave(listaValoriChiave);
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
				if (listUnitaImmobiliari.get(j).getNotaFinale().getValori().get(parts[1]) != null) {
					tmpNF.setValore(listUnitaImmobiliari.get(j).getNotaFinale().getValori().get(parts[1]));
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
			rigaTUI.setUririga("http://dkm.fbk.eu/georeporterUI_#" + codamm + "_" + ideimm);

			RigaTabella rigaTNI = new RigaTabella();
			rigaTNI.setNometabella("http://dkm.fbk.eu/georeporter#" + dataNote.getIdTabella().getMapping());
			rigaTNI.setListaattributi(listNoteI);
			String numni = listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get("numeronota");
			rigaTNI.setUririga("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numni);
			
			List<Relazione> listRelNotaIUI = new ArrayList<Relazione>();
			Relazione relNIUI = new Relazione();
			relNIUI.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTipoNota");
			relNIUI.setUriDomain("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numni);
			relNIUI.setUriRange("http://dkm.fbk.eu/georeporter#UI_" + codamm + "_" + ideimm);
			listRelNotaIUI.add(relNIUI);
			
			rigaTNI.setListarelazioni(listRelNotaIUI);

			RigaTabella rigaTNF = new RigaTabella();
			rigaTNF.setNometabella("http://dkm.fbk.eu/georeporter#" + dataNote.getIdTabella().getMapping());
			rigaTNF.setListaattributi(listNoteF);
			String numnf = listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get("numeronota");
			rigaTNF.setUririga("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numnf);

			List<Relazione> listRelNotaFUI = new ArrayList<Relazione>();
			Relazione relNFUI = new Relazione();
			relNFUI.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTipoNota");
			relNFUI.setUriDomain("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numni);
			relNFUI.setUriRange("http://dkm.fbk.eu/georeporter#UI_" + codamm + "_" + ideimm);
			listRelNotaFUI.add(relNFUI);
			
			rigaTNF.setListarelazioni(listRelNotaFUI);
			
			// inserisco NotaIniziale e Finale resisto per entrambe URI e iserisco la
			// relazione nella sua riga UI
			List<Relazione> listRelNota = new ArrayList<Relazione>();
			if (!listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get("progressivonota").isEmpty()) {
				String relNIuri = insertRigaReturn(rigaTNI);
				Relazione relNI = new Relazione();
				relNI.setNomerelazione("http://dkm.fbk.eu/georeporter#hasNotaIniziale");
				//relNI.setUriDomain("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numni);
				relNI.setUriDomain("http://dkm.fbk.eu/georeporter#UI_" + codamm + "_" + ideimm);
				relNI.setUriRange(relNIuri);
				listRelNota.add(relNI);
			}

			if (!listUnitaImmobiliari.get(j).getNotaFinale().getValori().get("progressivonota").isEmpty()) {
				String relNFuri = insertRigaReturn(rigaTNF);
				Relazione relNF = new Relazione();
				relNF.setNomerelazione("http://dkm.fbk.eu/georeporter#hasNotaFinale");
				//relNF.setUriDomain("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numnf);
				relNF.setUriDomain("http://dkm.fbk.eu/georeporter#UI_" + codamm + "_" + ideimm);
				relNF.setUriRange(relNFuri);
				listRelNota.add(relNF);
			}
			rigaTUI.setListarelazioni(listRelNota);
			insertRiga(rigaTUI);
		}

	}

	// metodo per l'inserimento dell'elemento pronto dopo il mapping
	public static String insertRigaReturn(RigaTabella riga) {

		String targetURL = "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";

		Gson gson = new Gson();
		String json = gson.toJson(riga);
//		System.out.println(json);

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
				//System.out.println(output);
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
	//	System.out.println(json);

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
				//System.out.println(output);
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

	private static void LoadFileIdentificativi(File fileParticella, File fileIdeCat) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		try {
			reader = new JsonReader(new FileReader(fileParticella));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(fileIdeCat));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeValIdentificativo(data, data2);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void associazioneMappingNomeValIdentificativo(MappingTabella data, MappingTabella data2) {
		// ciclo la lista degli elementi IDE CAt

		for (int j = 0; j < listIdentificativiCatastali.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			// ciclo per crea la listaCHIAVI e listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {
				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");
				Attributo tmp = new Attributo();
				// tmp.setIdDomain(data.getIdTabella().getMapping());
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if (listIdentificativiCatastali.get(j).getListaValoriChiave().get(0).get(parts[1]) != null) {
					tmp.setValore(listIdentificativiCatastali.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp);
				}

				if (listIdentificativiCatastali.get(j).getValori().get(parts[1]) != null) {
					tmp.setValore(listIdentificativiCatastali.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}

			}
			// riga di tipo RIGATABELLA per Particella
			RigaTabella rigaTPar = new RigaTabella();
			rigaTPar.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTPar.setListaattributi(listAttributi);
			rigaTPar.setListachiave(listChiavi);
			String codamm = listIdentificativiCatastali.get(j).getListaValoriChiave().get(0)
					.get("codiceamministrativo");
			String ideimm = listIdentificativiCatastali.get(j).getListaValoriChiave().get(0)
					.get("identificativoimmobile");
			String foglio = listIdentificativiCatastali.get(j).getValori().get("foglio");
			String num = listIdentificativiCatastali.get(j).getValori().get("numero");
			String den = listIdentificativiCatastali.get(j).getValori().get("denominatore");
			String sub = listIdentificativiCatastali.get(j).getValori().get("subalterno");
			rigaTPar.setUririga(
					"http://dkm.fbk.eu/georeporter#PA_C" + codamm + "_F" + foglio + "_N" + num + "_D" + den);

			String relRange = insertRigaReturn(rigaTPar);

			// ciclo per crea la listaCHIAVI e listaATTRIBUTI richiesti dal mapping

			List<Attributo> listAttributi2 = new ArrayList<Attributo>();
			List<Attributo> listChiavi2 = new ArrayList<Attributo>();
			for (int i = 0; i < data2.getAttributi().size(); i++) {
				String string = data2.getAttributi().get(i).getNome();
				String[] parts = string.split("#");
				Attributo tmp2 = new Attributo();
				// tmp.setIdDomain(data.getIdTabella().getMapping());
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());

				if (listIdentificativiCatastali.get(j).getListaValoriChiave().get(0).get(parts[1]) != null) {
					tmp2.setValore(listIdentificativiCatastali.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi2.add(tmp2);
				}
				if (listIdentificativiCatastali.get(j).getValori().get(parts[1]) != null) {
					tmp2.setValore(listIdentificativiCatastali.get(j).getValori().get(parts[1]));
					listAttributi2.add(tmp2);
				}
			}

			// creo la relazione particella -> identificativo catastale
			List<Relazione> listRelPartIdeCat = new ArrayList<Relazione>();
			Relazione relPartIdeCat = new Relazione();
			relPartIdeCat.setNomerelazione("http://dkm.fbk.eu/georeporter#hasParticella");
			relPartIdeCat.setUriDomain(
					"http://dkm.fbk.eu/georeporter#C" + codamm + "_F" + foglio + "_N" + num + "_D" + den + "_S" + sub);
			relPartIdeCat.setUriRange(relRange);
			listRelPartIdeCat.add(relPartIdeCat);

			Relazione relUIIdeCat = new Relazione();
			relUIIdeCat.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIdentificativoCatastale");
			relUIIdeCat.setUriDomain(
					"http://dkm.fbk.eu/georeporter#C" + codamm + "_F" + foglio + "_N" + num + "_D" + den + "_S" + sub);
			relUIIdeCat.setUriRange("http://dkm.fbk.eu/georeporter#" + codamm + ideimm);
			listRelPartIdeCat.add(relUIIdeCat);

			RigaTabella rigaTIdeCat = new RigaTabella();
			rigaTIdeCat.setNometabella("http://dkm.fbk.eu/georeporter#" + data2.getIdTabella().getMapping());
			rigaTIdeCat.setListaattributi(listAttributi2);
			rigaTIdeCat.setListachiave(listChiavi2);
			rigaTIdeCat.setUririga(
					"http://dkm.fbk.eu/georeporter#C" + codamm + "_F" + foglio + "_N" + num + "_D" + den + "_S" + sub);

			rigaTIdeCat.setListarelazioni(listRelPartIdeCat);
			//controllo relazione particella ide cat
			String relRange2 = insertRigaReturn(rigaTIdeCat);
			insertRiga(rigaTIdeCat);
		}

	}

	public static void main(String[] args) {

		// path del file .FAB e del file con gli HEADER inseriti a mano da un utente
		// String pathF =IDR0000115470_TIPOFACSN_CAMML322
		String pathF = "file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB";
		String pathP = "file/TN_header/headerfilefab.csv";

		// chiamata per l'estrazione degli header per la composizione della lista HEADER
		estrazioneHeaderFileFab(pathP);

		// chiamata per l'analisi del file .FAB
		letturaFileFab(pathF);

		// chiamata al metodo che accoppia ELEMENTO appena acquisito al NOME che serve
		// per l'inserimento
		// questo grazie ai file di mapping
		LoadFile(new File("file/file_mapping/mappingUI.json"), new File("file/file_mapping/mappingNota.json"));
		LoadFileIdentificativi(new File("file/file_mapping/mappingParticella.json"),
				new File("file/file_mapping/mappingIdentificativoCatastale.json"));

	}

}
