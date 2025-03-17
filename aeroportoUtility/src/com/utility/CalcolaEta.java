package com.utility;

import java.time.LocalDateTime;
import java.time.Period;

public class CalcolaEta {

	public Integer calcolo(LocalDateTime dataNascita) {
	    if (dataNascita == null) {
	        return null;
	    }
	    
	    LocalDateTime oggi = LocalDateTime.now();
	    
	    // Verifica che la data di nascita non sia nel futuro
	    if (dataNascita.isAfter(oggi)) {
	        return null;
	    }
	    
	    // Calcola la differenza in anni tra la data di nascita e oggi
	    Period periodo = Period.between(dataNascita.toLocalDate(), oggi.toLocalDate());
	    
	    return periodo.getYears();
	}
	
}
