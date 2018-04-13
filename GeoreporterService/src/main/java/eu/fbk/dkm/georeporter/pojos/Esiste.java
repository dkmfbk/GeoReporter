package eu.fbk.dkm.georeporter.pojos;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class Esiste {

	List<String> risposta=new ArrayList<String>();

	public List<String> getRisposta() {
		return risposta;
	}

	public void setRisposta(List<String> risposta) {
		this.risposta = risposta;
	}
	
	public boolean checkEsiste(String s) {
		
		if (risposta!=null) {
		return risposta.contains(s);
		}return false;
	}
}
