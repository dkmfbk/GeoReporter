package eu.fbk.dkm.georeporter.tn.wrappers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.AnagraficaComunale;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Famiglia;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Titolarita;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.tn.wrappers.ControlloValore;

public class WrapperFabIndirizzo {

	// vettore per l'elenco degli HEADER
	public static String[] header;
	// lista di tipo AC degli elementi estratti dal file
	public static List<AnagraficaComunale> listAnagraficaComunale = new ArrayList<AnagraficaComunale>();
	public static List<Famiglia> listFamiglia = new ArrayList<Famiglia>();

	// estrazione HEADER
	public static void estrazioneHeaderFile2(String path) {

		try {
			File file = new File(path);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();
			if (rigaCorrente != null) {
				header = rigaCorrente.split(";", -1);
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void estrazioneHeaderFile(String path) {

		try {
			File file = new File(path);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();
			if (rigaCorrente != null) {
				header = rigaCorrente.split(";", -1);
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static void LetturaFile(String path) {
		try {
			File file = new File(path);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();
			int prima_rig = 0;
			while (rigaCorrente != null) {
				if (prima_rig == 0) {
					// salto prima riga file
				} else {

					Map<String, String> campi = new HashMap<String, String>();

					String[] tmpRiga = rigaCorrente.split(";", -1);

					for (int i = 0; i < header.length; i++) {
						campi.put(ControlloValore.puliziaHeader(header[i]),
								ControlloValore.controlloValore(tmpRiga[i]));
					}

					// settare elemento della lista e aggiungerlo alla list
					AnagraficaComunale ac = new AnagraficaComunale();
					ac.setValori(campi);
					listAnagraficaComunale.add(ac);

				}
				prima_rig++;
				rigaCorrente = reader.readLine();
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void LetturaFile2(String path) {
		try {
			File file = new File(path);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();
			int prima_rig = 0;
			while (rigaCorrente != null) {
				if (prima_rig == 0) {
					// salto prima riga file
				} else {

					Map<String, String> campi = new HashMap<String, String>();

					String[] tmpRiga = rigaCorrente.split(";", -1);

					for (int i = 0; i < header.length; i++) {
						campi.put(ControlloValore.puliziaHeader(header[i]),
								ControlloValore.controlloValore(tmpRiga[i]));
					}

					// settare elemento della lista e aggiungerlo alla list
					Famiglia fam = new Famiglia();
					fam.setValori(campi);
					listFamiglia.add(fam);

				}
				prima_rig++;
				rigaCorrente = reader.readLine();
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

	}

}
