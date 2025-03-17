package com.convertitore;

import com.DTO.TrattaDTO;
import com.aeroportoEntity.Tratta;

public class TrattaConvertitore {
	
	public static TrattaDTO toDTO(Tratta tratta) {
	    TrattaDTO trattaDTO = new TrattaDTO();
	    if(tratta == null) {
	        trattaDTO =  null; 
	    }
	    
	    trattaDTO.setId(tratta.getId());
	    trattaDTO.setAeroportoPartenza(tratta.getAeroportoInPartenza());  
	    trattaDTO.setAeroportoArrivo(tratta.getAeroportoInArrivo());     
	    
	    return trattaDTO; 
	}
	
	public static Tratta toEntity(TrattaDTO trattaDTO) {
		Tratta tratta = new Tratta();
		if(trattaDTO == null) {
			tratta = null;
		}
		
		tratta.setAeroportoInArrivo(trattaDTO.getAeroportoArrivo());
		tratta.setAeroportoInPartenza(trattaDTO.getAeroportoPartenza());
		
		return tratta;
	}

}
