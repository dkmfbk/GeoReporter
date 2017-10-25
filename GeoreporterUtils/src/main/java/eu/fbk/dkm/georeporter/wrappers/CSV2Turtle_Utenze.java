package eu.fbk.dkm.georeporter.wrappers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import com.google.common.collect.Sets;

import eu.dbk.dkm.georeporter.wrappers.pojo.DataProperty;

public class CSV2Turtle_Utenze {

	
	
	
	public void CSV2Turle(){
		
	}
	
	
	public static void getDatatype(File file,Map<Integer, DataProperty> cellType_HM,List <String> columns) throws FileNotFoundException, IOException {
		   // new org.apache.poi.ss.usermodel.WorkbookFactory();
		   // org.apache.poi.ss.usermodel.Workbook wb = new XSSFWorkbook(); //Excell workbook
		  //  org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet(); //WorkSheet
		 //   org.apache.poi.ss.usermodel.Row row = sheet.createRow(2); //Row created at line 3
		  //  TableModel model = table.getModel(); //Table model
	//	 Map<Integer, DataProperty> cellType_HM = new HashMap<Integer , DataProperty>();
		String output="";

		    
	try {
		FileInputStream fis = new FileInputStream(file);
	    HSSFWorkbook workbook = new HSSFWorkbook(fis);
	    HSSFSheet sheet = workbook.getSheetAt(0);

	    
	    
	    
	    HSSFRow header= sheet.getRow(0);   
	    
	    Iterator rows = sheet.rowIterator();
	    HSSFRow row = (HSSFRow) rows.next();
	    
	    
	    short start=row.getFirstCellNum();
	    short end=row.getLastCellNum();
	    
	    
	        for (short colidx=start;colidx<end; colidx++){
	        
	        	
	     
	        	DataProperty dp=new DataProperty();
	        	 HSSFCell cell = row.getCell(colidx);
	        	 dp.setName(cell.getRichStringCellValue().toString());
	        	Integer key=new Integer(colidx);
	        	
	        	
	        	 cellType_HM.put(key, dp);
	        	}
	    
	    
	    
	    
	    
	    while (rows.hasNext()) {
	      
	    	row = (HSSFRow) rows.next();
	     
	    
	    
	        for (short colidx2=start;colidx2<end; colidx2++){
	        
	        	
	     
	        	DataProperty dp=new DataProperty();
	        	
	        	Integer key=new Integer(colidx2);
	        	
	        	
	        	 dp=cellType_HM.get(key);
	        
	    
	        HSSFCell cell = row.getCell(colidx2);
	      
	            String value="xsd:string";
	            CellType type = cell.getCellTypeEnum();
	            if (type == CellType.STRING) {
	            	
	            	String cellvalue=cell.getStringCellValue();
	            	cellvalue.replaceAll(",", "");
	            	try{ 
	                   Integer.parseInt(cellvalue);
	                   value="xsd:integer";
	               }catch (Exception  e){
	            		
	            	 value="xsd:string";
	            	}
	            	
	            	
	            		            	
	                System.out.println("[" + cell.getRowIndex() + ", "
	                        + cell.getColumnIndex() + "] = xsd:string; Value = "
	                        + cell.getRichStringCellValue().toString());
	                
	               
	            } else if (type == CellType.NUMERIC) {
	               
	               double dvalue= cell.getNumericCellValue();
	               value="xsd:float";
	               if((dvalue == Math.floor(dvalue)) && !Double.isInfinite(dvalue) ){
	            	   value="xsd:integer";   
	            	   
	               }
	              
	               System.out.println("[" + cell.getRowIndex() + ", "
	                       + cell.getColumnIndex() + "] =" +value+"; Value = "
	                       + cell.getNumericCellValue());
	               
	               
	            } else if (type == CellType.BOOLEAN) {
	                System.out.println("[" + cell.getRowIndex() + ", "
	                        + cell.getColumnIndex() + "] = BOOLEAN; Value = "
	                        + cell.getBooleanCellValue());
	                value="xsd:boolean";
	            } else if (type == CellType.BLANK) {
	                System.out.println("[" + cell.getRowIndex() + ", "
	                        + cell.getColumnIndex() + "] = BLANK CELL");
	                value="xsd:string";
	            }
	            dp.addType(value);
	            columns.add(dp.getName().toLowerCase().trim());
	        }
	        
	    }
	 //   try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("DatatypeExport-"+textField_nomeClasse.getText()+".ttl"), StandardCharsets.UTF_8))) {
	       // writer.write("text to write");
	 //       for (Integer key : cellType_HM.keySet()) {
	            //System.out.println("Key: " + key + ", Value: " + cellType_HM.get(key).getName()+"  Size: "+cellType_HM.get(key).getList_of_type().size());
	   //         System.out.println(cellType_HM.get(key).print(nomeClasse.getText()));
	    //        writer.write(cellType_HM.get(key).print(textField_nomeClasse.getText()));
	     //       writer.write("\n");    
	      //  }
	    
	    } 
	    catch (IOException ex) {
	        // Handle me
	    }  
	    
	  
	    
//	} catch (FileNotFoundException e) {
	//    e.printStackTrace();
	//}
	//return cellType_HM;
		}
	
	public static void main(String[] args) {
		
     File file1= new File("dati/TRAMBILENO_Utenze_I_C_I__I_M_U_TOTALI.xls");
	
	 Map<Integer, DataProperty> cellType_HM1 = new HashMap<Integer , DataProperty>();
	 List columns1= new ArrayList<String>();
	
	 File file2= new File("dati/TRAMBILENO_UtenzeRIFIUTUI.xls");	 
	 Map<Integer, DataProperty> cellType_HM2 = new HashMap<Integer , DataProperty>();
	 List columns2= new ArrayList<String>();
	 File file3= new File("dati/TRAMBILENO_Utenze_H2O_DA GARBAGE.xls");	
	 
	 Map<Integer, DataProperty> cellType_HM3 = new HashMap<Integer , DataProperty>();
	 List columns3= new ArrayList<String>();
	 
	 CSV2Turtle_Utenze csv2t =new CSV2Turtle_Utenze();
	 
	 try {
		 csv2t.getDatatype(file1, cellType_HM1, columns1);
		 csv2t.getDatatype(file2, cellType_HM2, columns2);
		 csv2t.getDatatype(file3, cellType_HM3, columns3);
		 
		 
		 Map<String, DataProperty> cellObj_HM1 = new HashMap<String , DataProperty>();
		 Map<String, DataProperty> cellObj_HM2 = new HashMap<String , DataProperty>();
		 Map<String, DataProperty> cellObj_HM3 = new HashMap<String , DataProperty>();
		 Map<String, DataProperty> cellObj_HMresult = new HashMap<String , DataProperty>();
		 List<DataProperty> results = new ArrayList<DataProperty>();
		 
		 for (Integer key : cellType_HM1.keySet()) {

			 cellObj_HM1.put( cellType_HM1.get(key).getName().toLowerCase().trim(),  cellType_HM1.get(key)) ;
			    
			}
		 for (Integer key : cellType_HM2.keySet()) {

			 cellObj_HM2.put( cellType_HM2.get(key).getName().toLowerCase().trim(),  cellType_HM2.get(key)) ;
			    
			}
		 for (Integer key : cellType_HM3.keySet()) {

			 cellObj_HM3.put( cellType_HM3.get(key).getName().toLowerCase().trim(),  cellType_HM3.get(key)) ;
			    
			}
		 
		 
		 
		 
	Set<String> lista_colonne = new TreeSet<String>();	 
		 
		 
		 lista_colonne.addAll(columns1);
		 lista_colonne.addAll(columns2);
		 lista_colonne.addAll(columns3);
		 
		 Set<String> set_ICI_IMU_ALL= new TreeSet<String>();
		 Set<String> set_Rifiuti_ALL= new TreeSet<String>();
		 Set<String> set_Acqua_ALL= new TreeSet<String>();
		 
		 set_ICI_IMU_ALL.addAll(columns1);
		 set_Rifiuti_ALL.addAll(columns2);
		 set_Acqua_ALL.addAll(columns3);
		 
		 
		
		 
		 Set<String> set_intersection_Acqua_Rifiuti = Sets.intersection(set_Rifiuti_ALL, set_Acqua_ALL);
		
		 Set <String> set_Tributi_e_Utenze = Sets.intersection(set_ICI_IMU_ALL, set_intersection_Acqua_Rifiuti);
		 
		 Set<String> set_ICI_IMU = new TreeSet(set_ICI_IMU_ALL);
		 set_ICI_IMU.removeAll(set_Tributi_e_Utenze);
		 
		 Set<String> set_Utenze = new TreeSet(set_intersection_Acqua_Rifiuti);
		 set_Utenze.removeAll(set_Tributi_e_Utenze);
		
		 
	     Set<String> set_Rifiuti=new TreeSet(set_Rifiuti_ALL); 
		 set_Rifiuti.removeAll(set_Tributi_e_Utenze);
		 set_Rifiuti.removeAll(set_Utenze);
		 
		 Set <String> set_Acqua=new TreeSet(set_Acqua_ALL);
		 set_Acqua.removeAll(set_Tributi_e_Utenze);
		 set_Acqua.removeAll(set_Utenze);
		 
		 
		 
		 for (String nome_colonna : lista_colonne) {
			 
			 if (set_Tributi_e_Utenze.contains(nome_colonna)){
				 
				 DataProperty dp= cellObj_HM1.get(nome_colonna);
				 dp.setObjecttype("Tributi_e_Utenze");
				 cellObj_HMresult.put( nome_colonna,dp  ) ;
				 results.add( dp  ) ;
				 
			 } if(set_Utenze.contains(nome_colonna)){
				 DataProperty dp= cellObj_HM2.get(nome_colonna);
				 dp.setObjecttype("Utenze");
				 cellObj_HMresult.put( nome_colonna,dp  ) ;
				 results.add( dp  ) ;
			 } if(set_Acqua.contains(nome_colonna)){
				 
				 DataProperty dp= cellObj_HM3.get(nome_colonna);
				 dp.setObjecttype("UtenzaAcqua");
				 cellObj_HMresult.put( nome_colonna,dp  ) ;
				 results.add( dp  ) ;
			 } if(set_Rifiuti.contains(nome_colonna)){
				 
				 DataProperty dp= cellObj_HM2.get(nome_colonna);
				 dp.setObjecttype("UtenzaRifiuti");
				 cellObj_HMresult.put( nome_colonna,dp  ) ;
				 results.add( dp  ) ;
			 
		     } if(set_ICI_IMU.contains(nome_colonna)){
			 
			 DataProperty dp= cellObj_HM1.get(nome_colonna);
			 dp.setObjecttype("ICI_IMU");
			 cellObj_HMresult.put( nome_colonna,dp  ) ;
			 results.add( dp  ) ;
		 }
			 
			 
			 
			 
			 
			 
		 }
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
/*		 for (String nome_colonna : lista_colonne) {
			 
			 
			 if (columns1.contains(nome_colonna)&&columns2.contains(nome_colonna)&&columns3.contains(nome_colonna)){
				 
				DataProperty dp= cellObj_HM1.get(nome_colonna);
				 dp.setObjecttype("Tributi_e_Utenze");
				 cellObj_HMresult.put( nome_colonna,dp  ) ; 
				
			 }else {
				 if(!columns1.contains(nome_colonna)){
			 
					 
					 if (columns2.contains(nome_colonna)&&columns3.contains(nome_colonna)){
						 DataProperty dp= cellObj_HM2.get(nome_colonna);
						 dp.setObjecttype("Utenze");
						 cellObj_HMresult.put( nome_colonna,dp  ) ; 
						 
					 }else {
						 if  (columns2.contains(nome_colonna)){
					 
						 DataProperty dp= cellObj_HM2.get(nome_colonna);
						 dp.setObjecttype("UtenzaRifiuti");
						 System.out.println("H2O"+ nome_colonna);
						 cellObj_HMresult.put( nome_colonna,dp  ) ; 
						
						 
						 }else {
							 if  (columns3.contains(nome_colonna)){
						 
						 DataProperty dp= cellObj_HM3.get(nome_colonna);
						 dp.setObjecttype("UtenzaAcqua");
						 cellObj_HMresult.put( nome_colonna,dp  ) ; 
						
							 }
						 
						 } if (columns1.contains(nome_colonna)){
							 
							 
							 DataProperty dp= cellObj_HM1.get(nome_colonna);
							 dp.setObjecttype("ICI_IMU");
							 cellObj_HMresult.put( nome_colonna,dp  ) ; 
						           }
					 }
				 }
			   
			    	   
			       
			 
		
			       }
						 }
*/			 
		 
		
		 
		 
		 
		 try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("DatatypeExport-Utenze_Tributi.ttl"), StandardCharsets.UTF_8))) {
		       // writer.write("text to write");
		      //  for (String key : cellObj_HMresult.keySet()) {
			 for (DataProperty dp : results){
		            //System.out.println("Key: " + key + ", Value: " + cellType_HM.get(key).getName()+"  Size: "+cellType_HM.get(key).getList_of_type().size());
		         //   System.out.println(cellObj_HMresult.get(key).print(textField_nomeClasse.getText()));
		   //         writer.write(cellObj_HMresult.get(key).print());
				 writer.write(dp.print());
		            writer.write("\n");    
		        }
			 
		    
		 
		 
		 
		 }	 
		 
		 
		 
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	 
	}
	
}

