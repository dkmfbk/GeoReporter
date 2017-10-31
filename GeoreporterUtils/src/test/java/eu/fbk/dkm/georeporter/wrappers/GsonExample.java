package eu.fbk.dkm.georeporter.wrappers;

import com.google.gson.Gson;

import eu.fbk.dkm.georeporter.interfaces.Attributo;
import eu.fbk.dkm.georeporter.interfaces.Record;
import eu.fbk.dkm.georeporter.interfaces.RigaTabella;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GsonExample {

    public static void main(String[] args) {

        Unitaimmobiliare staff = createDummyObject();


Gson gson = new Gson();
String json2 = gson.toJson(rec);

System.out.println(json2);
RigaTabella record = gson.fromJson(json2, RigaTabella.class);
System.out.println(record.getIdtabella());
System.out.println(record.getListaattributi().keySet());


        //1. Convert object to JSON string
        //Gson gson = new Gson();
        String json = gson.toJson(staff);
        
        System.out.println(json);
        Unitaimmobiliare ui = gson.fromJson(json, Unitaimmobiliare.class);
        System.out.println(ui.getAge());
        System.out.println(ui.getName());
        System.out.println(ui.getNotafinale().getPosition());
        System.out.println(ui.getNotainiziale().getPosition());
        System.out.println(ui.getListaattributi());

       
        
        
      
        
        
/*       try {
		JSONObject jObject= new JSONObject(json);
		
		
		Iterator<?> keys = jObject.keys();

		while( keys.hasNext() ) {
		    String key = (String)keys.next();
		    System.out.print(key);
		    if ( (jObject.get(key) instanceof JSONObject) ) {
		    	JSONObject jo=(JSONObject)jObject.get(key); 
		    	Iterator<?> keys2 = jo.keys();
		    	while( keys2.hasNext() ) {
				    String key3 = (String)keys2.next();
				    System.out.println(jo.get(key3));
		    }
		}else{
			
			System.out.println(" = "+jObject.get(key));
		}
		}
		
	} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       
        System.out.println(json);
*/
        //2. Convert object to JSON string and save into a file directly
       
       
    }

    private static Unitaimmobiliare createDummyObject() {

        Unitaimmobiliare ui = new Unitaimmobiliare();

        ui.setName("mkyong");
        ui.setAge(35);
        ui.setPosition("Founder");
        ui.setSalary(new BigDecimal("10000"));
        Map <String,String>list_attributi = new HashMap<String, String>();
        Nota notainiziale= new Nota();
        Nota notafinale= new Nota();
        notainiziale.setAge(22);
        notainiziale.setPosition("posizioneiniziale");
        
        notafinale.setAge(10);
        notafinale.setPosition("posizionefinale");
        ui.setNotafinale(notafinale);
        ui.setNotainiziale(notainiziale);  
        list_attributi.put("java","121");
        list_attributi.put("python","1313");
        list_attributi.put("shell","5355");
        ui.setListaattributi(list_attributi);
       

        return ui;

    }
  
       

        return rec;

    }

}