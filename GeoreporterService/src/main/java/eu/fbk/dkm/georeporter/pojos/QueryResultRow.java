package eu.fbk.dkm.georeporter.pojos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class QueryResultRow {

	List<QueryResultItem> listacelle=new ArrayList<QueryResultItem>();

	public List<QueryResultItem> getListacelle() {
		return listacelle;
	}

	public void setListacelle(List<QueryResultItem> listacelle) {
		this.listacelle = listacelle;
	}

	

	
}
