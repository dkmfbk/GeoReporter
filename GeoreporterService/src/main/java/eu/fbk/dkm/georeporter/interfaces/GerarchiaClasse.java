package eu.fbk.dkm.georeporter.interfaces;




	import javax.xml.bind.annotation.XmlRootElement;

	@XmlRootElement
	//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
	public class GerarchiaClasse {

		String idclasse;
		String field;
		String label;
		String idsuperclasse;
		public String getIdclasse() {
			return idclasse;
		}
		public void setIdclasse(String idclasse) {
			this.idclasse = idclasse;
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
		public String getIdsuperclasse() {
			return idsuperclasse;
		}
		public void setIdsuperclasse(String idsuperclasse) {
			this.idsuperclasse = idsuperclasse;
		}
		
		
		


}
