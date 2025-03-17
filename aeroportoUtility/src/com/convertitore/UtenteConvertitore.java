package com.convertitore;

import com.DTO.UtenteDTO;
import com.aeroportoEntity.Utente;

public class UtenteConvertitore {

	public static UtenteDTO toDTO(Utente utente) {
		UtenteDTO utenteDTO = new UtenteDTO();
		if (utente == null) {
	        return null;
	    }
		
		utenteDTO.setId(utente.getId());
		utenteDTO.setEmail(utente.getEmail());
		utenteDTO.setPassword(utente.getPassword());
		utenteDTO.setNome(utente.getNome());
		utenteDTO.setCognome(utente.getCognome());
		utenteDTO.setEta(utente.getEta());
		utenteDTO.setRuolo(utente.getRuolo());
		
		return utenteDTO;
	}
	
	public static Utente toEntity(UtenteDTO utenteDTO) {
		Utente utente = new Utente();
		if (utenteDTO == null) {
	        return null;
	    }
		
		utente.setEmail(utenteDTO.getEmail());
		utente.setPassword(utenteDTO.getPassword());
		utente.setNome(utenteDTO.getNome() != null ? utenteDTO.getNome() : "");
		utente.setCognome(utenteDTO.getCognome() != null ? utenteDTO.getCognome() : "");
		utente.setEta(utenteDTO.getEta()  != null ? utenteDTO.getEta() : 0);
		
		return utente;
	}
}
