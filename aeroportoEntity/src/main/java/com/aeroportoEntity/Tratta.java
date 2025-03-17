package com.aeroportoEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table ( name = "tratta")
public class Tratta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idPartenze")
	private Aeroporto aeroportoInPartenza;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idArrivi")
	private Aeroporto aeroportoInArrivo;

	public Tratta() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Aeroporto getAeroportoInPartenza() {
		return aeroportoInPartenza;
	}

	public void setAeroportoInPartenza(Aeroporto aeroportoInPartenza) {
		this.aeroportoInPartenza = aeroportoInPartenza;
	}

	public Aeroporto getAeroportoInArrivo() {
		return aeroportoInArrivo;
	}

	public void setAeroportoInArrivo(Aeroporto aeroportoInArrivo) {
		this.aeroportoInArrivo = aeroportoInArrivo;
	}
	
	
	
}
