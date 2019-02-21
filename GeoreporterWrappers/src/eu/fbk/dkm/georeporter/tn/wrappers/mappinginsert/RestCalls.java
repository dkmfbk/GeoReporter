package eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import eu.fbk.dkm.georeporter.config.Costanti;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.IDCatastale;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class RestCalls {
	
	
	
	
	public String serverurl;
	
	
	public RestCalls(String serverurl) {
		super();
		this.serverurl = serverurl;
	}

	public RestCalls() {
		Costanti cost= new Costanti();
		this.serverurl=cost.serveraddress+cost.restservicebaseaddress;
		//this.serverurl = "http://localhost:8081/GeoreporterService/servizio/rest/";
	}

	public  IDCatastale getIDCatastale(String codiceamministrativo,String comunecatastale, String numero,String denominatore, String subalterno) {

		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
		String targetURL = serverurl+"icdacampi";	
		Gson gson = new Gson();
		IDCatastale reply = new IDCatastale();
		reply.setId("http://dkm.fbk.eu/georeporter#A0_C0_N0_D0_S0");
           try {
			URL targetUrl = new URL(targetURL);
            ClientConfig clientConfig = new DefaultClientConfig();
            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            Client client = Client.create(clientConfig);
            WebResource webResourceGet = client.resource(targetURL)
            	   .queryParam("codiceamministrativo", codiceamministrativo)
            	   .queryParam("comunecatastale", comunecatastale)
        		   .queryParam("numero", numero)
        		   .queryParam("denominatore", denominatore)
        		   .queryParam("subalterno", subalterno);
            
            
         
          ClientResponse response = webResourceGet.get(ClientResponse.class);
          String risposta= webResourceGet.get(String.class);
          System.out.println( "risposta="+risposta.toString());
          
         if (risposta.equals("null")) {
        	 return reply;
         }
       ObjectMapper mapper = 
        		    new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

         JsonNode rootNode;
		try {
			rootNode = mapper.readTree(risposta);
			if (rootNode instanceof ArrayNode) {
	             // Read the json as a list:
	             IDCatastale[] objects = mapper.readValue(rootNode.toString(), IDCatastale[].class);
	            
	             for (int i = 0; i < objects.length; i++) {
					if (objects[i].getTipoParticella().equals("E"))
						return objects[i];
	             }
	            
		     	for (int i = 0; i < objects.length; i++) {
			    	if (objects[i].getTipoParticella().equals("F"))
						return objects[i];
	             }
	         } else if (rootNode instanceof JsonNode) {
	             // Read the json as a single object:
	        	 JsonNode ic=rootNode.get("idCatastale");
	        	 
	        	 IDCatastale objectIC = new IDCatastale();
	        	 objectIC.setId(ic.get("id").asText());
	        	 objectIC.setTipoParticella(ic.get("tipoParticella").asText());
	        	 
	        	 
	            return objectIC;
	         
	         }
	
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

           
           if (response.getStatus() != 200) {
               throw new WebApplicationException();
           }

          
           
           
     //      if (response.getStatus() != 200) {
      //     	 WebApplicationException e = response.getEntity(WebApplicationException.class);
       //    	 System.out.println(e.toString());
        
        //List<IDCatastale>   risultato = response.getEntity();
           
          
                   
           client.destroy();
           } catch (MalformedURLException e1) {
   			// TODO Auto-generated catch block
   			e1.printStackTrace();
   		}
           return reply;
	   }
    
	
	
	
		
	public  void insertRiga(RigaTabella riga) {

		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
		//String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";	
	
		Gson gson = new Gson();
		String json = gson.toJson(riga);
          
			//URL targetUrl = new URL(serverurl+);
		
           ClientConfig clientConfig = new DefaultClientConfig();
           clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
           Client client = Client.create(clientConfig);

           WebResource webResourcePost = client.resource(serverurl+"insertable");
           ClientResponse  response = webResourcePost.type("application/json").post(ClientResponse.class, json);
         
           if (response.getStatus() != 200) {
           	 WebApplicationException e = response.getEntity(WebApplicationException.class);
           	 System.out.println(e.toString());
          }
           String responseEntity = response.getEntity(String.class);
           
          
          // System.out.println(responseEntity.toString());
           
           client.destroy();
        
           
	   }
    	
 
 
 public static void main(String[] args) {

	    try {
	    	
	     RestCalls rc = new RestCalls();
	
	         
	     
	    IDCatastale json= rc.getIDCatastale("L322","404","162","1","1");
	    
	    	System.out.println("ID= "+json.getId());
	    	System.out.println("TipoParticella= "+json.getTipoParticella()
	    	);
	    	
	    	
	    	  
	    	//   IDCatastale attr = gson.fromJson(json, IDCatastale.class);
	         //  System.out.println(attr.getId());
	          // System.out.println(attr.getTipoParticella());
	           
	      } catch (Exception e) {

	        e.printStackTrace();

	      }

	    }
}