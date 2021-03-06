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
		String gestioneUI="";
           for (QueryObject queryObject : Query) {
			
        	   whereconditions=whereconditions+queryObject.printWhereConditons();
        
        	   if (queryObject.getType().equals("IdentificativoCatastale")){
        		   gestioneUI= "OPTIONAL { ?object1  :hasUnitaImmobiliare ?ui. "							 
							+ " ?"+queryObject.getObject()+" :hasParticella ?pa."
							 +" BIND(?"+queryObject.getObject()+" AS ?ic) "
							
							+ "  } ";
        	   	
		         }else  if (queryObject.getType().equals("Particella")){
			  
		        	  gestioneUI= "OPTIONAL { ?ic a :IdentificativoCatastale  ."
								+ "?ic :hasUnitaImmobiliare ?ui . "
								+ "?ic :hasParticella ?"+queryObject.getObject()+"."
								//+" ?"+queryObject.getObject()+ " a ?pa. "
										+" BIND(?"+queryObject.getObject()+" AS ?pa) "
	        				    +"   FILTER (?pa not in( owl:NamedIndividual))"
								+ "  } ";
		        	  
		         } else  if (queryObject.getType().equals("UnitaImmobiliare")){
		        	  gestioneUI= "OPTIONAL { ?ic a :IdentificativoCatastale . "
								+ "?ic :hasUnitaImmobiliare ?"+queryObject.getObject()+" . "
								//+"?"+queryObject.getObject()+" a ?ui . "
										+" BIND(?"+queryObject.getObject()+" AS ?ui) "
	        				    +"   FILTER (?ui not in( owl:NamedIndividual))"
								+ "?ic :hasParticella ?pa  } ";
			    }else if(queryObject.getType().equals("Locazione")||
			    		queryObject.getType().equals("UtenzaRifiuti")||
			    		queryObject.getType().equals("UtenzaAcqua")||
			    		queryObject.getType().equals("ICI_IMU_AbitazionePrincipale")||
			    		queryObject.getType().equals("Titolarita")||
			    		queryObject.getType().equals("ICI_IMU_NudaProprieta")
			    		){
				  
				  gestioneUI= "OPTIONAL { ?"+queryObject.getObject()+" :hasIdentificativoCatastale ?ic.  "
							+ "?ic :hasUnitaImmobiliare ?ui. "
							+ "?ic :hasParticella ?pa  } ";
			    }
        	 
              }
           
           
		return whereconditions + gestioneUI;
	}
	
	
	public String printString() {
		String result="Select DISTINCT ?ic ?pa ?ui";
		String select=printSelect();
		String where=" where{ ";
		
		String condition=printWhereConditions();
		String join="";
		String close=" }";
		result=result+ select+ where + condition + join +close;
		System.out.println("QUERY+"+result);
	return 	result;


	
	
}
	
	
	
}