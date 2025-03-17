package com.serviceInt;

import java.util.List;

import com.DTO.TrattaDTO;
import com.aeroportoEntity.Tratta;

import jakarta.ejb.Local;

@Local
public interface TrattaService {

	TrattaDTO insertTratta(TrattaDTO trattaDTO) throws Exception;
	List<TrattaDTO> getAll() throws Exception;
	Tratta findTrattaById(Long id) throws Exception;

}
