package eu.fbk.dkm.georeporter.interfaces;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class Relazione {
    String uriDomain;
    String nomerelazione;
    String uriRange;
    String label;
    
	public String getUriDomain() {
		return uriDomain;
	}
	public void setUriDomain(String uriDomain) {
		this.uriDomain = uriDomain;
	}
	public String getNomerelazione() {
		return nomerelazione;
	}
	public void setNomerelazione(String nomerelazione) {
		this.nomerelazione = nomerelazione;
	}
	public String getUriRange() {
		return uriRange;
	}
	public void setUriRange(String uriRange) {
		this.uriRange = uriRange;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
