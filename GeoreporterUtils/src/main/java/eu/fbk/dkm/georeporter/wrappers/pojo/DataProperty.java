package eu.fbk.dkm.georeporter.wrappers.pojo;

import java.util.Set;
import java.util.TreeSet;

public class DataProperty {

	
	String name;
	String type;
	String objecttype;
	public String getObjecttype() {
		return objecttype;
	}

	public void setObjecttype(String objecttype) {
		this.objecttype = objecttype;
	}


	Set<String> list_of_type ;
	
	public DataProperty(){
		
		list_of_type=new  TreeSet();
}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		
		if (list_of_type==null)
			return "xsd:string";
		if (list_of_type.size()==0)
			return "xsd:string";
		
		list_of_type.iterator().next();
		
		return list_of_type.iterator().next();
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<String> getList_of_type() {
		return list_of_type;
	}

	public void setList_of_type(Set<String> list_of_type) {
		this.list_of_type = list_of_type;
	}
    public void addType(String type){
	if( type!=null)
	this.list_of_type.add(type);
	}
    
    
	public String print(){
		
	return ":"+this.getName().toLowerCase()+" a  owl:DatatypeProperty  ;\n"+
		   "rdfs:label \""+ this.getName()+"\" ;\n"+
	       "rdfs:domain :"+ this.getObjecttype() + " ;\n"+
		   "rdfs:range " + this.getType()+ " .\n";	
		
	}
}

