package eu.fbk.dkm.georeporter.tn.wrappers.pojo;


	import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;;

	@XmlRootElement
	//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
	public class MappingTabellaHashMap {

		
		Attributo idTabella;
		List <Attributo> attributi;
		Map<String, Attributo> attributiHM;
		
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
		public Map<String, Attributo> getAttributiHM() {
			return attributiHM;
		}
		public void setAttributiHM(Map<String, Attributo> attributiHM) {
			this.attributiHM = attributiHM;
		}
		
		
		


}
