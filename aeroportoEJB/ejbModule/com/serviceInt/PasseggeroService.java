package com.serviceInt;

import java.util.List;

import com.DTO.PasseggeroDTO;
import com.aeroportoEntity.Passeggero;

import jakarta.ejb.Local;

@Local
public interface PasseggeroService {

	PasseggeroDTO insertPasseggero(PasseggeroDTO passeggeroDTO) throws Exception;
	Passeggero findById(Long id) throws Exception;
	List<PasseggeroDTO> getAll() throws Exception;
	Passeggero findByPosto(String posto , Long idBiglietto) throws Exception;
}
