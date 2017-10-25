package eu.fbk.dkm.georeporter.pojos;




	import javax.xml.bind.annotation.XmlRootElement;

	@XmlRootElement
	//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
	public class Attributo {

		
		String nome;
		String tipo;
		String valore;
		String mapping;
		public String getNome() {
			return nome;
		}
		public void setNome(String nome) {
			this.nome = nome;
		}
		public String getTipo() {
			return tipo;
		}
		public void setTipo(String tipo) {
			this.tipo = tipo;
		}
		public String getValore() {
			return valore;
		}
		public void setValore(String valore) {
			this.valore = valore;
		}
		public String getMapping() {
			return mapping;
		}
		public void setMapping(String mapping) {
			this.mapping = mapping;
		}
		
		
		


}
