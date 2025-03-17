package com.convertitore;

import com.DTO.AereoDTO;
import com.aeroportoEntity.Aereo;

public class AereoConvertitore {
	
	public static AereoDTO toDTO(Aereo aereo) {
		AereoDTO aereoDTO = new AereoDTO();
		if (aereo == null ) {
			return aereoDTO;
		}
		
		aereoDTO.setId(aereo.getId());
		aereoDTO.setModello(aereo.getModello());
		aereoDTO.setCapienza(aereo.getCapienza());
		
		return aereoDTO;
	}
	
	public static Aereo toEntity(AereoDTO aereoDTO) {
		Aereo aereo = new Aereo();
		if (aereoDTO==null) {
			return aereo;
		}
		aereo.setModello(aereoDTO.getModello());
		aereo.setCapienza(aereoDTO.getCapienza());
		
		return aereo;
	}

}


