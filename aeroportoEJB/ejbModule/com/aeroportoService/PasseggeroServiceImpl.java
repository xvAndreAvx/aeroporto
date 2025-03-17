package com.aeroportoService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.DTO.PasseggeroDTO;
import com.aeroportoDB.PasseggeroDAO;
import com.aeroportoEntity.Passeggero;
import com.convertitore.PasseggeroConvertitore;
import com.exception.ValidazioneException;
import com.producer.EntityManagerProducer;
import com.serviceInt.PasseggeroService;
import com.validazioni.UtenteValidazione;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;

@Stateless
public class PasseggeroServiceImpl implements PasseggeroService {

	private final PasseggeroDAO passeggeroDAO = new PasseggeroDAO();

	@Override
	public PasseggeroDTO insertPasseggero(PasseggeroDTO passeggeroDTO) throws Exception {
		EntityManager em = null;
		LocalDateTime now = LocalDateTime.now();
		Passeggero passeggeroNew = null;
		
		UtenteValidazione.validaFormatoDatiPasseggero(passeggeroDTO.getNome(), passeggeroDTO.getCognome(), passeggeroDTO.getCodiceFiscale());
		
		try {
			try {
				passeggeroNew = PasseggeroConvertitore.toEntity(passeggeroDTO);
			} catch (IllegalArgumentException e) {
				throw new ValidazioneException("I dati inseriti non sono validi");
			}

			em = EntityManagerProducer.getEntityManager();
			List<Passeggero> listaPasseggeri = passeggeroDAO.getAll(em);

			for (Passeggero passeggero : listaPasseggeri) {
			    if (passeggeroNew.getPosto() != null && 
			        passeggero.getPosto() != null && 
			        passeggeroNew.getPosto().equals(passeggero.getPosto()) &&
			        passeggero.getBiglietto() != null && 
			        passeggero.getBiglietto().getVolo() != null &&
			        passeggeroNew.getBiglietto() != null &&
			        passeggeroNew.getBiglietto().getVolo() != null &&
			        passeggero.getBiglietto().getVolo().getId().equals(passeggeroNew.getBiglietto().getVolo().getId())) {
			        
			        throw new ValidazioneException("Il posto è già assegnato per questo volo");
			    }
			}

			if (passeggeroNew.getBiglietto().getVolo().getDataPartenza().isBefore(now)) {
				throw new ValidazioneException("Il volo è già partito");
			}

			em.getTransaction().begin();
			passeggeroDAO.createPasseggero(passeggeroNew, em);
			em.getTransaction().commit();

			PasseggeroDTO risultato = PasseggeroConvertitore.toDTO(passeggeroNew);
			return risultato;

		} catch (Exception e) {
			e.printStackTrace();
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
	public Passeggero findById(Long id) throws Exception {
		EntityManager em = null;
		try {
			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();
			return passeggeroDAO.findById(id, em);
		} catch (Exception e) {
			throw e;
		} finally {
			if (em != null) {
				EntityManagerProducer.getInstance().closeEntityManager(em);
			}
		}
	}

	@Override
	public List<PasseggeroDTO> getAll() throws Exception {
		EntityManager em = null;

		try {

			List<PasseggeroDTO> listaPasseggeroDTO = new ArrayList<PasseggeroDTO>();
			List<Passeggero> listaPasseggero = new ArrayList<Passeggero>();

			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();
			listaPasseggero = passeggeroDAO.getAll(em);
			em.getTransaction().commit();

			for (Passeggero passeggero : listaPasseggero) {
				listaPasseggeroDTO.add(PasseggeroConvertitore.toDTO(passeggero));
			}

			return listaPasseggeroDTO;
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
	public Passeggero findByPosto(String posto, Long idBiglietto) throws Exception {
		EntityManager em = null;
		try {
			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();
			passeggeroDAO.findByPosto(posto,idBiglietto, em);
			return passeggeroDAO.findByPosto(posto,idBiglietto, em);
		} catch (Exception e) {
			throw e;
		} finally {
			if (em != null) {
				EntityManagerProducer.getInstance().closeEntityManager(em);
			}
		}
	}

	
}
