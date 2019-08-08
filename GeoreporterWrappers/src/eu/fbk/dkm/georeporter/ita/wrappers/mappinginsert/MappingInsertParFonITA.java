package eu.fbk.dkm.georeporter.ita.wrappers.mappinginsert;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Strings;
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
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Particella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Titolarita;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.ita.wrappers.WrapperParFonITA;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperParFon;
import eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert.RestCalls;

public class MappingInsertParFonITA {

	// lista di elementi PARTICELLA del file WRAPPER
	WrapperParFonITA wpartfond = new WrapperParFonITA();
	public  List<Particella> listParticella;


public  String targetURL;
public  String serviceURL;
	
public MappingInsertParFonITA(String targetURL_){
		
		targetURL=  targetURL_+"inserttable";
		
		serviceURL=targetURL_;
	}
	
	
	
	
	public  void LoadFile(File fileMappings) {

		Gson gson = new Gson();
		JsonReader reader;

		MappingTabella mappingParticella= new MappingTabella();
		MappingTabella mappingIdentificativocatastale=new MappingTabella();
		MappingTabella mappingIntavolazione=new MappingTabella();

		try {
			reader = new JsonReader(new FileReader(fileMappings));
			MappingTabelle mappings = gson.fromJson(reader, MappingTabelle.class);

			
			List<MappingTabella> listofMappings= mappings.getMappings();

			for (MappingTabella mappingTabella : listofMappings) {
			//	System.out.println(mappingTabella.getIdTabella().getNome());	
				if (mappingTabella.getIdTabella().getMapping().equals("Particella")){
					mappingParticella=mappingTabella;
					
				}else if(mappingTabella.getIdTabella().getMapping().equals("IdentificativoCatastale")){
					mappingIdentificativocatastale=mappingTabella;
					
					
				
			}else if(mappingTabella.getIdTabella().getMapping().equals("Intavolazione")){
				mappingIntavolazione=mappingTabella;
				
				
			}
			}
			

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(mappingParticella,mappingIntavolazione,mappingIdentificativocatastale);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
	
	
	
	
	
	private  void LoadFile(File filename, File filenameNote, File filename2) {
		// lettura fai JSON per mappatura
		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		Gson gsonNote = new Gson();
		JsonReader readerNote;

		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			readerNote = new JsonReader(new FileReader(filenameNote));
			MappingTabella dataNote = gsonNote.fromJson(readerNote, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(filename2));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);
			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(data, dataNote, data2);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public  void associazioneMappingNomeVal(MappingTabella data, MappingTabella dataIntavolazione,
			MappingTabella data2) {

		
		
		 //memorizzo le coppie nome/mapping
		Map<String,String> nameMappingsParticellaHM = new HashMap<String,String>();
		Map<String,String> nameMappingsIntavolazioneHM = new HashMap<String,String>();
		Map<String,String> nameMappingsIdentificativoCatastaleHM = new HashMap<String,String>();
		
	      
		
		
		for (int i = 0; i < data.getAttributi().size(); i++) {
			nameMappingsParticellaHM.put(data.getAttributi().get(i).getMapping().split("#")[1], data.getAttributi().get(i).getNome().split("#")[1]);
		
		}

		
		for (int i = 0; i < dataIntavolazione.getAttributi().size(); i++) {
			nameMappingsIntavolazioneHM.put(dataIntavolazione.getAttributi().get(i).getMapping().split("#")[1], dataIntavolazione.getAttributi().get(i).getNome().split("#")[1]);
		
		}
		
		for (int i = 0; i < data2.getAttributi().size(); i++) {
			nameMappingsIdentificativoCatastaleHM.put(data2.getAttributi().get(i).getMapping().split("#")[1], data2.getAttributi().get(i).getNome().split("#")[1]);
		
		}
		
		
		
		
		// ciclo la lista degli elementi PAR
		for (int j = 0; j < listParticella.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			List<Attributo> listAttributi2 = new ArrayList<Attributo>();
			List<Attributo> listChiavi2 = new ArrayList<Attributo>();

			List<Attributo> listIntavolazioneI = new ArrayList<Attributo>();
			List<Attributo> listIntavolazioneF = new ArrayList<Attributo>();

			List<Relazione> listRelPAR = new ArrayList<Relazione>();

			// ciclo per crea la listaCHIAVI e listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listParticella.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listParticella.get(j).getListaValoriChiave().get(0).get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listParticella.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp);
				}

				if ((listParticella.get(j).getValori().get(parts[1]) != null)
						&& (listParticella.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listParticella.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}

			}
			// ciclio per creare IntavolazioneINIZIALE e FINALE
			for (int k = 0; k < dataIntavolazione.getAttributi().size(); k++) {

				String string = dataIntavolazione.getAttributi().get(k).getNome();
				String[] parts = string.split("#");

				Attributo tmpINI = new Attributo();
				Attributo tmpINF = new Attributo();

				tmpINI.setNome(dataIntavolazione.getAttributi().get(k).getNome());
				tmpINI.setMapping(dataIntavolazione.getAttributi().get(k).getMapping());
				tmpINI.setTipo(dataIntavolazione.getAttributi().get(k).getTipo());

				tmpINF.setNome(dataIntavolazione.getAttributi().get(k).getNome());
				tmpINF.setMapping(dataIntavolazione.getAttributi().get(k).getMapping());
				tmpINF.setTipo(dataIntavolazione.getAttributi().get(k).getTipo());

				if ((listParticella.get(j).getIntavolazioneIniziale().getValori().get(parts[1]) != null)
						&& (listParticella.get(j).getIntavolazioneIniziale().getValori().get(parts[1])
								.isEmpty() == false)) {
					tmpINI.setValore(listParticella.get(j).getIntavolazioneIniziale().getValori().get(parts[1]));
					listIntavolazioneI.add(tmpINI);
				}

				if ((listParticella.get(j).getIntavolazioneFinale().getValori().get(parts[1]) != null)
						&& (listParticella.get(j).getIntavolazioneFinale().getValori().get(parts[1])
								.isEmpty() == false)) {
					tmpINF.setValore(listParticella.get(j).getIntavolazioneFinale().getValori().get(parts[1]));
					listIntavolazioneF.add(tmpINF);
				}
			}
			// ide cat
			for (int i = 0; i < data2.getAttributi().size(); i++) {
				String string = data2.getAttributi().get(i).getNome();
				String[] parts = string.split("#");
				Attributo tmp2 = new Attributo();
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());

				if ((listParticella.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listParticella.get(j).getListaValoriChiave().get(0).get(parts[1]).isEmpty() == false)) {
					tmp2.setValore(listParticella.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi2.add(tmp2);
				}
				if ((listParticella.get(j).getValori().get(parts[1]) != null)
						&& (listParticella.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp2.setValore(listParticella.get(j).getValori().get(parts[1]));
					listAttributi2.add(tmp2);
				}
			}
			
			List<Relazione> listRelPartIdeCat = new ArrayList<Relazione>();

	//	   String cc = listParticella.get(j).getListaValoriChiave().get(0).get(nameMappingsParticellaHM.get("codiceComuneCatastale"));
			   String cc = listParticella.get(j).getValori().get(nameMappingsIdentificativoCatastaleHM.get("foglio"));
		//	String cc="";
			   System.out.println("CODICE COMUNE CATASTALE="+cc);
			
		  //  String tipoparticellat = listParticella.get(j).getListaValoriChiave().get(0).get("tipoparticellat");
			//String tipoparticella = listParticella.get(j).getValori().get("tipoparticella");
			String tipoparticella = "F";
			//System.out.println("TIPOPARTICELLA="+tipoparticella);
			String codiceAmministrativo= listParticella.get(j).getListaValoriChiave().get(0).get(nameMappingsParticellaHM.get("codiceAmministrativo"));
		//	if (codiceAmministrativo==null||codiceAmministrativo.equals("")) {
		//		codiceAmministrativo = wpartfond.codiceAmministrativo;
		//	}
			
			String num = listParticella.get(j).getValori().get(nameMappingsParticellaHM.get("numero"));
	
			num=StringUtils.stripStart(num,"0");

			String den = listParticella.get(j).getValori().get(nameMappingsParticellaHM.get("denominatore"));
			if ((den!=null)&&(!den.equals(""))) {
				den=StringUtils.stripStart(den,"0");

			}
			
			String idepar = listParticella.get(j).getListaValoriChiave().get(0).get(nameMappingsParticellaHM.get("identificativoParticella"));

			RestCalls rc=new RestCalls();
			
			
			IDCatastale idc=rc.getIDCatastale(codiceAmministrativo, cc, num, den, "");
			
			// Intavolazione INIZIALE
			
			
			
			
			
		//	Attributo codComuneAmministrativo = new Attributo();
		//	codComuneAmministrativo.setNome("http://dkm.fbk.eu/georeporter#codiceamministrativo");
		//	codComuneAmministrativo.setMapping("http://dkm.fbk.eu/georeporter#codiceAmministrativo");
		//	codComuneAmministrativo.setTipo("http://www.w3.org/2001/XMLSchema#string");
		//	codComuneAmministrativo.setValore(wpartfond.codiceAmministrativo);
		//	listAttributi.add(codComuneAmministrativo);
			
			RigaTabella rigaTPAR = new RigaTabella();

			rigaTPAR.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTPAR.setListaattributi(listAttributi);
			if (!listChiavi.isEmpty()) {
				rigaTPAR.setListachiave(listChiavi);
			}
			
			
			String uriIdCatastale="http://dkm.fbk.eu/georeporter#A"+codiceAmministrativo +"_C" + cc + "_N"+tipoparticella + num + "_D" + den + "_S";
			//String uriParticella="http://dkm.fbk.eu/georeporter#PAE_A"+codiceAmministrativo+"_C" + cc + "_NE" + num + "_D" + den;
			
			String uriParticella="http://dkm.fbk.eu/georeporter#PA"+tipoparticella+"_A"+codiceAmministrativo+"_C" + cc + "_N"+tipoparticella + num + "_D" + den;
			
			
			//rigaTPAR.setUririga("http://dkm.fbk.eu/georeporter#PA"+tipoparticella+"_" + codamm + "_" + idepar);
			rigaTPAR.setUririga(uriParticella);
		
			
			
			RigaTabella rigaTINI = new RigaTabella();
			rigaTINI.setNometabella("http://dkm.fbk.eu/georeporter#" + dataIntavolazione.getIdTabella().getMapping());
			rigaTINI.setListaattributi(listIntavolazioneI);
			String numni = listParticella.get(j).getIntavolazioneIniziale().getValori().get(nameMappingsIntavolazioneHM.get("numeroDocumento"));
			rigaTINI.setUririga("http://dkm.fbk.eu/georeporter#INT_" +  idepar + "_" + numni);

			// creare relazione per la intavolazione iniziale con tipo documento / tipo
			// intavolazione
			if (!listParticella.get(j).getIntavolazioneIniziale().getValori().get(nameMappingsIntavolazioneHM.get("hasTipoDocumento")).isEmpty()) {
				List<Relazione> listRelNI = new ArrayList<Relazione>();
				Relazione relNITipo = new Relazione();
				relNITipo.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTipoDocumento");
				relNITipo.setUriDomain("http://dkm.fbk.eu/georeporter#INT_" + idepar + "_" + numni);
				relNITipo.setUriRange("http://dkm.fbk.eu/georeporter#"
						+ listParticella.get(j).getIntavolazioneIniziale().getValori().get(nameMappingsIntavolazioneHM.get("tipoDocumento")));
				listRelNI.add(relNITipo);
				rigaTINI.setListarelazioni(listRelNI);
				insertRiga(rigaTINI);
			}

			// Intavolazione FINALE
			RigaTabella rigaTINF = new RigaTabella();
			rigaTINF.setNometabella("http://dkm.fbk.eu/georeporter#" + dataIntavolazione.getIdTabella().getMapping());
			rigaTINF.setListaattributi(listIntavolazioneI);
			String numnf = listParticella.get(j).getIntavolazioneFinale().getValori().get(nameMappingsIntavolazioneHM.get("numeroDocumento"));
			rigaTINF.setUririga("http://dkm.fbk.eu/georeporter#NOT_" + cc + "_" + idepar + "_" + numnf);

			// creare relazione per la intavolazione iniziale con tipo documento / tipo
			// intavolazione
			if (!listParticella.get(j).getIntavolazioneFinale().getValori().get(nameMappingsIntavolazioneHM.get("hasTipoDocumento")).isEmpty()) {
				List<Relazione> listRelNF = new ArrayList<Relazione>();
				Relazione relNFTipo = new Relazione();
				relNFTipo.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTipoDocumento");
				// ??
				relNFTipo.setUriDomain("http://dkm.fbk.eu/georeporter#INT_" + cc + "_" + idepar + "_" + numnf);
				relNFTipo.setUriRange("http://dkm.fbk.eu/georeporter#"
						+ listParticella.get(j).getIntavolazioneFinale().getValori().get(nameMappingsIntavolazioneHM.get("hasTipoDocumento")));
				listRelNF.add(relNFTipo);
				rigaTINF.setListarelazioni(listRelNF);
				insertRiga(rigaTINF);
			}

			// inserisco IntavolazioneIniziale e Finale resisto per entrambe URI e iserisco
			// la
			// relazione nella sua riga TTC
			if (!listParticella.get(j).getIntavolazioneIniziale().getValori().get(nameMappingsIntavolazioneHM.get("hasTipoDocumento")).isEmpty()) {
				// String relNIuri = insertRigaReturn(rigaTINI);
				Relazione relNI = new Relazione();
				relNI.setNomerelazione("http://dkm.fbk.eu/georeporter#hasRegistrazioneIniziale");
				//relNI.setUriDomain("http://dkm.fbk.eu/georeporter#PA"+tipoparticella+"_C" + codamm + "_N" + num + "_D" + den);
				relNI.setUriDomain(uriParticella);
				
				relNI.setUriRange("http://dkm.fbk.eu/georeporter#INT_" + codiceAmministrativo + "_" + idepar + "_" + numni);
				listRelPAR.add(relNI);
			}

			if (!listParticella.get(j).getIntavolazioneFinale().getValori().get(nameMappingsIntavolazioneHM.get("hasTipoDocumento")).isEmpty()) {
				// String relNFuri = insertRigaReturn(rigaTINF);
				Relazione relNF = new Relazione();
				relNF.setNomerelazione("http://dkm.fbk.eu/georeporter#hasRegistrazioneFinale");
				//relNF.setUriDomain("http://dkm.fbk.eu/georeporter#PA"+tipoparticella+"_C" + codamm + "_N" + num + "_D" + den);
				relNF.setUriDomain(uriParticella);
				relNF.setUriRange("http://dkm.fbk.eu/georeporter#INT_" + codiceAmministrativo + "_" + idepar + "_" + numnf);
				listRelPAR.add(relNF);
			}
			// relazione tipo particella
//			if (!listParticella.get(j).getListaValoriChiave().get(0).get("tipoparticella").isEmpty()) {
//				Relazione relParTipo = new Relazione();
//				relParTipo.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTipoParticella");
//				relParTipo.setUriDomain("http://dkm.fbk.eu/georeporter#PAR_" + codamm + "_" + idepar);
//				relParTipo.setUriRange("http://dkm.fbk.eu/georeporter#"
//						+ listParticella.get(j).getListaValoriChiave().get(0).get("tipoparticella"));
//				listRelPAR.add(relParTipo);
//			}

			// setto relazioni
			rigaTPAR.setListarelazioni(listRelPAR);
			// inserisco l'elemento di RIGATABELLA PAr dopo aver inserito INTAVOLAZIONE e
			// creato le relazioni
			insertRiga(rigaTPAR);

		//	Attributo codiceComuneAmministrativo = new Attributo();
		//	codiceComuneAmministrativo.setNome("http://dkm.fbk.eu/georeporter#codiceamministrativo");
		//	codiceComuneAmministrativo.setMapping("http://dkm.fbk.eu/georeporter#codiceAmministrativo");
		//	codiceComuneAmministrativo.setTipo("http://www.w3.org/2001/XMLSchema#string");
		//	codiceComuneAmministrativo.setValore(wpartfond.codiceAmministrativo);
		//	listAttributi2.add(codComuneAmministrativo);
			
			
			RigaTabella rigaTIdeCat = new RigaTabella();
			rigaTIdeCat.setNometabella("http://dkm.fbk.eu/georeporter#" + data2.getIdTabella().getMapping());
			rigaTIdeCat.setListaattributi(listAttributi2);
			rigaTIdeCat.setListachiave(listChiavi2);
		//	rigaTIdeCat.setUririga("http://dkm.fbk.eu/georeporter#C" + codamm + "_N" + num + "_D" + den);

			
			
			
			
			rigaTIdeCat.setUririga(uriIdCatastale);
			// relazione tra particella fondiaria e ide cat
			Relazione relPartIdeCat = new Relazione();
		    relPartIdeCat.setNomerelazione("http://dkm.fbk.eu/georeporter#hasParticella");
//		    relPartIdeCat.setUriDomain("http://dkm.fbk.eu/georeporter#C" + codamm + "_N" + num + "_D" + den);
//			relPartIdeCat.setUriRange("http://dkm.fbk.eu/georeporter#PA"+tipoparticella+"_C" + codamm + "_N" + num + "_D" + den);

		    relPartIdeCat.setUriDomain(uriIdCatastale);
			relPartIdeCat.setUriRange(uriParticella);
			listRelPartIdeCat.add(relPartIdeCat);

			rigaTIdeCat.setListarelazioni(listRelPartIdeCat);
			insertRiga(rigaTIdeCat);
			// test inserimenti
		}
	}

	// metodo per l'inserimento dell'elemento pronto dopo il mapping
	public  String insertRigaReturn(RigaTabella riga) {

		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
		//String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";

		Gson gson = new Gson();
		String json = gson.toJson(riga);
		// System.out.println(json);

		String output = null;

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

			// String output;
			// System.out.println("Output from Server:\n");
			while ((output = responseBuffer.readLine()) != null) {
				// System.out.println(output);
				// output = responseBuffer.readLine();
				return output;
			}

			httpConnection.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return output;
	}

	
	
	public  void insertRiga(RigaTabella riga) {

		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
	//	String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";	
	
		Gson gson = new Gson();
		String json = gson.toJson(riga);
//System.out.println("json= "+json);		
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
    
	
	
	public static void insertRiga_old(RigaTabella riga) {

		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
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
			while ((output = responseBuffer.readLine()) != null) {
				 //System.out.println(output);
			}

			httpConnection.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	// metodo che dato UI mi restituisce IDECATASTALE
	public  String getICfromUI(String ui) {

		String result = "C0_N0_D0_S0";
		Client client = Client.create();

		WebResource webResource = client
				.resource(serviceURL+"icfromui?ui=" + ui);
		ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);

		// Status 200 is successful.
		
		client.destroy();
		if (response.getStatus() != 200) {
			System.out.println("Failed with HTTP Error code: " + response.getStatus());
			String error = response.getEntity(String.class);
			System.out.println("Error: " + error);

			return result;
		}
		return response.getEntity(String.class);

	}

	public  void run(String filePath, String fileHeaderPath, String fileMappings) {

		// path del file .PAR e del file con gli HEADER inseriti a mano da un utente
	//	String pathF = "file/TN_file/404_41097.PAR";
	//	String pathP = "file/TN_header/headerfileparfon.csv";

		// chiamata per l'estrazione degli header per la composizione della lista HEADER
		wpartfond.estrazioneHeaderFilePar(fileHeaderPath);

		// chiamata per l'analisi del file .PAR
		wpartfond.letturaFilePar(filePath);
	//	wpartfond.codiceAmministrativo=codiceamministrativo;
		listParticella = wpartfond.listParticella;
		// chiamata al metodo che accoppia ELEMENTO appena acquisito al NOME che serve
		// per l'inserimento
		// questo grazie ai file di mapping
		LoadFile(new File(fileMappings));
	//	LoadFile(new File("file/file_mapping/mappingParticella.json"),
	//			new File("file/file_mapping/mappingIntavolazione.json"),
	//			new File("file/file_mapping/mappingIdentificativoCatastale.json"));

	}
	
	
	public static void main(String[] args) {

		// path del file .PAR e del file con gli HEADER inseriti a mano da un utente
		String pathF = "file/TN_file/404_41097.PAR";
		String pathP = "file/TN_header/headerfileparfon.csv";

		// chiamata per l'estrazione degli header per la composizione della lista HEADER
	//	WrapperParFon.estrazioneHeaderFilePar(pathP);

		// chiamata per l'analisi del file .PAR
	//	WrapperParFon.letturaFilePar(pathF);

		// chiamata al metodo che accoppia ELEMENTO appena acquisito al NOME che serve
		// per l'inserimento
		// questo grazie ai file di mapping
	//	LoadFile(new File("file/file_mapping/mappingParticella.json"),
	//			new File("file/file_mapping/mappingIntavolazione.json"),
	//			new File("file/file_mapping/mappingIdentificativoCatastale.json"));

	}

}
