package eu.fbk.dkm.georeporter.pojos;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })

public class Locazione {

	
	String uri;
	Integer codiceUtenza;
	Date dataInizio;
	Date datafine;
	String codiceCondominio;
	String categoria;
	String interno;
	String occupante;
	Integer mqLocaliPertinenza;
	Integer mqLocali;
	Integer mqCatastali;
	Integer superficieCatastale;
	String indirizzoCompleto;
	String TipoUtenza;
	String categoriaDescrizione;
	Integer	annoLocazione;
    Integer codiceNegozio;
    String denominazioneFile;
    Integer idSiatelLocazioni;
    Float importoCanone;
    String	tipoCanone;
    String 	descrizione;
    Integer numero;
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public Integer getCodiceUtenza() {
		return codiceUtenza;
	}
	public void setCodiceUtenza(Integer codiceUtenza) {
		this.codiceUtenza = codiceUtenza;
	}
	public Date getDataInizio() {
		return dataInizio;
	}
	public void setDataInizio(Date dataInizio) {
		this.dataInizio = dataInizio;
	}
	public Date getDatafine() {
		return datafine;
	}
	public void setDatafine(Date datafine) {
		this.datafine = datafine;
	}
	public String getCodiceCondominio() {
		return codiceCondominio;
	}
	public void setCodiceCondominio(String codiceCondominio) {
		this.codiceCondominio = codiceCondominio;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getInterno() {
		return interno;
	}
	public void setInterno(String interno) {
		this.interno = interno;
	}
	public String getOccupante() {
		return occupante;
	}
	public void setOccupante(String occupante) {
		this.occupante = occupante;
	}
	public Integer getMqLocaliPertinenza() {
		return mqLocaliPertinenza;
	}
	public void setMqLocaliPertinenza(Integer mqLocaliPertinenza) {
		this.mqLocaliPertinenza = mqLocaliPertinenza;
	}
	public Integer getMqLocali() {
		return mqLocali;
	}
	public void setMqLocali(Integer mqLocali) {
		this.mqLocali = mqLocali;
	}
	public Integer getMqCatastali() {
		return mqCatastali;
	}
	public void setMqCatastali(Integer mqCatastali) {
		this.mqCatastali = mqCatastali;
	}
	public Integer getSuperficieCatastale() {
		return superficieCatastale;
	}
	public void setSuperficieCatastale(Integer superficieCatastale) {
		this.superficieCatastale = superficieCatastale;
	}
	public String getIndirizzoCompleto() {
		return indirizzoCompleto;
	}
	public void setIndirizzoCompleto(String indirizzoCompleto) {
		this.indirizzoCompleto = indirizzoCompleto;
	}
	public String getTipoUtenza() {
		return TipoUtenza;
	}
	public void setTipoUtenza(String tipoUtenza) {
		TipoUtenza = tipoUtenza;
	}
	public String getCategoriaDescrizione() {
		return categoriaDescrizione;
	}
	public void setCategoriaDescrizione(String categoriaDescrizione) {
		this.categoriaDescrizione = categoriaDescrizione;
	}
	public Integer getAnnoLocazione() {
		return annoLocazione;
	}
	public void setAnnoLocazione(Integer annoLocazione) {
		this.annoLocazione = annoLocazione;
	}
	public Integer getCodiceNegozio() {
		return codiceNegozio;
	}
	public void setCodiceNegozio(Integer codiceNegozio) {
		this.codiceNegozio = codiceNegozio;
	}
	public String getDenominazioneFile() {
		return denominazioneFile;
	}
	public void setDenominazioneFile(String denominazioneFile) {
		this.denominazioneFile = denominazioneFile;
	}
	public Integer getIdSiatelLocazioni() {
		return idSiatelLocazioni;
	}
	public void setIdSiatelLocazioni(Integer idSiatelLocazioni) {
		this.idSiatelLocazioni = idSiatelLocazioni;
	}
	public Float getImportoCanone() {
		return importoCanone;
	}
	public void setImportoCanone(Float importoCanone) {
		this.importoCanone = importoCanone;
	}
	public String getTipoCanone() {
		return tipoCanone;
	}
	public void setTipoCanone(String tipoCanone) {
		this.tipoCanone = tipoCanone;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public Integer getNumero() {
		return numero;
	}
	public void setNumero(Integer numero) {
		this.numero = numero;
	}


	
}
