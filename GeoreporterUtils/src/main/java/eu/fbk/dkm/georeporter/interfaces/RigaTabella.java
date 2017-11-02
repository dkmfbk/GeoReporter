package eu.fbk.dkm.georeporter.interfaces;

import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class RigaTabella {

	
	 String nometabella="";
	 String uririga="";
		List<Attributo> listaattributi;
		List<Relazione> listarelazioni;
		List <Attributo> listachiave;
		
		
    public String getNometabella() {
		return nometabella;
	}
	public void setNometabella(String nometabella) {
		this.nometabella = nometabella;
	}

	public List<Attributo> getListaattributi() {
		return listaattributi;
	}
	public void setListaattributi(List<Attributo> listaattributi) {
		this.listaattributi = listaattributi;
	}
	public List<Relazione> getListarelazioni() {
		return listarelazioni;
	}
	public void setListarelazioni(List<Relazione> listarelazioni) {
		this.listarelazioni = listarelazioni;
	}
	public List<Attributo> getListachiave() {
		return listachiave;
	}
	public void setListachiave(List<Attributo> listachiave) {
		this.listachiave = listachiave;
	}
	public String getUririga() {
		return uririga;
	}
	public void setUririga(String uririga) {
		this.uririga = uririga;
	}
	


	
}
