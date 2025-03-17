package com.validazioni;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.exception.ValidazioneException;

public class UtenteValidazione {
    private static final Logger logger = Logger.getLogger(UtenteValidazione.class.getName());
    private static final String EMAIL_REGEX = "^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$";
    private static final String NOME_REGEX = "^[A-Za-zÀ-ÿ][A-Za-zÀ-ÿ\\s'\\-]{0,38}[A-Za-zÀ-ÿ]$";
    private static final int MAX_ETA = 120;
    private static final int MIN_ETA = 18;
    
    public static void validaFormatoCredenziali(String email, String password) 
            throws ValidazioneException {
    	List<String> errori = new ArrayList<>();

        if (email == null || email.trim().isEmpty()) {
            errori.add("L'email non può essere vuota");
        } else if (!email.matches(EMAIL_REGEX)) {
            errori.add("Il formato dell'email non è valido");
        }

        if (password == null || password.trim().isEmpty()) {
            errori.add("La password non può essere vuota");
        } else {
            if (password.length() < 8) {
                errori.add("La password deve contenere almeno 8 caratteri");
            }
            if (!password.matches(".*[A-Z].*")) {
                errori.add("La password deve contenere almeno una lettera maiuscola");
            }
            if (!password.matches(".*[0-9].*")) {
                errori.add("La password deve contenere almeno un numero");
            }
        }

        if (!errori.isEmpty()) {
            logger.warning("Trovati " +errori.size()+ " errori nella validazione delle credenziali");
            throw new ValidazioneException(errori);
        }

        logger.info("Validazione credenziali completata con successo");
    }
    
    public static void validaFormatoDatiAnagrafici(String nome, String cognome, Integer eta) 
            throws ValidazioneException {
        List<String> errori = new ArrayList<>();

        if (nome == null || nome.trim().isEmpty()) {
            errori.add("Il nome non può essere vuoto");
        } else {
            if (nome.length() > 40) {
                errori.add("Il nome non può superare i 40 caratteri");
            }
            if (!nome.matches(NOME_REGEX)) {
                errori.add("Il nome contiene caratteri non validi");
            }
            if (nome.contains("  ")) {
                errori.add("Il nome non può contenere spazi multipli");
            }
            
            if (nome.equals(nome.toUpperCase())) {
            	errori.add("Il nome non può essere scritto in maiuscolo");
            }
        }

        if (cognome == null || cognome.trim().isEmpty()) {
            errori.add("Il cognome non può essere vuoto");
        } else {
            if (cognome.length() > 50) {
                errori.add("Il cognome non può superare i 50 caratteri");
            }
            if (!cognome.matches(NOME_REGEX)) {
                errori.add("Il cognome contiene caratteri non validi");
            }
            if (cognome.contains("  ")) {
                errori.add("Il cognome non può contenere spazi multipli");
            }
            if (cognome.equals(cognome.toUpperCase())) {
            	errori.add("Il cognome non può essere scritto in maiuscolo");
            }
        }

        if (eta == null) {
            errori.add("L'età è obbligatoria");
        } else {
            if (eta < MIN_ETA || eta > MAX_ETA) {
                errori.add(String.format("L'età deve essere maggiore dei %d  anni", MIN_ETA));
            }
        }

        if (!errori.isEmpty()) {
            throw new ValidazioneException(errori);
        }

        logger.info("Validazione dati anagrafici completata con successo");
    }
    
    public static void validaFormatoDatiPasseggero(String nome, String cognome, String codiceFiscale) throws ValidazioneException{
    	List<String> errori = new ArrayList<>();

        if (nome == null || nome.trim().isEmpty()) {
            errori.add("Il nome non può essere vuoto");
        } else {
            if (nome.length() > 40) {
                errori.add("Il nome non può superare i 40 caratteri");
            }
            if (!nome.matches(NOME_REGEX)) {
                errori.add("Il nome contiene caratteri non validi");
            }
            if (nome.contains("  ")) {
                errori.add("Il nome non può contenere spazi multipli");
            }
            
            if (nome.equals(nome.toUpperCase())) {
            	errori.add("Il nome non può essere scritto in maiuscolo");
            }
        }

        if (cognome == null || cognome.trim().isEmpty()) {
            errori.add("Il cognome non può essere vuoto");
        } else {
            if (cognome.length() > 50) {
                errori.add("Il cognome non può superare i 50 caratteri");
            }
            if (!cognome.matches(NOME_REGEX)) {
                errori.add("Il cognome contiene caratteri non validi");
            }
            if (cognome.contains("  ")) {
                errori.add("Il cognome non può contenere spazi multipli");
            }
            if (cognome.equals(cognome.toUpperCase())) {
            	errori.add("Il cognome non può essere scritto in maiuscolo");
            }
        }
        
        if (codiceFiscale == null) {
            errori.add("Il codice fiscale è obbligatorio");
        } else {
        	 if (nome.contains("  ")) {
                 errori.add("Il codice fiscale non può contenere spazi multipli");
             }
        	 if (cognome.length() > 16) {
                 errori.add("Il codice fiscale non può superare i 16 caratteri");
             }
        	 if (cognome.length() < 16) {
                 errori.add("Il codice fiscale non può essere inferiore i 16 caratteri");
             }
        }

        if (!errori.isEmpty()) {
            throw new ValidazioneException(errori);
        }
        
        
    }
}
