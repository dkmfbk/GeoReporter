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
	
	
}
