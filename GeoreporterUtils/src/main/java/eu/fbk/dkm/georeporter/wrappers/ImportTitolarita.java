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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.ListAutoNumber;

import com.google.common.collect.Sets;

import eu.dbk.dkm.georeporter.wrappers.pojo.IdentificativiCatastali;
import eu.dbk.dkm.georeporter.wrappers.pojo.Particella;
import eu.dbk.dkm.georeporter.wrappers.pojo.Soggetto;
import eu.dbk.dkm.georeporter.wrappers.pojo.Titolarita;
import eu.dbk.dkm.georeporter.wrappers.pojo.UnitaImmobiliare;

public class ImportTitolarita {

	public  List<IdentificativiCatastali> list_of_identifCat=new ArrayList<IdentificativiCatastali>();
	public  Map<String,IdentificativiCatastali> identifCat_HM=new HashMap<String,IdentificativiCatastali>();
	public  List<Soggetto> listaSoggetti=new ArrayList<Soggetto>();
	public  List<Titolarita> listaTitolarita=new ArrayList<Titolarita>();
	public  List<Particella> listParticelle=new ArrayList<Particella>();
	public  Map<String,UnitaImmobiliare> unitaimmobiliari_HM= new HashMap<String,UnitaImmobiliare>();
	
	
	
	
	
	
	public  void getSoggetti(String file) throws FileNotFoundException, IOException {
	
	
		
		
		
	FileInputStream inputStream = new FileInputStream(file);
	   
	    
	    List<String> riga=new ArrayList<String>();
	    
	
	    //List<String> lines=Files.readAllLines(Paths.get(file), Charset.forName("UTF-8"));
	    List<String> lines=Files.readAllLines(Paths.get(file), Charset.forName("ISO-8859-2"));
	    Iterator iterator = lines.iterator();
	    String header = (String) iterator.next();
	    String[] header_array = header.split(";");
   	    List<String> header_list = Arrays.asList(header_array);
   	   
	    while (iterator.hasNext()) {
	    	 Map <String,String> valori=new HashMap<String,String>();
            String row=(String)iterator.next();
	        String[] fields = row.split(";",-1);
	    	
	    	for (int i = 0; i < fields.length; i++) {
				
	    		valori.put(StringUtils.deleteWhitespace(header_array[i].toLowerCase()),fields[i]);
	    		
			}
	    	
	    	Soggetto sog= new Soggetto();
	    	sog.setIdentificativoSoggetto(valori.get("identificativosoggetto"));
	    	sog.setUri("sog_"+valori.get("identificativosoggetto"));
	    	sog.setValori(valori);
	    	listaSoggetti.add(sog);
	    	
	    	
	      }
	    
	   
	   
		
		
		
		
	}
	
	public  void getTitolarita(String file) throws FileNotFoundException, IOException {	
		
		
	FileInputStream inputStream = new FileInputStream(file);
	   
	    
	    List<String> riga=new ArrayList<String>();
	    
	
	    //List<String> lines=Files.readAllLines(Paths.get(file), Charset.forName("UTF-8"));
	    List<String> lines=Files.readAllLines(Paths.get(file), Charset.forName("ISO-8859-2"));
	    Iterator iterator = lines.iterator();
	    String header = (String) iterator.next();
	    String[] header_array = header.split(";");
   	    List<String> header_list = Arrays.asList(header_array);
   	   
	    while (iterator.hasNext()) {
	    	 Map <String,String> valori=new HashMap<String,String>();
            String row=(String)iterator.next();
	        String[] fields = row.split(";",-1);
	    	
	    	for (int i = 0; i < fields.length; i++) {
				
	    		valori.put(StringUtils.deleteWhitespace(header_array[i].toLowerCase()),fields[i]);
	    		
			}
	    	
	    	Titolarita tit= new Titolarita();
	    	tit.setIdentificativoSoggetto(valori.get("identificativosoggetto"));
	    	tit.setUri("tit_"+valori.get("identificativotitolarita"));
	    	tit.setIdentificativoImmobile(valori.get("identificativoimmobile"));
	    	tit.setCodiceamministrativo(valori.get("codiceamministrativo"));
	    	tit.setValori(valori);
	    	listaTitolarita.add(tit);
	    	
	    	
	      }
	    
	   
	   
		
		
		
		
	}
	

	
	
	
		
	
	public static void main(String[] args) {
		
     //String file1="dati/Trambileno/urbano_IDR0000115470_TIPOFACSN_CAMML322_SOG_pFis.csv";
     String file1="dati/Trambileno/urbano_trambileno-IDR0000115470_TIPOFACSN_CAMML322_SOG_pGiu.csv";
     //String file2="dati/Trambileno/urbano_IDR0000115470_TIPOFACSN_CAMML322_TIT.csv";
   //  String file3="dati/Trambileno/Trambileno-Particelle_Fondiario_tipo1.csv";
	
	
	 
	 
	 ImportTitolarita it =new ImportTitolarita();
	 
	 
		try {
			it.getSoggetti(file1);
			//it.getTitolarita(file2);
			//iui.getParticelle(file3);
			
			 try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Individui-SoggettiPGiuridiche.ttl"), StandardCharsets.UTF_8))) {
			       
					for (Soggetto sog : it.listaSoggetti) {
						
						writer.write(":"+sog.getUri() +" rdf:type owl:NamedIndividual ,\n"
								+ ":PersonaGiuridica ");
						
					
						sog.getValori().entrySet().forEach((e) -> { 
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
		
		 
/*			 try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Individui-Titolarita.ttl"), StandardCharsets.UTF_8))) {
			       
					for (Titolarita tit : it.listaTitolarita) {
						
						writer.write(":"+tit.uri +" rdf:type owl:NamedIndividual , :Titolarita ;\n");
						
					//	writer.write(":"+tit.uri +" rdf:type owl:NamedIndividual ,:IdentificativoCatastale ;\n");
						if (tit.getIdentificativoSoggetto()!=null){
							writer.write(":hasSoggetto  :"+ tit.getIdentificativoSoggetto() + ";\n");
						}
						if (tit.getIdentificativoImmobile()!=null){
							writer.write(":hasIdentificativoCatastale  :"+ tit.getCodiceamministrativo()+tit.getIdentificativoImmobile());
						}
						
						tit.getValori().entrySet().forEach((e) -> { 
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
				
*/			 
			 
		        } catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		 
		 }	 
		 
	
	
}

