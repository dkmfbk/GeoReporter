package eu.fbk.dkm.georeporter.tn.wrappers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ControlloValore {

	public static String controlloVIR(String val) {
		String[] tmp = val.split("\\.",-1);
		if ((tmp.length == 2) && (tmp[1].equals("0"))) {
			return tmp[0];
		} else {
			return val;
		}
	}

	public static String controlloData(String data) {
		DateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		Date date = null;
		try {
			date = format.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String reportDate = df.format(date);
		String[] tmp = reportDate.split("[/ ]");
		String tmp2 = tmp[2] + "-" + tmp[0] + "-" + tmp[1];
		return tmp2;
	}

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
