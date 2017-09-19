package eu.fbk.dkm.georeporter.pojos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class FornituraLocazioni {

	
	String denominazione;
	String comune;
	String codComune;
	Integer numeroMesiFatturazione;
	Integer idSiatelLocazioni;
	Integer ammontareFatturato;
	Integer anno;
	Integer numero;
	String  tipoCanone;
	Integer codiceNegozio;
	Integer importocanone;
	
	
}
