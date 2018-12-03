package eu.fbk.dkm.georeporter.tn.wrappers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.sun.jersey.api.client.WebResource;

import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Annotazioni;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Attributo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Comuni;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.IdentificativiCatastali;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Indirizzo;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.MappingTabellaHashMap;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Nota;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.PersonaFisica;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Relazione;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Rifiuti;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.RigaTabella;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Riserve;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.UnitaImmobiliare;

//import eu.fbk.dkm.georeporter.tn.wrappers.MappingInsertFabUiNote;

public class WrapperRifiuti {

	
	public  List<Rifiuti> listRifiuti = new ArrayList<Rifiuti>();
    public  MappingTabella mappingRifiuti;
    public  MappingTabellaHashMap mappingRifiutiHM;


	public  WrapperRifiuti(File file, File mapping) {
		
		try {
			loadFile(file);
			loadFileMapping(mapping);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	public  void loadFile(File file) throws FileNotFoundException, IOException {
		
					
	
		    List<String> lines=Files.readAllLines(Paths.get(file.getAbsolutePath()), Charset.forName("ISO-8859-2"));
		    Iterator iterator = lines.iterator();
		    String header = (String) iterator.next();
		    String[] header_array = header.split(";");
	   	    List<String> header_list = Arrays.asList(header_array);
	   	   
		    while (iterator.hasNext()) {
		    	 Map <String,String> valori=new HashMap<String,String>();
	            String row=(String)iterator.next();
		        String[] fields = row.split(";",-1);
		    	
		    	for (int i = 0; i < fields.length; i++) {
					
		    		valori.put(StringUtils.deleteWhitespace(header_array[i].toLowerCase()),fields[i]);
		    		
				}
		    	
		    	Rifiuti rif= new Rifiuti();
		    	rif.setCodcomune(valori.get("codcomune"));
		    	//rif.setUri("rif_"+valori.get("codiceutenza"));
		    	rif.setCodiceutenza(valori.get("codiceutenza"));
		    	rif.setCodicefiscale(valori.get("codicefiscale"));
		    	rif.setValori(valori);
		    	listRifiuti.add(rif);
		    	
		    	
		      }
		   	
			
		}


	// metodo con cui in base al tipo di elemento che ho appena estratto lo aggiungo
	// alla relativa lista di elementi dello stesso tipo
	public  void settareElemento(int indice, Map<String, String> campi,
			List<Map<String, String>> listaValoriChiave) {

		switch (indice) {
		case 1:
			break;
		case 2:
			IdentificativiCatastali ic = new IdentificativiCatastali();
			ic.setValori(campi);
			ic.setListaValoriChiave(listaValoriChiave);
			//listIdentificativiCatastali.add(ic);
			break;
		case 3:
			Indirizzo ii = new Indirizzo();
			ii.setValori(campi);
			ii.setListaValoriChiave(listaValoriChiave);
			//listIndirizzi.add(ii);
			break;
		case 4:
			Comuni cc = new Comuni();
			cc.setValori(campi);
			cc.setListaValoriChiave(listaValoriChiave);
		//	listComuni.add(cc);
			break;
		case 5:
			Riserve rr = new Riserve();
			rr.setValori(campi);
			rr.setListaValoriChiave(listaValoriChiave);
		//	listRiserve.add(rr);
			break;
		case 6:
			Annotazioni aa = new Annotazioni();
			aa.setValori(campi);
			aa.setListaValoriChiave(listaValoriChiave);
		//	listAnnotazioni.add(aa);
			break;
		default:
			;
			break;
		}

	}
	
	
	 void loadFileMapping(File filename) {

		Gson gson = new Gson();
		JsonReader reader;

		Gson gsonNote = new Gson();
		JsonReader readerNote;
		MappingTabella data = new MappingTabella();
		MappingTabellaHashMap dataHM = new MappingTabellaHashMap();
		try {
			reader = new JsonReader(new FileReader(filename));
			 data = gson.fromJson(reader, MappingTabella.class);		

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		 mappingRifiuti= data;
		 
		 dataHM.setIdTabella(mappingRifiuti.getIdTabella());
		 dataHM.setAttributi(mappingRifiuti.getAttributi());
		 Map <String,Attributo>  valoriHM= new HashMap<String,Attributo>();
		 for (Attributo attributo : mappingRifiuti.getAttributi()) {
			 valoriHM.put(attributo.getNome(), attributo); 
			 
		}
		 
	}

	public List<Rifiuti> getListRifiuti() {
		return listRifiuti;
	}

	public MappingTabella getMappingRifiuti() {
		return mappingRifiuti;
	}

	

		

}
