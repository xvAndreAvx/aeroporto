package com.aeroportoService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.DTO.VoloDTO;
import com.aeroportoDB.AereoDAO;
import com.aeroportoDB.VoloDAO;
import com.aeroportoEntity.Aereo;
import com.aeroportoEntity.Volo;
import com.convertitore.VoloConvertitore;
import com.exception.ValidazioneException;
import com.producer.EntityManagerProducer;
import com.serviceInt.VoloService;
import com.utility.GeneraCodice;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;

@Stateless
public class VoloServiceImpl implements VoloService {

	private final VoloDAO voloDAO = new VoloDAO();
	private final AereoDAO aereoDAO = new AereoDAO();

	@Override
	public Volo insertVolo(VoloDTO voloDTO) throws Exception {
		EntityManager em = null;
		try {
			Volo voloNew;
			try {
				voloNew = VoloConvertitore.toEntity(voloDTO);
			} catch (IllegalArgumentException e) {
				throw new ValidazioneException("I dati inseriti non sono validi");
			}
			em = EntityManagerProducer.getEntityManager();
			String codiceVolo = GeneraCodice.generaCodiceVolo();
			boolean codiceUnico = false;
			List<Volo> listaVolo = voloDAO.getAll(em);
			while (!codiceUnico) {
				codiceUnico = true;
				for (Volo volo : listaVolo) {
					if (volo.getCodiceVolo().equals(codiceVolo)) {
						if (volo.getDataArrivo().isBefore(LocalDateTime.now())) {
							codiceVolo = GeneraCodice.generaCodiceVolo();
							codiceUnico = false;
							break;
						}
					}
				}
			}

			boolean codiceaereo = false;
			List<Aereo> listaAereo = aereoDAO.getAll(em);
			while (!codiceaereo) {
				for (Aereo aereo : listaAereo) {
					if (aereo.getId().equals(voloDTO.getAereo().getId())) {
						voloNew.setPostiDisponibili(aereo.getCapienza());
						codiceaereo = true;
						break;
					}
				}
			}

			voloNew.setCodiceVolo(codiceVolo);
			em.getTransaction().begin();
			voloDAO.createVolo(voloNew, em);
			em.getTransaction().commit();

			return voloNew;
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
	public List<VoloDTO> getAll() throws Exception {
		EntityManager em = null;

		try {

			List<VoloDTO> listaVoloDTO = new ArrayList<VoloDTO>();
			List<Volo> listaVolo = new ArrayList<Volo>();

			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();
			listaVolo = voloDAO.getAll(em);
			em.getTransaction().commit();

			for (Volo volo : listaVolo) {
				listaVoloDTO.add(VoloConvertitore.toDTO(volo));
			}

			return listaVoloDTO;
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
	public Volo findVoloById(Long id) throws Exception {
		EntityManager em = null;
		try {
			em = EntityManagerProducer.getEntityManager();
			return voloDAO.findById(id, em);
		} catch (Exception e) {
			throw e;
		} finally {
			if (em != null) {
				EntityManagerProducer.getInstance().closeEntityManager(em);
			}
		}
	}

}
