
package eu.fbk.dkm.georeporter.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IndirizzoNormalizzato {

    @SerializedName("stringaricerca")
    @Expose
    private String stringaricerca;
    @SerializedName("coordinateLat")
    @Expose
    private Float coordinateLat;
    @SerializedName("coordinateLong")
    @Expose
    private Float coordinateLong;
    @SerializedName("indirizzoNormalizzato")
    @Expose
    private String indirizzoNormalizzato;

    public String getStringaricerca() {
        return stringaricerca;
    }

    public void setStringaricerca(String stringaricerca) {
        this.stringaricerca = stringaricerca;
    }

    public Float getCoordinateLat() {
        return coordinateLat;
    }

    public void setCoordinateLat(Float coordinateLat) {
        this.coordinateLat = coordinateLat;
    }

    public Float getCoordinateLong() {
        return coordinateLong;
    }

    public void setCoordinateLong(Float coordinateLong) {
        this.coordinateLong = coordinateLong;
    }

    public String getIndirizzoNormalizzato() {
        return indirizzoNormalizzato;
    }

    public void setIndirizzoNormalizzato(String indirizzoNormalizzato) {
        this.indirizzoNormalizzato = indirizzoNormalizzato;
    }

}
