package eu.dbk.dkm.georeporter.wrappers.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class UnitaImmobiliare {

	
	String uri;
	String identificativoImmobile;
	String codiceamministrativo;
	String sezione;
	String tipoimmobile;
	String progressivo;
    String tiporecord;
    Map <String,String> valori= new HashMap<String,String>();
	public Map<String, String> getValori() {
		return valori;
	}
	public void setValori(Map<String, String> valori) {
		this.valori = valori;
	}
	public UnitaImmobiliare() {
	
		// TODO Auto-generated constructor stub
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getIdentificativoImmobile() {
		return identificativoImmobile;
	}
	public void setIdentificativoImmobile(String identificativoImmobile) {
		this.identificativoImmobile = identificativoImmobile;
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
	public String getTipoimmobile() {
		return tipoimmobile;
	}
	public void setTipoimmobile(String tipoimmobile) {
		this.tipoimmobile = tipoimmobile;
	}
	public String getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(String progressivo) {
		this.progressivo = progressivo;
	}
	public String getTiporecord() {
		return tiporecord;
	}
	public void setTiporecord(String tiporecord) {
		this.tiporecord = tiporecord;
	}
	public List<String> getValoriRiga() {
		return valoriRiga;
	}
	public void setValoriRiga(List<String> valoriRiga) {
		this.valoriRiga = valoriRiga;
	}
	List<String> valoriRiga= new ArrayList();

	
	
	
	
	
	
}