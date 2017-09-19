package eu.fbk.dkm.georeporter.pojos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class Famiglia {

	
	Integer numeroComponenti;
	Integer numMinori;
	Integer NumFigli;
	Boolean monoparentale;
	
	
}
