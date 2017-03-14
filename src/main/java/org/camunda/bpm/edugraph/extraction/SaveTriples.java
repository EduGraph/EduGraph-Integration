package org.camunda.bpm.edugraph.extraction;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.connect.Connectors;
import org.camunda.connect.httpclient.HttpConnector;



public class SaveTriples implements JavaDelegate {

	public void execute(DelegateExecution execution) throws Exception {
		ObjectValue extract = (ObjectValue) execution.getVariable("extract");
		String payload = (String) extract.getValue();
		String requestURL = "http://fbwsvcdev.fh-brandenburg.de:8080/fuseki/EduGraph-ESWC-extract/data";
		String result;
		RuntimeService runtimeService = execution.getProcessEngineServices().getRuntimeService();
		
		try{
		
		HttpConnector http = Connectors.getConnector("http-connector");
		http.createRequest().post().header("Content-Type", "text/turtle").url(requestURL).payload(payload).execute();
		
		}
		catch (Exception e){
			
			result = "Error: " + e.getLocalizedMessage();
			execution.setVariable("result", result);
			runtimeService.setVariable(execution.getId(), "storeError", result);
			throw new BpmnError("SaveTripleError");
			
		}
	
		
	}


}
