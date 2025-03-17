package com.DTO;

import java.io.Serializable;

import com.aeroportoEntity.Prenotazione;
import com.aeroportoEntity.Volo;

public class BigliettoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String  codiceBiglietto;
	private Double costo;
	private Volo volo;
	private Prenotazione prenotazione;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodiceBiglietto() {
		return codiceBiglietto;
	}
	public void setCodiceBiglietto(String codiceBiglietto) {
		this.codiceBiglietto = codiceBiglietto;
	}
	public Double getCosto() {
		return costo;
	}
	public void setCosto(Double costo) {
		this.costo = costo;
	}
	public Volo getVolo() {
		return volo;
	}
	public void setVolo(Volo volo) {
		this.volo = volo;
	}
	public Prenotazione getPrenotazione() {
		return prenotazione;
	}
	public void setPrenotazione(Prenotazione prenotazione) {
		this.prenotazione = prenotazione;
	}
	
	

}
