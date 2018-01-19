package eu.fbk.dkm.georeporter.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.apache.commons.lang.StringUtils;
import org.openrdf.model.URI;
import org.openrdf.model.impl.ValueFactoryImpl;



/**
 * Costanti
 * 
 * 
 */
public final class Costanti
{

    // Namespace


    public Costanti() {
		
	}


	public static final String XML_NAMESPACE = "http://www.w3.org/2001/XMLSchema#";

    
    
    // Shared repository configuration

   
    public static final URI INTEGER = create("integer");

    public static final URI STRING = create("string");

    public static final URI FLOAT = create("float");
    
    public static final URI DATETIME = create("dateTime");
    
    public static final URI BOOLEAN = create("boolean");

    public static final String nomeFileComuni=loadProperties("filecomuni");
    public static final String georeporterNS=loadProperties("georeporternamespace");;
    public static final Map<String, String> comuni_italiani= loadComuni(nomeFileComuni);
   
   
    
    // Utilities and constructor

    
    private static Map loadComuni(String nomeFileComuni) {
    	
    Map<String, String> comuniHM= new HashMap<String, String>();
    
   // List<String> riga=new ArrayList<String>();
    //List<String> lines=Files.readAllLines(Paths.get(file), Charset.forName("UTF-8"));
    List<String> lines;
    InputStream inputStream = Costanti.class.getClassLoader()
            .getResourceAsStream("/"+nomeFileComuni);
    try {
	 lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
	

		//lines = Files.readAllLines(Paths.get(nomeFileComuni), Charset.forName("ISO-8859-2"));
		Iterator iterator = lines.iterator();
		String header = (String) iterator.next();
		String[] header_array = header.split(";");
	    List<String> header_list = Arrays.asList(header_array);
			   
		    while (iterator.hasNext()) {
		    	 
		        String row=(String)iterator.next();
		        String[] fields = row.split(";",-1);
		    //prendo codice amministrativo e descrizione				
		        comuniHM.put(fields[18],fields[5]);
		    		
				}
		   
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     
    	return comuniHM;
    }
        
    
    
    private static URI create(final String localName)
    {
        return ValueFactoryImpl.getInstance().createURI(XML_NAMESPACE, localName);
    }

    public static String getType(final String localName)
    {
    	
    	String result="";
    	
    	
    	if (localName.equals(Costanti.INTEGER.stringValue()))
    		result="^^xsd:integer";
    	if (localName.equals(Costanti.STRING.stringValue()))
    		result="^^xsd:string";
    	if (localName.equals(Costanti.FLOAT.stringValue()))
    		result="^^xsd:float";
    	if (localName.equals(Costanti.DATETIME.stringValue()))
    		result="^^xsd:dateTime";
    	if (localName.equals(Costanti.BOOLEAN.stringValue()))
    		result="^^xsd:boolean";
    		
    	
    	
        return result;
    }
    
    
    public static String loadProperties(String property){
    	
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
