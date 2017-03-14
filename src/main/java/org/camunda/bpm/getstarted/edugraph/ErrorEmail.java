package org.camunda.bpm.getstarted.edugraph;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ErrorEmail implements JavaDelegate{

	
		private static final String HOST = "smtp.gmail.com";
		  private static final String USER = "demodemo1234512345@gmail.com";
		  private static final String PWD = "google12345";

		  private final static Logger LOGGER = Logger.getLogger(ErrorEmail.class.getName());

		  public void execute(DelegateExecution execution) throws Exception {
			  
			  
		      String var1 = (String) execution.getVariable("extractError");
		      String var2 = (String) execution.getVariable("responseTripleError");
		      String var3 = (String) execution.getVariable("responseCatalogError");
		      String var4 = (String) execution.getVariable("storeError");
		      String var5 = (String) execution.getVariable("responseLiveError");
		      
		      String recipient = "happyhwx888@googlemail.com";
		      
		      Email email = new SimpleEmail();
		      email.setCharset("utf-8");
		      email.setHostName(HOST);
		      email.setSmtpPort(465);
		      email.setAuthentication(USER, PWD);
		      email.setSSL(true);
		      
		      if (var1 !=null){
		      try {
		          email.setFrom("noreply@camunda.org");
		          email.setSubject("Extraction Error");
		          email.setMsg("Hi! I'm sorry to tell you that there is an error during the running of the process:  " + var1 + ".\n\n Best wishes, \n\n Demo Demo");
		          
		          
		          email.addTo(recipient);

		          email.send();
		          LOGGER.info("Task Assignment Email successfully sent to address: " + recipient); 

		        } catch (Exception e) {
		          LOGGER.log(Level.WARNING, "Could not send email to assignee", e);
		        }
		      }
		      
		      if (var2 !=null){
			      try {
			          email.setFrom("noreply@camunda.org");
			          email.setSubject("Triple Store Error");
			          email.setMsg("Hi! I'm sorry to tell you that there is an error during the running of the process:  " + var2 + ".\n\n Best wishes, \n\n Demo Demo");
			          
			          
			          email.addTo(recipient);

			          email.send();
			          LOGGER.info("Task Assignment Email successfully sent to address: " + recipient); 

			        } catch (Exception e) {
			          LOGGER.log(Level.WARNING, "Could not send email to assignee", e);
			        }
			      }
		      
		      if (var3 !=null){
			      try {
			          email.setFrom("noreply@camunda.org");
			          email.setSubject("Analysis Error");
			          email.setMsg("Hi! I'm sorry to tell you that there is an error during the running of the process:  " + var3 + ".\n\n Best wishes, \n\n Demo Demo");
			          
			          
			          email.addTo(recipient);

			          email.send();
			          LOGGER.info("Task Assignment Email successfully sent to address: " + recipient); 

			        } catch (Exception e) {
			          LOGGER.log(Level.WARNING, "Could not send email to assignee", e);
			        }
			      }
		      
		      if (var4 !=null){
			      try {
			          email.setFrom("noreply@camunda.org");
			          email.setSubject("Pillar Store Error");
			          email.setMsg("Hi! I'm sorry to tell you that there is an error during the running of the process:  " + var4 + ".\n\n Best wishes, \n\n Demo Demo");
			          
			          
			          email.addTo(recipient);

			          email.send();
			          LOGGER.info("Task Assignment Email successfully sent to address: " + recipient); 

			        } catch (Exception e) {
			          LOGGER.log(Level.WARNING, "Could not send email to assignee", e);
			        }
			      }
		      
		      if (var5 !=null){
			      try {
			          email.setFrom("noreply@camunda.org");
			          email.setSubject("Transfer Error");
			          email.setMsg("Hi! I'm sorry to tell you that there is an error during the running of the process:  " + var4 + ".\n\n Best wishes, \n\n Demo Demo");
			          
			          
			          email.addTo(recipient);

			          email.send();
			          LOGGER.info("Task Assignment Email successfully sent to address: " + recipient); 

			        } catch (Exception e) {
			          LOGGER.log(Level.WARNING, "Could not send email to assignee", e);
			        }
			      }
		      
			   
		    }
		  
		 

		}
