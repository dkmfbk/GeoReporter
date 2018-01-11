package eu.fbk.dkm.georeporter.tn.wrappers;

import java.util.Map;

public class GetCoordinates {


	public static Map<String, Double> indirizzoLatLong(String val) {
		//System.out.println(val);
		Map<String, Double> coords;
		coords = OpenStreetMapUtils.getInstance().getCoordinates(val);
		//System.out.println("latitude :" + coords.get("lat"));
		//System.out.println("longitude:" + coords.get("lon"));
		return coords;
	}

	public static void main(String[] args) {

	}
}