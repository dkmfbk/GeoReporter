package eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.ws.rs.WebApplicationException;

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

public class MappingInsertFabUiNote {

	public  List<UnitaImmobiliare> listUnitaImmobiliari ;
	public  String targetURL;
	public WrapperFab wf = new WrapperFab();
	
	MappingInsertFabUiNote( String targetURL_){
		
		  targetURL=  targetURL_+"inserttable";
		
		
	}
	// metodo che acquisisce i 2 file di tipo json
	public  void LoadFile(File filename, File filenameNote) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gsonNote = new Gson();
		JsonReader readerNote;

		try {
			reader = new JsonReader(new FileReader(filename));
			MappingTabella data = gson.fromJson(reader, MappingTabella.class);

			readerNote = new JsonReader(new FileReader(filenameNote));
			MappingTabella dataNote = gsonNote.fromJson(readerNote, MappingTabella.class);

			
			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(data, dataNote);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public  void LoadFile(File fileMappings) {

		Gson gson = new Gson();
		JsonReader reader;

		MappingTabella mappingUntaImmobiliare= new MappingTabella();
		MappingTabella mappingNota=new MappingTabella();

		try {
			reader = new JsonReader(new FileReader(fileMappings));
			MappingTabelle mappings = gson.fromJson(reader, MappingTabelle.class);

			
			List<MappingTabella> listofMappings= mappings.getMappings();

			for (MappingTabella mappingTabella : listofMappings) {
				System.out.println(mappingTabella.getIdTabella().getNome());	
				if (mappingTabella.getIdTabella().getMapping().equals("UnitaImmobiliare")){
					mappingUntaImmobiliare=mappingTabella;
					
				}else if(mappingTabella.getIdTabella().getMapping().equals("Nota")){
					mappingNota=mappingTabella;
					
					
				}
			}
			

			// chiamata al metodo per l'accoppiamento effettivo
			associazioneMappingNomeVal(mappingUntaImmobiliare,mappingNota);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
	
	// accoppiamento valore dell'UI a quello di mapping
	public  void associazioneMappingNomeVal(MappingTabella data, MappingTabella dataNote) {
	
		
		
		Map<String,String> nameMappingsUnitaImmobiliareHM = new HashMap<String,String>();
		Map<String,String> nameMappingsNotaHM = new HashMap<String,String>();
	
	      
		
		
		for (int i = 0; i < data.getAttributi().size(); i++) {
			nameMappingsUnitaImmobiliareHM.put(data.getAttributi().get(i).getMapping().split("#")[1], data.getAttributi().get(i).getNome().split("#")[1]);
		
		}
		
		for (int i = 0; i < dataNote.getAttributi().size(); i++) {
			nameMappingsNotaHM.put(dataNote.getAttributi().get(i).getMapping().split("#")[1], dataNote.getAttributi().get(i).getNome().split("#")[1]);
		
		}
		
		
		
		
		
		
		
		
		// ciclo la lista degli elementi UI
		for (int j = 0; j < listUnitaImmobiliari.size(); j++) {
			List<Attributo> listAttributi = new ArrayList<Attributo>();
			// List<Attributo> listAttributiI = new ArrayList<Attributo>();
			List<Attributo> listChiavi = new ArrayList<Attributo>();

			List<Attributo> listNoteI = new ArrayList<Attributo>();
			List<Attributo> listNoteF = new ArrayList<Attributo>();
			// ciclo per crea la listaCHIAVI e listaATTRIBUTI richiesti dal mapping
			for (int i = 0; i < data.getAttributi().size(); i++) {

				String string = data.getAttributi().get(i).getNome();
				String[] parts = string.split("#");

				Attributo tmp = new Attributo();
				tmp.setNome(data.getAttributi().get(i).getNome());
				tmp.setMapping(data.getAttributi().get(i).getMapping());
				tmp.setTipo(data.getAttributi().get(i).getTipo());

				if ((listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get(parts[1]) != null)
						&& (listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get(parts[1])
								.isEmpty() == false)) {
					tmp.setValore(listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get(parts[1]));
					listChiavi.add(tmp);
				}

				if ((listUnitaImmobiliari.get(j).getValori().get(parts[1]) != null)
						&& (listUnitaImmobiliari.get(j).getValori().get(parts[1]).isEmpty() == false)
						|| parts[1].equals("interno") || parts[1].equals("piano")) {
					if (parts[1].equals("interno")) {
						int v = 1;
						while ( (v <= 2) && !listUnitaImmobiliari.get(j).getValori().get("interno" + v).isEmpty()) {
							Attributo tmp2 = new Attributo();
							tmp.setMultiplo(true);
							tmp2.setNome(data.getAttributi().get(i).getNome());
							tmp2.setMapping(data.getAttributi().get(i).getMapping());
							tmp2.setTipo(data.getAttributi().get(i).getTipo());
							tmp2.setValore(listUnitaImmobiliari.get(j).getValori().get("interno" + v));
							listAttributi.add(tmp2);
							v++;
						}
					} else if (parts[1].equals("piano")) {
						int v = 1;
						while ( (v <= 12) && !listUnitaImmobiliari.get(j).getValori().get("piano" + v).isEmpty()) {
							Attributo tmp2 = new Attributo();
							tmp.setMultiplo(true);
							tmp2.setNome(data.getAttributi().get(i).getNome());
							tmp2.setMapping(data.getAttributi().get(i).getMapping());
							tmp2.setTipo(data.getAttributi().get(i).getTipo());
							tmp2.setValore(listUnitaImmobiliari.get(j).getValori().get("piano" + v));
							listAttributi.add(tmp2);
							v++;
						}
					} else {
						tmp.setValore(listUnitaImmobiliari.get(j).getValori().get(parts[1]));
						listAttributi.add(tmp);
					}
				}

			}
			// ciclio per creare notaINIZIALE e notaFINALE
			for (int k = 0; k < dataNote.getAttributi().size(); k++) {

				String string = dataNote.getAttributi().get(k).getNome();
				String[] parts = string.split("#");

				Attributo tmpNI = new Attributo();
				Attributo tmpNF = new Attributo();

				tmpNI.setNome(dataNote.getAttributi().get(k).getNome());
				tmpNI.setMapping(dataNote.getAttributi().get(k).getMapping());
				tmpNI.setTipo(dataNote.getAttributi().get(k).getTipo());

				tmpNF.setNome(dataNote.getAttributi().get(k).getNome());
				tmpNF.setMapping(dataNote.getAttributi().get(k).getMapping());
				tmpNF.setTipo(dataNote.getAttributi().get(k).getTipo());

				if ((listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(parts[1]) != null)
						&& (listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(parts[1])
								.isEmpty() == false)) {
					tmpNI.setValore(listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(parts[1]));
					listNoteI.add(tmpNI);
				}
				if ((listUnitaImmobiliari.get(j).getNotaFinale().getValori().get(parts[1]) != null)
						&& (listUnitaImmobiliari.get(j).getNotaFinale().getValori().get(parts[1]).isEmpty() == false)) {
					tmpNF.setValore(listUnitaImmobiliari.get(j).getNotaFinale().getValori().get(parts[1]));
					listNoteF.add(tmpNF);
				}

			}
			// riga di tipo RIGATABELLA per UI
			RigaTabella rigaTUI = new RigaTabella();
			rigaTUI.setNometabella("http://dkm.fbk.eu/georeporter#" + data.getIdTabella().getMapping());
			rigaTUI.setListaattributi(listAttributi);
			rigaTUI.setListachiave(listChiavi);
			String codamm = listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get(nameMappingsUnitaImmobiliareHM.get("codiceAmministrativo"));
			String ideimm = listUnitaImmobiliari.get(j).getListaValoriChiave().get(0).get(nameMappingsUnitaImmobiliareHM.get("identificativoImmobile"));
			rigaTUI.setUririga("http://dkm.fbk.eu/georeporter#UI_" + codamm + "_" + ideimm);

			// NOTA INIZIALE
			RigaTabella rigaTNI = new RigaTabella();
			rigaTNI.setNometabella("http://dkm.fbk.eu/georeporter#" + dataNote.getIdTabella().getMapping());
			rigaTNI.setListaattributi(listNoteI);
			String numni = listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(nameMappingsNotaHM.get("numeroNota"));
			numni=numni.replaceAll("/", "_");
			rigaTNI.setUririga("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numni);

			// creare relazione per la nota iniziale con tipo nota
			List<Relazione> listRelNI = new ArrayList<Relazione>();
			Relazione relNITipo = new Relazione();
			relNITipo.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTipoNota");
			relNITipo.setUriDomain("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numni);
			relNITipo.setUriRange("http://dkm.fbk.eu/georeporter#"
					+ listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(nameMappingsNotaHM.get("hasTipoNota")));
			listRelNI.add(relNITipo);
			rigaTNI.setListarelazioni(listRelNI);

			// NOTA FINALE
			RigaTabella rigaTNF = new RigaTabella();
			rigaTNF.setNometabella("http://dkm.fbk.eu/georeporter#" + dataNote.getIdTabella().getMapping());
			rigaTNF.setListaattributi(listNoteF);
			String numnf = listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(nameMappingsNotaHM.get("numeroNota"));
			numnf=numnf.replaceAll("/", "_");
			rigaTNF.setUririga("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numnf);

			// creare relazione per la nota finale con tipo nota
			List<Relazione> listRelNF = new ArrayList<Relazione>();
			Relazione relNFTipo = new Relazione();
			relNFTipo.setNomerelazione("http://dkm.fbk.eu/georeporter#hasTipoNota");
			relNFTipo.setUriDomain("http://dkm.fbk.eu/georeporter#NOT_" + codamm + "_" + ideimm + "_" + numnf);
			relNFTipo.setUriRange("http://dkm.fbk.eu/georeporter#"
					+ listUnitaImmobiliari.get(j).getNotaFinale().getValori().get(nameMappingsNotaHM.get("hasTipoNota")));
			listRelNF.add(relNFTipo);
			rigaTNF.setListarelazioni(listRelNF);

			// inserisco NotaIniziale e Finale resisto per entrambe URI e iserisco la
			// relazione nella sua riga UI
			List<Relazione> listRelUI = new ArrayList<Relazione>();
			if (!listUnitaImmobiliari.get(j).getNotaIniziale().getValori().get(nameMappingsNotaHM.get("progressivoNota")).isEmpty()) {
				//String relNIuri = insertRigaReturn(rigaTNI);
				String relNIuri = insertRiga(rigaTNI);
				Relazione relNI = new Relazione();
				relNI.setNomerelazione("http://dkm.fbk.eu/georeporter#hasNotaIniziale");
				relNI.setUriDomain("http://dkm.fbk.eu/georeporter#UI_" + codamm + "_" + ideimm);
				relNI.setUriRange(relNIuri);
				listRelUI.add(relNI);
			}

			if (!listUnitaImmobiliari.get(j).getNotaFinale().getValori().get(nameMappingsNotaHM.get("progressivoNota")).isEmpty()) {
	//			String relNFuri = insertRigaReturn(rigaTNF);
				String relNFuri = insertRiga(rigaTNF);
				Relazione relNF = new Relazione();
				relNF.setNomerelazione("http://dkm.fbk.eu/georeporter#hasNotaFinale");
				relNF.setUriDomain("http://dkm.fbk.eu/georeporter#UI_" + codamm + "_" + ideimm);
				relNF.setUriRange(relNFuri);
				listRelUI.add(relNF);
			}

			// riga uri rel + inserimento
			rigaTUI.setListarelazioni(listRelUI);
			List<Attributo> tmp = rigaTUI.getListaattributi();
			boolean cancellato=false;
			String codice="";
			for (Attributo attributo : tmp) {
		//	System.out.println(attributo.getNome());
		//	System.out.println(	attributo.getValore());
		
				if ( attributo.getNome().equals("http://dkm.fbk.eu/georeporter#partita")&&attributo.getValore().equals("C")) {
				cancellato=true;
				//System.out.println("UI cancellata ="+attributo.getValore());
				//System.out.println(attributo.getNome());
				System.out.println(ideimm);
				}
					
			}
			
			if (!cancellato) {
			//	System.out.println("UI inserita");	
			insertRiga(rigaTUI);
			}
		}

	}

	// metodo per l'inserimento dell'elemento pronto dopo il mapping
	public  String insertRigaReturn_(RigaTabella riga) {

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
				// System.out.println(output);
				// output = responseBuffer.readLine();
				return output;
			}

			httpConnection.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		return output;
	}

	
	
	
	
	
	
	
	
	public  String insertRiga(RigaTabella riga) {

		// String targetURL =
		// "http://kermadec.fbk.eu:8080/GeoreporterService/servizio/rest/inserttable";
			
		 String responseEntity ="";
		Gson gson = new Gson();
		String json = gson.toJson(riga);
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
           
          
          // System.out.println(responseEntity.toString());
           
           client.destroy();
           } catch (MalformedURLException e1) {
   			// TODO Auto-generated catch block
   			e1.printStackTrace();
   		}
          return  responseEntity ; 
	   }
    
	
	

	
	
	
	
	
	
	
	
	
	
	
	public  void insertRiga_old(RigaTabella riga) {

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


	
	
	public  void run() {
		
		
		
		
		
		// IDR0000115470_TIPOFACSN_CAMML322
		String pathF = "file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB";
		String pathP = "file/TN_header/headerfilefab.csv";
		
		// chiamata per l'estrazione degli header per la composizione della lista HEADER
		wf.estrazioneHeaderFileFab(pathP);

		// chiamata per l'analisi del file .FAB
		wf.letturaFileFab(pathF);

		// chiamata al metodo che accoppia ELEMENTO appena acquisito al NOME che serve
		// per l'inserimento
		// questo grazie ai file di mapping
		LoadFile(new File("file/file_mapping/mappingUI.json"), new File("file/file_mapping/mappingNota.json"));
		
		
		
	}
	

public  void run(String pathFile,String pathFileHeader, String pathFileMappings) {

	
   
	// chiamata per l'estrazione degli header per la composizione della lista HEADER
	wf.estrazioneHeaderFileFab(pathFileHeader);

	// chiamata per l'analisi del file .FAB
	wf.letturaFileFab(pathFile);
	listUnitaImmobiliari = wf.listUnitaImmobiliari;
	// chiamata al metodo che accoppia ELEMENTO appena acquisito al NOME che serve
	// per l'inserimento
	// questo grazie ai file di mapping
	LoadFile(new File(pathFileMappings));
	
	

}	
	
	
	
	public  void main(String[] args) {

		// path del file .FAB e del file con gli HEADER inseriti a mano da un utente
		// IDR0000115470_TIPOFACSN_CAMML322
		String pathF = "file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB";
		String pathP = "file/TN_header/headerfilefab.csv";

		// chiamata per l'estrazione degli header per la composizione della lista HEADER
	//	WrapperFab.estrazioneHeaderFileFab(pathP);

		// chiamata per l'analisi del file .FAB
	//	WrapperFab.letturaFileFab(pathF);

		// chiamata al metodo che accoppia ELEMENTO appena acquisito al NOME che serve
		// per l'inserimento
		// questo grazie ai file di mapping
		LoadFile(new File("file/file_mapping/mappingUI.json"), new File("file/file_mapping/mappingNota.json"));

	}

}
