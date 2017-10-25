package eu.dbk.dkm.georeporter.wrappers.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Titolarita {

	
	String uri;
	String identificativoSoggetto;
	String identificativoImmobile;
	String tipoImmobile;
	String codiceamministrativo;
	String sezione;
	String tipsoggetto;
	List<String> valoriRiga= new ArrayList<String>();
	Map <String,String> valori= new HashMap<String,String>();
	
    public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getIdentificativoSoggetto() {
		return identificativoSoggetto;
	}

	public void setIdentificativoSoggetto(String identificativoSoggetto) {
		this.identificativoSoggetto = identificativoSoggetto;
	}

	public String getIdentificativoImmobile() {
		return identificativoImmobile;
	}

	public void setIdentificativoImmobile(String identificativoImmobile) {
		this.identificativoImmobile = identificativoImmobile;
	}

	public String getTipoImmobile() {
		return tipoImmobile;
	}

	public void setTipoImmobile(String tipoImmobile) {
		this.tipoImmobile = tipoImmobile;
	}

	public String getCodiceamministrativo() {
		return codiceamministrativo;
	}

	public void setCodiceamministrativo(String codiceamministrativo) {
		this.codiceamministrativo = codiceamministrativo;
	}

	public String getSezione() {
		return sezione;
	}

	public void setSezione(String sezione) {
		this.sezione = sezione;
	}

	public String getTipsoggetto() {
		return tipsoggetto;
	}

	public void setTipsoggetto(String tipsoggetto) {
		this.tipsoggetto = tipsoggetto;
	}

	public Map<String, String> getValori() {
		return valori;
	}
	
	public void setValori(Map<String, String> valori) {
		this.valori = valori;
	}
	public Titolarita() {
	
		// TODO Auto-generated constructor stub
	}

	public List<String> getValoriRiga() {
		return valoriRiga;
	}
	public void setValoriRiga(List<String> valoriRiga) {
		this.valoriRiga = valoriRiga;
	}


	
	
	
	
	
	
}