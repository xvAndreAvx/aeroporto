package com.aeroportoService;

import java.util.ArrayList;
import java.util.List;

import com.DTO.AereoDTO;
import com.aeroportoDB.AereoDAO;
import com.aeroportoEntity.Aereo;
import com.convertitore.AereoConvertitore;
import com.exception.ValidazioneException;
import com.producer.EntityManagerProducer;
import com.serviceInt.AereoService;
import com.validazioni.AereoValidazione;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;

@Stateless
public class AereoServiceImpl implements AereoService {

	private final AereoDAO aereoDAO = new AereoDAO();

	@Override
	public AereoDTO insertAereo(AereoDTO aereoDTO) throws ValidazioneException {
	    EntityManager em = null;
	    Aereo aereo = null;
	    try {
	    	
	    	AereoValidazione.validaModelloConCapienza(aereoDTO.getModello(), aereoDTO.getCapienza());
	    	
	        try {
	        	aereo = AereoConvertitore.toEntity(aereoDTO);
			} catch (IllegalArgumentException e) {
				throw new ValidazioneException("I dati inseriti non sono validi");
			}
	        
	        em = EntityManagerProducer.getEntityManager();
	        em.getTransaction().begin();
	        aereoDAO.createAereo(aereo, em);
	        em.getTransaction().commit();
	        
	        return aereoDTO;
	    } catch (ValidazioneException va) {

			throw va;    
	    } catch (Exception e) {
	        if (em != null && em.getTransaction().isActive()) {
	            em.getTransaction().rollback();
	        }
	        e.printStackTrace();
	        throw new RuntimeException("Errore durante l'inserimento dell'aereo", e);
	    } finally {
	        if (em != null) {
	            EntityManagerProducer.getInstance().closeEntityManager(em);
	        }
	    }
	}


	@Override
	public Aereo findyByIdAereo(Long id) throws Exception {
		EntityManager em = null;
	    try {
	    	Aereo aereo = null;	        
	        em = EntityManagerProducer.getEntityManager();
	        em.getTransaction().begin();
	        aereo = aereoDAO.findByIdAereo(id, em);
	        em.getTransaction().commit();
	        
	        return aereo;
	        
	    } catch (Exception e) {
	        if (em != null && em.getTransaction().isActive()) {
	            em.getTransaction().rollback();
	        }
	        e.printStackTrace();
	        throw new RuntimeException("Errore durante la ricerca dell'aereo", e);
	    } finally {
	        if (em != null) {
	            EntityManagerProducer.getInstance().closeEntityManager(em);
	        }
	    }
	}
	
	@Override
	public List<AereoDTO> getAll() throws Exception {
		EntityManager em = null;

		try {
			
			List<AereoDTO> listaAereoDTO = new ArrayList<AereoDTO>();
			List<Aereo> listaAereo = new ArrayList<Aereo>();
			
			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();			
			listaAereo = aereoDAO.getAll(em);
			em.getTransaction().commit();
			
			for(Aereo aereo : listaAereo) {
				listaAereoDTO.add(AereoConvertitore.toDTO(aereo));
			}

			return listaAereoDTO;
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
