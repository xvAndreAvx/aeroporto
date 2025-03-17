package com.interfacce;

import java.util.List;

import com.aeroportoEntity.Utente;

import jakarta.persistence.EntityManager;

public interface UtenteInterfacciaDAO {

	void createUtente(Utente utente, EntityManager em);
	
	void updateUtente(Utente utente, EntityManager em);
	
	void deleteUtente(Utente utente, EntityManager em);
	
	Utente findByIdUtente(Long id, EntityManager em);
	
	Utente findByEmailUtente(String email, EntityManager em);
	
	Utente findLogin(String email, String password, EntityManager em);
	
	List<Utente> getAllUtente(EntityManager em);
}
