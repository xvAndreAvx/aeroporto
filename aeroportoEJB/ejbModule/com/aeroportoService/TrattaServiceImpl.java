package com.aeroportoService;

import java.util.ArrayList;
import java.util.List;

import com.DTO.TrattaDTO;
import com.aeroportoDB.TrattaDAO;
import com.aeroportoEntity.Tratta;
import com.convertitore.TrattaConvertitore;
import com.exception.ValidazioneException;
import com.producer.EntityManagerProducer;
import com.serviceInt.TrattaService;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;

@Stateless
public class TrattaServiceImpl implements TrattaService {
	
	private final TrattaDAO trattaDAO = new TrattaDAO();

	@Override
	public TrattaDTO insertTratta(TrattaDTO trattaDTO) throws Exception {
		EntityManager em = null;

		try {
			Tratta tratta;
			try {
				tratta = TrattaConvertitore.toEntity(trattaDTO);
			} catch (IllegalArgumentException e) {
				throw new ValidazioneException("I dati inseriti non sono validi");
			}

			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();
			trattaDAO.createTratta(tratta, em);
			em.getTransaction().commit();

			return trattaDTO;
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
	public List<TrattaDTO> getAll() throws Exception {
		EntityManager em = null;

		try {
			
			List<TrattaDTO> listaTrattaDTO = new ArrayList<TrattaDTO>();
			List<Tratta> listaTratta = new ArrayList<Tratta>();
			
			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();			
			listaTratta = trattaDAO.getAll(em);
			em.getTransaction().commit();
			
			for(Tratta tratta : listaTratta) {
				listaTrattaDTO.add(TrattaConvertitore.toDTO(tratta));
			}

			return listaTrattaDTO;
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
	
	public Tratta findTrattaById(Long id) throws Exception {
	    EntityManager em = null;
	    try {
	        em = EntityManagerProducer.getEntityManager();
	        return trattaDAO.findById(id, em);
	    } catch (Exception e) {
	        throw e;
	    } finally {
	        if (em != null) {
	            EntityManagerProducer.getInstance().closeEntityManager(em);
	        }
	    }
	}

}
