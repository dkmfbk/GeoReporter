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
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Famiglia;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.FornituraGas;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Intavolazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.LetturaAcqua;
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

public class WrapperTULetturaA {

	// vettore per l'elenco degli HEADER
	public static String[] header;
	// lista di tipo LATTURA ACQUA degli elementi estratti dal file XLS
	public static List<LetturaAcqua> listLetturaAcqua = new ArrayList<LetturaAcqua>();

	// estrazione HEADER
	public static void estrazioneHeaderFile(String path) {

		try {
			File file = new File(path);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();
			if (rigaCorrente != null) {
				header = rigaCorrente.split(";", -1);
			}
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void letturaFile(String path) {
		try {
			File file = new File(path);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String rigaCorrente = reader.readLine();
			int prima_rig = 0;

			while (rigaCorrente != null) {
				if (prima_rig == 0) {
					// salto prima riga file
				} else {
					Map<String, String> campi = new HashMap<String, String>();

					String[] tmpRiga = rigaCorrente.split(";", -1);

					for (int i = 0; i < header.length; i++) {
						if (ControlloValore.puliziaHeader(header[i]).contains("data")) {
							campi.put(ControlloValore.puliziaHeader(header[i]),
									ControlloValore.dataBarrataOra(tmpRiga[i]));
						} else {
							campi.put(ControlloValore.puliziaHeader(header[i]),
									ControlloValore.controlloValore(tmpRiga[i]));
						}
					}
					// settare elemento della lista e aggiungerlo alla list
					LetturaAcqua la = new LetturaAcqua();
					la.setValori(campi);
					listLetturaAcqua.add(la);
				}
				prima_rig++;
				rigaCorrente = reader.readLine();
			}

			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {

	}

}
