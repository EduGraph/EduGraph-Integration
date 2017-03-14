package org.camunda.bpm.edugraph.extraction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class SPARQL implements JavaDelegate {
	public void execute(DelegateExecution execution) throws Exception {
		String uri = (String) execution.getVariable("enterURL");
		String triples = (String) execution.getVariable("extract");
		String query = "";
		String requestURL = "http://dbpedia.org/sparql";
		String result;
		
		try {
			result = getStringFromUrl(requestURL);			
			execution.setVariable("result", result);
		}
		catch (Exception e){
			result ="error:" + e;
		}	
		RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
		runtimeService.setVariable(execution.getId(), "endpoint", result);
		
		
		System.out.println();
		System.out.println("SPARQL-Endpoint");
		System.out.println(new Date(System.currentTimeMillis()));
		System.out.println(requestURL);
	}

	private String getStringFromUrl(String urlToReadFrom) throws Exception {
		URL url = new URL(urlToReadFrom);
	    
        URLConnection con = url.openConnection();
        final Reader reader = new InputStreamReader(con.getInputStream());
        final BufferedReader br = new BufferedReader(reader);        
        String line = "";
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {

        	sb.append(line);
        }        
        br.close();
        return sb.toString();
	}
	

}
