package eu.fbk.dkm.georeporter.tn.wrappers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.AnagraficaComunale;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.FornituraGas;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Intavolazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Locazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.NudaProprieta;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Particella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Titolarita;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.TitolaritaCompleta;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.tn.wrappers.ControlloValore;

public class WrapperTUIciImuNP {

	// vettore per l'elenco degli HEADER
	public static String[] header = new String[99];
	// lista di tipo TRIBUTO UTENZA ICI IMU NUDA PROPRIETA degli elementi estratti
	// dal file XLS
	public static List<NudaProprieta> listNudaProprieta = new ArrayList<NudaProprieta>();

	// estrazione degli HEADER
	public static void readXLSFile(String path) throws IOException {
		InputStream ExcelFileToRead = new FileInputStream(path);
		HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);

		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row;
		HSSFCell cell;

		Iterator rows = sheet.rowIterator();
		int i = 0;

		while (rows.hasNext()) {
			Map<String, String> campi = new HashMap<String, String>();
			row = (HSSFRow) rows.next();
			int j = 0;
			int k = 0;
			for (int g = 0; g < row.getLastCellNum(); g++) {

				if (i == 0) {
					header[j] = ControlloValore.puliziaHeader(row.getCell(g).toString());
					j++;
				} else {
					// aggiungere controllo in caso di valore null
					if (row.getCell(g) != null) {
						if (header[k].contains("data")) {
							campi.put(header[k], ControlloValore.controlloData(row.getCell(g).toString()));
						} else {
							campi.put(header[k], ControlloValore.controlloVIR(row.getCell(g).toString()));
						}
					} else {
						campi.put(header[k], "");
					}
					k++;
				}

			}
			System.out.println();
			i++;
			if (!campi.isEmpty()) {
				// settare elemento della lista e aggiungerlo alla list
				NudaProprieta np = new NudaProprieta();
				np.setValori(campi);
				listNudaProprieta.add(np);
			}

		}

	}

	public static void main(String[] args) {

	}

}
