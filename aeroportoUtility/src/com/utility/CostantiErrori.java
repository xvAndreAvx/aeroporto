package com.utility;

public class CostantiErrori {
	
	public static final String ERRORE = "Errore";
	public static final String ATTENZIONE = "Attenzione";
	public static final String SUCCESSO = "Successo";
	public static final String INFORMAZIONE = "Informazione";

	
	//UtenteBean Errori
	public static final String LOGIN_ERR = "Si è verificato un errore durante il login";
	public static final String NON_AUTENTICATO = "Utente non autenticato";
	public static final String NON_AUTORIZZATO = "Credenziali errate. Riprova.";
	
	//AereoBean Errori
	public static final String ERRORE_CREA_AEREO = "Errore creazione aereo";
	public static final String CREAZIONE_AEREO = "Aereo creato con successo";


	//TrattaBean Errori
	public static final String AEROPORTI_UGUALI = "Gli aeroporti di partenza e arrivo non possono essere uguali";
	public static final String ERR_AIR_SELECT = "Errore durante la selezione dell'aeroporto: ";
	public static final String MANCATA_SELEZIONE_AIR = "Selezionare entrambi gli aeroporti di partenza e arrivo";
	public static final String SUCCESSO_CREAZIONE_TRATTA = "Tratta creata con successo";
	public static final String ERR_LISTA_TRATTE = "Errore nel caricamento delle tratte!";
	public static final String ERR_CREA_TRATTA = "Errore durante la registrazione della tratta: ";

	//VoloBean Errori
	public static final String ERR_LISTA_VOLI = "Errore nel caricamento dei voli";
	public static final String TRATTA_MANCANTE = "Selezionare una tratta valida";
	public static final String DATA_NON_VALIDA = "Selezionare una data valida";
	public static final String SUCCESSO_CREAZIONE_VOLO = "Volo creata con successo";
	public static final String ERR_CREA_VOLO = "Errore durante la registrazione del volo: ";
	public static final String AEREO_NON_VALIDO = "Selezionare un Aereo valido";
	public static final String ERR_REC_TRATTA = "Errore nel recupero della tratta: ";
	public static final String ERR_REC_AEREO = "Errore nel recupero del aereo: ";

	//BigliettoBean
	public static final String VOLO_MANCANTE = "Selezionare un volo valido";
	public static final String COSTO_NON_VALIDO = "Inserire un costo valido";
	public static final String SUCCESSO_CREAZIONE_BIGLIETTO = "Biglietto creato con successo";
	public static final String ERR_CREA_BIGLIETTO = "Errore durante la registrazione del biglietto: ";
	public static final String ERR_RECUPERO_BIGLIETTO = "Errore durante il recupero del biglietto";

	//PrenotazioneBean
	public static final String UTENTE_NON_VALIDO = "Utente non valido";
	public static final String SUCCESSO_CREAZIONE_PRENOTAZIONE = "Prenotazione effettuata con successo";
	
	//PasseggeroBean
	public static final String POSTI_NON_CARICATI = "Errore nel caricamento dei posti";
	public static final String POSTO_NON_VALIDO = "Selezionare un posto valido";
	public static final String ERR_CREA_POSTO = "Errore nella registrazione del passeggero";
	public static final String BIGLIETTO_NON_TROVATO = "Biglietto non trovato";
	public static final String ERR_LISTA_PASSEGGERI = "Errore nel caricamento dei passeggeri!";

}