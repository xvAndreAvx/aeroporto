package com.DTO;

import java.io.Serializable;

import com.aeroportoEntity.Aeroporto;

public class TrattaDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long Id;
	private Aeroporto aeroportoPartenza;
	private Aeroporto aeroportoArrivo;
	
	public TrattaDTO() {
		super();
	}
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public Aeroporto getAeroportoPartenza() {
		return aeroportoPartenza;
	}
	public void setAeroportoPartenza(Aeroporto aeroportoPartenza) {
		this.aeroportoPartenza = aeroportoPartenza;
	}
	public Aeroporto getAeroportoArrivo() {
		return aeroportoArrivo;
	}
	public void setAeroportoArrivo(Aeroporto aeroportoArrivo) {
		this.aeroportoArrivo = aeroportoArrivo;
	}

	
	

}
