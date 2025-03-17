package com.aeroportoService;

import com.DTO.UtenteDTO;
import com.aeroportoDB.UtenteDAO;
import com.aeroportoEntity.Utente;
import com.convertitore.UtenteConvertitore;
import com.criptatore.PasswordCript;
import com.exception.ValidazioneException;
import com.producer.EntityManagerProducer;
import com.serviceInt.UtenteService;
import com.validazioni.UtenteValidazione;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;

@Stateless
public class UtenteServiceImpl implements UtenteService {

	private final UtenteDAO utenteDAO = new UtenteDAO();

	@Override
	public UtenteDTO insertUtente(UtenteDTO utenteDTO) throws ValidazioneException {
		EntityManager em = null;

		try {
			
			UtenteValidazione.validaFormatoCredenziali(utenteDTO.getEmail(), utenteDTO.getPassword());
			UtenteValidazione.validaFormatoDatiAnagrafici(utenteDTO.getNome(), utenteDTO.getCognome(),
					utenteDTO.getEta());
			Utente utente;
			try {
				utente = UtenteConvertitore.toEntity(utenteDTO);
			} catch (IllegalArgumentException e) {
				throw new ValidazioneException("I dati inseriti non sono validi");
			}

			utente.setPassword(PasswordCript.criptPassword(utenteDTO.getPassword()));

			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();
			utenteDAO.createUtente(utente, em);
			em.getTransaction().commit();

			return utenteDTO;

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
	public UtenteDTO login(UtenteDTO utenteDTO) throws ValidazioneException {
		EntityManager em = null;

		try {
			Utente utente;

			utenteDTO.setPassword(PasswordCript.criptPassword(utenteDTO.getPassword()));

			utente = UtenteConvertitore.toEntity(utenteDTO);

			em = EntityManagerProducer.getEntityManager();
			em.getTransaction().begin();
			utente = utenteDAO.findLogin(utente.getEmail(), utente.getPassword(), em);
			utenteDTO = UtenteConvertitore.toDTO(utente);
			em.getTransaction().commit();

			if (utente == null) {
				throw new ValidazioneException("Credenziali non valide");
			}

			return utenteDTO;
		} catch (ValidazioneException va) {

			throw va;

		} catch (Exception e) {

			e.printStackTrace();
			throw new ValidazioneException("Si è verificato un errore durante il login: " + e.getMessage());

		} finally {
			if (em != null) {
				EntityManagerProducer.getInstance().closeEntityManager(em);
			}
		}
	}

	@Override
	public Utente verificaUtente(UtenteDTO utenteDTO) throws Exception {
	    EntityManager em = null;
	    try {
	        String passwordCriptata = PasswordCript.criptPassword(utenteDTO.getPassword());
	        
	        em = EntityManagerProducer.getEntityManager();
	        em.getTransaction().begin();
	        
	        Utente utente = utenteDAO.findLogin(utenteDTO.getEmail(), passwordCriptata, em);
	        
	        em.getTransaction().commit();
	        
	        if (utente == null) {
	            throw new ValidazioneException("Credenziali non valide");
	        }
	        
	        return utente;
	    } catch (ValidazioneException va) {
	        throw va;
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new ValidazioneException("Si è verificato un errore durante il login: " + e.getMessage());
	    } finally {
	        if (em != null) {
	            EntityManagerProducer.getInstance().closeEntityManager(em);
	        }
	    }
	}

}
