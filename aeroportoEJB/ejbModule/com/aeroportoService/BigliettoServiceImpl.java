package com.aeroportoService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.DTO.BigliettoDTO;
import com.DTO.PrenotazioneDTO;
import com.aeroportoDB.BigliettoDAO;
import com.aeroportoDB.PrenotazioneDAO;
import com.aeroportoDB.VoloDAO;
import com.aeroportoEntity.Biglietto;
import com.aeroportoEntity.Prenotazione;
import com.convertitore.BigliettoConvertitore;
import com.exception.ValidazioneException;
import com.producer.EntityManagerProducer;
import com.serviceInt.BigliettoService;
import com.utility.GeneraCodice;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;

@Stateless
public class BigliettoServiceImpl implements BigliettoService {

	private final BigliettoDAO bigliettoDAO = new BigliettoDAO();
	private final PrenotazioneDAO prenotazioneDAO = new PrenotazioneDAO();
	private final VoloDAO voloDAO = new VoloDAO();

	@Override
	public BigliettoDTO insertBiglietto(BigliettoDTO bigliettoDTO) throws Exception {
		EntityManager em = null;
		try {
			Biglietto bigliettoNew;
			try {
				bigliettoNew = BigliettoConvertitore.toEntity(bigliettoDTO);
			} catch (IllegalArgumentException e) {
				throw new ValidazioneException("I dati inseriti non sono validi");
			}

			if (bigliettoDTO.getCosto() <= 0) {
				throw new ValidazioneException("Il costo del biglietto deve essere maggiore di zero");
			}

			em = EntityManagerProducer.getEntityManager();

			em.getTransaction().begin();
			bigliettoDAO.createBiglietto(bigliettoNew, em);
			em.getTransaction().commit();

			BigliettoDTO risultato = BigliettoConvertitore.toDTO(bigliettoNew);
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
	public List<BigliettoDTO> getAll() throws Exception {
		EntityManager em = null;

		try {

			List<BigliettoDTO> listaBigliettoDTO = new ArrayList<BigliettoDTO>();
			List<Biglietto> listaBiglietto = new ArrayList<Biglietto>();

			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();
			listaBiglietto = bigliettoDAO.getAll(em);
			em.getTransaction().commit();

			for (Biglietto biglietto : listaBiglietto) {
				listaBigliettoDTO.add(BigliettoConvertitore.toDTO(biglietto));
			}

			return listaBigliettoDTO;
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
	public Biglietto findById(Long id) throws Exception {
		EntityManager em = null;
		try {
			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();
			return bigliettoDAO.findById(id, em);
		} catch (Exception e) {
			throw e;
		} finally {
			if (em != null) {
				EntityManagerProducer.getInstance().closeEntityManager(em);
			}
		}
	}

	@Override
	public BigliettoDTO findByIdVolo(Long idVolo) throws Exception {
		EntityManager em = null;
		try {
			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();
			return BigliettoConvertitore.toDTO(bigliettoDAO.findByIdVolo(idVolo, em));
		} catch (Exception e) {
			throw e;
		} finally {
			if (em != null) {
				EntityManagerProducer.getInstance().closeEntityManager(em);
			}
		}
	}

	@Override
	public void updateBiglietto(BigliettoDTO bigliettoDTO, PrenotazioneDTO prenotazioneDTO) throws Exception {
		EntityManager em = null;
		try {
			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();

			Prenotazione prenotazione = null;
			if (prenotazioneDTO != null && prenotazioneDTO.getCodice() != null
					&& !prenotazioneDTO.getCodice().isEmpty()) {
				prenotazione = prenotazioneDAO.findPrenotazione(prenotazioneDTO.getCodice(), em);
				if (prenotazione == null) {
					throw new Exception("Prenotazione non trovata con codice: " + prenotazioneDTO.getCodice());
				}
			} else {
				throw new Exception("Prenotazione non valida: codice mancante");
			}

			List<Biglietto> listaBiglietto = bigliettoDAO.getAll(em);

			String codiceBiglietto = GeneraCodice.generaCodiceBiglietto();
			boolean codiceUnico = false;

			while (!codiceUnico) {
				codiceUnico = true;
				for (Biglietto biglietto : listaBiglietto) {
					String codiceBigliettoEsistente = biglietto.getCodice();
					if (codiceBigliettoEsistente != null && codiceBigliettoEsistente.equals(codiceBiglietto)) {
						if (biglietto.getVolo().getDataArrivo().isBefore(LocalDateTime.now()))
							codiceBiglietto = GeneraCodice.generaCodiceBiglietto();
						codiceUnico = false;
						break;
					}
				}
			}

			bigliettoDTO.setCodiceBiglietto(codiceBiglietto);

			if (bigliettoDTO.getPrenotazione() != null) {
				BigliettoDTO bigliettoNew = new BigliettoDTO();
				bigliettoNew.setVolo(bigliettoDTO.getVolo());
				bigliettoNew.setCosto(bigliettoDTO.getCosto());
				bigliettoNew.setCodiceBiglietto(codiceBiglietto);
				Biglietto bigliettoNuovo = BigliettoConvertitore.toEntity(insertBiglietto(bigliettoNew));
				voloDAO.updateDisponibilita(voloDAO.findById(bigliettoNuovo.getVolo().getId(), em), em);

				bigliettoDAO.updateBiglietto(bigliettoNuovo, prenotazione, em);
				em.getTransaction().commit();
			} else {
				Biglietto biglietto = BigliettoConvertitore.toEntity(bigliettoDTO);

				if (biglietto.getCodice() == null) {
					biglietto.setCodice(codiceBiglietto);
				}

				voloDAO.updateDisponibilita(voloDAO.findById(biglietto.getVolo().getId(), em), em);

				bigliettoDAO.updateBiglietto(biglietto, prenotazione, em);
				em.getTransaction().commit();
			}

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
	public BigliettoDTO findByIdVoloEPrenotazione(Long idVolo, Prenotazione prenotazione) throws Exception {
		EntityManager em = null;
		try {
			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();
			return BigliettoConvertitore.toDTO(bigliettoDAO.findByIdVoloEPrenotazione(idVolo,prenotazione, em));
		} catch (Exception e) {
			throw e;
		} finally {
			if (em != null) {
				EntityManagerProducer.getInstance().closeEntityManager(em);
			}
		}
	}

	@Override
	public Biglietto findByPosto(String posto, Long idBiglietto) throws Exception {
		EntityManager em = null;
		try {
			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();
			bigliettoDAO.findByPosto(posto,idBiglietto, em);
			return bigliettoDAO.findByPosto(posto,idBiglietto, em);
		} catch (Exception e) {
			throw e;
		} finally {
			if (em != null) {
				EntityManagerProducer.getInstance().closeEntityManager(em);
			}
		}
	}

}
