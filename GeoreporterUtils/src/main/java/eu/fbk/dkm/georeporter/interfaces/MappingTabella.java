package eu.fbk.dkm.georeporter.interfaces;




	import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import eu.fbk.dkm.georeporter.interfaces.Attributo;

	@XmlRootElement
	//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
	public class MappingTabella {

		
		Attributo id;
		List <Attributo> attributi;
		
		public List<Attributo> getAttributi() {
			return attributi;
		}
		public void setAttributi(List<Attributo> attributi) {
			this.attributi = attributi;
		}
		public Attributo getId() {
			return id;
		}
		public void setId(Attributo id) {
			this.id = id;
		}
		
		
		


}
