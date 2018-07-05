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
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Intavolazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Particella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Titolarita;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.TitolaritaCompleta;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.tn.wrappers.ControlloValore;

public class WrapperTtcFon {

	// vettore per l'elenco degli HEADER TTC
	public  String[] headerTtc;
	// lista di tipo TITOLARITA degli elementi estratti dal file TTC
	public  List<TitolaritaCompleta> listTitolaritaCompleta = new ArrayList<TitolaritaCompleta>();

	// estrazione degli HEADER
	public  void estrazioneHeaderFileTtcFon(String pathP) {

		try {
			File file = new File(pathP);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();

			if (rigaCorrente != null) {
				headerTtc = rigaCorrente.split(";", -1);
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// creazione e SET della LIST degli elementi del file TTC
	public  void letturaFileTtcFon(String pathP) {

		try {
			File file = new File(pathP);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();

			while (rigaCorrente != null) {

				Map<String, String> campi = new HashMap<String, String>();
				Map<String, String> valoriChiave = new HashMap<String, String>();
				List<Map<String, String>> listaValoriChiave = new ArrayList<Map<String, String>>();
				Map<String, String> intavolazioneIniziale = new HashMap<String, String>();
				Map<String, String> intavolazioneFinale = new HashMap<String, String>();

				String[] tmpRiga = rigaCorrente.split("\\|", -1);

				for (int i = 0; i < 7; i++) {
					valoriChiave.put(headerTtc[i].toLowerCase(), ControlloValore.controlloValore(tmpRiga[i]));
					listaValoriChiave.add(valoriChiave);
				}
				for (int i = 7; i < 12; i++) {
					campi.put(headerTtc[i].toLowerCase(), ControlloValore.controlloValore(tmpRiga[i]));
				}
				for (int i = 12; i < 17; i++) {
					// controllo data e sistemare in formato
					if ((headerTtc[i].toLowerCase().equals("datadiintavolazione"))
							|| (headerTtc[i].toLowerCase().equals("datadiregistrazioneinatti"))) {
						intavolazioneIniziale.put(headerTtc[i].toLowerCase(),
								ControlloValore.cambioData(ControlloValore.controlloValore(tmpRiga[i])));
					} else {
						intavolazioneIniziale.put(headerTtc[i].toLowerCase(),
								ControlloValore.controlloValore(tmpRiga[i]));
					}
				}
				for (int i = 17; i < 21; i++) {
					campi.put(headerTtc[i].toLowerCase(), ControlloValore.controlloValore(tmpRiga[i]));
				}
				for (int i = 21; i < 26; i++) {
					// controllo data e sistemare in formato
					if ((headerTtc[i].toLowerCase().equals("datadiintavolazione"))
							|| (headerTtc[i].toLowerCase().equals("datadiregistrazioneinatti"))) {
						intavolazioneFinale.put(headerTtc[i].toLowerCase(),
								ControlloValore.cambioData(ControlloValore.controlloValore(tmpRiga[i])));
					} else {
						intavolazioneFinale.put(headerTtc[i].toLowerCase(),
								ControlloValore.controlloValore(tmpRiga[i]));
					}
				}
				campi.put(headerTtc[26].toLowerCase(), ControlloValore.controlloValore(tmpRiga[26]));

				TitolaritaCompleta ttc = new TitolaritaCompleta();
				Intavolazione ini = new Intavolazione();
				Intavolazione inf = new Intavolazione();

				ttc.setValori(campi);
				ttc.setListaValoriChiave(listaValoriChiave);
				ini.setValori(intavolazioneIniziale);
				inf.setValori(intavolazioneFinale);

				ttc.setIntavolazioneIniziale(ini);
				ttc.setIntavolazioneFinale(inf);

				listTitolaritaCompleta.add(ttc);

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
