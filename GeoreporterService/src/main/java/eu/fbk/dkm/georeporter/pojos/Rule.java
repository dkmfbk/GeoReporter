package eu.fbk.dkm.georeporter.pojos;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


	@XmlRootElement
	//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
	public class Rule {

		String id;
		String field;
		 String type;
		 String input;
		 String operator;
		 String value;
		List<String> values;
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getInput() {
			return input;
		}
		public void setInput(String input) {
			this.input = input;
		}
		public String getOperator() {
			return operator;
		}
		public void setOperator(String operator) {
			this.operator = operator;
		}
		public String getValue() {
			String result=value;
		
			if (getType().equals("string")){
				result ="\""+value+"\"";
			}else if  (getType().equals("dateTime")){
				result ="\""+value+"\"^^xsd:dateTime";
			}
			
			
			return result;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public List<String> getValues() {
			
			List <String> result=values;
			if( values!=null) {
			if (getType().equals("string")){
				result= new ArrayList<String>();
				for (String value : values) {
					result.add("\""+value+"\"");
				}
				
			}else if (getType().equals("dateTime")){
				result= new ArrayList<String>();
				for (String value : values) {
					result.add("\""+value+"\"^^xsd:dateTime");
				}
				
			}
			}
			
			return result;
		}
		public void setValues(List<String> values) {
			this.values = values;
		}
		
		
		



}
