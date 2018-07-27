package eu.fbk.dkm.georeporter.config;

import java.util.ResourceBundle;

public class ConfigurationManager {

	public static ResourceBundle propertyfile;
	
	public ConfigurationManager(){
		
		propertyfile= ResourceBundle.getBundle("eu.fbk.dkm.georeporter.config.fileconfig");
		
		
	}
	
	public ResourceBundle getConfigFile() {
		
		return propertyfile; 
	}
	
	public  String getPropertyValue(String key) {
		
		return propertyfile.getString(key);
		
		
	}
	
}
