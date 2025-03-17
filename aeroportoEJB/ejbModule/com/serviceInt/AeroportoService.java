package com.serviceInt;

import java.util.List;

import com.DTO.AeroportoDTO;
import com.aeroportoEntity.Aeroporto;

import jakarta.ejb.Local;

@Local
public interface AeroportoService {

	AeroportoDTO insertAeroporto(AeroportoDTO aeroportoDTO) throws Exception;
	AeroportoDTO findByName(AeroportoDTO aeroportoDTO) throws Exception; 
	Aeroporto findById(Long id) throws Exception;
	List<AeroportoDTO> getAll() throws Exception;
}
