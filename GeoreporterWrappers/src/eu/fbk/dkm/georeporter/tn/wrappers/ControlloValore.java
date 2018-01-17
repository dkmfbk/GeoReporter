package eu.fbk.dkm.georeporter.tn.wrappers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ControlloValore {

	public static String controlloVIR(String val) {
		String[] tmp = val.split("\\.", -1);
		if ((tmp.length == 2) && (tmp[1].equals("0"))) {
			return tmp[0];
		} else {
			return val;
		}
	}

	public static String controlloData(String data) {
		String tmp2 = "";
		if (!data.isEmpty()) {
			DateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
			format.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date date = null;
			try {
				date = format.parse(data);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			String reportDate = df.format(date);
			String[] tmp = reportDate.split("[/ ]");
			tmp2 = tmp[2] + "-" + tmp[0] + "-" + tmp[1];
		}
		return tmp2;
	}

	public static String controlloDataEXCEL(Date data) {
		String reportDate = "";
		if (data != null) {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			reportDate = format.format(data);
		}

		return reportDate;
	}

	public static String TolgoZeri(String valore) {
		String tmp = valore;
		if ((!valore.isEmpty()) && (valore.substring(0, 1).equals("0"))) {
			tmp = (Integer.toString(Integer.parseInt(valore)));
		}
		return tmp;
	}

	public static String controlloValore(String valore) {
		return valore.trim();
	}

	public static String puliziaHeader(String tmp) {
		return tmp.replaceAll("[ /._\"]", "").toLowerCase();
	}

	public static String cambioData(String data) {
		String newdata = data;
		if (!data.isEmpty()) {
			newdata = data.substring(4, 8) + "-" + data.substring(2, 4) + "-" + data.substring(0, 2);
		}
		return newdata;
	}

	public static String dataANACU(String data) {
		String newdata = data;
		if (data.length() == 8) {
			newdata = data.substring(0, 4) + "-" + data.substring(4, 6) + "-" + data.substring(6, 8);
		}
		return newdata;
	}

	public static String dataBarrataOra(String data) {
		String tmp2 = "";
		if (!data.isEmpty()) {
			String[] tmp = data.split("[/ ]");
			tmp2 = tmp[2] + "-" + tmp[1] + "-" + tmp[0];
		}
		return tmp2;
	}

	public static String dataBarrata(String data) {
		String[] tmp = data.split("/");
		String tmp2 = tmp[2] + "-" + tmp[1] + "-" + tmp[0];
		return tmp2;
	}

	public static void main(String[] args) {
	}

}
