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

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.FornituraGas;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Intavolazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Locazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Particella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Titolarita;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.TitolaritaCompleta;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.tn.wrappers.ControlloValore;

public class WrapperForLoc {

	// vettore per l'elenco degli HEADER LOCAZIONE
	public  String[] headerFL;
	// lista di tipo FLOCAZIONE degli elementi estratti dal file XLS
	public  List<Locazione> listLocazione = new ArrayList<Locazione>();

	// estrazione degli HEADER
	public  void readXLSFile(String path) throws IOException {
		InputStream ExcelFileToRead = new FileInputStream(path);
		HSSFWorkbook wb = new HSSFWorkbook(ExcelFileToRead);

		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow row;
		HSSFCell cell;
		int noOfColumns = sheet.getRow(0).getLastCellNum();
		headerFL= new String[noOfColumns];
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
					headerFL[j] = ControlloValore.puliziaHeader(row.getCell(g).toString());
					j++;
				} else {
					// aggiungere controllo in caso di valore null
					if (row.getCell(g) != null) {

						if (headerFL[k].contains("codice")) {
							campi.put(headerFL[k], row.getCell(g).toString().trim());
						} else if (headerFL[k].contains("data")) {
							//campi.put(headerFL[k], ControlloValore.controlloData(row.getCell(g).toString()));
							campi.put(headerFL[k], ControlloValore.controlloDataEXCEL(row.getCell(g).getDateCellValue()));
							
						} else {
							campi.put(headerFL[k], ControlloValore.controlloVIR(row.getCell(g).toString().trim()));
						}
					} else {
						campi.put(headerFL[k], "");
					}
					k++;
				}

			}
			//System.out.println();
			i++;
			if (!campi.isEmpty()) {
				// settare elemento della lista e aggiungerlo alla list
				Locazione loc = new Locazione();
				loc.setValori(campi);
				listLocazione.add(loc);
			}

		}

	}

	public static void main(String[] args) {

	}

}
