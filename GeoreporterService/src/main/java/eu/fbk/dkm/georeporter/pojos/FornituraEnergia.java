package eu.fbk.dkm.georeporter.pojos;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
//@XmlType(propOrder = { "nome", "cognome", "denominazionetit","kwFatturato", "spesaConsumo" ,"denominazione"})
public class FornituraEnergia {

	String uri;
	String nome;
	String cognome;
	String denominazionetit;
	
	String denominazione;
	String comune;
	String codComune;
	Integer numeroMesiFatturazione;
	Integer idSiatelEnergiaDettaglio;
	Integer ammontareFatturato;
	Integer consumoFatturato;
	String  tipoUtenzaEnergia;
	Integer kwFatturato;
	Integer spesaConsumo;
	Integer idSiatelEnergia;

	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public String getComune() {
		return comune;
	}
	public void setComune(String comune) {
		this.comune = comune;
	}
	public String getCodComune() {
		return codComune;
	}
	public void setCodComune(String codComune) {
		this.codComune = codComune;
	}
	public Integer getNumeroMesiFatturazione() {
		return numeroMesiFatturazione;
	}
	public void setNumeroMesiFatturazione(Integer numeroMesiFatturazione) {
		this.numeroMesiFatturazione = numeroMesiFatturazione;
	}
	public Integer getIdSiatelEnergiaDettaglio() {
		return idSiatelEnergiaDettaglio;
	}
	public void setIdSiatelEnergiaDettaglio(Integer idSiatelEnergiaDettaglio) {
		this.idSiatelEnergiaDettaglio = idSiatelEnergiaDettaglio;
	}
	public Integer getAmmontareFatturato() {
		return ammontareFatturato;
	}
	public void setAmmontareFatturato(Integer ammontareFatturato) {
		this.ammontareFatturato = ammontareFatturato;
	}
	public Integer getConsumoFatturato() {
		return consumoFatturato;
	}
	public void setConsumoFatturato(Integer consumoFatturato) {
		this.consumoFatturato = consumoFatturato;
	}
	public String getTipoUtenzaEnergia() {
		return tipoUtenzaEnergia;
	}
	public void setTipoUtenzaEnergia(String tipoUtenzaEnergia) {
		this.tipoUtenzaEnergia = tipoUtenzaEnergia;
	}
	public Integer getKwFatturato() {
		return kwFatturato;
	}
	public void setKwFatturato(Integer kwFatturato) {
		this.kwFatturato = kwFatturato;
	}
	public Integer getSpesaConsumo() {
		return spesaConsumo;
	}
	public void setSpesaConsumo(Integer spesaConsumo) {
		this.spesaConsumo = spesaConsumo;
	}
	public Integer getIdSiatelEnergia() {
		return idSiatelEnergia;
	}
	public void setIdSiatelEnergia(Integer idSiatelEnergia) {
		this.idSiatelEnergia = idSiatelEnergia;
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
	public String getDenominazionetit() {
		return denominazionetit;
	}
	public void setDenominazionetit(String denominazionetit) {
		this.denominazionetit = denominazionetit;
	}
	
	
}
