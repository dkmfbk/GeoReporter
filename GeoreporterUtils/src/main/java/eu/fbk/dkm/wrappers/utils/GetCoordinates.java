package eu.fbk.dkm.wrappers.utils;

import java.util.Map;

public class GetCoordinates {

    static String address = "Trento Sabbioni 11";

    public static void main(String[] args) {
        Map<String, Double> coords;
        coords = OpenStreetMapUtils.getInstance().getCoordinates(address);
        System.out.println("latitude :" + coords.get("lat"));
        System.out.println("longitude:" + coords.get("lon"));
    }
}