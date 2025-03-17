package com.aeroportoDB;

import java.util.List;

import com.aeroportoEntity.Tratta;
import com.interfacce.TrattaInterfacciaDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class TrattaDAO implements TrattaInterfacciaDAO {

	@Override
	public void createTratta(Tratta tratta, EntityManager em) {
		em.persist(tratta);
	}

	@Override
	public List<Tratta> getAll(EntityManager em) {
	    try {
	        return em
	            .createQuery("SELECT t FROM Tratta t "
	                + "JOIN FETCH t.aeroportoInPartenza "
	                + "JOIN FETCH t.aeroportoInArrivo", Tratta.class)
	            .getResultList();
	    } catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Tratta findById(Long id, EntityManager em) {
	    try {
	        return em
	            .createQuery("SELECT t FROM Tratta t "
	                + "JOIN FETCH t.aeroportoInPartenza "
	                + "JOIN FETCH t.aeroportoInArrivo "
	                + "WHERE t.id = :id", Tratta.class)
	            .setParameter("id", id)
	            .getSingleResult();
	    } catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
