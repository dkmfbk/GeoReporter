package eu.fbk.dkm.georeporter.tn.wrappers.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtenzaRifiuti {

	Map<String, String> valori = new HashMap<String, String>();
	List<Map<String, String>> ListaValoriChiave = new ArrayList<Map<String, String>>();

	public Map<String, String> getValori() {
		return valori;
	}

	public void setValori(Map<String, String> valori) {
		this.valori = valori;
	}

	public List<Map<String, String>> getListaValoriChiave() {
		return ListaValoriChiave;
	}

	public void setListaValoriChiave(List<Map<String, String>> ListaValoriChiave) {
		this.ListaValoriChiave = ListaValoriChiave;
	}

	public UtenzaRifiuti() {
		// TODO Auto-generated constructor stub
	}

}