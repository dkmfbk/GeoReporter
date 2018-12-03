
package eu.fbk.dkm.georeporter.pojos;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IndirizziNormalizzazione {

    @SerializedName("indirizzoNormalizzato")
    @Expose
    private List<IndirizzoNormalizzato> indirizzoNormalizzato = null;

    public List<IndirizzoNormalizzato> getIndirizzoNormalizzato() {
        return indirizzoNormalizzato;
    }

    public void setIndirizzoNormalizzato(List<IndirizzoNormalizzato> indirizzoNormalizzato) {
        this.indirizzoNormalizzato = indirizzoNormalizzato;
    }

}
