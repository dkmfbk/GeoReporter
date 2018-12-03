package eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.QueryParam;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;

import eu.fbk.dkm.georeporter.config.Costanti;
import eu.fbk.dkm.georeporter.config.ProgramDirectoryUtilities;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.ListaReport;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Report;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.ReportValore;

public class ReportManager {

	public ReportManager(String file) {
		super();
		this.file = file;
		loadReportsFile();
	}

	public Report report;
	public String file;
	public URL fileurl;


	
	public String getReportFile(
		String logfilename
			) {
//URL u= ReportManager.class.getClassLoader().getResource("./"+logfilename);
		URL u= ReportManager.class.getClassLoader().getResource("./"+logfilename);
		
		System.out.println("url=" +u);
	String contents="";
	try {
		contents = Resources.toString(u, Charsets.UTF_8);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return contents;
	}
	
	
	
	
	
	
	
	public void loadReportsFile() {
		Gson gson = new Gson();
		BufferedReader br = null;
		try {

			fileurl = ReportManager.class.getClassLoader().getResource("./" + file);
			// System.out.println("fileurl="+ fileurl);

			URLConnection yc;
			try {
				yc = fileurl.openConnection();
				br = new BufferedReader(new InputStreamReader(yc.getInputStream()));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.report = gson.fromJson(br, Report.class);

			if (this.report != null) {
				for (ReportValore r : this.report.getReports()) {
					System.out.println(r.getId());
					System.out.println(r.getLabel());
					System.out.println(r.getValue());
					System.out.println(r.getTotale());
				}
			}

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public void updateReportsFile(List<ReportValore> rvList) {

		for (ReportValore rvin : rvList) {
			for (ReportValore rv : this.report.getReports()) {

				if (rv.getId().equals(rvin.getId())) {
					rv.setValue(rvin.getValue());
					rv.setTotale(rvin.getTotale());

				}
			}
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {

			String jsonString = gson.toJson(this.report);
			// Write JSON String to file

		//	FileWriter fileWriter = new FileWriter(new File(fileurl.getPath()));
			FileWriter fileWriter = new FileWriter(new File(ReportManager.class.getClassLoader().getResource("./" + file).getFile()));
			
			fileWriter.write(jsonString);
			fileWriter.close();

		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

	}

	public static void main(String[] args) {
		ReportManager rm = new ReportManager("report.json");
		// rm.loadReportsFile();
		ReportValore rv = new ReportValore("anagrafica_codiciFiscali_nulli");
		rv.setValue(444);
		rv.setTotale(444);

		ReportValore rv2 = new ReportValore("fabbricati_titolarita_idcatastali_mancanti");
		rv2.setValue(22);
		rv2.setTotale(22);
		List<ReportValore> rvlist = new ArrayList<ReportValore>();
		rvlist.add(rv);
		rvlist.add(rv2);

		rm.updateReportsFile(rvlist);
		String path =System.getenv("CATALINA_HOME");
		System.out.println("path=" +path);
		System.out.println("current dir=" +ProgramDirectoryUtilities.getProgramDirectory());
	}

}