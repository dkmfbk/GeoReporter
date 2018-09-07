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

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.AnagraficaComunale;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Famiglia;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.FornituraGas;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabelle;
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

	
	
	public WrapperForGas wgas = new WrapperForGas();
	public  List<FornituraGas> listFornituraGas ;

	
	
	public  String targetURL;
	public MappingInsertForGas(String targetURL_) {
		
		targetURL=  targetURL_+"inserttable";
		
	}
	
	private Logger log = Logger.getLogger(MappingInsertForGas.class);
	public  void LoadFile(File fileMappings) {

		Gson gson = new Gson();
		JsonReader reader;

		MappingTabella mappingFornituraGas= new MappingTabella();
		MappingTabella mappingContratto=new MappingTabella();
		MappingTabella mappingIndirizzo=new MappingTabella();

		try {
			reader = new JsonReader(new FileReader(fileMappings));
			MappingTabelle mappings = gson.fromJson(reader, MappingTabelle.class);

			
			List<MappingTabella> listofMappings= mappings.getMappings();

			for (MappingTabella mappingTabella : listofMappings) {
				System.out.println(mappingTabella.getIdTabella().getNome());	
				if (mappingTabella.getIdTabella().getMapping().equals("FornituraGas")){
					mappingFornituraGas=mappingTabella;
					
				}else if(mappingTabella.getIdTabella().getMapping().equals("Contratto")){
					mappingContratto=mappingTabella;
					
					
				}else if(mappingTabella.getIdTabella().getMapping().equals("Indirizzo")){
					mappingIndirizzo=mappingTabella;
					
					
				}
			}
			

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(mappingFornituraGas,mappingContratto,mappingIndirizzo);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
	
	
	
	
	private  void LoadFile_old(File filename, File filename2, File mappingIndirizzoContratti) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		Gson gson3 = new Gson();
		JsonReader reader3;
		
		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(filename2));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);

			reader3 = new JsonReader(new FileReader(mappingIndirizzoContratti));
			MappingTabella data3 = gson3.fromJson(reader3, MappingTabella.class);

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(data, data2,data3);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public  void associazioneMappingNomeVal(MappingTabella data, MappingTabella data2,MappingTabella data3) {
		// ciclo la lista degli elementi FG

		
	    //memorizzo le coppie nome/mapping
			Map<String,String> nameMappingsFornituraGasHM = new HashMap<String,String>();
			Map<String,String> nameMappingsContrattoHM = new HashMap<String,String>();
			Map<String,String> nameMappingsIndirizzoHM = new HashMap<String,String>();
		      
			
			
			for (int i = 0; i < data.getAttributi().size(); i++) {
				nameMappingsFornituraGasHM.put(data.getAttributi().get(i).getMapping().split("#")[1], data.getAttributi().get(i).getNome().split("#")[1]);
			
			}
			
			for (int i = 0; i < data2.getAttributi().size(); i++) {
				nameMappingsContrattoHM.put(data2.getAttributi().get(i).getMapping().split("#")[1], data2.getAttributi().get(i).getNome().split("#")[1]);
			
			}
			for (int i = 0; i < data3.getAttributi().size(); i++) {
				nameMappingsIndirizzoHM.put(data3.getAttributi().get(i).getMapping().split("#")[1], data3.getAttributi().get(i).getNome().split("#")[1]);
			
			}
		
		
		
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

			List<Attributo> listAttributiI = new ArrayList<Attributo>();
			for (int i = 0; i < data3.getAttributi().size(); i++) {

				String string3 = data3.getAttributi().get(i).getNome();
				String[] parts3 = string3.split("#");

				Attributo tmp3 = new Attributo();
				tmp3.setNome(data3.getAttributi().get(i).getNome());
				tmp3.setMapping(data3.getAttributi().get(i).getMapping());
				tmp3.setTipo(data3.getAttributi().get(i).getTipo());

				if ((listFornituraGas.get(j).getValori().get(parts3[1]) != null)
						&& (listFornituraGas.get(j).getValori().get(parts3[1]).isEmpty() == false)) {
					tmp3.setValore((listFornituraGas.get(j).getValori().get(parts3[1])));
					listAttributiI.add(tmp3);
				}
			}

			
			
			
			
			// riga di tipo RIGATABELLA per FOR GAS
			RigaTabella rigaTFG = new RigaTabella();
			rigaTFG.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTFG.setListaattributi(listAttributi);
			String idcfg = listFornituraGas.get(j).getValori().get(nameMappingsFornituraGasHM.get("idSiatelGasDettaglio")).trim();
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
			}else {
				log.info("manca codice fiscale titolare contratto="+idcfg);
			}
			
			// riga di tipo RIGATABELLA per IND
			RigaTabella rigaTIND = new RigaTabella();
			rigaTIND.setNometabella("http://dkm.fbk.eu/georeporter#" + data3.getIdTabella().getMapping());
			rigaTIND.setListaattributi(listAttributiI);
			// creo l'indirizzo univoco grazie dalla data d'inserimento
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			long time = cal.getTimeInMillis();
			String uriInd="http://dkm.fbk.eu/georeporter#IND_" + time;
			rigaTIND.setUririga(uriInd);
			insertRiga(rigaTIND);
			
			// relazione FOR GAS con INDIRIZZO
			if (listFornituraGas.get(j).getValori().get("indirizzoutenza").isEmpty() == false) {
				Relazione relCFGIND = new Relazione();
				relCFGIND.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIndirizzoUtenza");
				relCFGIND.setUriDomain("http://dkm.fbk.eu/georeporter#CFG_" + idcfg);
				// creo l'indirizzo univoco grazie dalla data d'inserimento
				//Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				//long time = cal.getTimeInMillis();
				relCFGIND.setUriRange(uriInd);
				listRelCFG.add(relCFGIND);
			}
			rigaTFG.setListarelazioni(listRelCFG);

			insertRiga(rigaTFG);

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

		//String targetURL = "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
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
	public  void run(String filePath, String fileMappings)
	{
		//String path = "file/TN_file/trambileno_Fornitura_Gas_dettaglio.xls";
		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		try {
			wgas.readXLSFile(filePath);
			 listFornituraGas = wgas.listFornituraGas;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mapping e insert
		LoadFile(new File(fileMappings));
		
	}
	public static void main(String[] args) {

		String path = "file/TN_file/trambileno_Fornitura_Gas_dettaglio.xls";
		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
	//	try {
		//	WrapperForGas.readXLSFile(path);
	//	} catch (IOException e) {
			// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}
		// mapping e insert
	//	LoadFile(new File("file/file_mapping/mappingFornituraGas.json"),
	//			new File("file/file_mapping/mappingContratto.json"),new File("file/file_mapping/mappingIndirizzoContratti.json"));

	}

}
