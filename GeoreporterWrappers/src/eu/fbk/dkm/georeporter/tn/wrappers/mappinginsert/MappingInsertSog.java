package eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import java.util.Date;
import java.util.Calendar;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.PersonaFisica;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.PersonaGiuridica;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.ProprietarioproTempore;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.ControlloValore;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperSog;

public class MappingInsertSog {

	private static void LoadFile1(File filename, File filename2, List<PersonaFisica> listPers) {
		// lettura fai JSON per mappatura
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

			
			//for (Attributo attributo : listChiavi) {}
			
			// ciclo per creare listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listPers.get(j).getValori().get(parts[1]) != null)
						&& (listPers.get(j).getValori().get(parts[1]).isEmpty() == false)) {

					if ((parts[1].equals("datadinascita"))) {
						tmp.setValore(ControlloValore.cambioData(listPers.get(j).getValori().get(parts[1])));
					} else {
						tmp.setValore(listPers.get(j).getValori().get(parts[1]));
					}
					listAttributi.add(tmp);

				}

			}
			// ciclo per creare listaCHIAVI richiesti dal mapping
			for (int i = 0; i < data2.getAttributi().size(); i++) {

				String string = data2.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp2 = new Attributo();
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());

				if ((listPers.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listPers.get(j).getListaValoriChiave().get(0).get(parts[1]).isEmpty() == false)) {
					tmp2.setValore(listPers.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp2);
				}

			}
			String codfis = listPers.get(j).getValori().get("codicefiscale");
			if (!codfis.isEmpty()) {
				//inserire log in caso di assenza COD FIS
				// riga di tipo RIGATABELLA per PF
				RigaTabella rigaTPF = new RigaTabella();
				rigaTPF.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
				rigaTPF.setListaattributi(listAttributi);
				rigaTPF.setListachiave(listChiavi);
				rigaTPF.setUririga("http://dkm.fbk.eu/georeporter#SOG_" + codfis);
				// inserimento dell'elemento
				insertRiga(rigaTPF);
			}

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
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listPers.get(j).getValori().get(parts[1]) != null)
						&& (listPers.get(j).getValori().get(parts[1]).isEmpty() == false)) {

					// controllo data e sistemare in formato
					if ((parts[1].equals("datadinascita"))) {
						tmp.setValore(ControlloValore.cambioData(listPers.get(j).getValori().get(parts[1])));
					} else {
						tmp.setValore(listPers.get(j).getValori().get(parts[1]));
					}
					listAttributi.add(tmp);

				}

			}
			// ciclo per creare listaCHIAVI richiesti dal mapping
			for (int i = 0; i < data2.getAttributi().size(); i++) {

				String string = data2.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp2 = new Attributo();
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());

				if ((listPers.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listPers.get(j).getListaValoriChiave().get(0).get(parts[1]).isEmpty() == false)) {
					tmp2.setValore(listPers.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp2);
				}

			}
			String codfis = listPers.get(j).getValori().get("codicefiscale");
			if (!codfis.isEmpty()) {
				// riga di tipo RIGATABELLA per P
				RigaTabella rigaTPF = new RigaTabella();
				rigaTPF.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
				rigaTPF.setListaattributi(listAttributi);
				rigaTPF.setListachiave(listChiavi);
				rigaTPF.setUririga("http://dkm.fbk.eu/georeporter#SOG_" + codfis);
				// inserimento dell'elemento
				insertRiga(rigaTPF);
			}

		}

	}

	public static void associazioneMappingNomeVal3(MappingTabella data, MappingTabella data2,
			List<ProprietarioproTempore> listPers) {
		// ciclo la lista degli elementi PPT
		for (int j = 0; j < listPers.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			// ciclo per creare listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listPers.get(j).getValori().get(parts[1]) != null)
						&& (listPers.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listPers.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}

			}
			// ciclo per creare listaCHIAVI richiesti dal mapping
			for (int i = 0; i < data2.getAttributi().size(); i++) {

				String string = data2.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp2 = new Attributo();
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());

				if ((listPers.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listPers.get(j).getListaValoriChiave().get(0).get(parts[1]).isEmpty() == false)) {
					tmp2.setValore(listPers.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp2);
				}

			}
			// riga di tipo RIGATABELLA per PPT
			RigaTabella rigaTPF = new RigaTabella();
			rigaTPF.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTPF.setListaattributi(listAttributi);
			rigaTPF.setListachiave(listChiavi);
			String codamm = listPers.get(j).getListaValoriChiave().get(0).get("codiceamministrativo");
			String idesog = listPers.get(j).getListaValoriChiave().get(0).get("identificativosoggetto");
			rigaTPF.setUririga("http://dkm.fbk.eu/georeporter#SOG_" + codamm + "_" + idesog);
			// inserimento dell'elemento
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

		String pathS = "file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.SOG";
		String pathP = "file/TN_header/headerfilesog.csv";

		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		// dal file SOG
		WrapperSog.estrazioneHeaderFileSog(pathP);
		WrapperSog.letturaFileSog(pathS);
		// mapping e insert degli elementi PF PG PPT
		LoadFile1(new File("file/file_mapping/mappingPersonaFisica.json"),
				new File("file/file_mapping/mappingSoggetto.json"), WrapperSog.listPersonaFisica);

		LoadFile2(new File("file/file_mapping/mappingPersonaGiuridica.json"),
				new File("file/file_mapping/mappingSoggetto.json"), WrapperSog.listPersonaGiuridica);
		// LoadFile3(new File("file/file_mapping/mappingProprietarioProTempore.json"),
		// new File("file/file_mapping/mappingSoggetto.json"),
		// WrapperSog.listProprietarioproTempore);

	}

}