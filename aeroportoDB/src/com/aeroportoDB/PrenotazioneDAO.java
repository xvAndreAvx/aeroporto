package com.aeroportoDB;

import java.time.LocalDateTime;
import java.util.List;

import com.aeroportoEntity.Prenotazione;
import com.interfacce.PrenotazioneInterfacciaDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class PrenotazioneDAO implements PrenotazioneInterfacciaDAO {

	@Override
	public void createTratta(Prenotazione prenotazione, EntityManager em) {
		em.persist(prenotazione);
	}

	@Override
	public List<Prenotazione> getAll(EntityManager em) {
		try {
			return em.createQuery("SELECT p FROM Prenotazione p " + "JOIN FETCH p.utente ", Prenotazione.class)
					.getResultList();
		} catch (NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Prenotazione findPrenotazione(String codice, EntityManager em) {
		try {
			return em.createQuery("SELECT p FROM Prenotazione p " + "JOIN FETCH p.utente " + "WHERE p.codice = :codice",
					Prenotazione.class).setParameter("codice", codice).getSingleResult();
		} catch (NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void inserisciData(Prenotazione prenotazione, LocalDateTime data, EntityManager em) {
		try {
			int updatedRows = em
					.createQuery("UPDATE Prenotazione p SET p.dataAcquisto = :data WHERE p.codice = :codice")
					.setParameter("data", data).setParameter("codice", prenotazione.getCodice()).executeUpdate();

			if (updatedRows == 0) {
				throw new RuntimeException("Nessuna prenotazione aggiornata con codice: " + prenotazione.getCodice());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Errore durante l'aggiornamento della prenotazione: " + e.getMessage());
		}
	}

}
