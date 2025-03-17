package com.serviceInt;

import java.util.List;

import com.DTO.AereoDTO;
import com.aeroportoEntity.Aereo;

import jakarta.ejb.Local;

@Local
public interface AereoService {
	
	AereoDTO insertAereo(AereoDTO aereoDTO) throws Exception;
	
	Aereo findyByIdAereo(Long id) throws Exception;
	
	List<AereoDTO> getAll() throws Exception;


}
