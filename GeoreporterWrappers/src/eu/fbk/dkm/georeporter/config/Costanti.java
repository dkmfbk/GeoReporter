package eu.fbk.dkm.georeporter.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
//import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;





/**
 * Costanti
 * 
 * 
 */
public final class Costanti
{

    // Namespace

    public  String nomeFileToponimi;
  
    public  Map<String, String> toponimi;
   

    public Costanti() {
    	  nomeFileToponimi=loadProperties("toponimi");
   	   
    	  toponimi=  loadToponimi(nomeFileToponimi); 
   	  
	}


	public static final String XML_NAMESPACE = "http://www.w3.org/2001/XMLSchema#";

    
    
    // Shared repository configuration

   


	 

    
    // Utilities and constructor

    
    private    Map<String, String> loadToponimi(String nomeFileToponimi) {
    	
    Map<String, String> toponimiHM= new HashMap<String, String>();
    
   // List<String> riga=new ArrayList<String>();
    //List<String> lines=Files.readAllLines(Paths.get(file), Charset.forName("UTF-8"));
    List<String> lines;
    InputStream inputStream = Costanti.class.getClassLoader()
            .getResourceAsStream("/"+nomeFileToponimi);
    try {
	// lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
	

	//	lines = Files.readAllLines(Paths.get(nomeFileToponimi), Charset.forName("ISO-8859-2"));
		URL u= Costanti.class.getClassLoader().getResource(nomeFileToponimi);
		
		System.out.println("url=" +u);
		lines=Resources.readLines(u, Charsets.UTF_8);
		
		Iterator iterator = lines.iterator();
		String header = (String) iterator.next();
		String[] header_array = header.split(";");
	    List<String> header_list = Arrays.asList(header_array);
			   
		    while (iterator.hasNext()) {
		    	 
		        String row=(String)iterator.next();
		        String[] fields = row.split(";",-1);
		    //prendo codice  e descrizione				
		        toponimiHM.put(fields[0],fields[1]);
		    		
				}
		    for (String name : toponimiHM.keySet())  
	        { 
	            // search  for value 
	            String url = toponimiHM.get(name); 
	            System.out.println("Key = " + name + ", Value = " + url); 
	        } 
	    
	
		  
		   
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     
    	return toponimiHM;
    }
        
    
    
 	
    	
   
    
    public  String loadProperties(String property){
    	
    	String result="";
    	Properties prop = new Properties();
    	InputStream input = null;

    	try {

    		String filename = "config.properties";
    		input = Costanti.class.getClassLoader().getResourceAsStream(filename);
    		if(input==null){
    	            System.out.println("Sorry, unable to find " + filename);
    		    return result;
    		}

    		//load a properties file from class path, inside static method
    		prop.load(input);

                //get the property value and print it out
    		    result=  prop.getProperty(property);
    	        
    	   
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally{
        	if(input!=null){
        		try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	}
        }
    
           return result;    
  
    }

}
