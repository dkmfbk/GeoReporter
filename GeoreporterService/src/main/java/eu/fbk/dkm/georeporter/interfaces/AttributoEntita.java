package eu.fbk.dkm.georeporter.interfaces;




	import javax.xml.bind.annotation.XmlRootElement;

	@XmlRootElement
	//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
	public class AttributoEntita {

		String id;
		String field;
		String label;
		String type;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		

}
