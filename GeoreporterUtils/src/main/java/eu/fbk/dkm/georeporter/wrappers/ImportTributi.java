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

import eu.dbk.dkm.georeporter.wrappers.pojo.Acqua;
import eu.dbk.dkm.georeporter.wrappers.pojo.ICI_IMU;
import eu.dbk.dkm.georeporter.wrappers.pojo.IdentificativiCatastali;
import eu.dbk.dkm.georeporter.wrappers.pojo.Particella;
import eu.dbk.dkm.georeporter.wrappers.pojo.Rifiuti;
import eu.dbk.dkm.georeporter.wrappers.pojo.Soggetto;
import eu.dbk.dkm.georeporter.wrappers.pojo.Titolarita;
import eu.dbk.dkm.georeporter.wrappers.pojo.UnitaImmobiliare;

public class ImportTributi {

	public  List<IdentificativiCatastali> list_of_identifCat=new ArrayList<IdentificativiCatastali>();
	public  Map<String,IdentificativiCatastali> identifCat_HM=new HashMap<String,IdentificativiCatastali>();
	public  List<Soggetto> listaSoggetti=new ArrayList<Soggetto>();
	public  List<ICI_IMU> listaICI=new ArrayList<ICI_IMU>();
	public  List<Rifiuti> listaRifiuti=new ArrayList<Rifiuti>();
	public  List<Acqua> listaAcqua=new ArrayList<Acqua>();
	public  List<Titolarita> listaTitolarita=new ArrayList<Titolarita>();
	public  List<Particella> listParticelle=new ArrayList<Particella>();
	public  Map<String,UnitaImmobiliare> unitaimmobiliari_HM= new HashMap<String,UnitaImmobiliare>();
	
	
	
	
	
	
	public  void getICI(String file) throws FileNotFoundException, IOException {
	
	
		
		
		
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
	    	
	    	ICI_IMU ici= new ICI_IMU();
	    	ici.setCodcomune(valori.get("codcomune"));
	    	ici.setUri("ici_"+valori.get("codiceutenza"));
	    	ici.setCodiceutenza(valori.get("codiceutenza"));
	    	ici.setCodicefiscale(valori.get("codicefiscale"));
	    	ici.setValori(valori);
	    	listaICI.add(ici);
	    	
	    	
	      }
	    
	   
	   
		
		
		
		
	}
	
	public  void getRifiuti(String file) throws FileNotFoundException, IOException {
		
		
		
		
		
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
		    	
		    	Rifiuti rif= new Rifiuti();
		    	rif.setCodcomune(valori.get("codcomune"));
		    	rif.setUri("rif_"+valori.get("codiceutenza"));
		    	rif.setCodiceutenza(valori.get("codiceutenza"));
		    	rif.setCodicefiscale(valori.get("codicefiscale"));
		    	rif.setValori(valori);
		    	listaRifiuti.add(rif);
		    	
		    	
		      }
		   	
			
		}
		
	public  void getAcqua(String file) throws FileNotFoundException, IOException {
		
		
		
		
		
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
		    	
		    	Acqua acq= new Acqua();
		    	acq.setCodcomune(valori.get("codcomune"));
		    	acq.setUri("acq_"+valori.get("codiceutenza"));
		    	acq.setCodiceutenza(valori.get("codiceutenza"));
		    	acq.setCodicefiscale(valori.get("codicefiscale"));
		    	acq.setValori(valori);
		    	listaAcqua.add(acq);
		    	
		    	
		      }
		    
		   
		   
			
			
			
			
		}
	
	public static void main(String[] args) {
		
     String file1="dati/Trambileno/TRAMBILENO_Utenze_I_C_I__I_M_U_TOTALI.csv";
     String file2="dati/Trambileno/TRAMBILENO_UtenzeRIFIUTUI.csv";
     String file3="dati/Trambileno/TRAMBILENO_Utenze_H2O_DA GARBAGE.csv";
	
	
	 
	 
	 ImportTributi it =new ImportTributi();
	 
	 
		try {
			
/*			it.getICI(file1);
		      
		
		 
			 try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Individui-ICI.ttl"), StandardCharsets.UTF_8))) {
			       
					for (ICI_IMU ici : it.listaICI) {
						
						writer.write(":"+ici.uri +" rdf:type owl:NamedIndividual , :ICI_IMU ");
						
					//	writer.write(":"+tit.uri +" rdf:type owl:NamedIndividual ,:IdentificativoCatastale ;\n");
					//	if (ici.getIdentificativoSoggetto()!=null){
					//		writer.write(":hasSoggetto  :"+ ici.getIdentificativoSoggetto() + ";\n");
					//	}
					//	if (ici.getIdentificativoImmobile()!=null){
					//		writer.write(":hasIdentificativoCatastale  :"+ ici.getCodiceamministrativo()+ici.getIdentificativoImmobile());
					//	}
						
						ici.getValori().entrySet().forEach((e) -> { 
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
		
		/*	 it.getRifiuti(file2);
		      
				
			 
			 try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Individui-Rifiuti.ttl"), StandardCharsets.UTF_8))) {
			       
					for (Rifiuti rif : it.listaRifiuti) {
						
						writer.write(":"+rif.uri +" rdf:type owl:NamedIndividual , :UtenzaRifiuti ");
						
						
						rif.getValori().entrySet().forEach((e) -> { 
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
	 it.getAcqua(file3);
		      
				
			 
			 try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Individui-Acqua.ttl"), StandardCharsets.UTF_8))) {
			       
					for (Acqua rif : it.listaAcqua) {
						
						writer.write(":"+rif.getUri() +" rdf:type owl:NamedIndividual , :UtenzaAcqua ");
						
						
						rif.getValori().entrySet().forEach((e) -> { 
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

