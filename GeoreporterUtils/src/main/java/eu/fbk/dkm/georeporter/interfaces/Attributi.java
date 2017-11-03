package eu.fbk.dkm.georeporter.interfaces;

import java.util.ArrayList;
import java.util.List;

public class Attributi {
	Identificativo id;
public Identificativo getId() {
		return id;
	}

	public void setId(Identificativo id) {
		this.id = id;
	}

List<Attributo2> attributi =new ArrayList<Attributo2>();

public List<Attributo2> getAttributi() {
	return attributi;
}

public void setAttributi(List<Attributo2> attributi) {
	this.attributi = attributi;
}

}
