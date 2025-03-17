package com.interfacce;

import java.util.List;

import com.aeroportoEntity.Biglietto;
import com.aeroportoEntity.Prenotazione;

import jakarta.persistence.EntityManager;

public interface BigliettoInterfacciaDAO {
	
	void createBiglietto(Biglietto biglietto, EntityManager em);
	List<Biglietto> getAll(EntityManager em);
	Biglietto findById(Long id , EntityManager em);
	void updateBiglietto(Biglietto biglietto, Prenotazione prenotazione, EntityManager em);
	Biglietto findByIdVolo(Long idVolo , EntityManager em);
	Biglietto findByIdVoloEPrenotazione(Long id, Prenotazione prenotazione, EntityManager em);
	Biglietto findByPosto(String posto, Long idBiglietto, EntityManager em);



}
