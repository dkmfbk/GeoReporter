package eu.fbk.dkm.georeporter.tn.wrappers.pojo;



import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListaReport {

@SerializedName("Reports")
@Expose
private List<Report> reports = null;

public List<Report> getReports() {
return reports;
}

public void setReports(List<Report> reports) {
this.reports = reports;
}

}