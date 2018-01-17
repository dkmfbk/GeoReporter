package eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sun.jersey.api.client.WebResource;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Annotazioni;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Comuni;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.IdentificativiCatastali;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Indirizzo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Riserve;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.tn.wrappers.WrapperFab;

public class MappingInsertFabIde {

	public static List<IdentificativiCatastali> listIdentificativiCatastali = WrapperFab.listIdentificativiCatastali;

	// metodo che acquisisce i 2 file di tipo json
	public static void LoadFileIdentificativi(File fileParticella, File fileIdeCat) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gson2 = new Gson();
		JsonReader reader2;

		try {
			reader = new JsonReader(new FileReader(fileParticella));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			reader2 = new JsonReader(new FileReader(fileIdeCat));
			MappingTabella data2 = gson2.fromJson(reader2, MappingTabella.class);

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeValIdentificativo(data, data2);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void associazioneMappingNomeValIdentificativo(MappingTabella data, MappingTabella data2) {
		// ciclo la lista degli elementi IDE CAt

		for (int j = 0; j < listIdentificativiCatastali.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			// ciclo per crea la listaCHIAVI e listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {
				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listIdentificativiCatastali.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listIdentificativiCatastali.get(j).getListaValoriChiave().get(0).get(parts[1])
								.isEmpty() == false)) {
					tmp.setValore(listIdentificativiCatastali.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp);
				}

				if ((listIdentificativiCatastali.get(j).getValori().get(parts[1]) != null)
						&& (listIdentificativiCatastali.get(j).getValori().get(parts[1]).isEmpty() == false)) {
					tmp.setValore(listIdentificativiCatastali.get(j).getValori().get(parts[1]));
					listAttributi.add(tmp);
				}

			}
			// riga di tipo RIGATABELLA per Particella
			RigaTabella rigaTPar = new RigaTabella();
			rigaTPar.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTPar.setListaattributi(listAttributi);
			rigaTPar.setListachiave(listChiavi);
			String codamm = listIdentificativiCatastali.get(j).getListaValoriChiave().get(0)
					.get("codiceamministrativo");
			String ideimm = listIdentificativiCatastali.get(j).getListaValoriChiave().get(0)
					.get("identificativoimmobile");
			// controllo su uri
			String num = listIdentificativiCatastali.get(j).getValori().get("numero");
			String den = listIdentificativiCatastali.get(j).getValori().get("denominatore");
			String sub = listIdentificativiCatastali.get(j).getValori().get("subalterno");

			if (num != null) {
				String[] tmpv = num.split("/", -1);

				if ((num.isEmpty()) || (num == null)) {
					num = "";
					den = "";
				} else if (tmpv.length == 2) {
					num = tmpv[0];
					den = tmpv[1];
				}
			}
			rigaTPar.setUririga("http://dkm.fbk.eu/georeporter#PA_C" + codamm + "_N" + num + "_D" + den);

			String relRange = MappingInsertFabUiNote.insertRigaReturn(rigaTPar);

			// ciclo per crea la listaCHIAVI e listaATTRIBUTI richiesti dal mapping
			List<Attributo> listAttributi2 = new ArrayList<Attributo>();
			List<Attributo> listChiavi2 = new ArrayList<Attributo>();
			for (int i = 0; i < data2.getAttributi().size(); i++) {
				String string = data2.getAttributi().get(i).getNome();
				String[] parts = string.split("#");
				Attributo tmp2 = new Attributo();
				tmp2.setNome(data2.getAttributi().get(i).getNome());
				tmp2.setMapping(data2.getAttributi().get(i).getMapping());
				tmp2.setTipo(data2.getAttributi().get(i).getTipo());

				if ((listIdentificativiCatastali.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listIdentificativiCatastali.get(j).getListaValoriChiave().get(0).get(parts[1])
								.isEmpty() == false)) {
					tmp2.setValore(listIdentificativiCatastali.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi2.add(tmp2);
				}
				/*
				 * if ((listIdentificativiCatastali.get(j).getValori().get(parts[1]) != null) &&
				 * (listIdentificativiCatastali.get(j).getValori().get(parts[1]).isEmpty() ==
				 * false)) {
				 * tmp2.setValore(listIdentificativiCatastali.get(j).getValori().get(parts[1]));
				 * listAttributi2.add(tmp2); }
				 */

				if ((listIdentificativiCatastali.get(j).getValori().get(parts[1]) != null)
						&& (listIdentificativiCatastali.get(j).getValori().get(parts[1]).isEmpty() == false)
						|| parts[1].equals("pm")) {
					if (parts[1].equals("pm")) {
						int v = 1;
						while (((v <= 10) && !listIdentificativiCatastali.get(j).getValori().get("pm" + v).isEmpty())) {
							Attributo tmp3 = new Attributo();
							tmp3.setNome(data2.getAttributi().get(i).getNome());
							tmp3.setMapping(data2.getAttributi().get(i).getMapping());
							tmp3.setTipo(data2.getAttributi().get(i).getTipo());
							tmp3.setValore(listIdentificativiCatastali.get(j).getValori().get("pm" + v));
							listAttributi2.add(tmp3);
							v++;
						}
					} else {
						tmp2.setValore(listIdentificativiCatastali.get(j).getValori().get(parts[1]));
						listAttributi2.add(tmp2);
					}
				}
			}

			// creo la relazione particella -> identificativo catastale
			List<Relazione> listRelPartIdeCat = new ArrayList<Relazione>();
			Relazione relPartIdeCat = new Relazione();
			relPartIdeCat.setNomerelazione("http://dkm.fbk.eu/georeporter#hasParticella");
			relPartIdeCat
					.setUriDomain("http://dkm.fbk.eu/georeporter#C" + codamm + "_N" + num + "_D" + den + "_S" + sub);
			relPartIdeCat.setUriRange(relRange);
			listRelPartIdeCat.add(relPartIdeCat);

			Relazione relUIIdeCat = new Relazione();
			relUIIdeCat.setNomerelazione("http://dkm.fbk.eu/georeporter#hasUnitaImmobiliare");
			relUIIdeCat.setUriDomain("http://dkm.fbk.eu/georeporter#C" + codamm + "_N" + num + "_D" + den + "_S" + sub);
			relUIIdeCat.setUriRange("http://dkm.fbk.eu/georeporter#UI_" + codamm + "_" + ideimm);
			listRelPartIdeCat.add(relUIIdeCat);

			RigaTabella rigaTIdeCat = new RigaTabella();
			rigaTIdeCat.setNometabella("http://dkm.fbk.eu/georeporter#" + data2.getIdTabella().getMapping());
			rigaTIdeCat.setListaattributi(listAttributi2);
			rigaTIdeCat.setListachiave(listChiavi2);
			rigaTIdeCat.setUririga("http://dkm.fbk.eu/georeporter#C" + codamm + "_N" + num + "_D" + den + "_S" + sub);

			rigaTIdeCat.setListarelazioni(listRelPartIdeCat);
			// controllo relazione particella ide cat
			// String relRange2 = MappingInsertFABUINoteFab.insertRigaReturn(rigaTIdeCat);
			MappingInsertFabUiNote.insertRiga(rigaTIdeCat);
		}

	}

	public static void main(String[] args) {

		// path del file .FAB e del file con gli HEADER inseriti a mano da un utente
		// IDR0000115470_TIPOFACSN_CAMML322
		String pathF = "file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB";
		String pathP = "file/TN_header/headerfilefab.csv";

		// chiamata per l'estrazione degli header per la composizione della lista HEADER
		WrapperFab.estrazioneHeaderFileFab(pathP);

		// chiamata per l'analisi del file .FAB
		WrapperFab.letturaFileFab(pathF);

		// chiamata al metodo che accoppia ELEMENTO appena acquisito al NOME che serve
		// per l'inserimento
		// questo grazie ai file di mapping
		LoadFileIdentificativi(new File("file/file_mapping/mappingParticella.json"),
				new File("file/file_mapping/mappingIdentificativoCatastale.json"));

	}

}
