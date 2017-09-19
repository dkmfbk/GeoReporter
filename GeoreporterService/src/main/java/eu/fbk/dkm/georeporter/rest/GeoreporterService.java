package eu.fbk.dkm.georeporter.rest;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.plaf.metal.MetalBorders.Flush3DBorder;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;


import eu.fbk.dkm.georeporter.pojos.FornituraEnergia;
import eu.fbk.dkm.georeporter.pojos.FornituraGas;
import eu.fbk.dkm.georeporter.pojos.FornituraLocazioni;
import eu.fbk.dkm.georeporter.pojos.Particella;
import eu.fbk.dkm.georeporter.pojos.Soggetto;
import eu.fbk.dkm.georeporter.pojos.TributiICI;
import eu.fbk.dkm.georeporter.pojos.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.pojos.UtenzaAcqua;
import eu.fbk.dkm.georeporter.pojos.UtenzaRifiuti;







/**
 * GeoreporterService e' un servizio REST che espone i metodi per gestire la knowledge base
 * Fornisce inoltre una serie di metodi per recuperare le informazioni
 * 
 * @author Gaetano Calabrese,
 *
 */
@Path("/rest")
public class GeoreporterService {
	
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String queryStringPrefix= 
			   "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
			+	"PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
			+	"PREFIX owl:<http://www.w3.org/2002/07/owl#>\n"
			+	"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n"
			+	"PREFIX  :<http://dkm.fbk.eu/georeporter#>\n";

    public  GeoreporterService (){
    	super();
    }

    /**
     * Method to test Springles Service -
     * url : http://localhost:8080/GeoreporterService/rest/hallo
     * @return message to a web page
     */
    @GET
	@Path("/hallo")
	@Produces(MediaType.TEXT_PLAIN)
	  public String sayPlainTextHello() {
	    return "Hello!It's SpringlesService";
	  }

	 
    /**
     * Metodo per inserire i dati relativi ad una fornitura di Gas
     * url : http://localhost:8080/GeoreporterService/rest/forniruregas
     * @return message to a web page
     */  
	@POST
	@Path("/fornituregas")
	@Produces(MediaType.APPLICATION_JSON)
	public void setFornitureGas(		
						
										
							
							@QueryParam("ammontarefatturato") String ammontarefatturato,
							@QueryParam("consumofatturato") String consumofatturato,				
							@QueryParam("tipoutenzagas") String tipoutenzagas,
							@QueryParam("idsiatelgas") String idsiatelgas,
							@QueryParam("idsiatelgasdettaglio") String idsiatelgasdettaglio	,
							@QueryParam("Denominazione") String	denominazione,
							@QueryParam("CodComune") String	codcomune,		
							@QueryParam("AnnoRiferimento") String	annoriferimento	,
							@QueryParam("CodFiscaleTitolareUtenza") String		CodFiscaleTitolareUtenza,
							@QueryParam("TipoPersona") String		TipoPersona,
							@QueryParam("DatiAnagrafici	") String		DatiAnagrafici,
							@QueryParam("TipoUtenzaEnergia	") String		TipoUtenzaEnergia,
							@QueryParam("IndirizzoUtenza	") String		IndirizzoUtenza,
						    @QueryParam("SpesaConsumo	") String		SpesaConsumo,
          				    @QueryParam("KWhFatturato	") String		KWhFatturato,
						    @QueryParam("NumeroMesiFatturazione	") String		NumeroMesiFatturazione,
						    @QueryParam("CAPUtenza") String		CAPUtenza
				) {
		
		
		
		
		// if ((hasTerm==null)||hasTerm.equals("")){
				
				ResponseBuilderImpl builder = new ResponseBuilderImpl();
				builder.status(Response.Status.BAD_REQUEST);
				builder.entity("ERROR! term parameter missing or null");
				Response response = builder.build();
				throw new WebApplicationException(response);
				
				
				
		//	 }
  
	}
	
	
	 /**
     * Metodo per inserire i dati relativi ad una fornitura di Energia
     * url : http://localhost:8080/GeoreporterService/rest/fornirureenergia
     * @return message to a web page
     */  
	@POST
	@Path("/fornitureenergia")
	@Produces(MediaType.APPLICATION_JSON)
	public void setFornitureEnergia(		
							@QueryParam("idsiatelenergisdettaglio") String idsiatelenergisdettaglio	,
							@QueryParam("ammontarefatturato") String ammontarefatturato,
							@QueryParam("consumofatturato") String consumofatturato,				
							@QueryParam("tipoutenzaenergia") String tipoutenzaenergia,
							@QueryParam("idsiatelenergia") String idsiatelenergia,
							@QueryParam("Denominazione") String	denominazione,
							@QueryParam("CodComune") String	codcomune,		
							@QueryParam("AnnoRiferimento") String	annoriferimento	,
							@QueryParam("CodFiscaleTitolareUtenza") String		CodFiscaleTitolareUtenza,
							@QueryParam("TipoPersona") String		TipoPersona,
							@QueryParam("DatiAnagrafici	") String		DatiAnagrafici,
							@QueryParam("TipoUtenzaEnergia	") String		TipoUtenzaEnergia,
							@QueryParam("IndirizzoUtenza	") String		IndirizzoUtenza,
						    @QueryParam("SpesaConsumo	") String		SpesaConsumo,
          				    @QueryParam("KWhFatturato	") String		KWhFatturato,
						    @QueryParam("NumeroMesiFatturazione	") String		NumeroMesiFatturazione,
						    @QueryParam("IdSiatelEnergia") String		IdSiatelEnergia,
						    @QueryParam("CAPUtenza") String		CAPUtenza
		
				) {
		
		
		
		
		// if ((hasTerm==null)||hasTerm.equals("")){
				
				ResponseBuilderImpl builder = new ResponseBuilderImpl();
				builder.status(Response.Status.BAD_REQUEST);
				builder.entity("ERROR! term parameter missing or null");
				Response response = builder.build();
				throw new WebApplicationException(response);
				
				
				
		//	 }
  
	}
	

	
	
	/**
     * Metodo che restituisce la lista di tutte le particelle
     * 
     *<p> SELECT   ?particella ?ui "
     *  WHERE { "
     *    ?x a :IdentificativoCatastale . 
     *    ?x :hasParticella ?particella . 
     *     ?x :hasUnitaImmobiliare ?ui 
	 *  } ORDER by ?particella 
     * </p>
     * url : http://localhost:8080/GeoreporterService/rest/hallo
     * @return message to a web page
     */
	@GET
	@Path("/particelle")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Particella> getParticelle(){
	
//		@QueryParam("springlesserverURL") String springlesserverURL,
//		@QueryParam("springlesrepositoryID") String springlesrepositoryID
//		) {
			 String springlesserverURL = "http://localhost:8080/openrdf-sesame";
			 
String springlesrepositoryID ="georeporter";

List<BindingSet> tuples = new ArrayList<BindingSet>();
List<Particella> listaParticella	=new ArrayList<Particella>();	

Repository		myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
	try {
		myRepository.initialize();
	
		RepositoryConnection connection = myRepository.getConnection();
		





String	 queryString = queryStringPrefix
			
+"SELECT   ?particella ?ui ?comunecatastale ?numero  ?foglio ?denominatore "
+"WHERE { "
+"    ?x a :IdentificativoCatastale . "
+"    ?x :hasParticella ?particella . "
+"    ?x :hasUnitaImmobiliare ?ui ."

+"    ?particella :numero ?numero . "
+"    ?particella :codicecomunecatastale ?comunecatastale . "
+" OPTIONAL{   ?particella :foglio ?foglio  }. "
+" OPTIONAL{   ?particella :denominatore ?denominatore  } . "

+"}  ";
	
		
	

System.out.println(queryString);		
TupleQuery tupleQuery;

int i = 0;


	tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL,
			queryString);
	
	TupleQueryResult qresult = tupleQuery.evaluate();


	while (qresult.hasNext()) {
		BindingSet bindingSet = qresult.next();
	
		Value uriparticella = bindingSet.getValue("particella");
		Value uriUI = bindingSet.getValue("ui");
		Value numero = bindingSet.getValue("numero");
		Value denominatore = bindingSet.getValue("denominatore");
		Value foglio = bindingSet.getValue("foglio");
		Value comunecatastale = bindingSet.getValue("comunecatastale");
		
		
		Particella particella= new Particella();
		
	
		if (uriparticella != null) {
			particella.setUri(uriparticella.stringValue());
		}

		if (uriUI != null) {
			particella.setUi(uriUI.stringValue());
		}
		if (foglio != null) {
			particella.setFoglio(foglio.stringValue());
		}
		if (numero != null) {
			particella.setNumero(numero.stringValue());
		}
		if (denominatore != null) {
			particella.setDenomintore(denominatore.stringValue());
		}
		if (comunecatastale != null) {
			particella.setComuneCatastale(comunecatastale.stringValue());
		}
		
	
	listaParticella.add(particella);
	}	
	qresult.close();
	connection.close();
	} catch (RepositoryException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
	} catch (MalformedQueryException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (QueryEvaluationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		
	}

return listaParticella;
}


	
	
	
	@GET
	@Path("/unitaimmobiliari_su_particella")
	@Produces(MediaType.APPLICATION_JSON)
	public List<UnitaImmobiliare> getUnitaImmobiliariSuParticella(

		@QueryParam("particella") String particella)
		
	{	
			List<UnitaImmobiliare> listaUnitaImmobiliari = new ArrayList<UnitaImmobiliare>();
	Soggetto sog= new Soggetto();
	 String springlesserverURL = "http://localhost:8080/openrdf-sesame";
	 String springlesrepositoryID ="georeporter";

List<BindingSet> tuples = new ArrayList<BindingSet>();	


///Soggetto soggetto =new Soggetto();

Repository		myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
try {
	myRepository.initialize();

	RepositoryConnection connection = myRepository.getConnection();
	

String	 queryString = queryStringPrefix
		
+" select   ?ui ?classe ?piano ?interno ?scala ?categoria ?titolare ?superficie"
+" where { "
+"    ?x a :IdentificativoCatastale. "
+"    ?x :hasParticella "+particella+" . "
+"    ?x :hasUnitaImmobiliare ?ui ."
+"     ?ui :classe ?classe ."
+"     ?ui :piano1 ?piano. "
+"     ?ui :superficie ?superficie. "
+"   OPTIONAL{  ?ui :interno ?interno}. "
+"   OPTIONAL{    ?ui :scala ?scala}. "
+"    OPTIONAL{   ?ui :categoria ?categoria}. "
+"     OPTIONAL{  ?ui :titolare ?titolare}. "

+"	} ";
	
System.out.println(queryString);		
TupleQuery tupleQuery;

int i = 0;

tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL,
		queryString);

TupleQueryResult qresult = tupleQuery.evaluate();


while (qresult.hasNext()) {
	BindingSet bindingSet = qresult.next();

	Value uiuri = bindingSet.getValue("ui");
	Value piano = bindingSet.getValue("piano");
	Value scala = bindingSet.getValue("scala");
	Value interno = bindingSet.getValue("interno");
	Value categoria = bindingSet.getValue("categoria");
	Value titolare = bindingSet.getValue("titolare");
	Value superficie = bindingSet.getValue("superficie");

	
  UnitaImmobiliare ui= new UnitaImmobiliare();
	

	if (uiuri != null) {
		ui.setUri(uiuri.stringValue());
	}

	if (piano != null) {
	ui.setPiano(piano.stringValue());
	}
	
	if (scala != null) {
		ui.setScala((scala.stringValue()));
	}
	if (interno != null) {
		ui.setInterno((interno.stringValue()));
	}
	if (categoria != null) {
		ui.setCategoria((categoria.stringValue()));
	}
	if (titolare != null) {
		ui.setTitolare((titolare.stringValue()));
	}
	if (superficie != null) {
		ui.setSuperficie((superficie.stringValue()));
	}

	listaUnitaImmobiliari.add(ui);
}	
//qresult.close();
//connection.close();
} catch (RepositoryException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	
} catch (MalformedQueryException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (QueryEvaluationException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} finally {
	
}
return listaUnitaImmobiliari;

}


	
	
										 
  
	
	
	@GET
	@Path("/anagraficasoggettoui")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Soggetto> getAnagraficaSoggettoui(@QueryParam("ui") String ui)	{	
							
		Soggetto sog= new Soggetto();
		 String springlesserverURL = "http://localhost:8080/openrdf-sesame";
		 
String springlesrepositoryID ="georeporter";

List<BindingSet> tuples = new ArrayList<BindingSet>();
List<Soggetto> listaSoggetto	=new ArrayList<Soggetto>();	
///Soggetto soggetto =new Soggetto();

Repository		myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
	try {
		myRepository.initialize();
	
		RepositoryConnection connection = myRepository.getConnection();
		





String	 queryString = queryStringPrefix
			
+" select    ?soggetto ?codicefiscale ?nome ?cognome"
+" where { "
+"    ?x a :Titolarita ."
+"   ?x :hasSoggetto ?soggetto . "
+"  ?x :hasIdentificativoCatastale " +ui+ " . "
+"  ?soggetto :codicefiscale ?codicefiscale . "
+"  ?soggetto :nome ?nome . "
+"  ?soggetto :cognome ?cognome  "

	
+" }  ";
	
		
	

System.out.println(queryString);		
TupleQuery tupleQuery;

int i = 0;


	tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL,
			queryString);
	
	TupleQueryResult qresult = tupleQuery.evaluate();


	while (qresult.hasNext()) {
		BindingSet bindingSet = qresult.next();
	
		Value urisoggetto = bindingSet.getValue("soggetto");
		Value codicefiscale = bindingSet.getValue("codicefiscale");
		Value nome = bindingSet.getValue("nome");
		Value cognome = bindingSet.getValue("cognome");
		
		Soggetto soggetto= new Soggetto();
		
	
		if (codicefiscale != null) {
			soggetto.setCodiceFiscale(codicefiscale.stringValue());
		}

		if (urisoggetto != null) {
			soggetto.setUri(urisoggetto.stringValue());
		}
		if (nome != null) {
			soggetto.setNome(nome.stringValue());
		}
		if (cognome != null) {
			soggetto.setCognome(cognome.stringValue());
		}
		
	
	listaSoggetto.add(soggetto);
	}	
	qresult.close();
	connection.close();
	} catch (RepositoryException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
	} catch (MalformedQueryException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (QueryEvaluationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		
	}
		return listaSoggetto;								 
  
	}
	
	
	@GET
	@Path("/utenzeacquaui")
	@Produces(MediaType.APPLICATION_JSON)
	public List<UtenzaAcqua> getUtenzeAcquaUI(
			
			@QueryParam("codcomunecatastale") String codcomunecatastale,
			@QueryParam("particella") String particella,
			@QueryParam("subalterno") String subalterno

			
			)	{	
		
		Soggetto sog= new Soggetto();
		 String springlesserverURL = "http://localhost:8080/openrdf-sesame";
		 
String springlesrepositoryID ="georeporter";

List<BindingSet> tuples = new ArrayList<BindingSet>();	
List<UtenzaAcqua> listaUtenzaAcqua = new ArrayList<UtenzaAcqua>();

///Soggetto soggetto =new Soggetto();

Repository		myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
	try {
		myRepository.initialize();
	
		RepositoryConnection connection = myRepository.getConnection();
		





String	 queryString = queryStringPrefix
			
	
		
+" select    ?acqua ?datainizio ?datafine ?contribuente ?categoriadescrizione"
+" where{ "
+"     ?acqua a :UtenzaAcqua .   "
+"     ?acqua :comunecatastale \""+codcomunecatastale+"\" . "
+"     ?acqua :datainizio ?datainizio. "
+"     ?acqua :categoriadescrizione ?categoriadescrizione. "
+"     ?acqua :contribuente ?contribuente. "
+"     ?acqua :particellaedificabile \""+particella+"\" . "


+"     ?acqua :subalterno \""+subalterno+"\" "
+" }" ;





System.out.println(queryString);		
TupleQuery tupleQuery;

int i = 0;


	tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL,
			queryString);
	
	TupleQueryResult qresult = tupleQuery.evaluate();


	while (qresult.hasNext()) {
		BindingSet bindingSet = qresult.next();
	
		Value acquaURI = bindingSet.getValue("acqua");
		Value datainizio = bindingSet.getValue("datainizio");
		Value datafine = bindingSet.getValue("datafine");
		Value contribuente = bindingSet.getValue("contribuente");
		Value categoriadescrizione = bindingSet.getValue("categoriadescrizione");
		
		UtenzaAcqua utenzaacqua= new UtenzaAcqua();
		
	
		if (acquaURI != null) {
			utenzaacqua.setUri(acquaURI.stringValue());
		}

		if (datainizio != null) {
		utenzaacqua.setDataInizio(datainizio.stringValue());
		}
		if (datafine != null) {
			
		utenzaacqua.setDatafine(datafine.stringValue());
		}
		if (contribuente != null) {
			utenzaacqua.setContribuente((contribuente.stringValue()));
		}
		if (categoriadescrizione != null) {
			utenzaacqua.setCategoriaDescrizione((categoriadescrizione.stringValue()));
		}
	
	listaUtenzaAcqua.add(utenzaacqua);
	}	
	//qresult.close();
	//connection.close();
	} catch (RepositoryException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
	} catch (MalformedQueryException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (QueryEvaluationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		
	}
		return listaUtenzaAcqua;								 
  
	}
	
	
			
	@GET
	@Path("/iciimuui")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TributiICI> getICI_IMU_UI(
			
			@QueryParam("codcomunecatastale") String codcomunecatastale,
			@QueryParam("particella") String particella,
			@QueryParam("subalterno") String subalterno

			
			)	{	
		
		Soggetto sog= new Soggetto();
		 String springlesserverURL = "http://localhost:8080/openrdf-sesame";
		 
String springlesrepositoryID ="georeporter";

List<BindingSet> tuples = new ArrayList<BindingSet>();	
List<TributiICI > listaTributiICI = new ArrayList<TributiICI>();

///Soggetto soggetto =new Soggetto();

Repository		myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
	try {
		myRepository.initialize();
	
		RepositoryConnection connection = myRepository.getConnection();
		





String	 queryString = queryStringPrefix
			
	
		
+" select    ?ici  ?contribuente ?indirizzo ?categoriadescrizione"
+" where{ "
+"     ?ici a :ICI_IMU .   "
+"     ?ici :codcomune \""+codcomunecatastale+"\" . "
+"     ?ici :indirizzo ?indirizzo. "
+"     ?ici :categoriadescrizione ?categoriadescrizione. "
+"     ?ici :contribuente ?contribuente. "
+"     ?ici :particellaedificabile \""+particella+"\" . "
+"     ?ici :subalterno \""+subalterno+"\" "
+" }" ;





System.out.println(queryString);		
TupleQuery tupleQuery;

int i = 0;


	tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL,
			queryString);
	
	TupleQueryResult qresult = tupleQuery.evaluate();


	while (qresult.hasNext()) {
		BindingSet bindingSet = qresult.next();
	
		Value iciURI = bindingSet.getValue("ici");
		Value indirizzo = bindingSet.getValue("indirizzo");
	//	Value datafine = bindingSet.getValue("datafine");
		Value contribuente = bindingSet.getValue("contribuente");
		Value categoriadescrizione = bindingSet.getValue("categoriadescrizione");
		
		TributiICI tributiici= new TributiICI();
		
	
		if (iciURI != null) {
			tributiici.setUri(iciURI.stringValue());
		}

		if (indirizzo != null) {
		tributiici.setIndirizzo(indirizzo.stringValue());
		}
		
		if (contribuente != null) {
			tributiici.setContribuente((contribuente.stringValue()));
		}
		if (categoriadescrizione != null) {
			tributiici.setCategoriadescrizione((categoriadescrizione.stringValue()));
		}
	
		listaTributiICI.add(tributiici);
	}	
	//qresult.close();
	//connection.close();
	} catch (RepositoryException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		
	} catch (MalformedQueryException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (QueryEvaluationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
		
	}
		return listaTributiICI;								 
  
	}
	
	
			
										 
  

	
	@GET
	@Path("/utenzerifiutiintestatarioui")
	@Produces(MediaType.APPLICATION_JSON)
	public List<UtenzaRifiuti> getUtenzeRifiuti()	{	
							
		List<UtenzaRifiuti> listaUtenzaRifiuti = new ArrayList<UtenzaRifiuti>();
	
		return listaUtenzaRifiuti;
										 
  
	}
	@GET
	@Path("/tributiiciintestatarioui")
	@Produces(MediaType.APPLICATION_JSON)
	public  List<TributiICI> getTributi()	{	
							
   List<TributiICI> listaTributiICI = new ArrayList<TributiICI>();
	
		return listaTributiICI;
										 
  
	}
	@GET
	@Path("/fornituregasintestatarioui")
	@Produces(MediaType.APPLICATION_JSON)
	public  List<FornituraGas> getFornitureGasIntestatarioui()	{	
							
    List<FornituraGas> listaFornituraGas= new ArrayList<FornituraGas>();
	
		return listaFornituraGas;
		
										 
  
	}
	@GET
	@Path("/fornitureenergiaintestatarioui")
	@Produces(MediaType.APPLICATION_JSON)
	public  List<FornituraEnergia> getFornitureEnergiaIntestatarioui()	{	
							
    List<FornituraEnergia> listaFornituraEnergia= new ArrayList<FornituraEnergia>();
	
		return listaFornituraEnergia;
		
										 
  
	}
	
	@GET
	@Path("/forniturelocazioniintestatarioui")
	@Produces(MediaType.APPLICATION_JSON)
	public  List<FornituraLocazioni> getFornitureLocazioniIntestatarioui()	{	
							
    List<FornituraLocazioni> listaFornituraLocazioni= new ArrayList<FornituraLocazioni>();
	
		return listaFornituraLocazioni;
		
										 
  
	}
}




