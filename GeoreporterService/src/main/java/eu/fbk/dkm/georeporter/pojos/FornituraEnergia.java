package eu.fbk.dkm.georeporter.pojos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class FornituraEnergia {

	
	String denominazione;
	String comune;
	String codComune;
	Integer numeroMesiFatturazione;
	Integer idSiatelEnergiaDettaglio;
	Integer ammontareFatturato;
	Integer consumoFatturato;
	String tipoUtenzaEnergia;
	Integer kwFatturato;
	Integer spesaConsumo;
	Integer idSiatelEnergia;
	
	
}
