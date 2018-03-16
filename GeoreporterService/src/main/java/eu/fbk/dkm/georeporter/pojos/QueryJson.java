package eu.fbk.dkm.georeporter.pojos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class QueryJson {


	List<QueryObject> Query;

	public List<QueryObject> getQuery() {
		return Query;
	}

	public void setQuery(List<QueryObject> query) {
		Query = query;
	};
	
	
	
	
	
	public List<String> getCampiSelect() {
	 List<String> campiSelect= new ArrayList<String>();	
       for (QueryObject queryObject : Query) {
			
			campiSelect.addAll(Arrays.asList(queryObject.getSelect()));
	 }
       return campiSelect;
	}
	private String printSelect() {
		
		String selectfields=" ";
		
		for (QueryObject queryObject : Query) {
			
			selectfields=selectfields+queryObject.printSelectParams();
			
		}
		
		
		
	return selectfields;
	}	
		

	private String printWhereConditions() {
		String whereconditions="";
           for (QueryObject queryObject : Query) {
			
        	   whereconditions=whereconditions+queryObject.printWhereConditons();
			
		  }
		
		
		
		return whereconditions;
	}
	
	
	public String printString() {
		String result="Select DISTINCT ";
		String select=printSelect();
		String where=" where{ ";
		
		String condition=printWhereConditions();
		String join="";
		String close=" }";
		result=result+ select+ where + condition + join +close;
		System.out.println(result);
	return 	result;


	
	
}
	
	
	
}