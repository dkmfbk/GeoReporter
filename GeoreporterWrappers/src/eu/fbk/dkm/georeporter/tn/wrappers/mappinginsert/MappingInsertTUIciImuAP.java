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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.AbitazionePrincipale;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.ControlloValore;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperForLoc;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperTUIciImuAP;

public class MappingInsertTUIciImuAP {

	public static List<AbitazionePrincipale> listICIIMU_AP = WrapperTUIciImuAP.listICIIMU_AP;

	private static void LoadFile(File filename, File filename2, File filename3, File filename4, File filename5,
			File filename6) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		Gson gson3 = new Gson();
		JsonReader reader3;

		Gson gson4 = new Gson();
		JsonReader reader4;

		Gson gson5 = new Gson();
		JsonReader reader5;

		Gson gson6 = new Gson();
		JsonReader reader6;

		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(filename2));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);

			reader3 = new JsonReader(new FileReader(filename3));
			MappingTabella data3 = gson3.fromJson(reader3, MappingTabella.class);

			reader4 = new JsonReader(new FileReader(filename4));
			MappingTabella data4 = gson4.fromJson(reader4, MappingTabella.class);

			reader5 = new JsonReader(new FileReader(filename5));
			MappingTabella data5 = gson5.fromJson(reader5, MappingTabella.class);

			reader6 = new JsonReader(new FileReader(filename6));
			MappingTabella data6 = gson6.fromJson(reader6, MappingTabella.class);

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(data, data2, data3, data4, data5, data6);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void associazioneMappingNomeVal(MappingTabella data, MappingTabella data2, MappingTabella data3,
			MappingTabella data4, MappingTabella data5, MappingTabella data6) {
		// ciclo la lista degli elementi UtenzaRifiuti
		for (int j = 0; j < listICIIMU_AP.size(); j++) {

			List<Attributo> listAttributi = new ArrayList<Attributo>();

			List<Attributo> listAttributiSOG = new ArrayList<Attributo>();
			List<Attributo> listChiaveSOG = new ArrayList<Attributo>();

			List<Attributo> listAttributiIND = new ArrayList<Attributo>();
			List<Attributo> listChiaveIND = new ArrayList<Attributo>();

			List<Attributo> listAttributiIDECAT = new ArrayList<Attributo>();
			List<Attributo> listChiaveIDECAT = new ArrayList<Attributo>();

			// ciclo per crea listaATTRIBUTI richiesti dal mapping Tributo o Utenza
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listICIIMU_AP.get(j).getValori().get(parts[1]) != null)
						&& (listICIIMU_AP.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					if (parts[1].contains("data")) {
						tmp.setValore(ControlloValore.controlloData(listICIIMU_AP.get(j).getValori().get(parts[1])));
					} else {
						tmp.setValore(listICIIMU_AP.get(j).getValori().get(parts[1]));
					}
					listAttributi.add(tmp);
				}
			}

			// ciclo per crea e listaATTRIBUTI richiesti dal mapping Utenza
			for (int i = 0; i < data2.getAttributi().size(); i++) {

				String string2 = data2.getAttributi().get(i).getNome();
				String[] parts2 = string2.split("#");

				Attributo tmp2 = new Attributo();
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());

				if ((listICIIMU_AP.get(j).getValori().get(parts2[1]) != null)
						&& (listICIIMU_AP.get(j).getValori().get(parts2[1]).isEmpty() == false)) {
					if (parts2[1].contains("data")) {
						tmp2.setValore(ControlloValore.controlloData(listICIIMU_AP.get(j).getValori().get(parts2[1])));
					} else {
						tmp2.setValore(listICIIMU_AP.get(j).getValori().get(parts2[1]));
					}
					listAttributi.add(tmp2);
				}
			}
			// ciclo per crea e listaATTRIBUTI richiesti dal mapping 0 SOG
			for (int i = 0; i < data3.getAttributi().size(); i++) {
				String string3 = data3.getAttributi().get(i).getNome();
				String[] parts3 = string3.split("#");

				Attributo tmp3 = new Attributo();
				tmp3.setNome(data3.getAttributi().get(i).getNome());
				tmp3.setMapping(data3.getAttributi().get(i).getMapping());
				tmp3.setTipo(data3.getAttributi().get(i).getTipo());

				if ((listICIIMU_AP.get(j).getValori().get(parts3[1]) != null)
						&& (listICIIMU_AP.get(j).getValori().get(parts3[1]).isEmpty() == false)) {
					if (parts3[1].contains("data")) {
						tmp3.setValore(ControlloValore.controlloData(listICIIMU_AP.get(j).getValori().get(parts3[1])));
					} else {
						tmp3.setValore(listICIIMU_AP.get(j).getValori().get(parts3[1]));
					}
					listChiaveSOG.add(tmp3);
				}
			}
			for (int i = 0; i < data4.getAttributi().size(); i++) {
				String string4 = data4.getAttributi().get(i).getNome();
				String[] parts4 = string4.split("#");

				Attributo tmp4 = new Attributo();
				tmp4.setNome(data4.getAttributi().get(i).getNome());
				tmp4.setMapping(data4.getAttributi().get(i).getMapping());
				tmp4.setTipo(data4.getAttributi().get(i).getTipo());

				if ((listICIIMU_AP.get(j).getValori().get(parts4[1]) != null)
						&& (listICIIMU_AP.get(j).getValori().get(parts4[1]).isEmpty() == false)) {
					if (parts4[1].contains("data")) {
						tmp4.setValore(ControlloValore.controlloData(listICIIMU_AP.get(j).getValori().get(parts4[1])));
					} else {
						tmp4.setValore(listICIIMU_AP.get(j).getValori().get(parts4[1]));
					}
					listAttributiSOG.add(tmp4);
				}
			}

			for (int i = 0; i < data5.getAttributi().size(); i++) {

				String string5 = data5.getAttributi().get(i).getNome();
				String[] parts5 = string5.split("#");

				Attributo tmp5 = new Attributo();
				tmp5.setNome(data5.getAttributi().get(i).getNome());
				tmp5.setMapping(data5.getAttributi().get(i).getMapping());
				tmp5.setTipo(data5.getAttributi().get(i).getTipo());

				if ((listICIIMU_AP.get(j).getValori().get(parts5[1]) != null)
						&& (listICIIMU_AP.get(j).getValori().get(parts5[1]).isEmpty() == false)) {
					if (parts5[1].contains("data")) {
						tmp5.setValore(ControlloValore.controlloData(listICIIMU_AP.get(j).getValori().get(parts5[1])));
					} else {
						tmp5.setValore(listICIIMU_AP.get(j).getValori().get(parts5[1]));
					}
					listAttributiIND.add(tmp5);
				}
				if ((listICIIMU_AP.get(j).getValori().get(parts5[1]) != null)
						&& (listICIIMU_AP.get(j).getValori().get(parts5[1]).isEmpty() == false)) {
					if (parts5[1].contains("data")) {
						tmp5.setValore(ControlloValore.controlloData(listICIIMU_AP.get(j).getValori().get(parts5[1])));
					} else {
						tmp5.setValore(listICIIMU_AP.get(j).getValori().get(parts5[1]));
					}
					listChiaveIND.add(tmp5);
				}
			}
			// ciclio per creare listaAttributi richiesti dal mapping Utenza Acqua IDE CAT
			for (int i = 0; i < data6.getAttributi().size(); i++) {

				String string6 = data6.getAttributi().get(i).getNome();
				String[] parts6 = string6.split("#");

				Attributo tmp6 = new Attributo();
				tmp6.setNome(data6.getAttributi().get(i).getNome());
				tmp6.setMapping(data6.getAttributi().get(i).getMapping());
				tmp6.setTipo(data6.getAttributi().get(i).getTipo());

				if ((listICIIMU_AP.get(j).getValori().get(parts6[1]) != null)
						&& (listICIIMU_AP.get(j).getValori().get(parts6[1]).isEmpty() == false)) {
					if (parts6[1].contains("data")) {
						tmp6.setValore(ControlloValore.controlloData(listICIIMU_AP.get(j).getValori().get(parts6[1])));
					} else {
						tmp6.setValore(listICIIMU_AP.get(j).getValori().get(parts6[1]));
					}
					listAttributiIDECAT.add(tmp6);
				}
				if ((listICIIMU_AP.get(j).getValori().get(parts6[1]) != null)
						&& (listICIIMU_AP.get(j).getValori().get(parts6[1]).isEmpty() == false)) {
					if (parts6[1].contains("data")) {
						tmp6.setValore(ControlloValore.controlloData(listICIIMU_AP.get(j).getValori().get(parts6[1])));
					} else {
						tmp6.setValore(listICIIMU_AP.get(j).getValori().get(parts6[1]));
					}
					listChiaveIDECAT.add(tmp6);
				}
			}

			// riga di tipo RIGATABELLA per PF
			if (listICIIMU_AP.get(j).getValori().get("codfiscale").isEmpty() == false) {
				RigaTabella rigaTPF = new RigaTabella();
				rigaTPF.setNometabella("http://dkm.fbk.eu/georeporter#" + data4.getIdTabella().getMapping());
				rigaTPF.setListaattributi(listAttributiSOG);
				rigaTPF.setListachiave(listChiaveSOG);
				String codfis = listICIIMU_AP.get(j).getValori().get("codicefiscale");
				rigaTPF.setUririga("http://dkm.fbk.eu/georeporter#SOG_" + codfis);
				// inserimento dell'elemento
				insertRiga(rigaTPF);
			}

			// riga di tipo RIGATABELLA per IND
			if (listICIIMU_AP.get(j).getValori().get("indirizzo").isEmpty() == false) {
				RigaTabella rigaTIND = new RigaTabella();
				rigaTIND.setNometabella("http://dkm.fbk.eu/georeporter#" + data5.getIdTabella().getMapping());
				rigaTIND.setListaattributi(listAttributiIND);
				rigaTIND.setListachiave(listChiaveIND);
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				long time = cal.getTimeInMillis();
				rigaTIND.setUririga("http://dkm.fbk.eu/georeporter#IND_" + time);
				// inserimento dell'elemento
				insertRiga(rigaTIND);
			}

			// riga di tipo RIGATABELLA per IDE CAT
				RigaTabella rigaTIDECAT = new RigaTabella();
				rigaTIDECAT.setNometabella("http://dkm.fbk.eu/georeporter#" + data6.getIdTabella().getMapping());
				rigaTIDECAT.setListaattributi(listAttributiIDECAT);
				rigaTIDECAT.setListachiave(listChiaveIDECAT);
				String cc = listICIIMU_AP.get(j).getValori().get("codcomune");
				String num = listICIIMU_AP.get(j).getValori().get("particellaedificabile");
				String den = "";
				String[] numden = num.split("/", -1);
				if (numden.length == 2) {
					num = numden[0];
					den = numden[1];
				}
				String sub = listICIIMU_AP.get(j).getValori().get("subalterno");
				if (listICIIMU_AP.get(j).getValori().get("particellaedificabile").isEmpty()) {
					num = "";
					den = "";
					sub = "";
				}
				rigaTIDECAT.setUririga("http://dkm.fbk.eu/georeporter#C" + cc + "_N" + num + "_D" + den + "_S" + sub);
				// inserimento dell'elemento
				insertRiga(rigaTIDECAT);

			// riga di tipo RIGATABELLA per AbitazionePrincipale

			RigaTabella rigaTUA = new RigaTabella();
			rigaTUA.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTUA.setListaattributi(listAttributi);
			// ID UNIVOCO ??
			String id = listICIIMU_AP.get(j).getValori().get("codutenza");
			rigaTUA.setUririga("http://dkm.fbk.eu/georeporter#UTAP_" + id);

			// lista per le relazioni
			List<Relazione> listRelUA = new ArrayList<Relazione>();

			// relazione TributiUtenza AbitazionePrincipale con CONTRIBUENTE
			if (listICIIMU_AP.get(j).getValori().get("codfiscale").isEmpty() == false) {
				Relazione rel = new Relazione();
				rel.setNomerelazione("http://dkm.fbk.eu/georeporter#hasContribuente");
				rel.setUriDomain("http://dkm.fbk.eu/georeporter#UTAP_" + id);
				// ID contribuente
				String codfisc = listICIIMU_AP.get(j).getValori().get("codfiscale");
				rel.setUriRange("http://dkm.fbk.eu/georeporter#SOG_" + codfisc);
				listRelUA.add(rel);
			}

			// relazione TributiUtenza AbitazionePrincipale con Identificativo Catastale
			if (listICIIMU_AP.get(j).getValori().get("particellaedificabile").isEmpty() == false) {
				Relazione rel = new Relazione();
				rel.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIdentifCatastale");
				rel.setUriDomain("http://dkm.fbk.eu/georeporter#UTAP_" + id);
				// ID
				rel.setUriRange("http://dkm.fbk.eu/georeporter#C" + cc + "_N" + num + "_D" + den + "_S" + sub);
				listRelUA.add(rel);
			}

			// relazione TributiUtenza AbitazionePrincipale con Indirizzo Utenza
			if (listICIIMU_AP.get(j).getValori().get("indirizzo").isEmpty() == false) {
				Relazione rel = new Relazione();
				rel.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIndirizzoUtenza");
				rel.setUriDomain("http://dkm.fbk.eu/georeporter#UTAP_" + id);
				// ID conribuente
				// creo l'indirizzo univoco grazie dalla data d'inserimento
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				long time = cal.getTimeInMillis();
				rel.setUriRange("http://dkm.fbk.eu/georeporter#IND_" + time);
				listRelUA.add(rel);
			}

			// relazione TributiUtenza AbitazionePrincipale con Indirizzo Recapito
			// Contribuente
			if (listICIIMU_AP.get(j).getValori().get("indirizzo").isEmpty() == false) {
				Relazione rel = new Relazione();
				rel.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIndirizzoRecapitoContribuente");
				rel.setUriDomain("http://dkm.fbk.eu/georeporter#UTAP_" + id);
				// ID
				// creo l'indirizzo univoco grazie dalla data d'inserimento
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				long time = cal.getTimeInMillis();
				rel.setUriRange("http://dkm.fbk.eu/georeporter#IND_" + time);
				listRelUA.add(rel);
			}

			// relazione TributiUtenza AbitazionePrincipale con Indirizzo Contribuente
			if (listICIIMU_AP.get(j).getValori().get("indirizzo").isEmpty() == false) {
				Relazione rel = new Relazione();
				rel.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIndirizzoContribuente");
				rel.setUriDomain("http://dkm.fbk.eu/georeporter#UTAP_" + id);
				// ID
				// creo l'indirizzo univoco grazie dalla data d'inserimento
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				long time = cal.getTimeInMillis();
				rel.setUriRange("http://dkm.fbk.eu/georeporter#IND_" + time);
				listRelUA.add(rel);
			}

			rigaTUA.setListarelazioni(listRelUA);

			insertRiga(rigaTUA);

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

		String path = "file/TN_file/TRAMBILENO_Utenze_I_C_I__I_M_U_ABPRINCIPALE.xls";
		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		try {
			WrapperTUIciImuAP.readXLSFile(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mapping e insert
		LoadFile(new File("file/file_mapping/mappingICIIMU.json"),
				new File("file/file_mapping/mappingTributoOUtenza.json"),
				new File("file/file_mapping/mappingSoggetto.json"),
				new File("file/file_mapping/mappingPersonaFisica.json"),
				new File("file/file_mapping/mappingIndirizzo.json"),
				new File("file/file_mapping/mappingIdentificativoCatastale.json"));

	}

}
