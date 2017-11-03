package eu.fbk.dkm.georeporter.pojos;




	import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import eu.fbk.dkm.georeporter.interfaces.Attributo;

	@XmlRootElement
	//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
	public class MappingTabella {

		
		Attributo idTabella;
		List <Attributo> attributi;
		
		public List<Attributo> getAttributi() {
			return attributi;
		}
		public void setAttributi(List<Attributo> attributi) {
			this.attributi = attributi;
		}
		public Attributo getIdTabella() {
			return idTabella;
		}
		public void setIdTabella(Attributo idTabella) {
			this.idTabella = idTabella;
		}
		
		
		
		


}
