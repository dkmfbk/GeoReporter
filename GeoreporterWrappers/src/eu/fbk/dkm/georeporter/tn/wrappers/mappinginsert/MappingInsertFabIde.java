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

import javax.ws.rs.WebApplicationException;

import org.apache.log4j.Logger;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Annotazioni;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Comuni;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.IdentificativiCatastali;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Indirizzo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabelle;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Riserve;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;

import eu.fbk.dkm.georeporter.tn.wrappers.WrapperFab;

public class MappingInsertFabIde {

	public  List<IdentificativiCatastali> listIdentificativiCatastali ;
	public  String targetURL;
	public WrapperFab wf = new WrapperFab();
	
	
	public MappingInsertFabIde(String targetURL_) {
		
		
		targetURL=  targetURL_+"inserttable";
		
		
		
		
	}
	// metodo che acquisisce i 2 file di tipo json
	public  void LoadFileIdentificativi_old(File fileParticella, File fileIdeCat) {

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
			//associazioneMappingNomeValIdentificativo(data, data2);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	
	
	public  void LoadFileIdentificativi(File fileMappings) {

		Gson gson = new Gson();
		JsonReader reader;

		MappingTabella mappingParticella= new MappingTabella();
		MappingTabella mappingIdentificativocatastale=new MappingTabella();
		MappingTabella mappingUnitaImmobiliare=new MappingTabella();

		try {
			reader = new JsonReader(new FileReader(fileMappings));
			MappingTabelle mappings = gson.fromJson(reader, MappingTabelle.class);

			
			List<MappingTabella> listofMappings= mappings.getMappings();

			for (MappingTabella mappingTabella : listofMappings) {
			//	System.out.println(mappingTabella.getIdTabella().getNome());	
				if (mappingTabella.getIdTabella().getMapping().equals("Particella")){
					mappingParticella=mappingTabella;
					
				}else if(mappingTabella.getIdTabella().getMapping().equals("IdentificativoCatastale")){
					mappingIdentificativocatastale=mappingTabella;
					
					
				}else if(mappingTabella.getIdTabella().getMapping().equals("UnitaImmobiliare")){
					mappingUnitaImmobiliare=mappingTabella;
					
					
				}
			}
			

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeValIdentificativo(mappingParticella,mappingIdentificativocatastale,mappingUnitaImmobiliare);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
	
	
	
	public  void associazioneMappingNomeValIdentificativo(MappingTabella data, MappingTabella data2,MappingTabella data3) {
		// ciclo la lista degli elementi IDE CAt

	
		

	    //memorizzo le coppie nome/mapping
		Map<String,String> nameMappingsParticellaHM = new HashMap<String,String>();
		Map<String,String> nameMappingsIDCatastaliHM = new HashMap<String,String>();
		Map<String,String> nameMappingsUIHM = new HashMap<String,String>();
	      
		
		
		for (int i = 0; i < data.getAttributi().size(); i++) {
			nameMappingsParticellaHM.put(data.getAttributi().get(i).getMapping().split("#")[1], data.getAttributi().get(i).getNome().split("#")[1]);
		
		}
		
		for (int i = 0; i < data2.getAttributi().size(); i++) {
			nameMappingsIDCatastaliHM.put(data2.getAttributi().get(i).getMapping().split("#")[1], data2.getAttributi().get(i).getNome().split("#")[1]);
		
		}
		for (int i = 0; i < data3.getAttributi().size(); i++) {
			nameMappingsUIHM.put(data3.getAttributi().get(i).getMapping().split("#")[1], data3.getAttributi().get(i).getNome().split("#")[1]);
		
		}
		
		
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
			//aggiungo il tipo particella = E edificiale
			Attributo tmp = new Attributo();
			tmp.setNome("http://dkm.fbk.eu/georeporter#tipoparticella");
			tmp.setMapping("http://dkm.fbk.eu/georeporter#tipoDiParticella");
			tmp.setTipo("http://www.w3.org/2001/XMLSchema#string");
			tmp.setValore("E");
			listAttributi.add(tmp);
			
			// riga di tipo RIGATABELLA per Particella
			RigaTabella rigaTPar = new RigaTabella();
			rigaTPar.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTPar.setListaattributi(listAttributi);
			rigaTPar.setListachiave(listChiavi);
		//	String codamm = listIdentificativiCatastali.get(j).getListaValoriChiave().get(0).get("codiceamministrativo");
			String codamm = listIdentificativiCatastali.get(j).getListaValoriChiave().get(0).get(nameMappingsParticellaHM.get("codiceAmministrativo"));
		//	String ideimm = listIdentificativiCatastali.get(j).getListaValoriChiave().get(0).get("identificativoimmobile");
			String ideimm = listIdentificativiCatastali.get(j).getListaValoriChiave().get(0).get(nameMappingsUIHM.get("identificativoImmobile"));
			
			// controllo su uri
			String num = listIdentificativiCatastali.get(j).getValori().get(nameMappingsIDCatastaliHM.get("numero"));
			String den = listIdentificativiCatastali.get(j).getValori().get(nameMappingsIDCatastaliHM.get("denominatore"));
			String sub = listIdentificativiCatastali.get(j).getValori().get(nameMappingsIDCatastaliHM.get("subalterno"));
			String comunecatastale = listIdentificativiCatastali.get(j).getValori().get(nameMappingsParticellaHM.get("codiceComuneCatastale"));

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
			
			
			String uriIdCatastale="http://dkm.fbk.eu/georeporter#A"+codamm +"_C" + comunecatastale + "_NE" + num + "_D" + den + "_S" + sub;
			String uriParticella="http://dkm.fbk.eu/georeporter#PAE_A"+codamm+"_C" + comunecatastale + "_NE" + num + "_D" + den;
			
			
			//rigaTPar.setUririga("http://dkm.fbk.eu/georeporter#PAE_C" + comunecatastale + "_N" + num + "_D" + den);
			rigaTPar.setUririga(uriParticella);
			String relRange = insertRiga(rigaTPar);

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
			//relPartIdeCat.setUriDomain("http://dkm.fbk.eu/georeporter#C" + comunecatastale + "_N" + num + "_D" + den + "_S" + sub);
			relPartIdeCat.setUriDomain(uriIdCatastale);
			relPartIdeCat.setUriRange(relRange);
			listRelPartIdeCat.add(relPartIdeCat);

			Relazione relUIIdeCat = new Relazione();
			relUIIdeCat.setNomerelazione("http://dkm.fbk.eu/georeporter#hasUnitaImmobiliare");
			relUIIdeCat.setUriDomain(uriIdCatastale);
			relUIIdeCat.setUriRange("http://dkm.fbk.eu/georeporter#UI_" + codamm + "_" + ideimm);
			listRelPartIdeCat.add(relUIIdeCat);

			RigaTabella rigaTIdeCat = new RigaTabella();
			rigaTIdeCat.setNometabella("http://dkm.fbk.eu/georeporter#" + data2.getIdTabella().getMapping());
			rigaTIdeCat.setListaattributi(listAttributi2);
		    rigaTIdeCat.setListachiave(listChiavi2);
			rigaTIdeCat.setUririga(uriIdCatastale);

			rigaTIdeCat.setListarelazioni(listRelPartIdeCat);
			// controllo relazione particella ide cat
			// String relRange2 = MappingInsertFABUINoteFab.insertRigaReturn(rigaTIdeCat);
			insertRiga(rigaTIdeCat);
		}

	}
	
	
	// metodo per l'inserimento dell'elemento pronto dopo il mapping
	public  String insertRigaReturn_old(RigaTabella riga) {

		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
		//String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";

		Gson gson = new Gson();
		String json = gson.toJson(riga);
		// System.out.println(json);

		String output = null;

		try {

			URL targetUrl = new URL(targetURL);

			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("Accept", "application/json");

			String input = json;

			OutputStream outputStream = httpConnection.getOutputStream();
			outputStream.write(input.getBytes());
			outputStream.flush();

			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
			}

			BufferedReader responseBuffer = new BufferedReader(
					new InputStreamReader((httpConnection.getInputStream())));

			// String output;
			// System.out.println("Output from Server:\n");
			while ((output = responseBuffer.readLine()) != null) {
				System.out.println("return="+output);
				// output = responseBuffer.readLine();
				return output;
			}

			httpConnection.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		System.out.println("return="+output);
		return output;
	}

	
	
	public  String insertRiga(RigaTabella riga) {
		 Logger log = Logger.getLogger(MappingInsertFabIde.class);
		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
		//String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";	
		 String responseEntity="";
		Gson gson = new Gson();
		String json = gson.toJson(riga);
		//System.out.println("insertriga= "+json);
           try {
			URL targetUrl = new URL(targetURL);
		
           ClientConfig clientConfig = new DefaultClientConfig();
           clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
           Client client = Client.create(clientConfig);

           WebResource webResourcePost = client.resource(targetURL);
           ClientResponse  response = webResourcePost.type("application/json").post(ClientResponse.class, json);
         
           if (response.getStatus() != 200) {
           	 WebApplicationException e = response.getEntity(WebApplicationException.class);
           	 System.out.println(e.toString());
          }
           responseEntity = response.getEntity(String.class);
           
          log.debug(responseEntity.toString());
         //  System.out.println(responseEntity.toString());
           
           client.destroy();
           } catch (MalformedURLException e1) {
   			// TODO Auto-generated catch block
   			e1.printStackTrace();
   		}
           return responseEntity;
	   }
    
	
	
	
	
	public  void insertRiga2(RigaTabella riga) {

		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
		String targetURL = "http://localhost:8080/GeoreporterService/servizio/rest/inserttable";

		Gson gson = new Gson();
		String json = gson.toJson(riga);
		//System.out.println(json);

		try {

			URL targetUrl = new URL(targetURL);

			HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("Accept", "application/json");

			String input = json;

			OutputStream outputStream = httpConnection.getOutputStream();
			outputStream.write(input.getBytes());
			outputStream.flush();

			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
			}

			BufferedReader responseBuffer = new BufferedReader(
					new InputStreamReader((httpConnection.getInputStream())));

			String output;
			// System.out.println("Output from Server:\n");
			while ((output = responseBuffer.readLine()) != null) {
				// System.out.println(output);
				// output = responseBuffer.readLine();

			}

			httpConnection.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		// return output;
	}

	public  void run(String pathFile,String pathFileHeader, String pathFileMappings) {

		

		// chiamata per l'estrazione degli header per la composizione della lista HEADER
        wf.estrazioneHeaderFileFab(pathFileHeader);

		// chiamata per l'analisi del file .FAB
		wf.letturaFileFab(pathFile);
		listIdentificativiCatastali = wf.listIdentificativiCatastali;

		// chiamata al metodo che accoppia ELEMENTO appena acquisito al NOME che serve
		// per l'inserimento
		// questo grazie ai file di mapping
		LoadFileIdentificativi(new File(pathFileMappings));
		
		

	}

	public  void main(String[] args) {

		// path del file .FAB e del file con gli HEADER inseriti a mano da un utente
		// IDR0000115470_TIPOFACSN_CAMML322
		String pathF = "file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB";
		String pathP = "file/TN_header/headerfilefab.csv";

		// chiamata per l'estrazione degli header per la composizione della lista HEADER
//		WrapperFab.estrazioneHeaderFileFab(pathP);

		// chiamata per l'analisi del file .FAB
//		WrapperFab.letturaFileFab(pathF);

		// chiamata al metodo che accoppia ELEMENTO appena acquisito al NOME che serve
		// per l'inserimento
		// questo grazie ai file di mapping
		LoadFileIdentificativi_old(new File("file/file_mapping/mappingParticella.json"),
				new File("file/file_mapping/mappingIdentificativoCatastale.json"));

	}

}

