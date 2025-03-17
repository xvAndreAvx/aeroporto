package com.DTO;

import java.io.Serializable;

import com.aeroportoEntity.Biglietto;

public class PasseggeroDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String codiceFiscale;
	private String nome;
	private String cognome;
	private String posto;
	private Biglietto biglietto;
	
	
	public PasseggeroDTO() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPosto() {
		return posto;
	}

	public void setPosto(String posto) {
		this.posto = posto;
	}

	public Biglietto getBiglietto() {
		return biglietto;
	}

	public void setBiglietto(Biglietto boglietto) {
		this.biglietto = boglietto;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	
	

}
