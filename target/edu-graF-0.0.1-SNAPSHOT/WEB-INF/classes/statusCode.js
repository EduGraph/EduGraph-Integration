// fetch execution variables
var statusCode = connector.getVariable("statusCode");
var response = connector.getVariable("response");

if (statusCode == 403) {
	
    throw new org.camunda.bpm.engine.delegate.BpmnError("SaveTripleError");
	
}
