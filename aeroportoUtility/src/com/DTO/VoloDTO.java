package com.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.aeroportoEntity.Aereo;
import com.aeroportoEntity.Tratta;

public class VoloDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long Id;	
	private String codiceVolo;
	private Aereo aereo;
	private LocalDateTime dataPartenza;
	private LocalDateTime dataArrivo;
	private Tratta tratta;
	private Long postiDisponibili;
	
	public VoloDTO() {
		super();
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public Aereo getAereo() {
		return aereo;
	}

	public void setAereo(Aereo aereo) {
		this.aereo = aereo;
	}

	public LocalDateTime getDataPartenza() {
		return dataPartenza;
	}

	public void setDataPartenza(LocalDateTime dataPartenza) {
		this.dataPartenza = dataPartenza;
	}

	public LocalDateTime getDataArrivo() {
		return dataArrivo;
	}

	public void setDataArrivo(LocalDateTime dataArrivo) {
		this.dataArrivo = dataArrivo;
	}

	public Tratta getTratta() {
		return tratta;
	}

	public void setTratta(Tratta tratta) {
		this.tratta = tratta;
	}

	public String getCodiceVolo() {
		return codiceVolo;
	}

	public void setCodiceVolo(String codiceVolo) {
		this.codiceVolo = codiceVolo;
	}

	public Long getPostiDisponibili() {
		return postiDisponibili;
	}

	public void setPostiDisponibili(Long postiDisponibili) {
		this.postiDisponibili = postiDisponibili;
	}
	
	

}
