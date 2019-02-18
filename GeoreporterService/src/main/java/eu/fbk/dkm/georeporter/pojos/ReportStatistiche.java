
package eu.fbk.dkm.georeporter.pojos;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
@XmlRootElement
public class ReportStatistiche {

    @SerializedName("statistiche")
    @Expose
    private List<Statistiche> statistiche = null;

    public List<Statistiche> getStatistiche() {
        return statistiche;
    }

    public void setStatistiche(List<Statistiche> statistiche) {
        this.statistiche = statistiche;
    }

}
