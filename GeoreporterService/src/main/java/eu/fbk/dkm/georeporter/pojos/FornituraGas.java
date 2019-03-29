package eu.fbk.dkm.georeporter.pojos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class FornituraGas {

	
	String Denominazione;
	String Comune;
	String CodComune;
	Integer NumeroMesiFatturazione;
	Integer IdSiatelGasDettaglio;
	Integer AmmontareFatturato;
	Integer ConsumoFatturato;
	String TipoUtenzaGas;
	Integer IdSiatelGas;
	public String getDenominazione() {
		return Denominazione;
	}
	public void setDenominazione(String denominazione) {
		Denominazione = denominazione;
	}
	public String getComune() {
		return Comune;
	}
	public void setComune(String comune) {
		Comune = comune;
	}
	public String getCodComune() {
		return CodComune;
	}
	public void setCodComune(String codComune) {
		CodComune = codComune;
	}
	public Integer getNumeroMesiFatturazione() {
		return NumeroMesiFatturazione;
	}
	public void setNumeroMesiFatturazione(Integer numeroMesiFatturazione) {
		NumeroMesiFatturazione = numeroMesiFatturazione;
	}
	public Integer getIdSiatelGasDettaglio() {
		return IdSiatelGasDettaglio;
	}
	public void setIdSiatelGasDettaglio(Integer idSiatelGasDettaglio) {
		IdSiatelGasDettaglio = idSiatelGasDettaglio;
	}
	public Integer getAmmontareFatturato() {
		return AmmontareFatturato;
	}
	public void setAmmontareFatturato(Integer ammontareFatturato) {
		AmmontareFatturato = ammontareFatturato;
	}
	public Integer getConsumoFatturato() {
		return ConsumoFatturato;
	}
	public void setConsumoFatturato(Integer consumoFatturato) {
		ConsumoFatturato = consumoFatturato;
	}
	public String getTipoUtenzaGas() {
		return TipoUtenzaGas;
	}
	public void setTipoUtenzaGas(String tipoUtenzaGas) {
		TipoUtenzaGas = tipoUtenzaGas;
	}
	public Integer getIdSiatelGas() {
		return IdSiatelGas;
	}
	public void setIdSiatelGas(Integer idSiatelGas) {
		IdSiatelGas = idSiatelGas;
	}
	
	
}
