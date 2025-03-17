package com.convertitore;

import com.DTO.PrenotazioneDTO;
import com.aeroportoEntity.Prenotazione;

public class PrenotazioneConvertitore {

	public static PrenotazioneDTO toDTO(Prenotazione prenotazione) {
		PrenotazioneDTO prenotazioneDTO = new PrenotazioneDTO();
		if (prenotazione == null ) {
			prenotazioneDTO = null;
		}
		
		prenotazioneDTO.setId(prenotazione.getId());
		prenotazioneDTO.setCodice(prenotazione.getCodice());
		prenotazioneDTO.setDataAcquisto(prenotazione.getDataAcquisto());
		prenotazioneDTO.setUtente(prenotazione.getUtente());
		
		return prenotazioneDTO;
	}
	
	public static Prenotazione toEntity(PrenotazioneDTO prenotazioneDTO) {
		Prenotazione prenotazione = new Prenotazione();
		if (prenotazioneDTO == null ) {
			prenotazione = null;
		}
		
		prenotazione.setCodice(prenotazioneDTO.getCodice());
		prenotazione.setDataAcquisto(prenotazioneDTO.getDataAcquisto());
		prenotazione.setUtente(prenotazioneDTO.getUtente());
		
		return prenotazione;
	}
	
}
