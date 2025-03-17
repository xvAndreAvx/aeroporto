package com.interfacce;

import java.util.List;

import com.aeroportoEntity.Aeroporto;

import jakarta.persistence.EntityManager;

public interface AeroportoInterfacciaDAO {

	void createAeroporto(Aeroporto aeroporto, EntityManager em);
	
	Aeroporto findByName(String nome, EntityManager em);
	
	Aeroporto findById(Long id, EntityManager em);
	
	List<Aeroporto> getAll(EntityManager em);
}
