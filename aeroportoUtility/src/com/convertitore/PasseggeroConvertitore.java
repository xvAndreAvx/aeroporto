package com.convertitore;

import com.DTO.PasseggeroDTO;
import com.aeroportoEntity.Passeggero;

public class PasseggeroConvertitore {
	
	public static PasseggeroDTO toDTO(Passeggero passeggero) {
		PasseggeroDTO passeggeroDTO = new PasseggeroDTO();
		if (passeggero == null) {
			return passeggeroDTO;
		}
		
		passeggeroDTO.setId(passeggero.getId());
		passeggeroDTO.setCodiceFiscale(passeggero.getCodiceFiscale());
		passeggeroDTO.setNome(passeggero.getNome());
		passeggeroDTO.setCognome(passeggero.getCognome());
		passeggeroDTO.setPosto(passeggero.getPosto());
		passeggeroDTO.setBiglietto(passeggero.getBiglietto());
		
		return passeggeroDTO;
	}
	
	public static Passeggero toEntity(PasseggeroDTO passeggeroDTO) {
		Passeggero passeggero = new Passeggero();
		if (passeggeroDTO == null) {
			return passeggero;
		}
		
		passeggero.setCodiceFiscale(passeggeroDTO.getCodiceFiscale());
		passeggero.setNome(passeggeroDTO.getNome());
		passeggero.setCognome(passeggeroDTO.getCognome());
		passeggero.setPosto(passeggeroDTO.getPosto());
		passeggero.setBiglietto(passeggeroDTO.getBiglietto());
		
		return passeggero;
	}

}
