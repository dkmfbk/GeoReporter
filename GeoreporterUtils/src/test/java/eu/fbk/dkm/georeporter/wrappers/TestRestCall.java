package eu.fbk.dkm.georeporter.wrappers;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import eu.fbk.dkm.georeporter.interfaces.RigaTabella;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
public class TestRestCall {
	
	
	
	
	public void getJsonAsString(String uri){
		


		     Client client = Client.create();
		 
		     
		     WebResource webResource = client.resource("http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/unitaimmobiliari_su_particella?particella="+uri);
		     ClientResponse response = webResource.accept("application/json").get(ClientResponse.class);
		 
		     // Status 200 is successful.
		     if (response.getStatus() != 200) {
		         System.out.println("Failed with HTTP Error code: " + response.getStatus());
		         String error= response.getEntity(String.class);
		         System.out.println("Error: "+error);
		         return;
		     }
		 
		     String output = response.getEntity(String.class);
		 
		     System.out.println("Output from Server .... \n");
		     System.out.println(output);
		 }
		 
		
		
	public void putJson(String service){
		
		//service=unitaimmobiliare
		//service=particella
		//service=identificativocatastale
		//service=indirizzo
		
		   Client client = Client.create();

	        WebResource webResource = client.resource("http://localhost.fbk.eu:8080/GeoreporterService/servizio/rest/"+service);
	        //WebResource webResource = client.resource("http://localhost:8080/SpringlesREST/rest/rest/"+service);
	     
	   
RigaTabella rt= new RigaTabella();
	        
	        rt.setNometabella("UnitaImmobiliare");
	        rt.setUririga("ui_999999");
	        Gson gson = new Gson();
	        String json = gson.toJson(rt);
	     /*   JsonObject obj = new JsonObject();
	        obj.addProperty("idtabella","UnitaImmobiliare");
	        obj.addProperty("uriid","1234999");

	        obj.addProperty("http://dkm.fbk.eu/georeporter#codiceAmministrativo","1234999");
	        obj.addProperty("http://dkm.fbk.eu/georeporter#progressivo", "12345");
	        obj.addProperty("http://dkm.fbk.eu/georeporter#superficie", "205");*/
	        System.out.println(json);
	        
	        
	        //WebResource resource = c.resource("http://example.com/helloWorld");
	        MultivaluedMap queryParams = new MultivaluedMapImpl();
	        queryParams.add("input", json);
	     //   queryParams.add("param2", "val2");
	        String response = webResource.queryParams(queryParams).get(String.class);
	        
	        
	        
	      

	       // obj.addProperty("http://dkm.fbk.eu/georeporter#uiuri", ":123456933fsfsdff");
	        
	        //CAMPI OBBLIGATORI!!!
	      
	        
	  //      ClientResponse response = webResource.type("application/json")
	   //        .post(ClientResponse.class, obj.toString());*/

	     //   if (response.getStatus() != 200) {
	      //      throw new RuntimeException("Failed : HTTP error code : "
	       //          + response.getStatus()+ response.toString());
	        //}

	        System.out.println("Output from Server .... \n");
	   //     String output = response.getEntity(String.class);
	   //    System.out.println(output);
		
		
		
		
	}
	
 
 
 public static void main(String[] args) {

	    try {

	     TestRestCall trc = new TestRestCall();
	     
	    	//trc.getJsonAsString("pa_CL322_F3_N819_D");	
	    	trc.putJson("inserttable");
	      } catch (Exception e) {

	        e.printStackTrace();

	      }

	    }
}