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
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.FornituraEnergia;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.FornituraGas;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Locazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabelle;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Titolarita;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperAnagraficaComunale;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperForEne;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperForGas;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperForLoc;

public class MappingInsertForLocazione {

	public  List<Locazione> listLocazione = WrapperForLoc.listLocazione;

	
	public  String targetURL;
	public MappingInsertForLocazione(String targetURL_) {
		
		targetURL=  targetURL_+"inserttable";
		
	}
	 
	public  void LoadFile(File fileMappings) {

		Gson gson = new Gson();
		JsonReader reader;

		MappingTabella mappingFornituraLocazione= new MappingTabella();
		MappingTabella mappingContratto=new MappingTabella();
		MappingTabella mappingIdentificativoCatastale=new MappingTabella();
		MappingTabella mappingIndirizzo=new MappingTabella();
		

		try {
			reader = new JsonReader(new FileReader(fileMappings));
			MappingTabelle mappings = gson.fromJson(reader, MappingTabelle.class);

			
			List<MappingTabella> listofMappings= mappings.getMappings();

			for (MappingTabella mappingTabella : listofMappings) {
				System.out.println(mappingTabella.getIdTabella().getNome());	
				if (mappingTabella.getIdTabella().getMapping().equals("Locazione")){
					mappingFornituraLocazione=mappingTabella;
					
				}else if(mappingTabella.getIdTabella().getMapping().equals("Contratto")){
					mappingContratto=mappingTabella;
				}else if(mappingTabella.getIdTabella().getMapping().equals("IdentificativoCatastale")){
					mappingIdentificativoCatastale=mappingTabella;
					
					
				}else if(mappingTabella.getIdTabella().getMapping().equals("Indirizzo")){
					mappingIndirizzo=mappingTabella;
					
					
				}
			}
			

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(mappingFornituraLocazione,mappingContratto,mappingIdentificativoCatastale,mappingIndirizzo);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	private  void LoadFile(File filename, File filename2, File filename3, File mappingIndirizzoContratti) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		Gson gson3 = new Gson();
		JsonReader reader3;
		Gson gson4 = new Gson();
		JsonReader reader4;
		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(filename2));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);
			reader3 = new JsonReader(new FileReader(filename3));
			MappingTabella data3 = gson3.fromJson(reader3, MappingTabella.class);
			reader4 = new JsonReader(new FileReader(mappingIndirizzoContratti));
			MappingTabella data4 = gson4.fromJson(reader4, MappingTabella.class);
			
			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(data, data2, data3, data4);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public  void associazioneMappingNomeVal(MappingTabella data, MappingTabella data2, MappingTabella data3,MappingTabella data4) {
		// ciclo la lista degli elementi LOC
		for (int j = 0; j < listLocazione.size(); j++) {

			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			// ciclo per crea listaATTRIBUTI richiesti dal mapping LOC
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listLocazione.get(j).getValori().get(parts[1]) != null)
						&& (listLocazione.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listLocazione.get(j).getValori().get(parts[1]));
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

				if ((listLocazione.get(j).getValori().get(parts2[1]) != null)
						&& (listLocazione.get(j).getValori().get(parts2[1]).isEmpty() == false)) {
					tmp2.setValore(listLocazione.get(j).getValori().get(parts2[1]));
					listAttributi.add(tmp2);
				}

			}

			
			// ciclo per crea e listaATTRIBUTI richiesti dal mapping Contratto
						for (int i = 0; i < data3.getAttributi().size(); i++) {

							String string3 = data3.getAttributi().get(i).getNome();
							String[] parts3 = string3.split("#");

							Attributo tmp3 = new Attributo();
							tmp3.setNome(data3.getAttributi().get(i).getNome());
							tmp3.setMapping(data3.getAttributi().get(i).getMapping());
							tmp3.setTipo(data3.getAttributi().get(i).getTipo());

							if ((listLocazione.get(j).getValori().get(parts3[1]) != null)
									&& (listLocazione.get(j).getValori().get(parts3[1]).isEmpty() == false)) {
								tmp3.setValore(listLocazione.get(j).getValori().get(parts3[1]));
								listAttributi.add(tmp3);
							}

						}
			
			
			
						List<Attributo> listAttributiI = new ArrayList<Attributo>();
						// ciclo per crea e listaATTRIBUTI richiesti dal mapping Contratto
						for (int i = 0; i < data4.getAttributi().size(); i++) {

							String string4 = data4.getAttributi().get(i).getNome();
							String[] parts4 = string4.split("#");

							Attributo tmp4 = new Attributo();
							tmp4.setNome(data4.getAttributi().get(i).getNome());
							tmp4.setMapping(data4.getAttributi().get(i).getMapping());
							tmp4.setTipo(data4.getAttributi().get(i).getTipo());

							if ((listLocazione.get(j).getValori().get(parts4[1]) != null)
									&& (listLocazione.get(j).getValori().get(parts4[1]).isEmpty() == false)) {
								tmp4.setValore(listLocazione.get(j).getValori().get(parts4[1]));
								listAttributiI.add(tmp4);
							}

						}
			
			
			// riga di tipo RIGATABELLA per FOR LOCAZIONE
			RigaTabella rigaTFL = new RigaTabella();
			rigaTFL.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTFL.setListaattributi(listAttributi);
			// creo l'indirizzo univoco grazie dalla data d'inserimento
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			long time = cal.getTimeInMillis();
			String uriCFL="http://dkm.fbk.eu/georeporter#CFL_" + time;
			rigaTFL.setUririga(uriCFL);

			List<Relazione> listRelCFL = new ArrayList<Relazione>();
			// relazione FOR LOCAZIONE con PROPRIETARIO
			String codfpro = listLocazione.get(j).getValori().get("codicefiscaleproprietario");
			if (!codfpro.isEmpty()) {
				Relazione relCFLPRO = new Relazione();
				relCFLPRO.setNomerelazione("http://dkm.fbk.eu/georeporter#hasProprietario");
				relCFLPRO.setUriDomain("http://dkm.fbk.eu/georeporter#CFL_" + time);
				relCFLPRO.setUriRange("http://dkm.fbk.eu/georeporter#SOG_" + codfpro.trim());

				listRelCFL.add(relCFLPRO);
			}

			// riga di tipo RIGATABELLA per IND
			
			
			
			// riga di tipo RIGATABELLA per IND
						
						// relazione FOR GAS con INDIRIZZO
						if (listLocazione.get(j).getValori().get("indirizzo").isEmpty() == false) {
						
							
							
							RigaTabella rigaTIND = new RigaTabella();
							rigaTIND.setNometabella("http://dkm.fbk.eu/georeporter#" + data4.getIdTabella().getMapping());
							rigaTIND.setListaattributi(listAttributiI);
							// creo l'indirizzo univoco grazie dalla data d'inserimento
							Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
							long time2 = cal.getTimeInMillis();
							String uriInd="http://dkm.fbk.eu/georeporter#IND_" + time2;
							rigaTIND.setUririga(uriInd);
							insertRiga(rigaTIND);
								
							
							
							
							
							Relazione relCFLIND = new Relazione();
							relCFLIND.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIndirizzoUtenza");
							relCFLIND.setUriDomain(uriCFL);
							// creo l'indirizzo univoco grazie dalla data d'inserimento
						//	Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
						//	long time = cal.getTimeInMillis();
							relCFLIND.setUriRange(uriInd);
							listRelCFL.add(relCFLIND);
						}
						
			// relazione FOR LOCAZIONE con INQUILINO
			String codfinq = listLocazione.get(j).getValori().get("codicefiscaleinquilino");
			if (!codfinq.isEmpty()) {
				Relazione relCFLINQ = new Relazione();
				relCFLINQ.setNomerelazione("http://dkm.fbk.eu/georeporter#hasInquilino");
				relCFLINQ.setUriDomain("http://dkm.fbk.eu/georeporter#CFL_" + time);
				relCFLINQ.setUriRange("http://dkm.fbk.eu/georeporter#SOG_" + codfinq.trim());
				listRelCFL.add(relCFLINQ);
			}

			// relazione FOR LOCAZIONE con IDE CAT
			Relazione relCFLIDECAT = new Relazione();
			relCFLIDECAT.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIdentificativoCatastale");
			relCFLIDECAT.setUriDomain("http://dkm.fbk.eu/georeporter#CFL_" + time);
			String codamm = listLocazione.get(j).getValori().get("codcomune").trim();
			String sub = listLocazione.get(j).getValori().get("subalterno").trim();
			String numden = listLocazione.get(j).getValori().get("particella").trim();
			// controllo particella
			numden.replace("/", " ");
			//tolgo tutti gli spazi doppi
			numden = numden.replaceAll("\\s+", " ");
			String[] tmp = numden.split("[ ]", -1);
			String num, den;
			if (tmp.length == 2) {
				num = tmp[0];
				den = tmp[1];
			} else {
				num = numden;
				den = "";
			}
			if (sub.equals("0")){
				sub="";
			}
			if (num.equals("")){
				relCFLIDECAT.setUriRange("http://dkm.fbk.eu/georeporter#C0_N0_D0_S0");
			}else {
				relCFLIDECAT.setUriRange("http://dkm.fbk.eu/georeporter#C" + codamm + "_N" + num + "_D" + den + "_S" + sub);	
			}
			listRelCFL.add(relCFLIDECAT);
			
			rigaTFL.setListarelazioni(listRelCFL);

			insertRiga(rigaTFL);

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
		//String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";

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
	public  void run(String filePath, String fileMappings)
	{
		String path = "file/TN_file/trambileno_Fornitura_Locazione_dettaglio.xls";
		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		try {
			WrapperForLoc.readXLSFile(filePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mapping e insert
		LoadFile(new File(fileMappings));
	}
	public static void main(String[] args) {

		String path = "file/TN_file/trambileno_Fornitura_Locazione_dettaglio.xls";
		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
		try {
			WrapperForLoc.readXLSFile(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// mapping e insert
		//LoadFile(new File("file/file_mapping/mappingFornituraLocazione.json"),
		//		new File("file/file_mapping/mappingContratto2.json"),
		//		new File("file/file_mapping/mappingIdentificativoCatastale3.json"),
		//		new File("file/file_mapping/mappingIndirizzoLocazioni.json"));

	}

}
