package eu.fbk.dkm.georeporter.pojos;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class Relazione {


	String classe1;
	String relazione;
	String classe2;
	public String getClasse1() {
		return classe1;
	}
	public void setClasse1(String classe1) {
		this.classe1 = classe1;
	}
	public String getRelazione() {
		return relazione;
	}
	public void setRelazione(String relazione) {
		this.relazione = relazione;
	}
	public String getClasse2() {
		return classe2;
	}
	public void setClasse2(String classe2) {
		this.classe2 = classe2;
	}
	
	
}
