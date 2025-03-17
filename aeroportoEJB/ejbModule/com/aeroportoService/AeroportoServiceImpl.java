package com.aeroportoService;

import java.util.ArrayList;
import java.util.List;

import com.DTO.AeroportoDTO;
import com.aeroportoDB.AeroportoDAO;
import com.aeroportoEntity.Aeroporto;
import com.convertitore.AeroportoConvertitore;
import com.exception.ValidazioneException;
import com.producer.EntityManagerProducer;
import com.serviceInt.AeroportoService;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;

@Stateless
public class AeroportoServiceImpl implements AeroportoService {

	private final AeroportoDAO aeroportoDAO = new AeroportoDAO();

	@Override
	public AeroportoDTO insertAeroporto(AeroportoDTO aeroportoDTO) throws Exception {
		EntityManager em = null;

		try {
			Aeroporto aeroporto;
			try {
				aeroporto = AeroportoConvertitore.toEntity(aeroportoDTO);
			} catch (IllegalArgumentException e) {
				throw new ValidazioneException("I dati inseriti non sono validi");
			}

			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();
			aeroportoDAO.createAeroporto(aeroporto, em);
			em.getTransaction().commit();

			return aeroportoDTO;
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
	public AeroportoDTO findByName(AeroportoDTO aeroportoDTO) throws Exception {
		return null;
	}

	@Override
	public List<AeroportoDTO> getAll() throws Exception {
		EntityManager em = null;

		try {
			
			List<AeroportoDTO> listaAeroportoDTO = new ArrayList<AeroportoDTO>();
			List<Aeroporto> listaAeroporto = new ArrayList<Aeroporto>();
			
			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();			
			listaAeroporto = aeroportoDAO.getAll(em);
			em.getTransaction().commit();
			
			for(Aeroporto a : listaAeroporto) {
				listaAeroportoDTO.add(AeroportoConvertitore.toDTO(a));
			}

			return listaAeroportoDTO;
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
	public Aeroporto findById(Long id) throws Exception {
		EntityManager em = null;

		try {
			
			Aeroporto aeroporto;
			
			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();			
			aeroporto = aeroportoDAO.findById(id,em);
			em.getTransaction().commit();

			return aeroporto;
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
