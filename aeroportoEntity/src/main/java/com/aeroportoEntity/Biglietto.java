package com.aeroportoEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table (name = "biglietto")
public class Biglietto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "codice", nullable = true)
	private String codice;
	
	@Column(name = "costo", nullable = false)
	private Double costo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idVolo")
	private Volo volo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idPrenotazione")
	private Prenotazione prenotazione;

	public Biglietto() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBiglietto() {
		return codice;
	}

	public void setBiglietto(String codice) {
		this.codice = codice;
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

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}
	
	
	
}
