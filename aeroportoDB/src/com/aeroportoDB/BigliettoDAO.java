package com.aeroportoDB;

import java.util.List;

import com.aeroportoEntity.Biglietto;
import com.aeroportoEntity.Prenotazione;
import com.interfacce.BigliettoInterfacciaDAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class BigliettoDAO implements BigliettoInterfacciaDAO{

	@Override
	public void createBiglietto(Biglietto biglietto, EntityManager em) {
		em.persist(biglietto);
	}
	
	@Override
	public List<Biglietto> getAll(EntityManager em) {
		try {
	        return em.createQuery("SELECT b FROM Biglietto b "
	        		+ "JOIN FETCH b.volo "
	        		+ "JOIN FETCH b.prenotazione ", Biglietto.class)
	        		.getResultList();
		} catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Biglietto findById(Long id, EntityManager em) {
		try {
			return em.createQuery("SELECT b FROM Biglietto b "
	        		+ "LEFT JOIN FETCH b.volo "
	        		+ "LEFT JOIN FETCH b.prenotazione "
	        		+ "WHERE b.id = :id ", Biglietto.class)
					.setParameter("id", id)
	        		.getSingleResult();
		} catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	

	@Override
	public void updateBiglietto(Biglietto biglietto, Prenotazione prenotazione, EntityManager em) {
		try {
	        int updatedRows = em.createQuery("UPDATE Biglietto b "
	                + "SET b.prenotazione = :prenotazione, "
	                + "b.codice = :codice "
	                + "WHERE b.id = :id")
	            .setParameter("prenotazione", prenotazione)
	            .setParameter("id", biglietto.getId())
	            .setParameter("codice", biglietto.getCodice())
	            .executeUpdate();

	        if (updatedRows == 0) {
	            throw new RuntimeException("Nessun biglietto aggiornato con ID: " + biglietto.getId());
	        }
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Biglietto findByIdVolo(Long idVolo, EntityManager em) {
		try {
	        return em.createQuery("SELECT b FROM Biglietto b " +
	                "JOIN FETCH b.volo v " +
	                "LEFT JOIN FETCH b.prenotazione " +
	                "WHERE v.id = :idVolo", Biglietto.class)
	                .setParameter("idVolo", idVolo)
	                .getSingleResult();
		} catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public Biglietto findByIdVoloEPrenotazione(Long idVolo, Prenotazione prenotazione, EntityManager em) {
	    try {
	        if (prenotazione == null) {
	            return em.createQuery("SELECT b FROM Biglietto b "
	                + "LEFT JOIN FETCH b.volo v "
	                + "WHERE v.id = :idVolo "
	                + "AND b.prenotazione IS NULL", Biglietto.class)
		            .setParameter("idVolo", idVolo)
	                .getSingleResult();
	        } else {
	            return em.createQuery("SELECT b FROM Biglietto b "
	                + "LEFT JOIN FETCH b.volo v "
	                + "LEFT JOIN FETCH b.prenotazione "
	                + "WHERE v.id = :idVolo "
	                + "AND b.prenotazione = :prenotazione", Biglietto.class)
	            		.setParameter("idVolo", idVolo)
	                .setParameter("prenotazione", prenotazione)
	                .getSingleResult();
	        }
	    } catch(NoResultException n) {
			throw n;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Biglietto findByPosto(String posto, Long idBiglietto, EntityManager em) {
	    try {
	        return em.createQuery("SELECT p.biglietto FROM Passeggero p "
	            + "WHERE p.posto = :posto "
	            + "AND p.biglietto.id = :idBiglietto", Biglietto.class)
	            .setParameter("posto", posto)
	            .setParameter("idBiglietto", idBiglietto)
	            .getSingleResult();
	    } catch(NoResultException n) {
	        return null; 
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw e;
	    }
	}
}
