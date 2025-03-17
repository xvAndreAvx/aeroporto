package com.aeroportoEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "aereo")
public class Aereo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    
    @Column(name = "modello", nullable = false)
    private String modello;
    
    @Column(name = "capienza", nullable = false)
    private Long capienza;
    
    public Aereo() {
		super();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getModello() {
		return modello;
	}


	public void setModello(String modello) {
		this.modello = modello;
	}

	public Long getCapienza() {
		return capienza;
	}

	public void setCapienza(Long capienza) {
		this.capienza = capienza;
	}
	
	
    
    
}
