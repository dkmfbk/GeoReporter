package eu.fbk.dkm.georeporter.wrappers;

import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class QueryResult {

	List<QueryResultRow>  listQueryResultRow;

	public List<QueryResultRow> getListQueryResultRow() {
		return listQueryResultRow;
	}

	public void setListQueryResultRow(List<QueryResultRow> listQueryResultRow) {
		this.listQueryResultRow = listQueryResultRow;
	}

	
	
}
