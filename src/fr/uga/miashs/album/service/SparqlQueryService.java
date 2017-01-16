package fr.uga.miashs.album.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;

public class SparqlQueryService {
	
	public static final String BASE_URL = "http://www.semanticweb.org/projetAlbum";
	public static final String NS = BASE_URL + "#";
	public static final String NS_PREFIX = "<" + NS + ">";
	public static final String RDFS = "<http://www.w3.org/2000/01/rdf-schema#>";
	public static final String FOAF = "<http://xmlns.com/foaf/0.1/>";
	public static final String DC = "<http://purl.org/dc/elements/1.1/>";
	public static final String SOURCE_TRIPLE_STORE = "http://localhost:3030/ALBUM/update";
	
	public List<String> getAllPictures(){
		String queryString = "SELECT ?p  WHERE {?p a ns:Picture .}";
		return getQuery(queryString);
	}
	
	public List<String> getWhatTagPicture(String strURI) {
		String queryString = "SELECT ?p WHERE { <" + strURI + "> ns:what ?p .}";
		System.out.println(queryString);
		return getQuery(queryString);
	}

	public List<String> getWhoTagPicture(String strURI) {
		String queryString = "SELECT ?p WHERE { <" + strURI + "> ns:who ?p .}";
		System.out.println(queryString);
		return getQuery(queryString);
	}
	
	public List<String> getWhenTagPicture(String strURI) {
		String queryString = "SELECT ?p WHERE { <" + strURI + "> ns:when ?p .}";
		return getQuery(queryString);
	}
	
	public List<String> getWhereTagPicture(String strURI) {
		String queryString = "SELECT ?p WHERE { <" + strURI + "> ns:where ?p .}";
		return getQuery(queryString);
	}
	
	public List<String> getQuery(String queryString) {
		Query query = QueryFactory.create(
				"PREFIX rdfs: " + RDFS + "\n" +
				"PREFIX foaf: " + FOAF + "\n" +
				"PREFIX ns: " + NS_PREFIX + "\n" +
				"PREFIX dc: " + DC + "\n" +
				queryString);
		
		List<String> resultList = new ArrayList<String>();
		
		  try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ALBUM/sparql",query)) {
		    ResultSet results = qexec.execSelect() ;
		    for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode x = soln.get("s") ;       // Get a result variable by name.
		      Resource r = soln.getResource("p") ; // Get a result variable - must be a resource
		      Literal l = soln.getLiteral("o") ;   // Get a result variable - must be a literal
		      System.out.println(x+" "+r+" "+l);
		      
		      if (r != null){
		    	  resultList.add(r.toString());
		      }
		    }
		    System.out.println(resultList);
		  }
		  
		  return resultList;
	}

	public List<String> getUnicorn() {
		String queryString = "SELECT ?p  WHERE {?p a ns:Picture ;"
								+ "ns:who ?who ."
								+ "?who a ns:Unicorn .}";
		
		return getQuery(queryString);
	}

	public List<String> getRoger() {
		String queryString = "SELECT ?p  WHERE {?p a ns:Picture ;"
								+ "ns:who ns:roger .}";

		return getQuery(queryString);
	}
	
	public List<String> getRogerAndBen() {	
		String queryString = "SELECT ?p  WHERE {?p a ns:Picture ;"
								+ "ns:who ns:roger ;"
								+ "ns:who ns:Ben .}";
	
		return getQuery(queryString);
	}

	public List<String> getPeople() {
		String queryString = "SELECT ?p  WHERE {?p a ns:Picture ;"
								+ "ns:who ?who ."
								+ "who a foaf:Person .}";

		return getQuery(queryString);
	}

	public List<String> getWithoutPeople() {
		String queryString = 
				"SELECT ?p  WHERE {?p a ns:Picture ."
				+ "OPTIONAL {"
					+ "?p ns:who ?who ."
					+ "FILTER (?who != foaf:Person)"
					+ "}";
		
		return getQuery(queryString);
	}

	public List<String> getSport() {
		String queryString = 
				"SELECT ?p  WHERE {"
					+ "{ ?p a ns:Picture ;"
						+ " ns:what ?what ."
						+ "?what a ns:Sport ."
						+ "}"
					+ "UNION "
					+ "{?p a ns:Picture ;"
						+ "ns:what ns:Sport ."
						+ "}"
				+ "}";
		
		return getQuery(queryString);
	}

	public List<String> getNature() {
		String queryString = 
				"SELECT ?p  WHERE {"
					+ "{ ?p a ns:Picture ;"
						+ " ns:what ?what ."
						+ "?what a ns:Nature ."
						+ "}"
					+ "UNION "
					+ "{?p a ns:Picture ;"
						+ "ns:what ns:Nature ."
						+ "}"
				+ "}";
		
		return getQuery(queryString);
	}

	public List<String> getLastYear() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}