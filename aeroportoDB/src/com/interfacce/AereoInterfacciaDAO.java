package com.interfacce;
import java.util.List;

import com.aeroportoEntity.Aereo;

import jakarta.persistence.EntityManager;

public interface AereoInterfacciaDAO {
	
	void createAereo(Aereo aereo, EntityManager em);
	
	Aereo findByIdAereo(Long id, EntityManager em);

	List<Aereo> getAll(EntityManager em);
	
	
	
}

