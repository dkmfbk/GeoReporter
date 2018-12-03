package eu.fbk.dkm.georeporter.tn.wrappers.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Report {

@SerializedName("Reports")
@Expose
private List<ReportValore> reports = null;

public List<ReportValore> getReports() {
return reports;
}

public void setReports(List<ReportValore> reports) {
this.reports = reports;
}

}