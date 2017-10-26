package eu.fbk.dkm.georeporter.wrappers.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Particella {

	
	String uri;
	
	String comunecatastale;
	String sezione;
	String identificativoparticella;
	String tipoparticella;
	String progressivo;
    String tiporecord;
    String foglio;
    String codicecomunecatastale;
    String numero;
    String denominatore;
    String tipoparticellac;
    
    Map <String,String> valori= new HashMap<String,String>();
	public Map<String, String> getValori() {
		return valori;
	}
	public void setValori(Map<String, String> valori) {
		this.valori = valori;
	}
	public Particella() {
	
		// TODO Auto-generated constructor stub
	}

	
	public Particella(String uri) {
		
		this.uri=uri;// TODO Auto-generated constructor stub
	}
	public Particella( String codicecomunecatastale,String foglio, String numero, String denominatore) {
		super();
		this.foglio = foglio;
		this.codicecomunecatastale = codicecomunecatastale;
		this.numero = numero;
		this.denominatore = denominatore;
		this.uri="pa_C"+codicecomunecatastale+"_F"+foglio+"_N"+numero+"_D"+denominatore;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getComunecatastale() {
		return comunecatastale;
	}
	public void setComunecatastale(String comunecatastale) {
		this.comunecatastale = comunecatastale;
	}
	public String getSezione() {
		return sezione;
	}
	public void setSezione(String sezione) {
		this.sezione = sezione;
	}
	public String getIdentificativoparticella() {
		return identificativoparticella;
	}
	public void setIdentificativoparticella(String identificativoparticella) {
		this.identificativoparticella = identificativoparticella;
	}
	public String getTipoparticella() {
		return tipoparticella;
	}
	public void setTipoparticella(String tipoparticella) {
		this.tipoparticella = tipoparticella;
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
	
	
	public String stampa(){
		String result="";
		
		result = " :codicecomunecatastale \""+this.codicecomunecatastale+"\" ;"
		        +" :foglio \""+this.foglio+"\" ;"
		        +" :numero \""+this.numero+"\" ";
		if (!this.denominatore.equals("")){
		       result= result+"; :denominatore \""+this.denominatore+"\" ";
		}
		result = result+" . \n";
		return result;
	}
	
	
}