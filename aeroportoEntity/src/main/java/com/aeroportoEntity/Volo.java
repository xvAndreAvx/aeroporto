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
@Table (name = "volo")
public class Volo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idAereo")
	private Aereo aereo;
	
	@Column(name = "codiceVolo", nullable = false)
	private String  codiceVolo;	
	
	@Column(name = "dataPartenza", nullable = false)
	private LocalDateTime  dataPartenza;	
	
	@Column(name = "dataArrivo", nullable = false)
	private LocalDateTime  dataArrivo;	
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "idTratta")
	private Tratta tratta;
	
	@Column(name = "postiDisponibili", nullable = false)
	private Long  postiDisponibili;	

	public Volo() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Aereo getAereo() {
		return aereo;
	}

	public void setAereo(Aereo aereo) {
		this.aereo = aereo;
	}

	public LocalDateTime  getDataPartenza() {
		return dataPartenza;
	}

	public void setDataPartenza(LocalDateTime  dataPartenza) {
		this.dataPartenza = dataPartenza;
	}

	public LocalDateTime  getDataArrivo() {
		return dataArrivo;
	}

	public void setDataArrivo(LocalDateTime  dataArrivo) {
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
