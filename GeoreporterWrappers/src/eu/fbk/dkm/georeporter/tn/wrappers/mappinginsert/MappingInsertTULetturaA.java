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
import java.util.List;

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
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.LetturaAcqua;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperTULetturaA;

public class MappingInsertTULetturaA {

	public WrapperTULetturaA wletturaacqua = new WrapperTULetturaA();
	public  List<LetturaAcqua> listLetturaAcqua ;

	public  String targetURL;
	public MappingInsertTULetturaA(String targetURL_) {
		
		targetURL=  targetURL_+"inserttable";
		
	}
	private Logger log = Logger.getLogger(MappingInsertTULetturaA.class);
	private  void LoadFile(File filename) {

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

	public  void associazioneMappingNomeVal(MappingTabella data) {
		// ciclo la lista degli elementi Lettura Acqua
		for (int j = 0; j < listLetturaAcqua.size(); j++) {

			List<Attributo> listAttributi = new ArrayList<Attributo>();

			// ciclo per crea listaATTRIBUTI richiesti dal mapping Lettura Acqua
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listLetturaAcqua.get(j).getValori().get(parts[1]) != null)
						&& (listLetturaAcqua.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listLetturaAcqua.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}
			}

			// riga di tipo RIGATABELLA per Lettura Acqua

			RigaTabella rigaTLA = new RigaTabella();
			rigaTLA.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTLA.setListaattributi(listAttributi);
			// CODICE UNIVOCO ??
			String idcodu = listLetturaAcqua.get(j).getValori().get("codutenza");
			String idmat = listLetturaAcqua.get(j).getValori().get("matricola");
			idmat=idmat.replaceAll("/", "_");
			idmat=idmat.replaceAll(" ", "_");
			
			rigaTLA.setUririga("http://dkm.fbk.eu/georeporter#LETACQ_" + idcodu + idmat);

			List<Relazione> listRelLA = new ArrayList<Relazione>();

			// relazione Lettura Acqua con U
			if (listLetturaAcqua.get(j).getValori().get("codutenza").isEmpty() == false) {
				Relazione relLAUA = new Relazione();
				relLAUA.setNomerelazione("http://dkm.fbk.eu/georeporter#hasUtenzaAcqua");
				// URI UTENZA ACQUA E LETTURA ACQUA
				relLAUA.setUriDomain("http://dkm.fbk.eu/georeporter#LETACQ_" + idcodu + idmat);
				relLAUA.setUriRange("http://dkm.fbk.eu/georeporter#TUUA_" + idcodu);
				listRelLA.add(relLAUA);
			}
			rigaTLA.setListarelazioni(listRelLA);

			insertRiga(rigaTLA);

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
public  void run(String filePath, String fileMappings) {
	//String path = "file/TN_file/TRAMBILENO_H2OExportlLETTURE_ixu3oqzmsnlv2bwhra5xvd3p1860500686.csv";
	// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
	wletturaacqua.estrazioneHeaderFile(filePath);
	wletturaacqua.letturaFile(filePath);
	listLetturaAcqua = wletturaacqua.listLetturaAcqua;
	// mapping e insert
//	LoadFile(new File("file/file_mapping/mappingLetturaAcqua.json"));
	LoadFile(new File(fileMappings));

	
	
}
	public static void main(String[] args) {

		String path = "file/TN_file/TRAMBILENO_H2OExportlLETTURE_ixu3oqzmsnlv2bwhra5xvd3p1860500686.csv";
		// chiamata ai metodi nel file WRAPPER estrazione HEADER ed estrazione elementi
	//	WrapperTULetturaA.estrazioneHeaderFile(path);
	//	WrapperTULetturaA.letturaFile(path);
		// mapping e insert
	//	LoadFile(new File("file/file_mapping/mappingLetturaAcqua.json"));

	}

}
