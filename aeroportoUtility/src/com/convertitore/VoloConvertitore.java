package com.convertitore;

import com.DTO.VoloDTO;
import com.aeroportoEntity.Volo;

public class VoloConvertitore {

	public static VoloDTO toDTO(Volo volo) {
		VoloDTO voloDTO = new VoloDTO();
		if (volo == null) {
			voloDTO = null;
		}
		
		voloDTO.setId(volo.getId());
		voloDTO.setCodiceVolo(volo.getCodiceVolo());
		voloDTO.setAereo(volo.getAereo());
		voloDTO.setDataPartenza(volo.getDataPartenza());
		voloDTO.setDataArrivo(volo.getDataArrivo());
		voloDTO.setTratta(volo.getTratta());
		voloDTO.setPostiDisponibili(volo.getPostiDisponibili());
		
		return voloDTO;
	}
	
	public static Volo toEntity(VoloDTO voloDTO) {
		Volo volo = new Volo();
		if (voloDTO == null) {
			volo = null;
		}
		
		volo.setCodiceVolo(voloDTO.getCodiceVolo());
		volo.setAereo(voloDTO.getAereo());
		volo.setDataPartenza(voloDTO.getDataPartenza());
		volo.setDataArrivo(voloDTO.getDataArrivo());
		volo.setTratta(voloDTO.getTratta());
		volo.setPostiDisponibili(voloDTO.getPostiDisponibili());
		
		return volo;
	}
	
}
