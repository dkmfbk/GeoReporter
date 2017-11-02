package eu.fbk.dkm.georeporter.wrappers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import com.google.gson.Gson;


import eu.fbk.dkm.georeporter.interfaces.Attributo;
import eu.fbk.dkm.georeporter.interfaces.MappingTabella;


public class GsonTest {

    public static void main(String[] args) throws FileNotFoundException {
        String path = "mappingUI.json";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

        Gson gson = new Gson();
       MappingTabella attr = gson.fromJson(bufferedReader, MappingTabella.class);
        System.out.println(attr.getId().getNome());
        List<Attributo> attrlist=attr.getAttributi();
        for (Attributo attributo : attrlist) {
        	System.out.println(attributo.getMapping());			
		}

       // System.out.println(json.getClass());
       // System.out.println(json.toString());
    }
}