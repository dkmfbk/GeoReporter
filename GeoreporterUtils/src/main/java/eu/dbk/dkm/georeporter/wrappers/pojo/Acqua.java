package eu.dbk.dkm.georeporter.wrappers.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Acqua {

	
	String uri;
	String codiceutenza;
	String codutenza;
	String codcomune;
	String codicefiscale;
	String numero;
	String foglio;
	String particella;
	String subalterno;
	String comunecatastale;
	
	//public String getIdentificativoCatastale(){
		
	//	return  String uri="C"+fields[6]+"_F"+fields[7].replaceAll(",", "_").replaceAll("/", "_")+"_N"+fields[8]+"_D"+fields[9]+"_S"+fields[10];
	//}
	
	public String getFoglio() {
		return foglio;
	}

	public void setFoglio(String foglio) {
		this.foglio = foglio;
	}

	public String getParticella() {
		return particella;
	}

	public void setParticella(String particella) {
		this.particella = particella;
	}

	public String getSubalterno() {
		return subalterno;
	}

	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}

	public String getComunecatastale() {
		return comunecatastale;
	}

	public void setComunecatastale(String comunecatastale) {
		this.comunecatastale = comunecatastale;
	}
	List<String> valoriRiga= new ArrayList<String>();
	Map <String,String> valori= new HashMap<String,String>();
	
  



    public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getCodiceutenza() {
		return codiceutenza;
	}

	public void setCodiceutenza(String codiceutenza) {
		this.codiceutenza = codiceutenza;
	}

	public String getCodutenza() {
		return codutenza;
	}

	public void setCodutenza(String codutenza) {
		this.codutenza = codutenza;
	}

	public String getCodcomune() {
		return codcomune;
	}

	public void setCodcomune(String codcomune) {
		this.codcomune = codcomune;
	}

	public String getCodicefiscale() {
		return codicefiscale;
	}

	public void setCodicefiscale(String codicefiscale) {
		this.codicefiscale = codicefiscale;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Map<String, String> getValori() {
		return valori;
	}
	
	public void setValori(Map<String, String> valori) {
		this.valori = valori;
	}
	public Acqua() {
	
		// TODO Auto-generated constructor stub
	}

	public List<String> getValoriRiga() {
		return valoriRiga;
	}
	public void setValoriRiga(List<String> valoriRiga) {
		this.valoriRiga = valoriRiga;
	}


	
	
	
	
	
	
}