package eu.fbk.dkm.georeporter.tn.wrappers.pojo;




	import javax.xml.bind.annotation.XmlRootElement;

	@XmlRootElement
	//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
	public class Attributo {

		public String getIdDomain() {
			return idDomain;
		}
		public void setIdDomain(String idDomain) {
			this.idDomain = idDomain;
		}
		String idDomain;
		String nome;
		String tipo;
		String valore;
		String mapping;
		boolean multiplo=false;
		
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
		public boolean isMultiplo() {
			return multiplo;
		}
		public void setMultiplo(boolean multiplo) {
			this.multiplo = multiplo;
		}

		
		
		


}
