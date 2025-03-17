package com.pdf;

import java.io.Serializable;

import com.aeroportoEntity.Biglietto;
import com.aeroportoEntity.Passeggero;
import com.aeroportoEntity.Prenotazione;
import com.aeroportoEntity.Utente;

public class PdfBiglietto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Utente utente;
	private Prenotazione prenotazione;
	private Biglietto biglietto;
	private Passeggero passeggero;
	
	public PdfBiglietto() {
		super();
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Prenotazione getPrenotazione() {
		return prenotazione;
	}

	public void setPrenotazione(Prenotazione prenotazione) {
		this.prenotazione = prenotazione;
	}

	public Biglietto getBiglietto() {
		return biglietto;
	}

	public void setBiglietto(Biglietto biglietto) {
		this.biglietto = biglietto;
	}

	public Passeggero getPasseggero() {
		return passeggero;
	}

	public void setPasseggero(Passeggero passeggero) {
		this.passeggero = passeggero;
	}

	

}
