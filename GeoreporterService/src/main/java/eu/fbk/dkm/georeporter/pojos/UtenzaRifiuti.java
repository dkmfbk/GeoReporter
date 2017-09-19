package eu.fbk.dkm.georeporter.pojos;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })

public class UtenzaRifiuti {

	
	
	Integer codiceUtenza;
	Date dataInizio;
	Date datafine;
	String CodiceCondominio;
	String Categoria;
	String Interno;
	String Occupante;
	Integer MqMin;
	Integer MQMax;
	Integer MqLocaliPertinenza;
	Integer MqLocali;
	Integer MqCatastali;
	Integer SuperficieCatastale;
	
	
	
	
}
