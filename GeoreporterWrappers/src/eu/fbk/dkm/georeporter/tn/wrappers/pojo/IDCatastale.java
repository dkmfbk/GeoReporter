package eu.fbk.dkm.georeporter.tn.wrappers.pojo;


	

	import javax.xml.bind.annotation.XmlRootElement;

	@XmlRootElement
	//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
	public class IDCatastale {

		String id;
		String comuneCatastale;
		String foglio;
		String numero;
		Integer denomintore;
		String subalterno;
	    String tipoParticella;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getComuneCatastale() {
			return comuneCatastale;
		}
		public void setComuneCatastale(String comuneCatastale) {
			this.comuneCatastale = comuneCatastale;
		}
		public String getFoglio() {
			return foglio;
		}
		public void setFoglio(String foglio) {
			this.foglio = foglio;
		}
		public String getNumero() {
			return numero;
		}
		public void setNumero(String numero) {
			this.numero = numero;
		}
		public Integer getDenomintore() {
			return denomintore;
		}
		public void setDenomintore(Integer denomintore) {
			this.denomintore = denomintore;
		}
		public String getSubalterno() {
			return subalterno;
		}
		public void setSubalterno(String subalterno) {
			this.subalterno = subalterno;
		}
		public String getTipoParticella() {
			return tipoParticella;
		}
		public void setTipoParticella(String tipoParticella) {
			this.tipoParticella = tipoParticella;
		}
		
		
	

}
