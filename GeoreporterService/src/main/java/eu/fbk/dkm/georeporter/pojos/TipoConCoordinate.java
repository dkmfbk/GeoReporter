package eu.fbk.dkm.georeporter.pojos;




	import javax.xml.bind.annotation.XmlRootElement;

	@XmlRootElement
	//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
	public class TipoConCoordinate {

		
		String idTipo;
		String idIndirizzo;
		float longitude;
		float latitude;
		public String getIdTipo() {
			return idTipo;
		}
		public void setIdTipo(String idTipo) {
			this.idTipo = idTipo;
		}
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
		
		
		
		


}
