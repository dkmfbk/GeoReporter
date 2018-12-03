package eu.fbk.dkm.georeporter.tn.wrappers.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportValore {

@SerializedName("id")
@Expose
private String id;
@SerializedName("label")
@Expose
private String label;
@SerializedName("value")
@Expose
private Integer value=0;
@SerializedName("totale")
@Expose
private Integer totale=0;


public ReportValore(String id) {
	
	this.id = id;
	
	
}

public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

public String getLabel() {
return label;
}

public void setLabel(String label) {
this.label = label;
}

public Integer getValue() {
return value;
}

public void setValue(Integer value) {
this.value = value;
}

public Integer getTotale() {
return totale;
}

public void setTotale(Integer totale) {
this.totale = totale;
}


public void incrementaValore() {
	this.value=this.value+1;
	
}
public void incrementaTotale() {
	this.totale=this.totale+1;
	}	
}
