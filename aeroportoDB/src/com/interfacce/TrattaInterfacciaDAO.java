package com.interfacce;

import java.util.List;

import com.aeroportoEntity.Tratta;

import jakarta.persistence.EntityManager;

public interface TrattaInterfacciaDAO {
	
	void createTratta(Tratta tratta, EntityManager em);
	List<Tratta> getAll(EntityManager em);
	Tratta findById(Long id, EntityManager em);

}
