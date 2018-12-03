package eu.fbk.dkm.georeporter.interfaces;




	import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

	@XmlRootElement
	//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
	public class Entita {

		String idEntita;
		String field;
		String label;
		List<AttributoEntita> listaAttributiEntita;
		List<RelazioneEntita> ListaRelazioniEntita;
		public String getIdEntita() {
			return idEntita;
		}
		public void setIdEntita(String idEntita) {
			this.idEntita = idEntita;
		}
		public List<AttributoEntita> getListaAttributiEntita() {
			return listaAttributiEntita;
		}
		public void setListaAttributiEntita(List<AttributoEntita> listaAttributiEntita) {
			this.listaAttributiEntita = listaAttributiEntita;
		}
		
		public String getLabel() {
			return label;
		}
		public void setLabel(String label) {
			this.label = label;
		}
		public String getField() {
			return field;
		}
		public void setField(String field) {
			this.field = field;
		}
		public List<RelazioneEntita> getListaRelazioniEntita() {
			return ListaRelazioniEntita;
		}
		public void setListaRelazioniEntita(List<RelazioneEntita> listaRelazioniEntita) {
			ListaRelazioniEntita = listaRelazioniEntita;
		}
		
	}