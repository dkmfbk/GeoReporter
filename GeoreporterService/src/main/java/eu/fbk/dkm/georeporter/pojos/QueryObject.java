package eu.fbk.dkm.georeporter.pojos;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class QueryObject {

	
	String object;
	String type;
	String condition;
	String valid;
	String not;
	List<Rule> rules;
	String[]  select;
	
	Join joinWith;
	

	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public String getNot() {
		return not;
	}
	public void setNot(String not) {
		this.not = not;
	}
	public List<Rule> getRules() {
		return rules;
	}
	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}
	public String[] getSelect() {
		return select;
	}
	public void setSelect(String[] select) {
		this.select = select;
	}
	public Join getJoinWith() {
		return joinWith;
	}
	public void setJoinWith(Join joinWith) {
		this.joinWith = joinWith;
	}

public String printSelectParams() {
	
	//String selectString="Select ";
	String selectvars="";
	for (int i = 0; i < select.length; i++) {
		selectvars=selectvars+ " ?"+select[i];
	}
	
	return selectvars;
}

public String printSelectCondition() {
	
	//String selectString="Select ";
	String selectvars="";
	for (int i = 0; i < select.length; i++) {
		selectvars=selectvars+ "  OPTIONAL{ ?"+getObject()+ " :"+select[i]+ " ?"+select[i]+" }.";
	//	selectvars=selectvars+ " ?"+getObject()+ " :"+select[i]+ " ?"+select[i]+" .";
	}
	
	return selectvars;
}
private String printIsa() {
	
	String isaString=" ?"+getObject()+" a :"+ getType();
	
	return isaString;
}

public String printWhereConditons() {
	String whereConditions=printIsa()+" . ";
	String selectCondition=printSelectCondition();
	String joinCondition=printJoin();
	String rulestring="";
	for (Rule rule : rules) {
		rulestring= rulestring+"  ?"+getObject()+ " :" +rule.getId()+ " ?"+getObject() + "_"+rule.getId()+" . FILTER (?"+getObject() + "_"+rule.getId()+" "+rule.getOperator()+" "+rule.getValue()+" ) . ";
		
	}
	return whereConditions +selectCondition+rulestring + joinCondition;
}

public String printJoin() {
	
	String join="";
	if (joinWith!=null) {
	if(!joinWith.getJoin().equals(null)) {
	 join="?"+getObject()+" "+joinWith.getJoin();
	}
	}
	System.out.println("QUERYOBJECTJOIN= "+join);
	return join;
}



public String getObject() {
	return object;
}
public void setObject(String object) {
	this.object = object;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
	
	
	


}
