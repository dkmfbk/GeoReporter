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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

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
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Indirizzo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabelle;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Titolarita;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.config.Costanti;
import eu.fbk.dkm.georeporter.tn.wrappers.ControlloValore;
import eu.fbk.dkm.georeporter.tn.wrappers.GetCoordinates;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperFab;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperFamiglia;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperFabIndirizzo;

public class MappingInsertFabIndirizzo {

	public  List<Indirizzo> listIndirizzi;
	public WrapperFab wf = new WrapperFab();
	public  String targetURL;
	
	public MappingInsertFabIndirizzo( String targetURL_) {
		
		targetURL=  targetURL_+"inserttable";
	}
	
	
	public  void LoadFile(File fileMappings) {

		Gson gson = new Gson();
		JsonReader reader;

		MappingTabella mappingIndirizzo= new MappingTabella();
		

		try {
			reader = new JsonReader(new FileReader(fileMappings));
			MappingTabelle mappings = gson.fromJson(reader, MappingTabelle.class);

			
			List<MappingTabella> listofMappings= mappings.getMappings();

			for (MappingTabella mappingTabella : listofMappings) {
				//System.out.println(mappingTabella.getIdTabella().getNome());	
				if (mappingTabella.getIdTabella().getMapping().equals("Indirizzo")){
					mappingIndirizzo=mappingTabella;				
					
				}
			}
			

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(mappingIndirizzo);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
	
	
	
	
	
	private  void LoadFile_old(File filename) {

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

	
	
	
	public String getDescrizioneToponimo(String codToponimo) {
		
		//System.out.println("codicecomune="+ codComune);
		Costanti c= new Costanti();
	//	System.out.println("COSTANTI cod toponimo=="+codToponimo);
		
		String descrizione=c.toponimi.get(codToponimo);
	//	System.out.println(descrizione);
		return descrizione;
		
		
	}
	
	
	
	
	public  void associazioneMappingNomeVal(MappingTabella data) {
		
		
		 //memorizzo le coppie nome/mapping
		Map<String,String> nameMappingsIndirizzoHM = new HashMap<String,String>();
		
	      
		
		
		for (int i = 0; i < data.getAttributi().size(); i++) {
			nameMappingsIndirizzoHM.put(data.getAttributi().get(i).getMapping().split("#")[1], data.getAttributi().get(i).getNome().split("#")[1]);
		
		}
		
		
		
		
		
		
		
		
		
		
		
		// ciclo la lista degli elementi IND
		for (int j = 0; j < listIndirizzi.size(); j++) {

			List<Attributo> listAttributi = new ArrayList<Attributo>();

			// ciclo per crea la listaCHIAVI e listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());
				
				
				if (parts[1].equals("toponimo")) {
					tmp.setValore(getDescrizioneToponimo(listIndirizzi.get(j).getValori().get(parts[1])));
					listAttributi.add(tmp);
					continue;
				}
				if (parts[1].equals("codiceamministrativo")) {
					tmp.setValore(listIndirizzi.get(j).getListaValoriChiave().get(i).get("codiceamministrativo"));
					//tmp.setMapping(data.getAttributi().get(i).getMapping());
					if (tmp.getValore()!=null)
					listAttributi.add(tmp);
					continue;
				}
				if ((listIndirizzi.get(j).getValori().get(parts[1]) != null)
						&& (listIndirizzi.get(j).getValori().get(parts[1]).isEmpty() == false)
						|| parts[1].equals("civico")) {
					
					if (parts[1].equals("civico")) {
						int v = 1;
						while ((v <= 3) && !listIndirizzi.get(j).getValori().get("civico" + v).isEmpty()) {
							Attributo tmp2 = new Attributo();
							tmp2.setNome(data.getAttributi().get(i).getNome());
							tmp2.setMapping(data.getAttributi().get(i).getMapping());
							tmp2.setTipo(data.getAttributi().get(i).getTipo());
							tmp2.setValore(listIndirizzi.get(j).getValori().get("civico" + v));
							listAttributi.add(tmp2);
							v++;
						}
					} else {
						tmp.setValore(listIndirizzi.get(j).getValori().get(parts[1]));
						listAttributi.add(tmp);
					}
				}

			}

			// riga di tipo RIGATABELLA per IND
			RigaTabella rigaTIND = new RigaTabella();

			rigaTIND.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTIND.setListaattributi(listAttributi);
			// creo l'indirizzo univoco grazie dalla data d'inserimento
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			long time = cal.getTimeInMillis();
			String id_indirizzo="http://dkm.fbk.eu/georeporter#IND_" + time;
			rigaTIND.setUririga(id_indirizzo);

			insertRiga(rigaTIND);

			// riga tab UI con relazione con indirizzo
			RigaTabella rigaTUI = new RigaTabella();
			rigaTUI.setNometabella("http://dkm.fbk.eu/georeporter#UnitaImmobiliare");
			//String codamm = listIndirizzi.get(j).getListaValoriChiave().get(0).get(nameMappingsIndirizzoHM.get("codiceAmministrativo"));
			//String ideimm = listIndirizzi.get(j).getListaValoriChiave().get(0).get(nameMappingsIndirizzoHM.get("identificativoImmobile"));
			String codamm = listIndirizzi.get(j).getListaValoriChiave().get(0).get("codiceamministrativo");
			String ideimm = listIndirizzi.get(j).getListaValoriChiave().get(0).get("identificativoimmobile");
			rigaTUI.setUririga("http://dkm.fbk.eu/georeporter#UI_" + codamm + "_" + ideimm);
			List<Relazione> listRelUI = new ArrayList<Relazione>();

			Relazione relUIIND = new Relazione();
			relUIIND.setNomerelazione("http://dkm.fbk.eu/georeporter#hasIndirizzo");
			relUIIND.setUriDomain("http://dkm.fbk.eu/georeporter#UI_" + codamm + "_" + ideimm);
			//relUIIND.setUriRange("http://dkm.fbk.eu/georeporter#IND_" + time);
			relUIIND.setUriRange(id_indirizzo);
			listRelUI.add(relUIIND);
			rigaTUI.setListarelazioni(listRelUI);

			insertRiga(rigaTUI);

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
           	// System.out.println(e.toString());
          }
           String responseEntity = response.getEntity(String.class);
           
          
          // System.out.println(responseEntity.toString());
           
           client.destroy();
           } catch (MalformedURLException e1) {
   			// TODO Auto-generated catch block
   			e1.printStackTrace();
   		}
           
	   }
    
	
	
	
	
	
	
	public  void insertRiga2(RigaTabella riga) {

		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
		String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";

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

public  void run(String pathFile,String pathFileHeader, String pathFileMappings) {

		
	
		// chiamata per l'estrazione degli header per la composizione della lista HEADER
	//	WrapperFab.estrazioneHeaderFileFab(pathFileHeader);

		// chiamata per l'analisi del file .FAB
	//	WrapperFab.letturaFileFab(pathFile);
	wf.estrazioneHeaderFileFab(pathFileHeader);

	// chiamata per l'analisi del file .FAB
	wf.letturaFileFab(pathFile);
	listIndirizzi = wf.listIndirizzi;
		// chiamata al metodo che accoppia ELEMENTO appena acquisito al NOME che serve
		// per l'inserimento
		// questo grazie ai file di mapping
		LoadFile(new File(pathFileMappings));
		
		

	}

	public  void run() {
		String pathF = "file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB";
		String pathP = "file/TN_header/headerfilefab.csv";

		// chiamata per l'estrazione degli header per la composizione della lista HEADER
	//	WrapperFab.estrazioneHeaderFileFab(pathP);

		// chiamata per l'analisi del file .FAB
	//	WrapperFab.letturaFileFab(pathF);

		LoadFile(new File("file/file_mapping/mappingIndirizzoFab.json"));
		
	}
	
	public  void main(String[] args) {

		String pathF = "file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB";
		String pathP = "file/TN_header/headerfilefab.csv";

		// chiamata per l'estrazione degli header per la composizione della lista HEADER
	//	WrapperFab.estrazioneHeaderFileFab(pathP);

		// chiamata per l'analisi del file .FAB
	//	WrapperFab.letturaFileFab(pathF);

		LoadFile(new File("file/file_mapping/mappingIndirizzoFab.json"));

	}

}
