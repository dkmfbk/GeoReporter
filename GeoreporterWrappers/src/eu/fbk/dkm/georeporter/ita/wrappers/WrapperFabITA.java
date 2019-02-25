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

import eu.fbk.dkm.georeporter.tn.wrappers.ControlloValore;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Comuni;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.IdentificativiCatastali;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Indirizzo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Riserve;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;

public class WrapperFabITA {

	public  List<String[]> header = new ArrayList<String[]>();

	public  List<UnitaImmobiliare> listUnitaImmobiliari = new ArrayList<UnitaImmobiliare>();
	public  List<IdentificativiCatastali> listIdentificativiCatastali = new ArrayList<IdentificativiCatastali>();
	public  List<Indirizzo> listIndirizzi = new ArrayList<Indirizzo>();
	public  List<Comuni> listComuni = new ArrayList<Comuni>();
	public  List<Riserve> listRiserve = new ArrayList<Riserve>();

	public  void estrazioneHeaderFileFab(String pathP) {

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

	public  void letturaFileFab(String pathP) {

		try {

			File file = new File(pathP);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();

			while (rigaCorrente != null) {
				Map<String, String> valoriChiave = new HashMap<String, String>();
				List<Map<String, String>> listaValoriChiave = new ArrayList<Map<String, String>>();
				Map<String, String> campi = new HashMap<String, String>();
				Map<String, String> notaIniziale = new HashMap<String, String>();
				Map<String, String> notaFinale = new HashMap<String, String>();
				String[] tmpRiga = rigaCorrente.split("\\|", -1);
				int indice = Integer.parseInt(tmpRiga[5]);
				if (indice == 1) {
					int var=0;
					
					
	/*				
					for (int i = 23; i < 28; i++) {

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
					for (int i = 29; i < 34; i++) {
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
					*/
						
					
					
					
					
					
				
						//for (int j = 0; j < header.get(indice - 1).length; j++) {
						
							for (int i = 0; i < 6; i++) {
								valoriChiave.put(header.get(indice - 1)[i].toLowerCase(),
										ControlloValore.controlloValore(tmpRiga[i]));
								listaValoriChiave.add(valoriChiave);
							}
							for (int i = 6; i < 23; i++) {
								campi.put(header.get(indice - 1)[i].toLowerCase(), ControlloValore.controlloValore(tmpRiga[i]));
							}
						//	for (int i = 13; i < 31; i++) {
						//		campi.put(header.get(indice - 1)[i + var].toLowerCase(),
							//			ControlloValore.controlloValore(tmpRiga[i]));
							//}
							for (int i = 23; i < 28; i++) {

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
							for (int i = 28; i < 34; i++) {
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
							for (int i = 34; i < 40; i++) {
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
							
							
						//	settareElemento(indice, campi,listaValoriChiave);	
							
						
							
					
				
					
				}else {
				
				
				if (((tmpRiga.length)) == (header.get(indice - 1).length)) {
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
					
					
					
					
					
					
					
/*//				} else {
//					int rip = ((tmpRiga.length) - 7) / ((header.get(indice - 1).length) - 7);
//					System.out.println(((header.get(indice - 1).length) - 7));
//					for (int j = 0; j < rip; j++) {
//						Map<String, String> campi2 = new HashMap<String, String>();
//						for (int i = 0; i < 6; i++) {
//							
//								valoriChiave.put(header.get(indice - 1)[i].toLowerCase(),
//										ControlloValore.controlloValore(tmpRiga[i]));
//								listaValoriChiave.add(valoriChiave);
//							
//							//campi2.put(header.get(indice - 1)[i].toLowerCase(), tmpRiga[i]);
//						}
//						for (int k = 6; k < ((header.get(indice - 1).length)); k++) {
//							campi2.put(header.get(indice - 1)[k].toLowerCase(),
//									tmpRiga[k + (((header.get(indice - 1).length) - 7) * j)]);
//						}
//						settareElemento(indice, campi2,listaValoriChiave);
//					}
//				}
*/					} else {
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

	public  void settareElemento(int indice, Map<String, String> campi,List<Map<String, String>> listaValoriChiave)  {

		switch (indice) {
		case 1:
			UnitaImmobiliare ui = new UnitaImmobiliare();
			ui.setValori(campi);
			ui.setListaValoriChiave(listaValoriChiave);
			listUnitaImmobiliari.add(ui);
			break;
		case 2:
			IdentificativiCatastali ic = new IdentificativiCatastali();
			ic.setValori(campi);
			ic.setListaValoriChiave(listaValoriChiave);
			listIdentificativiCatastali.add(ic);
			break;
		case 3:
			Indirizzo ii = new Indirizzo();
			ii.setListaValoriChiave(listaValoriChiave);
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

	public  void stampaFileFab1() {
		try {
			FileWriter writer = new FileWriter("unitaImmobiliari.csv", false);

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

	public  void stampaFileFab2() {
		try {
			FileWriter writer = new FileWriter("file/identificativiCatastali.csv", false);

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

	public  void stampaFileFab3() {
		try {
			FileWriter writer = new FileWriter("file/indirizzi.csv", false);

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

	public  void stampaFileFab4() {
		try {
			FileWriter writer = new FileWriter("file/comuni.csv", false);

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

	public  void stampaFileFab5() {
		try {
			FileWriter writer = new FileWriter("file/riserve.csv", false);

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

	//	String pathF = "file/ITA_file/A281305833_1.FAB";
	//	String pathP = "file/ITA_headerfilefab.csv";

	//	estrazioneHeaderFileFab(pathP);
	//	letturaFileFab(pathF);

	//	stampaFileFab1();
	//	stampaFileFab2();
	//	stampaFileFab3();
	//	stampaFileFab4();
	//	stampaFileFab5();

	}

}
