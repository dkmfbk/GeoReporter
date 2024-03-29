package eu.fbk.dkm.georeporter.ita.wrappers;

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

import eu.fbk.dkm.georeporter.tn.wrappers.ControlloValore;
import eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert.MappingInsertFabUiNote;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Annotazioni;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Comuni;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.IdentificativiCatastali;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Indirizzo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Intavolazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Particella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.PorzioneParticella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Riserve;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;

public class WrapperParFonITA {

	// lista degli "header" inseriti dal file esterno HEADER FILE FAB, 6 liste
	// diverse, una per ogni TIPO RECORD
	public  List<String[]> headerP = new ArrayList<String[]>();
	// liste che raccolgono tutti gli elementi del file PAR divisi per TIPO
	public  List<Particella> listParticella = new ArrayList<Particella>();
	public  List<PorzioneParticella> listPorzioneParticella = new ArrayList<PorzioneParticella>();
	public  String codiceAmministrativo;
	// metodo che analizza il file (che deve esser caricato) degli HEADER e crea la
	// lista dei 6 header
	public  void estrazioneHeaderFilePar(String pathP) {
		String[] tmp;

		try {
			File file = new File(pathP);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();

			while (rigaCorrente != null) {
				tmp = rigaCorrente.split(";", -1);
				headerP.add(tmp);
				rigaCorrente = reader.readLine();
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public  void letturaFilePar(String pathP) {

		try {

			File file = new File(pathP);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();

			// ciclo tutti il file, riga per riga fino a fine file
			while (rigaCorrente != null) {
				Map<String, String> campi = new HashMap<String, String>();
				Map<String, String> valoriChiave = new HashMap<String, String>();
				List<Map<String, String>> listaValoriChiave = new ArrayList<Map<String, String>>();
				Map<String, String> intavolazioneIniziale = new HashMap<String, String>();
				Map<String, String> intavolazioneFinale = new HashMap<String, String>();

				// controllo il campo 6 del file per capire a che categoria corrisponde la riga
				// che sto leggendo
				String[] tmpRiga = rigaCorrente.split("\\|", -1);
				int indice = Integer.parseInt(tmpRiga[5]);

				// se tipo = 1
				if (indice == 1) {

					// ciclio nel caso in cui abbiamo solo un elemento per riga

					for (int i = 0; i < 6; i++) {
						valoriChiave.put(headerP.get(indice - 1)[i].toLowerCase(),
								ControlloValore.controlloValore(tmpRiga[i]));
						listaValoriChiave.add(valoriChiave);
					}
					for (int i = 6; i < 23; i++) {
						campi.put(headerP.get(indice - 1)[i].toLowerCase(),
								ControlloValore.controlloValore(tmpRiga[i]));
					}
					for (int i = 23; i < 29; i++) {
						// controllo data
						if ((headerP.get(indice - 1)[i].toLowerCase().equals("datadiefficacia"))
								|| (headerP.get(indice - 1)[i].toLowerCase().equals("datadiregistrazioneinatti"))) {
							intavolazioneIniziale.put(headerP.get(indice - 1)[i].toLowerCase(),
									ControlloValore.cambioData(ControlloValore.controlloValore(tmpRiga[i])));
						} else {
							intavolazioneIniziale.put(headerP.get(indice - 1)[i].toLowerCase(),
									ControlloValore.controlloValore(tmpRiga[i]));
						}

					} 
					
					//for (int i = 25; i < 29; i++) {
					//	campi.put(headerP.get(indice - 1)[i].toLowerCase(),
					//			ControlloValore.controlloValore(tmpRiga[i]));
					//}
				//	campi.put(headerP.get(indice - 1)[20].toLowerCase(), ControlloValore.controlloValore(tmpRiga[20]));
					for (int i = 29; i < 35; i++) {
						// controllo data
						if ((headerP.get(indice - 1)[i].toLowerCase().equals("datadiefficacia"))
								|| (headerP.get(indice - 1)[i].toLowerCase().equals("datadiregistrazioneinatti"))) {
							intavolazioneFinale.put(headerP.get(indice - 1)[i].toLowerCase(),
									ControlloValore.cambioData(ControlloValore.controlloValore(tmpRiga[i])));
						} else {
							intavolazioneFinale.put(headerP.get(indice - 1)[i].toLowerCase(),
									ControlloValore.controlloValore(tmpRiga[i]));
						}
					}
					campi.put(headerP.get(indice - 1)[35].toLowerCase(), ControlloValore.controlloValore(tmpRiga[35]));
					for (int i = 36; i < 38; i++) {
						campi.put(headerP.get(indice - 1)[i].toLowerCase(),
								ControlloValore.controlloValore(tmpRiga[i]));
					}

					Particella par = new Particella();
					Intavolazione ini = new Intavolazione();
					Intavolazione inf = new Intavolazione();

					par.setValori(campi);
					par.setListaValoriChiave(listaValoriChiave);
					ini.setValori(intavolazioneIniziale);
					inf.setValori(intavolazioneFinale);

					par.setIntavolazioneIniziale(ini);
					par.setIntavolazioneFinale(inf);

					listParticella.add(par);

				} else {
					// porzione di particella non la utilizziamo
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
	public  void settareElemento(Map<String, String> campi, List<Map<String, String>> listaValoriChiave) {

		PorzioneParticella pp = new PorzioneParticella();
		pp.setValori(campi);
		pp.setListaValoriChiave(listaValoriChiave);
		listPorzioneParticella.add(pp);

	}

	public static void main(String[] args) {

	}

}
