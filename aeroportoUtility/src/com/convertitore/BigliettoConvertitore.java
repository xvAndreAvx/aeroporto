package com.convertitore;

import com.DTO.BigliettoDTO;
import com.aeroportoEntity.Biglietto;

public class BigliettoConvertitore {
	
	public static BigliettoDTO toDTO(Biglietto biglietto) {
		BigliettoDTO bigliettoDTO = new BigliettoDTO();
		if (biglietto == null) {
			bigliettoDTO = new BigliettoDTO();;
		}
		
		bigliettoDTO.setId(biglietto.getId());
		bigliettoDTO.setCodiceBiglietto(biglietto.getCodice());
		bigliettoDTO.setCosto(biglietto.getCosto());
		bigliettoDTO.setVolo(biglietto.getVolo());
		bigliettoDTO.setPrenotazione(biglietto.getPrenotazione());
		
		return bigliettoDTO;
	}
	
	public static Biglietto toEntity(BigliettoDTO bigliettoDTO) {
		Biglietto biglietto = new Biglietto();
		if (bigliettoDTO == null) {
			biglietto = new Biglietto();
		}
		
		biglietto.setId(bigliettoDTO.getId());
		biglietto.setCodice(bigliettoDTO.getCodiceBiglietto());
		biglietto.setCosto(bigliettoDTO.getCosto());
		biglietto.setVolo(bigliettoDTO.getVolo());
		biglietto.setPrenotazione(bigliettoDTO.getPrenotazione());
		
		return biglietto;
	}
	
}
