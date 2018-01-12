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

import eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert.MappingInsertFabIde;
import eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert.MappingInsertFabUiNote;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Annotazioni;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Comuni;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.IdentificativiCatastali;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Indirizzo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Riserve;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;

public class WrapperFab {

	// lista degli "header" inseriti dal file esterno HEADER FILE FAB, 6 liste
	// diverse, una per ogni TIPO RECORD
	public static List<String[]> header = new ArrayList<String[]>();
	// liste che raccolgono tutti gli elementi del file FAB divisi per TIPO
	public static List<UnitaImmobiliare> listUnitaImmobiliari = new ArrayList<UnitaImmobiliare>();
	public static List<IdentificativiCatastali> listIdentificativiCatastali = new ArrayList<IdentificativiCatastali>();
	public static List<Indirizzo> listIndirizzi = new ArrayList<Indirizzo>();
	public static List<Comuni> listComuni = new ArrayList<Comuni>();
	public static List<Riserve> listRiserve = new ArrayList<Riserve>();
	public static List<Annotazioni> listAnnotazioni = new ArrayList<Annotazioni>();

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
						valoriChiave.put(header.get(indice - 1)[i].toLowerCase(),
								ControlloValore.controlloValore(tmpRiga[i]));
						listaValoriChiave.add(valoriChiave);
					}
					for (int i = 6; i < 13; i++) {
						campi.put(header.get(indice - 1)[i].toLowerCase(), ControlloValore.controlloValore(tmpRiga[i]));
					}
					for (int i = 13; i < 31; i++) {
						campi.put(header.get(indice - 1)[i + var].toLowerCase(),
								ControlloValore.controlloValore(tmpRiga[i]));
					}
					for (int i = 31; i < 37; i++) {

						if ((header.get(indice - 1)[i + var].toLowerCase().equals("datadiefficacia"))
								|| (header.get(indice - 1)[i + var].toLowerCase()
										.equals("datadiregistrazioneinatti"))) {
							notaIniziale.put(header.get(indice - 1)[i + var].toLowerCase(),
									ControlloValore.cambioData(ControlloValore.controlloValore(tmpRiga[i])));
						} else {
							notaIniziale.put(header.get(indice - 1)[i + var].toLowerCase(),
									ControlloValore.controlloValore(tmpRiga[i]));
						}
					}
					for (int i = 37; i < 43; i++) {
						if ((header.get(indice - 1)[i + var].toLowerCase().equals("datadiefficacia"))
								|| (header.get(indice - 1)[i + var].toLowerCase()
										.equals("datadiregistrazioneinatti"))) {
							notaFinale.put(header.get(indice - 1)[i + var].toLowerCase(),
									ControlloValore.cambioData(ControlloValore.controlloValore(tmpRiga[i])));
						} else {
							notaFinale.put(header.get(indice - 1)[i + var].toLowerCase(),
									ControlloValore.controlloValore(tmpRiga[i]));
						}
					}
					for (int i = 43; i < 47; i++) {
						campi.put(header.get(indice - 1)[i + var].toLowerCase(),
								ControlloValore.controlloValore(tmpRiga[i]));
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
								valoriChiave.put(header.get(indice - 1)[i].toLowerCase(),
										ControlloValore.controlloValore(tmpRiga[i]));
								listaValoriChiave.add(valoriChiave);
							} else {

								if ((header.get(indice - 1)[i].toLowerCase().equals("foglio"))
										|| (header.get(indice - 1)[i].toLowerCase().equals("numero"))
										|| (header.get(indice - 1)[i].toLowerCase().equals("denominatore"))
										|| (header.get(indice - 1)[i].toLowerCase().equals("subalterno"))
												&& (header.get(indice - 1)[i].toLowerCase() != null)) {
									campi.put(header.get(indice - 1)[i].toLowerCase(),
											ControlloValore.TolgoZeri(ControlloValore.controlloValore(tmpRiga[i])));
								} else {

									campi.put(header.get(indice - 1)[i].toLowerCase(),
											ControlloValore.controlloValore(tmpRiga[i]));
								}

							}
						}
						settareElemento(indice, campi, listaValoriChiave);
					} else {
						int rip = ((tmpRiga.length) - 7) / ((header.get(indice - 1).length) - 7);
						for (int j = 0; j < rip; j++) {
							Map<String, String> campi2 = new HashMap<String, String>();
							// Map<String, String> valoriChiave2 = new HashMap<String, String>();
							for (int i = 0; i < 6; i++) {
								valoriChiave.put(header.get(indice - 1)[i].toLowerCase(),
										ControlloValore.controlloValore(tmpRiga[i]));
								listaValoriChiave.add(valoriChiave);
							}
							for (int k = 6; k < ((header.get(indice - 1).length)); k++) {

								if ((header.get(indice - 1)[k].toLowerCase().equals("foglio"))
										|| (header.get(indice - 1)[k].toLowerCase().equals("numero"))
										|| (header.get(indice - 1)[k].toLowerCase().equals("denominatore"))
										|| (header.get(indice - 1)[k].toLowerCase().equals("subalterno"))
												&& (header.get(indice - 1)[k].toLowerCase() != null)) {
									campi2.put(header.get(indice - 1)[k].toLowerCase(),
											ControlloValore.TolgoZeri(ControlloValore.controlloValore(
													tmpRiga[k + (((header.get(indice - 1).length) - 7) * j)])));
								} else {
									campi2.put(header.get(indice - 1)[k].toLowerCase(), ControlloValore
											.controlloValore(tmpRiga[k + (((header.get(indice - 1).length) - 7) * j)]));
								}

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
			break;
		case 2:
			IdentificativiCatastali ic = new IdentificativiCatastali();
			ic.setValori(campi);
			ic.setListaValoriChiave(listaValoriChiave);
			listIdentificativiCatastali.add(ic);
			break;
		case 3:
			Indirizzo ii = new Indirizzo();
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

	public static void main(String[] args) {

	}

}
