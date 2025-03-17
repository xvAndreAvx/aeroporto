package com.aeroportoService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.DTO.PrenotazioneDTO;
import com.aeroportoDB.PrenotazioneDAO;
import com.aeroportoEntity.Prenotazione;
import com.convertitore.PrenotazioneConvertitore;
import com.exception.ValidazioneException;
import com.producer.EntityManagerProducer;
import com.serviceInt.PrenotazioneService;
import com.utility.GeneraCodice;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;

@Stateless
public class PrenotazioneServiceImpl implements PrenotazioneService {
	
	private final PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();

	@Override
	public PrenotazioneDTO insertPrenotazione(PrenotazioneDTO prenotazioneDTO) throws Exception {
		EntityManager em = null;

	    try {
	        Prenotazione prenotazione;
	        try {
	            prenotazione = PrenotazioneConvertitore.toEntity(prenotazioneDTO);
	        } catch (IllegalArgumentException e) {
	            throw new ValidazioneException("I dati inseriti non sono validi");
	        }

	        em = EntityManagerProducer.getEntityManager();
	        String codicePrenotazione = GeneraCodice.generaCodiceVolo();
	        prenotazione.setCodice(codicePrenotazione);
	        
	        em.getTransaction().begin();
	        prenotazioneDAO.createTratta(prenotazione, em);
	        em.getTransaction().commit();

	        PrenotazioneDTO risultato = PrenotazioneConvertitore.toDTO(prenotazione);
	        return risultato;
	    } catch (Exception e) {
	        if (em != null && em.getTransaction().isActive()) {
	            em.getTransaction().rollback();
	        }
	        throw e;
	    } finally {
	        if (em != null) {
	            EntityManagerProducer.getInstance().closeEntityManager(em);
	        }
	    }
	
	}
	
	@Override
	public List<PrenotazioneDTO> getAll() throws Exception {
		EntityManager em = null;

		try {

			List<PrenotazioneDTO> listaPrenotazioneDTO = new ArrayList<PrenotazioneDTO>();
			List<Prenotazione> listaPrenotazione = new ArrayList<Prenotazione>();

			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();
			listaPrenotazione = prenotazioneDAO.getAll(em);
			em.getTransaction().commit();

			for (Prenotazione prenotazione : listaPrenotazione) {
				listaPrenotazioneDTO.add(PrenotazioneConvertitore.toDTO(prenotazione));
			}

			return listaPrenotazioneDTO;
		} catch (Exception e) {
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}

			throw e;
		} finally {
			if (em != null) {
				EntityManagerProducer.getInstance().closeEntityManager(em);
			}
		}
	}

	@Override
	public Prenotazione findByCodice(String codice) throws Exception {
		EntityManager em = null;
	    try {
	        em = EntityManagerProducer.getEntityManager();
	        return prenotazioneDAO.findPrenotazione(codice, em);
	    } catch (Exception e) {
	        throw e;
	    } finally {
	        if (em != null) {
	            EntityManagerProducer.getInstance().closeEntityManager(em);
	        }
	    }
	}

	@Override
	public PrenotazioneDTO inserisciData(PrenotazioneDTO prenotazioneDTO) throws Exception {
	    EntityManager em = null;
	    LocalDateTime now = LocalDateTime.now();
		try {
	        em = EntityManagerProducer.getEntityManager();
	        em.getTransaction().begin();
	        
	        Prenotazione prenotazione = PrenotazioneConvertitore.toEntity(prenotazioneDTO);
	        prenotazioneDAO.inserisciData(prenotazione, now, em);
	        prenotazioneDTO.setDataAcquisto(now);
	        em.getTransaction().commit();
	        
	        return prenotazioneDTO;
	        
	    } catch (Exception e) {
	        if (em != null && em.getTransaction().isActive()) {
	            em.getTransaction().rollback();
	        }
	        throw e;
	    } finally {
	        if (em != null) {
	            EntityManagerProducer.getInstance().closeEntityManager(em);
	        }
	    }
	}

}
