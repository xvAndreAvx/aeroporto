package com.aeroportoEntity;

import java.time.LocalDateTime;

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
@Table (name = "prenotazione")
public class Prenotazione {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column( name = "codice" , nullable = true)
	private String codice;	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idUtente")
	private Utente utente;
	
	@Column( name = "dataAcquisto" , nullable = true)
	private LocalDateTime dataAcquisto;	

	public Prenotazione() {
		super();
	}

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
