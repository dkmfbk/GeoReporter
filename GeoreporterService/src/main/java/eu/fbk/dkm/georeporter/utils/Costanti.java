package eu.fbk.dkm.georeporter.utils;

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


    public static final String XML_NAMESPACE = "http://www.w3.org/2001/XMLSchema#";

    // Shared repository configuration

   
    public static final URI INTEGER = create("integer");

    public static final URI STRING = create("string");

    public static final URI FLOAT = create("float");
    
    public static final URI DATETIME = create("dateTime");
    
    public static final URI BOOLEAN = create("boolean");

   
    // Utilities and constructor

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
    
    
    
    
    
    private Costanti()
    {
    }

}
