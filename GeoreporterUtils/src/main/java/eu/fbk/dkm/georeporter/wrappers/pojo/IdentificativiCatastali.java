package eu.fbk.dkm.georeporter.wrappers.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class IdentificativiCatastali {

	
	
	
	String codiceamministrativo;
	String sezione;
	String identificativoImmobile;
	String tipoimmobile;
	String progressivo;
    String tiporecord;
    String uri;
	String uriImmobile;
	String uriparticella;
	public String getUriparticella() {
		return uriparticella;
	}

	public void setUriparticella(String uriparticella) {
		this.uriparticella = uriparticella;
	}
	String codicecomunecatastale;
	String foglio;
	String numero;
	String denominatore;
	
	

	public String getCodicecomunecatastale() {
		return codicecomunecatastale;
	}

	public void setCodicecomunecatastale(String codicecomunecatastale) {
		this.codicecomunecatastale = codicecomunecatastale;
	}

	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getDenominatore() {
		return denominatore;
	}

	public void setDenominatore(String denominatore) {
		this.denominatore = denominatore;
	}
	Map <String,String>valori=new HashMap<String,String>();
	
	
	
	
	public Map<String, String> getValori() {
		return valori;
	}

	public void setValori(Map<String, String> valori) {
		this.valori = valori;
	}

	public IdentificativiCatastali() {
	
		// TODO Auto-generated constructor stub
	}
	
	public String getUriImmobile() {
		return uriImmobile;
	}
	public void setUriImmobile(String uriImmobile) {
		this.uriImmobile = uriImmobile;
	}
	
	
	
	
	
	public IdentificativiCatastali(String codiceamministrativo, String sezione, String identificativoImmobile,
			String tipoimmobile, String progressivo, String tiporecord) {
		super();
		this.codiceamministrativo = codiceamministrativo;
		this.sezione = sezione;
		this.identificativoImmobile = identificativoImmobile;
		this.tipoimmobile = tipoimmobile;
		this.progressivo = progressivo;
		this.tiporecord = tiporecord;
		setUriImmobile(codiceamministrativo+identificativoImmobile);
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


	
	
	
	
	
	
}