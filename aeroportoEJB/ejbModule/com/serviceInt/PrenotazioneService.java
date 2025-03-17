package com.serviceInt;

import java.util.List;

import com.DTO.PrenotazioneDTO;
import com.aeroportoEntity.Prenotazione;

import jakarta.ejb.Local;

@Local
public interface PrenotazioneService {
	
	PrenotazioneDTO insertPrenotazione(PrenotazioneDTO prenotazioneDTO) throws Exception;
	List<PrenotazioneDTO> getAll() throws Exception;
	Prenotazione findByCodice(String codice) throws Exception;
	PrenotazioneDTO inserisciData(PrenotazioneDTO prenotazioneDTO) throws Exception;

}
