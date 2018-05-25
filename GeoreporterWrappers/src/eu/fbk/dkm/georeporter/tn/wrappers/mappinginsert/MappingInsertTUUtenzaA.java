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

import javax.ws.rs.WebApplicationException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabelle;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UtenzaAcqua;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperTUUtenzaA;

public class MappingInsertTUUtenzaA {

	public  List<UtenzaAcqua> listUtenzaAcqua = WrapperTUUtenzaA.listUtenzaAcqua;

	
	public  String targetURL;
	public MappingInsertTUUtenzaA(String targetURL_) {
		
		targetURL=  targetURL_+"inserttable";
		
	}
	
	
	
	public  void LoadFile(File fileMappings) {

		Gson gson = new Gson();
		JsonReader reader;

		MappingTabella mappingUtenzaAcqua= new MappingTabella();
		MappingTabella mappingTributo_o_Utenza=new MappingTabella();
		MappingTabella mappingUtenza=new MappingTabella();
		MappingTabella mappingSoggetto=new MappingTabella();
		MappingTabella mappingPersonaFisica=new MappingTabella();
		MappingTabella mappingIndirizzoContribuente=new MappingTabella();
		MappingTabella mappingIndirizzoRecapitoContribuente=new MappingTabella();
		MappingTabella mappingIndirizzoUtenza=new MappingTabella();
		MappingTabella mappingIdentificativoCatastale=new MappingTabella();
	
		
		
		try {
			reader = new JsonReader(new FileReader(fileMappings));
			MappingTabelle mappings = gson.fromJson(reader, MappingTabelle.class);

			
			List<MappingTabella> listofMappings= mappings.getMappings();

			for (MappingTabella mappingTabella : listofMappings) {
				System.out.println(mappingTabella.getIdTabella().getNome());	
				if (mappingTabella.getIdTabella().getNome().equals("UtenzaAcqua")){
					mappingUtenzaAcqua=mappingTabella;
					
				}else if(mappingTabella.getIdTabella().getNome().equals("Utenza")){
					mappingUtenza=mappingTabella;
						
					
				}else if(mappingTabella.getIdTabella().getNome().equals("Tributo_o_Utenza")){
					mappingTributo_o_Utenza=mappingTabella;
					
					
				}else if(mappingTabella.getIdTabella().getNome().equals("Soggetto")){
					mappingSoggetto=mappingTabella;
					
				}else if(mappingTabella.getIdTabella().getNome().equals("PersonaFisica")){
					mappingPersonaFisica=mappingTabella;
					
				}else if(mappingTabella.getIdTabella().getNome().equals("IndirizzoContribuente")){
					mappingIndirizzoContribuente=mappingTabella;
					
				}else if(mappingTabella.getIdTabella().getNome().equals("IndirizzoRecapitoContribuente")){
					mappingIndirizzoRecapitoContribuente=mappingTabella;
					
				}else if(mappingTabella.getIdTabella().getNome().equals("IndirizzoUtenza")){
					mappingIndirizzoUtenza=mappingTabella;
					
				}else if(mappingTabella.getIdTabella().getNome().equals("IdentificativoCatastale")){
					mappingIdentificativoCatastale=mappingTabella;
							
				}
			}
			

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(mappingTributo_o_Utenza,mappingUtenza,mappingUtenzaAcqua,mappingSoggetto,mappingPersonaFisica,
					mappingIndirizzoContribuente,mappingIndirizzoRecapitoContribuente,mappingIndirizzoUtenza,mappingIdentificativoCatastale	);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	
	
	
	
	
	
	
	private  void LoadFile(File filename, File filename2, File filename3, File filename4, File filename5,
			File filename6, File filename7, File filename8, File filename9) {

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

		Gson gson7 = new Gson();
		JsonReader reader7;

		Gson gson8 = new Gson();
		JsonReader reader8;

		Gson gson9 = new Gson();
		JsonReader reader9;

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

			reader7 = new JsonReader(new FileReader(filename7));
			MappingTabella data7 = gson7.fromJson(reader7, MappingTabella.class);

			reader8 = new JsonReader(new FileReader(filename8));
			MappingTabella data8 = gson8.fromJson(reader8, MappingTabella.class);

			reader9 = new JsonReader(new FileReader(filename9));
			MappingTabella data9 = gson9.fromJson(reader9, MappingTabella.class);

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(data, data2, data3, data4, data5, data6, data7, data8, data9);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public  void associazioneMappingNomeVal(MappingTabella data, MappingTabella data2, MappingTabella data3,
			MappingTabella data4, MappingTabella data5, MappingTabella data6, MappingTabella data7,
			MappingTabella data8, MappingTabella data9) {
		// ciclo la lista degli elementi UtenzaRifiuti
		for (int j = 0; j < listUtenzaAcqua.size(); j++) {

			List<Attributo> listAttributi = new ArrayList<Attributo>();

			List<Attributo> listAttributiSOG = new ArrayList<Attributo>();
			List<Attributo> listChiaveSOG = new ArrayList<Attributo>();

			List<Attributo> listAttributiINDU = new ArrayList<Attributo>();
			List<Attributo> listAttributiINDC = new ArrayList<Attributo>();
			List<Attributo> listAttributiINDRC = new ArrayList<Attributo>();

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

				if ((listUtenzaAcqua.get(j).getValori().get(parts[1]) != null)
						&& (listUtenzaAcqua.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listUtenzaAcqua.get(j).getValori().get(parts[1]));
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

				if ((listUtenzaAcqua.get(j).getValori().get(parts2[1]) != null)
						&& (listUtenzaAcqua.get(j).getValori().get(parts2[1]).isEmpty() == false)) {
					tmp2.setValore(listUtenzaAcqua.get(j).getValori().get(parts2[1]));
					listAttributi.add(tmp2);
				}
			}

			// ciclo per crea e listaATTRIBUTI richiesti dal mapping Utenza Acqua
			for (int i = 0; i < data3.getAttributi().size(); i++) {

				String string3 = data3.getAttributi().get(i).getNome();
				String[] parts3 = string3.split("#");

				Attributo tmp3 = new Attributo();
				tmp3.setNome(data3.getAttributi().get(i).getNome());
				tmp3.setMapping(data3.getAttributi().get(i).getMapping());
				tmp3.setTipo(data3.getAttributi().get(i).getTipo());

				if ((listUtenzaAcqua.get(j).getValori().get(parts3[1]) != null)
						&& (listUtenzaAcqua.get(j).getValori().get(parts3[1]).isEmpty() == false)) {
					tmp3.setValore(listUtenzaAcqua.get(j).getValori().get(parts3[1]));
					listAttributi.add(tmp3);
				}
			}

			// ciclo per crea e listaATTRIBUTI richiesti dal mapping Utenza Acqua SOG
			for (int i = 0; i < data4.getAttributi().size(); i++) {

				String string4 = data4.getAttributi().get(i).getNome();
				String[] parts4 = string4.split("#");

				Attributo tmp4 = new Attributo();
				tmp4.setNome(data4.getAttributi().get(i).getNome());
				tmp4.setMapping(data4.getAttributi().get(i).getMapping());
				tmp4.setTipo(data4.getAttributi().get(i).getTipo());

				if ((listUtenzaAcqua.get(j).getValori().get(parts4[1]) != null)
						&& (listUtenzaAcqua.get(j).getValori().get(parts4[1]).isEmpty() == false)) {
					tmp4.setValore(listUtenzaAcqua.get(j).getValori().get(parts4[1]));
					listChiaveSOG.add(tmp4);
				}
			}
			for (int i = 0; i < data5.getAttributi().size(); i++) {

				String string5 = data5.getAttributi().get(i).getNome();
				String[] parts5 = string5.split("#");

				Attributo tmp5 = new Attributo();
				tmp5.setNome(data5.getAttributi().get(i).getNome());
				tmp5.setMapping(data5.getAttributi().get(i).getMapping());
				tmp5.setTipo(data5.getAttributi().get(i).getTipo());

				if ((listUtenzaAcqua.get(j).getValori().get(parts5[1]) != null)
						&& (listUtenzaAcqua.get(j).getValori().get(parts5[1]).isEmpty() == false)) {
					tmp5.setValore(listUtenzaAcqua.get(j).getValori().get(parts5[1]));
					listAttributiSOG.add(tmp5);
				}
			}

			for (int i = 0; i < data6.getAttributi().size(); i++) {

				String string6 = data6.getAttributi().get(i).getNome();
				String[] parts6 = string6.split("#");

				Attributo tmp6 = new Attributo();
				tmp6.setNome(data6.getAttributi().get(i).getNome());
				tmp6.setMapping(data6.getAttributi().get(i).getMapping());
				tmp6.setTipo(data6.getAttributi().get(i).getTipo());

				if ((listUtenzaAcqua.get(j).getValori().get(parts6[1]) != null)
						&& (listUtenzaAcqua.get(j).getValori().get(parts6[1]).isEmpty() == false)) {
					tmp6.setValore(listUtenzaAcqua.get(j).getValori().get(parts6[1]));
					listAttributiINDC.add(tmp6);
				}
			}
			// ciclio per creare listaAttributi richiesti dal mapping Utenza Acqua  
			for (int i = 0; i < data7.getAttributi().size(); i++) {

				String string7 = data7.getAttributi().get(i).getNome();
				String[] parts7 = string7.split("#");

				Attributo tmp7 = new Attributo();
				tmp7.setNome(data7.getAttributi().get(i).getNome());
				tmp7.setMapping(data7.getAttributi().get(i).getMapping());
				tmp7.setTipo(data7.getAttributi().get(i).getTipo());

				if ((listUtenzaAcqua.get(j).getValori().get(parts7[1]) != null)
						&& (listUtenzaAcqua.get(j).getValori().get(parts7[1]).isEmpty() == false)) {
					tmp7.setValore(listUtenzaAcqua.get(j).getValori().get(parts7[1]));
					listAttributiINDRC.add(tmp7);
				}
			}
			// ciclio per creare listaAttributi richiesti dal mapping Utenza Acqua  
			for (int i = 0; i < data8.getAttributi().size(); i++) {

				String string8 = data8.getAttributi().get(i).getNome();
				String[] parts8 = string8.split("#");

				Attributo tmp8 = new Attributo();
				tmp8.setNome(data8.getAttributi().get(i).getNome());
				tmp8.setMapping(data8.getAttributi().get(i).getMapping());
				tmp8.setTipo(data8.getAttributi().get(i).getTipo());

				if ((listUtenzaAcqua.get(j).getValori().get(parts8[1]) != null)
						&& (listUtenzaAcqua.get(j).getValori().get(parts8[1]).isEmpty() == false)) {
					tmp8.setValore(listUtenzaAcqua.get(j).getValori().get(parts8[1]));
					listAttributiINDU.add(tmp8);
				}
			}
			// ciclio per creare listaAttributi richiesti dal mapping Utenza Acqua IDE CAT
			for (int i = 0; i < data9.getAttributi().size(); i++) {

				String string9 = data9.getAttributi().get(i).getNome();
				String[] parts9 = string9.split("#");

				Attributo tmp9 = new Attributo();
				tmp9.setNome(data9.getAttributi().get(i).getNome());
				tmp9.setMapping(data9.getAttributi().get(i).getMapping());
				tmp9.setTipo(data9.getAttributi().get(i).getTipo());

				if ((listUtenzaAcqua.get(j).getValori().get(parts9[1]) != null)
						&& (listUtenzaAcqua.get(j).getValori().get(parts9[1]).isEmpty() == false)) {
					tmp9.setValore(listUtenzaAcqua.get(j).getValori().get(parts9[1]));
					listAttributiIDECAT.add(tmp9);
				}
				if ((listUtenzaAcqua.get(j).getValori().get(parts9[1]) != null)
						&& (listUtenzaAcqua.get(j).getValori().get(parts9[1]).isEmpty() == false)) {
					tmp9.setValore(listUtenzaAcqua.get(j).getValori().get(parts9[1]));
					listChiaveIDECAT.add(tmp9);
				}
			}

			// lista per le relazioni
			List<Relazione> listRelUA = new ArrayList<Relazione>();
			// ID UNIVOCO ??
			String id = listUtenzaAcqua.get(j).getValori().get("codutenza");

			// riga di tipo RIGATABELLA per PF
			if (listUtenzaAcqua.get(j).getValori().get("codfiscale").isEmpty() == false) {
				RigaTabella rigaTPF = new RigaTabella();
				
				rigaTPF.setListaattributi(listAttributiSOG);
				rigaTPF.setListachiave(listChiaveSOG);
				String codfis = listUtenzaAcqua.get(j).getValori().get("codfiscale");
				if (codfis.matches("[0-9]+")){
					rigaTPF.setNometabella("http://dkm.fbk.eu/georeporter#PersonaGiuridica");
				}else {
					rigaTPF.setNometabella("http://dkm.fbk.eu/georeporter#" + data5.getIdTabella().getMapping());
				}
				rigaTPF.setUririga("http://dkm.fbk.eu/georeporter#SOG_" + codfis);
				// inserimento dell'elemento
				insertRiga(rigaTPF);
			}
			// riga di tipo RIGATABELLA per IND C
			if (listUtenzaAcqua.get(j).getValori().get("indirizzocontribuente").isEmpty() == false) {
				RigaTabella rigaTINDC = new RigaTabella();
				rigaTINDC.setNometabella("http://dkm.fbk.eu/georeporter#" + data6.getIdTabella().getMapping());
				rigaTINDC.setListaattributi(listAttributiINDC);
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				long time = cal.getTimeInMillis();
				rigaTINDC.setUririga("http://dkm.fbk.eu/georeporter#IND_" + time);
				// inserimento dell'elemento
				insertRiga(rigaTINDC);

				// relazione TributiUtenza UtenzaRifiuti con Indirizzo Contribuente
				Relazione rel = new Relazione();
				rel.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIndirizzoContribuente");
				rel.setUriDomain("http://dkm.fbk.eu/georeporter#TUUA_" + id);
				// ID
				// creo l'indirizzo univoco grazie dalla data d'inserimento
				rel.setUriRange("http://dkm.fbk.eu/georeporter#IND_" + time);
				listRelUA.add(rel);
			}
			// riga di tipo RIGATABELLA per IND R
			if (listUtenzaAcqua.get(j).getValori().get("indirizzorecapitocontribuente").isEmpty() == false) {
				RigaTabella rigaTINDRC = new RigaTabella();
				rigaTINDRC.setNometabella("http://dkm.fbk.eu/georeporter#" + data7.getIdTabella().getMapping());
				rigaTINDRC.setListaattributi(listAttributiINDRC);
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				long time = cal.getTimeInMillis();
				rigaTINDRC.setUririga("http://dkm.fbk.eu/georeporter#IND_" + time);
				// inserimento dell'elemento
				insertRiga(rigaTINDRC);

				// relazione TributiUtenza UtenzaRifiuti con Indirizzo Recapito Contribuente
				Relazione rel = new Relazione();
				rel.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIndirizzoRecapitoContribuente");
				rel.setUriDomain("http://dkm.fbk.eu/georeporter#TUUA_" + id);
				// ID
				// creo l'indirizzo univoco grazie dalla data d'inserimento
				rel.setUriRange("http://dkm.fbk.eu/georeporter#IND_" + time);
				listRelUA.add(rel);
			}
			// riga di tipo RIGATABELLA per IND U
			if (listUtenzaAcqua.get(j).getValori().get("indirizzo").isEmpty() == false) {
				RigaTabella rigaTINDU = new RigaTabella();
				rigaTINDU.setNometabella("http://dkm.fbk.eu/georeporter#" + data8.getIdTabella().getMapping());
				rigaTINDU.setListaattributi(listAttributiINDU);
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				long time = cal.getTimeInMillis();
				rigaTINDU.setUririga("http://dkm.fbk.eu/georeporter#IND_" + time);
				// inserimento dell'elemento
				insertRiga(rigaTINDU);

				// relazione TributiUtenza UtenzaRifiuti con Indirizzo Utenza
				Relazione rel = new Relazione();
				rel.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIndirizzoUtenza");
				rel.setUriDomain("http://dkm.fbk.eu/georeporter#TUUA_" + id);
				// ID conribuente
				// creo l'indirizzo univoco grazie dalla data d'inserimento
				rel.setUriRange("http://dkm.fbk.eu/georeporter#IND_" + time);
				listRelUA.add(rel);
			}

			String num = listUtenzaAcqua.get(j).getValori().get("particellaedificabile");
			if (!num.isEmpty()) {
				// riga di tipo RIGATABELLA per IDE CAT
				RigaTabella rigaTIDECAT = new RigaTabella();
				rigaTIDECAT.setNometabella("http://dkm.fbk.eu/georeporter#" + data9.getIdTabella().getMapping());
				rigaTIDECAT.setListaattributi(listAttributiIDECAT);
				rigaTIDECAT.setListachiave(listChiaveIDECAT);
				String cc = listUtenzaAcqua.get(j).getValori().get("codcomune");
				String den = listUtenzaAcqua.get(j).getValori().get("particellaestensione");
				String sub = listUtenzaAcqua.get(j).getValori().get("subalterno");
				if (num.contains("/")) {
					
					String[] parts=num.split("/");
					num=parts[0];
					den=parts[1];
				}
				if (sub.equals("0")){
					sub="";
				}
				rigaTIDECAT.setUririga("http://dkm.fbk.eu/georeporter#C" + cc + "_N" + num + "_D" + den + "_S" + sub);
				// inserimento dell'elemento
				insertRiga(rigaTIDECAT);

				Relazione rel = new Relazione();
				rel.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIdentificativoCatastale");
				rel.setUriDomain("http://dkm.fbk.eu/georeporter#TUUA_" + id);
				// ID
				if (sub.equals("0")){
					sub="";
				}
				rel.setUriRange("http://dkm.fbk.eu/georeporter#C" + cc + "_N" + num + "_D" + den + "_S" + sub);
				listRelUA.add(rel);
			}

			// riga di tipo RIGATABELLA per UTENZA ACUQA
			RigaTabella rigaTUA = new RigaTabella();
			rigaTUA.setNometabella("http://dkm.fbk.eu/georeporter#UtenzaAcqua");
			rigaTUA.setListaattributi(listAttributi);
			rigaTUA.setUririga("http://dkm.fbk.eu/georeporter#TUUA_" + id);

			// relazione TributiUtenza UtenzaAcqua con CONTRIBUENTE
			if (listUtenzaAcqua.get(j).getValori().get("codfiscale").isEmpty() == false) {
				Relazione rel = new Relazione();
				rel.setNomerelazione("http://dkm.fbk.eu/georeporter#hasContribuente");
				rel.setUriDomain("http://dkm.fbk.eu/georeporter#TUUA_" + id);
				// ID contribuente
				String codfisc = listUtenzaAcqua.get(j).getValori().get("codfiscale");
				rel.setUriRange("http://dkm.fbk.eu/georeporter#SOG_" + codfisc);
				listRelUA.add(rel);
			}

			rigaTUA.setListarelazioni(listRelUA);

			insertRiga(rigaTUA);

		}
	}

	
	
	
	public  void insertRiga(RigaTabella riga) {

		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
	//	String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";	
	
		Gson gson = new Gson();
		String json = gson.toJson(riga);
           try {
			URL targetUrl = new URL(targetURL);
		
           ClientConfig clientConfig = new DefaultClientConfig();
           clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
           Client client = Client.create(clientConfig);

           WebResource webResourcePost = client.resource(targetURL);
           ClientResponse  response = webResourcePost.type("application/json").post(ClientResponse.class, json);
         
           if (response.getStatus() != 200) {
           	 WebApplicationException e = response.getEntity(WebApplicationException.class);
           	 System.out.println(e.toString());
          }
           String responseEntity = response.getEntity(String.class);
           
          
          // System.out.println(responseEntity.toString());
           
           client.destroy();
           } catch (MalformedURLException e1) {
   			// TODO Auto-generated catch block
   			e1.printStackTrace();
   		}
           
	   }
    
	
	
	
	public  void insertRiga_old(RigaTabella riga) {

		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
	//	String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";

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
	public  void run(String filePath, String fileMappings) {

		
	//	String path = "file/TN_file/TRAMBILENO_Utenze_H2O_DA GARBAGE.xls";
		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		try {
			WrapperTUUtenzaA.readXLSFile(filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// mapping e insert
		LoadFile(new File(fileMappings) );
			

		
	}
	public  void main(String[] args) {

		String path = "file/TN_file/TRAMBILENO_Utenze_H2O_DA GARBAGE.xls";
		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		try {
			WrapperTUUtenzaA.readXLSFile(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// mapping e insert
		LoadFile(new File("file/file_mapping/mappingTributoOUtenza.json"),
				new File("file/file_mapping/mappingUtenza.json"),
				new File("file/file_mapping/mappingUtenzaAcqua.json"),
				new File("file/file_mapping/mappingSoggetto.json"),
				new File("file/file_mapping/mappingPersonaFisicaCONTRIBUENTEua.json"),
				new File("file/file_mapping/mappingIndirizzoCONTRIBUENTE.json"),
				new File("file/file_mapping/mappingIndirizzoRECAPITOCONTRIBUENTE.json"),
				new File("file/file_mapping/mappingIndirizzoUTENZA.json"),
				new File("file/file_mapping/mappingIdentificativoCatastale2.json"));

		
		
	}

}
