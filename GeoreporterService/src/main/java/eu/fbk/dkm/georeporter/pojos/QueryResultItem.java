package eu.fbk.dkm.georeporter.pojos;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class QueryResultItem {

String colonna;
String valore;
String tipo;
public String getColonna() {
	return colonna;
}
public void setColonna(String colonna) {
	this.colonna = colonna;
}
public String getValore() {
	return valore;
}
public void setValore(String valore) {
	this.valore = valore;
}
public String getTipo() {
	return tipo;
}
public void setTipo(String tipo) {
	this.tipo = tipo;
}


}
