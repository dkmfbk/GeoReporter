package eu.fbk.dkm.georeporter.tn.wrappers;

import java.util.Map;

public class GetCoordinates {


	public static Map<String, Double> indirizzoLatLong(String val) {
		//System.out.println(val);
		Map<String, Double> coords;
		coords = OpenStreetMapUtils.getInstance().getCoordinates(val);
		System.out.println("latitude :" + coords.get("lat"));
		System.out.println("longitude:" + coords.get("lon"));
		return coords;
	}

	public static void main(String[] args) {

		GetCoordinates gc = new GetCoordinates();
		
		try {
			gc.indirizzoLatLong("via de Gasperi ,Trento");
			Thread.sleep(2000);
			gc.indirizzoLatLong("Trento via alcide de gasperi");
			Thread.sleep(2000);
			gc.indirizzoLatLong("via sabbioni  5/b Trento");
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		
	}
}