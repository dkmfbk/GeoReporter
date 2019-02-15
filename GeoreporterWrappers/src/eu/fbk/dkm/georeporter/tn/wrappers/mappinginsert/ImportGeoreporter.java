package eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import eu.fbk.dkm.georeporter.config.ConfigurationManager;
import eu.fbk.dkm.georeporter.ita.wrappers.mappinginsert.MappingInsertFabIdeITA;
import eu.fbk.dkm.georeporter.ita.wrappers.mappinginsert.MappingInsertFabIndirizzoITA;
import eu.fbk.dkm.georeporter.ita.wrappers.mappinginsert.MappingInsertFabUiNoteITA;
import eu.fbk.dkm.georeporter.ita.wrappers.mappinginsert.MappingInsertParFonITA;
import eu.fbk.dkm.georeporter.ita.wrappers.mappinginsert.MappingInsertSogITA;
import eu.fbk.dkm.georeporter.ita.wrappers.mappinginsert.MappingInsertTitITA;



public class ImportGeoreporter {

	
	public ImportGeoreporter(String inserServiceURL) {
		
		targetURL=inserServiceURL;
		
	}
	
	public  String targetURL ;
	
	

	public  void importaUnitaImmobiliariITA2(String filePath,String fileHeader, String fileMappings) {
		
		MappingInsertFabIdeITA fabIDE=new MappingInsertFabIdeITA(targetURL);
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

public  void importaParticelleFondiarieITA(String filePath,String fileHeader, String fileMappings) {
	MappingInsertParFonITA inppartfon=new MappingInsertParFonITA(targetURL); 
	inppartfon.run(filePath, fileHeader, fileMappings);
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










/**
 * @param args
 */
public static void main(String[] args) {
	
	


	
	
		//Fabbricati
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
        String serviceURL="http://localhost:8081/GeoreporterService/servizio/rest/";
		ImportGeoreporter importgr= new ImportGeoreporter(serviceURL);

		
	///////////////////////////////////////////////////////////////////////////////////////////////
	/////////////////////////// ANDRANO ////////////////////////////////////////////////////////	
		
		importgr.importaUnitaImmobiliariITA1("file/Andrano/UnitaImmobiliari/A281305833_1.FAB","file/Andrano/UnitaImmobiliari/headerfilefabITA.csv","file/Andrano/UnitaImmobiliari/mappingFabbricatiUIITA.json");
//    	importgr.importaUnitaImmobiliariITA2("file/Andrano/UnitaImmobiliari/A281305833_1.FAB","file/Andrano/UnitaImmobiliari/headerfilefabITA.csv","file/Andrano/UnitaImmobiliari/mappingFabbricatiUIITA.json");
//		importgr.importaUnitaImmobiliariITA3("file/Andrano/UnitaImmobiliari/A281305833_1.FAB","file/Andrano/UnitaImmobiliari/headerfilefabITA.csv","file/Andrano/UnitaImmobiliari/mappingFabbricatiUIITA.json");
//      importgr.importaSoggettiFabbricatiITA("file/Andrano/UnitaImmobiliariSoggetti/A281305833_1.SOG","file/Andrano/UnitaImmobiliariSoggetti/headerfilesogITA.csv","file/Andrano/UnitaImmobiliariSoggetti/mappingSoggettiFabbricatiITA.json");
//		importgr.importaTitolaritaFabbricatiITA("file/Andrano/UnitaImmobiliariTitolarita/A281305833_1.TIT","file/Andrano/UnitaImmobiliariTitolarita/headerfiletitITA.csv","file/Andrano/UnitaImmobiliariTitolarita/mappingTitolaritaFabITA.json"); 
//		importgr.importaParticelleFondiarieITA( "file/Andrano/ParticelleFondiarie/A281305835_1.TER","file/Andrano/ParticelleFondiarie/headerfileparfonITA.csv","file/Andrano/ParticelleFondiarie/mappingParticellaFondiariaITA.json");				
//	    importgr.importaSoggettiFabbricatiITA("file/Andrano/ParticelleFondiarieSoggetti/A281305835_1.SOG","file/Andrano/ParticelleFondiarieSoggetti/headerfilesogfonITA.csv","file/Andrano/ParticelleFondiarieSoggetti/mappingSoggettiFondiariaITA.json");
//		importgr.importaTitolaritaFabbricatiITA("file/Andrano/ParticelleFondiarieTitolarita/A281305835_1.TIT","file/Andrano/ParticelleFondiarieTitolarita/headerfiletitfonITA.csv","file/Andrano/ParticelleFondiarieTitolarita/mappingTitolaritaFondiariaITA.json"); 
		

		
///////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////    VOLANO   /////////////////////////////////////////////////////////////
		
		
		
		
		//********************* FABBRICATI *************************************//		
		System.out.println("1-Inserimento UnitaImmobiliari e Note. 4min"+date);
	   importgr.importaUnitaImmobiliari1("file/Volano/UnitaImmobiliari/IDR0000115480_TIPOFACSN_CAMMM113.FAB","file/Volano/UnitaImmobiliari/headerfilefab.csv","file/Volano/UnitaImmobiliari/mappingFabbricatiUI.json");
		
		
		// file di import, file header, file di mappings
        date = new Date();
        System.out.println("2-Inserimento Identificativi Catastali..6min"+date);
        
        
//        importgr.importaUnitaImmobiliari2("file/Volano/UnitaImmobiliari/IDR0000115480_TIPOFACSN_CAMMM113.FAB","file/Volano/UnitaImmobiliari/headerfilefab.csv","file/Volano/UnitaImmobiliari/mappingFabbricatiUI.json");

      
		date = new Date();
		System.out.println("3-Inserimento UI..Indirizzi 3min"+date);
//		importgr.importaUnitaImmobiliari3("file/Volano/UnitaImmobiliari/IDR0000115480_TIPOFACSN_CAMMM113.FAB","file/Volano/UnitaImmobiliari/headerfilefab.csv","file/Volano/UnitaImmobiliari/mappingFabbricatiUI.json");
  
		
		date = new Date();
		System.out.println("4-Inserimento Fabbricati..Soggetti "+dateFormat.format(date));
//	  	importgr.importaSoggettiFabbricati("file/Volano/UnitaImmobiliariSoggetti/IDR0000115480_TIPOFACSN_CAMMM113.SOG","file/Volano/UnitaImmobiliariSoggetti/headerfilesog.csv","file/Volano/UnitaImmobiliariSoggetti/mappingSoggettiFabbricati.json");
		
		
		date = new Date();
		System.out.println("5-Inserimento Fabbricati..Titolarita 13min"+date);
//		importgr.importaTitolaritaFabbricati("file/Volano/UnitaImmobiliariTitolarita/IDR0000115480_TIPOFACSN_CAMMM113.TIT","file/Volano/UnitaImmobiliariTitolarita/headerfiletit.csv","file/Volano/UnitaImmobiliariTitolarita/mappingTitolaritaFab.json");

		
//****************** Anagrafica ******************************************//
		date = new Date();
		System.out.println("6-Inserimento Anagrafica.. 5min"+date);
//		importgr.importaAnagraficaComunale("file/Volano/AnagraficaComunale/anagrafe-DGASBANN.csv","file/Volano/AnagraficaComunale/mappingAnagraficaComunale.json","442");

		date = new Date();
	    System.out.println("7-Inserimento Anagrafica..famiglie "+date);
//	    importgr.importaAnagraficaFamiglie("file/Volano/AnagraficaFamiglie/anagrafe-DGASBAN2.csv","file/Volano/AnagraficaFamiglie/mappingFamiglia.json","442");
	
	   
//******************************* Particelle Fondiarie *********************************//	    
	    
		System.out.println("8-Inserimento Particelle fondiarie 20min"+date);
//        importgr.importaParticelleFondiarie( "file/Volano/ParticelleFondiarie/442_41055.PAR","file/Volano/ParticelleFondiarie/headerfileparfon.csv","file/Volano/ParticelleFondiarie/mappingParticellaFondiaria.json","M113");
	
		date = new Date();
		System.out.println("9-Inserimento Soggetti Particelle fondiarie "+date);
//        importgr.importaSoggettiParticelleFondiarie("file/Volano/ParticelleFondiarieSoggetti/442_41055.SOG","file/Volano/ParticelleFondiarieSoggetti/headerfilesogfon.csv","file/Volano/ParticelleFondiarieSoggetti/mappingSoggettoFon.json");

        
        date = new Date();
	    System.out.println("10-Inserimento Titolarita Particelle fondiarie 2 ore!!"+date);
//        importgr.importaTitolaritaParticelleFondiarie("file/Volano/ParticelleFondiarieTitolarita/442_41055.TTC","file/Volano/ParticelleFondiarieTitolarita/headerfilettcfon.csv","file/Volano/ParticelleFondiarieTitolarita/mappingTitolaritaFondiaria.json");
		
	    
	    
	    
	    
//************* Contratti **************************************/
		date = new Date();
     	System.out.println("11-Inserimento Fornitura Energia "+date);
//     	importgr.importaFornitureElettriche("file/Volano/ContrattiEnergia/VOLANO_Fornitura_Energia_dettaglio.xls","file/Volano/ContrattiEnergia/mappingFornituraEnergia.json");
		
     	date = new Date();
	    System.out.println("12-Inserimento Fornitura Gas "+date);	
//	    importgr.importaFornitureGas("file/Volano/ContrattiGas/VOLANO_Fornitura_Gas_dettaglio.xls","file/Volano/ContrattiGas/mappingFornituraGas.json");
	    
		date = new Date();
		System.out.println("13-Inserimento Fornitura Locazione "+date);
 //     importgr.importaContrattiLocazione("file/Volano/ContrattiLocazioni/VOLANO_Fornitura_Locazione2015_dettaglio.xls","file/Volano/ContrattiLocazioni/mappingFornituraLocazione.json");

    
    
//*********************************  Utenze ********************************//
		date = new Date();
  	    System.out.println("14-Inserimento ICI AP "+date);
//  	    importgr.importaTributiUtenzaICI_IMU_AbitazionePrincipale("file/Volano/TributiICI_IMU_AbitazionePrincipale/VOLANO_Utenze_I_C_I__I_M_U 2016 ABITAZIONE PRINCPALE_.xls","file/Volano/TributiICI_IMU_AbitazionePrincipale/mappingICIIMU.json");

  	    
  	    date = new Date();
		System.out.println("15-Inserimento ICI NP "+date);

//		importgr.importaTributiUtenzaICI_IMU_NudaProprieta("file/Volano/TributiICI_IMU_NudaProprieta/VOLANO_Utenze_I_C_I__I_M_U 2016 SOLO NUDI PROPRIETARI_.xls","file/Volano/TributiICI_IMU_NudaProprieta/mappingICIIMU.json");
		date = new Date();
		System.out.println("16-Inserimento Acqua "+date);

		//	    importgr.importaTributiUtenzaAcqua("file/TN_file/TambilenoMaggio2018/utenze_attive_nel_2017_trambileno_da_nostro_gestionale.xls","file/file_mapping/mappingUtenzaAcqua.json");
	    date = new Date();
		System.out.println("17-Inserimento Lettura Acqua "+date);		

//	    importgr.importaTributiLetturaAcqua("file/TN_file/TRAMBILENO_H2OExportlLETTURE_ixu3oqzmsnlv2bwhra5xvd3p1860500686.csv","file/file_mapping/mappingLetturaAcqua.json");
	    date = new Date();
		System.out.println("18-Inserimento Rifiuti "+date);

//		importgr.importaTributiUtenzeRifiuti("file/Volano/UtenzeRifiuti/VOLANO_UtenzeRIFIUTI 2016.xls","file/Volano/UtenzeRifiuti/mappingUtenzaRifiuti.json");
		date = new Date();
		
		
		
		date = new Date();
		System.out.println("FINE"+date);
	
	

		
		
		
		
		
		
		
		/////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////////
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//********************* FABBRICATI *************************************//		
		System.out.println("1-Inserimento UnitaImmobiliari e Note. 4min"+date);
//	   importgr.importaUnitaImmobiliari1("file/TN_file/IDR0000115470_TIPOFACSN_CAMML323.FAB_copy","file/TN_header/headerfilefab.csv","file/file_mapping/mappingFabbricatiUI.json");
		
		
		// file di import, file header, file di mappings
        date = new Date();
        System.out.println("2-Inserimento Identificativi Catastali..6min"+date);
      // importgr.importaUnitaImmobiliari2("file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB","file/TN_header/headerfilefab.csv","file/file_mapping/mappingFabbricatiUI.json");

      
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
  //	importgr.importaTributiUtenzaICI_IMU_AbitazionePrincipale("file/TN_file/TambilenoMaggio2018/TRAMBILENO_Utenze_I_C_I__I_M_U_ABPRINCIPALE2017.xls","file/file_mapping/mappingICIIMU.json");

  	    
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
