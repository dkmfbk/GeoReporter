package eu.fbk.dkm.georeporter.ita.wrappers.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Nota {
	
	Map<String, String> notaIniziale = new HashMap<String, String>();

	public Map<String, String> getNotaIniziale() {
		return notaIniziale;
	}

	public void setNotaIniziale(Map<String, String> notaIniziale) {
		this.notaIniziale = notaIniziale;
	}
	
	Map<String, String> notaFinale = new HashMap<String, String>();

	public Map<String, String> getNotaFinale() {
		return notaFinale;
	}

	public void setNotaFinale(Map<String, String> notaFinale) {
		this.notaFinale = notaFinale;
	}
	
	public Nota() {
	
		// TODO Auto-generated constructor stub
	}
	
}