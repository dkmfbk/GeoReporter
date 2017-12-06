package eu.fbk.dkm.georeporter.tn.wrappers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.PersonaFisica;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.PersonaGiuridica;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.ProprietarioproTempore;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;

public class WrapperSog {

	// lista di vettori per l'elenco degli HEADER SOG (3 tipi)
	public static List<String[]> headerSog = new ArrayList<String[]>();

	public static String[] filename = { "P", "G", "T" };

	// lista di tipo P G e T degli elementi estratti dal file SOG
	public static List<PersonaFisica> listPersonaFisica = new ArrayList<PersonaFisica>();
	public static List<PersonaGiuridica> listPersonaGiuridica = new ArrayList<PersonaGiuridica>();
	public static List<ProprietarioproTempore> listProprietarioproTempore = new ArrayList<ProprietarioproTempore>();

	// estrazione delle liste di HEADER
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

	// creazione e SET della LIST degli elementi del file SOG
	public static void letturaFileSog(String pathP) {

		try {
			File file = new File(pathP);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();

			while (rigaCorrente != null) {
				Map<String, String> campi = new HashMap<String, String>();
				Map<String, String> valoriChiave = new HashMap<String, String>();
				List<Map<String, String>> listaValoriChiave = new ArrayList<Map<String, String>>();
				String[] tmpRiga = rigaCorrente.split("\\|", -1);

				String indiceL = tmpRiga[3];

				int indice;
				// controllo per il TIPO DI SOGGETTO
				if (indiceL.equals("P")) {
					indice = 0;

					for (int i = 0; i < tmpRiga.length; i++) {

						if (i < 4) {
							valoriChiave.put(headerSog.get(indice)[i].toLowerCase(),
									ControlloValore.controlloValore(tmpRiga[i]));
							listaValoriChiave.add(valoriChiave);
						}

						if (((tmpRiga.length) - 1) == 15) {
							campi.put(headerSog.get(indice)[i].toLowerCase(),
									ControlloValore.controlloValore(tmpRiga[i]));
						} else {
							if ((i > 3) && (i < 9)) {
								campi.put(headerSog.get(indice)[i].toLowerCase(),
										ControlloValore.controlloValore(tmpRiga[i]));
							} else if (i == 9) {
								campi.put(headerSog.get(indice)[i + 2].toLowerCase(),
										ControlloValore.controlloValore(tmpRiga[i]));
							} else if ((i == 10) || (i == 11)) {
								campi.put(headerSog.get(indice)[i + 3].toLowerCase(),
										ControlloValore.controlloValore(tmpRiga[i]));
							}

						}

					}

				} else if (indiceL.equals("G")) {
					indice = 1;
					for (int i = 0; i < tmpRiga.length; i++) {
						if (i < 4) {
							valoriChiave.put(headerSog.get(indice)[i].toLowerCase(),
									ControlloValore.controlloValore(tmpRiga[i]));
							listaValoriChiave.add(valoriChiave);
						}
						if (((tmpRiga.length) - 1) == 11) {
							campi.put(headerSog.get(indice)[i].toLowerCase(),
									ControlloValore.controlloValore(tmpRiga[i]));
						} else {
							if ((i > 3) && (i < 7)) {
								campi.put(headerSog.get(indice)[i].toLowerCase(),
										ControlloValore.controlloValore(tmpRiga[i]));
							} else if (i == 7) {
								campi.put(headerSog.get(indice)[i + 2].toLowerCase(),
										ControlloValore.controlloValore(tmpRiga[i]));
							}
						}
					}

				} else {
					indice = 2;
					// ciclio per 0 a 5 e non per DIMENSIONE RIGA perche' il file arriva' errato
					for (int i = 0; i < 6; i++) {
						if (i < 4) {
							valoriChiave.put(headerSog.get(indice)[i].toLowerCase(),
									ControlloValore.controlloValore(tmpRiga[i]));
							listaValoriChiave.add(valoriChiave);
						} else {
							campi.put(headerSog.get(indice)[i].toLowerCase(),
									ControlloValore.controlloValore(tmpRiga[i]));
						}
					}

				}

				settareElementoSog(indice, campi, listaValoriChiave);

				rigaCorrente = reader.readLine();
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// metodo per settare le 3 differenti liste di elementi
	public static void settareElementoSog(int indice, Map<String, String> campi,
			List<Map<String, String>> listaValoriChiave) {

		switch (indice) {
		case 0:
			PersonaFisica pf = new PersonaFisica();
			pf.setValori(campi);
			pf.setListaValoriChiave(listaValoriChiave);
			listPersonaFisica.add(pf);
			break;
		case 1:
			PersonaGiuridica pg = new PersonaGiuridica();
			pg.setValori(campi);
			pg.setListaValoriChiave(listaValoriChiave);
			listPersonaGiuridica.add(pg);
			break;
		case 2:
			ProprietarioproTempore pt = new ProprietarioproTempore();
			pt.setValori(campi);
			pt.setListaValoriChiave(listaValoriChiave);
			listProprietarioproTempore.add(pt);
			break;
		default:
			;
			break;
		}

	}

	public static void main(String[] args) {

	}

}
