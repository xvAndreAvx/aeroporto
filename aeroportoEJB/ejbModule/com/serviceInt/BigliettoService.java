package com.serviceInt;

import java.util.List;

import com.DTO.BigliettoDTO;
import com.DTO.PrenotazioneDTO;
import com.aeroportoEntity.Biglietto;
import com.aeroportoEntity.Prenotazione;

import jakarta.ejb.Local;

@Local
public interface BigliettoService {

	BigliettoDTO insertBiglietto(BigliettoDTO bigliettoDTO) throws Exception;
	List<BigliettoDTO> getAll() throws Exception;
	Biglietto findById(Long id) throws Exception;
	void updateBiglietto(BigliettoDTO biglietto, PrenotazioneDTO prenotazioneDTO) throws Exception;
	BigliettoDTO findByIdVolo(Long idVolo) throws Exception;
	BigliettoDTO findByIdVoloEPrenotazione(Long idVolo, Prenotazione prenotazione) throws Exception;
	Biglietto findByPosto(String posto , Long idBiglietto) throws Exception;
}
