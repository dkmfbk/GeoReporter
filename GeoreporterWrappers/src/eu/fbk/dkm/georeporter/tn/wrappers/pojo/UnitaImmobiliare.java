package eu.fbk.dkm.georeporter.tn.wrappers.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UnitaImmobiliare {
	
	Map<String, String> valori = new HashMap<String, String>();
	Nota notaIniziale = new Nota();
	Nota notaFinale = new Nota();
	List<Map<String, String>> ListaValoriChiave = new ArrayList<Map<String, String>>();

	public Map<String, String> getValori() {
		return valori;
	}

	public void setValori(Map<String, String> valori) {
		this.valori = valori;
	}
	
	public Nota getNotaIniziale() {
		return notaIniziale;
	}
	public void setNotaIniziale(Nota notaIniziale) {
		this.notaIniziale = notaIniziale;
	}
	public Nota getNotaFinale() {
		return notaFinale;
	}
	public void setNotaFinale(Nota notaFinale) {
		this.notaFinale = notaFinale;
	}
	
	public  List<Map<String, String>> getListaValoriChiave() {
		return ListaValoriChiave;
	}

	public void setListaValoriChiave( List<Map<String, String>> ListaValoriChiave) {
		this.ListaValoriChiave = ListaValoriChiave;
	}

	public UnitaImmobiliare() {
		// TODO Auto-generated constructor stub
	}

}