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
import java.util.Map;

import javax.ws.rs.WebApplicationException;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Calendar;

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
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.PersonaFisica;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.PersonaGiuridica;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.ProprietarioproTempore;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperSog;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperSogFon;

public class MappingInsertSogFon {

 public WrapperSogFon wsogfond = new WrapperSogFon();
	public  List<PersonaFisica> listPersFisica ;
	public  List<PersonaGiuridica> listPersGiuridica;
	public  List<ProprietarioproTempore> listProprietarioProTempore;
	

	
public  String targetURL;
	
MappingInsertSogFon(String targetURL_){
		
		targetURL=  targetURL_+"inserttable";
		
		
	}
private Logger log = Logger.getLogger(MappingInsertSogFon.class);

	public  void LoadFile(File fileMappings) {

		Gson gson = new Gson();
		JsonReader reader;

		MappingTabella mappingSoggetto= new MappingTabella();
		MappingTabella mappingPersonaFisica=new MappingTabella();
		MappingTabella mappingPersonaGiuridica=new MappingTabella();
		MappingTabella mappingProprietarioProtempore=new MappingTabella();
		

		try {
			reader = new JsonReader(new FileReader(fileMappings));
			MappingTabelle mappings = gson.fromJson(reader, MappingTabelle.class);

			
			List<MappingTabella> listofMappings= mappings.getMappings();

			for (MappingTabella mappingTabella : listofMappings) {
				System.out.println(mappingTabella.getIdTabella().getNome());	
				if (mappingTabella.getIdTabella().getMapping().equals("Soggetto")){
					mappingSoggetto=mappingTabella;
					
				}else if(mappingTabella.getIdTabella().getMapping().equals("PersonaFisica")){
					mappingPersonaFisica=mappingTabella;
					
				}else if(mappingTabella.getIdTabella().getMapping().equals("PersonaGiuridica")){
					mappingPersonaGiuridica=mappingTabella;
					
				}else if(mappingTabella.getIdTabella().getMapping().equals("ProprietarioProTempore")){
					mappingProprietarioProtempore=mappingTabella;
						
				}
			}
			

			// chiamata al metodo per l'accoppiamento effettivo
			
			associazioneMappingNomeVal(mappingPersonaFisica, mappingSoggetto, listPersFisica);
			associazioneMappingNomeVal2(mappingPersonaGiuridica, mappingSoggetto, listPersGiuridica);
			associazioneMappingNomeVal3(mappingProprietarioProtempore, mappingSoggetto, listProprietarioProTempore);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	private  void LoadFile1(File filename, File filename2) {
		// lettura fai JSON per mappatura persona Fisica
		//input json personafisica, soggetto
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
			associazioneMappingNomeVal(data, data2, listPersFisica);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private  void LoadFile2(File filename, File filename2) {
		// lettura fai JSON per mappatura persona Giuridica
		//input json personagiuridica, soggetto
		
		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(filename2));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);

			associazioneMappingNomeVal2(data, data2, listPersGiuridica);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private  void LoadFile3(File filename, File filename2) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		try {

			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(filename2));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);

			associazioneMappingNomeVal3(data, data2, listProprietarioProTempore);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public  void associazioneMappingNomeVal(MappingTabella data, MappingTabella data2,
			List<PersonaFisica> listPers) {
		//input json perso, soggetto
		// ciclo la lista degli elementi P
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
			String codfis = listPers.get(j).getValori().get("codicefiscale");
			List<Map<String,String>> attributichiave_hm = listPers.get(j). getListaValoriChiave();
			String idsoggetto="";
			for (Map<String, String> map : attributichiave_hm) {
				if (map.containsKey("identificativosoggetto")){
					idsoggetto=map.get("identificativosoggetto");
				}
				
			}
			
			if (codfis.isEmpty()||codfis==null) {
			//	System.out.println("CODICE_FISCALE="+codfis);
			//	System.out.println("idsoggetto="+idsoggetto);
				codfis=idsoggetto;
				
			}
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

	public  void associazioneMappingNomeVal2(MappingTabella data, MappingTabella data2,
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

			// riga di tipo RIGATABELLA per PG
			String codfis = listPers.get(j).getValori().get("codicefiscale");
			
			
			List<Map<String,String>> attributichiave_hm = listPers.get(j). getListaValoriChiave();
			String idsoggetto="";
			for (Map<String, String> map : attributichiave_hm) {
				if (map.containsKey("identificativosoggetto")){
					idsoggetto=map.get("identificativosoggetto");
				}
				
			}
			
			if (codfis==null||codfis.isEmpty()) {
				System.out.println("CODICE_FISCALE="+codfis);
				System.out.println("idsoggetto="+idsoggetto);
				codfis=idsoggetto;
				
			}
			
			
			if (!codfis.isEmpty()) {
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
	
	public  void associazioneMappingNomeVal3(MappingTabella data, MappingTabella data2,
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
			String codamm = listPers.get(j).getListaValoriChiave().get(0).get("comunecatastale");
			String idesog = listPers.get(j).getListaValoriChiave().get(0).get("identificativosoggetto");
			rigaTPF.setUririga("http://dkm.fbk.eu/georeporter#SOG_" + codamm + "_" + idesog);
			//	System.out.println("CODICE_FISCALE="+codfis);
				System.out.println("idsoggetto="+idesog);
			// inserimento dell'elemento
			insertRiga(rigaTPF);
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

	
	public  void run(String filePath,String fileHeaderPath ,String fileMappings){

		// path del file .SOG e del file con gli HEADER inseriti a mano da un utente
//		String pathF = "file/TN_file/404_41097.SOG";
//		String pathP = "file/TN_header/headerfilesogfon.csv";

		// chiamata per l'estrazione degli header per la composizione della lista HEADER
		wsogfond.estrazioneHeaderFileSogFon(fileHeaderPath);

		// chiamata per l'analisi del file .SOG FON
		wsogfond.letturaFileSogFon(filePath);
		 listPersFisica = wsogfond.listPersonaFisicaFon;
	     listPersGiuridica= wsogfond.listPersonaGiuridicaFon;
	     listProprietarioProTempore= wsogfond.listProprietarioproTempore;
		// mapping e insert degli elementi PF PG
		LoadFile(new File(fileMappings));
		
//		LoadFile1(new File("file/file_mapping/mappingPersonaFisica.json"),
//				new File("file/file_mapping/mappingSoggetto.json"));
//		LoadFile2(new File("file/file_mapping/mappingPersonaGiuridica.json"),
//				new File("file/file_mapping/mappingSoggetto.json"));
//		LoadFile3(new File("file/file_mapping/mappingProprietarioProTempore.json"),
//				new File("file/file_mapping/mappingSoggetto.json"));
	}

	
	
	public static void main(String[] args) {

		// path del file .SOG e del file con gli HEADER inseriti a mano da un utente
		String pathF = "file/TN_file/404_41097.SOG";
		String pathP = "file/TN_header/headerfilesogfon.csv";

		// chiamata per l'estrazione degli header per la composizione della lista HEADER
	//	WrapperSogFon.estrazioneHeaderFileSogFon(pathP);

		// chiamata per l'analisi del file .SOG FON
	//	WrapperSogFon.letturaFileSogFon(pathF);

		// mapping e insert degli elementi PF PG
//		LoadFile1(new File("file/file_mapping/mappingPersonaFisica.json"),
//				new File("file/file_mapping/mappingSoggetto.json"), WrapperSogFon.listPersonaFisicaFon);
//		LoadFile2(new File("file/file_mapping/mappingPersonaGiuridica.json"),
//				new File("file/file_mapping/mappingSoggetto.json"), WrapperSogFon.listPersonaGiuridicaFon);
//		LoadFile3(new File("file/file_mapping/mappingProprietarioProTempore.json"),
//				new File("file/file_mapping/mappingSoggetto.json"), WrapperSogFon.listProprietarioproTempore);

	}

}
