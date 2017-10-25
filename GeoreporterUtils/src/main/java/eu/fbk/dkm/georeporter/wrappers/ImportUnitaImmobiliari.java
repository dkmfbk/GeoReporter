package eu.fbk.dkm.georeporter.wrappers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.ListAutoNumber;

import com.google.common.collect.Sets;

import eu.dbk.dkm.georeporter.wrappers.pojo.IdentificativiCatastali;
import eu.dbk.dkm.georeporter.wrappers.pojo.Particella;
import eu.dbk.dkm.georeporter.wrappers.pojo.UnitaImmobiliare;

public class ImportUnitaImmobiliari {

	 public  List<IdentificativiCatastali> list_of_identifCat=new ArrayList<IdentificativiCatastali>();
	 public  Map<String,IdentificativiCatastali> identifCat_HM=new HashMap<String,IdentificativiCatastali>();
	public  List<UnitaImmobiliare> listUnitaImmobiliari=new ArrayList<UnitaImmobiliare>();
	public  List<Particella> listParticelle=new ArrayList<Particella>();
	public Set<Particella> setParticella =new TreeSet<Particella>();
	public  Map<String,UnitaImmobiliare> unitaimmobiliari_HM= new HashMap<String,UnitaImmobiliare>();
	public void ImportUnitaImmobiliari(){
		
	}
	
	
	
	
	
	public  void getUnitaImmobiliari(String file) throws FileNotFoundException, IOException {
	
	
		
		
		
	FileInputStream inputStream = new FileInputStream(file);
	   
	    
	    List<String> riga=new ArrayList<String>();
	    List<String> lines=Files.readAllLines(Paths.get(file), Charset.forName("UTF-8"));
	    Iterator iterator = lines.iterator();
	    String header = (String) iterator.next();
	    String[] header_array = header.split(";");
   	    List<String> header_list = Arrays.asList(header_array);

	    while (iterator.hasNext()) {
	   	    Map <String,String> valori=new HashMap<String,String>();
            String row=(String)iterator.next();
	        String[] fields = row.split(";",-1);
	    	
	    	for (int i = 0; i < fields.length; i++) {
				
	    		valori.put(header_array[i].toLowerCase(),fields[i]);
			}
	    	UnitaImmobiliare ui= new UnitaImmobiliare();
	    	ui.setIdentificativoImmobile(valori.get("identificativoimmobile"));
	    	ui.setUri(valori.get("codiceamministrativo")+valori.get("identificativoimmobile"));
	    	ui.setValori(valori);
	    	listUnitaImmobiliari.add(ui);
	    	unitaimmobiliari_HM.put(ui.getUri(),ui);
	    	
	      }	
	}
	
	
	
	
	public  void getParticelle(String file) throws FileNotFoundException, IOException {
		
		FileInputStream inputStream = new FileInputStream(file);
		  
		    List<String> riga=new ArrayList<String>();
		    List<String> lines=Files.readAllLines(Paths.get(file), Charset.forName("UTF-8"));
		    Iterator iterator = lines.iterator();
		    String header = (String) iterator.next();
		    String[] header_array = header.split(";");
	   	    List<String> header_list = Arrays.asList(header_array);
	   
		    while (iterator.hasNext()) {
			    Map <String,String> valori=new HashMap<String,String>();
	            String row=(String)iterator.next();
		        String[] fields = row.split(";",-1);
		    	
		    	for (int i = 0; i < fields.length; i++) {
					
		    		valori.put(header_array[i].toLowerCase(),fields[i]);
		    		
				}
		    	Particella part= new Particella();
		    	IdentificativiCatastali ic= new IdentificativiCatastali();
		    	ic.setFoglio(valori.get("fogliomappa"));
		    	ic.setNumero(valori.get("numero"));
		    	ic.setDenominatore(valori.get("denominatore"));
		    	ic.setCodicecomunecatastale(valori.get("codicecomunecatastale"));
		    	String uri2="C"+ic.getCodicecomunecatastale()+"_F"+ic.getFoglio().replaceAll(",", "_").replaceAll("/", "_")+"_N"+ic.getNumero()+"_D"+ic.getDenominatore();
		    	ic.setUri(uri2);
		    	part.setUri("part_"+uri2);
		    	ic.setUriparticella(part.getUri());
		    //	ic.setValori(valori);
		    	listParticelle.add(part);
		    	setParticella.add(part);
		    	list_of_identifCat.add(ic);
		   // if (identifCat_HM.containsKey(uri2)){
		    	
		    //	IdentificativiCatastali idcat= identifCat_HM.get(uri2);
		    //	idcat.setUriparticella(part.getUri());
		   // }
		   // else{
		    	
		   // 	identifCat_HM.put(ic.getUri(), ic);
		    	
		   // }
		      }
		    
		    
		    
		    
		    
		    
		    
		   
			
			
			
			
		}
		
	
	
	
	
	public  void getIdentificativiCatastali(String file) throws FileNotFoundException, IOException {
		   
		

	
		    
	
		FileInputStream inputStream = new FileInputStream(file);
	   
	    
	    List<String> riga=new ArrayList<String>();
	    
	 
	    List<String> lines=Files.readAllLines(Paths.get(file), Charset.forName("UTF-8"));
	    Iterator iterator = lines.iterator();
	    String header = (String) iterator.next();
	    String[] header_array = header.split(";");
   	    List<String> header_list = Arrays.asList(header_array);
   	   
	    while (iterator.hasNext()) {
		
            String row=(String)iterator.next();
	        String[] fields = row.split(";",-1);
	    	
	    	IdentificativiCatastali ic= new IdentificativiCatastali(fields[0],
	    			fields[1],fields[2],fields[3],fields[4],fields[5]);
	    			
	    	 Map <String,String> valori=new HashMap<String,String>();
	    	
	    	 for (int i = 6; i < 21; i++) {
			   valori.put(header_array[i], fields[i]);	
			 }
	    	 
	    	 ic.setValori(valori);
	    	 String uri="C"+fields[6]+"_F"+fields[7].replaceAll(",", "_").replaceAll("/", "_")+"_N"+fields[8]+"_D"+fields[9]+"_S"+fields[10];
	    	Particella par=new Particella(fields[6],fields[7].replaceAll(",", "_").replaceAll("/", "_") , fields[8], fields[9]);
	    	 ic.setUri(uri);
	    	 ic.setUriparticella(par.getUri());
	    	 list_of_identifCat.add(ic);
	    	 identifCat_HM.put(ic.getUri(), ic);
	    	listParticelle.add(par);
	    	 
	    	 if (fields[22]!=null){
	    		 
	    		 IdentificativiCatastali ic2= new IdentificativiCatastali(fields[0],
	 	    			fields[1],fields[2],fields[3],fields[4],fields[5]);
	 	    			
	 	    	 Map <String,String> valori2=new HashMap<String,String>();
	    	 
	    	 for (int i = 22; i < 37; i++) {
				   valori2.put(header_array[i], fields[i]);	
				 }
	    	 
	    	 ic2.setValori(valori2);
	    	 String uri2="C"+fields[6]+"_F"+fields[7].replaceAll(",", "_").replaceAll("/", "_")+"_N"+fields[8]+"_D"+fields[9]+"_S"+fields[10];
	    	 Particella par2=new Particella(fields[6],fields[7].replaceAll(",", "_").replaceAll("/", "_") , fields[8], fields[9]);
	    	 ic2.setUri(uri2);
	    	 ic2.setUriparticella(par2.getUri());
	    	 list_of_identifCat.add(ic2);
	    	 identifCat_HM.put(ic2.getUri(), ic2);
	    	 listParticelle.add(par2);
	    	 if (fields[38]!=null){
	    		 IdentificativiCatastali ic3= new IdentificativiCatastali(fields[0],
	 	    			fields[1],fields[2],fields[3],fields[4],fields[5]);
	 	    			
	 	    	 Map <String,String> valori3=new HashMap<String,String>();
	    	    for (int i = 38; i < 53; i++) {
				   valori3.put(header_array[i], fields[i]);	
				 }
	    	    String uri3="C"+fields[6]+"_F"+fields[7].replaceAll(",", "_").replaceAll("/", "_")+"_N"+fields[8]+"_D"+fields[9]+"_S"+fields[10];
	    	    Particella par3=new Particella(fields[6],fields[7].replaceAll(",", "_").replaceAll("/", "_") , fields[8], fields[9]); 
	    	    ic3.setUri(uri3);
	    	    ic3.setValori(valori3);
	    	    ic3.setUriparticella(par3.getUri());
		    	 list_of_identifCat.add(ic3);
		    	 identifCat_HM.put(ic3.getUri(), ic3);
		    	 listParticelle.add(par3);
	    	 }
	    	 }
	      
	    	
	    }
	
	       
	}
	
		
	
	public static void main(String[] args) {
		
     String file1="dati/Trambileno/Trambileno_Urbano-IDR0000115470_TIPOFACSN_CAMML322-TIPO1.csv";
     String file2="dati/Trambileno/Trambileno_Urbano-IDR0000115470_TIPOFACSN_CAMML322-TIPO2-identificativi.csv";
  //   String file3="dati/Trambileno/Trambileno-Particelle_Fondiario_tipo1.csv";
	
	
	 
	 
	 ImportUnitaImmobiliari iui =new ImportUnitaImmobiliari();
	 
	 
		try {
			iui.getUnitaImmobiliari(file1);
			iui.getIdentificativiCatastali(file2);
			//iui.getParticelle(file3);
			
			 try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Individui-UI-TOT.ttl"), StandardCharsets.UTF_8))) {
			       
					for (UnitaImmobiliare ui : iui.listUnitaImmobiliari) {
						
						writer.write(":"+ui.getUri() +" rdf:type owl:NamedIndividual ,\n"
								+ ":UnitaImmobiliare ");
						
					
						ui.getValori().entrySet().forEach((e) -> { 
							try {
								if (!e.getValue().equals("")){
								writer.write(" ;\n");
								writer.write(":"+e.getKey() + " \""+ e.getValue()+"\"" );
								}
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						
					            
					        });        
						writer.write(" .\n");
					} 	
			
					
					Set<Particella> eliminacopie= new HashSet<Particella>();
                  eliminacopie.addAll(iui.listParticelle);

			for (Particella part : eliminacopie) {	
						writer.write(":"+part.getUri() +" rdf:type owl:NamedIndividual , "
								+ ":Particella ;\n");
						
					
						writer.write(part.stampa());
						
					            
					              
						
					} 	
			
					
			Set<IdentificativiCatastali> eliminacopieIC= new HashSet<IdentificativiCatastali>();
            eliminacopieIC.addAll(iui.list_of_identifCat);
					
                 for (IdentificativiCatastali ic : eliminacopieIC) {
						
                	
                	 
						writer.write(":"+ic.getUri() +" rdf:type owl:NamedIndividual ,:IdentificativoCatastale ;\n");
						if (ic.getUriImmobile()!=null){
							writer.write(":hasUnitaImmobiliare  :"+ ic.getUriImmobile());
						}
						if (ic.getUriparticella()!=null){
							//writer.write(" \n");
							writer.write("; :hasParticella  :"+ ic.getUriparticella());
						}
					
						ic.getValori().entrySet().forEach((e) -> { 
							try {
								if (!e.getValue().equals("")){
								writer.write(" ;\n");
								writer.write(":"+e.getKey() + " \""+ e.getValue()+"\"" );
								}
								
								
								
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						
					            
					        });        
						writer.write(" .\n");
					} 	
					
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		 
		 
		 
		
				 
						
		        } catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			 
		    
		 
		 
		 
		 }	 
		 
	
	
}

