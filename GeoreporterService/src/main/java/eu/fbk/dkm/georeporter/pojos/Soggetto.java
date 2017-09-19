package eu.fbk.dkm.georeporter.pojos;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class Soggetto {
	
	
	
	
	String uri;
	String codiceFiscale;
	String nome;
	String cognome;
	Date dataNascita;
	String codiceComuneDiNascita;
	String Sesso;
	Famiglia famiglia;
	Indirizzo indirizzoresidenza;
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public Date getDataNascita() {
		return dataNascita;
	}
	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getCodiceComuneDiNascita() {
		return codiceComuneDiNascita;
	}
	public void setCodiceComuneDiNascita(String codiceComuneDiNascita) {
		this.codiceComuneDiNascita = codiceComuneDiNascita;
	}
	public String getSesso() {
		return Sesso;
	}
	public void setSesso(String sesso) {
		Sesso = sesso;
	}
	public Famiglia getFamiglia() {
		return famiglia;
	}
	public void setFamiglia(Famiglia famiglia) {
		this.famiglia = famiglia;
	}
	public Indirizzo getIndirizzoresidenza() {
		return indirizzoresidenza;
	}
	public void setIndirizzoresidenza(Indirizzo indirizzoresidenza) {
		this.indirizzoresidenza = indirizzoresidenza;
	}
}