package eu.fbk.dkm.georeporter.tn.wrappers.pojo;


	import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;;

	@XmlRootElement
	//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
	public class MappingTabelle {

		
		
		List <MappingTabella> mappings;

		public List<MappingTabella> getMappings() {
			return mappings;
		}

		public void setMappings(List<MappingTabella> mappings) {
			this.mappings = mappings;
		}

		
		
		
		
		
		


}
