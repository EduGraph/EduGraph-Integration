package org.camunda.bpm.edugraph.extraction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;

public class SaveSubjectCategories {
	
	public void execute(DelegateExecution execution) throws Exception {
		
			ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtimeService = processEngine.getRuntimeService();
			
			String requestURL ="http://fbwsemantic.fh-brandenburg.de:9000/projects/extractor/enrichment.php";
			String result;
			
			try {
				result = getStringFromUrl(requestURL);
				runtimeService.setVariable(execution.getId(), "enrich", result);
			} catch (Exception e) {
				result = "error" + e.getLocalizedMessage();
				runtimeService.setVariable(execution.getId(), "enrichError", result);
				throw new BpmnError("EnrichError");
			}
			
			
			// Ausgabe auf der Tomcat-Console 
			System.out.println();
	 		System.out.println("Ergebnis Anreicherung: "+result);
			
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
