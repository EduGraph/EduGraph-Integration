package org.camunda.bpm.edugraph.extraction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

//import org.camunda.bpm.application.ProcessApplicationContext;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.Variables.SerializationDataFormats;
import org.camunda.bpm.engine.variable.value.ObjectValue;

public class RDFExtractor implements JavaDelegate {

	public void execute(DelegateExecution execution) throws Exception {
		
		//ProcessApplicationContext.setCurrentProcessApplication("EduGraphApplication");
		
		String uri = (String) execution.getVariable("enterURL");
		String requestURL = "http://rdf-translator.appspot.com/convert/detect/n3/" + uri;
		String result;
		ObjectValue r;
		//ManagementService managementService = execution.getProcessEngineServices().getManagementService();
		//EmbeddedProcessApplication processApplication = new EmbeddedProcessApplication();
	    //ProcessApplicationReference reference = processApplication.getReference();
		//managementService.registerProcessApplication(execution.getId(), reference);
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
		//ProcessApplicationContext.clear();

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
