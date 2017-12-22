package eu.fbk.dkm.georeporter.tn.wrappers.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class TitolaritaCompleta {

	Map<String, String> valori = new HashMap<String, String>();
	Intavolazione intavolazioneIniziale = new Intavolazione();
	Intavolazione intavolazioneFinale = new Intavolazione();
	List<Map<String, String>> ListaValoriChiave = new ArrayList<Map<String, String>>();

	public Map<String, String> getValori() {
		return valori;
	}

	public void setValori(Map<String, String> valori) {
		this.valori = valori;
	}

	public Intavolazione getIntavolazioneIniziale() {
		return intavolazioneIniziale;
	}

	public void setIntavolazioneIniziale(Intavolazione intavolazioneIniziale) {
		this.intavolazioneIniziale = intavolazioneIniziale;
	}

	public Intavolazione getIntavolazioneFinale() {
		return intavolazioneFinale;
	}

	public void setIntavolazioneFinale(Intavolazione intavolazioneFinale) {
		this.intavolazioneFinale = intavolazioneFinale;
	}
	public List<Map<String, String>> getListaValoriChiave() {
		return ListaValoriChiave;
	}

	public void setListaValoriChiave(List<Map<String, String>> ListaValoriChiave) {
		this.ListaValoriChiave = ListaValoriChiave;
	}

	public TitolaritaCompleta() {
		// TODO Auto-generated constructor stub
	}

}