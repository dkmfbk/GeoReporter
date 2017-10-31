package eu.fbk.dkm.georeporter.wrappers;

import java.io.IOException;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class TestGeoDecoder {

	
public void doit(){	
	final Geocoder geocoder = new Geocoder(); 
	GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress("via de gasperi trento").setLanguage("it").getGeocoderRequest(); 
	Gson gson = new Gson();

	try {
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
		String json = gson.toJson(geocoderResponse);
		GeocodeResponse roundtrip = gson.fromJson(json, GeocodeResponse.class);

		if(!geocoderResponse.equals(roundtrip)) {
			//logger.error("Roundtrip failed for: " + line);
		}
		
System.out.println( json);
		
		//System.out.println(geocoderResponse);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
 }

public static void main(String[] args){
	
	
	TestGeoDecoder t = new TestGeoDecoder();
	t.doit();
	
	
	
}
}
