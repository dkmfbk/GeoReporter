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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.ws.rs.WebApplicationException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.IDCatastale;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabelle;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.NudaProprieta;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperTUIciImuAP;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperTUIciImuNP;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperTUUtenzaR;

public class MappingInsertTUIciImuNP {

	
	public WrapperTUIciImuNP wiciNP = new WrapperTUIciImuNP();
	public  List<NudaProprieta> listNudaProprieta ;

	public  String targetURL;
	public MappingInsertTUIciImuNP(String targetURL_) {
		
		targetURL=  targetURL_+"inserttable";
		
	}
	
	private Logger log = Logger.getLogger(MappingInsertTUIciImuNP.class);
	public  void LoadFile(File fileMappings) {

		Gson gson = new Gson();
		JsonReader reader;

		MappingTabella mappingICI_IMU= new MappingTabella();
		MappingTabella mappingTributo_o_Utenza=new MappingTabella();
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
				if (mappingTabella.getIdTabella().getNome().equals("ICI_IMU")){
					mappingICI_IMU=mappingTabella;
					
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
			associazioneMappingNomeVal(mappingICI_IMU,mappingTributo_o_Utenza,mappingSoggetto,mappingPersonaFisica,
					mappingIndirizzoContribuente,mappingIndirizzoRecapitoContribuente,mappingIndirizzoUtenza,mappingIdentificativoCatastale	);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
	
	
	private  void LoadFile_old(File filename, File filename2, File filename3, File filename4, File filename5,
			File filename6, File filename7, File filename8) {

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
			MappingTabella data7 = gson5.fromJson(reader7, MappingTabella.class);

			reader8 = new JsonReader(new FileReader(filename8));
			MappingTabella data8 = gson8.fromJson(reader8, MappingTabella.class);

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(data, data2, data3, data4, data5, data6, data7, data8);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public  void associazioneMappingNomeVal(MappingTabella data, MappingTabella data2, MappingTabella data3,
			MappingTabella data4, MappingTabella data5, MappingTabella data6, MappingTabella data7,
			MappingTabella data8) {
	
		
		
		
		Map<String,String> nameMappingsIdentificativoCatastaleHM = new HashMap<String,String>();
		Map<String,String> namePersonaFisicaHM = new HashMap<String,String>();
		Map<String,String> nameIndirizzoContribuenteHM = new HashMap<String,String>();
		
	      
		
		
		for (int i = 0; i < data8.getAttributi().size(); i++) {
			nameMappingsIdentificativoCatastaleHM.put(data8.getAttributi().get(i).getMapping().split("#")[1], 
					data8.getAttributi().get(i).getNome().split("#")[1]);
		
		}

		for (int i = 0; i < data4.getAttributi().size(); i++) {
			namePersonaFisicaHM.put(data4.getAttributi().get(i).getMapping().split("#")[1], 
					data4.getAttributi().get(i).getNome().split("#")[1]);
		
		}

		
		for (int i = 0; i < data5.getAttributi().size(); i++) {
			nameIndirizzoContribuenteHM.put(data5.getAttributi().get(i).getMapping().split("#")[1],
					data5.getAttributi().get(i).getNome().split("#")[1]);
		
		}
		
		
		
		
		
		// ciclo la lista degli elementi UtenzaRifiuti
		for (int j = 0; j < listNudaProprieta.size(); j++) {

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

				if ((listNudaProprieta.get(j).getValori().get(parts[1]) != null)
						&& (listNudaProprieta.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listNudaProprieta.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}
			}

			// ciclo per crea e listaATTRIBUTI richiesti dal mapping Nuda Proprieta
			for (int i = 0; i < data2.getAttributi().size(); i++) {

				String string2 = data2.getAttributi().get(i).getNome();
				String[] parts2 = string2.split("#");

				Attributo tmp2 = new Attributo();
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());

				if ((listNudaProprieta.get(j).getValori().get(parts2[1]) != null)
						&& (listNudaProprieta.get(j).getValori().get(parts2[1]).isEmpty() == false)) {
					tmp2.setValore(listNudaProprieta.get(j).getValori().get(parts2[1]));
					listAttributi.add(tmp2);
				}
			}
			// ciclo per crea e listaATTRIBUTI richiesti dal mapping SOG
			for (int i = 0; i < data3.getAttributi().size(); i++) {
				String string3 = data3.getAttributi().get(i).getNome();
				String[] parts3 = string3.split("#");

				Attributo tmp3 = new Attributo();
				tmp3.setNome(data3.getAttributi().get(i).getNome());
				tmp3.setMapping(data3.getAttributi().get(i).getMapping());
				tmp3.setTipo(data3.getAttributi().get(i).getTipo());

				if ((listNudaProprieta.get(j).getValori().get(parts3[1]) != null)
						&& (listNudaProprieta.get(j).getValori().get(parts3[1]).isEmpty() == false)) {
					tmp3.setValore(listNudaProprieta.get(j).getValori().get(parts3[1]));
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

				if ((listNudaProprieta.get(j).getValori().get(parts4[1]) != null)
						&& (listNudaProprieta.get(j).getValori().get(parts4[1]).isEmpty() == false)) {
					tmp4.setValore(listNudaProprieta.get(j).getValori().get(parts4[1]));
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

				if ((listNudaProprieta.get(j).getValori().get(parts5[1]) != null)
						&& (listNudaProprieta.get(j).getValori().get(parts5[1]).isEmpty() == false)) {
					tmp5.setValore(listNudaProprieta.get(j).getValori().get(parts5[1]));
					listAttributiINDC.add(tmp5);
				}
			}

			for (int i = 0; i < data6.getAttributi().size(); i++) {

				String string6 = data6.getAttributi().get(i).getNome();
				String[] parts6 = string6.split("#");

				Attributo tmp6 = new Attributo();
				tmp6.setNome(data6.getAttributi().get(i).getNome());
				tmp6.setMapping(data6.getAttributi().get(i).getMapping());
				tmp6.setTipo(data6.getAttributi().get(i).getTipo());

				if ((listNudaProprieta.get(j).getValori().get(parts6[1]) != null)
						&& (listNudaProprieta.get(j).getValori().get(parts6[1]).isEmpty() == false)) {
					tmp6.setValore(listNudaProprieta.get(j).getValori().get(parts6[1]));
					listAttributiIDECAT.add(tmp6);
				}
				if ((listNudaProprieta.get(j).getValori().get(parts6[1]) != null)
						&& (listNudaProprieta.get(j).getValori().get(parts6[1]).isEmpty() == false)) {
					tmp6.setValore(listNudaProprieta.get(j).getValori().get(parts6[1]));
					listAttributiINDRC.add(tmp6);
				}
			}
			// ciclio per creare listaAttributi richiesti dal mapping Utenza Acqua IDE CAT
			for (int i = 0; i < data7.getAttributi().size(); i++) {

				String string7 = data7.getAttributi().get(i).getNome();
				String[] parts7 = string7.split("#");

				Attributo tmp7 = new Attributo();
				tmp7.setNome(data7.getAttributi().get(i).getNome());
				tmp7.setMapping(data7.getAttributi().get(i).getMapping());
				tmp7.setTipo(data7.getAttributi().get(i).getTipo());

				if ((listNudaProprieta.get(j).getValori().get(parts7[1]) != null)
						&& (listNudaProprieta.get(j).getValori().get(parts7[1]).isEmpty() == false)) {
					tmp7.setValore(listNudaProprieta.get(j).getValori().get(parts7[1]));
					listAttributiINDU.add(tmp7);
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

				if ((listNudaProprieta.get(j).getValori().get(parts8[1]) != null)
						&& (listNudaProprieta.get(j).getValori().get(parts8[1]).isEmpty() == false)) {
					tmp8.setValore(listNudaProprieta.get(j).getValori().get(parts8[1]));
					listAttributiIDECAT.add(tmp8);
				}
				if ((listNudaProprieta.get(j).getValori().get(parts8[1]) != null)
						&& (listNudaProprieta.get(j).getValori().get(parts8[1]).isEmpty() == false)) {
					tmp8.setValore(listNudaProprieta.get(j).getValori().get(parts8[1]));
					listChiaveIDECAT.add(tmp8);
				}
			}
			// lista per le relazioni
			List<Relazione> listRelNP = new ArrayList<Relazione>();
			// ID UNIVOCO ??
			String id = listNudaProprieta.get(j).getValori().get("codutenza");

			// riga di tipo RIGATABELLA per PF
			if (listNudaProprieta.get(j).getValori().get(namePersonaFisicaHM.get("codiceFiscale")).isEmpty() == false) {
				RigaTabella rigaTPF = new RigaTabella();
				rigaTPF.setNometabella("http://dkm.fbk.eu/georeporter#" + data4.getIdTabella().getMapping());
				rigaTPF.setListaattributi(listAttributiSOG);
				rigaTPF.setListachiave(listChiaveSOG);
				String codfis = listNudaProprieta.get(j).getValori().get(namePersonaFisicaHM.get("codiceFiscale"));
				rigaTPF.setUririga("http://dkm.fbk.eu/georeporter#SOG_" + codfis);
				// inserimento dell'elemento
				insertRiga(rigaTPF);
			}else {
				log.info("manca codice fiscale titolare contratto="+id);
				}
			// riga di tipo RIGATABELLA per IND C
			if (listNudaProprieta.get(j).getValori().get("indirizzocontribuente").isEmpty() == false) {
				RigaTabella rigaTINDC = new RigaTabella();
				rigaTINDC.setNometabella("http://dkm.fbk.eu/georeporter#" + data5.getIdTabella().getMapping());
				rigaTINDC.setListaattributi(listAttributiINDC);
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				long time = cal.getTimeInMillis();
				rigaTINDC.setUririga("http://dkm.fbk.eu/georeporter#IND_" + time);
				// inserimento dell'elemento
				insertRiga(rigaTINDC);

				Relazione rel = new Relazione();
				rel.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIndirizzoContribuente");
				rel.setUriDomain("http://dkm.fbk.eu/georeporter#TUNP_" + id);
				rel.setUriRange("http://dkm.fbk.eu/georeporter#IND_" + time);
				listRelNP.add(rel);
			}
			// riga di tipo RIGATABELLA per IND R
			if (listNudaProprieta.get(j).getValori().get("indirizzorecapitocontribuente").isEmpty() == false) {
				RigaTabella rigaTINDRC = new RigaTabella();
				rigaTINDRC.setNometabella("http://dkm.fbk.eu/georeporter#" + data6.getIdTabella().getMapping());
				rigaTINDRC.setListaattributi(listAttributiINDRC);
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				long time = cal.getTimeInMillis();
				rigaTINDRC.setUririga("http://dkm.fbk.eu/georeporter#IND_" + time);
				// inserimento dell'elemento
				insertRiga(rigaTINDRC);

				// relazione TributiUtenza UtenzaRifiuti con Indirizzo Recapito Contribuente
				Relazione rel = new Relazione();
				rel.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIndirizzoRecapitoContribuente");
				rel.setUriDomain("http://dkm.fbk.eu/georeporter#TUNP_" + id);
				// ID
				// creo l'indirizzo univoco grazie dalla data d'inserimento
				rel.setUriRange("http://dkm.fbk.eu/georeporter#IND_" + time);
				listRelNP.add(rel);
			}
			// riga di tipo RIGATABELLA per IND U
			if (listNudaProprieta.get(j).getValori().get("indirizzo").isEmpty() == false) {
				RigaTabella rigaTINDU = new RigaTabella();
				rigaTINDU.setNometabella("http://dkm.fbk.eu/georeporter#" + data7.getIdTabella().getMapping());
				rigaTINDU.setListaattributi(listAttributiINDU);
				Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				long time = cal.getTimeInMillis();
				rigaTINDU.setUririga("http://dkm.fbk.eu/georeporter#IND_" + time);
				// inserimento dell'elemento
				insertRiga(rigaTINDU);

				// relazione TributiUtenza UtenzaRifiuti con Indirizzo Utenza
				Relazione rel = new Relazione();
				rel.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIndirizzoUtenza");
				rel.setUriDomain("http://dkm.fbk.eu/georeporter#TUNP_" + id);
				// ID conribuente
				// creo l'indirizzo univoco grazie dalla data d'inserimento
				rel.setUriRange("http://dkm.fbk.eu/georeporter#IND_" + time);
				listRelNP.add(rel);
			}
			String num = listNudaProprieta.get(j).getValori().get(nameMappingsIdentificativoCatastaleHM.get("numero"));
			if (!num.isEmpty()) {
				// riga di tipo RIGATABELLA per IDE CAT
				RigaTabella rigaTIDECAT = new RigaTabella();
				rigaTIDECAT.setNometabella("http://dkm.fbk.eu/georeporter#" + data8.getIdTabella().getMapping());
				rigaTIDECAT.setListaattributi(listAttributiIDECAT);
				rigaTIDECAT.setListachiave(listChiaveIDECAT);
				
				String codiceAmministrativo = listNudaProprieta.get(j).getValori().get(nameMappingsIdentificativoCatastaleHM.get("codiceAmministrativo"));
				String cc = listNudaProprieta.get(j).getValori().get(nameMappingsIdentificativoCatastaleHM.get("comuneCatastale"));
				String den = listNudaProprieta.get(j).getValori().get(nameMappingsIdentificativoCatastaleHM.get("denominatore"));
				String sub = listNudaProprieta.get(j).getValori().get(nameMappingsIdentificativoCatastaleHM.get("subalterno"));
				
				
				if (sub.equals("0")){
					sub="";
				}
				
				
				RestCalls rc=new RestCalls();
				IDCatastale idc=rc.getIDCatastale(codiceAmministrativo, cc, num, den, sub);
				
				
				//rigaTIDECAT.setUririga("http://dkm.fbk.eu/georeporter#C" + cc + "_N" + num + "_D" + den + "_S" + sub);
				rigaTIDECAT.setUririga(idc.getId());
				// inserimento dell'elemento
				insertRiga(rigaTIDECAT);

				Relazione rel = new Relazione();
				rel.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIdentificativoCatastale");
				rel.setUriDomain("http://dkm.fbk.eu/georeporter#TUNP_" + id);
				// ID
				if (sub.equals("0")){
					sub="";
				}
				//rel.setUriRange("http://dkm.fbk.eu/georeporter#C" + cc + "_N" + num + "_D" + den + "_S" + sub);
				rel.setUriRange(idc.getId());
				listRelNP.add(rel);
			 }else {
					
					log.info("manca riferimento a identificativo catastale= "+id);
				
				}
			
			// riga di tipo RIGATABELLA per NUDA PROPRIETA
			RigaTabella rigaTNP = new RigaTabella();
			rigaTNP.setNometabella("http://dkm.fbk.eu/georeporter#ICI_IMU_NudaProprieta");
			rigaTNP.setListaattributi(listAttributi);
			rigaTNP.setUririga("http://dkm.fbk.eu/georeporter#TUNP_" + id);

			// relazione TributiUtenza Nuda Proprieta con CONTRIBUENTE
			if (listNudaProprieta.get(j).getValori().get("codfiscale").isEmpty() == false) {
				Relazione rel = new Relazione();
				rel.setNomerelazione("http://dkm.fbk.eu/georeporter#hasContribuente");
				rel.setUriDomain("http://dkm.fbk.eu/georeporter#TUNP_" + id);
				// ID contribuente
				String codfisc = listNudaProprieta.get(j).getValori().get("codfiscale");
				rel.setUriRange("http://dkm.fbk.eu/georeporter#SOG_" + codfisc);
				listRelNP.add(rel);
			 }else {
					
					log.info("manca codice fiscale contribuente ="+id);
				}


			rigaTNP.setListarelazioni(listRelNP);

			insertRiga(rigaTNP);

		}
	}

	
	
	public  void insertRiga(RigaTabella riga) {

		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
		//String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";	
	
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
		//String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";

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
		
		
		
		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		try {
			wiciNP.readXLSFile(filePath);
			listNudaProprieta = wiciNP.listNudaProprieta;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mapping e insert
		LoadFile(new File(fileMappings));
			

	}	
		
	
	public  void run_old() {
		String path = "file/TN_file/TRAMBILENO_Utenze_I_C_I__I_M_U_nudoprop.xls";
		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
	//	try {
	//		WrapperTUIciImuNP.readXLSFile(path);
	//	} catch (IOException e) {
			// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}
		// mapping e insert
	//	LoadFile(new File("file/file_mapping/mappingICIIMU.json"));

		
	}
	public static void main(String[] args) {

		String path = "file/TN_file/TRAMBILENO_Utenze_I_C_I__I_M_U_nudoprop.xls";
		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
	//	try {
	//		WrapperTUIciImuNP.readXLSFile(path);
	//	} catch (IOException e) {
			// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}
		// mapping e insert
	//	LoadFile(new File("file/file_mapping/mappingICIIMU.json"),
	//			new File("file/file_mapping/mappingTributoOUtenza.json"),
	//			new File("file/file_mapping/mappingSoggetto.json"),
	//			new File("file/file_mapping/mappingPersonaFisicaCONTRIBUENTEua.json"),
	//			new File("file/file_mapping/mappingIndirizzoCONTRIBUENTE.json"),
	//			new File("file/file_mapping/mappingIndirizzoRECAPITOCONTRIBUENTE.json"),
	//			new File("file/file_mapping/mappingIndirizzoUTENZA.json"),
	//			new File("file/file_mapping/mappingIdentificativoCatastale2.json"));

	}

}
