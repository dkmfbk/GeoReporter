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
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Titolarita;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.tn.wrappers.ControlloValore;

public class WrapperTit {

	// vettore per l'elenco degli HEADER TIT
	public  String[] headerTit;
	// lista di tipo TITOLARITA degli elementi estratti dal file TIT
	public  List<Titolarita> listTitolarita = new ArrayList<Titolarita>();

	// estrazione degli HEADER
	public  void estrazioneHeaderFileTit(String pathP) {

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

	// creazione e SET della LIST degli elementi del file TIT
	public  void letturaFileTit(String pathP) {

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
					valoriChiave.put(headerTit[i].toLowerCase(), ControlloValore.controlloValore(tmpRiga[i]));
					listaValoriChiave.add(valoriChiave);
				}
				for (int i = 6; i < 19 - var; i++) {
					campi.put(headerTit[i + var].toLowerCase(), ControlloValore.controlloValore(tmpRiga[i]));
				}
				for (int i = 19 - var; i < 25 - var; i++) {
					// controllo data e sistemare in formato
					if ((headerTit[i + var].toLowerCase().equals("datadivalidita"))
							|| (headerTit[i + var].toLowerCase().equals("datadiregistrazioneinatti"))) {
						notaIniziale.put(headerTit[i + var].toLowerCase(),
								ControlloValore.cambioData(ControlloValore.controlloValore(tmpRiga[i])));
					} else {
						notaIniziale.put(headerTit[i + var].toLowerCase(), ControlloValore.controlloValore(tmpRiga[i]));
					}

				}
				campi.put(headerTit[25].toLowerCase(), ControlloValore.controlloValore(tmpRiga[25]));
				for (int i = 26 - var; i < 31 - var; i++) {
					if ((headerTit[i + var].toLowerCase().equals("datadivalidita"))
							|| (headerTit[i + var].toLowerCase().equals("datadiregistrazioneinatti"))) {
						notaFinale.put(headerTit[i + var].toLowerCase(),
								ControlloValore.cambioData(ControlloValore.controlloValore(tmpRiga[i])));
					} else {
						notaFinale.put(headerTit[i + var].toLowerCase(), ControlloValore.controlloValore(tmpRiga[i]));
					}

				}
				for (int i = 32 - var; i < 35 - var; i++) {
					campi.put(headerTit[i + var].toLowerCase(), ControlloValore.controlloValore(tmpRiga[i]));
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

	public static void main(String[] args) {

	}

}
