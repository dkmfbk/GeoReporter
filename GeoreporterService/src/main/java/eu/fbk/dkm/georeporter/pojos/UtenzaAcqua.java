package eu.fbk.dkm.georeporter.pojos;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class UtenzaAcqua {

	
	String uri;
	Integer codiceUtenza;
	String dataInizio;
	String datafine;
	String CodiceCondominio;
	String Categoria;
	String CategoriaDescrizione;
	String Contribuente;
	String Interno;
	String notabreve;
	String codicefiscale;
	Integer UtenzaCondominiale;
	String NumeroDipendenze;
	
	public Integer getCodiceUtenza() {
		return codiceUtenza;
	}
	public void setCodiceUtenza(Integer codiceUtenza) {
		this.codiceUtenza = codiceUtenza;
	}

	public String getCodiceCondominio() {
		return CodiceCondominio;
	}
	public void setCodiceCondominio(String codiceCondominio) {
		CodiceCondominio = codiceCondominio;
	}
	public String getCategoria() {
		return Categoria;
	}
	public void setCategoria(String categoria) {
		Categoria = categoria;
	}
	public String getCategoriaDescrizione() {
		return CategoriaDescrizione;
	}
	public void setCategoriaDescrizione(String categoriaDescrizione) {
		CategoriaDescrizione = categoriaDescrizione;
	}

	public String getInterno() {
		return Interno;
	}
	public void setInterno(String interno) {
		Interno = interno;
	}
	public Integer getUtenzaCondominiale() {
		return UtenzaCondominiale;
	}
	public void setUtenzaCondominiale(Integer utenzaCondominiale) {
		UtenzaCondominiale = utenzaCondominiale;
	}
	public String getNumeroDipendenze() {
		return NumeroDipendenze;
	}
	public void setNumeroDipendenze(String numeroDipendenze) {
		NumeroDipendenze = numeroDipendenze;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getContribuente() {
		return Contribuente;
	}
	public void setContribuente(String contribuente) {
		Contribuente = contribuente;
	}
	public String getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(String dataInizio) {
		this.dataInizio = dataInizio;
	}
	public String getDatafine() {
		return datafine;
	}
	public void setDatafine(String datafine) {
		this.datafine = datafine;
	}
	public String getNotabreve() {
		return notabreve;
	}
	public void setNotabreve(String notabreve) {
		this.notabreve = notabreve;
	}
	public String getCodicefiscale() {
		return codicefiscale;
	}
	public void setCodicefiscale(String codicefiscale) {
		this.codicefiscale = codicefiscale;
	}
	
	
}
