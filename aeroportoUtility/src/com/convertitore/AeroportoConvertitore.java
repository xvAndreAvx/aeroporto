package com.convertitore;

import com.DTO.AeroportoDTO;
import com.aeroportoEntity.Aeroporto;

public class AeroportoConvertitore {
	
	public static AeroportoDTO toDTO(Aeroporto aEnty) {
		AeroportoDTO aDTO = new AeroportoDTO();
		
		aDTO.setId(aEnty.getId());
		aDTO.setNome(aEnty.getNome());
		aDTO.setCitta(aEnty.getCitta());
		
		return aDTO;
	}
	
	public static Aeroporto toEntity(AeroportoDTO aDTO) {
		Aeroporto aEnty = new Aeroporto();
		
		aEnty.setNome(aDTO.getNome());
		aEnty.setCitta(aDTO.getCitta());
		
		return aEnty;
	}
}
