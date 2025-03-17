package com.interfacce;

import java.util.List;

import com.aeroportoEntity.Passeggero;

import jakarta.persistence.EntityManager;

public interface PasseggeroInterfacciaDAO {
	
	void createPasseggero(Passeggero passeggero, EntityManager em);
	Passeggero findById(Long id, EntityManager em);
	List<Passeggero> getAll(EntityManager em);
	Passeggero findByPosto(String posto, Long idBiglietto, EntityManager em);

}
