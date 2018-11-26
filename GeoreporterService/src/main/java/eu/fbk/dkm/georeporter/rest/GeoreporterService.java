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
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.IOUtils;
import org.apache.naming.factory.ResourceFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.openrdf.OpenRDFException;
import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.model.vocabulary.XMLSchema;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.Update;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.json.JSONWithPadding;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataParam;
import com.sun.jersey.multipart.MultiPart;

import eu.fbk.dkm.georeporter.ExceptionHandlingRelated.CustomerDataNotFoundException;

import eu.fbk.dkm.georeporter.interfaces.Attributo;
import eu.fbk.dkm.georeporter.interfaces.AttributoEntita;
import eu.fbk.dkm.georeporter.interfaces.Entita;
import eu.fbk.dkm.georeporter.interfaces.GerarchiaClasse;
import eu.fbk.dkm.georeporter.interfaces.Relazione;
import eu.fbk.dkm.georeporter.interfaces.RelazioneEntita;
import eu.fbk.dkm.georeporter.interfaces.RigaTabella;
import eu.fbk.dkm.georeporter.pojos.Esiste;
import eu.fbk.dkm.georeporter.pojos.FornituraEnergia;
import eu.fbk.dkm.georeporter.pojos.FornituraGas;
import eu.fbk.dkm.georeporter.pojos.FornituraLocazioni;
import eu.fbk.dkm.georeporter.pojos.IDCatastale;
import eu.fbk.dkm.georeporter.pojos.Indirizzo;
import eu.fbk.dkm.georeporter.pojos.Locazione;
import eu.fbk.dkm.georeporter.pojos.MappingTabella;
import eu.fbk.dkm.georeporter.pojos.Particella;
import eu.fbk.dkm.georeporter.pojos.QueryJson;
import eu.fbk.dkm.georeporter.pojos.QueryResult;
import eu.fbk.dkm.georeporter.pojos.QueryResultItem;
import eu.fbk.dkm.georeporter.pojos.QueryResultRow;
import eu.fbk.dkm.georeporter.pojos.ReportStatistiche;
import eu.fbk.dkm.georeporter.pojos.Soggetto;
import eu.fbk.dkm.georeporter.pojos.Statistiche;
import eu.fbk.dkm.georeporter.pojos.Subcategoria;
import eu.fbk.dkm.georeporter.pojos.TipoConCoordinate;
import eu.fbk.dkm.georeporter.pojos.TributiICI;
import eu.fbk.dkm.georeporter.pojos.UIConCoordinate;
import eu.fbk.dkm.georeporter.pojos.UnitaImmobiliare;
import eu.fbk.dkm.georeporter.pojos.UtenzaAcqua;
import eu.fbk.dkm.georeporter.pojos.UtenzaRifiuti;
import eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert.ImportGeoreporter;
import eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert.ReportManager;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.Report;
import eu.fbk.dkm.georeporter.tn.wrappers.pojo.ReportValore;
//import eu.fbk.dkm.georeporter.tn.wrappers.mappinginsert.ImportGeoreporter;
import eu.fbk.dkm.georeporter.utils.Costanti;
import eu.fbk.dkm.georeporter.utils.ProgramDirectoryUtilities;


/**
 * GeoreporterService e' un servizio REST che espone i metodi per gestire la
 * knowledge base Fornisce inoltre una serie di metodi per recuperare le
 * informazioni
 * 
 * @author Gaetano Calabrese,
 *
 */
@Path("/rest")
public class GeoreporterService {

	String springlesserverURL = "http://localhost:8080/openrdf-sesame";
	String springlesrepositoryID = "georeporter";
	String uricontesto = "http://dkm.fbk.eu/ckr/meta#global";

	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String queryStringPrefix = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
			+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n" + "PREFIX owl:<http://www.w3.org/2002/07/owl#>\n"
			+ "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n" + "PREFIX ckr:<http://dkm.fbk.eu/ckr/meta#> \n"
			+ "PREFIX meta:<http://dkm.fbk.eu/ckr/meta#>\n" + "PREFIX  :<http://dkm.fbk.eu/georeporter#>\n";

	public GeoreporterService() {
		super();
		springlesserverURL=Costanti.springlesserverURL;
		springlesrepositoryID=Costanti.springlesrepositoryID;
	}

	/**
	 * Method to test Springles Service - url :
	 * http://localhost:8080/GeoreporterService/rest/hallo
	 * 
	 * @return message to a web page
	 */
	@GET
	@Path("/hallo")
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "Hello!It's SpringlesService";
	}

	/**
	 * Metodo per inserire i dati relativi ad una fornitura di Gas url :
	 * http://localhost:8080/GeoreporterService/rest/forniruregas
	 * 
	 * @return message to a web page
	 */
	@POST
	@Path("/fornituregas")
	@Produces(MediaType.APPLICATION_JSON)
	public void setFornitureGas(

			@QueryParam("ammontarefatturato") String ammontarefatturato,
			@QueryParam("consumofatturato") String consumofatturato, @QueryParam("tipoutenzagas") String tipoutenzagas,
			@QueryParam("idsiatelgas") String idsiatelgas,
			@QueryParam("idsiatelgasdettaglio") String idsiatelgasdettaglio,
			@QueryParam("Denominazione") String denominazione, @QueryParam("CodComune") String codcomune,
			@QueryParam("AnnoRiferimento") String annoriferimento,
			@QueryParam("CodFiscaleTitolareUtenza") String CodFiscaleTitolareUtenza,
			@QueryParam("TipoPersona") String TipoPersona, @QueryParam("DatiAnagrafici	") String DatiAnagrafici,
			@QueryParam("TipoUtenzaEnergia	") String TipoUtenzaEnergia,
			@QueryParam("IndirizzoUtenza	") String IndirizzoUtenza,
			@QueryParam("SpesaConsumo	") String SpesaConsumo, @QueryParam("KWhFatturato	") String KWhFatturato,
			@QueryParam("NumeroMesiFatturazione	") String NumeroMesiFatturazione,
			@QueryParam("CAPUtenza") String CAPUtenza) {

		// if ((hasTerm==null)||hasTerm.equals("")){

		ResponseBuilderImpl builder = new ResponseBuilderImpl();
		builder.status(Response.Status.BAD_REQUEST);
		builder.entity("ERROR! term parameter missing or null");
		Response response = builder.build();
		throw new WebApplicationException(response);

		// }

	}

	/**
	 * Metodo per inserire i dati relativi ad una fornitura di Energia url :
	 * http://localhost:8080/GeoreporterService/rest/fornirureenergia
	 * 
	 * @return message to a web page
	 */
	@POST
	@Path("/fornitureenergia")
	@Produces(MediaType.APPLICATION_JSON)
	public void setFornitureEnergia(@QueryParam("idsiatelenergisdettaglio") String idsiatelenergisdettaglio,
			@QueryParam("ammontarefatturato") String ammontarefatturato,
			@QueryParam("consumofatturato") String consumofatturato,
			@QueryParam("tipoutenzaenergia") String tipoutenzaenergia,
			@QueryParam("idsiatelenergia") String idsiatelenergia, @QueryParam("Denominazione") String denominazione,
			@QueryParam("CodComune") String codcomune, @QueryParam("AnnoRiferimento") String annoriferimento,
			@QueryParam("CodFiscaleTitolareUtenza") String CodFiscaleTitolareUtenza,
			@QueryParam("TipoPersona") String TipoPersona, @QueryParam("DatiAnagrafici	") String DatiAnagrafici,
			@QueryParam("TipoUtenzaEnergia	") String TipoUtenzaEnergia,
			@QueryParam("IndirizzoUtenza	") String IndirizzoUtenza,
			@QueryParam("SpesaConsumo	") String SpesaConsumo, @QueryParam("KWhFatturato	") String KWhFatturato,
			@QueryParam("NumeroMesiFatturazione	") String NumeroMesiFatturazione,
			@QueryParam("IdSiatelEnergia") String IdSiatelEnergia, @QueryParam("CAPUtenza") String CAPUtenza

	) {

		// if ((hasTerm==null)||hasTerm.equals("")){

		ResponseBuilderImpl builder = new ResponseBuilderImpl();
		builder.status(Response.Status.BAD_REQUEST);
		builder.entity("ERROR! term parameter missing or null");
		Response response = builder.build();
		throw new WebApplicationException(response);

		// }

	}
	
	
	@GET
	@Path("/logfile")
	@Produces(MediaType.APPLICATION_JSON)
	public String getLogFile(
			@QueryParam("logfilename") String logfilename
			) {
	String contents="";
	try {
		contents = new String(Files.readAllBytes(Paths.get(logfilename)));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return contents;
	}
	
	
	
	@GET
	@Path("/reportfile")
	@Produces(MediaType.APPLICATION_JSON)
	public String getReportFile(
			@QueryParam("reportfilename") String reportfilename
			) {
	
	ReportManager report=new ReportManager(reportfilename);
	return report.getReportFile(reportfilename);
	
	
	}
	
	
	
	
	
	
	@GET
	@Path("/particelle_old")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Particella> getParticelle() {

		// @QueryParam("springlesserverURL") String springlesserverURL,
		// @QueryParam("springlesrepositoryID") String springlesrepositoryID
		// ) {

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<Particella> listaParticella = new ArrayList<Particella>();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ "SELECT DISTINCT ?particella ?ui ?comunecatastale ?numero  ?foglio ?denominatore " + "WHERE { "
					+ "    ?x a :IdentificativoCatastale . " + "    ?x :hasParticella ?particella . "
					+ "    ?x :hasUnitaImmobiliare ?ui ."

					+ "    ?particella :numero ?numero . "
					+ "    ?particella :codiceComuneCatastale ?comunecatastale . "
					+ " OPTIONAL{   ?particella :foglio ?foglio  }. "
					+ " OPTIONAL{   ?particella :denominatore ?denominatore  } . "

					+ "}  ";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value uriparticella = bindingSet.getValue("particella");
				Value uriUI = bindingSet.getValue("ui");
				Value numero = bindingSet.getValue("numero");
				Value denominatore = bindingSet.getValue("denominatore");
				Value foglio = bindingSet.getValue("foglio");
				Value comunecatastale = bindingSet.getValue("comunecatastale");

				Particella particella = new Particella();

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
					particella.setDenominatore(denominatore.stringValue());
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


	
	/**
	 * Metodo che restituisce la lista di tutte le particelle
	 * 
	 * <p>
	 * SELECT DISTINCT ?particella ?ui ?comunecatastale ?numero  ?foglio ?denominatore 
	 *  "WHERE { 
				    ?x a :IdentificativoCatastale .
				    ?x :hasParticella ?particella . 
				    ?x :hasUnitaImmobiliare ?ui .
                    ?x :numero ?numero . 
				 OPTIONAL{   ?x :comuneCatastale ?comunecatastale }. 
				 OPTIONAL{   ?x :foglio ?foglio  }. 
				 OPTIONAL{   ?x :denominatore ?denominatore  } . 

				}  

	 * </p>
	 * url : http://localhost:8080/GeoreporterService/rest/particelle
	 * @param void
	 * @return List<Particella>
	 */
	@GET
	@Path("/particelle")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Particella> getParticelleNEW() {

		// @QueryParam("springlesserverURL") String springlesserverURL,
		// @QueryParam("springlesrepositoryID") String springlesrepositoryID
		// ) {

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<Particella> listaParticella = new ArrayList<Particella>();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ "SELECT DISTINCT ?particella ?ui ?comunecatastale ?numero  ?foglio ?denominatore " + "WHERE { "
					+ "    ?x a :IdentificativoCatastale . " + "    ?x :hasParticella ?particella . "
					+ "    ?x :hasUnitaImmobiliare ?ui ."

					+ "    ?x :numero ?numero . "
					+ " OPTIONAL{   ?x :comuneCatastale ?comunecatastale }. "
					+ " OPTIONAL{   ?x :foglio ?foglio  }. "
					+ " OPTIONAL{   ?x :denominatore ?denominatore  } . "

					+ "}  ";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value uriparticella = bindingSet.getValue("particella");
				Value uriUI = bindingSet.getValue("ui");
				Value numero = bindingSet.getValue("numero");
				Value denominatore = bindingSet.getValue("denominatore");
				Value foglio = bindingSet.getValue("foglio");
				Value comunecatastale = bindingSet.getValue("comunecatastale");

				Particella particella = new Particella();

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
					particella.setDenominatore(denominatore.stringValue());
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
	@Path("/unitaimmobiliari_su_particella_old")
	@Produces(MediaType.APPLICATION_JSON)
	// @Produces({"application/javascript"})

	 public JSONWithPadding getUnitaImmobiliariSuParticella(
	//public List<UnitaImmobiliare> getUnitaImmobiliariSuParticella(

			@QueryParam("particella") String particella, @QueryParam("callback") String callback)

	{
		List<UnitaImmobiliare> listaUnitaImmobiliari = new ArrayList<UnitaImmobiliare>();
		Soggetto sog = new Soggetto();
		// String springlesserverURL = "http://localhost:8080/openrdf-sesame";
		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();

		/// Soggetto soggetto =new Soggetto();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ " select   ?ui ?classe ?piano ?interno ?scala ?categoria ?titolare ?superficie" + " where { "
					+ "    ?x a :IdentificativoCatastale. " 
					+ "    ?x :hasParticella :" + particella + " . "
					+ "    ?x :hasUnitaImmobiliare ?ui ." 
					+ "     OPTIONAL{ ?ui :classe ?classe }."
					+ "     OPTIONAL{ ?ui :piano1 ?piano}. " 
					+ "     OPTIONAL{  ?ui :superficie ?superficie}. "
					+ "     OPTIONAL{  ?ui :interno ?interno}. " 
					+ "     OPTIONAL{    ?ui :scala ?scala}. "
					+ "     OPTIONAL{   ?ui :categoria ?categoria}. " 
					+ "     OPTIONAL{  ?ui :titolare ?titolare}. "

					+ "	} ";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

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

				UnitaImmobiliare ui = new UnitaImmobiliare();

				if (uiuri != null) {
					// ui.setUri(uiuri.stringValue());
					// ui.setUi(uiuri.stringValue().substring(u));
					ui.setUi(uiuri.stringValue().substring(uiuri.stringValue().lastIndexOf('#') + 1));
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
			// qresult.close();
			// connection.close();
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
		return new JSONWithPadding(new
	 GenericEntity<List<UnitaImmobiliare>>(listaUnitaImmobiliari){},
		 callback);

		//return listaUnitaImmobiliari;
	}

	
	
	
	
	
	
	
	
	/**
	 * Metodo che restituisce tutte le Unità Immobiliari presenti su una particella
	 * 
	 * <p>
	 * select   ?ui ?classe ?piano ?interno ?scala ?categoria ?superficie ?renditaeuro ?valoreimis
	 *				 where { 
	 *				    ?x a :IdentificativoCatastale. 
	 *				    ?x :hasParticella :" + particella + " . "
	 *				    ?x :hasUnitaImmobiliare ?ui .
	 *				    OPTIONAL{ ?ui :classe ?classe }.
	 *				    OPTIONAL{ ?ui :piano ?piano}. 
	 *				    OPTIONAL{ ?ui :superficie ?superficie}. 
	 *				    OPTIONAL{ ?ui :interno ?interno}. 
	 *				    OPTIONAL{ ?ui :scala ?scala}. 
	 *				    OPTIONAL{ ?ui :categoria ?categoria}. 
	 *				    OPTIONAL{ ?ui :renditaEuro ?renditaeuro}.  
	 *				    OPTIONAL{ ?ui :valoreIMIS ?valoreimis}. 
	 *				} 

	 * </p>
	 * url : http://localhost:8080/GeoreporterService/rest/unitaimmobiliari_su_particella
	 * @param particella=IDParticella
	 * @return List<UnitaImmobiliare>
	 */
	@GET
	@Path("/unitaimmobiliari_su_particella2")
	@Produces(MediaType.APPLICATION_JSON)
	// @Produces({"application/javascript"})

	// public JSONWithPadding getUnitaImmobiliariSuParticella(
	public List<UnitaImmobiliare> getUnitaImmobiliariSuParticella2(

			@QueryParam("particella") String particella, @QueryParam("callback") String callback)

	{
		List<UnitaImmobiliare> listaUnitaImmobiliari = new ArrayList<UnitaImmobiliare>();
		Soggetto sog = new Soggetto();
		// String springlesserverURL = "http://localhost:8080/openrdf-sesame";
		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();

		/// Soggetto soggetto =new Soggetto();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ " select   ?ui ?classe ?piano ?interno ?scala ?categoria ?superficie ?renditaeuro ?valoreimis" 
					+ " where { "
					+ "    ?x a :IdentificativoCatastale. " 
					+ "    ?x :hasParticella :" + particella + " . "
					+ "    ?x :hasUnitaImmobiliare ?ui ." 
					+ "    OPTIONAL{ ?ui :classe ?classe }."
					+ "    OPTIONAL{ ?ui :piano ?piano}. " 
					+ "    OPTIONAL{  ?ui :superficie ?superficie}. "
					+ "    OPTIONAL{  ?ui :interno ?interno}. " 
					+ "    OPTIONAL{   ?ui :scala ?scala}. "
					+ "    OPTIONAL{   ?ui :categoria ?categoria}. "
					+ "    OPTIONAL{   ?ui :renditaEuro ?renditaeuro}. " 
					+ "    OPTIONAL{   ?ui :valoreIMIS ?valoreimis}. " + "     "
				+ "	} ";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value uiuri = bindingSet.getValue("ui");
				Value piano = bindingSet.getValue("piano");
				Value scala = bindingSet.getValue("scala");
				Value interno = bindingSet.getValue("interno");
				Value categoria = bindingSet.getValue("categoria");
			//	Value titolare = bindingSet.getValue("titolare");
				Value superficie = bindingSet.getValue("superficie");
				Value renditaeuro = bindingSet.getValue("renditaeuro");
				Value valoreimis = bindingSet.getValue("valoreimis");
				
				UnitaImmobiliare ui = new UnitaImmobiliare();

				if (uiuri != null) {
					// ui.setUri(uiuri.stringValue());
					// ui.setUi(uiuri.stringValue().substring(u));
					ui.setUi(uiuri.stringValue().substring(uiuri.stringValue().lastIndexOf('#') + 1));
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
				
				if (superficie != null) {
					ui.setSuperficie((superficie.stringValue()));
				}

				if (renditaeuro != null) {
					ui.setRenditaEuro((renditaeuro.stringValue()));
				}
				

				if (valoreimis != null) {
					ui.setValoreIMIS((valoreimis.stringValue()));
				}
				
				
				
				
				
				
				listaUnitaImmobiliari.add(ui);
			}
			// qresult.close();
			// connection.close();
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
		// return new JSONWithPadding(new
		// GenericEntity<List<UnitaImmobiliare>>(listaUnitaImmobiliari){},
		// callback);

		return listaUnitaImmobiliari;
	}

	
	
	
	
	
	/**
	 * Metodo che restituisce tutte le Unità Immobiliari presenti su una particella
	 * 
	 * <p>
	 * select   ?ui ?classe ?piano ?interno ?scala ?categoria ?superficie ?renditaeuro ?valoreimis
	 *				 where { 
	 *				    ?x a :IdentificativoCatastale. 
	 *				    ?x :hasParticella :" + particella + " . "
	 *				    ?x :hasUnitaImmobiliare ?ui .
	 *				    OPTIONAL{ ?ui :classe ?classe }.
	 *				    OPTIONAL{ ?ui :piano ?piano}. 
	 *				    OPTIONAL{ ?ui :superficie ?superficie}. 
	 *				    OPTIONAL{ ?ui :interno ?interno}. 
	 *				    OPTIONAL{ ?ui :scala ?scala}. 
	 *				    OPTIONAL{ ?ui :categoria ?categoria}. 
	 *				    OPTIONAL{ ?ui :renditaEuro ?renditaeuro}.  
	 *				    OPTIONAL{ ?ui :valoreIMIS ?valoreimis}. 
	 *				} 

	 * </p>
	 * url : http://localhost:8080/GeoreporterService/rest/unitaimmobiliari_su_particella
	 * @param particella=IDParticella
	 * @return List<UnitaImmobiliare>
	 */
	@GET
	@Path("/unitaimmobiliari_su_particella")
	@Produces(MediaType.APPLICATION_JSON)
	// @Produces({"application/javascript"})

	// public JSONWithPadding getUnitaImmobiliariSuParticella(
	public List<UnitaImmobiliare> getUnitaImmobiliariSuParticella3(

			@QueryParam("particella") String particella, @QueryParam("callback") String callback)

	{
		List<UnitaImmobiliare> listaUnitaImmobiliari = new ArrayList<UnitaImmobiliare>();
		Soggetto sog = new Soggetto();
		// String springlesserverURL = "http://localhost:8080/openrdf-sesame";
		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();

		/// Soggetto soggetto =new Soggetto();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ " select   ?ui ?numero ?denominatore ?subalterno ?classe ?piano ?interno ?scala ?categoria ?superficie ?renditaeuro ?valoreimis" 
					+ " where { "
					+ "    ?x a :IdentificativoCatastale. " 
					+ "    ?x :hasParticella :" + particella + " . "
					+ "    ?x :hasUnitaImmobiliare ?ui ." 
					+ "    ?x :numero ?numero ."
					+ "    OPTIONAL{ ?x :denominatore ?denominatore }."
					+ "    OPTIONAL{ ?x :subalterno ?subalterno }."
					+ "    OPTIONAL{ ?ui :classe ?classe }."
					+ "    OPTIONAL{ ?ui :piano ?piano}. " 
					+ "    OPTIONAL{  ?ui :superficie ?superficie}. "
					+ "    OPTIONAL{  ?ui :interno ?interno}. " 
					+ "    OPTIONAL{   ?ui :scala ?scala}. "
					+ "    OPTIONAL{   ?ui :categoria ?categoria}. "
					+ "    OPTIONAL{   ?ui :renditaEuro ?renditaeuro}. " 
					+ "    OPTIONAL{   ?ui :valoreIMIS ?valoreimis}. " + "     "
				+ "	} ";

			System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value uiuri = bindingSet.getValue("ui");
				
				Value numero = bindingSet.getValue("numero");
				Value denominatore = bindingSet.getValue("denominatore");
				Value subalterno = bindingSet.getValue("subalterno");
				
				
				
				Value piano = bindingSet.getValue("piano");
				Value scala = bindingSet.getValue("scala");
				Value interno = bindingSet.getValue("interno");
				Value categoria = bindingSet.getValue("categoria");
			//	Value titolare = bindingSet.getValue("titolare");
				Value superficie = bindingSet.getValue("superficie");
				Value renditaeuro = bindingSet.getValue("renditaeuro");
				Value valoreimis = bindingSet.getValue("valoreimis");
				
				UnitaImmobiliare ui = new UnitaImmobiliare();

				if (uiuri != null) {
					// ui.setUri(uiuri.stringValue());
					// ui.setUi(uiuri.stringValue().substring(u));
					ui.setUi(uiuri.stringValue().substring(uiuri.stringValue().lastIndexOf('#') + 1));
				}

				
				if (numero != null) {
					ui.setNumero(numero.stringValue());
				}
				if (denominatore != null) {
					ui.setDenominatore(denominatore.stringValue());
				}
				if (subalterno != null) {
					ui.setSubalterno(subalterno.stringValue());
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
				
				if (superficie != null) {
					ui.setSuperficie((superficie.stringValue()));
				}

				if (renditaeuro != null) {
					ui.setRenditaEuro((renditaeuro.stringValue()));
				}
				

				if (valoreimis != null) {
					ui.setValoreIMIS((valoreimis.stringValue()));
				}
						
				
				listaUnitaImmobiliari.add(ui);
			}
			// qresult.close();
			// connection.close();
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
		// return new JSONWithPadding(new
		// GenericEntity<List<UnitaImmobiliare>>(listaUnitaImmobiliari){},
		// callback);

		return listaUnitaImmobiliari;
	}
	

	
	
	
	
	
	
	
	
	
	
	
	@GET
	@Path("/anagraficasoggettoui_old")
	// @Produces(MediaType.APPLICATION_JSON)
	@Produces({ "application/javascript" })
	// public List<Soggetto> getAnagraficaSoggettoui(@QueryParam("ui") String ui) {
	public JSONWithPadding getAnagraficaSoggettoui(@QueryParam("callback") String callback,
			@QueryParam("ui") String ui) {

		Soggetto sog = new Soggetto();
		// String springlesserverURL = "http://localhost:8080/openrdf-sesame";

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<Soggetto> listaSoggetto = new ArrayList<Soggetto>();
		/// Soggetto soggetto =new Soggetto();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ " select    ?soggetto ?tiposoggetto ?codicefiscale ?nome ?cognome ?denominazioneita "
					+ " where { " + "    ?x a :Titolarita ." + "   ?x :hasSoggetto ?soggetto . "
					+ "  ?x :hasIdentificativoCatastale :" + ui + " . " + " ?soggetto :tiposoggetto ?tiposoggetto . "
					+ " OPTIONAL{ ?soggetto :codicefiscale ?codicefiscale } . "
					+ " OPTIONAL{ ?soggetto :denominazioneita ?denominazioneita } . "
					+ " OPTIONAL{ ?soggetto :nome ?nome }. " + " OPTIONAL{ ?soggetto :cognome ?cognome}  "

					+ " }  ";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value urisoggetto = bindingSet.getValue("soggetto");
				Value codicefiscale = bindingSet.getValue("codicefiscale");
				Value nome = bindingSet.getValue("nome");
				Value cognome = bindingSet.getValue("cognome");

				Value tiposoggetto = bindingSet.getValue("tiposoggetto");
				Value denominazioneita = bindingSet.getValue("denominazioneita");

				Soggetto soggetto = new Soggetto();

				if (codicefiscale != null) {
					soggetto.setCodiceFiscale(codicefiscale.stringValue());
				}

				if (tiposoggetto != null) {
					soggetto.setTiposoggetto(tiposoggetto.stringValue());
				}

				if (denominazioneita != null) {
					soggetto.setDenominazioneita(denominazioneita.stringValue());
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
		// return listaSoggetto;
		return new JSONWithPadding(new GenericEntity<List<Soggetto>>(listaSoggetto) {
		}, callback);

	}

	
	/**
	 * Metodo che restituisce l’anagrafica dei titolari di un’unità immobiliare in formato JSON
	 * 
	 * <p>
	 *	 select  distinct  ?soggetto  ?codicefiscale ?nome ?cognome ?denominazione ?datanascita ?sesso ?luogonascita
	 *				 where {
	 *				  ?x a :Titolarita .
	 *				  ?x :hasSoggetto ?soggetto . 
	 *				  ?x :hasIdentificativoCatastale ?ic . 
	 *				 ?ic :hasUnitaImmobiliare :"+ui+" .
	 *				 OPTIONAL{ ?soggetto :codiceFiscale ?codicefiscale } . 
	 *				 OPTIONAL{ ?soggetto :denominazione ?denominazione } . 
	 *				 OPTIONAL{ ?soggetto :nome ?nome }. 
	 *				 OPTIONAL{ ?soggetto :cognome ?cognome}  
	 *				 OPTIONAL{ ?soggetto :dataNascita ?datanascita}  
	 *				 OPTIONAL{ ?soggetto :sesso ?sesso}  
	 *				 OPTIONAL{ ?soggetto :luogoNascita ?luogonascita}  
     *
	 *				 }  
	 * </p>
	 * url : http://localhost:8080/GeoreporterService/rest/anagraficasoggettoui
	 * @param ui=IDUnitaImmobiliare
	 * @return List<Soggetto>
	 */
	@GET
	@Path("/anagraficasoggettoui")
	// @Produces(MediaType.APPLICATION_JSON)
	@Produces({ "application/javascript" })
	// public List<Soggetto> getAnagraficaSoggettoui(@QueryParam("ui") String ui) {
	public JSONWithPadding getAnagraficaSoggettoui_new(@QueryParam("callback") String callback,
			@QueryParam("ui") String ui) {

	//	Soggetto sog = new Soggetto();
		// String springlesserverURL = "http://localhost:8080/openrdf-sesame";

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<Soggetto> listaSoggetto = new ArrayList<Soggetto>();
		/// Soggetto soggetto =new Soggetto();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ " select  distinct  ?soggetto  ?codicefiscale ?nome ?cognome ?denominazione ?datanascita ?sesso ?luogonascita"
					+ " where { " 
					+ "  ?x a :Titolarita ."
					+ "  ?x :hasSoggetto ?soggetto . "
					+ "  ?x :hasIdentificativoCatastale ?ic . "
					+" ?ic :hasUnitaImmobiliare :"+ui+" ."
					+ " OPTIONAL{ ?soggetto :codiceFiscale ?codicefiscale } . "
					+ " OPTIONAL{ ?soggetto :denominazione ?denominazione } . "
					+ " OPTIONAL{ ?soggetto :nome ?nome }. " 
					+ " OPTIONAL{ ?soggetto :cognome ?cognome}  "
					+ " OPTIONAL{ ?soggetto :dataNascita ?datanascita}  "
					+ " OPTIONAL{ ?soggetto :sesso ?sesso}  "
					+ " OPTIONAL{ ?soggetto :luogoNascita ?luogonascita}  "

					+ " }  ";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value urisoggetto = bindingSet.getValue("soggetto");
				Value codicefiscale = bindingSet.getValue("codicefiscale");
				Value nome = bindingSet.getValue("nome");
				Value cognome = bindingSet.getValue("cognome");

				Value tiposoggetto = bindingSet.getValue("tiposoggetto");
				Value denominazioneita = bindingSet.getValue("denominazione");
				Value datanascita = bindingSet.getValue("datanascita");
				Value sesso = bindingSet.getValue("sesso");
				Value luogonascita = bindingSet.getValue("luogonascita");
	

				Soggetto soggetto = new Soggetto();

				if (codicefiscale != null) {
					soggetto.setCodiceFiscale(codicefiscale.stringValue());
				}

				if (tiposoggetto != null) {
					soggetto.setTiposoggetto(tiposoggetto.stringValue());
				}

				if (denominazioneita != null) {
					soggetto.setDenominazioneita(denominazioneita.stringValue());
				}

				if (urisoggetto != null) {
			//		soggetto.setUri(urisoggetto.stringValue());
				}
				if (nome != null) {
					soggetto.setNome(nome.stringValue());
				}
				if (cognome != null) {
					soggetto.setCognome(cognome.stringValue());
				}
				if (datanascita != null) {
				//	System.out.println(datanascita.stringValue());
					soggetto.setDataNascita(datanascita.stringValue());
				}
				if (sesso != null) {
					soggetto.setSesso(sesso.stringValue());
				}
				if (urisoggetto != null) {
					//		soggetto.setUri(urisoggetto.stringValue());
					listaSoggetto.add(soggetto);
						}
			
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
		// return listaSoggetto;
		return new JSONWithPadding(new GenericEntity<List<Soggetto>>(listaSoggetto) {
		}, callback);

	}
	
	
	
	
	
	
	
	
	
	
	@GET
	@Path("/utenzeacquaui_old")
	@Produces(MediaType.APPLICATION_JSON)
	public List<UtenzaAcqua> getUtenzeAcquaUI_old(

			@QueryParam("codcomunecatastale") String codcomunecatastale, @QueryParam("particella") String particella,
			@QueryParam("subalterno") String subalterno

	) {

		Soggetto sog = new Soggetto();
		// String springlesserverURL = "http://localhost:8080/openrdf-sesame";

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<UtenzaAcqua> listaUtenzaAcqua = new ArrayList<UtenzaAcqua>();

		/// Soggetto soggetto =new Soggetto();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					/*
					 * +" select    ?acqua ?datainizio ?datafine ?contribuente ?categoriadescrizione"
					 * +" where{ " +"     ?acqua a :UtenzaAcqua .   "
					 * +"     ?acqua :comunecatastale \""+codcomunecatastale+"\" . "
					 * +"     ?acqua :datainizio ?datainizio. "
					 * +"     ?acqua :categoriadescrizione ?categoriadescrizione. "
					 * +"     ?acqua :contribuente ?contribuente. "
					 * +"     ?acqua :particellaedificabile \""+particella+"\" . "
					 * 
					 * 
					 * +"     ?acqua :subalterno \""+subalterno+"\" " +" }" ;
					 * 
					 */

					+ " select    ?acqua ?datainizio ?datafine ?contribuente ?categoriadescrizione" + " where{ "
					+ "     ?acqua a :UtenzaAcqua .   " + "     ?acqua :comunecatastale \"" + codcomunecatastale
					+ "\" . " + "     ?acqua :datainizio ?datainizio. "
					+ "     ?acqua :categoriadescrizione ?categoriadescrizione. "
					+ "     ?acqua :contribuente ?contribuente. " + "     ?acqua :particellaedificabile \"" + particella
					+ "\" . "

					+ "     ?acqua :subalterno \"" + subalterno + "\" " + " }";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value acquaURI = bindingSet.getValue("acqua");
				Value datainizio = bindingSet.getValue("datainizio");
				Value datafine = bindingSet.getValue("datafine");
				Value contribuente = bindingSet.getValue("contribuente");
				Value categoriadescrizione = bindingSet.getValue("categoriadescrizione");

				UtenzaAcqua utenzaacqua = new UtenzaAcqua();

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
			// qresult.close();
			// connection.close();
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
	@Path("/utenzeacquaui_old")
	@Produces({ "application/javascript" })
	// @Produces(MediaType.APPLICATION_JSON)

	// public List<UtenzaAcqua> getUtenzeAcquaUI(
	public JSONWithPadding getUtenzeAcquaUI(@QueryParam("callback") String callback, @QueryParam("codui") String codUI
	// @QueryParam("particella") String particella,
	// @QueryParam("subalterno") String subalterno

	) {

		Soggetto sog = new Soggetto();
		// String springlesserverURL = "http://localhost:8080/openrdf-sesame";

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<UtenzaAcqua> listaUtenzaAcqua = new ArrayList<UtenzaAcqua>();

		/// Soggetto soggetto =new Soggetto();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					/*
					 * +" select    ?acqua ?datainizio ?datafine ?contribuente ?categoriadescrizione"
					 * +" where{ " +"     ?acqua a :UtenzaAcqua .   "
					 * +"     ?acqua :comunecatastale \""+codcomunecatastale+"\" . "
					 * +"     ?acqua :datainizio ?datainizio. "
					 * +"     ?acqua :categoriadescrizione ?categoriadescrizione. "
					 * +"     ?acqua :contribuente ?contribuente. "
					 * +"     ?acqua :particellaedificabile \""+particella+"\" . "
					 * 
					 * 
					 * +"     ?acqua :subalterno \""+subalterno+"\" " +" }" ;
					 * 
					 */

					+ " select    ?acqua ?datainizio ?contribuente ?categoriadescrizione ?notabreve ?codicefiscale ?interno"
					+ " where{ "

					+ "     ?idcat a :IdentificativoCatastale .   " + "     ?idcat :hasUnitaImmobiliare :" + codUI
					+ " ." + "     ?idcat :Subalterno ?subalterno ." + "     ?idcat :Numero ?numero ."

					+ "     ?acqua a :UtenzaAcqua .   "

					+ "     ?acqua :datainizio ?datainizio . "
					+ "     ?acqua :categoriadescrizione ?categoriadescrizione . "
					+ "     ?acqua :contribuente ?contribuente . " + "     ?acqua :particellaedificabile ?numero . "

					+ "   OPTIONAL{   ?acqua :codfiscale ?codicefiscale }. "
					+ "   OPTIONAL{  ?acqua :notabreve ?notabreve }. " + "   OPTIONAL{  ?acqua :interno ?interno} . "
					+ "     ?acqua :subalterno ?subalterno  " + " }";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value acquaURI = bindingSet.getValue("acqua");
				Value datainizio = bindingSet.getValue("datainizio");
				Value interno = bindingSet.getValue("interno");
				Value notabreve = bindingSet.getValue("notabreve");
				Value codicefiscale = bindingSet.getValue("codicefiscale");
				Value contribuente = bindingSet.getValue("contribuente");
				Value categoriadescrizione = bindingSet.getValue("categoriadescrizione");

				UtenzaAcqua utenzaacqua = new UtenzaAcqua();

				if (acquaURI != null) {
					utenzaacqua.setUri(acquaURI.stringValue());
				}
				if (datainizio != null) {
					utenzaacqua.setDataInizio(datainizio.stringValue());
				}

				if (interno != null) {
					utenzaacqua.setInterno(interno.stringValue());
				}
				if (notabreve != null) {

					utenzaacqua.setNotabreve((notabreve.stringValue()));
				}
				if (codicefiscale != null) {

					utenzaacqua.setCodicefiscale((codicefiscale.stringValue()));
				}

				if (contribuente != null) {
					utenzaacqua.setContribuente((contribuente.stringValue()));
				}
				if (categoriadescrizione != null) {
					utenzaacqua.setCategoriaDescrizione((categoriadescrizione.stringValue()));
				}

				listaUtenzaAcqua.add(utenzaacqua);
			}
			// qresult.close();
			// connection.close();
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
		// return listaUtenzaAcqua;
		return new JSONWithPadding(new GenericEntity<List<UtenzaAcqua>>(listaUtenzaAcqua) {
		}, callback);
	}


	
	
	/**
	 * Metodo che restituisce la lista delle utenze acqua relative ad un’unità immobiliare in formato JSON
	 * 
	 * <p>
	 *select    ?acqua ?datainizio ?cognome ?nome ?contribuente ?categoriadescrizione ?notabreve  ?interno
	 *		 where{ 
	 *		  ?idcat a :IdentificativoCatastale .        
	 *		  ?idcat :hasUnitaImmobiliare :" + codUI +" ."
	 *	      ?acqua a :UtenzaAcqua .   "
	 *	      ?acqua :hasIdentificativoCatastale ?idcat .   
	 *	      ?acqua :dataInizio ?datainizio . 
	 *		  ?acqua :categoriaDescrizione ?categoriadescrizione . 
	 *		  ?acqua :codiceContribuente ?codcontribuente . 
	 *		   OPTIONAL{   ?acqua :hasContribuente ?contribuente }. 
	 *		   OPTIONAL{   ?contribuente :cognome ?cognome }. 
	 *		   OPTIONAL{   ?contribuente :nome ?nome }. 
	 *		   OPTIONAL{  ?acqua :notaBreve ?notabreve }.  
	 *		   OPTIONAL{  ?acqua :interno ?interno}.}
	 * </p>
	 * url : http://localhost:8080/GeoreporterService/rest/utenzeacquaui
	 * @param codui=IDUnitaImmobiliare
	 * @return List<UtenzaAcqua>
	 */
	
	
	@GET
	@Path("/utenzeacquaui")
	@Produces({ "application/javascript" })
	// @Produces(MediaType.APPLICATION_JSON)

	// public List<UtenzaAcqua> getUtenzeAcquaUI(
	public JSONWithPadding getUtenzeAcquaUInew(@QueryParam("callback") String callback, @QueryParam("codui") String codUI
	// @QueryParam("particella") String particella,
	// @QueryParam("subalterno") String subalterno

	) {

		Soggetto sog = new Soggetto();
		// String springlesserverURL = "http://localhost:8080/openrdf-sesame";

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<UtenzaAcqua> listaUtenzaAcqua = new ArrayList<UtenzaAcqua>();

		/// Soggetto soggetto =new Soggetto();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					/*
					 * +" select    ?acqua ?datainizio ?datafine ?contribuente ?categoriadescrizione"
					 * +" where{ " +"     ?acqua a :UtenzaAcqua .   "
					 * +"     ?acqua :comunecatastale \""+codcomunecatastale+"\" . "
					 * +"     ?acqua :datainizio ?datainizio. "
					 * +"     ?acqua :categoriadescrizione ?categoriadescrizione. "
					 * +"     ?acqua :contribuente ?contribuente. "
					 * +"     ?acqua :particellaedificabile \""+particella+"\" . "
					 * 
					 * 
					 * +"     ?acqua :subalterno \""+subalterno+"\" " +" }" ;
					 * 
					 */

					+ " select    ?acqua ?datainizio ?cognome ?nome ?contribuente ?categoriadescrizione ?notabreve  ?interno"
					+ " where{ "

					+ "     ?idcat a :IdentificativoCatastale .   " + "     ?idcat :hasUnitaImmobiliare :" + codUI +" ."
				//	+ "     ?idcat :subalterno ?subalterno ."
				//	+ "     ?idcat :numero ?numero ."
				//	+ "    OPTIONAL{ ?idcat :denominatore ?denominatore .}"
					+ "     ?acqua a :UtenzaAcqua .   "
					+ "     ?acqua :hasIdentificativoCatastale ?idcat .   "
					+ "     ?acqua :dataInizio ?datainizio . "
					+ "     ?acqua :categoriaDescrizione ?categoriadescrizione . "
					+ "     ?acqua :codiceContribuente ?codcontribuente . "

					+ "   OPTIONAL{   ?acqua :hasContribuente ?contribuente }. "
					+ "   OPTIONAL{   ?contribuente :cognome ?cognome }. "
					+ "   OPTIONAL{   ?contribuente :nome ?nome }. "
					+ "   OPTIONAL{  ?acqua :notaBreve ?notabreve }. " 
					+"   OPTIONAL{  ?acqua :interno ?interno}.}";

		//	System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value acquaURI = bindingSet.getValue("acqua");
				Value datainizio = bindingSet.getValue("datainizio");
				Value interno = bindingSet.getValue("interno");
				Value notabreve = bindingSet.getValue("notabreve");
				Value codicefiscale = bindingSet.getValue("codicefiscale");
				Value contribuente = bindingSet.getValue("contribuente");
				Value cognome = bindingSet.getValue("cognome");
				Value nome = bindingSet.getValue("nome");
				Value categoriadescrizione = bindingSet.getValue("categoriadescrizione");

				UtenzaAcqua utenzaacqua = new UtenzaAcqua();

				if (acquaURI != null) {
				//	utenzaacqua.setUri(acquaURI.stringValue());
				}
				if (datainizio != null) {
					utenzaacqua.setDataInizio(datainizio.stringValue());
				}

				if (interno != null) {
					utenzaacqua.setInterno(interno.stringValue());
				}
				if (notabreve != null) {

					utenzaacqua.setNotabreve((notabreve.stringValue()));
				}
				if (codicefiscale != null) {

					utenzaacqua.setCodicefiscale((codicefiscale.stringValue()));
				}

				if (contribuente != null) {
					String soggetto="";
					if (cognome !=null) {
						soggetto =soggetto+cognome.stringValue();	
					}
					if (nome !=null) {
						soggetto =soggetto+" "+nome.stringValue();	
					}
					//System.out.println("SOGGETTO="+ soggetto);
					utenzaacqua.setContribuente(soggetto);
					
				}
				if (categoriadescrizione != null) {
					utenzaacqua.setCategoriaDescrizione((categoriadescrizione.stringValue()));
				}

				listaUtenzaAcqua.add(utenzaacqua);
			}
			// qresult.close();
			// connection.close();
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
		// return listaUtenzaAcqua;
		return new JSONWithPadding(new GenericEntity<List<UtenzaAcqua>>(listaUtenzaAcqua) {
		}, callback);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	@GET
	@Path("/iciimuui_old")
	@Produces({ "application/javascript" })
	// @Produces(MediaType.APPLICATION_JSON)
	// public List<TributiICI> getICI_IMU_UI(

	public JSONWithPadding getICI_IMU_UI(@QueryParam("callback") String callback, @QueryParam("codui") String codUI

	) {

		Soggetto sog = new Soggetto();
		// String springlesserverURL = "http://localhost:8080/openrdf-sesame";

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<TributiICI> listaTributiICI = new ArrayList<TributiICI>();

		/// Soggetto soggetto =new Soggetto();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ " select    ?ici  ?contribuente ?indirizzo ?categoriadescrizione ?rendita" + " where{ "

					+ "     ?idcat a :IdentificativoCatastale .   " + "     ?idcat :hasUnitaImmobiliare :" + codUI
					+ " ." + "     ?idcat :Subalterno ?subalterno ." + "     ?idcat :Numero ?numero ."

					+ "     ?ici a :ICI_IMU .   "
					// +" ?ici :codcomune \""+codcomunecatastale+"\" . "
					+ "     ?ici :indirizzo ?indirizzo. " + "     ?ici :categoriadescrizione ?categoriadescrizione. "
					+ "     ?ici :contribuente ?contribuente. " + "     ?ici :rendita ?rendita. "
					+ "     ?ici :particellaedificabile ?numero . " + "     ?ici :subalterno  ?subalterno  " + " }";

		//	System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value iciURI = bindingSet.getValue("ici");
				Value indirizzo = bindingSet.getValue("indirizzo");
				Value rendita = bindingSet.getValue("rendita");
				Value contribuente = bindingSet.getValue("contribuente");
				Value categoriadescrizione = bindingSet.getValue("categoriadescrizione");

				TributiICI tributiici = new TributiICI();

				if (iciURI != null) {
					tributiici.setUri(iciURI.stringValue());
				}

				if (indirizzo != null) {
					tributiici.setIndirizzo(indirizzo.stringValue());
				}
				if (rendita != null) {
					tributiici.setRendita(rendita.stringValue());
				}
				if (contribuente != null) {
					tributiici.setContribuente((contribuente.stringValue()));
				}
				if (categoriadescrizione != null) {
					tributiici.setCategoriadescrizione((categoriadescrizione.stringValue()));
				}

				listaTributiICI.add(tributiici);
			}
			// qresult.close();
			// connection.close();
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
		return new JSONWithPadding(new GenericEntity<List<TributiICI>>(listaTributiICI) {
		}, callback);
		// return listaTributiICI;

	}

	
	/**
	 * Metodo che restituisce la lista dei Tributi ICI/IMU i relative ad un’unità immobiliare in formato JSON
	 * 
	 * <p>
     *  select    ?ici  ?cognome ?nome ?contribuente ?indirizzo ?categoriadescrizione ?rendita
     *   where{ 
		*			     ?idcat a :IdentificativoCatastale .   
		*			     ?idcat :hasUnitaImmobiliare : codUI. 
		*			     ?ici a :ICI_IMU_AbitazionePrincipale .   
		*		    	 ?ici :hasIdentificativoCatastale ?idcat. 
		*			     ?ici :hasIndirizzoContribuente ?idindirizzo. 
		*			     OPTIONAL{    ?idindirizzo :indirizzoCompleto ?indirizzo}. 
		*			     ?ici :categoriaDescrizione ?categoriadescrizione. 
		*			     ?ici :hasContribuente ?contribuente. 
		*    			OPTIONAL { ?contribuente :cognome ?cognome} .
		*	    		OPTIONAL { ?contribuente :nome ?nome} .
		*			    ?ici :rendita ?rendita. 			
		*			 }
	 * </p>
	 * url : http://localhost:8080/GeoreporterService/rest/iciimuui
	 * @param codui=IDUnitaImmobiliare
	 * @return List<TributiICI>
	 */
	
	@GET
	@Path("/iciimuui")
	@Produces({ "application/javascript" })
	// @Produces(MediaType.APPLICATION_JSON)
	// public List<TributiICI> getICI_IMU_UI(

	public JSONWithPadding getICI_IMU_UI_new(@QueryParam("callback") String callback, @QueryParam("codui") String codUI

	) {

		Soggetto sog = new Soggetto();
		// String springlesserverURL = "http://localhost:8080/openrdf-sesame";

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<TributiICI> listaTributiICI = new ArrayList<TributiICI>();

		/// Soggetto soggetto =new Soggetto();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ " select    ?ici  ?cognome ?nome ?contribuente ?indirizzo ?categoriadescrizione ?rendita" + " where{ "

					+ "     ?idcat a :IdentificativoCatastale .   "
					+ "     ?idcat :hasUnitaImmobiliare :" + codUI
					+ " ." 
					//+ "     ?idcat :Subalterno ?subalterno ." + "     ?idcat :Numero ?numero ."

					+ "     ?ici a :ICI_IMU_AbitazionePrincipale .   "
					     +" ?ici :hasIdentificativoCatastale ?idcat. "
					// +" ?ici :codcomune \""+codcomunecatastale+"\" . "
					+ "     ?ici :hasIndirizzoContribuente ?idindirizzo. " 
					+ " OPTIONAL{    ?idindirizzo :indirizzoCompleto ?indirizzo}. "
					+ "     ?ici :categoriaDescrizione ?categoriadescrizione. "
					+ "     ?ici :hasContribuente ?contribuente. " 
					+  "OPTIONAL { ?contribuente :cognome ?cognome} ."
					+  "OPTIONAL { ?contribuente :nome ?nome} ."
					+ "     ?ici :rendita ?rendita. "
					
					+ " }";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value iciURI = bindingSet.getValue("ici");
				Value indirizzo = bindingSet.getValue("indirizzo");
				Value cognome = bindingSet.getValue("cognome");
				Value nome = bindingSet.getValue("nome");
				Value rendita = bindingSet.getValue("rendita");
				Value contribuente = bindingSet.getValue("contribuente");
				Value categoriadescrizione = bindingSet.getValue("categoriadescrizione");

				TributiICI tributiici = new TributiICI();

				if (iciURI != null) {
			//		tributiici.setUri(iciURI.stringValue());
				}

				if (indirizzo != null) {
					tributiici.setIndirizzo(indirizzo.stringValue());
				}
				if (rendita != null) {
					tributiici.setRendita(rendita.stringValue());
				}
				if (contribuente != null) {
					String soggetto="";
					if (cognome !=null) {
						soggetto =soggetto+cognome.stringValue();	
					}
					if (nome !=null) {
						soggetto =soggetto+" "+nome.stringValue();	
					}
				//	System.out.println("SOGGETTO="+ soggetto);
					tributiici.setContribuente(soggetto);
				}
				if (categoriadescrizione != null) {
					tributiici.setCategoriadescrizione((categoriadescrizione.stringValue()));
				}

				listaTributiICI.add(tributiici);
			}
			// qresult.close();
			// connection.close();
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
		return new JSONWithPadding(new GenericEntity<List<TributiICI>>(listaTributiICI) {
		}, callback);
		// return listaTributiICI;

	}

	
	
	
	
	
	
	
	/**
	 * Metodo che restituisce la lista delle utenze rifiuti relative ad un’unità immobiliare in formato JSON
	 * 
	 * <p>
	 *		 select    ?ur  ?categoria ?tipoUtenza ?mqLocali ?occupante ?categoriaDescrizione ?indirizzoCompleto 
	 *		 where{ 
	 *                  ?idcat a :IdentificativoCatastale .   
	 *				     ?idcat :hasUnitaImmobiliare :"+codUI+ " ." 
	 *				     ?ur a :UtenzaRifiuti .   
	 *				     ?ur :hasIdentificativoCatastale ?idcat. 
	 *				     ?ur :hasIndirizzoContribuente ?indirizzo.  
								
	 *				   OPTIONAL{   ?ur :categoria ?categoria }. 
	 *				   OPTIONAL{   ?ur :tipoUtenza ?tipoUtenza }. 
	 *				   OPTIONAL{   ?ur :mqLocali ?mqLocali }. 
	 *				   OPTIONAL{   ?ur :occupante ?occupante }. 
	 *				   OPTIONAL{   ?ur :categoriaDescrizione ?categoriaDescrizione }. 			
	 *				   OPTIONAL{   ?indirizzo :indirizzoCompleto ?indirizzoCompleto }. 
					
	 *				 }
	 * </p>
	 * url : http://localhost:8080/GeoreporterService/rest/rifiutiui
	 * @param codui=IDUnitaImmobiliare
	 * @return List<UtenzaRifiuti>
	 */
	@GET
	@Path("/rifiutiui")
	@Produces({ "application/javascript" })
	// @Produces(MediaType.APPLICATION_JSON)
	// public List<TributiICI> getICI_IMU_UI(

	public JSONWithPadding getUtenzeRifiutiSuUI(@QueryParam("callback") String callback, @QueryParam("codui") String codUI

	) {

		
		// String springlesserverURL = "http://localhost:8080/openrdf-sesame";

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<UtenzaRifiuti> listaUtenzaRifiuti = new ArrayList<UtenzaRifiuti>();

		/// Soggetto soggetto =new Soggetto(); 

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ " select    ?ur  ?categoria ?tipoUtenza ?mqLocali ?occupante ?categoriaDescrizione ?indirizzoCompleto" 
					+ " where{ "

					+ "     ?idcat a :IdentificativoCatastale .   "
					+ "     ?idcat :hasUnitaImmobiliare :"+codUI+ " ." 
					+ "     ?ur a :UtenzaRifiuti .   "
					+ "     ?ur :hasIdentificativoCatastale ?idcat. "
					+ "     ?ur :hasIndirizzoContribuente ?indirizzo. " 
								
					+ "   OPTIONAL{   ?ur :categoria ?categoria }. "
					+ "   OPTIONAL{   ?ur :tipoUtenza ?tipoUtenza }. "
					+ "   OPTIONAL{   ?ur :mqLocali ?mqLocali }. "
					+ "   OPTIONAL{   ?ur :occupante ?occupante }. "
					+ "   OPTIONAL{   ?ur :categoriaDescrizione ?categoriaDescrizione }. "			
					+ "   OPTIONAL{   ?indirizzo :indirizzoCompleto ?indirizzoCompleto }. "
					
					+ " }";

			//System.out.println(queryString);
			TupleQuery tupleQuery;
			int i = 0;
			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
			TupleQueryResult qresult = tupleQuery.evaluate();
			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value urURI = bindingSet.getValue("ur");
				Value categoria = bindingSet.getValue("categoria");
				Value tipoUtenza = bindingSet.getValue("tipoUtenza");
				Value mqLocali = bindingSet.getValue("mqLocali");
				Value occupante = bindingSet.getValue("occupante");
				Value categoriaDescrizione = bindingSet.getValue("categoriaDescrizione");
				Value indirizzoCompleto = bindingSet.getValue("indirizzoCompleto");
				
				UtenzaRifiuti utenzaRifiuti = new UtenzaRifiuti();

				if (urURI != null) {
				//	utenzaRifiuti.setUri(urURI.stringValue());
				}
				if (categoria != null) {
					utenzaRifiuti.setCategoria((categoria.stringValue()));
				}
				if (tipoUtenza != null) {
					utenzaRifiuti.setTipoUtenza((tipoUtenza.stringValue()));
				}
				if (mqLocali != null) {
					utenzaRifiuti.setMqLocali(new Integer(mqLocali.stringValue()));
				}
				if (occupante != null) {
					utenzaRifiuti.setOccupante((occupante.stringValue()));
				}
				if (indirizzoCompleto != null) {
					utenzaRifiuti.setIndirizzoCompleto((indirizzoCompleto.stringValue()));
				}
				if (categoriaDescrizione != null) {
					utenzaRifiuti.setCategoriaDescrizione((categoriaDescrizione.stringValue()));
				}

				listaUtenzaRifiuti.add(utenzaRifiuti);
			}
			// qresult.close();
			// connection.close();
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
		return new JSONWithPadding(new GenericEntity<List<UtenzaRifiuti>>(listaUtenzaRifiuti) {
		}, callback);
		// return listaTributiICI;

	}



	
	/**
	 * Metodo che restituisce la lista dei contratti di locazione relative ad un’unità immobiliare in formato JSON
	 * 
	 * <p>
     *    select    ?locaz  ?tipoCanone ?importoCanone ?denominazione ?denominazioneFile   
	 *				 where{ 
     *				     ?idcat a :IdentificativoCatastale .   
	 *				     ?idcat :hasUnitaImmobiliare :"+codUI+ " ." 
	 *				     ?locaz a :Locazione .   
	 *				     ?locaz :hasIdentificativoCatastale ?idcat. 							
     *				   OPTIONAL{   ?locaz :tipoCanone ?tipoCanone }.
	 *				   OPTIONAL{   ?locaz :importoCanone ?importoCanone }. 
	 *				   OPTIONAL{   ?locaz :denominazione ?denominazione }.
	 *				   OPTIONAL{   ?locaz :denominazioneFile ?denominazioneFile }. 
	 *				 }
	 * </p>
	 * url : http://localhost:8080/GeoreporterService/rest/locazioniui
	 * @param codui=IDUnitaImmobiliare
	 * @return List<Locazione>
	 */
	
	
	
	
	
	@GET
	@Path("/locazioniui")
	@Produces({ "application/javascript" })
	// @Produces(MediaType.APPLICATION_JSON)
	// public List<TributiICI> getICI_IMU_UI(

	public JSONWithPadding getContrattoLocazioniSuUI(@QueryParam("callback") String callback, @QueryParam("codui") String codUI

	) {

		
		// String springlesserverURL = "http://localhost:8080/openrdf-sesame";

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<Locazione> listaLocazioni = new ArrayList<Locazione>();

		/// Soggetto soggetto =new Soggetto(); 

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ " select    ?locaz  ?tipoCanone ?importoCanone ?denominazione ?denominazioneFile  " 
					+ " where{ "

					+ "     ?idcat a :IdentificativoCatastale .   "
					+ "     ?idcat :hasUnitaImmobiliare :"+codUI+ " ." 
					+ "     ?locaz a :Locazione .   "
					+ "     ?locaz :hasIdentificativoCatastale ?idcat. "
					//+ "     ?locaz :hasIndirizzoContribuente ?indirizzo. " 
								
					+ "   OPTIONAL{   ?locaz :tipoCanone ?tipoCanone }. "
					+ "   OPTIONAL{   ?locaz :importoCanone ?importoCanone }. "
					+ "   OPTIONAL{   ?locaz :denominazione ?denominazione }. "
					+ "   OPTIONAL{   ?locaz :denominazioneFile ?denominazioneFile }. "
						
					
					
					+ " }";

			//System.out.println(queryString);
			TupleQuery tupleQuery;
			int i = 0;
			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);
			TupleQueryResult qresult = tupleQuery.evaluate();
			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value urLocaz = bindingSet.getValue("locaz");
				Value tipoCanone = bindingSet.getValue("tipoCanone");
				Value importoCanone = bindingSet.getValue("importoCanone");
				Value denominazione = bindingSet.getValue("denominazione");
				Value denominazioneFile = bindingSet.getValue("denominazioneFile");
				
				
				Locazione locazione = new Locazione();

				if (urLocaz != null) {
				//	locazione.setUri(urLocaz.stringValue());
				}
				if (tipoCanone != null) {
					locazione.setTipoCanone((tipoCanone.stringValue()));
				}
				if (importoCanone != null) {
					locazione.setImportoCanone(new Float((importoCanone.stringValue())));
				}
				if (denominazione != null) {
					locazione.setDenominazioneFile(denominazioneFile.stringValue());
				}
				
				
				listaLocazioni.add(locazione);
			}
			// qresult.close();
			// connection.close();
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
		return new JSONWithPadding(new GenericEntity<List<Locazione>>(listaLocazioni) {
		}, callback);
		// return listaTributiICI;

	}

	
	
	
	
	
	@POST
	@Path("/inserttable")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String insertTable(

			//JSONObject input
			RigaTabella rigatabella

	) {

		String result = "FAIL";

	//	Gson gson = new Gson();
//		String json = gson.toJson(input.toString());

//		System.out.println(json);
	///	RigaTabella rigatabella = gson.fromJson(input.toString(), RigaTabella.class);
				
		
		try {
			

			Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);

			List<Attributo> tableAttributiChiave_List = rigatabella.getListachiave();
			List<Attributo> tableAttributi_List = rigatabella.getListaattributi();
            List<Relazione> tableRelazioni_List= rigatabella.getListarelazioni();
		
			

			myRepository.initialize();
			RepositoryConnection con = myRepository.getConnection();
			String queryString = "";

			ValueFactory factory = myRepository.getValueFactory();

        //     System.out.println("AAAAAAAAAAAAAAAAAAAA");
		//	 if ((rigatabella.getUririga()==null)||rigatabella.getUririga().equals("")){
		//		 System.out.println("BBBBBBBBBBBBBBBBBBBBBB");					
					//ResponseBuilderImpl builder = new ResponseBuilderImpl();
					//builder.status(Response.Status.UNSUPPORTED_MEDIA_TYPE);
					//builder.entity("ERROR! URIRiga parameter missing or null");
					//builder.type(MediaType.TEXT_HTML);
					//Response response = builder.build();
				//	throw new WebApplicationException(response);
			//		throw new CustomerDataNotFoundException("Customer status not found with id ");
				// }
			
			
		
			 //System.out.println("CCCCCCCCCCCCCCCCCCCCCCCC");			
			
			
			
			URI uriid = new URIImpl(rigatabella.getUririga());
			URI tabella = new URIImpl(rigatabella.getNometabella());
		    
			Esiste esiste= esiste(rigatabella.getUririga(),rigatabella.getNometabella());
			
			
			
		//	URI context = factory.createURI(uricontesto);

			con.begin();
		//	con.setNamespace("","" );
			con.add(uriid, RDF.TYPE, tabella);
			con.add(uriid, RDF.TYPE,new URIImpl("http://www.w3.org/2002/07/owl#NamedIndividual"));

			if (tableAttributiChiave_List!=null) {
			for (Attributo attributoChiave : tableAttributiChiave_List) {
				if (attributoChiave.isMultiplo()||!esiste.checkEsiste(attributoChiave.getMapping())){
				//Literal lit = factory.createLiteral(attributoChiave.getValore());
				//Statemtent stmt = new StatementImpl(subject, predicate, object);
				Value literal = new LiteralImpl(attributoChiave.getValore(),new URIImpl( attributoChiave.getTipo()));
				con.add(uriid, new URIImpl(attributoChiave.getMapping()), literal);
			//	System.out.println(uriid + " " + factory.createURI(attributoChiave.getMapping()) + " " + literal + " " );
				}
			}
			}
			if (tableAttributi_List!=null) {
			for (Attributo attributo : tableAttributi_List) {
				if (attributo.isMultiplo()||!esiste.checkEsiste(attributo.getMapping())){
				Value literal = new LiteralImpl(attributo.getValore(),new URIImpl( attributo.getTipo()));
				con.add(uriid, new URIImpl(attributo.getMapping()), literal);
			//	System.out.println(uriid + " " + factory.createURI(attributo.getMapping()) + " " + literal + " " );
				}
			}
			}
			if (tableRelazioni_List!=null) {
			for (Relazione relazione : tableRelazioni_List) {
				if (!esiste.checkEsiste(relazione.getNomerelazione())){
				//Literal lit = getLiteral(attributo, factory);
				//System.out.println(factory.createURI(relazione.getUriDomain())+" "+ factory.createURI(relazione.getNomerelazione())+" "+  factory.createURI(relazione.getUriRange()));
				con.add( factory.createURI(relazione.getUriDomain()), factory.createURI(relazione.getNomerelazione()), factory.createURI(relazione.getUriRange()));
				
				//System.out.println(factory.createURI(relazione.getUriDomain())+" "+ factory.createURI(relazione.getNomerelazione())+" "+  factory.createURI(relazione.getUriRange()));
				}
			}
			}

			con.commit();

			result = rigatabella.getUririga();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return  result;
	}
	
	
	@POST
	@Path("/inseriscirigassss")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String insertTableRowssssss(

			JSONObject input

	) {

		String result = "FAIL";
	//	ObjectMapper mapper = new ObjectMapper();
	//	MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
	//	Map<String, Object> data;

		// data = mapper.readValue(input, type);

		String iduri;
		String idtabella;
		try {
			iduri = (String) input.get("iduri");
			idtabella = (String) input.get("idtabella");

			Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);

			Map<String, String> inputparam_HM = new HashMap<String, String>();
			Map<String, Attributo> tableAttributi_HM = new HashMap<String, Attributo>();
			Map<String, Attributo> insert_HM = new HashMap<String, Attributo>();

			inputparam_HM = jsonToMap(input);

			List<Attributo> tableAttribut_List = getAttributiTabella(idtabella);

			// converto la lista in HM
			for (Attributo attributo : tableAttribut_List) {
				//System.out.println(attributo.getNome() + " " + attributo.getTipo() + " " + attributo.getValore());
				tableAttributi_HM.put(attributo.getNome(), attributo);
			}
			// preparo la HM dei parametri per l'inserimento
			for (String key : inputparam_HM.keySet()) {
				// prendo solo quelli che hanno nome giusto
				if (tableAttributi_HM.containsKey(key)) {

					Attributo attr = tableAttributi_HM.get(key);
					attr.setValore(inputparam_HM.get(key));
					insert_HM.put(key, attr);

				}
			}

			myRepository.initialize();
			RepositoryConnection con = myRepository.getConnection();
			String queryString = "";

			ValueFactory factory = myRepository.getValueFactory();
			URI uiuruui = factory.createURI(iduri);
			URI ui = factory.createURI(":" + idtabella);
			URI context = factory.createURI(uricontesto);

			con.begin();
			con.add(uiuruui, RDF.TYPE, ui, context);

			for (String key : insert_HM.keySet()) {
				Attributo attr = insert_HM.get(key);
				// getLiteral mi restituisce il tipo Literal corretto
				Literal lit = getLiteral(attr, factory);
				//System.out.println(uiuruui + " " + factory.createURI(key) + " " + lit + " " + context);
				con.add(uiuruui, factory.createURI(key), lit, context);
			}

			con.commit();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//System.out.println("ERROREzxsss: parametro iduri o idtabella mancante");
			// e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	

	public Literal getLiteral(Attributo attributo, ValueFactory factory) {

		Literal l = factory.createLiteral("");
		String result = "";
		String localName = attributo.getTipo();
		if (localName.equals(Costanti.INTEGER.stringValue())) {
			result = "^^xsd:integer";
			Integer intvalue = (new Integer(attributo.getValore()));
			l = factory.createLiteral(intvalue.intValue());
		}
		if (localName.equals(Costanti.STRING.stringValue())) {
			result = "^^xsd:string";
			l = factory.createLiteral(attributo.getValore());
		}
	
		if (localName.equals(Costanti.FLOAT.stringValue())) {
			result = "^^xsd:float";
			Float floatvalue = (new Float(attributo.getValore()));
			l = factory.createLiteral(floatvalue.floatValue());
		}
		if (localName.equals(Costanti.DATETIME.stringValue())) {
			result = "^^xsd:dateTime";
			Date datevalue = (new Date(attributo.getValore()));
			l = factory.createLiteral(datevalue);
		}
		if (localName.equals(Costanti.BOOLEAN.stringValue())) {
			result = "^^xsd:boolean";
			Boolean boolvalue = (new Boolean(attributo.getValore()));
			l = factory.createLiteral(boolvalue.booleanValue());
		}
		return l;

	}

	
	

	

	 
	
	@POST
	@Path("/importaunitaimmobiliari")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	//public String uploadUnitaImmobiliari (MultiPart multiPart,MultiPart multiPart2) {
	public String  uploadUnitaImmobiliari (
      
	
	@FormDataParam("file") InputStream uploadedInputStream,
	@FormDataParam("file") FormDataContentDisposition fileDetail) {
	
	
	
	
	String result="fail";
       
	String uploadedFileLocation =  fileDetail.getFileName();

	// save it
	writeToFile(uploadedInputStream, uploadedFileLocation);

	String output = "File uploaded to : " + uploadedFileLocation;

	return output;

	
	}
	
	
	
	

	/**
	 * Servizio per il caricamento dei file (anche tramite l’interfaccia web http://www.georeporter.it:8080/GeoreporterService/tools/import.html)
	 * 
	 * <p>	 
     * File Catasto fabbricati(Unità Immobiliari, Soggetti,  Titolarità), 
     * Anagrafe Comunale, Famiglia, 
     * Contratti di Luce, Gas e Locazione, 
     * Tributi ICI_IMU, Utenze Rifiuti, Utenza Acqua, Letture Acqua,
     * File del catasto fondiario (Particelle Fondiarie, Soggetti, Titolarità)
	 * </p>
	 * url : http://localhost:8080/GeoreporterService/rest/importadati
	 * @param Prende i parametri da una form html:file dati, file di mapping, file header, nome del tipo dati

	 * @return List<UnitaImmobiliare>
	 */
	
	
	@POST
	@Path("/importadati")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	//public String uploadUnitaImmobiliari (MultiPart multiPart,MultiPart multiPart2) {
	public String  importaDati (
			@FormDataParam("tipodati") String tipodati,
			@FormDataParam("serviceURL") String serviceURL,
			@FormDataParam("codiceComuneCatastale") String codiceComuneCatastale,			
			@FormDataParam("fileheader") InputStream fileHeaderIS,
            @FormDataParam("fileheader") FormDataContentDisposition fileHeaderDetail,
            @FormDataParam("filedati") InputStream fileDatiIS,
            @FormDataParam("filedati") FormDataContentDisposition fileDatiDetail,
            @FormDataParam("filemappings") InputStream fileMappingIS,
            @FormDataParam("filemappings") FormDataContentDisposition fileMappingsDetail) throws IOException 
	
	{
		String uploadedFileMappingsLocation =  fileMappingsDetail.getFileName();
		String output = "File uploaded to : " + uploadedFileMappingsLocation;
		System.out.println(output);
		
		System.out.println("filedati="+fileDatiDetail.getFileName());
		System.out.println("fileMappings="+fileMappingsDetail.getFileName());		
		String fileHeader="";
		// save it
		writeToFile(fileDatiIS, fileDatiDetail.getFileName());
		writeToFile(fileMappingIS, fileMappingsDetail.getFileName());
		if (fileHeaderIS!=null) {
		writeToFile(fileHeaderIS, fileHeaderDetail.getFileName());
		 fileHeader=fileHeaderDetail.getFileName();
		}

     String filePath=fileDatiDetail.getFileName();
     String fileMappings=fileMappingsDetail.getFileName();
		
	
	String result="fail";
	// String serviceURL="http://localhost:8080/GeoreporterService/servizio/rest/";
		ImportGeoreporter importgr= new ImportGeoreporter(serviceURL);
		
	//ImportGeoreporter.importaContrattiLocazione(fileDatiDetail.getFileName(), fileMappingsDetail.getFileName());
	switch (tipodati) {
    case "fabbricati": 
    //	importgr.importaUnitaImmobiliari1(filePath, fileHeader, fileMappings);
    //	importgr.importaUnitaImmobiliari2(filePath, fileHeader, fileMappings);
    	importgr.importaUnitaImmobiliari3(filePath, fileHeader, fileMappings);
             break;
    case "soggettifabbricati": 
    	importgr.importaSoggettiFabbricati(filePath, fileHeader, fileMappings);    
    		break;
    case "titolaritafabbricati": 
    	importgr.importaTitolaritaFabbricati(filePath, fileHeader, fileMappings);
            break;
    case "anagraficacomunale": 
    	importgr.importaAnagraficaComunale(filePath, fileMappings,codiceComuneCatastale);
            break;
    case "anagraficafamiglie": 
    	importgr.importaAnagraficaFamiglie(filePath, fileMappings,codiceComuneCatastale);
            break;
    case "fornitureelettriche": 
    	importgr.importaFornitureElettriche(filePath, fileMappings);
            break;
    case "fornituregas": 
    	importgr.importaFornitureGas(filePath, fileMappings);
            break;  
    case "contrattilocazione": 
    	importgr.importaContrattiLocazione(filePath, fileMappings);
            break;   
    case "utenzerifiuti": 
    	importgr.importaTributiUtenzeRifiuti(filePath, fileMappings);
            break;
    case "utenzeacqua": 
    	importgr.importaTributiUtenzaAcqua(filePath, fileMappings);
            break; 
    case "lettureacqua": 
    	importgr.importaTributiLetturaAcqua(filePath, fileMappings);
            break;   
    case "iciimuabitazioneprincipale": 
    	importgr.importaTributiUtenzaICI_IMU_AbitazionePrincipale(filePath, fileMappings);
            break;   
            
    case "iciimunudaproprieta": 
    	importgr.importaTributiUtenzaICI_IMU_NudaProprieta(filePath, fileMappings);
            break;   

    case "particellefondiarie": 
    	importgr.importaParticelleFondiarie(filePath, fileHeader, fileMappings,codiceComuneCatastale);
             break;
    case "soggettiparticellefondiarie": 
    	importgr.importaSoggettiParticelleFondiarie(filePath, fileHeader, fileMappings);    
    		break;
    case "titolaritaparticellefondiare": 
    	importgr.importaTitolaritaParticelleFondiarie(filePath, fileHeader, fileMappings);
            break;            
             
             
	}
        result="Import effettuato";     
//	String uploadedFileLocation =  fileDetail.getFileName();

	// save it
//	writeToFile(uploadedInputStream, uploadedFileLocation);

//	String output = "File uploaded to : " + uploadedFileLocation;

	return result;

	

	
	
	
	
	
	

	}
	

	
	
	
	
	
	private void writeToFile(InputStream uploadedInputStream,
			String uploadedFileLocation) {

			try {
				OutputStream out = new FileOutputStream(new File(
						uploadedFileLocation));
				int read = 0;
				byte[] bytes = new byte[1024];

				out = new FileOutputStream(new File(uploadedFileLocation));
				while ((read = uploadedInputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				out.flush();
				out.close();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	
	
	
	
	
 /*           BodyPartEntity bpe = (BodyPartEntity) multiPart.getBodyParts().get(0)
                    .getEntity();
            BodyPartEntity bpe2 = (BodyPartEntity) multiPart2.getBodyParts().get(0)
                    .getEntity();
            
            boolean isProcessed = false;
            String message = null;
         
          InputStream source = bpe.getInputStream();
          try {
			String sresult = IOUtils.toString(source, StandardCharsets.UTF_8);
			System.out.println(sresult);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

*/
	
	
	
	
	@GET
	@Path("/tributiiciintestatarioui")
	@Produces(MediaType.APPLICATION_JSON)
	public List<TributiICI> getTributi() {

		List<TributiICI> listaTributiICI = new ArrayList<TributiICI>();

		return listaTributiICI;

	}

	@GET
	@Path("/fornituregasintestatarioui")
	@Produces(MediaType.APPLICATION_JSON)
	public List<FornituraGas> getFornitureGasIntestatarioui() {

		List<FornituraGas> listaFornituraGas = new ArrayList<FornituraGas>();

		return listaFornituraGas;

	}

	@GET
	@Path("/fornitureenergiaintestatarioui")
	@Produces(MediaType.APPLICATION_JSON)
	public List<FornituraEnergia> getFornitureEnergiaIntestatarioui() {

		List<FornituraEnergia> listaFornituraEnergia = new ArrayList<FornituraEnergia>();

		return listaFornituraEnergia;

	}

	@GET
	@Path("/forniturelocazioniintestatarioui")
	@Produces(MediaType.APPLICATION_JSON)
	public List<FornituraLocazioni> getFornitureLocazioniIntestatarioui() {

		List<FornituraLocazioni> listaFornituraLocazioni = new ArrayList<FornituraLocazioni>();

		return listaFornituraLocazioni;

	}

	@POST
	@Path("/unitaimmobiliare")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String insertUnitaimmobiliare(

			JSONObject input

	) {

		String result = "FAIL";
		ObjectMapper mapper = new ObjectMapper();
		MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
		Map<String, Object> data;

		// data = mapper.readValue(input, type);

		String uiuri;
		try {
			uiuri = input.getString("uiuri");

			
			
			
			Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
			if ((uiuri == null) || uiuri.equals("")) {

				ResponseBuilderImpl builder = new ResponseBuilderImpl();
				builder.status(Response.Status.BAD_REQUEST);
				builder.entity("ERROR! uiuri parameter missing or null");
				Response response = builder.build();
				throw new WebApplicationException(response);
			}
			result = uiuri;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	
	
	
	
	
	@POST
	@Path("/indentificativocatastale")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String insertIdentificativiCatastale(

			JSONObject input

	) {

		String result = "FAIL";
		ObjectMapper mapper = new ObjectMapper();
		MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
		Map<String, Object> data;

		// data = mapper.readValue(input, type);

		String uiuri;
		try {
			uiuri = (String) input.get("uri");

			// Repository myRepository = new HTTPRepository(springlesserverURL,
			// springlesrepositoryID);
			if ((uiuri == null) || uiuri.equals("")) {

				ResponseBuilderImpl builder = new ResponseBuilderImpl();
				builder.status(Response.Status.BAD_REQUEST);
				builder.entity("ERROR! uiuri parameter missing or null");
				Response response = builder.build();
				throw new WebApplicationException(response);
			}
			result = uiuri;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@POST
	@Path("/particella")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String insertParticella(

			JSONObject input

	) {

		String result = "FAIL";
		ObjectMapper mapper = new ObjectMapper();
		MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
		Map<String, Object> data;

		// data = mapper.readValue(input, type);

		String uiuri;
		try {
			uiuri = (String) input.get("uri");

			// Repository myRepository = new HTTPRepository(springlesserverURL,
			// springlesrepositoryID);
			if ((uiuri == null) || uiuri.equals("")) {

				ResponseBuilderImpl builder = new ResponseBuilderImpl();
				builder.status(Response.Status.BAD_REQUEST);
				builder.entity("ERROR! uiuri parameter missing or null");
				Response response = builder.build();
				throw new WebApplicationException(response);
			}
			result = uiuri;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@POST
	@Path("/indirizzo")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String insertIndirizzo(

			JSONObject input

	) {

		String result = "FAIL";
		ObjectMapper mapper = new ObjectMapper();
		MapType type = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
		Map<String, Object> data;

		// data = mapper.readValue(input, type);

		String uiuri;
		try {
			uiuri = (String) input.get("uri");

			// Repository myRepository = new HTTPRepository(springlesserverURL,
			// springlesrepositoryID);
			if ((uiuri == null) || uiuri.equals("")) {

				ResponseBuilderImpl builder = new ResponseBuilderImpl();
				builder.status(Response.Status.BAD_REQUEST);
				builder.entity("ERROR! uiuri parameter missing or null");
				Response response = builder.build();
				throw new WebApplicationException(response);
			}
			result = uiuri;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	@GET
	@Path("/inserttopic")
	@Produces(MediaType.APPLICATION_JSON)
	public String insertTopic(@QueryParam("topicDescr") String topicDescr,
			@QueryParam("URIParentTopic") String URIParentTopic

	) {

		// URIParentTopic= checkNameSpace(URIParentTopic);
		String topicURI = ":topic_" + topicDescr.toLowerCase().trim().replace(" ", "_").replaceAll("[^a-zA-Z0-9_]", "");
		String termURI = topicURI + "_t";

		// String rewURI= POIURI+ UUID.randomUUID();

		String result = "FAIL";
		// String delimiter = "\\,";

		Repository myRepository = new HTTPRepository(springlesserverURL, "toolisse");
		try {
			myRepository.initialize();
			RepositoryConnection con = myRepository.getConnection();

			// result= result+""+con.size();
			try {

				String queryString = "";

				// ObjectMapper mapper = new ObjectMapper();
				// try {
				// Kbelement kbelement= mapper.readValue(rdfPayload,Kbelement.class);
				con.begin();

				String queryStringPrefix2 = "PREFIX :<http://dkm.fbk.eu/trentour#>\n"
						+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
						+ "PREFIX owl2xml:<http://www.w3.org/2006/12/owl2-xml#>\n"
						+ "PREFIX trentour:<http://dkm.fbk.eu/trentour#>\n"
						+ "PREFIX test:<http://dkm.fbk.eu/trentour/test#>\n"
						+ "PREFIX owl:<http://www.w3.org/2002/07/owl#>\n"
						+ "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n"
						+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
						+ "PREFIX ckr: <http://dkm.fbk.eu/ckr/meta#>\n" + "PREFIX meta:<http://dkm.fbk.eu/ckr/meta#>\n";
				queryString = queryStringPrefix2 + "	 INSERT DATA {" + "  GRAPH meta:global {" + "   " + topicURI
						+ " a :Topic ;" + "    :hasTerm \"" + termURI + "\";" + "   :hasParentTopic \"" + URIParentTopic
						+ "\" ." + "   " + termURI + " rdf:type :Term;" + "	:hasText 	\"" + topicDescr + "\" .}"
						+ "}							";

			//	System.out.println(queryString);

				int i = 0;
				// conn.prepareUpdate(QueryLanguage.SPARQL, updateQuery);
				Update insert = con.prepareUpdate(QueryLanguage.SPARQL, queryString);
				insert.getIncludeInferred();
				insert.execute();
				con.commit();
				con.close();
				result = ("{\"topicURI\":\"" + termURI + "\"}");
				// tupleQuery.setIncludeInferred(true);
				// TupleQueryResult qresult = tupleQuery.evaluate();
				try {

				} finally {
					// qresult.close();

				}

			} finally {
				con.close();

			}

		} catch (OpenRDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	@GET
	@Path("/insertpoi")
	@Produces(MediaType.APPLICATION_JSON)
	public String insertPOI(

			@QueryParam("address") String hasAddress, // R//
			@QueryParam("contactName") String hasContactName, @QueryParam("phoneNumber") String hasPhoneNumber,
			@QueryParam("faxNumber") String hasFaxNumber, @QueryParam("mailAddress") String hasMailAddress,
			@QueryParam("ticketCost") String hasTicketCost, @QueryParam("openingHours") String hasOpeningHours,
			@QueryParam("accessibilityInfo") String hasAccessibilityInfo, @QueryParam("capacity") String hasCapacity,
			@QueryParam("status") String hasStatus, @QueryParam("image") String hasImage,
			@QueryParam("video") String hasVideo, @QueryParam("sourceID") String hasSourceID,
			@QueryParam("timeZone") String hasTimeZone, @QueryParam("sourceURL") String hasSourceURL,
			@QueryParam("externalAuthorName") String hasExternalAuthorName,
			@QueryParam("externalAuthorPic") String hasExternalAuthorPic,
			@QueryParam("externalAuthorLink") String hasExternalAuthorLink, @QueryParam("ranking") String hasRanking,
			@QueryParam("parentPOI") String hasParentPOI, @QueryParam("review") String hasReview,

			// Location attributes

			@QueryParam("longitude") String hasLongitude, // R//
			@QueryParam("latitude") String hasLatitude, // R//
			@QueryParam("altitude") String hasAltitude, @QueryParam("radius") String hasRadius,
			@QueryParam("parentLocation") String hasParentlocation,
			// KnowledgeElement attributes

			@QueryParam("description") String hasDescription, // R//
			@QueryParam("externalPage") String hasExternalPage,
			@QueryParam("wikipediaArticle") String hasWikipediaArticle, @QueryParam("term") String hasTerm,

			@QueryParam("POICategory") String hasPOICategory
	// @QueryParam("reviewURL") String hasReviewURL,
	// @QueryParam("reviewText") String hasReviewText
	) {

		if ((hasTerm == null) || hasTerm.equals("")) {

			ResponseBuilderImpl builder = new ResponseBuilderImpl();
			builder.status(Response.Status.BAD_REQUEST);
			builder.entity("ERROR! term parameter missing or null");
			Response response = builder.build();
			throw new WebApplicationException(response);
		}

		/*
		 * if ((hasAddress==null)||hasAddress.equals("")){
		 * 
		 * ResponseBuilderImpl builder = new ResponseBuilderImpl();
		 * builder.status(Response.Status.BAD_REQUEST);
		 * builder.entity("ERROR! POI Address parameter missing or null"); Response
		 * response = builder.build(); throw new WebApplicationException(response); }
		 */

		if ((hasLatitude == null) || hasLongitude.equals("")) {

			ResponseBuilderImpl builder = new ResponseBuilderImpl();
			builder.status(Response.Status.BAD_REQUEST);
			builder.entity("ERROR! POI Latitude parameter missing or null");
			Response response = builder.build();
			throw new WebApplicationException(response);
		}
		if ((hasLongitude == null) || hasLongitude.equals("")) {

			ResponseBuilderImpl builder = new ResponseBuilderImpl();
			builder.status(Response.Status.BAD_REQUEST);
			builder.entity("ERROR! POI Logitude parameter missing or null");
			Response response = builder.build();
			throw new WebApplicationException(response);
		}
		if ((hasParentlocation != null) && !hasParentlocation.equals("")) {
			// hasParentlocation=findKBLocationURI(hasParentlocation).getId();
		}
		// checkNameSpace("parentLocation",parentLocation);
		// checkNameSpace("poiCategory",poicategory);

		String hasAddress_string = "";
		String hasContactName_string = "";
		String hasPhoneNumber_string = "";
		String hasFaxNumber_string = "";
		String hasMailAddress_string = "";
		String hasTicketCost_string = "";
		String hasOpeningHours_string = "";
		String hasAccessibilityInfo_string = "";
		String hasCapacity_string = "";
		String hasStatus_string = "";
		String hasImage_string = "";
		String hasVideo_string = "";
		String hasSourceID_string = "";
		String hasTimeZone_string = "";
		String hasSourceURL_string = "";

		String hasExternalAuthorName_string = "";
		String hasExternalAuthorPic_string = "";
		String hasExternalAuthorLink_string = "";
		String hasRanking_string = "";
		String hasParentPOI_string = "";
		String hasReview_string = "";

		// Location attributes

		String hasLongitude_string = "";// R//
		String hasLatitude_string = "";// R//
		String hasAltitude_string = "";
		String hasRadius_string = "";
		String hasParentLocation_string = "";
		// KnowledgeElement attributes

		String hasDescription_string = "";// R//
		String hasExternalPage_string = "";
		String hasWikipediaArticle_string = "";

		String hasPOICategory_string = "";

		if (!(hasAddress == null) && (!hasAddress.equals(""))) {
			hasAddress_string = "	 :hasAddress \"" + hasAddress + "\" ; ";
		}

		if (!(hasContactName == null) && (!hasContactName.equals(""))) {
			hasContactName_string = "	 :hasContactName \"" + hasContactName + "\" ; ";
		}
		if (!(hasPhoneNumber == null) && (!hasPhoneNumber.equals(""))) {
			hasPhoneNumber_string = "	 :hasPhoneNumber \"" + hasPhoneNumber + "\" ; ";
		}
		if (!(hasFaxNumber == null) && (!hasFaxNumber.equals(""))) {
			hasFaxNumber_string = "	 :hasFaxNumber \"" + hasFaxNumber + "\" ; ";
		}
		if (!(hasMailAddress == null) && (!hasMailAddress.equals(""))) {
			hasMailAddress_string = "	 :hasMailAddress \"" + hasMailAddress + "\" ; ";
		}

		if (!(hasTicketCost == null) && (!hasTicketCost.equals(""))) {
			hasTicketCost_string = "	 :hasTicketCost \"" + hasTicketCost + "\" ; ";
		}

		if (!(hasOpeningHours == null) && (!hasOpeningHours.equals(""))) {
			hasOpeningHours_string = "	 :hasOpeningHours \"" + hasOpeningHours + "\" ; ";
		}
		if (!(hasAccessibilityInfo == null) && (!hasAccessibilityInfo.equals(""))) {
			hasAccessibilityInfo_string = "	 :hasAccessibilityInfo \"" + hasAccessibilityInfo + "\" ; ";
		}
		if (!(hasCapacity == null) && (!hasCapacity.equals(""))) {
			hasCapacity_string = "	 :hasCapacity \"" + hasCapacity + "\" ; ";
		}
		if (!(hasStatus == null) && (!hasStatus.equals(""))) {
			hasStatus_string = "	 :hasStatus \"" + hasStatus + "\" ; ";
		}
		if (!(hasImage == null) && (!hasImage.equals(""))) {
			hasImage_string = "	 :hasImage \"" + hasImage + "\" ; ";
		}
		if (!(hasVideo == null) && (!hasVideo.equals(""))) {
			hasVideo_string = "	 :hasVideo \"" + hasVideo + "\" ; ";
		}
		if (!(hasSourceID == null) && (!hasSourceID.equals(""))) {
			hasSourceID_string = "	 :hasSourceID \"" + hasSourceID + "\" ; ";
		}
		if (!(hasTimeZone == null) && (!hasTimeZone.equals(""))) {
			hasTimeZone_string = "	 :hasTimeZone \"" + hasTimeZone + "\" ; ";
		}
		if (!(hasSourceURL == null) && (!hasSourceURL.equals(""))) {
			hasSourceURL_string = "	 :hasSourceURL \"" + hasSourceURL + "\" ; ";
		}

		if (!(hasExternalAuthorName == null) && (!hasExternalAuthorName.equals(""))) {
			hasExternalAuthorName_string = "	 :hasExternalAuthorName \"" + hasExternalAuthorName + "\" ; ";
		}
		if (!(hasExternalAuthorPic == null) && (!hasExternalAuthorPic.equals(""))) {
			hasExternalAuthorPic_string = "	 :hasExternalAuthorPic \"" + hasExternalAuthorPic + "\" ; ";
		}
		if (!(hasExternalAuthorLink == null) && (!hasExternalAuthorLink.equals(""))) {
			hasExternalAuthorLink_string = "	 :hasExternalAuthorLink \"" + hasExternalAuthorLink + "\" ; ";
		}
		if (!(hasRanking == null) && (!hasRanking.equals(""))) {
			hasRanking_string = "	 :hasRanking \"" + hasRanking + "\"^^xsd:int ; ";
		}
		if (!(hasReview == null) && (!hasReview.equals(""))) {
			hasReview_string = "	 :hasReview \"" + hasReview + "\" ; ";
		}

		if (!(hasParentPOI == null) && (!hasParentPOI.equals(""))) {
			hasParentPOI_string = "	 :hasParentPOI " + hasParentPOI + " ; ";
		}
		//////////////////
		if (!(hasLongitude == null) && (!hasLongitude.equals(""))) {
			hasLongitude_string = "	 :hasLongitude \"" + hasLongitude + "\" ;";
		}
		if (!(hasLatitude == null) && (!hasLatitude.equals(""))) {
			hasLatitude_string = "	 :hasLatitude \"" + hasLatitude + "\" ;";
		}
		if (!(hasAltitude == null) && (!hasAltitude.equals(""))) {
			hasAltitude_string = "	 :hasAltitude \"" + hasAltitude + "\" ;";
		}
		if (!(hasRadius == null) && (!hasRadius.equals(""))) {
			hasRadius_string = "	 :hasRadius \"" + hasRadius + "\" ;";
		}

		if (!(hasParentlocation == null) && (!hasParentlocation.equals(""))) {
			hasParentLocation_string = "	 :hasParentLocation :" + hasParentlocation + " ;";
		}

		//////////
		if (!(hasExternalPage == null) && (!hasExternalPage.equals(""))) {
			hasExternalPage_string = "	 :hasExternalPage \"" + hasExternalPage + "\" ; ";
		}

		if (!(hasDescription == null) && (!hasDescription.equals(""))) {
			hasDescription_string = "	 :hasDescription \"" + hasDescription + "\" ;";
		}

		if (!(hasWikipediaArticle == null) && (!hasWikipediaArticle.equals(""))) {
			hasWikipediaArticle_string = "	 :hasWikipediaArticle \"" + hasWikipediaArticle + "\" ;";
		}

		if (!(hasPOICategory == null) && (!hasPOICategory.equals(""))) {
			hasPOICategory_string = "	 :hasPOICategory " + ":cat_" + hasPOICategory + " ;";
		}

		String result = "{\"Status\":\"FAIL\"}";
		// String delimiter = "\\,";
		String name_norm = "poi_" + hasTerm.toLowerCase().trim().replace(" ", "_").replaceAll("[^a-zA-Z0-9_]", "");
		// name_norm=name_norm.replace(" ", "_");
		String term = name_norm + "_t";
		// String[] keywords_array;
		// keywords_array = keywords.split(delimiter);
		Repository myRepository = new HTTPRepository(springlesserverURL, "toolisse");
		try {
			myRepository.initialize();
			RepositoryConnection con = myRepository.getConnection();

			// result= result+""+con.size();
			try {

				String queryString = "";

				ObjectMapper mapper = new ObjectMapper();
				// try {
				// Kbelement kbelement= mapper.readValue(rdfPayload,Kbelement.class);

				String queryStringPrefix2 = "PREFIX :<http://dkm.fbk.eu/trentour#>\n"
						+ "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n"
						+ "PREFIX owl2xml:<http://www.w3.org/2006/12/owl2-xml#>\n"
						+ "PREFIX trentour:<http://dkm.fbk.eu/trentour#>\n"
						+ "PREFIX test:<http://dkm.fbk.eu/trentour/test#>\n"
						+ "PREFIX owl:<http://www.w3.org/2002/07/owl#>\n"
						+ "PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n"
						+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
						+ "PREFIX ckr: <http://dkm.fbk.eu/ckr/meta#>\n" + "PREFIX meta:<http://dkm.fbk.eu/ckr/meta#>\n";
				queryString = queryStringPrefix2

						+ " INSERT DATA { " + "	 GRAPH meta:global { :" + name_norm + " a :POI ;" + hasAddress_string
						+ hasContactName_string + hasPhoneNumber_string + hasFaxNumber_string + hasMailAddress_string
						+ hasTicketCost_string + hasOpeningHours_string + hasAccessibilityInfo_string
						+ hasCapacity_string + hasStatus_string + hasImage_string + hasVideo_string + hasSourceID_string
						+ hasTimeZone_string + hasSourceURL_string + hasExternalAuthorName_string
						+ hasExternalAuthorPic_string + hasExternalAuthorLink_string + hasRanking_string
						+ hasReview_string + hasParentPOI_string

						// Location attributes

						+ hasLongitude_string// R//
						+ hasLatitude_string// R//
						+ hasAltitude_string + hasRadius_string + hasParentLocation_string
						// KnowledgeElement attributes

						+ hasDescription_string// R//
						+ hasExternalPage_string + hasWikipediaArticle_string + hasPOICategory_string

						+ "	 :hasTerm :" + term + " ." + "	 :" + term + " a :Term ;" + "	  :hasText " + "	 \""
						+ hasTerm + "\"@it .}" + "	 }";
				con.begin();

			//	System.out.println(queryString);

				int i = 0;
				// conn.prepareUpdate(QueryLanguage.SPARQL, updateQuery);
				Update insert = con.prepareUpdate(QueryLanguage.SPARQL, queryString);
				insert.execute();
				con.commit();
				con.close();
				result = "{\"POIURI\":\"" + name_norm + "\"}";
				// tupleQuery.setIncludeInferred(true);
				// TupleQueryResult qresult = tupleQuery.evaluate();
				try {

				} finally {
					// qresult.close();

				}

			} finally {
				con.close();

			}

		} catch (OpenRDFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	
	@GET
	@Path("/mappingtabella")
	@Produces(MediaType.APPLICATION_JSON)
	public MappingTabella getMappingTabella(

			@QueryParam("tabella") String tabella
	// @QueryParam("springlesrepositoryID") String springlesrepositoryID
	) {

		// String springlesrepositoryID ="georeporter";
        MappingTabella mappingtabella =new MappingTabella();
 		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<Attributo> listaAttributi = new ArrayList<Attributo>();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ "select ?property ?range where { "
					+ "  ?property rdfs:domain/(owl:unionOf/rdf:rest*/rdf:first)* :" + tabella + " ."
					+ " ?property rdfs:range ?range " + "}";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value property = bindingSet.getValue("property");
				Value range = bindingSet.getValue("range");

				Attributo attributo = new Attributo();

				if (property != null) {
					attributo.setNome(property.stringValue());
					attributo.setMapping(property.stringValue());
				}

				if (range != null) {
					attributo.setTipo(range.stringValue());
				}

				listaAttributi.add(attributo);
			}
			Attributo nome =new Attributo();
			nome.setNome(tabella);
			nome.setMapping(tabella);
			mappingtabella.setIdTabella(nome);
			mappingtabella.setAttributi(listaAttributi);
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

		return mappingtabella;
	}
	
	
	public List<TipoConCoordinate> getListaTipoConCoordinate(

		String tipo, String relazione
			
			
	// @QueryParam("springlesrepositoryID") String springlesrepositoryID
	) {

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<TipoConCoordinate> listaTCC = new ArrayList<TipoConCoordinate>();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix 

			+"	SELECT   ?idTipo ?idIndirizzo ?longitudine ?latitudine WHERE {"     
			+"    ?idTipo a :"+tipo+" .    "
			+"    ?idTipo :"+relazione+" ?idIndirizzo . "
			+"    ?idIndirizzo :coordinateLong ?longitudine . "
			+"    ?idIndirizzo :coordinateLat ?latitudine "
			+ "} ";

			System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value idTipo = bindingSet.getValue("idTipo");
				Value idIndirizzo = bindingSet.getValue("idIndirizzo");
				Value longitudine = bindingSet.getValue("longitudine");
				Value latitudine = bindingSet.getValue("latitudine");
				

				TipoConCoordinate tcc = new TipoConCoordinate();

				if (idTipo != null) {
					tcc.setIdTipo(idTipo.stringValue());
				}

				if (idIndirizzo != null) {
					tcc.setIdIndirizzo(idIndirizzo.stringValue());
				}

				if (longitudine != null) {
					tcc.setLongitude(new Float(longitudine.stringValue()));
				}
				
				
				if (latitudine != null) {
					tcc.setLatitude(new Float(latitudine.stringValue()));
				}
				
						
				
				listaTCC.add(tcc);
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

		return listaTCC;
	}

	

	public List<UIConCoordinate> getListaUIConCoordinate(

			
				
				
		// @QueryParam("springlesrepositoryID") String springlesrepositoryID
		) {

			// String springlesrepositoryID ="georeporter";

			List<BindingSet> tuples = new ArrayList<BindingSet>();
			List<UIConCoordinate> listaUCC = new ArrayList<UIConCoordinate>();

			Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
			try {
				myRepository.initialize();

				RepositoryConnection connection = myRepository.getConnection();

				String queryString = queryStringPrefix 

				+"	SELECT   ?idic ?idui ?idIndirizzo ?longitudine ?latitudine WHERE {"     
				+"    ?idic a :IdentificativoCatastale .  "
				+"    ?idic :hasUnitaImmobiliare ?idui . "
				+"    ?idui :hasIndirizzo ?idIndirizzo ."
				+"    ?idIndirizzo :coordinateLong ?longitudine . "
				+"    ?idIndirizzo :coordinateLat ?latitudine "
				+ "} ";


				
				System.out.println(queryString);
				TupleQuery tupleQuery;

				int i = 0;

				tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

				TupleQueryResult qresult = tupleQuery.evaluate();

				while (qresult.hasNext()) {
					BindingSet bindingSet = qresult.next();

					Value idic = bindingSet.getValue("idic");
					Value idui = bindingSet.getValue("idui");
					Value idIndirizzo = bindingSet.getValue("idIndirizzo");
					Value longitudine = bindingSet.getValue("longitudine");
					Value latitudine = bindingSet.getValue("latitudine");
					

					UIConCoordinate tcc = new UIConCoordinate();

					if (idic != null) {
						tcc.setIdIC(idic.stringValue());
					}
					if (idui != null) {
						tcc.setIdUI(idui.stringValue());
					}
					if (idIndirizzo != null) {
						tcc.setIdIndirizzo(idIndirizzo.stringValue());
					}

					if (longitudine != null) {
						tcc.setLongitude(new Float(longitudine.stringValue()));
					}
					
					
					if (latitudine != null) {
						tcc.setLatitude(new Float(latitudine.stringValue()));
					}
						
					
					listaUCC.add(tcc);
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

			return listaUCC;
		}


	
	
	
	
	
	@GET
	@Path("/attributitabella")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Attributo> getAttributiTabella(

			@QueryParam("tabella") String tabella
	// @QueryParam("springlesrepositoryID") String springlesrepositoryID
	) {

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<Attributo> listaAttributi = new ArrayList<Attributo>();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ "select ?property ?range where { "
					+ "  ?property rdfs:domain/(owl:unionOf/rdf:rest*/rdf:first)* :" + tabella + " ."
					+ " ?property rdfs:range ?range " + "}";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value property = bindingSet.getValue("property");
				Value range = bindingSet.getValue("range");

				Attributo attributo = new Attributo();

				if (property != null) {
					attributo.setNome(property.stringValue());
				}

				if (range != null) {
					attributo.setTipo(range.stringValue());
				}

				listaAttributi.add(attributo);
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

		return listaAttributi;
	}

	

	
	
	
	
	
	
	
	
	
	
	
	
	@GET
	@Path("/attributientita")
	@Produces({ "application/javascript" })
	public JSONWithPadding searchDatatypeProperties(
			@QueryParam("callback") String callback ,
			@QueryParam("entita") String idEntita
	// @QueryParam("springlesrepositoryID") String springlesrepositoryID
	) {

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		Entita entita = new Entita();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

				//	+ "select ?property ?domain ?range where { "
				//	+ "  ?property rdfs:domain/(owl:unionOf/rdf:rest*/rdf:first)* :" + entita + " ."
				//	+ " ?property rdfs:range ?range " + "}";

					
					
			//+ "			SELECT DISTINCT ?domain ?property  ?range  ?label"
			//+ "		WHERE { "
			//+ "		  ?property a owl:DatatypeProperty ;"
			//+ "	            rdfs:domain/(owl:unionOf/rdf:rest*/rdf:first)* :" + idEntita + ";"
			//+ "	            rdfs:range ?range ;"
		    //+ "             rdfs:label ?label ."
			////+ "			  FILTER (!isBlank(?domain))"
			//+ "	}";
			
			
			
			+ "			SELECT DISTINCT ?domain ?property  ?range  ?label "
			+ "		WHERE { "
			+ "		  ?property a owl:DatatypeProperty ;"
			+ "	            rdfs:domain/(owl:unionOf/rdf:rest*/rdf:first)* ?y;"
			+ "	            rdfs:range ?range ;"
		   + "             rdfs:label ?label ."
			//+ "			  FILTER (!isBlank(?domain))"
			+ "	            :" + idEntita + " rdfs:subClassOf* ?y ."
			
			+ "	}";
			
			
			
			
			
			
			
			
			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();
			
			entita.setIdEntita(idEntita);
			List<AttributoEntita> listaAttributiEntita= new ArrayList<AttributoEntita>();
			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();
				Value label = bindingSet.getValue("label");
				Value property = bindingSet.getValue("property");
				Value range = bindingSet.getValue("range");

				AttributoEntita attributoEntita =new AttributoEntita();

				if (property != null) {
				
					attributoEntita.setId(property.stringValue().substring(property.stringValue().lastIndexOf('#') + 1));
					attributoEntita.setField(property.stringValue().substring(property.stringValue().lastIndexOf('#') + 1));
					attributoEntita.setLabel(property.stringValue().substring(property.stringValue().lastIndexOf('#') + 1));
				}
				if (label != null) {
					
			
					attributoEntita.setLabel(label.stringValue().substring(label.stringValue().lastIndexOf('#') + 1));
				}
				if (range != null) {
					attributoEntita.setType(range.stringValue().substring(range.stringValue().lastIndexOf('#') + 1));
				}

				listaAttributiEntita.add(attributoEntita);
			}
			entita.setListaAttributiEntita(listaAttributiEntita);
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
		return new JSONWithPadding(new GenericEntity<Entita>(entita) {
		}, callback);
	
	}

	
	
	
	
	@GET
	@Path("/query")
	@Produces({ "application/javascript" })
	public JSONWithPadding querybuilder(
			@QueryParam("callback") String callback ,
			@QueryParam("json") String json_query
	// @QueryParam("springlesrepositoryID") String springlesrepositoryID
	) {
	
	
	
String json=
	"{         "
	+"	  \"condition\": \"AND\",  "
	+"	  \"rules\": [    "
	+"	    {"
	+"	      \"id\": \"mqCatastali\","
	+"	      \"field\": \"mqCatastali\","
	+"	      \"type\": \"integer\","
	+"	      \"input\": \"number\","
	+"	      \"operator\": \"equal\","
	+"	      \"value\": \"1000\""
	+"	    },"
	+"	    {"
	+"	      \"id\": \"mqLocaliPertinenza\","
	+"	      \"field\": \"mqLocaliPertinenza\","
	+"	      \"type\": \"integer\","
	+"	      \"input\": \"number\","
	+"	      \"operator\": \"equal\","
	+"	      \"value\": \"50\""
	+"	    }"
	+"	  ],"
	+"	  \"not\": false,"
	+"	  \"valid\": true,"
	+"     \"tablename\": UtenzaRifiuti, "
	+"     \"fields\":[{\"id\", \"cod\", \"cog\"}]              "
	
	
	+"	}";
	








	
Gson gson = new Gson();



	QueryJson data = gson.fromJson(json, QueryJson.class);

	
	
	return new JSONWithPadding(new GenericEntity<QueryJson>(data) {
	}, callback);

}
	
	
	
	
	
	@GET
	@Path("/relazionientita")
	@Produces({ "application/javascript" })
	public JSONWithPadding searchObjectProperties(
			@QueryParam("callback") String callback ,
			@QueryParam("entita") String idEntita
	// @QueryParam("springlesrepositoryID") String springlesrepositoryID
	) {

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		Entita entita = new Entita();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

				//	+ "select ?property ?domain ?range where { "
				//	+ "  ?property rdfs:domain/(owl:unionOf/rdf:rest*/rdf:first)* :" + entita + " ."
				//	+ " ?property rdfs:range ?range " + "}";

					
					
					
					
					
					
					
			+ "			SELECT DISTINCT ?domain ?property  ?range  ?label ?rangelabel"
			+ "		WHERE { "
			+ "		  ?property a owl:ObjectProperty ;"
			+ "	            rdfs:domain/(owl:unionOf/rdf:rest*/rdf:first)* ?y;"
	
			+ "	            rdfs:range ?range ;"
		   + "             rdfs:label ?label ."
		    + "            ?range rdfs:label ?rangelabel ."
			//+ "			  FILTER (!isBlank(?domain))"
			+ "	            :" + idEntita + " rdfs:subClassOf* ?y ."
			+ "	}";
			
			
		//	System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();
			
			entita.setIdEntita(idEntita);
			List<RelazioneEntita> listaRelazioniEntita= new ArrayList<RelazioneEntita>();
			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();
				Value label = bindingSet.getValue("label");
				Value property = bindingSet.getValue("property");
				Value range = bindingSet.getValue("range");
				Value rangelabel = bindingSet.getValue("rangelabel");

				RelazioneEntita relazioneEntita =new RelazioneEntita();

				if (property != null) {
				
					relazioneEntita.setId(property.stringValue().substring(property.stringValue().lastIndexOf('#') + 1));
					relazioneEntita.setField(property.stringValue().substring(property.stringValue().lastIndexOf('#') + 1));
					relazioneEntita.setLabel(property.stringValue().substring(property.stringValue().lastIndexOf('#') + 1));
				}
				if (label != null) {
					
			
					relazioneEntita.setLabel(label.stringValue().substring(label.stringValue().lastIndexOf('#') + 1));
				}
				if (range != null) {
					
					Entita entitarange = new Entita();
					entitarange.setIdEntita(range.stringValue().substring(range.stringValue().lastIndexOf('#') + 1));
					entitarange.setField(range.stringValue().substring(range.stringValue().lastIndexOf('#') + 1));
					entitarange.setLabel(range.stringValue().substring(range.stringValue().lastIndexOf('#') + 1));
					if (rangelabel!=null) {
						entitarange.setLabel(rangelabel.stringValue().substring(rangelabel.stringValue().lastIndexOf('#') + 1));
					}
					relazioneEntita.setEntitarange(entitarange);
				}

				listaRelazioniEntita.add(relazioneEntita);
			}
			entita.setListaRelazioniEntita(listaRelazioniEntita);
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
		return new JSONWithPadding(new GenericEntity<Entita>(entita) {
		}, callback);
	
	}

	
	
	/**
	 * Metodo che restituisce la gerarchia delle classi che compongono l’ontologia in formato JSON
	 *  
	 * url : http://localhost:8080/GeoreporterService/rest/gerarchiaclassi
	 * @param 
	 * @return List<GerarchiaClasse>
	 */
	
	
	@GET
	@Path("/gerarchiaclassi")
	@Produces({ "application/javascript" })
	public JSONWithPadding getGerarchiaClass(
			@QueryParam("callback") String callback 
	
	// @QueryParam("springlesrepositoryID") String springlesrepositoryID
	) {

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<GerarchiaClasse> listaClassi= new ArrayList<GerarchiaClasse>();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

				//	+ "select ?property ?domain ?range where { "
				//	+ "  ?property rdfs:domain/(owl:unionOf/rdf:rest*/rdf:first)* :" + entita + " ."
				//	+ " ?property rdfs:range ?range " + "}";

					
					
			+ "				SELECT DISTINCT ?class ?label ?superclass "
			+ "				WHERE {  "
			+ "				  ?class a owl:Class ; "
			+ "					    rdfs:label ?label . "
			+ "				  OPTIONAL {?class rdfs:subClassOf ?superclass . } "
			+ "				  FILTER (!isBlank(?class)) "
			+ "				} ";
					
	
			
			
			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();
			
			
			
			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();
				Value label = bindingSet.getValue("label");
				Value classe = bindingSet.getValue("class");
				Value superclasse = bindingSet.getValue("superclass");
				

				GerarchiaClasse gerarchiaClasse =new GerarchiaClasse();

				if (classe != null) {
				
					gerarchiaClasse.setIdclasse(classe.stringValue().substring(classe.stringValue().lastIndexOf('#') + 1));
					gerarchiaClasse.setField(classe.stringValue().substring(classe.stringValue().lastIndexOf('#') + 1));
					gerarchiaClasse.setLabel(classe.stringValue().substring(classe.stringValue().lastIndexOf('#') + 1));
				}
				if (label != null) {
					
			
					gerarchiaClasse.setLabel(label.stringValue().substring(label.stringValue().lastIndexOf('#') + 1));
				}
				if (superclasse != null) {
					
					gerarchiaClasse.setIdsuperclasse(superclasse.stringValue().substring(superclasse.stringValue().lastIndexOf('#') + 1));
					
				}

				listaClassi.add(gerarchiaClasse);
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
		return new JSONWithPadding(new GenericEntity<List<GerarchiaClasse>>(listaClassi) {
		}, callback);
	
	}

	
	
	
	
	/**
	 * Metodo che normalizza gli indirizzi presenti nella Knowledge Base utilizzando il servizio di geodecoding di Google
	 * 
	 * 
	 * url : http://localhost:8080/GeoreporterService/rest/normalizzaindirizzi
	 * @param limit=”numero_di_indirizzi”
	 * @return void
	 */	

	@GET
	@Path("/normalizzaindirizzi")

	public  String normalizzaIndirizzi(
			@QueryParam("callback") String callback,
			@QueryParam("limit") Integer limit
	
	// @QueryParam("springlesrepositoryID") String springlesrepositoryID
	) {

		// String springlesrepositoryID ="georeporter";
		String returno="";	
		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<Indirizzo> listaClassi= new ArrayList<Indirizzo>();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		//System.out.println("Limit=" +limit);
		int limiteiterazioni=limit.intValue();
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

				//	+ "select ?property ?domain ?range where { "
				//	+ "  ?property rdfs:domain/(owl:unionOf/rdf:rest*/rdf:first)* :" + entita + " ."
				//	+ " ?property rdfs:range ?range " + "}";

					
					
			+ "	SELECT ?s ?p ?o WHERE {\r\n" + 
			"  ?s rdf:type :Indirizzo .\r\n" + 
			"  ?s ?p ?o .\r\n" + 
			"  ?p rdf:type owl:DatatypeProperty\r\n" + 
			"}";
					
	
			
			
		//	System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();
			
			Map <String,Map<String,String>> indirizzi_HM= new HashMap<String,Map<String,String>>();
			
			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();
				Value uri = bindingSet.getValue("s");
				Value proprieta = bindingSet.getValue("p");
				Value valore = bindingSet.getValue("o");
				
                String key="";
                String prop="";
                String val="";
				if (uri != null) {
				
				key=	uri.stringValue().substring(uri.stringValue().lastIndexOf('#') + 1);
					
				}
				if (proprieta != null) {
					
			
					prop=proprieta.stringValue().substring(proprieta.stringValue().lastIndexOf('#') + 1);
				}
				if (valore != null) {
					
				val=	valore.stringValue();
					
				}

				if (indirizzi_HM.containsKey(key)) {
					
					Map tmp_valori_HM=indirizzi_HM.get(key);
					
					tmp_valori_HM.put(prop, val);
				}else {
					Map <String,String> valori_HM = new HashMap<String,String>();
					valori_HM.put(prop, val);
					indirizzi_HM.put(key, valori_HM);
					
				}
			}
				
			
		
			for (String key : indirizzi_HM.keySet()) {
				Map<String,String> attributi_indirizzo = indirizzi_HM.get(key);			
				String stringa_indirizzo="";
				//System.out.println("INRIRIZZO NORMALIZZATO=="+attributi_indirizzo.get("indirizzoNormalizzato"));
				 
                if (attributi_indirizzo.get("indirizzoNormalizzato")==null||attributi_indirizzo.get("indirizzoNormalizzato").equals("")){
            	
        
                	  stringa_indirizzo = safeToString(attributi_indirizzo.get("indirizzoCompleto"));
                	  stringa_indirizzo = stringa_indirizzo + " " + safeToString(attributi_indirizzo.get("civico"));
                if 	 (stringa_indirizzo.equals("")) { 
                	 // System.out.println("Indirizzo completo ="+ stringa_indirizzo);
                  
            	stringa_indirizzo=safeToStringFrazione(attributi_indirizzo.get("viaFrazione"));
				stringa_indirizzo = stringa_indirizzo + " " + safeToString(attributi_indirizzo.get("civico"));
				stringa_indirizzo = stringa_indirizzo + safeToString(attributi_indirizzo.get("lettera"));
				stringa_indirizzo = stringa_indirizzo + " " + safeToString(attributi_indirizzo.get("localita"));
				stringa_indirizzo = stringa_indirizzo + "," + safeToString(attributi_indirizzo.get("cap"));
                  }
				stringa_indirizzo = stringa_indirizzo + " " + safeToString(getNomeComune(attributi_indirizzo.get("codiceAmministrativo")));
                  
                //  System.out.println("Stringa Indirizzo "+safeToString(stringa_indirizzo));	 
			
				
				try {
					Thread.sleep(1100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				
				 if (limiteiterazioni>0) {
					
						
				
		    	 Indirizzo indirizzoGeoDecoded= geoDecode(safeToStringFrazione(stringa_indirizzo));
						
				  limiteiterazioni--;
				 if(!indirizzoGeoDecoded.getIndirizzoCompleto().equals("NULLO")) {
				  RigaTabella rt= new RigaTabella();
				  rt.setNometabella("http://dkm.fbk.eu/georeporter#Indirizzo");
				  rt.setUririga("http://dkm.fbk.eu/georeporter#"+key);
				//  System.out.println("KEY="+key);
				  List<Attributo> listaattributi= new ArrayList<Attributo>();
				  Attributo latitudine =new Attributo();
				  Attributo longitudine =new Attributo();
				  Attributo indirizzoNormalizzato =new Attributo();
				  
				  latitudine.setMapping("http://dkm.fbk.eu/georeporter#coordinateLat");
				  latitudine.setValore(String.valueOf(indirizzoGeoDecoded.getCoordinateLat()));
				  latitudine.setTipo("http://www.w3.org/2001/XMLSchema#float");
				  
				  longitudine.setMapping("http://dkm.fbk.eu/georeporter#coordinateLong");
				  longitudine.setValore(String.valueOf(indirizzoGeoDecoded.getCoordinateLong()));
				  longitudine.setTipo("http://www.w3.org/2001/XMLSchema#float");
				  indirizzoNormalizzato.setMapping("http://dkm.fbk.eu/georeporter#indirizzoNormalizzato");
				  indirizzoNormalizzato.setValore(indirizzoGeoDecoded.getIndirizzoCompleto());
				  indirizzoNormalizzato.setTipo("http://www.w3.org/2001/XMLSchema#string");
				  
				  listaattributi.add(latitudine);
				  listaattributi.add(longitudine);
				  listaattributi.add(indirizzoNormalizzato);
				  rt.setListaattributi(listaattributi);
				  insertTable(rt);
				 }
				  }else {
				         break;
					  }
				
                }
			
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
		

		
		return returno;
	
		
	}
	
	
	
	
	@GET
	@Path("/creatabellaindirizzi")

	public  void creaTabellaIndirizzi(
			@QueryParam("callback") String callback
		
	
	// @QueryParam("springlesrepositoryID") String springlesrepositoryID
	) {

		// String springlesrepositoryID ="georeporter";
		String returno="";	
		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<Indirizzo> listaClassi= new ArrayList<Indirizzo>();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		//System.out.println("Limit=" +limit);
		
		
			try {
				myRepository.initialize();
		
			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

				//	+ "select ?property ?domain ?range where { "
				//	+ "  ?property rdfs:domain/(owl:unionOf/rdf:rest*/rdf:first)* :" + entita + " ."
				//	+ " ?property rdfs:range ?range " + "}";

					
					
			+ "	SELECT ?s ?p ?o WHERE {\r\n" + 
			"  ?s rdf:type :Indirizzo .\r\n" + 
			"  ?s ?p ?o .\r\n" + 
			"  ?p rdf:type owl:DatatypeProperty\r\n" + 
			"}";
					
	
			
			
		//	System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();
			
			Map <String,Map<String,String>> indirizzi_HM= new HashMap<String,Map<String,String>>();
			
			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();
				Value uri = bindingSet.getValue("s");
				Value proprieta = bindingSet.getValue("p");
				Value valore = bindingSet.getValue("o");
				
                String key="";
                String prop="";
                String val="";
				if (uri != null) {
				
				key=	uri.stringValue().substring(uri.stringValue().lastIndexOf('#') + 1);
					
				}
				if (proprieta != null) {
					
			
					prop=proprieta.stringValue().substring(proprieta.stringValue().lastIndexOf('#') + 1);
				}
				if (valore != null) {
					
				val=	valore.stringValue();
					
				}

				if (indirizzi_HM.containsKey(key)) {
					
					Map tmp_valori_HM=indirizzi_HM.get(key);
					
					tmp_valori_HM.put(prop, val);
				}else {
					Map <String,String> valori_HM = new HashMap<String,String>();
					valori_HM.put(prop, val);
					indirizzi_HM.put(key, valori_HM);
					
				}
			}
				
			
		
			for (String key : indirizzi_HM.keySet()) {
				Map<String,String> attributi_indirizzo = indirizzi_HM.get(key);	
				
				Set<String> header = attributi_indirizzo.keySet();
			//scrivi i titoli	
			}
			for (String key : indirizzi_HM.keySet()) {
				Map<String,String> attributi_indirizzo = indirizzi_HM.get(key);	
				
				
				String stringa_indirizzo="";
				//System.out.println("INRIRIZZO NORMALIZZATO=="+attributi_indirizzo.get("indirizzoNormalizzato"));
				 
              //  if (attributi_indirizzo.get("indirizzoNormalizzato")==null||attributi_indirizzo.get("indirizzoNormalizzato").equals("")){
            	
        
                	  stringa_indirizzo = safeToString(attributi_indirizzo.get("indirizzoCompleto"));
                	  stringa_indirizzo = stringa_indirizzo + " " + safeToString(attributi_indirizzo.get("civico"));
                if 	 (stringa_indirizzo.equals("")) { 
                	 // System.out.println("Indirizzo completo ="+ stringa_indirizzo);
                  
            	stringa_indirizzo=safeToStringFrazione(attributi_indirizzo.get("viaFrazione"));
				stringa_indirizzo = stringa_indirizzo + " " + safeToString(attributi_indirizzo.get("civico"));
				stringa_indirizzo = stringa_indirizzo + safeToString(attributi_indirizzo.get("lettera"));
				stringa_indirizzo = stringa_indirizzo + " " + safeToString(attributi_indirizzo.get("localita"));
				stringa_indirizzo = stringa_indirizzo + "," + safeToString(attributi_indirizzo.get("cap"));
                  }
				stringa_indirizzo = stringa_indirizzo + " " + safeToString(getNomeComune(attributi_indirizzo.get("codiceAmministrativo")));
                  
                //  System.out.println("Stringa Indirizzo "+safeToString(stringa_indirizzo));	 
			
				
				try {
					Thread.sleep(1100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}				
				
			} catch (RepositoryException | MalformedQueryException | QueryEvaluationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	
			
						
						
				
		   //	
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Servizio che restituisce l’Identificativo Catastale dell’unità immobiliare che si trova più vicina. Il sistema confronta le coordinate (latitudine e longitudine) degli indirizzi che sono stati normalizzati con il servizio offerto da Google.
	 * 
	 * 
	 * url : http://localhost:8080/GeoreporterService/rest/cercaidcatastalevicino
	 * @param tipo=“nome_tabella”
     * @param relazione=”nome_relazione_indirizzo”
	 * @return void
	 */	
	@GET
	@Path("/cercaidcatastalevicino")

	public  String cercaidcatastalevicino(
			@QueryParam("callback") String callback,
			@QueryParam("tipo") String tipo,
			@QueryParam("relazione") String relazione
	
	// @QueryParam("springlesrepositoryID") String springlesrepositoryID
	) {

		// String springlesrepositoryID ="georeporter";
		String returno="";	
		
		List<TipoConCoordinate> listaTipoConCoordinate=getListaTipoConCoordinate(tipo, relazione);
		List<UIConCoordinate> listaUIConCoordinate=getListaUIConCoordinate();
			
		
		for (TipoConCoordinate tipoConCoordinate : listaTipoConCoordinate) {
			System.out.println(tipoConCoordinate.getIdIndirizzo());
			System.out.println(tipoConCoordinate.getIdTipo());
			System.out.println(tipoConCoordinate.getLatitude());
			System.out.println(tipoConCoordinate.getLongitude());
			
			
			
			String icConDistanzaMinima=getICCondistanzaMinima(tipoConCoordinate,listaUIConCoordinate);
			
			 RigaTabella rigaTFL= new RigaTabella();
			 rigaTFL.setNometabella("http://dkm.fbk.eu/georeporter#"+tipo);
			 rigaTFL.setUririga(tipoConCoordinate.getIdTipo());
			  List<Relazione> listRelCFL = new ArrayList<Relazione>();
			  Relazione relCFLIDECAT = new Relazione();
				relCFLIDECAT.setNomerelazione("http://dkm.fbk.eu/georeporter#hasPossibileIdentificativoCatastale");
				relCFLIDECAT.setUriDomain(tipoConCoordinate.getIdTipo());
				relCFLIDECAT.setUriRange(icConDistanzaMinima);
				listRelCFL.add(relCFLIDECAT);
				rigaTFL.setListarelazioni(listRelCFL);
                insertTable(rigaTFL);
			System.out.println(icConDistanzaMinima);
		}
		return returno;
	
		
	}
	
	
	private String getICCondistanzaMinima(TipoConCoordinate tipoConCoordinate , List<UIConCoordinate> listaUIConCoordinate) {
		
		String result="";
		
		
		double distanzaminima= calcolaDistanzaMinimaFraCoordinate(tipoConCoordinate.getLatitude(),tipoConCoordinate.getLongitude(),listaUIConCoordinate.get(0).getLatitude(),listaUIConCoordinate.get(0).getLongitude());	
		 result= listaUIConCoordinate.get(0).getIdIC();
		for (UIConCoordinate uiConCoordinate : listaUIConCoordinate) {
			System.out.println("ListaUICoordinate"+uiConCoordinate.getLatitude());
			double distanzaminimatmp= calcolaDistanzaMinimaFraCoordinate(tipoConCoordinate.getLatitude(),tipoConCoordinate.getLongitude(),uiConCoordinate.getLatitude(),uiConCoordinate.getLongitude());
		
			System.out.println("distanzaminimatmp= "+distanzaminimatmp);
			if (distanzaminimatmp < distanzaminima){
			
			distanzaminima=distanzaminimatmp;
		    result= uiConCoordinate.getIdIC();
		}
		}
		return result;
		
	}
	
	
	
	double calcolaDistanzaMinimaFraCoordinate(double lat1, double long1,double lat2, double long2) {
		
		double result =-1;	
		
		result =Math.sqrt(
			    Math.pow(lat2-lat1, 2) +
			    Math.pow(long2-long1, 2)
			);
		
		return result;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	static String safeToString(Object obj) {
		 
		return   obj == null ? "" : obj.toString();
		
		
}
	
	
	static String safeToStringFrazione(Object obj) {
		 
				String result=  obj == null ? "" : obj.toString();
			//	System.out.println("Frazione via="+result);
				//if (result.contains("FR.")) {
					result=result.replace("FRAZ.", "FRAZIONE");
					result=result.replace("FR.", "FRAZIONE");
					result=result.replace("-", " ");
				//}
				return result;
		}
	private Indirizzo geoDecode(String indirizzo){	
		Geocoder geocoder;
		
		Indirizzo indirizzo_normalizzato= new Indirizzo();
	//	indirizzo_normalizzato.setIndirizzoCompleto(indirizzo);
	//	indirizzo_normalizzato.setCoordinateLat(Float.valueOf("2222.222"));
	//	indirizzo_normalizzato.setCoordinateLong(Float.valueOf("2222.222"));
		
		
			     GeoApiContext context = new GeoApiContext.Builder()
	        	    .apiKey("AIzaSyC135Qkw2a-S2R7MvGHkcz3aECrXuAk_z4")
	        	    .build();
	        	GeocodingResult[] results;
				try {
					results = GeocodingApi.geocode(context,
					   indirizzo).await();
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
		        //	System.out.println(gson.toJson(results[0].addressComponents));
					if (results.length >0) {
					indirizzo_normalizzato.setIndirizzoCompleto(gson.toJson(results[0].formattedAddress));
					indirizzo_normalizzato.setCoordinateLat(Float.valueOf(gson.toJson(results[0].geometry.location.lat)));
					indirizzo_normalizzato.setCoordinateLong(Float.valueOf(gson.toJson(results[0].geometry.location.lng)));
		        	
				//	System.out.println(gson.toJson(results[0].formattedAddress));
		        //	System.out.println(gson.toJson(results[0].geometry.location.lat));
		        //	System.out.println(gson.toJson(results[0].geometry.location.lng));
					}else {
						indirizzo_normalizzato.setIndirizzoCompleto("NULLO");
						//System.out.println("INDIRIZZO NON TROVATO="+indirizzo);
					}
				} catch (ApiException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	    return indirizzo_normalizzato;
	    
	 }
public String getNomeComune(String codComune) {
	
	//System.out.println("codicecomune="+ codComune);
	
	return Costanti.comuni_italiani.get(codComune);
	
	
}
	
/*	@GET
	@Path("/checkesiste_old")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String  getCheckEsiste_old(

			
			JSONObject input
	// @QueryParam("springlesrepositoryID") String springlesrepositoryID
	) {
String result="";
		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<Attributo> listaAttributi = new ArrayList<Attributo>();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		
		JSONArray chiave_array;
		String nometabella;
	
			try {
				nometabella = (String) input.get("nometabella");
				chiave_array = (JSONArray) input.getJSONArray(""
						+ "");
			
				
		
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();
			ValueFactory factory = myRepository.getValueFactory();
			
			
			String listaattributi =buidListaAttributi(chiave_array, factory);
			
			
			String queryString = queryStringPrefix

					+ "select ?uriid  where { "
					+ " ?uriid  a" + nometabella + " ."
					+ listaattributi + "}";

			System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();
			
			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value uriid= bindingSet.getValue("uriid");
			
				if (uriid!=null)
					result=uriid.stringValue();
				
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
		}	 catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return result;
	}
	
*/

	@GET
	@Path("/test")
	//@Produces({ "application/javascript" })
	@Produces(MediaType.APPLICATION_JSON)
	
	public void test(){
		
		Esiste esiste= esiste("L322627536","UnitaImmobiliare");
		
	//	System.out.println(esiste.checkEsiste("http://dkm.fbk.eu/georeporter#consistenza"));
		
		
		
	}
	
	
	
	
	
	@GET
	@Path("/exist")
	//@Produces({ "application/javascript" })
	@Produces(MediaType.APPLICATION_JSON)
	// public List<Soggetto> getAnagraficaSoggettoui(@QueryParam("ui") String ui) {

	//restituisce la lista degli attributi di un individuo di una classe
	public Esiste esiste(
			// @QueryParam("callback") String callback,
		 @QueryParam("uri") String uri,
		 @QueryParam("classe") String classe
		 ) {

		// String springlesrepositoryID ="georeporter";
   //     System.out.println(uri);
   //     System.out.println(classe);
        
		String uripart[] = uri.split("#");
		String classepart[] = classe.split("#");
		uri=uripart[1];
		classe=classepart[1];
		Esiste risposta=new Esiste();
		List<String> listaProprieta = new ArrayList<String>();
        String queryString="";
		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			 queryString = queryStringPrefix

			+"		SELECT DISTINCT ?p ?o " 
			+"				WHERE {"
			+"				         :"+uri+" rdf:type :"+classe+" ;" 
			+"				             ?p ?o       "
			+"				         filter ( ?p not in ( rdf:type ) )"
			+"				} ";

			
		//S	 queryString = String.format(queryString, uri,classe);
			
		//	System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value p = bindingSet.getValue("p");
			    String proprieta="";
				if (proprieta!=null) {
					proprieta=p.stringValue();
			        listaProprieta.add(proprieta);
			    }
			}
			risposta.setRisposta(listaProprieta);
			qresult.close();
			connection.close();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.err.println(queryString);

		} catch (MalformedQueryException e) {
			// TODO Auto-generated catch block
			//System.err.println(queryString);

			e.printStackTrace();
		} catch (QueryEvaluationException e) {
			// TODO Auto-generated catch block
			System.err.println("QueryEvaluationException");
			System.err.println(queryString);

			e.printStackTrace();
		} finally {

		}
		//return new JSONWithPadding(new GenericEntity<Risposta>(risposta) {
		//}, callback);
		return risposta;
		
	}
	
	@POST
	@Path("/checkesiste")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String  getCheckEsiste(

			
			RigaTabella input
	// @QueryParam("springlesrepositoryID") String springlesrepositoryID
	) {
String result="";
		// String springlesrepositoryID ="georeporter";

		//List<BindingSet> tuples = new ArrayList<BindingSet>();
		//List<Attributo> listaAttributi = new ArrayList<Attributo>();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		
		//JSONArray chiave_array;
		
	
			String	nometabella = input.getNometabella();
				
			
				
		
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();
			ValueFactory factory = myRepository.getValueFactory();
			
			
			String listaattributi =buidListaAttributi(input.getListachiave(), factory);
			
			
			String queryString = queryStringPrefix

					+ "select ?uriid  where { "
					+ " ?uriid  a <" + nometabella + "> ."
					+ listaattributi + "}";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();
			
			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value uriid= bindingSet.getValue("uriid");
			
				if (uriid!=null)
					result=uriid.stringValue();
				
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
	
		return "{"+result+"}";
	}

	
	
	
	
	
	@GET
	@Path("/numerouicategorie")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONWithPadding  getNumberCategorieUI(String callback) {
String result="";


Map<String, List<String>> categoriecatastali_hm= new HashMap<String,List<String>>();
List categoriaA = new ArrayList<String>();
categoriaA.add("A01");
categoriaA.add("A02");
categoriaA.add("A03");
categoriaA.add("A04");
categoriaA.add("A05");
categoriaA.add("A06");
categoriaA.add("A07");
categoriaA.add("A08");
categoriaA.add("A09");
categoriaA.add("A10");
categoriaA.add("A11");


List categoriaB = new ArrayList<String>();


categoriaB.add("B01");
categoriaB.add("B02");
categoriaB.add("B03");
categoriaB.add("B04");
categoriaB.add("B05");
categoriaB.add("B06");
categoriaB.add("B07");
categoriaB.add("B08");



List categoriaC = new ArrayList<String>();
categoriaC.add("C01");
categoriaC.add("C02");
categoriaC.add("C03");
categoriaC.add("C04");
categoriaC.add("C05");
categoriaC.add("C06");
categoriaC.add("C07");


List categoriaD = new ArrayList<String>();

categoriaD.add("D01");
categoriaD.add("D02");
categoriaD.add("D03");
categoriaD.add("D04");
categoriaD.add("D05");
categoriaD.add("D06");
categoriaD.add("D07");
categoriaD.add("D08");
categoriaD.add("D09");
categoriaD.add("D10");


List categoriaE = new ArrayList<String>();
categoriaE.add("E01");
categoriaE.add("E02");
categoriaE.add("E03");
categoriaE.add("E04");
categoriaE.add("E05");
categoriaE.add("E06");
categoriaE.add("E07");
categoriaE.add("E08");
categoriaE.add("E09");


List categoriaF = new ArrayList<String>();
categoriaF.add("F01");
categoriaF.add("F02");
categoriaF.add("F03");
categoriaF.add("F04");
categoriaF.add("F05");
categoriaF.add("F06");
categoriaF.add("F07");
categoriaF.add("F08");
categoriaF.add("F09");
categoriaF.add("F10");
categoriaF.add("F11");

categoriecatastali_hm.put("A", categoriaA);
categoriecatastali_hm.put("B", categoriaB);
categoriecatastali_hm.put("C", categoriaC);
categoriecatastali_hm.put("D", categoriaF);
categoriecatastali_hm.put("E", categoriaF);
categoriecatastali_hm.put("F", categoriaF);




		// String springlesrepositoryID ="georeporter";
		//List<BindingSet> tuples = new ArrayList<BindingSet>();
		//List<Attributo> listaAttributi = new ArrayList<Attributo>();
		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		
		//JSONArray chiave_array;
		Map<String,Integer> categorieresult_hm= new HashMap<String,Integer>();
		ReportStatistiche repstat= new ReportStatistiche();
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();
			ValueFactory factory = myRepository.getValueFactory();

			String sparql="SELECT (COUNT( DISTINCT ?ui) as ?pCount ) ?cat " + 
					" where{" + 
					"			  ?ui rdf:type :UnitaImmobiliare . " + 
					 
					"			  ?ui :categoria ?cat" + 
					"			}" +  
					" Group by ?cat" + 
					" ORDER BY ?cat";
					
			String queryString = queryStringPrefix +sparql;

					
			System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();
			
			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value count= bindingSet.getValue("pCount");
				Value category= bindingSet.getValue("cat");
		
				if (category!=null)
				   if (count!=null)
					 categorieresult_hm.put(category.stringValue(), new Integer(count.stringValue()));
				System.out.println("category= "+category);
				System.out.println("count= "+count);	
			}
				
				
			qresult.close();
			connection.close();
		
			List<Statistiche> statList = new ArrayList<Statistiche>();
			
			
			
			
			for (String categoria : categoriecatastali_hm.keySet()) {
				Statistiche stat= new Statistiche();   
			    List<String> sottocategoriecatastali = categoriecatastali_hm.get(categoria);
			  
			    stat.setCategoria(categoria);
			List<Subcategoria> substatList = new ArrayList<Subcategoria>(); 
			    for (String sottocategoriacatastale : sottocategoriecatastali) {
					Subcategoria subcatstat = new Subcategoria();
					subcatstat.setId(sottocategoriacatastale);
					subcatstat.setValore(categorieresult_hm.get(sottocategoriacatastale));				
			    substatList.add(subcatstat);
			}
			stat.setSubcategoria(substatList);
			
		statList.add(stat);	
		
		}
			repstat.setStatistiche(statList);			
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
	
		return new JSONWithPadding(new GenericEntity<ReportStatistiche>(repstat) {
		}, callback);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	@GET
	@Path("/statistica")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String  getStatistica(
	 @QueryParam("sparql") String sparql
	) {
String result="";
		// String springlesrepositoryID ="georeporter";

		//List<BindingSet> tuples = new ArrayList<BindingSet>();
		//List<Attributo> listaAttributi = new ArrayList<Attributo>();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		
		//JSONArray chiave_array;
		
		
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();
			ValueFactory factory = myRepository.getValueFactory();

			
			String queryString = queryStringPrefix +sparql;

					
			System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();
			
			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value count= bindingSet.getValue("pCount");
			
				if (count!=null)
					result=count.stringValue();
				
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
	
		return result;
	}

	
	
	@GET
	@Path("/report")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public JSONWithPadding  getReport(@QueryParam("callback") String callback)
	 {

ReportStatistiche report;
		

	Gson gson = new Gson();
	BufferedReader br = null;
	try {

	URL	fileurl = GeoreporterService.class.getClassLoader().getResource("./" + "reportstatistiche.json");
		 System.out.println("fileurl="+ fileurl);

		URLConnection yc;
		try {
			yc = fileurl.openConnection();
			br = new BufferedReader(new InputStreamReader(yc.getInputStream()));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		report = gson.fromJson(br, ReportStatistiche.class);
		String path =System.getenv("CATALINA_HOME");
		System.out.println("path=" +path);
		System.out.println("current dir=" +ProgramDirectoryUtilities.getProgramDirectory());
		
		
		if (report != null) {
			for (Statistiche r : report.getStatistiche()) {
				System.out.println(r.getCategoria());
				System.out.println(r.getSparql());
				r.setTotale(getStatistica(r.getSparql()));
				r.setSparql("");
				System.out.println(r.getTotale());
				
				for (Subcategoria subcategoria: r.getSubcategoria()) {
					
					
					subcategoria.setMancanti(getStatistica(subcategoria.getSparql()));
					subcategoria.setSparql("");
				}
			}
		}

	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	return new JSONWithPadding(new GenericEntity<ReportStatistiche>(report) {
	}, callback);


}





		

	
		
	
	
	@GET
	@Path("icdacampi")


	 @Consumes("application/json")
    @Produces("application/json")	
	
	
	
	public List<IDCatastale> getICFromFields(
			@QueryParam("codiceamministrativo") String codiceamministrativo,
			@QueryParam("comunecatastale") String comunecatastale,
			@QueryParam("numero") String numero,
			@QueryParam("denominatore") String denominatore,
			@QueryParam("subalterno") String subalterno
			
		// @QueryParam("springlesserverURL") String springlesserverURL,
		// @QueryParam("springlesrepositoryID") String springlesrepositoryID
		 ) {

		// String springlesrepositoryID ="georeporter";
		 List<IDCatastale> risultato = new  ArrayList<IDCatastale> ();
		String result="FAIL";

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ "SELECT ?ic  ?tpa WHERE { "
					+ "    ?ic a :IdentificativoCatastale ." 
					+ "    ?ic :codiceAmministrativo  \"" +codiceamministrativo +"\". "
			        + "    ?ic :numero  " +numero +" . "
			      //  + "    ?ic :denominatore  " +denominatore +" ."
			        + "    ?ic :hasParticella ?pa . " 
			     
			        + "    ?pa :tipoDiParticella ?tpa . ";
			       
			
			
			
			
			  if (comunecatastale==null||comunecatastale.equals("")) {   
		        	queryString=queryString  + "	 FILTER NOT EXISTS { ?ic :comunecatastale ?z  }. "	;
		        			
		        }else {
		        	
		      
		            queryString=queryString  + "     ?ic :comuneCatastale  \"" +comunecatastale +"\" .";
					      
		        }
		
			  if (denominatore==null||denominatore.equals("")) {   
		        	queryString=queryString  + "	 FILTER NOT EXISTS { ?ic :denominatore ?z  }. "	;
		        			
		        }else {
		        	
		      
		            queryString=queryString  + "     ?ic :denominatore  " +denominatore +" .";
					System.out.println("denominatore="+ denominatore);        
		        }  
			  
			  
			  if (subalterno==null||subalterno.equals("")) {   
			        	queryString=queryString  + "	 FILTER NOT EXISTS { ?ic :subalterno ?y  }. " ;	
			        		
			        }else {
			        	
			      
			            queryString=queryString  + "     ?ic :subalterno  \"" +subalterno +"\" . ";
			            System.out.println("subalterno="+ subalterno);              
			        
			        }
			 	
			        queryString=queryString +"}";
			        
			System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

		
		
			
			
			while (qresult.hasNext()) {
				IDCatastale idc= new IDCatastale();
				
				BindingSet bindingSet = qresult.next();
				Value icURI = bindingSet.getValue("ic");
				Value tipoParticella = bindingSet.getValue("tpa");
				
				
				if (icURI!=null) {
					
					idc.setId(icURI.stringValue());	
				}
				if (tipoParticella!=null) {
					
					idc.setTipoParticella(tipoParticella.stringValue());	
					}
				
				
				risultato.add(idc);
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

		return risultato;
	}
	

	
	@GET
	@Path("dettaglioparticella")


	 @Consumes("application/json")
    @Produces("application/json")	
	
	
	
	public Particella getDettaglioParticella(
			@QueryParam("idparticella") String idparticella
			
		// @QueryParam("springlesserverURL") String springlesserverURL,
		// @QueryParam("springlesrepositoryID") String springlesrepositoryID
		 ) {

		// String springlesrepositoryID ="georeporter";
		Particella risultato = new  Particella();
		String result="FAIL";

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ "SELECT  ?ca ?ccc ?numero ?denominatore ?foglio ?tipo WHERE { "
					+ "  :"+ idparticella +" a :Particella ;" 
					+ "     :codiceAmministrativo ?ca ;"
					+ "     :codiceComuneCatastale ?ccc ;"
			        + "     :numero  ?numero  ; "
			        + "     :tipoParticella ?tipo ;"
			        + "   OPTIONAL { :"+ idparticella + " :denominatore ?denominatore} ."
			        + "   OPTIONAL { :"+ idparticella + " :foglioMappa ?foglio }." ;
		        //    + " }";		 
			 	
			        queryString=queryString +"}";
			        
			System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();	
			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

			
				Value comunecatastale = bindingSet.getValue("ccc");
				Value codiceAmministrativo = bindingSet.getValue("ca");
				Value numero = bindingSet.getValue("numero");
				Value denominatore = bindingSet.getValue("denominatore");
				Value foglio = bindingSet.getValue("foglio");
				Value tipo = bindingSet.getValue("tipo");
			

				Particella particella = new Particella();

				if (codiceAmministrativo != null) {
					particella.setCodiceAmministrativo(codiceAmministrativo.stringValue());
				}

				if (comunecatastale != null) {
					particella.setComuneCatastale(comunecatastale.stringValue());
				}
				if (foglio != null) {
					particella.setFoglio(foglio.stringValue());
				}
				if (numero != null) {
					particella.setNumero(numero.stringValue());
				}
				if (denominatore != null) {
					particella.setDenominatore(denominatore.stringValue());
				}
				if (tipo != null) {
					particella.setTipo(tipo.stringValue());
				}
				
               risultato=particella;
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

		return risultato;
	}
	
	
	
	
	
	
	
	@GET
	@Path("icfromui")
	@Produces(MediaType.APPLICATION_JSON)
	public String getICFromUi(

			@QueryParam("ui") String ui
		// @QueryParam("springlesserverURL") String springlesserverURL,
		// @QueryParam("springlesrepositoryID") String springlesrepositoryID
		 ) {

		// String springlesrepositoryID ="georeporter";

		String result="FAIL";

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ "SELECT ?ic  WHERE { "
					+ "    ?ic a :IdentificativoCatastale ." 
					+ "    ?ic :hasUnitaImmobiliare  :"+ ui 

					

					+ " }  ";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				
				Value icURI = bindingSet.getValue("ic");
				result= icURI.stringValue();
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

		return result;
	}
	

	@GET
	@Path("icfrompar")
	@Produces(MediaType.APPLICATION_JSON)
	public String getICFromPar(

			@QueryParam("par") String par
		// @QueryParam("springlesserverURL") String springlesserverURL,
		// @QueryParam("springlesrepositoryID") String springlesrepositoryID
		 ) {

		// String springlesrepositoryID ="georeporter";

		String result="FAIL";

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ "SELECT ?ic  WHERE { "
					+ "    ?ic a :IdentificativoCatastale ." 
					+ "    ?ic :hasParticellaFondiaria  :"+ par
					+ " }  ";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				
				Value icURI = bindingSet.getValue("ic");
				result= icURI.stringValue();
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

		return result;
	}
	
	@GET
	@Path("icfromparid")
	@Produces(MediaType.APPLICATION_JSON)
	public String getICFromParID(

			@QueryParam("parid") String parid
		// @QueryParam("springlesserverURL") String springlesserverURL,
		// @QueryParam("springlesrepositoryID") String springlesrepositoryID
		 ) {

		// String springlesrepositoryID ="georeporter";

		String result="FAIL";

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+"SELECT  ?ic "  
					+"		WHERE { " 
					+"			{ ?ic a :IdentificativoCatastale . "
					+"		  ?ic :hasParticellaFondiaria  ?parf . "
					+"		    ?parf :identificativoParticella "+parid+"} "
					+"		 UNION{?ic a :IdentificativoCatastale . "
					+"							     ?ic :hasParticella  ?par . "
					+"		    ?par :identificativoParticella "+parid+" "
							  
					+"		  } "

					+"		}  ";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				
				Value icURI = bindingSet.getValue("ic");
				result= icURI.stringValue();
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

		return result;
	}
	
	
	
	
	
	@GET
	@Path("icfromIndirizzo")
	@Produces(MediaType.APPLICATION_JSON)
	public String getICFromIndirizzo(

			@QueryParam("tipo") String tipo,
			@QueryParam("relazione") String relazione
		// @QueryParam("springlesserverURL") String springlesserverURL,
		// @QueryParam("springlesrepositoryID") String springlesrepositoryID
		 ) {

		// String springlesrepositoryID ="georeporter";

		String result="FAIL";

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+"SELECT  ?ic "  
					+"		WHERE { " 
					+"			 ?ic a :IdentificativoCatastale . "
					+"		      ?ic :UnitaImmobiliare  ?ui . "
					+"		      ?ui :hasIndirizzo ?ind ."
					+"            ?ind latitudine ?lat ."
					+"            ?ind l ?lat ."
							  
					+"		  } "

					+"		  ";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				
				Value icURI = bindingSet.getValue("ic");
				result= icURI.stringValue();
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

		return result;
	}
	
	
	
	
	
	
	
	
	@GET
	@Path("idIndirizzodacoordinate")
	@Produces(MediaType.APPLICATION_JSON)
	public String getIDIndirizzoFromCoordinates(

			@QueryParam("latitudine") String latitudine,
			@QueryParam("longitudine") String longitudine
		// @QueryParam("springlesserverURL") String springlesserverURL,
		// @QueryParam("springlesrepositoryID") String springlesrepositoryID
		 ) {

		// String springlesrepositoryID ="georeporter";

		String result="FAIL";

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

					+ "SELECT ?id  WHERE { "
					+ "    ?id a :Indirizzo ." 
					+ "    ?id :coordinateLat  :"+ latitudine +"."
					+ "    ?id :coordinateLong  :"+ longitudine + "."
					+ " }  ";

			//System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();
				Value idURI = bindingSet.getValue("id");
				result= idURI.stringValue();
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

		return result;
	}
	
	
	@GET
	@Path("urisoggettodaid")
	@Produces(MediaType.APPLICATION_JSON)
	public String getUriSoggettoFromID(
		   @QueryParam("identificativoSoggetto") String idSoggetto
		// @QueryParam("springlesserverURL") String springlesserverURL,
		// @QueryParam("springlesrepositoryID") String springlesrepositoryID
		 ) {

		// String springlesrepositoryID ="georeporter";

		String result="FAIL";

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();
			//idSoggetto=idSoggetto.replaceAll("\t", "");
			idSoggetto = idSoggetto.replaceAll("(\\r|\\n|\\t)", "");
			String queryString = queryStringPrefix
					+ "Select Distinct ?id "
					+ "where{"
					+ "?id :identificativoSoggetto  "+idSoggetto +" }";
				//+"	  {?id a :PersonaGiuridica .}"
				//+"	  UNION {?id a :PersonaFisica .}"
				//+"	   UNION {?id a :ProprietarioProTempore .}"
		//	System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();
				
				Value idURI = bindingSet.getValue("id");
				if (!idURI.equals(null))
				result= idURI.stringValue();
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

		return result;
	}
	
	
	@GET
	@Path("/strutturaontologia")
	@Produces({ "application/javascript" })
	// @Produces(MediaType.APPLICATION_JSON)

	// public List<UtenzaAcqua> getUtenzeAcquaUI(
	public JSONWithPadding getStrutturaOntologia(@QueryParam("callback") String callback 
				// @QueryParam("particella") String particella,
	// @QueryParam("subalterno") String subalterno

	) {

	
		// String springlesserverURL = "http://localhost:8080/openrdf-sesame";

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		List<Relazione> listaRelazioni= new ArrayList<Relazione>();

		/// Soggetto soggetto =new Soggetto();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix

/*			+"		SELECT DISTINCT ?classe1 ?p ?classe2 WHERE {"
			+"	?classe1  a owl:Class . "
			+"	?classe2 a owl:Class .  "
				 
			+"	    ?p rdfs:domain ?classe1 . "
			+"	    ?p  rdfs:range ?classe2 .  "
			 
			+"	  FILTER NOT EXISTS  { ?classe1 rdfs:subClassOf ?classe2} " 
			+"	 FILTER (!isBlank(?classe1))} ";
*/
			
			
			
			
			
			+"		SELECT DISTINCT ?classe1 ?p ?classe2 ?label WHERE {"
			+"	?classe1 a owl:Class . "
			+"	?classe2 a owl:Class .  "
				 
		//	+"	    ?p rdfs:domain ?classe1 . "
			+ "?p  rdfs:domain/(owl:unionOf/rdf:rest*/rdf:first)* ?x."
			+ "?p  rdfs:range/(owl:unionOf/rdf:rest*/rdf:first)* ?y."
			+ " ?p rdfs:label ?label ."
			//+"	    ?p  rdfs:range ?classe2 .  "
			+"  ?classe1 rdfs:subClassOf* ?x ."	
			+"  ?classe2 rdfs:subClassOf* ?y ."	 
			+"	  FILTER NOT EXISTS  { ?x rdfs:subClassOf ?y} " 
			+"	  FILTER  NOT EXISTS  { ?z rdfs:subClassOf ?classe1} "
			+"	  FILTER  NOT EXISTS  { ?k rdfs:subClassOf ?classe2} " 
			+"	 FILTER (!isBlank(?classe1))}";
			
			
			
			
			
			
			
		//	System.out.println(queryString);
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();

			while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				Value classe1 = bindingSet.getValue("classe1");
				Value relazione = bindingSet.getValue("p");
				Value label = bindingSet.getValue("label");
				Value classe2 = bindingSet.getValue("classe2");
				

				Relazione rel = new Relazione();
				Relazione relinv = new Relazione();

				if (classe1 != null) {
				rel.setUriDomain(classe1.stringValue().substring(classe1.stringValue().lastIndexOf('#') + 1));
				relinv.setUriRange(classe1.stringValue().substring(classe1.stringValue().lastIndexOf('#') + 1));
				}
				
			
				if (classe2 != null) {
					rel.setUriRange((classe2.stringValue().substring(classe2.stringValue().lastIndexOf('#') + 1)));
					relinv.setUriDomain((classe2.stringValue().substring(classe2.stringValue().lastIndexOf('#') + 1)));
				}

				if (relazione != null) {
					rel.setNomerelazione((relazione.stringValue().substring(relazione.stringValue().lastIndexOf('#') + 1)));
					relinv.setNomerelazione(("inversa_"+relazione.stringValue().substring(relazione.stringValue().lastIndexOf('#') + 1)));
				
				}
				
				if (label != null) {
					rel.setLabel((relazione.stringValue()).substring(relazione.stringValue().lastIndexOf('#') + 1));
					relinv.setLabel( (relazione.stringValue().substring(relazione.stringValue().lastIndexOf('#') + 1).replaceFirst("has", "is")+"Of"));
				
				}
				
				
				
				
				

				listaRelazioni.add(rel);
				listaRelazioni.add(relinv);
			}
			// qresult.close();
			// connection.close();
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
		// return listaUtenzaAcqua;
		return new JSONWithPadding(new GenericEntity<List<Relazione>>(listaRelazioni) {
		}, callback);
	}
	
	
	
	@POST
	@Path("/sparqlquery2")
	//@Produces({ "application/javascript" })
	
	@Produces(MediaType.APPLICATION_JSON)

	// public List<UtenzaAcqua> getUtenzeAcquaUI(
	public JSONWithPadding sparqlQueryManager2(@QueryParam("callback") String callback,
			 QueryJson queryJson
	// @QueryParam("particella") String particella,
	// @QueryParam("subalterno") String subalterno

	) {



		// String springlesserverURL = "http://localhost:8080/openrdf-sesame";

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		
		QueryResult queryResult = new QueryResult();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix +queryJson.printString();
			
		
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();
             List<String> campiSelect = queryJson.getCampiSelect();
         	List<QueryResultRow> listQueryresultrow = new ArrayList <QueryResultRow>();
         			/// Soggetto soggetto =new Soggetto();
        
             while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

				
				QueryResultRow queryResultRow = new QueryResultRow();
				List<QueryResultItem> listQueryResultItems = new ArrayList<QueryResultItem>();
				for (String campo : campiSelect) {
					QueryResultItem resultItem= new QueryResultItem();
					Value value = bindingSet.getValue(campo);
					if (value!=null) {
						resultItem.setColonna(campo);
					    resultItem.setValore(value.stringValue());
					    listQueryResultItems.add(resultItem);  
					}
					
					queryResultRow.setListacelle(listQueryResultItems);
				}
				
				listQueryresultrow.add(queryResultRow);
			
			}
             
             queryResult.setRighe(listQueryresultrow);
			// qresult.close();
			// connection.close();
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
		// return listaUtenzaAcqua;
		return new JSONWithPadding(new GenericEntity<QueryResult>(queryResult) {
		}, callback);
	}	
	
	@POST
	@Path("/sparqlquery")
	@Produces({ "application/javascript" })
	
	//@Produces(MediaType.APPLICATION_JSON)

	// public List<UtenzaAcqua> getUtenzeAcquaUI(
	public JSONWithPadding sparqlQueryManager(@QueryParam("callback") String callback,
			 String jsonquery
	// @QueryParam("particella") String particella,
	// @QueryParam("subalterno") String subalterno

	) {
//System.out.println("data= "+jsonquery);
		Gson gson = new Gson();

		QueryJson queryJson = gson.fromJson(jsonquery, QueryJson.class);

		// String springlesserverURL = "http://localhost:8080/openrdf-sesame";

		// String springlesrepositoryID ="georeporter";

		List<BindingSet> tuples = new ArrayList<BindingSet>();
		
		QueryResult queryResult = new QueryResult();

		Repository myRepository = new HTTPRepository(springlesserverURL, springlesrepositoryID);
		try {
			myRepository.initialize();

			RepositoryConnection connection = myRepository.getConnection();

			String queryString = queryStringPrefix +queryJson.printString();
			
		
			TupleQuery tupleQuery;

			int i = 0;

			tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, queryString);

			TupleQueryResult qresult = tupleQuery.evaluate();
             List<String> campiSelect =new ArrayList<String>();
         	campiSelect.add("ui");
			campiSelect.add("pa");
			campiSelect.add("ic");
			campiSelect.addAll(queryJson.getCampiSelect());
			
			
             List<QueryResultRow> listQueryresultrow = new ArrayList <QueryResultRow>();
         			/// Soggetto soggetto =new Soggetto();
        
             while (qresult.hasNext()) {
				BindingSet bindingSet = qresult.next();

			
				QueryResultRow queryResultRow = new QueryResultRow();
				List<QueryResultItem> listQueryResultItems = new ArrayList<QueryResultItem>();
				for (String campo : campiSelect) {
					QueryResultItem resultItem= new QueryResultItem();
					Value value = bindingSet.getValue(campo);
					
					if (value!=null) {
						resultItem.setColonna(campo);
					    resultItem.setValore(value.stringValue().substring(value.stringValue().lastIndexOf('#') + 1));
					   
					    listQueryResultItems.add(resultItem);  
					}
					
					queryResultRow.setListacelle(listQueryResultItems);
				}
				
				listQueryresultrow.add(queryResultRow);
			
			}
             
             queryResult.setRighe(listQueryresultrow);
             
             
             queryResult.setColonne(campiSelect.toArray(new String[campiSelect.size()]));
			// qresult.close();
			// connection.close();
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
		// return listaUtenzaAcqua;
		return new JSONWithPadding(new GenericEntity<QueryResult>(queryResult) {
		}, callback);
	}	
	
	
	public static HashMap<String, String> jsonToMap(JSONObject jObject) throws JSONException {

		HashMap<String, String> map = new HashMap<String, String>();

		Iterator<?> keys = jObject.keys();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			String value = jObject.getString(key);
			map.put(key, value);

		}

		//System.out.println("json : " + jObject);
		//System.out.println("map : " + map);
		return map;
	}
	private String buidListaAttributi(List<Attributo> lista_attributi, ValueFactory factory)  {
		
		String result="";
		for (Attributo attr : lista_attributi) {
			//Value literal = new LiteralImpl(attr.getValore(),new URIImpl( attr.getTipo()));
			
			   result = result + "?uriid <"+attr.getMapping()+ ">  \""+attr.getValore() +"\"^^"+attr.getTipo().replace("http://www.w3.org/2001/XMLSchema#", "xsd:") +" . "; 	
		}
	
           return result;
		
		
	}

		
}
