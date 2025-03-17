package com.serviceInt;

import java.util.List;

import com.DTO.VoloDTO;
import com.aeroportoEntity.Volo;

import jakarta.ejb.Local;

@Local
public interface VoloService {

	Volo insertVolo(VoloDTO voloDTO) throws Exception;
	List<VoloDTO> getAll() throws Exception;
	Volo findVoloById(Long id) throws Exception;

}
