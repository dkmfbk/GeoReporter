package eu.fbk.dkm.georeporter.ita.wrappers;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Comuni;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.IdentificativiCatastali;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Indirizzo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Riserve;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;

public class LetturaFabIta {

	public static List<String[]> header = new ArrayList<String[]>();

	public static List<UnitaImmobiliare> listUnitaImmobiliari = new ArrayList<UnitaImmobiliare>();
	public static List<IdentificativiCatastali> listIdentificativiCatastali = new ArrayList<IdentificativiCatastali>();
	public static List<Indirizzo> listIndirizzi = new ArrayList<Indirizzo>();
	public static List<Comuni> listComuni = new ArrayList<Comuni>();
	public static List<Riserve> listRiserve = new ArrayList<Riserve>();

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
				String[] tmpRiga = rigaCorrente.split("\\|", -1);
				int indice = Integer.parseInt(tmpRiga[5]);

				if (((tmpRiga.length)) == (header.get(indice - 1).length)) {
					for (int i = 0; i < header.get(indice - 1).length; i++) {
						campi.put(header.get(indice - 1)[i].toLowerCase(), tmpRiga[i]);
					}
					settareElemento(indice, campi);
				} else {
					int rip = ((tmpRiga.length) - 7) / ((header.get(indice - 1).length) - 7);
					System.out.println(((header.get(indice - 1).length) - 7));
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
			UnitaImmobiliare ui = new UnitaImmobiliare();
			ui.setValori(campi);
			listUnitaImmobiliari.add(ui);
			break;
		case 2:
			IdentificativiCatastali ic = new IdentificativiCatastali();
			ic.setValori(campi);
			listIdentificativiCatastali.add(ic);
			break;
		case 3:
			Indirizzo ii = new Indirizzo();
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
		default:
			;
			break;
		}

	}

	public static void stampaFileFab1() {
		try {
			FileWriter writer = new FileWriter("/home/ameneghini/Desktop/ITA_stampa/unitaImmobiliari.csv", false);

			for (int k = 0; k < header.get(0).length; k++) {
				writer.append(header.get(0)[k]);
				writer.append(';');
			}
			writer.append('\n');

			for (int j = 0; j < listUnitaImmobiliari.size(); j++) {
				for (int i = 0; i < header.get(0).length; i++) {
					String key = header.get(0)[i].toLowerCase();

					writer.append(listUnitaImmobiliari.get(j).getValori().get(key));
					writer.append(';');
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
			FileWriter writer = new FileWriter("/home/ameneghini/Desktop/ITA_stampa/identificativiCatastali.csv", false);

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
			FileWriter writer = new FileWriter("/home/ameneghini/Desktop/ITA_stampa/indirizzi.csv", false);

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
			FileWriter writer = new FileWriter("/home/ameneghini/Desktop/ITA_stampa/comuni.csv", false);

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
			FileWriter writer = new FileWriter("/home/ameneghini/Desktop/ITA_stampa/riserve.csv", false);

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

	public static void main(String[] args) {

		String pathF = "/home/ameneghini/Desktop/ITA_file/A281305833_1.FAB";
		String pathP = "/home/ameneghini/Desktop/ITA_header/filefab.csv";

		estrazioneHeaderFileFab(pathP);
		letturaFileFab(pathF);

		stampaFileFab1();
		stampaFileFab2();
		stampaFileFab3();
		stampaFileFab4();
		stampaFileFab5();

	}

}
