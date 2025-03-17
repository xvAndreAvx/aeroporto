package com.producer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerProducer {
    private static volatile EntityManagerFactory emf;
    private static volatile EntityManagerProducer instance;
    
    private EntityManagerProducer() {
        initializeEMF();
    }
    
    private void initializeEMF() {
        try {
            emf = Persistence.createEntityManagerFactory("Aeroporto");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Errore nell'inizializzazione dell'EntityManagerFactory", e);
        }
    }

    public static EntityManagerProducer getInstance() {
        if (instance == null) {
            synchronized (EntityManagerProducer.class) {
                if (instance == null) {
                    instance = new EntityManagerProducer();
                }
            }
        }
        return instance;
    }

    public static EntityManager getEntityManager() {
        EntityManagerProducer producer = getInstance();
        if (emf == null) {
            synchronized (EntityManagerProducer.class) {
                if (emf == null) {
                    producer.initializeEMF();
                }
            }
        }
        return emf.createEntityManager();
    }

    public void closeEntityManager(EntityManager em) {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}