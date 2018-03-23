package eu.fbk.dkm.georeporter.pojos;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class TributiICI {
	
	String uri;
	String codiceUtenza;
	String MQLOcali;
	String Occupante;
	String Classe;
	String categoriadescrizione;
	String contribuente;
	String indirizzo;
	String rendita;

	public String getCodiceUtenza() {
		return codiceUtenza;
	}
	public void setCodiceUtenza(String codiceUtenza) {
		this.codiceUtenza = codiceUtenza;
	}
	public String getMQLOcali() {
		return MQLOcali;
	}
	public void setMQLOcali(String mQLOcali) {
		MQLOcali = mQLOcali;
	}
	public String getOccupante() {
		return Occupante;
	}
	public void setOccupante(String occupante) {
		Occupante = occupante;
	}
	public String getClasse() {
		return Classe;
	}
	public void setClasse(String classe) {
		Classe = classe;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getCategoriadescrizione() {
		return categoriadescrizione;
	}
	public void setCategoriadescrizione(String categoriadescrizione) {
		this.categoriadescrizione = categoriadescrizione;
	}
	public String getContribuente() {
		return contribuente;
	}
	public void setContribuente(String contribuente) {
		this.contribuente = contribuente;
	}
	public String getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}
	public String getRendita() {
		return rendita;
	}
	public void setRendita(String rendita) {
		this.rendita = rendita;
	}

}
