package eu.fbk.dkm.georeporter.ita.wrappers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Titolarita;


public class LetturaTitIta {

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
				String[] tmpRiga = rigaCorrente.split("\\|", -1);

				//if (tmpRiga.length == 36) {
					for (int i = 0; i < headerTit.length; i++) {
						campi.put(headerTit[i].toLowerCase(), tmpRiga[i]);
					}
				/*} else {
					for (int i = 0; i < 6; i++) {
						campi.put(headerTit[i].toLowerCase(), tmpRiga[i]);
					}
					for (int i = 12; i < 36; i++) {
						campi.put(headerTit[i].toLowerCase(), tmpRiga[i - 6]);
					}
				}*/

				settareElementoTit(campi);

				rigaCorrente = reader.readLine();
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void settareElementoTit(Map<String, String> campi) {

		Titolarita tt = new Titolarita();
		tt.setValori(campi);
		listTitolarita.add(tt);

	}

	public static void stampaFileTit() {
		try {
			FileWriter writer = new FileWriter("/home/ameneghini/Desktop/ITA_stampa/titolarita.csv", false);

			for (int k = 0; k < headerTit.length; k++) {
				writer.append(headerTit[k]);
				writer.append(';');
			}
			writer.append('\n');

			for (int j = 0; j < listTitolarita.size(); j++) {
				for (int i = 0; i < headerTit.length; i++) {
					String key = headerTit[i].toLowerCase();

					writer.append(listTitolarita.get(j).getValori().get(key));
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

		String pathT = "/home/ameneghini/Desktop/ITA_file/A281305833_1.TIT";
		String pathP = "/home/ameneghini/Desktop/ITA_header/filetit.csv";

		estrazioneHeaderFileTit(pathP);
		
		letturaFileTit(pathT);
		
		stampaFileTit();
	}

}