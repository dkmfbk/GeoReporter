package eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaCodificaRUN;

public class Run2Xls {

	private List<RigaCodificaRUN> fileCodificaRUN;

	public static Function<String, RigaCodificaRUN> mapToPerson = (line) -> {
		String[] p = line.split(";");
		return new RigaCodificaRUN(Integer.parseInt(p[0]), Integer.parseInt(p[1]), p[2]);
	};

	public void LoadFileCodificaRUN(String nomefile) {

		InputStream is;
		try {
			is = new FileInputStream(new File(nomefile));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			this.fileCodificaRUN = br.lines().map(mapToPerson).collect(Collectors.toList());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<RigaCodificaRUN> getFileCodificaRUN() {
		return fileCodificaRUN;
	}

	

	public static void main(String[] args) {
		String fileCodificaRUN = "fileCodificaRUN.csv";
		String fileRUN = "fileRUN.run";
		String fileExcel = "ExcelRUN.xls";

		Run2Xls run = new Run2Xls();
		run.LoadFileCodificaRUN(fileCodificaRUN);

		List<RigaCodificaRUN> lista = run.getFileCodificaRUN();

		// Creo il file excel
		String excelFileName = fileExcel;// name of excel file

		String sheetName = "Sheet1";// name of sheet

		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet(sheetName);

		InputStream is;
		try {
			is = new FileInputStream(new File(fileRUN));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String line;

			HSSFRow header = sheet.createRow(0);
			// inserisce i nomi delle colonne nella prima riga

			int i = 0;
			for (RigaCodificaRUN codifica : lista) {
				HSSFCell cell = header.createCell(i);
				cell.setCellValue(codifica.getTitolo());
				i++;
			}

			int r = 1;
			while ((line = br.readLine()) != null) {

				// iterating r number of rows

				HSSFRow row = sheet.createRow(r);

				// iterating c number of columns
				int c = 0;
				for (RigaCodificaRUN codifica : lista) {
					HSSFCell cell = row.createCell(c);

					System.out.println(line);
					System.out.println(codifica.getDa() + " " + codifica.getA());
					System.out.println(line.substring(codifica.getDa() - 1, codifica.getA()));
					cell.setCellValue(line.substring(codifica.getDa() - 1, codifica.getA()));
					c++;
				}

				r++;
			}
			FileOutputStream fileOut = new FileOutputStream(excelFileName);

			// write this workbook to an Outputstream.
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (RigaCodificaRUN rigaCodificaRUN : lista) {
			System.out.println(rigaCodificaRUN.getDa());
			System.out.println(rigaCodificaRUN.getA());
			System.out.println(rigaCodificaRUN.getTitolo());

		}

	}
}
