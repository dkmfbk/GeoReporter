package eu.fbk.dkm.georeporter.pojos;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class Particella {

	String uri;
	String comuneCatastale;
	String foglio;
	String numero;
	String denominatore;
	String subalterno;
	String porzionemateriale;
	String ui;
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getComuneCatastale() {
		return comuneCatastale;
	}
	public void setComuneCatastale(String comuneCatastale) {
		this.comuneCatastale = comuneCatastale;
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

	public String getSubalterno() {
		return subalterno;
	}
	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}
	public String getPorzionemateriale() {
		return porzionemateriale;
	}
	public void setPorzionemateriale(String porzionemateriale) {
		this.porzionemateriale = porzionemateriale;
	}
	public String getUi() {
		return ui;
	}
	public void setUi(String ui) {
		this.ui = ui;
	}
	public String getDenominatore() {
		return denominatore;
	}
	public void setDenominatore(String denominatore) {
		this.denominatore = denominatore;
	}
	
	
	
	
}
