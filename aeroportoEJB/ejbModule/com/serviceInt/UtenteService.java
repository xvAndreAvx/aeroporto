package com.serviceInt;

import com.DTO.UtenteDTO;
import com.aeroportoEntity.Utente;

import jakarta.ejb.Local;

@Local
public interface UtenteService {
	
	UtenteDTO insertUtente(UtenteDTO utenteDTO) throws Exception;
	
	UtenteDTO login(UtenteDTO utenteDTO) throws Exception;
	
	Utente verificaUtente(UtenteDTO utenteDTO) throws Exception;

}
