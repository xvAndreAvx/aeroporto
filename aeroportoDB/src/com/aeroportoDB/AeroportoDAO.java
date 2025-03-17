package com.aeroportoDB;

import java.util.List;

import com.aeroportoEntity.Aeroporto;
import com.interfacce.AeroportoInterfacciaDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class AeroportoDAO implements AeroportoInterfacciaDAO {

	@Override
	public void createAeroporto(Aeroporto aeroporto, EntityManager em) {
		em.persist(aeroporto);
	}

	@Override
	public Aeroporto findByName(String nome, EntityManager em) {
		try {
			return em.createQuery("SELECT a FROM Aeroporto a WHERE a.nome = :nome", Aeroporto.class)
					.setParameter("nome", nome).getSingleResult();
			
		} catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<Aeroporto> getAll(EntityManager em) {
	    try {
	        return em.createQuery("SELECT a FROM Aeroporto a", Aeroporto.class).getResultList();
	    } catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Aeroporto findById(Long id, EntityManager em) {
		try {
			return em.createQuery("SELECT a FROM Aeroporto a WHERE a.id = :id", Aeroporto.class)
					.setParameter("id", id).getSingleResult();
			
		} catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
