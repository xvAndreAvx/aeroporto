package com.aeroportoDB;

import java.util.List;

import com.aeroportoEntity.Utente;
import com.interfacce.UtenteInterfacciaDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class UtenteDAO implements UtenteInterfacciaDAO {

	@Override
	public void createUtente(Utente utente, EntityManager em) {
		em.persist(utente);

	}
	
	//da modificare perche update non deve modificare il ruolo
	@Override
	public void updateUtente(Utente utente, EntityManager em) {
		em.merge(utente);
	}

	@Override
	public void deleteUtente(Utente utente, EntityManager em) {

		if (em.find(Utente.class, utente) != null) {
			em.remove(utente);
		}

	}

	@Override
	public Utente findByIdUtente(Long id, EntityManager em) {
		Utente utente = new Utente();
		utente.setId(id);
		if (em.contains(utente)) {
			utente = em.find(utente.getClass(), utente.getId());
			return utente;
		} else {
			return null;
		}
	}

	@Override
	public Utente findByEmailUtente(String email, EntityManager em) {
		try {
			return em.createQuery("SELECT u FROM Utente u WHERE u.email = :email", Utente.class)
					.setParameter("email", email).getSingleResult();
		} catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	@Override
	public List<Utente> getAllUtente(EntityManager em) {
	    try {
	        return em.createQuery("SELECT u FROM Utente u", Utente.class).getResultList();
	    } catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Utente findLogin(String email, String password, EntityManager em) {
		try {
			return em
					.createQuery("SELECT u FROM Utente u WHERE u.email = :email AND u.password = :password",
							Utente.class)
					.setParameter("email", email).setParameter("password", password).getSingleResult();
		} catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}
