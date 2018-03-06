package eu.fbk.dkm.georeporter.wrappers.pojo;

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
		String result="Select ";
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