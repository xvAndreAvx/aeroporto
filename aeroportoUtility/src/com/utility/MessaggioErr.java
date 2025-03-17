package com.utility;


import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.FacesMessage.Severity;
import jakarta.faces.context.FacesContext;


public class MessaggioErr {
	
	/**
	 * Restituisce un messaggio di errore
	 */
	public void addMessage(Severity severity, String message) {
		
	    String summary;
	    if (severity.equals(FacesMessage.SEVERITY_FATAL)) {
	        summary = CostantiErrori.ERRORE;
	    } else if (severity.equals(FacesMessage.SEVERITY_ERROR)) {
	        summary = CostantiErrori.ATTENZIONE;
	    } else  if (severity.equals(FacesMessage.SEVERITY_WARN)){
	        summary = CostantiErrori.INFORMAZIONE;
	    } else {
	    	summary = CostantiErrori.SUCCESSO;
	    }
	    
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, message));
	}

}
