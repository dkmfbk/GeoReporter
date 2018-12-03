package eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class PulisciUI {

	



  

    	
    	public static void letturaFileFab() {

    		try {
    			String filePath="file/TN_file/listauicancellate.txt";
    			File file = new File(filePath);
    			BufferedReader reader = new BufferedReader(new FileReader(file));
    			String rigaCorrente = reader.readLine();
                Set<String> insieme= new TreeSet<String>();

    			// ciclo tutti il file, riga per riga fino a fine file
    			while (rigaCorrente != null) {
    				

    				// controllo il campo 6 del file per capire a che categoria corrisponde la riga
    				// che sto leggendo
    				 insieme.add(rigaCorrente);  	
    				 rigaCorrente = reader.readLine();
    				// System.out.println(rigaCorrente);
    			}
    	
    	
    	
    	
    	
    	
    		
    	
    	
    	
      File inFile = new File("file/TN_file/IDR0000115470_TIPOFACSN_CAMML322.FAB");

      if (!inFile.isFile()) {
        System.out.println("Parameter is not an existing file");
        return;
      }

      //Construct the new file that will later be renamed to the original filename.
      File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

      BufferedReader br = new BufferedReader(new FileReader(inFile));
      PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

      String line = null;

      
      
   
      

  
      
      
      
      
      
      
      
      //Read from the original file and write to the new
      //unless content matches data to be removed.
      while ((line = br.readLine()) != null) {
          boolean contiene= false;
    	  for (String stringa : insieme) {
       String line2=line.replace("|","");
           if (line2.contains(stringa.toString().trim())) {
        	   System.out.println("contiene "+stringa);
        	contiene=true;
            }
        
        }
        if(!contiene) {
    	  pw.println(line);
          pw.flush();
    	     	  
    	  }
        
      }
      pw.close();
      br.close();

      //Delete the original file
    //  if (!inFile.delete()) {
     //   System.out.println("Could not delete file");
    //    return;
     // }

      //Rename the new file to the filename the original file had.
      //if (!tempFile.renameTo(inFile))
        //System.out.println("Could not rename file");

    }
    catch (FileNotFoundException ex) {
      ex.printStackTrace();
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }
  }	
	
    	public static void main(String[] args) {
		
		PulisciUI pu=new PulisciUI();
		pu.letturaFileFab();;
		
		
	}
	
	
}
