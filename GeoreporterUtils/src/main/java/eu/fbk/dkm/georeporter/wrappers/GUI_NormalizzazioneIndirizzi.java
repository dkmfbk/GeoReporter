package eu.fbk.dkm.georeporter.wrappers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.ss.usermodel.WorkbookFactory;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;
import com.google.gson.Gson;

import eu.dbk.dkm.georeporter.wrappers.pojo.DataProperty;
import eu.dbk.dkm.georeporter.wrappers.pojo.RowNumberTable;
import eu.fbk.dkm.wrappers.utils.TableColumnAdjuster;
import jxl.read.biff.BiffException;


public class GUI_NormalizzazioneIndirizzi extends JFrame {
	static Map<Integer, DataProperty> cellType_HM = new HashMap<Integer , DataProperty>();
	static JTable table;
	static JScrollPane scroll;
	  JTextArea output;
	// header is Vector contains table Column
	static Vector headers = new Vector();
	// Model is used to construct JTable
	static DefaultTableModel model = null;
	// data is Vector contains Data from Excel File
	static Vector data = new Vector();
	static JButton jbClick;
	static JFileChooser jChooser;
	static     String[] empty={""};
	String newline = "\n";
    ListSelectionModel listSelectionModel;
    JTextField textField_nome_comune = new JTextField(20);
    JTextField textField_min_row = new JTextField(4);
    JTextField textField_max_row = new JTextField(4);
    static JTextField textField_nomeClasse = new JTextField(20);
	static JComboBox combo1 = new JComboBox(empty) ;
 	static  JComboBox combo2 = new JComboBox(empty);
 	static   JComboBox combo3 = new JComboBox(empty);
	static int tableWidth = 0; // set the tableWidth
	static int tableHeight = 0; // set the tableHeight
    static  File file;
	public GUI_NormalizzazioneIndirizzi() {
	    super("Normalizzazione Indirizzi");
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setBackground(Color.white);
	    jChooser = new JFileChooser();
	    jbClick = new JButton("Select Excel File");
	    buttonPanel.add(jbClick, BorderLayout.CENTER);
	    // Show Button Click Event
	  
	    jbClick.addActionListener(new ActionListener() {
	    	
	        
	        public void actionPerformed(ActionEvent arg0) {
	            jChooser.showOpenDialog(null);

	             file = jChooser.getSelectedFile();
	            if (!file.getName().endsWith("xls")) {
	                JOptionPane.showMessageDialog(null,
	                        "Please select only Excel file.",
	                        "Error", JOptionPane.ERROR_MESSAGE);
	            } else {
	                fillData(file);
	              
					// TODO Auto-generated catch block
					
	                model = new DefaultTableModel(data,
	                        headers);
	                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	                TableColumnAdjuster tca = new TableColumnAdjuster(table);
	                tca.adjustColumns();
	             //   tableWidth = model.getColumnCount()
	              //          * 150;
	              //  tableHeight = model.getRowCount()
	               //         * 25;
	               // table.setPreferredSize(new Dimension(
	                //        tableWidth, tableHeight));
	                
	       //         Iterator iter= headers.iterator();
	        //      while(iter.hasNext()){
	          //  	  String value=(String) iter.next();
	            	//  combo1.addItem(value);
	            	 // combo2.addItem(value);
	            	 // combo3.addItem(value);
	            //  }
	      	  //  model = new DefaultTableModel(data, headers);
	     	   listSelectionModel = table.getSelectionModel();
	     	   listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
	     	   // listSelectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	     	   // table.setColumnSelectionAllowed(true);
	     	table.setSelectionModel(listSelectionModel);
	                table.setModel(model);
	            }
	        }
	    });
	    JPanel comboPanel = new JPanel();
	 

	  //Create the combo box, select item at index 4.
	  //Indices start at 0, so 4 specifies the pig.
	 //   String[] stringnull ={"","",""};
	 // petList.setSelectedIndex(4);
	    
	    JButton start_button= new JButton("Normalizza");
	    start_button.addActionListener(new ActionListener() {
	    	
	    	public void actionPerformed(ActionEvent arg0) {
	    	
	    	try {
				addNormalizedData();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	
	    	}
    });
	    
	    JButton datatype_button= new JButton("Datatype");
	    datatype_button.addActionListener(new ActionListener() {
	    	
	    	public void actionPerformed(ActionEvent arg0) {
	    	
	    	try {
	    		getDatatype();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	
	    	}
    });
	    
	    JButton export_button= new JButton("Export");
	    export_button.addActionListener(new ActionListener() {
	    	
	    	public void actionPerformed(ActionEvent arg0) {
	    		int returnVal = jChooser.showSaveDialog(null);
	    		
	    		 File file_=file ;
	            if (returnVal == JFileChooser.APPROVE_OPTION) {
	                 file_ = jChooser.getSelectedFile();
	                //This is where a real application would save the file.
	               // log.append("Saving: " + file.getName() + "." + newline);
	            } else {
	              //  log.append("Save command cancelled by user." + newline);
	            }
	            //log.setCaretPosition(log.getDocument().getLength());
	    	try {
	    		 writeToExcell(table,FileSystems.getDefault().getPath("output", file_.getName()));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	
	    	}
    });
	    
	    
	    //comboPanel.add(textField);
	  //omboPanel.add(combo1);
	  //comboPanel.add(combo2);
	//  comboPanel.add(combo3);
	    buttonPanel.add(new JLabel("Comune:"));
	    buttonPanel.add(textField_nome_comune);
	    buttonPanel.add(new JLabel("da riga:"));
	    buttonPanel.add(textField_min_row);
	    buttonPanel.add(new JLabel("a riga:"));
	    buttonPanel.add(textField_max_row);
	  buttonPanel.add(start_button);
	  buttonPanel.add(export_button);
	  buttonPanel.add(new JLabel("nome Classe:"));
	    buttonPanel.add(textField_nomeClasse);
	  buttonPanel.add(datatype_button);
	  
	//  petList.addActionListener(this);
	    table = new JTable();
	 //   table.setAutoCreateRowSorter(true);
	    model = new DefaultTableModel(data, headers);
	   listSelectionModel = table.getSelectionModel();
	   listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
	   // listSelectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	    table.setColumnSelectionAllowed(true);
	    table.setSelectionModel(listSelectionModel);
	    table.setModel(model);
	    table.getTableHeader().addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            int clickedIndex = table.convertColumnIndexToModel(table.columnAtPoint(e.getPoint()));
	           
	            table.setColumnSelectionInterval(clickedIndex, clickedIndex); //selects which column will have all its rows selected
	            table.setRowSelectionInterval(0, table.getRowCount() - 1); //once column has been selected, select all rows from 0 to the end of that column
	        }
	    });
	 //   table.setBackground(Color.pink);

	    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	 /*   for (int column = 0; column < table.getColumnCount(); column++)
	    {
	        TableColumn tableColumn = table.getColumnModel().getColumn(column);
	        int preferredWidth = tableColumn.getMinWidth();
	        int maxWidth = tableColumn.getMaxWidth();
	     
	        for (int row = 0; row < table.getRowCount(); row++)
	        {
	            TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
	            Component c = table.prepareRenderer(cellRenderer, row, column);
	            int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
	            preferredWidth = Math.max(preferredWidth, width);
	     
	            //  We've exceeded the maximum width, no need to check other rows
	     
	            if (preferredWidth >= maxWidth)
	            {
	                preferredWidth = maxWidth;
	                break;
	            }
	        }
	     
	        tableColumn.setPreferredWidth( preferredWidth );
	    }*/
	   
	    TableColumnAdjuster tca = new TableColumnAdjuster(table);
	    tca.adjustColumns();
	   table.setEnabled(true);
	 //  table.setRowHeight(25);
	 //   table.setRowMargin(4);

	//    tableWidth = model.getColumnCount() * 150;
	 //  tableHeight = model.getRowCount() * 25;
	  // table.setPreferredSize(new Dimension(
	   //      tableWidth, tableHeight));

	    
	    output = new JTextArea(10, 10);
        output.setEditable(false);
        JScrollPane outputPane = new JScrollPane(output,
                         ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                         ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

	    
      
      
	    scroll = new JScrollPane(table);
	    
	    
	    
	    
	    
	    JTable rowTable = new RowNumberTable(table);
        scroll.setRowHeaderView(rowTable);
        scroll.setCorner(JScrollPane.UPPER_LEFT_CORNER,
            rowTable.getTableHeader());
	    
	    
	    
	    
	  //  scroll.setBackground(Color.pink);
	    scroll.setPreferredSize(new Dimension(1200, 400));
	    scroll.setHorizontalScrollBarPolicy(
	            JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scroll.setVerticalScrollBarPolicy(
	            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	    getContentPane().add(buttonPanel,
	            BorderLayout.NORTH);
	    getContentPane().add(scroll,
	            BorderLayout.CENTER);
	
	    getContentPane().add(outputPane, BorderLayout.SOUTH);
	    table.getTableHeader().setPreferredSize(new Dimension(10000,32));
        setSize(1600, 600);
	    setResizable(true);
	    setVisible(true);
	}

	
	
	
	void addNormalizedData() throws FileNotFoundException, IOException{
		
		
		
	int[] columns=	table.getSelectedColumns();
	
	
	for (int row=  Integer.parseInt(textField_min_row.getText()); row<=Integer.parseInt(textField_max_row.getText()); row++){	
		String address=textField_nome_comune.getText()+",";
	  for (int i = 0; i < columns.length; i++) {
		address=address+" "+table.getValueAt(row, columns[i]);
  	}
	
//	System.out.println(address);
	    String normalized_address="";
	    BigDecimal latitude= new BigDecimal(0);
	    BigDecimal longitude=new BigDecimal(0);
	    GeocodeResponse geocoderResponse=getGoogleNormalizedAddress(address);
	    Gson gson = new Gson();
        String json="";
	List <GeocoderResult> gclist=geocoderResponse.getResults();
	 json = gson.toJson(geocoderResponse);
	for (GeocoderResult geocoderResult : gclist) {
		normalized_address=normalized_address+geocoderResult.getFormattedAddress();
		//System.out.println( geocoderResult.getFormattedAddress());
		latitude=geocoderResult.getGeometry().getLocation().getLat();
		longitude=geocoderResult.getGeometry().getLocation().getLng();
	}
	
//	output.append(address);
	 output.append(json);
	 
	 output.append("\n");
	
	
	// int row=i;
	 model.setValueAt(normalized_address, row,table.getColumn("indirizzo_normalizzato").getModelIndex());
	 model.setValueAt(latitude, row,table.getColumn("latitudine").getModelIndex());
	 model.setValueAt(longitude, row,table.getColumn("longitudine").getModelIndex());

	}
	/*	  Workbook workbook = null;
		    try {
		        try {
		            workbook = Workbook.getWorkbook(file);
		        } catch (IOException ex) {
		            Logger.getLogger(
		                    ExcelTojTable.class.
		                    getName()).log(Level.SEVERE,
		                            null, ex);
		        }
		        Sheet sheet = workbook.getSheet(0);

		      
		        for (int i = 0; i < sheet.getColumns(); i++) {
		            Cell cell1 = sheet.getCell(i, 0);
		            headers.add(cell1.getContents());
		        }

		        data.clear();
		        for (int j = 1; j < sheet.getRows(); j++) {
		            Vector d = new Vector();
		            for (int i = 0; i < sheet.getColumns(); i++) {

		                Cell cell = sheet.getCell(i, j);

		                d.add(cell.getContents());

		            }
		            d.add("\n");
		            data.add(d);
		        }
		    } catch (BiffException e) {
		        e.printStackTrace();
		    }
*/		
	}
	private static void writeToExcell(JTable table, Path path) throws FileNotFoundException, IOException {
	    new org.apache.poi.ss.usermodel.WorkbookFactory();
	    org.apache.poi.ss.usermodel.Workbook wb = new XSSFWorkbook(); //Excell workbook
	    org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet(); //WorkSheet
	    org.apache.poi.ss.usermodel.Row row = sheet.createRow(2); //Row created at line 3
	  //  TableModel model = table.getModel(); //Table model


	    org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0); //Create row at line 0
	    for(int headings = 0; headings < model.getColumnCount(); headings++){ //For each column
	        headerRow.createCell(headings).setCellValue(model.getColumnName(headings));//Write column name
	   //     System.out.println(model.getColumnName(headings));
	    }

	    for(int rows = 0; rows < model.getRowCount(); rows++){ //For each table row
	        for(int cols = 0; cols < table.getColumnCount(); cols++){ //For each table column
	            row.createCell(cols).setCellValue(model.getValueAt(rows, cols).toString()); //Write value
//System.out.println(model.getValueAt(rows, cols).toString());
	        }

	        //Set the row to the next one in the sequence 
	        row = sheet.createRow((rows + 3)); 
	    }
	    wb.write(new FileOutputStream(path.toString()));//Save the file     
	}
	
	private static void getDatatype() throws FileNotFoundException, IOException {
	   // new org.apache.poi.ss.usermodel.WorkbookFactory();
	   // org.apache.poi.ss.usermodel.Workbook wb = new XSSFWorkbook(); //Excell workbook
	  //  org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet(); //WorkSheet
	 //   org.apache.poi.ss.usermodel.Row row = sheet.createRow(2); //Row created at line 3
	  //  TableModel model = table.getModel(); //Table model
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
           
            if (cell!=null){
            	 CellType type = cell.getCellTypeEnum(); 	
            if (type == CellType.STRING) {
                System.out.println("[" + cell.getRowIndex() + ", "
                        + cell.getColumnIndex() + "] = xsd:string; Value = "
                        + cell.getRichStringCellValue().toString());
                
                value="xsd:string";
            } else if (type == CellType.NUMERIC) {
               
               double dvalue= cell.getNumericCellValue();
               value="xsd:float";
               if((dvalue == Math.floor(dvalue)) && !Double.isInfinite(dvalue) ){
            	   value="xsd:int";   
            	   
               }
              
               System.out.println("[" + cell.getRowIndex() + ", "
                       + cell.getColumnIndex() + "] =" +value+"; Value = "
                       + cell.getNumericCellValue());
               
               
            } else if (type == CellType.BOOLEAN) {
                System.out.println("[" + cell.getRowIndex() + ", "
                        + cell.getColumnIndex() + "] = BOOLEAN; Value = "
                        + cell.getBooleanCellValue());
                value="xsd:bool";
            } else if (type == CellType.BLANK) {
                System.out.println("[" + cell.getRowIndex() + ", "
                        + cell.getColumnIndex() + "] = BLANK CELL");
                value="xsd:string";
            }
            dp.addType(value);
            }
        }
        
    }
    try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("DatatypeExport-"+textField_nomeClasse.getText()+".ttl"), StandardCharsets.UTF_8))) {
       // writer.write("text to write");
        for (Integer key : cellType_HM.keySet()) {
            //System.out.println("Key: " + key + ", Value: " + cellType_HM.get(key).getName()+"  Size: "+cellType_HM.get(key).getList_of_type().size());
            
        	cellType_HM.get(key).setObjecttype(textField_nomeClasse.getText());
        	System.out.println(cellType_HM.get(key).print());
            writer.write(cellType_HM.get(key).print());
            writer.write("\n");    
        }
    
    } 
    catch (IOException ex) {
        // Handle me
    }  
    
  
    
} catch (FileNotFoundException e) {
    e.printStackTrace();
}
	}
	
	private String getRowType(int index){
		
		String result= "xsd:string";
		 for(int rows = 0; rows < model.getRowCount(); rows++){ //For each table row
		     model.getValueAt(rows, index);
		 }
		
		return result;
	}
	public GeocodeResponse getGoogleNormalizedAddress(String address){	
		final Geocoder geocoder = new Geocoder(); 
		GeocodeResponse geocoderResponse=null;
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(address).setLanguage("it").getGeocoderRequest(); 
		Gson gson = new Gson();
        String json="";
		try {
			 geocoderResponse = geocoder.geocode(geocoderRequest);
			// json = gson.toJson(geocoderResponse);
			//GeocodeResponse roundtrip = gson.fromJson(json, GeocodeResponse.class);
			//if(!geocoderResponse.equals(roundtrip)) {
				//logger.error("Roundtrip failed for: " + line);
			//}
			
			
//	System.out.println( json);
			
			//System.out.println(geocoderResponse);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return geocoderResponse;
	 }
	
	/**
	 * Fill JTable with Excel file data.
	 *
	 * @param file file :contains xls file to display in jTable
	 */
	void fillData(File file) {

	    jxl.Workbook workbook = null;
	    try {
	        try {
	            workbook = jxl.Workbook.getWorkbook(file);
	        } catch (IOException ex) {
	            Logger.getLogger(
	                    GUI_NormalizzazioneIndirizzi.class.
	                    getName()).log(Level.SEVERE,
	                            null, ex);
	        }
	        jxl.Sheet sheet = workbook.getSheet(0);

	        headers.clear();
	        for (int i = 0; i < sheet.getColumns(); i++) {
	            jxl.Cell cell1 = sheet.getCell(i, 0);
	            headers.add(cell1.getContents());
	        }
	        headers.add("indirizzo_normalizzato");
	        headers.add("latitudine");
	        headers.add("longitudine");
	        data.clear();
	        for (int j = 1; j < sheet.getRows(); j++) {
	            Vector d = new Vector();
	            for (int i = 0; i < sheet.getColumns(); i++) {

	                jxl.Cell cell = sheet.getCell(i, j);

	                d.add(cell.getContents());

	            }
	            d.add("");
	            d.add("");
	            d.add("");
	            
	            
	            d.add("\n");
	            data.add(d);
	        }
	    } catch (BiffException e) {
	        e.printStackTrace();
	    }
	}
	  class SharedListSelectionHandler implements ListSelectionListener {
	        public void valueChanged(ListSelectionEvent e) { 
	            ListSelectionModel lsm = (ListSelectionModel)e.getSource();

	            int firstIndex = e.getFirstIndex();
	            int lastIndex = e.getLastIndex();
	            boolean isAdjusting = e.getValueIsAdjusting(); 
	          //  output.append("Event for indexes "
	          //                + firstIndex + " - " + lastIndex
	          //                + "; isAdjusting is " + isAdjusting
	           //               + "; selected indexes:");

	            if (lsm.isSelectionEmpty()) {
	              //  output.append(" <none>");
	            } else {
	                // Find out which indexes are selected.
	                int minIndex = lsm.getMinSelectionIndex();
	                int maxIndex = lsm.getMaxSelectionIndex();
	                for (int i = minIndex; i <= maxIndex; i++) {
	                    if (lsm.isSelectedIndex(i)) {
	                      //  output.append(" " + i);
	                    }
	                }
	            }
	          //  output.append(newline);
	          //  output.setCaretPosition(output.getDocument().getLength());
	        }
	    }

	public static void main(String[] args) {

		GUI_NormalizzazioneIndirizzi jxl= new GUI_NormalizzazioneIndirizzi();
	 /*   try {
	    	jxl.Workbook workbook = jxl.Workbook.getWorkbook(file);
	    	jxl.Sheet sheet = workbook.getSheet(0);
	    	headers.clear();
	    	for (int i = 0; i < sheet.getColumns(); i++) {
	    	jxl.Cell cell1 = sheet.getCell(i, 0);
	    	headers.add(cell1.getContents());
	    	}
	    	data.clear();
	    	for (int j = 1; j < sheet.getRows(); j++) {
	    	Vector d = new Vector();
	    	for (int i = 0; i < sheet.getColumns(); i++) {
	    	jxl.Cell cell = sheet.getCell(i, j);
	    	d.add(cell.getContents());
	    	}
	    	d.add("\n");
	    	data.add(d);
	    	}
	    	}
	    	catch (Exception e) {
	    	e.printStackTrace();
	    	}*/
	}
	

}
