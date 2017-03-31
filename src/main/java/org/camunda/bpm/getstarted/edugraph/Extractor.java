package org.camunda.bpm.getstarted.edugraph;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.Variables.SerializationDataFormats;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class Extractor implements JavaDelegate {
	public void execute(DelegateExecution execution) throws Exception {
		
		String uri = (String) execution.getVariable("enterURL");
		String requestURL = "http://rdf-translator.appspot.com/convert/json-ld/n3/" + uri;
		String result;
		String msg;
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
			
			msg = getMessage(uri);
			result = msg + "\n" + e.getLocalizedMessage();
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
	
	private String getMessage(String urlEnter) throws Exception {
		String message = null;
		String service = "http://fbwsvcdev.th-brandenburg.de:8080/any23/any23/";
		String format = "turtle";
		String parms = "&fix=on&report=on";
		String any23URL = service + "?format=" + format + "&uri=" + urlEnter + parms;
		URL url = new URL(any23URL);
		
		HttpURLConnection http = (HttpURLConnection)url.openConnection();
		http.setRequestMethod("GET");
		int status = http.getResponseCode();
		String exception = http.getResponseMessage();
		
		if (status == 0) {
			message = "Not connect.\n Verify Network.";
		}else if (status >= 300) {
			InputStream errorstream = http.getErrorStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(errorstream));
			String line = "";
	        StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);		
				sb.append(System.getProperty("line.separator"));
			}
			String responseMessage = sb.toString();
		
			if (status == 400) {
				message = "Bad Request: Missing or malformed input parameter.";
			} else if (status == 404) {
				message = "Not Found: Malformed request IRI.";
			} else if (status == 406) {
				message = "Not Acceptable:  None of the media types specified in the Accept header are supported.";
			} else if (status == 415) {
				message = "Unsupported Media Type: Document body with unsupported media type was POSTed.";
			} else if (status == 501) {
				message = "Not Implemented:  Extraction from input was successful, but yielded zero triples.";
			} else if (status == 502) {
				message = "Bad Gateway: Input document from a remote server could not be fetched or parsed.";
				
				try {
			        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			        DocumentBuilder parser = factory.newDocumentBuilder();
			        ////Document dc= parser.parse(response.getEntity().getContent());
			        Document dc= parser.parse(new InputSource(new StringReader(responseMessage)));			      
			        String errorText = dc.getElementsByTagName("message").item(0).getTextContent();
			        String errorRaw = dc.getElementsByTagName("error").item(0).getTextContent();
			        String pattern = "([A-Z]+):[ \t]+'(.+)'";
			        Pattern r = Pattern.compile(pattern);
			        Matcher m = r.matcher(errorRaw);
			        List<String> error = new ArrayList<String>();
			        while (m.find()) {
                    	error.add(m.group());
                    }
			        message = message + "\n--------" + errorText + "--------" + "\n" + error;
                }
                catch(Exception err) {
                	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			        DocumentBuilder parser = factory.newDocumentBuilder();
			        Document dc= parser.parse(new InputSource(new StringReader(responseMessage + "</issueReport></report>")));		      
			        String errorText = dc.getElementsByTagName("message").item(0).getTextContent();
			        String errorRaw = dc.getElementsByTagName("error").item(0).getTextContent();
			        String pattern = "([A-Z]+):[ \t]+'(.+)'";
			        Pattern r = Pattern.compile(pattern);
			        Matcher m = r.matcher(errorRaw);
			        List<String> error = new ArrayList<String>();
			        while (m.find()) {
                    	error.add(m.group());                    	   
                    	}
			        String errorMessage = "";
			        for(int i=0;i<error.size();i++){		  
			            errorMessage = errorMessage + error.get(i) + "\n";
			        }
			        message = message + "\n--------" + errorText + "--------" + "\n" + errorMessage;
                }
				
				
			} else if (status == 500) {
				message = "Internal Server Error [500].";
			} else if (exception.equals("parsererror")) {
				message = "Requested JSON parse failed.";
			} else if (exception.equals("timeout")) {
				message = "Time out error.";
			} else {
				message = "Uncaught Error.\n" + responseMessage;
			}
		}

		return message.toString();
	}
	

}