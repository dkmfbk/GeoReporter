
package eu.fbk.dkm.georeporter.pojos;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@XmlRootElement
@XmlType(propOrder = { "categoria", "totale", "sparql", "subcategoria" })
public class Statistiche {
	
    @SerializedName("categoria")
    @Expose
    private String categoria;
    @SerializedName("sparql")
    @Expose
    private String sparql;
    @SerializedName("totale")
    @Expose
    private String totale;
    @SerializedName("subcategoria")
    @Expose
    private List<Subcategoria> subcategoria = null;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSparql() {
        return sparql;
    }

    public void setSparql(String sparql) {
        this.sparql = sparql;
    }

    public String getTotale() {
        return totale;
    }

    public void setTotale(String totale) {
        this.totale = totale;
    }

    public List<Subcategoria> getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(List<Subcategoria> subcategoria) {
        this.subcategoria = subcategoria;
    }

}
