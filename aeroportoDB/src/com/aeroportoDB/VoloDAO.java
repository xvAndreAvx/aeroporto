package com.aeroportoDB;

import java.util.List;

import com.aeroportoEntity.Volo;
import com.interfacce.VoloInterfacciaDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class VoloDAO implements VoloInterfacciaDAO {

	@Override
	public void createVolo(Volo volo, EntityManager em) {
		em.persist(volo);
	}

	@Override
	public List<Volo> getAll(EntityManager em) {
		try {
	        return em.createQuery("SELECT v FROM Volo v "
	        		+ "JOIN FETCH v.tratta "
	        		+ "JOIN FETCH v.aereo ", Volo.class)
	        		.getResultList();
		} catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Volo findById(Long id, EntityManager em) {
		try {
	        return em
	            .createQuery("SELECT v FROM Volo v "
	                + "JOIN FETCH v.tratta "
	                + "JOIN FETCH v.aereo "
	                + "WHERE v.id = :id", Volo.class)
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
	public void updateDisponibilita(Volo volo, EntityManager em) {
	    try {
	        int updatedRows = em.createQuery("UPDATE Volo v "
	                + "SET v.postiDisponibili = v.postiDisponibili - 1 "
	                + "WHERE v.id = :id")
	                .setParameter("id", volo.getId())
	                .executeUpdate();
	        if (updatedRows == 0) {
	            throw new Exception("Nessun volo aggiornato con codice: " + volo.getCodiceVolo());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Errore durante l'aggiornamento del volo: " + e.getMessage());
	    }
	}

}
