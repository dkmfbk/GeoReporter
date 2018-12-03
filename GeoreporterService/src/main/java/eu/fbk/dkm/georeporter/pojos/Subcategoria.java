
package eu.fbk.dkm.georeporter.pojos;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@XmlRootElement
public class Subcategoria {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("sparql")
    @Expose
    private String sparql;
    @SerializedName("mancanti")
    @Expose
    private String mancanti;

    @SerializedName("valore")
    @Expose
    private Integer valore;

    
    
    
    
    
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

    public String getSparql() {
        return sparql;
    }

    public void setSparql(String sparql) {
        this.sparql = sparql;
    }

    public String getMancanti() {
        return mancanti;
    }

    public void setMancanti(String mancanti) {
        this.mancanti = mancanti;
    }

	public Integer getValore() {
		return valore;
	}

	public void setValore(Integer valore) {
		this.valore = valore;
	}

}
