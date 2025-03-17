package com.validazioni;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.exception.ValidazioneException;

public class AereoValidazione {
    private static final Logger logger = Logger.getLogger(UtenteValidazione.class.getName());
    
    
    public static void validaModelloConCapienza(String modello, Long capienza) throws ValidazioneException{
        List<String> errori = new ArrayList<>();
        
        if (modello == null) {
        	errori.add("Il modello non può essere vuoto");
        } else {
        	if (modello.length()<3) {
                errori.add("Il modello non può essere inferiore ai 3 caratteri");
        	}
        	if (modello.length()>100) {
                errori.add("Il modello non può superare i 100 caratteri");
        	}
        	if (modello.charAt(0) == '.') {
        	    errori.add("Il modello non può iniziare con un punto");
        	}
        }	
        
        if ( capienza == null) {
        	errori.add("La capienza non può essere vuota");

        } else {
        	if ( capienza <= 0 ) {
        		errori.add("La capienza non può essere minore di 1");
        	}
        	if ( capienza > 1000) {
        		errori.add(("La capienza non può supereare 1000"));
        	}
        }
        
        	
        logger.info("Validazione dati aereo completata con successo");

        
    }
}
