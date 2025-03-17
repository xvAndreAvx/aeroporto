package com.DTO;

import java.io.Serializable;

public class AereoDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String modello;
	private Long capienza;
	
	public AereoDTO() {
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
