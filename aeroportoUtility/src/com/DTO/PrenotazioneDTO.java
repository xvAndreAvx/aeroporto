package com.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.aeroportoEntity.Utente;

public class PrenotazioneDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String codice;
	private Utente utente;
	private LocalDateTime dataAcquisto;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Utente getUtente() {
		return utente;
	}
	public void setUtente(Utente utente) {
		this.utente = utente;
	}
	public LocalDateTime getDataAcquisto() {
		return dataAcquisto;
	}
	public void setDataAcquisto(LocalDateTime dataAcquisto) {
		this.dataAcquisto = dataAcquisto;
	}
	public String getCodice() {
		return codice;
	}
	public void setCodice(String codice) {
		this.codice = codice;
	}
	

}
