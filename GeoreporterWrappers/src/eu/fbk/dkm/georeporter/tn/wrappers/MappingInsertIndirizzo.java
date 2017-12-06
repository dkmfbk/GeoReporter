package eu.fbk.dkm.georeporter.tn.wrappers;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

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
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperIndirizzo;

public class MappingInsertIndirizzo {

	public static List<AnagraficaComunale> listAnagraficaComunale = WrapperIndirizzo.listAnagraficaComunale;
	public static List<Famiglia> listFamiglia = WrapperFamiglia.listFamiglia;

	private static void LoadFile(File filename) {

		Gson gson = new Gson();
		JsonReader reader;

		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(data);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void LoadFile2(File filename) {

		Gson gson = new Gson();
		JsonReader reader;

		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal2(data);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void associazioneMappingNomeVal(MappingTabella data) {
		// ciclo la lista degli elementi IND
		for (int j = 0; j < listAnagraficaComunale.size(); j++) {

			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			// ciclo per crea la listaCHIAVI e listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listAnagraficaComunale.get(j).getValori().get(parts[1]) != null)
						&& (listAnagraficaComunale.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listAnagraficaComunale.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}

			}

			// riga di tipo RIGATABELLA per IND
			RigaTabella rigaTIND = new RigaTabella();

			rigaTIND.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTIND.setListaattributi(listAttributi);
			//creo l'indirizzo univoco grazie dalla data d'inserimento
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			long time = cal.getTimeInMillis();
			
			rigaTIND.setUririga("http://dkm.fbk.eu/georeporter#IND_"+time);

			insertRiga(rigaTIND);

		}
	}
	
	public static void associazioneMappingNomeVal2(MappingTabella data) {
		// ciclo la lista degli elementi IND
		for (int j = 0; j < listFamiglia.size(); j++) {

			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			// ciclo per crea la listaCHIAVI e listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listFamiglia.get(j).getValori().get(parts[1]) != null)
						&& (listFamiglia.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listFamiglia.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}

			}

			// riga di tipo RIGATABELLA per IND
			RigaTabella rigaTIND = new RigaTabella();

			rigaTIND.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTIND.setListaattributi(listAttributi);
			//creo l'indirizzo univoco grazie dalla data d'inserimento
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			long time = cal.getTimeInMillis();
			
			rigaTIND.setUririga("http://dkm.fbk.eu/georeporter#IND_"+time);

			insertRiga(rigaTIND);

		}
	}

	public static void insertRiga(RigaTabella riga) {

		String targetURL = "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";

		Gson gson = new Gson();
		String json = gson.toJson(riga);
		//System.out.println(json);

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

		String path = "file/TN_file/DGASBANN.csv";
		String path2 = "file/TN_file/DGASBAN2.csv";
		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		WrapperIndirizzo.estrazioneHeaderFile(path);
		WrapperIndirizzo.LetturaFile(path);
		
		WrapperIndirizzo.estrazioneHeaderFile2(path2);
		WrapperIndirizzo.LetturaFile2(path2);
		
		// mapping e insert
		LoadFile(new File("file/file_mapping/mappingIndirizzo.json"));
		
		LoadFile2(new File("file/file_mapping/mappingIndirizzo2.json"));

	}

}
