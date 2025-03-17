package com.aeroportoDB;

import java.util.List;

import com.aeroportoEntity.Aereo;
import com.interfacce.AereoInterfacciaDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class AereoDAO implements AereoInterfacciaDAO{
	
	
	@Override
	public void createAereo(Aereo aereo, EntityManager em) {
			em.persist(aereo);
	}

	

	@Override
	public Aereo findByIdAereo(Long id, EntityManager em) {
		try {
	        return em
	            .createQuery("SELECT a FROM Aereo a "
	                + "WHERE a.id = :id", Aereo.class)
	            .setParameter("id", id)
	            .getSingleResult();
		} catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public List<Aereo> getAll(EntityManager em) {
	    try {
	        return em
	            .createQuery("SELECT a FROM Aereo a ", Aereo.class)
	            .getResultList();
	    } catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	

}


