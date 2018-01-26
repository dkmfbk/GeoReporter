package eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.AnagraficaComunale;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Famiglia;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.FornituraGas;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Titolarita;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.tn.wrappers.ControlloValore;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperAnagraficaComunale;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperForEne;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperForGas;

public class MappingInsertForGas {

	public static List<FornituraGas> listFornituraGas = WrapperForGas.listFornituraGas;

	private static void LoadFile(File filename, File filename2) {

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
			associazioneMappingNomeVal(data, data2);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void associazioneMappingNomeVal(MappingTabella data, MappingTabella data2) {
		// ciclo la lista degli elementi FG
		for (int j = 0; j < listFornituraGas.size(); j++) {

			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			// ciclo per crea listaATTRIBUTI richiesti dal mapping FG
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listFornituraGas.get(j).getValori().get(parts[1]) != null)
						&& (listFornituraGas.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listFornituraGas.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}
			}

			// ciclo per crea e listaATTRIBUTI richiesti dal mapping Contratto
			for (int i = 0; i < data2.getAttributi().size(); i++) {

				String string2 = data2.getAttributi().get(i).getNome();
				String[] parts2 = string2.split("#");

				Attributo tmp2 = new Attributo();
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());

				if ((listFornituraGas.get(j).getValori().get(parts2[1]) != null)
						&& (listFornituraGas.get(j).getValori().get(parts2[1]).isEmpty() == false)) {
					tmp2.setValore((listFornituraGas.get(j).getValori().get(parts2[1])));
					listAttributi.add(tmp2);
				}
			}

			// riga di tipo RIGATABELLA per FOR GAS
			RigaTabella rigaTFG = new RigaTabella();
			rigaTFG.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTFG.setListaattributi(listAttributi);
			String idcfg = listFornituraGas.get(j).getValori().get("idsiatelgasdettaglio").trim();
			rigaTFG.setUririga("http://dkm.fbk.eu/georeporter#CFG_" + idcfg);

			List<Relazione> listRelCFG = new ArrayList<Relazione>();
			// relazione FOR GAS con SOG
			String codfsog = listFornituraGas.get(j).getValori().get("codfiscaletitolareutenza").trim();
			if (!codfsog.isEmpty()) {
				Relazione relCFGSOG = new Relazione();
				relCFGSOG.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTitolareContratto");
				relCFGSOG.setUriDomain("http://dkm.fbk.eu/georeporter#CFG_" + idcfg);
				relCFGSOG.setUriRange("http://dkm.fbk.eu/georeporter#SOG_" + codfsog);
				listRelCFG.add(relCFGSOG);
			}
			// relazione FOR GAS con INDIRIZZO
			if (listFornituraGas.get(j).getValori().get("indirizzoutenza").isEmpty() == false) {
				Relazione relCFGIND = new Relazione();
				relCFGIND.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIndirizzoUtenza");
				relCFGIND.setUriDomain("http://dkm.fbk.eu/georeporter#CFG_" + idcfg);
				// creo l'indirizzo univoco grazie dalla data d'inserimento
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				long time = cal.getTimeInMillis();
				relCFGIND.setUriRange("http://dkm.fbk.eu/georeporter#IND_" + time);
				listRelCFG.add(relCFGIND);
			}
			rigaTFG.setListarelazioni(listRelCFG);

			insertRiga(rigaTFG);

		}
	}

	public static void insertRiga(RigaTabella riga) {

		//String targetURL = "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
		String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";


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
	public static void run()
	{
		String path = "file/TN_file/trambileno_Fornitura_Gas_dettaglio.xls";
		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		try {
			WrapperForGas.readXLSFile(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mapping e insert
		LoadFile(new File("file/file_mapping/mappingFornituraGas.json"),
				new File("file/file_mapping/mappingContratto.json"));
		
	}
	public static void main(String[] args) {

		String path = "file/TN_file/trambileno_Fornitura_Gas_dettaglio.xls";
		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		try {
			WrapperForGas.readXLSFile(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mapping e insert
		LoadFile(new File("file/file_mapping/mappingFornituraGas.json"),
				new File("file/file_mapping/mappingContratto.json"));

	}

}
