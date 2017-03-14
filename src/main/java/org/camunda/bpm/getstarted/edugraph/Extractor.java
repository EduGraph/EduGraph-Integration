package org.camunda.bpm.getstarted.edugraph;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.Variables.SerializationDataFormats;
import org.camunda.bpm.engine.variable.value.ObjectValue;

public class Extractor implements JavaDelegate {
	public void execute(DelegateExecution execution) throws Exception {
		
		String uri = (String) execution.getVariable("enterURL");
		String requestURL = "http://rdf-translator.appspot.com/convert/json-ld/n3/" + uri;
		String result;
		ObjectValue r;
		RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
		
		try {
			result = getStringFromUrl(requestURL);
			r = Variables
			          .objectValue(result)
			              // tells the engine to use java serialization for persisting the value 
			          .serializationDataFormat(SerializationDataFormats.JAVA)  
			          .create();
			
			execution.setVariable("result", r);
			runtimeService.setVariable(execution.getId(), "extract", r);
			
		}
		catch (Exception e){
			
			result = "Error: " + e.getLocalizedMessage();
			execution.setVariable("result", result);
			runtimeService.setVariable(execution.getId(), "extractError", result);
			throw new BpmnError("ExtractError");
			
		}	

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