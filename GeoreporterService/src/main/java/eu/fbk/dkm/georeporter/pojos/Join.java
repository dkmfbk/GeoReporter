package eu.fbk.dkm.georeporter.pojos;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class Join {


	String nomeRelazione;
	String range;

	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getNomeRelazione() {
		return nomeRelazione;
	}
	public void setNomeRelazione(String nomeRelazione) {
		this.nomeRelazione = nomeRelazione;
	}
	
public String getJoin() {
	String join="";
if (getNomeRelazione()!=null) {	
	if (getNomeRelazione().startsWith("inversa_")) {
		
		 join=" ?"+getRange() +" :"+getNomeRelazione().replaceAll("inversa_", "")+ " ?$$$ . ";	
	}else {
       join=":"+getNomeRelazione()+" ?"+getRange()+" .";	
       }
	System.out.println("JOIN= "+join);
}
	return join;
	
}
}	