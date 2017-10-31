package eu.fbk.dkm.wrappers.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang.WordUtils;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Tab2CSV {

	
	
List<String> header = new ArrayList<String>();

public  Tab2CSV(String filename){
	
	loadheader(new File(filename));
}
	

public void loadheader(File file){
	try {
	
		List<String> lines = Files.readLines(file, Charsets.UTF_8);
		
		for (String line : lines) {
			header.add(WordUtils.capitalizeFully(line).replaceAll(" ",""));	
		}
		
		
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
//	 try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("headertitolarita1.csv"), StandardCharsets.UTF_8))) {
	       // writer.write("text to write");
	      //  for (String key : cellObj_HMresult.keySet()) {

//		 for (String string : header) {
			
	            //System.out.println("Key: " + key + ", Value: " + cellType_HM.get(key).getName()+"  Size: "+cellType_HM.get(key).getList_of_type().size());
	         //   System.out.println(cellObj_HMresult.get(key).print(textField_nomeClasse.getText()));
	   //         writer.write(cellObj_HMresult.get(key).print());
//			 writer.write(string);
//	            writer.write(";");    
//	        }
//	 } catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
	//	e.printStackTrace();
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	
}

public void parseTABFile(File file){
	
	//List<String> lines = Files.readLines(file, Charsets.UTF_8);

	 try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getName()+".csv"), StandardCharsets.UTF_8))) {
	       // writer.write("text to write");
	      //  for (String key : cellObj_HMresult.keySet()) {
		
		 for (String string : header) {
			
	            //System.out.println("Key: " + key + ", Value: " + cellType_HM.get(key).getName()+"  Size: "+cellType_HM.get(key).getList_of_type().size());
	         //   System.out.println(cellObj_HMresult.get(key).print(textField_nomeClasse.getText()));
	   //         writer.write(cellObj_HMresult.get(key).print());
			 writer.write(string);
	            writer.write(";");    
	        }
		 
		  writer.write("\n");
			Scanner scanner;
		 scanner = new Scanner(file);
			scanner.useDelimiter("\n");
			while (scanner.hasNextLine()) {
				  String line = scanner.nextLine();
				  // process the line using String#split function
			 //line.split(")
				  String tmp=new String(line);
				
				  writer.write(tmp.replaceAll("\\|", ";"));
				 
				  
				  writer.write("\n");
				} 
		 
		 
		 
		 
	 } catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	

	
	
	
	
	
	
}


public static void main(String[] args) {
	
//	Tab2CSV t2c= new Tab2CSV("dati/headertitolarita.txt");
//	t2c.parseTABFile(new File("dati/IDR0000090609_TIPOFACSI_CAMMM113.TIT"));
	//t2c.loadheader(file);
//	Tab2CSV t2c= new Tab2CSV("dati/headerunitaimmobiliari.txt");
//	t2c.parseTABFile(new File("dati/IDR0000115480_TIPOFACSN_CAMMM113.FAB"));
//	Tab2CSV t2c= new Tab2CSV("dati/headerparticelle.txt");
//	t2c.parseTABFile(new File("dati/442_41055.PAR"));
//	Tab2CSV t2c= new Tab2CSV("dati/headerunitaimmobiliari.txt");
//	t2c.parseTABFile(new File("dati/IDR0000115470_TIPOFACSN_CAMML322.FAB"));
	Tab2CSV t2c= new Tab2CSV("dati/headerparticelle.txt");
t2c.parseTABFile(new File("dati/Trambileno/fondiario-404_41097.PAR"));
	
	

}

}
