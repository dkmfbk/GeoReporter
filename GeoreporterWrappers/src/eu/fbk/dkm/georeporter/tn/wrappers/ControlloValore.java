package eu.fbk.dkm.georeporter.tn.wrappers;

public class ControlloValore {

	public static String controlloValore(String valore) {
		return valore.trim();
	}

	public static String puliziaHeader(String tmp) {
		return tmp.replaceAll("[ /._\"]", "").toLowerCase();
	}

	public static String cambioData(String data) {
		String newdata = data.substring(4, 8) + "-" + data.substring(2, 4) + "-" + data.substring(0, 2);
		return newdata;
	}

	public static void main(String[] args) {
	}

}
