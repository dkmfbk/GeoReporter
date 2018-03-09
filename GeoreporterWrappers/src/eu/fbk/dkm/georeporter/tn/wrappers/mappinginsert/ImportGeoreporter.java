package eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import eu.fbk.dkm.georeporter.tn.wrappers.WrapperFab;

public class ImportGeoreporter {

	
	
	
	public static void main(String[] args) {
	
	
		//Fabbricati
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43

		System.out.println("1-Inserimento UnitaImmobiliari e Note. 7min"+date);
//        MappingInsertFabUiNote.run("file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB","file/TN_header/headerfilefab.csv","file/file_mapping/mappingUI_Nota.json");
		// file di import, file header, file di mappings
        date = new Date();
        System.out.println("2-Inserimento Identificativi Catastali..6min"+date);
//		MappingInsertFabIde.run("file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB","file/TN_header/headerfilefab.csv","file/file_mapping/mappingCatastoFabbricati.json");
		
		date = new Date();
		System.out.println("3-Inserimento UI..Indirizzi 3min"+date);
     	//MappingInsertFabIndirizzo.run("file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB","file/TN_header/headerfilefab.csv","file/file_mapping/mappingIndirizzoFab.json");
		
		date = new Date();
		System.out.println("4-Inserimento Fabbricati..Soggetti "+dateFormat.format(date));
	//	MappingInsertSog.run();
		
		date = new Date();
		System.out.println("5-Inserimento Fabbricati..Titolarita "+date);
	MappingInsertTit.run();
			
		//anagrafica
		date = new Date();
		System.out.println("6-Inserimento Anagrafica.. "+date);
//	    MappingInsertAnagraficaComunale.run();
	    
	    date = new Date();
	    System.out.println("7-Inserimento Anagrafica..famiglie "+date);
//		MappingInsertFamiglia.run();

		//Contratti/
		date = new Date();
    	System.out.println("8-Inserimento Fornitura Energia "+date);
//		MappingInsertForEnergia.run();
		
		date = new Date();
		System.out.println("9-Inserimento Fornitura Gas "+date);
//		MappingInsertForGas.run();
		
		date = new Date();
		System.out.println("10-Inserimento Fornitura Locazione "+date);
//		MappingInsertForLocazione.run();
		
		//Utenze
		date = new Date();
   	    System.out.println("11-Inserimento ICI AP "+date);
//		MappingInsertTUIciImuAP.run();
		
		date = new Date();
		System.out.println("12-Inserimento ICI NP "+date);
//		MappingInsertTUIciImuNP.run();
		
		date = new Date();
		System.out.println("13-Inserimento Acqua "+date);
//	    MappingInsertTUUtenzaA.run();
	    
	    date = new Date();
		System.out.println("14-Inserimento Lettura Acqua "+date);		
//	    MappingInsertTULetturaA.run();
	    
	    date = new Date();
		System.out.println("15-Inserimento Rifiuti "+date);
//		MappingInsertTUUtenzaR.run();
		
		date = new Date();
		System.out.println("16-Inserimento Particelle fondiarie 20min"+date);
//		MappingInsertParFon.run();
		
		date = new Date();
		System.out.println("17-Inserimento Soggetti Particelle fondiarie "+date);
 //      MappingInsertSogFon.run();
        
        date = new Date();
	    System.out.println("18-Inserimento Titolarita Particelle fondiarie 2 ore!!"+date);
//		MappingInsertTtcFon.run();
		
		date = new Date();
		System.out.println("FINE"+date);
	}
	
}
