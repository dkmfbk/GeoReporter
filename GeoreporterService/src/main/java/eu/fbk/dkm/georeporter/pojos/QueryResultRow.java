package eu.fbk.dkm.georeporter.pojos;

import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class QueryResultRow {

	List<QueryResultItem> listQueryResultItem;

	public List<QueryResultItem> getListQueryResultItem() {
		return listQueryResultItem;
	}

	public void setListQueryResultItem(List<QueryResultItem> listQueryResultItem) {
		this.listQueryResultItem = listQueryResultItem;
	}

	
}
