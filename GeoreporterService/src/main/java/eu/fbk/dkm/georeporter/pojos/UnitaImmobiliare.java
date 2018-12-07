package eu.fbk.dkm.georeporter.pojos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class UnitaImmobiliare {

	
	
	String uri;
	String ui;
	String piano;
	String interno;
	String scala;
	String categoria;
	String titolare;
	String superficie;
	String renditaEuro;
	String valoreIMIS;
	
	String numero;
	String denominatore;
	String subalterno;
	
	
	Soggetto stitolare;
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getPiano() {
		return piano;
	}
	public void setPiano(String piano) {
		this.piano = piano;
	}
	public String getInterno() {
		return interno;
	}
	public void setInterno(String interno) {
		this.interno = interno;
	}
	public String getScala() {
		return scala;
	}
	public void setScala(String scala) {
		this.scala = scala;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public Soggetto getTitolare() {
		return stitolare;
	}
	public void setTitolare(String string) {
		this.titolare = string;
	}
	public Soggetto getStitolare() {
		return stitolare;
	}
	public void setStitolare(Soggetto stitolare) {
		this.stitolare = stitolare;
	}
	public String getSuperficie() {
		return superficie;
	}
	public void setSuperficie(String superficie) {
		this.superficie = superficie;
	}
	public String getUi() {
		return ui;
	}
	public void setUi(String ui) {
		this.ui = ui;
	}
	public String getRenditaEuro() {
		return renditaEuro;
	}
	public void setRenditaEuro(String renditaEuro) {
		this.renditaEuro = renditaEuro;
	}
	public String getValoreIMIS() {
		return valoreIMIS;
	}
	public void setValoreIMIS(String valoreIMIS) {
		this.valoreIMIS = valoreIMIS;
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
	public String getSubalterno() {
		return subalterno;
	}
	public void setSubalterno(String subalterno) {
		this.subalterno = subalterno;
	}
	
}
