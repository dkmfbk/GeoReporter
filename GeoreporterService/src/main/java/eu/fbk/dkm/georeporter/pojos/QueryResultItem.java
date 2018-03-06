package eu.fbk.dkm.georeporter.pojos;

import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
//@XmlType(propOrder = { "id", "nome", "cognome", "eta", "telefono", "email"  })
public class QueryResultItem {

String itemName;
String itemValue;
String ItemType;
public String getItemName() {
	return itemName;
}
public void setItemName(String itemName) {
	this.itemName = itemName;
}
public String getItemValue() {
	return itemValue;
}
public void setItemValue(String itemValue) {
	this.itemValue = itemValue;
}
public String getItemType() {
	return ItemType;
}
public void setItemType(String itemType) {
	ItemType = itemType;
}


}
