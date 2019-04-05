package eu.fbk.dkm.georeporter.pojos;




	import javax.xml.bind.annotation.XmlRootElement;

	@XmlRootElement
	//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
	public class UIConCoordinate {

		String idIC;
		String idUI;
		String idIndirizzo;
		float longitude;
		float latitude;
		String soggetto;
		
		public String getIdIndirizzo() {
			return idIndirizzo;
		}
		public void setIdIndirizzo(String idIndirizzo) {
			this.idIndirizzo = idIndirizzo;
		}
		public float getLongitude() {
			return longitude;
		}
		public void setLongitude(float longitude) {
			this.longitude = longitude;
		}
		public float getLatitude() {
			return latitude;
		}
		public void setLatitude(float latitude) {
			this.latitude = latitude;
		}
		public String getIdIC() {
			return idIC;
		}
		public void setIdIC(String idIC) {
			this.idIC = idIC;
		}
		public String getIdUI() {
			return idUI;
		}
		public void setIdUI(String idUI) {
			this.idUI = idUI;
		}
		public String getSoggetto() {
			return soggetto;
		}
		public void setSoggetto(String soggetto) {
			this.soggetto = soggetto;
		}
		
		
		
		


}
