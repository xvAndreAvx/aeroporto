package com.exception;

public class AeroportoException  extends Exception {

	private static final long serialVersionUID = 1L;

	private final String codice;

	public AeroportoException(String codice, String messaggio) {
		super(messaggio);
		this.codice = codice;
	}

	public String getCodice() {
		return codice;
	}

}
