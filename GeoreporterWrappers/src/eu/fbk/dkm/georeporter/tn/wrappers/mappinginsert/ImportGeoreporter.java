package eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import eu.fbk.dkm.georeporter.config.ConfigurationManager;



public class ImportGeoreporter {

	
	public ImportGeoreporter(String inserServiceURL) {
		
		targetURL=inserServiceURL;
		
	}
	
	public  String targetURL ;
	
	

	public  void importaUnitaImmobiliariITA2(String filePath,String fileHeader, String fileMappings) {
		
		MappingInsertFabIde_ITA fabIDE=new MappingInsertFabIde_ITA(targetURL);
		fabIDE.run(filePath,fileHeader,fileMappings);
		
		
	}
	
public  void importaUnitaImmobiliariITA1(String filePath,String fileHeader, String fileMappings) {
		
	MappingInsertFabUiNoteITA fabIDE=new MappingInsertFabUiNoteITA(targetURL);
		fabIDE.run(filePath,fileHeader,fileMappings);
		
		
	}


public  void importaUnitaImmobiliariITA3(String filePath,String fileHeader, String fileMappings) {
	
	MappingInsertFabIndirizzoITA fabind=new MappingInsertFabIndirizzoITA(targetURL);
	fabind.run(filePath,fileHeader,fileMappings);
}


	public  void importaUnitaImmobiliari1(String filePath,String fileHeader, String fileMappings) {
		
	//	 MappingInsertFabUiNote.run("file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB","file/TN_header/headerfilefab.csv","file/file_mapping/mappingFabbricatiUI.json");
	//	 MappingInsertFabIde.run("file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB","file/TN_header/headerfilefab.csv","file/file_mapping/mappingFabbricatiUI.json");
	//	 MappingInsertFabIndirizzo.run("file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB","file/TN_header/headerfilefab.csv","file/file_mapping/mappingFabbricatiUI.json");	 	   
	//	System.out.println("Inizio inserimento1");
		MappingInsertFabUiNote fabui=new MappingInsertFabUiNote(targetURL);
		fabui.run(filePath,fileHeader,fileMappings);
		
	//	System.out.println("Inizio inserimento2");
	//	MappingInsertFabIde fabIDE=new MappingInsertFabIde(targetURL);
	//	fabIDE.run(filePath,fileHeader,fileMappings);
	//	System.out.println("Inizio inserimento3");
	//	MappingInsertFabIndirizzo fabind=new MappingInsertFabIndirizzo(targetURL);
	//	fabind.run(filePath,fileHeader,fileMappings);
		
		//MappingInsertFabUiNote.run(targetURL,filePath,fileHeader,fileMappings);
	    //MappingInsertFabIde.run(filePath,fileHeader,fileMappings);
	    //MappingInsertFabIndirizzo.run(filePath,fileHeader,fileMappings);	 	   
	}
	
	public  void importaUnitaImmobiliari2(String filePath,String fileHeader, String fileMappings) {
		
		MappingInsertFabIde fabIDE=new MappingInsertFabIde(targetURL);
		fabIDE.run(filePath,fileHeader,fileMappings);
		
		
	}
	
	
	public  void importaUnitaImmobiliari3(String filePath,String fileHeader, String fileMappings) {
		
		MappingInsertFabIndirizzo fabind=new MappingInsertFabIndirizzo(targetURL);
		fabind.run(filePath,fileHeader,fileMappings);
	}
	
	
	public  void importaSoggettiFabbricatiITA(String filePath,String fileHeader, String fileMappings) {
		MappingInsertSogITA inssog= new MappingInsertSogITA(targetURL);
		inssog.run(filePath, fileHeader, fileMappings);
	}
	
	
	public  void importaSoggettiFabbricati(String filePath,String fileHeader, String fileMappings) {
		MappingInsertSog inssog= new MappingInsertSog(targetURL);
		inssog.run(filePath, fileHeader, fileMappings);
	}
	
	public  void importaTitolaritaFabbricatiITA(String filePath,String fileHeader, String fileMappings) {
		MappingInsertTitITA instit=new MappingInsertTitITA(targetURL);
		instit.run(filePath, fileHeader, fileMappings);
	}
	
	public  void importaTitolaritaFabbricati(String filePath,String fileHeader, String fileMappings) {
		MappingInsertTit instit=new MappingInsertTit(targetURL);
		instit.run(filePath, fileHeader, fileMappings);
	}
	
    public  void importaAnagraficaComunale(String filePath, String fileMappings, String codiceComuneCatastale) {
    	MappingInsertAnagraficaComunale insanagcom = new MappingInsertAnagraficaComunale(targetURL);
    	insanagcom.run(filePath, fileMappings, codiceComuneCatastale);
	}
	
      
    
   public void importaAnagraficaFamiglie(String filePath, String fileMappings,String codiceComuneCatastale)  {
	   MappingInsertFamiglia insfamiglia = new MappingInsertFamiglia(targetURL);
	   insfamiglia.run(filePath, fileMappings, codiceComuneCatastale);
	}

   
   public  void importaFornitureGas(String filePath, String fileMappings) {
	   MappingInsertForGas insgas = new MappingInsertForGas(targetURL);
	   insgas.run(filePath, fileMappings);
   }
    
  public  void importaFornitureElettriche(String filePath, String fileMappings) {
	
	  MappingInsertForEnergia insenergia = new MappingInsertForEnergia(targetURL);
	  insenergia.run(filePath, fileMappings);
  }
    
     
  public  void importaContrattiLocazione(String filePath, String fileMappings) {
	  MappingInsertForLocazione insLocaz = new MappingInsertForLocazione(targetURL);
	  insLocaz.run(filePath,fileMappings);
}
    
    
  
public  void importaTributiUtenzeRifiuti(String filePath, String fileMappings) {
	MappingInsertTUUtenzaR insrifiuti = new MappingInsertTUUtenzaR(targetURL);	
	insrifiuti.run(filePath, fileMappings);
} 
  
  
public  void importaTributiUtenzaAcqua(String filePath, String fileMappings) {
	MappingInsertTUUtenzaA insacqua= new MappingInsertTUUtenzaA(targetURL);
	insacqua.run(filePath, fileMappings);
}  
  
  
 
public  void importaTributiLetturaAcqua(String filePath, String fileMappings) {
	
	MappingInsertTULetturaA insletturaacqua = new MappingInsertTULetturaA(targetURL);
	insletturaacqua.run(filePath, fileMappings);
}  


public  void importaTributiUtenzaICI_IMU_AbitazionePrincipale(String filePath, String fileMappings) {
	MappingInsertTUIciImuAP insiciap= new MappingInsertTUIciImuAP(targetURL);
	insiciap.run(filePath, fileMappings);
}  

public  void importaTributiUtenzaICI_IMU_NudaProprieta(String filePath, String fileMappings) {
	MappingInsertTUIciImuNP insicinp = new MappingInsertTUIciImuNP(targetURL); 
	insicinp.run(filePath, fileMappings);
}  
  
public  void importaParticelleFondiarie(String filePath,String fileHeader, String fileMappings,String codiceComuneCatastale) {
	MappingInsertParFon inppartfon=new MappingInsertParFon(targetURL); 
	inppartfon.run(filePath, fileHeader, fileMappings,codiceComuneCatastale);
}    
  
  
public  void importaSoggettiParticelleFondiarie(String filePath,String fileHeader, String fileMappings){
	MappingInsertSogFon inssogfond= new MappingInsertSogFon(targetURL); 
	inssogfond.run(filePath, fileHeader, fileMappings);
}   
  
  
public  void importaTitolaritaParticelleFondiarie(String filePath,String fileHeader, String fileMappings){
	MappingInsertTtcFon insTitfon= new MappingInsertTtcFon(targetURL); 
	insTitfon.run(filePath, fileHeader, fileMappings);
}










public static void main(String[] args) {
	
	


	
	
		//Fabbricati
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
        String serviceURL="http://localhost:8081/GeoreporterService/servizio/rest/";
		ImportGeoreporter importgr= new ImportGeoreporter(serviceURL);
	 
		
		
		// importgr.importaUnitaImmobiliariITA2("file/ITA_file/A281305833_1.FAB","file/ITA_headerfilefab.csv","file/file_mapping/mappingFabbricatiUI_ITA.json");
        // importgr.importaSoggettiFabbricatiITA("file/ITA_file/A281305833_1.SOG","file/ITA_File/headerfilesogITA.csv","file/file_mapping/mappingSoggettiFabbricati_ITA.json");
		/// importgr.importaTitolaritaFabbricatiITA("file/ITA_file/A281305833_1.TIT","file/TN_header/headerfiletitITA.csv","file/file_mapping/mappingTitolaritaFabITA.json"); 
				

		
//********************* FABBRICATI *************************************//		
		System.out.println("1-Inserimento UnitaImmobiliari e Note. 4min"+date);
	   //importgr.importaUnitaImmobiliari1("file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB","file/TN_header/headerfilefab.csv","file/file_mapping/mappingFabbricatiUI.json");
		
		
		// file di import, file header, file di mappings
        date = new Date();
        System.out.println("2-Inserimento Identificativi Catastali..6min"+date);
//        importgr.importaUnitaImmobiliari2("file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB","file/TN_header/headerfilefab.csv","file/file_mapping/mappingFabbricatiUI.json");

      
		date = new Date();
		System.out.println("3-Inserimento UI..Indirizzi 3min"+date);
//		importgr.importaUnitaImmobiliari3("file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB","file/TN_header/headerfilefab.csv","file/file_mapping/mappingFabbricatiUI.json");
  
		
		date = new Date();
		System.out.println("4-Inserimento Fabbricati..Soggetti "+dateFormat.format(date));
//	  	importgr.importaSoggettiFabbricati("file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.SOG","file/TN_header/headerfilesog.csv","file/file_mapping/mappingSoggettiFabbricati.json");
		
		
		date = new Date();
		System.out.println("5-Inserimento Fabbricati..Titolarita 13min"+date);
//		importgr.importaTitolaritaFabbricati("file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.TIT","file/TN_header/headerfiletit.csv","file/file_mapping/mappingTitolaritaFab.json");

		
//****************** Anagrafica ******************************************//
		date = new Date();
		System.out.println("6-Inserimento Anagrafica.. 5min"+date);
//		importgr.importaAnagraficaComunale("file/TN_file/DGASBANN.csv","file/file_mapping/mappingAnagraficaComunale.json","L322");

		date = new Date();
	    System.out.println("7-Inserimento Anagrafica..famiglie "+date);
//	    importgr.importaAnagraficaFamiglie("file/TN_file/DGASBAN2.csv","file/file_mapping/mappingFamiglia.json","L322");
	
	   
//******************************* Particelle Fondiarie *********************************//	    
	    
		System.out.println("8-Inserimento Particelle fondiarie 20min"+date);
    	//importgr.importaParticelleFondiarie( "file/TN_file/404_41097.PAR","file/TN_header/headerfileparfon.csv","file/file_mapping/mappingParticellaFondiaria.json","L322");
	
		date = new Date();
		System.out.println("9-Inserimento Soggetti Particelle fondiarie "+date);
//       importgr.importaSoggettiParticelleFondiarie("file/TN_file/404_41097.SOG","file/TN_header/headerfilesogfon.csv","file/file_mapping/mappingSoggettoFon.json");

        
        date = new Date();
	    System.out.println("10-Inserimento Titolarita Particelle fondiarie 2 ore!!"+date);
 //       importgr.importaTitolaritaParticelleFondiarie("file/TN_file/404_41097.TTC","file/TN_header/headerfilettcfon.csv","file/file_mapping/mappingTitolaritaFondiaria.json");
		
	    
	    
	    
	    
//************* Contratti **************************************/
		date = new Date();
     	System.out.println("11-Inserimento Fornitura Energia "+date);
 //    	importgr.importaFornitureElettriche("file/TN_file/trambileno_Fornitura_Energia_dettaglio.xls","file/file_mapping/mappingFornituraEnergia.json");
		
     	date = new Date();
	    System.out.println("12-Inserimento Fornitura Gas "+date);	
//	    importgr.importaFornitureGas("file/TN_file/trambileno_Fornitura_Gas_dettaglio.xls","file/file_mapping/mappingFornituraGas.json");
	    
		date = new Date();
		System.out.println("13-Inserimento Fornitura Locazione "+date);
//        importgr.importaContrattiLocazione("file/TN_file/trambileno_Fornitura_Locazione_dettaglio.xls","file/file_mapping/mappingFornituraLocazione.json");

    
    
//*********************************  Utenze ********************************//
		date = new Date();
  	    System.out.println("14-Inserimento ICI AP "+date);
//  	importgr.importaTributiUtenzaICI_IMU_AbitazionePrincipale("file/TN_file/TambilenoMaggio2018/TRAMBILENO_Utenze_I_C_I__I_M_U_ABPRINCIPALE2017.xls","file/file_mapping/mappingICIIMU.json");

  	    
  	    date = new Date();
		System.out.println("15-Inserimento ICI NP "+date);

//		importgr.importaTributiUtenzaICI_IMU_NudaProprieta("file/TN_file/TambilenoMaggio2018/TRAMBILENO_Utenze_I_C_I__I_M_U_nudoprop2017.xls","file/file_mapping/mappingICIIMU.json");
		date = new Date();
		System.out.println("16-Inserimento Acqua "+date);

		//	    importgr.importaTributiUtenzaAcqua("file/TN_file/TambilenoMaggio2018/utenze_attive_nel_2017_trambileno_da_nostro_gestionale.xls","file/file_mapping/mappingUtenzaAcqua.json");
	    date = new Date();
		System.out.println("17-Inserimento Lettura Acqua "+date);		

//	    importgr.importaTributiLetturaAcqua("file/TN_file/TRAMBILENO_H2OExportlLETTURE_ixu3oqzmsnlv2bwhra5xvd3p1860500686.csv","file/file_mapping/mappingLetturaAcqua.json");
	    date = new Date();
		System.out.println("18-Inserimento Rifiuti "+date);

//		importgr.importaTributiUtenzeRifiuti("file/TN_file/TambilenoMaggio2018/TRAMBILENO_UtenzeRIFIUTi2017.xls","file/file_mapping/mappingUtenzaRifiuti.json");
		date = new Date();
		
		
		
		date = new Date();
		System.out.println("FINE"+date);
	}
	
}
