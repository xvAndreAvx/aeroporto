package com.interfacce;

import java.util.List;

import com.aeroportoEntity.Volo;

import jakarta.persistence.EntityManager;

public interface VoloInterfacciaDAO {

	void createVolo(Volo volo, EntityManager em);
	List<Volo> getAll(EntityManager em);
	Volo findById(Long id, EntityManager em);
	void updateDisponibilita(Volo volo, EntityManager em);

}
