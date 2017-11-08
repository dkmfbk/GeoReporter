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

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.PersonaFisica;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.PersonaGiuridica;

public class LetturaSogIta {

	public static List<String[]> headerSog = new ArrayList<String[]>();

	public static String[] filename = { "P", "G" };

	public static List<PersonaFisica> listPersonaFisica = new ArrayList<PersonaFisica>();
	public static List<PersonaGiuridica> listPersonaGiuridica = new ArrayList<PersonaGiuridica>();

	public static void estrazioneHeaderFileSog(String pathP) {
		
		String[] tmp = new String[99];

		try {
			File file = new File(pathP);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();

			while (rigaCorrente != null) {

				tmp = rigaCorrente.split(";", -1);
				headerSog.add(tmp);
				rigaCorrente = reader.readLine();

			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void letturaFileSog(String pathP) {

		try {
			File file = new File(pathP);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();

			while (rigaCorrente != null) {
				Map<String, String> campi = new HashMap<String, String>();
				String[] tmpRiga = rigaCorrente.split("\\|", -1);
				
				String indiceL = tmpRiga[3];

				int indice;
				if (indiceL.equals("P")) {
					indice = 0;
				} else {
					indice = 1;
				} 
				
				for (int i = 0; i < headerSog.get(indice).length; i++) {
					campi.put(headerSog.get(indice)[i].toLowerCase(), tmpRiga[i]);
				}

				settareElementoSog(indice, campi);

				rigaCorrente = reader.readLine();
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void settareElementoSog(int indice, Map<String, String> campi) {

		switch (indice) {
		case 0:
			PersonaFisica pf = new PersonaFisica();
			pf.setValori(campi);
			listPersonaFisica.add(pf);
			break;
		case 1:
			PersonaGiuridica pg = new PersonaGiuridica();
			pg.setValori(campi);
			listPersonaGiuridica.add(pg);
			break;
		default:
			;
			break;
		}

	}
	
	public static void stampaFileSog1() {
		try {
			FileWriter writer = new FileWriter("/home/ameneghini/Desktop/ITA_stampa/personaFisica.csv", false);

			for (int k = 0; k < headerSog.get(0).length; k++) {
				writer.append(headerSog.get(0)[k]);
				writer.append(';');
			}
			writer.append('\n');
			
			for (int j = 0; j < listPersonaFisica.size(); j++) {
				for (int i = 0; i < headerSog.get(0).length; i++) {
					String key = headerSog.get(0)[i].toLowerCase();

					writer.append(listPersonaFisica.get(j).getValori().get(key));
					writer.append(';');
				}
				writer.append('\n');
			}

			writer.flush();
			writer.close();
		} catch (IOException e) {
		}
	}
	
	public static void stampaFileSog2() {
		try {
			FileWriter writer = new FileWriter("/home/ameneghini/Desktop/ITA_stampa/personaGiuridica.csv", false);

			for (int k = 0; k < headerSog.get(1).length; k++) {
				writer.append(headerSog.get(1)[k]);
				writer.append(';');
			}
			writer.append('\n');
			
			for (int j = 0; j < listPersonaGiuridica.size(); j++) {
				for (int i = 0; i < headerSog.get(1).length; i++) {
					String key = headerSog.get(1)[i].toLowerCase();

					writer.append(listPersonaGiuridica.get(j).getValori().get(key));
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

		String pathS = "/home/ameneghini/Desktop/ITA_file/A281305833_1.SOG";
		String pathP = "/home/ameneghini/Desktop/ITA_header/filesog.csv";

		estrazioneHeaderFileSog(pathP);
		letturaFileSog(pathS);
		
		stampaFileSog1();
		stampaFileSog2();
	}

}
