package eu.fbk.dkm.georeporter.pojos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class IdentificativoCatastale {


	String comuneCatastale;
	String foglio;
	String numero;
	Integer denomintore;
	String subalterno;
	String porzionemateriale;
	Particella hasParticella;
	UnitaImmobiliare hasUnitaImmobiliare;
	
	
}
