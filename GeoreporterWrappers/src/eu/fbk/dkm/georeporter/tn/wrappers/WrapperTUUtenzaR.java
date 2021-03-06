package eu.fbk.dkm.georeporter.tn.wrappers;

import java.io.FileInputStream;
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

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UtenzaRifiuti;
import eu.fbk.dkm.georeporter.tn.wrappers.ControlloValore;

public class WrapperTUUtenzaR {

	// vettore per l'elenco degli HEADER
	public  String[] header ;
	// lista di tipo UTENZA RIFIUTI degli elementi estratti dal file XLS
	public  List<UtenzaRifiuti> listUtenzaRifiuti = new ArrayList<UtenzaRifiuti>();

	// estrazione degli HEADER
	public  void readXLSFile(String path) throws IOException {
		InputStream ExcelFileToRead = new FileInputStream(path);
		HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);

		HSSFSheet sheet = wb.getSheetAt(0);
		
		HSSFRow row;
		HSSFCell cell;
		int noOfColumns = sheet.getRow(0).getLastCellNum();
		header= new String[noOfColumns];
		Iterator rows = sheet.rowIterator();
		int i = 0;

		while (rows.hasNext()) {
			Map<String, String> campi = new HashMap<String, String>();
			row = (HSSFRow) rows.next();
			Iterator cells = row.cellIterator();
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
							campi.put(header[k], ControlloValore.controlloDataEXCEL(row.getCell(g).getDateCellValue()));
						} else {
							campi.put(header[k], ControlloValore.controlloVIR(row.getCell(g).toString()));
						}
					} else {
						campi.put(header[k], "");
					}
					k++;
				}

			}
			//System.out.println();
			i++;
			if (!campi.isEmpty()) {
				// settare elemento della lista e aggiungerlo alla list
				UtenzaRifiuti ur = new UtenzaRifiuti();
				ur.setValori(campi);
				listUtenzaRifiuti.add(ur);
			}

		}

	}

	public static void main(String[] args) {

	}

}
