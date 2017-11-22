package eu.fbk.dkm.georeporter.tn.wrappers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.PersonaFisica;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.PersonaGiuridica;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.ProprietarioproTempore;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;

public class LetturaSog {

	public static List<String[]> headerSog = new ArrayList<String[]>();

	public static String[] filename = { "P", "G", "T" };

	public static List<PersonaFisica> listPersonaFisica = new ArrayList<PersonaFisica>();
	public static List<PersonaGiuridica> listPersonaGiuridica = new ArrayList<PersonaGiuridica>();
	public static List<ProprietarioproTempore> listProprietarioproTempore = new ArrayList<ProprietarioproTempore>();

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
				Map<String, String> valoriChiave = new HashMap<String, String>();
				List<Map<String, String>> listaValoriChiave = new ArrayList<Map<String, String>>();
				String[] tmpRiga = rigaCorrente.split("\\|", -1);

				String indiceL = tmpRiga[3];

				int indice;

				if (indiceL.equals("P")) {
					indice = 0;

					// modificare ciclo, da 0 a dimensione massima della riga
					for (int i = 0; i < headerSog.get(indice).length; i++) {

						if (i < 4) {
							valoriChiave.put(headerSog.get(indice)[i].toLowerCase(), tmpRiga[i]);
							listaValoriChiave.add(valoriChiave);
						}

						if (((tmpRiga.length) - 1) == 15) {
							campi.put(headerSog.get(indice)[i].toLowerCase(), tmpRiga[i]);
						} else {
							if ((i > 3) && (i < 9)) {
								campi.put(headerSog.get(indice)[i].toLowerCase(), tmpRiga[i]);
							} else if (i == 9) {
								campi.put(headerSog.get(indice)[i + 2].toLowerCase(), tmpRiga[i]);
							} else if ((i == 10) || (i == 11)) {
								campi.put(headerSog.get(indice)[i + 3].toLowerCase(), tmpRiga[i]);
							}

						}

					}

				} else if (indiceL.equals("G")) {
					indice = 1;
					// modificare ciclo, da 0 a dimensione massima della riga
					for (int i = 0; i < headerSog.get(indice).length; i++) {
						if (i < 4) {
							valoriChiave.put(headerSog.get(indice)[i].toLowerCase(), tmpRiga[i]);
							listaValoriChiave.add(valoriChiave);
						}
						if (((tmpRiga.length) - 1) == 11) {
							campi.put(headerSog.get(indice)[i].toLowerCase(), tmpRiga[i]);
						} else {
							if ((i > 3) && (i < 7)) {
								campi.put(headerSog.get(indice)[i].toLowerCase(), tmpRiga[i]);
							} else if (i == 7) {
								campi.put(headerSog.get(indice)[i + 2].toLowerCase(), tmpRiga[i]);
							}
						}
					}

				} else {
					indice = 2;
					// System.out.println("INIZIO RIGA");
					for (int i = 0; i < 6; i++) {
						if (i < 4) {
							valoriChiave.put(headerSog.get(indice)[i].toLowerCase(), tmpRiga[i]);
							listaValoriChiave.add(valoriChiave);
							// System.out.println(headerSog.get(indice)[i].toLowerCase() + "->" +
							// tmpRiga[i]);
						} else {
							campi.put(headerSog.get(indice)[i].toLowerCase(), tmpRiga[i]);
							// System.out.println(headerSog.get(indice)[i].toLowerCase() + "->" +
							// tmpRiga[i]);
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

	private static void LoadFile1(File filename, File filename2, List<PersonaFisica> listPers) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(filename2));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(data, data2, listPers);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void LoadFile2(File filename, File filename2, List<PersonaGiuridica> listPers) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(filename2));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal2(data, data2, listPers);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void LoadFile3(File filename, File filename2, List<ProprietarioproTempore> listPers) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		try {
		
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(filename2));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);
			
			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal3(data, data2, listPers);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void associazioneMappingNomeVal(MappingTabella data, MappingTabella data2,
			List<PersonaFisica> listPers) {
		// ciclo la lista degli elementi P
		for (int j = 0; j < listPers.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			// ciclo per creare listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				// tmp.setIdDomain(data.getIdTabella().getMapping());
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if (listPers.get(j).getValori().get(parts[1]) != null) {
					tmp.setValore(listPers.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}

			}

			for (int i = 0; i < data2.getAttributi().size(); i++) {

				String string = data2.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp2 = new Attributo();
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());

				if (listPers.get(j).getValori().get(parts[1]) != null) {
					tmp2.setValore(listPers.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp2);
				}

			}

			// riga di tipo RIGATABELLA per PF
			RigaTabella rigaTPF = new RigaTabella();
			rigaTPF.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTPF.setListaattributi(listAttributi);
			rigaTPF.setListachiave(listChiavi);
			String codamm = listPers.get(j).getListaValoriChiave().get(0).get("codiceamministrativo");
			String idesog = listPers.get(j).getListaValoriChiave().get(0).get("identificativosoggetto");
			rigaTPF.setUririga("http://dkm.fbk.eu/georeporter#SOG_" + codamm + "_" + idesog);

			insertRiga(rigaTPF);
		}

	}

	public static void associazioneMappingNomeVal2(MappingTabella data, MappingTabella data2,
			List<PersonaGiuridica> listPers) {
		// ciclo la lista degli elementi PG
		for (int j = 0; j < listPers.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			// ciclo per creare listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				// tmp.setIdDomain(data.getIdTabella().getMapping());
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if (listPers.get(j).getValori().get(parts[1]) != null) {
					tmp.setValore(listPers.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}

			}

			for (int i = 0; i < data2.getAttributi().size(); i++) {

				String string = data2.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp2 = new Attributo();
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());

				if (listPers.get(j).getValori().get(parts[1]) != null) {
					tmp2.setValore(listPers.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp2);
				}

			}

			// riga di tipo RIGATABELLA per PF
			RigaTabella rigaTPF = new RigaTabella();
			rigaTPF.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTPF.setListaattributi(listAttributi);
			rigaTPF.setListachiave(listChiavi);
			String codamm = listPers.get(j).getListaValoriChiave().get(0).get("codiceamministrativo");
			String idesog = listPers.get(j).getListaValoriChiave().get(0).get("identificativosoggetto");
			rigaTPF.setUririga("http://dkm.fbk.eu/georeporter#SOG_" + codamm + "_" + idesog);

			insertRiga(rigaTPF);
		}

	}

	public static void associazioneMappingNomeVal3(MappingTabella data, MappingTabella data2,
			List<ProprietarioproTempore> listPers) {
		// ciclo la lista degli elementi PP
		for (int j = 0; j < listPers.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			// ciclo per creare listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				// tmp.setIdDomain(data.getIdTabella().getMapping());
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if (listPers.get(j).getValori().get(parts[1]) != null) {
					tmp.setValore(listPers.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}

			}

			for (int i = 0; i < data2.getAttributi().size(); i++) {

				String string = data2.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp2 = new Attributo();
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());

				if (listPers.get(j).getValori().get(parts[1]) != null) {
					tmp2.setValore(listPers.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp2);
				}

			}

			// riga di tipo RIGATABELLA per PF
			RigaTabella rigaTPF = new RigaTabella();
			rigaTPF.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTPF.setListaattributi(listAttributi);
			rigaTPF.setListachiave(listChiavi);
			String codamm = listPers.get(j).getListaValoriChiave().get(0).get("codiceamministrativo");
			String idesog = listPers.get(j).getListaValoriChiave().get(0).get("identificativosoggetto");
			rigaTPF.setUririga("http://dkm.fbk.eu/georeporter#SOG_" + codamm + "_" + idesog);

			insertRiga(rigaTPF);
		}

	}

	public static void insertRiga(RigaTabella riga) {

		String targetURL = "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";

		Gson gson = new Gson();
		String json = gson.toJson(riga);
		// System.out.println(json);

		try {

			URL targetUrl = new URL(targetURL);

			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("Accept", "application/json");

			String input = json;

			OutputStream outputStream = httpConnection.getOutputStream();
			outputStream.write(input.getBytes());
			outputStream.flush();

			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
			}

			BufferedReader responseBuffer = new BufferedReader(
					new InputStreamReader((httpConnection.getInputStream())));

			String output;
			// System.out.println("Output from Server:\n");
			while ((output = responseBuffer.readLine()) != null) {
				// System.out.println(output);
				// output = responseBuffer.readLine();

			}

			httpConnection.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		// return output;
	}

	public static void main(String[] args) {

		// String pathS =
		// "/home/ameneghini/Desktop/TN_file/IDR0000115470_TIPOFACSN_CAMML322.SOG";
		String pathS = "file/TN_file/prova.SOG";
		String pathP = "file/TN_header/headerfilesog.csv";

		estrazioneHeaderFileSog(pathP);
		letturaFileSog(pathS);
		// LoadFile1(new File("file/file_mapping/mappingPersonaFisica.json"),
		// new File("file/file_mapping/mappingSoggetto.json"), listPersonaFisica);
		// LoadFile2(new File("file/file_mapping/mappingPersonaGiuridica.json"),
		// new File("file/file_mapping/mappingSoggetto.json"), listPersonaGiuridica);
		LoadFile3(new File("file/file_mapping/mappingProprietarioProTempore.json"),
				new File("file/file_mapping/mappingSoggetto.json"), listProprietarioproTempore);

	}

}
