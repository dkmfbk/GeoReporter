package eu.fbk.dkm.georeporter.pojos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class QueryResult {

	String[] colonne;
	
	List<QueryResultRow>  righe =new ArrayList<QueryResultRow>();

	

	public String[] getColonne() {
		return colonne;
	}

	public void setColonne(String[] colonne) {
		this.colonne = colonne;
	}

	public List<QueryResultRow> getRighe() {
		return righe;
	}

	public void setRighe(List<QueryResultRow> righe) {
		this.righe = righe;
	}

	
	
}
