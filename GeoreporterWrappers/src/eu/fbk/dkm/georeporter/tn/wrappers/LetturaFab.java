package eu.fbk.dkm.georeporter.tn.wrappers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Annotazioni;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Comuni;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.IdentificativiCatastali;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Indirizzi;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Riserve;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;

public class LetturaFab {

	public static List<String[]> header = new ArrayList<String[]>();

	public static List<UnitaImmobiliare> listUnitaImmobiliari = new ArrayList<UnitaImmobiliare>();
	public static List<IdentificativiCatastali> listIdentificativiCatastali = new ArrayList<IdentificativiCatastali>();
	public static List<Indirizzi> listIndirizzi = new ArrayList<Indirizzi>();
	public static List<Comuni> listComuni = new ArrayList<Comuni>();
	public static List<Riserve> listRiserve = new ArrayList<Riserve>();
	public static List<Annotazioni> listAnnotazioni = new ArrayList<Annotazioni>();
	
	public static List<Annotazioni> listRigaTabella = new ArrayList<Annotazioni>();

	public static List<Nota> ListaNote = new ArrayList<Nota>();

	public static List<RigaTabella<Relazione>> listRigaTabUI = new ArrayList<RigaTabella<Relazione>>();
	public static List<RigaTabella<Relazione>> listRigaTabNote = new ArrayList<RigaTabella<Relazione>>();

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

			while (rigaCorrente != null) {
				Map<String, String> campi = new HashMap<String, String>();

				Map<String, String> valoriChiave = new HashMap<String, String>();
				List<Map<String, String>> listaValoriChiave = new ArrayList<Map<String, String>>();

				Map<String, String> notaIniziale = new HashMap<String, String>();

				Map<String, String> notaFinale = new HashMap<String, String>();

				String[] tmpRiga = rigaCorrente.split("\\|", -1);
				int indice = Integer.parseInt(tmpRiga[5]);

				if (indice == 1) {

					int var = 0;
					if (((tmpRiga.length) - 1) == 46) {
						var = -1;
					}

					for (int i = 0; i < 6; i++) {
						valoriChiave.put(header.get(indice - 1)[i].toLowerCase(), tmpRiga[i]);
					}
					listaValoriChiave.add(valoriChiave);

					for (int i = 6; i < 13; i++) {
						campi.put(header.get(indice - 1)[i].toLowerCase(), tmpRiga[i]);
					}
					for (int i = 13; i < 31 + var; i++) {
						campi.put(header.get(indice - 1)[i].toLowerCase(), tmpRiga[i]);
					}

					for (int i = 31 + var; i < 37 + var; i++) {
						notaIniziale.put(header.get(indice - 1)[i].toLowerCase(), tmpRiga[i]);
					}
					for (int i = 37 + var; i < 42 + var; i++) {
						notaFinale.put(header.get(indice - 1)[i].toLowerCase(), tmpRiga[i]);
					}

					for (int i = 43 + var; i < 47 + var; i++) {
						campi.put(header.get(indice - 1)[i].toLowerCase(), tmpRiga[i]);
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

					// settareElemento(indice, campi);

				} else {

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

	public static void settareElemento(int indice, Map<String, String> campi) {

		switch (indice) {
		case 1:
			/*
			 * UnitaImmobiliare ui = new UnitaImmobiliare(); ui.setValori(campi);
			 * 
			 * listUnitaImmobiliari.add(ui);
			 */

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

			associazioneMappingNomeVal(data,dataNote);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void associazioneMappingNomeVal(MappingTabella data, MappingTabella dataNote) {

		for (int j = 0; j < listUnitaImmobiliari.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();
			
			List<Attributo> listNoteI = new ArrayList<Attributo>();
			List<Attributo> listNoteF = new ArrayList<Attributo>();

			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setIdDomain(data.getIdTabella().getMapping());
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if (listUnitaImmobiliari.get(j).getValori().get(parts[1]) != null) {
					tmp.setValore(listUnitaImmobiliari.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				} else {
					tmp.setValore(listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp);
				}
				
			}
			
			for(int k=0; k<dataNote.getAttributi().size(); k++) {
				
				String string = dataNote.getAttributi().get(k).getNome();
				String[] parts = string.split("#");

				Attributo tmpNI = new Attributo();
				Attributo tmpNF = new Attributo();
				tmpNI.setIdDomain(dataNote.getIdTabella().getMapping());
				tmpNI.setNome(dataNote.getAttributi().get(k).getNome());
				tmpNI.setMapping(dataNote.getAttributi().get(k).getMapping());
				tmpNI.setTipo(dataNote.getAttributi().get(k).getTipo());
				
				tmpNF.setIdDomain(dataNote.getIdTabella().getMapping());
				tmpNF.setNome(dataNote.getAttributi().get(k).getNome());
				tmpNF.setMapping(dataNote.getAttributi().get(k).getMapping());
				tmpNF.setTipo(dataNote.getAttributi().get(k).getTipo());
				
				if (listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(parts[1]) != null) {
					tmpNI.setValore(listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(parts[1]) );
					listNoteI.add(tmpNI);
				}
				if (listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(parts[1]) != null) {
					tmpNF.setValore(listUnitaImmobiliari.get(j).getNotaFinale().getValori().get(parts[1]) );
					listNoteF.add(tmpNF);
				}

			}
			
			RigaTabella<Relazione> rigaTUI = new RigaTabella<Relazione>();
			rigaTUI.setNometabella(data.getIdTabella().getMapping());
			rigaTUI.setListaattributi(listAttributi);
			rigaTUI.setListachiave(listChiavi);
			String ideamm = listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get("identificativoimmobile");
			String codamm = listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get("codiceamministrativo");
			rigaTUI.setUririga(codamm+ideamm);
			
			
			Attributo tmpPN = new Attributo();
			tmpPN.setValore(listUnitaImmobiliari.get(j).getNotaFinale().getValori().get("progressivonota") );
			List<Attributo> listChiaviNP = listChiavi;
			listChiaviNP.add(tmpPN);
			
			RigaTabella<Relazione> rigaTNI = new RigaTabella<Relazione>();
			rigaTNI.setNometabella(dataNote.getIdTabella().getMapping());
			rigaTNI.setListaattributi(listNoteI);
			rigaTNI.setListachiave(listChiaviNP);
			String progni = listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get("progressivonota");
			rigaTNI.setUririga(codamm+ideamm+progni);
			
			RigaTabella<Relazione> rigaTNF = new RigaTabella<Relazione>();
			rigaTNF.setNometabella(dataNote.getIdTabella().getMapping());
			rigaTNF.setListaattributi(listNoteF);
			rigaTNF.setListachiave(listChiaviNP);
			String prognf = listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get("progressivonota");
			rigaTNF.setUririga(codamm+ideamm+prognf);
			
			listRigaTabUI.add(rigaTUI);
			listRigaTabNote.add(rigaTNI);
			listRigaTabNote.add(rigaTNF);
		}
		
	}

	public static void stampaFileFab1() {
		try {
			FileWriter writer = new FileWriter("/home/ameneghini/Desktop/TN_stampa/unitaImmobiliari.csv", false);

			int var = 0;
			// if ((() - 1) == 46) {
			// var = -1;
			// }

			for (int k = 0; k < header.get(0).length; k++) {
				writer.append(header.get(0)[k]);
				writer.append(';');
			}
			writer.append('\n');

			for (int j = 0; j < listUnitaImmobiliari.size(); j++) {
				for (int i = 0; i < header.get(0).length; i++) {
					String key = header.get(0)[i].toLowerCase();

					if (i < 6) {
						writer.append(listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get(key));
						writer.append(';');
					} else if ((i > 31 + var) && (i < 37 + var)) {

					} else if ((i > 37 + var) && (i < 42 + var)) {

					} else {
						writer.append(listUnitaImmobiliari.get(j).getValori().get(key));
						writer.append(';');
					}

				}
				writer.append('\n');
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
		}
	}

	public static void stampaFileFab2() {
		try {
			FileWriter writer = new FileWriter("/home/ameneghini/Desktop/TN_stampa/identificativiCatastali.csv", false);

			for (int k = 0; k < header.get(1).length; k++) {
				writer.append(header.get(1)[k]);
				writer.append(';');
			}
			writer.append('\n');

			for (int j = 0; j < listIdentificativiCatastali.size(); j++) {
				for (int i = 0; i < header.get(1).length; i++) {
					String key = header.get(1)[i].toLowerCase();

					writer.append(listIdentificativiCatastali.get(j).getValori().get(key));
					writer.append(';');
				}
				writer.append('\n');
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
		}
	}

	public static void stampaFileFab3() {
		try {
			FileWriter writer = new FileWriter("/home/ameneghini/Desktop/TN_stampa/indirizzi.csv", false);

			for (int k = 0; k < header.get(2).length; k++) {
				writer.append(header.get(2)[k]);
				writer.append(';');
			}
			writer.append('\n');

			for (int j = 0; j < listIndirizzi.size(); j++) {
				for (int i = 0; i < header.get(2).length; i++) {
					String key = header.get(2)[i].toLowerCase();

					writer.append(listIndirizzi.get(j).getValori().get(key));
					writer.append(';');
				}
				writer.append('\n');
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
		}
	}

	public static void stampaFileFab4() {
		try {
			FileWriter writer = new FileWriter("/home/ameneghini/Desktop/TN_stampa/comuni.csv", false);

			for (int k = 0; k < header.get(3).length; k++) {
				writer.append(header.get(3)[k]);
				writer.append(';');
			}
			writer.append('\n');

			for (int j = 0; j < listComuni.size(); j++) {
				for (int i = 0; i < header.get(3).length; i++) {
					String key = header.get(3)[i].toLowerCase();

					writer.append(listComuni.get(j).getValori().get(key));
					writer.append(';');
				}
				writer.append('\n');
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
		}
	}

	public static void stampaFileFab5() {
		try {
			FileWriter writer = new FileWriter("/home/ameneghini/Desktop/TN_stampa/riserve.csv", false);

			for (int k = 0; k < header.get(4).length; k++) {
				writer.append(header.get(4)[k]);
				writer.append(';');
			}
			writer.append('\n');

			for (int j = 0; j < listRiserve.size(); j++) {
				for (int i = 0; i < header.get(4).length; i++) {
					String key = header.get(4)[i].toLowerCase();

					writer.append(listRiserve.get(j).getValori().get(key));
					writer.append(';');
				}
				writer.append('\n');
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
		}
	}

	public static void stampaFileFab6() {
		try {
			FileWriter writer = new FileWriter("/home/ameneghini/Desktop/TN_stampa/annotazioni.csv", false);

			for (int k = 0; k < header.get(5).length; k++) {
				writer.append(header.get(5)[k]);
				writer.append(';');
			}
			writer.append('\n');

			for (int j = 0; j < listAnnotazioni.size(); j++) {
				for (int i = 0; i < header.get(5).length; i++) {
					String key = header.get(5)[i].toLowerCase();

					writer.append(listAnnotazioni.get(j).getValori().get(key));
					writer.append(';');
				}
				writer.append('\n');
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
		}
	}

	public static void main(String[] args) {

		// String pathF =
		// "/home/ameneghini/Desktop/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB";
		String pathF = "/home/ameneghini/Desktop/TN_file/prova.FAB";
		String pathP = "/home/ameneghini/Desktop/TN_header/filefab.csv";

		estrazioneHeaderFileFab(pathP);
		letturaFileFab(pathF);

		LoadFile(new File("/home/ameneghini/Desktop/mappingUI.json"), new File("/home/ameneghini/Desktop/mappingNota.json"));

		// stampaFileFab1();
		// stampaFileFab2();
		// stampaFileFab3();
		// stampaFileFab4();
		// stampaFileFab5();
		// stampaFileFab6();

	}

}
