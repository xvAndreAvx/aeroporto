package com.aeroportoDB;

import java.util.List;

import com.aeroportoEntity.Passeggero;
import com.interfacce.PasseggeroInterfacciaDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class PasseggeroDAO implements PasseggeroInterfacciaDAO {

	@Override
	public void createPasseggero(Passeggero passeggero, EntityManager em) {
		em.persist(passeggero);
	}

	@Override
	public Passeggero findById(Long id, EntityManager em) {
		try {
			return em.createQuery("SELECT p FROM Passeggero p "
					+ "JOIN FETCH p.biglietto "
					+ "WHERE p.id = :id ", Passeggero.class)
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
	public List<Passeggero> getAll(EntityManager em) {
		try {
	        return em.createQuery("SELECT p FROM Passeggero p "
	        		+ "JOIN FETCH p.biglietto ", Passeggero.class)
	        		.getResultList();
		} catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public Passeggero findByPosto(String posto, Long idBiglietto, EntityManager em) {
		try {
			return em.createQuery("SELECT p FROM Passeggero p "
			        + "JOIN FETCH p.biglietto b "
			        + "WHERE p.posto = :posto "
			        + "AND b.id = :idBiglietto", Passeggero.class)
					.setParameter("posto", posto)
					.setParameter("idBiglietto", idBiglietto)
		    		.getSingleResult();
		} catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
