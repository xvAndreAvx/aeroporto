package com.interfacce;

import java.time.LocalDateTime;
import java.util.List;

import com.aeroportoEntity.Prenotazione;

import jakarta.persistence.EntityManager;

public interface PrenotazioneInterfacciaDAO {
	
	void createTratta(Prenotazione prenotazione, EntityManager em);
	List<Prenotazione> getAll(EntityManager em);
	Prenotazione findPrenotazione(String codice, EntityManager em);
	void inserisciData(Prenotazione prenotazione, LocalDateTime data, EntityManager em);

}
