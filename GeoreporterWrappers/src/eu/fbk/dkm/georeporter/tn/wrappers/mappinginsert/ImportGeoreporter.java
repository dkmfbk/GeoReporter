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
        MappingInsertFabUiNote.run("file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB","file/TN_header/headerfilefab.csv","file/file_mapping/mappingUI_Nota.json");
		// file di import, file header, file di mappings
        date = new Date();
        System.out.println("2-Inserimento Identificativi Catastali..6min"+date);
  	    MappingInsertFabIde.run("file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB","file/TN_header/headerfilefab.csv","file/file_mapping/mappingCatastoFabbricati.json");
		
		date = new Date();
		System.out.println("3-Inserimento UI..Indirizzi 3min"+date);
    	MappingInsertFabIndirizzo.run("file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB","file/TN_header/headerfilefab.csv","file/file_mapping/mappingIndirizzoFab.json");
		
		date = new Date();
		System.out.println("4-Inserimento Fabbricati..Soggetti "+dateFormat.format(date));
		MappingInsertSog.run( "file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.SOG","file/TN_header/headerfilesog.csv","file/file_mapping/mappingSoggettiFabbricati.json");
		
		date = new Date();
		System.out.println("5-Inserimento Fabbricati..Titolarita "+date);
	    MappingInsertTit.run();
			
		//anagrafica
		date = new Date();
		System.out.println("6-Inserimento Anagrafica.. "+date);
//	    MappingInsertAnagraficaComunale.run("file/TN_file/DGASBANN.csv","file/file_mapping/mappingAnagraficaComunale.json","L322");
	    
	    date = new Date();
	    System.out.println("7-Inserimento Anagrafica..famiglie "+date);
//		MappingInsertFamiglia.run("file/TN_file/DGASBAN2.csv","file/file_mapping/mappingFamiglia.json","L322");

		//Contratti/
		date = new Date();
     	System.out.println("8-Inserimento Fornitura Energia "+date);
//	    MappingInsertForEnergia.run("file/TN_file/trambileno_Fornitura_Energia_dettaglio.xls","file/file_mapping/mappingFornituraEnergia.json");
		
		date = new Date();
	    System.out.println("9-Inserimento Fornitura Gas "+date);
//		MappingInsertForGas.run("file/TN_file/trambileno_Fornitura_Gas_dettaglio.xls","file/file_mapping/mappingFornituraGas.json");
		
		date = new Date();
		System.out.println("10-Inserimento Fornitura Locazione "+date);
 //   	MappingInsertForLocazione.run("file/TN_file/trambileno_Fornitura_Locazione_dettaglio.xls","file/file_mapping/mappingFornituraLocazione.json");
	
		//Utenze
		date = new Date();
  	    System.out.println("11-Inserimento ICI AP "+date);
//		MappingInsertTUIciImuAP.run("file/TN_file/TRAMBILENO_Utenze_I_C_I__I_M_U_ABPRINCIPALE.xls","file/file_mapping/mappingICIIMU.json");
		
		date = new Date();
		System.out.println("12-Inserimento ICI NP "+date);
//		MappingInsertTUIciImuNP.run("file/TN_file/TRAMBILENO_Utenze_I_C_I__I_M_U_nudoprop.xls","file/file_mapping/mappingICIIMU.json");
		
		date = new Date();
		System.out.println("13-Inserimento Acqua "+date);
//	   MappingInsertTUUtenzaA.run("file/TN_file/TRAMBILENO_Utenze_H2O_DA GARBAGE.xls","file/file_mapping/mappingUtenza.json");
	    
	    date = new Date();
		System.out.println("14-Inserimento Lettura Acqua "+date);		
//	    MappingInsertTULetturaA.run("file/TN_file/TRAMBILENO_H2OExportlLETTURE_ixu3oqzmsnlv2bwhra5xvd3p1860500686.csv","file/file_mapping/mappingLetturaAcqua.json");
	    
	    date = new Date();
		System.out.println("15-Inserimento Rifiuti "+date);
 //    	MappingInsertTUUtenzaR.run("file/TN_file/TRAMBILENO_UtenzeRIFIUTUI.xls","file/file_mapping/mappingUtenzaRifiuti.json");
		
		date = new Date();
//		System.out.println("16-Inserimento Particelle fondiarie 20min"+date);
//		MappingInsertParFon.run();
		
		date = new Date();
//		System.out.println("17-Inserimento Soggetti Particelle fondiarie "+date);
//     MappingInsertSogFon.run();
        
        date = new Date();
//	    System.out.println("18-Inserimento Titolarita Particelle fondiarie 2 ore!!"+date);
//		MappingInsertTtcFon.run();
		
		date = new Date();
		System.out.println("FINE"+date);
	}
	
}
