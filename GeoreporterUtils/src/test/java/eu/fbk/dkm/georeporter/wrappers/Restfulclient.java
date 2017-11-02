package eu.fbk.dkm.georeporter.wrappers;


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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sun.jersey.api.client.WebResource;

import eu.fbk.dkm.georeporter.interfaces.Attributo;
import eu.fbk.dkm.georeporter.interfaces.Attributo2;
import eu.fbk.dkm.georeporter.interfaces.MappingTabella;
import eu.fbk.dkm.georeporter.interfaces.RigaTabella;

	public class Restfulclient {

	    private static final String targetURL = "http://localhost.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";

	    		
	    		
private static void LoadFile (File filename){
	
	
	Gson gson = new Gson();
	JsonReader reader;
	try {
		reader = new JsonReader(new FileReader(filename));
		MappingTabella data = gson.fromJson(reader, MappingTabella.class); 
	//	for (Attributo2 attributo : data) {
			System.out.println(data.getId().getNome());
	//	}
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
}
	    		
	  private static RigaTabella createDummyRecord() {

	    	RigaTabella rec = new RigaTabella();
	       rec.setNometabella("http://dkm.fbk.eu/georeporter#UnitaImmobiliare");
	       rec.setUririga("http://dkm.fbk.eu/georeporter#aaaaaaa0001234tizsllkg");
          
	      	List <Attributo> listachiave = new ArrayList<Attributo>();
	        
	        Attributo attr1 = new Attributo();
	        attr1.setNome("http://dkm.fbk.eu/georeporter#identificativoimmobile");
	        attr1.setMapping("http://dkm.fbk.eu/georeporter#identificativoimmobile");
	        attr1.setValore("11111111.23");
	        attr1.setTipo("http://www.w3.org/2001/XMLSchema#float");
	        
	        Attributo attr2 = new Attributo();
	        attr2.setNome("http://dkm.fbk.eu/georeporter#codiceamministrativo");
	        attr2.setMapping("http://dkm.fbk.eu/georeporter#codiceamministrativo");
	       
	        attr2.setValore("L322");
	        attr2.setTipo("http://www.w3.org/2001/XMLSchema#string");
	        
	        listachiave.add(attr1);
	        listachiave.add(attr2);
	       
	        rec.setListachiave(listachiave);
	        
	        return rec;
	    		 }	
	    		
	    		
	    		
	    public static void main(String[] args) {

	    	RigaTabella rec=createDummyRecord();
            LoadFile(new File("mappingUI.json"));

	    	Gson gson = new Gson();
	    	String json = gson.toJson(rec);

	    	
	    	
	        try {

	            URL targetUrl = new URL(targetURL);

	            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
	            httpConnection.setDoOutput(true);
	            httpConnection.setRequestMethod("POST");
	            httpConnection.setRequestProperty("Content-Type", "application/json");
	            httpConnection.setRequestProperty("Accept", "application/json");

//	            String input = "{\"id\":1,\"firstName\":\"Liam\",\"age\":22,\"lastName\":\"Marco\"}";
	            String input = json;

	            OutputStream outputStream = httpConnection.getOutputStream();
	            outputStream.write(input.getBytes());
	            outputStream.flush();

	            if (httpConnection.getResponseCode() != 200) {
	                throw new RuntimeException("Failed : HTTP error code : "
	                    + httpConnection.getResponseCode());
	            }

	            BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(
	                    (httpConnection.getInputStream())));

	            String output;
	            System.out.println("Output from Server:\n");
	            while ((output = responseBuffer.readLine()) != null) {
	                System.out.println(output);
	            }

	            httpConnection.disconnect();

	          } catch (MalformedURLException e) {

	            e.printStackTrace();

	          } catch (IOException e) {

	            e.printStackTrace();

	         }

	        }    
	    }