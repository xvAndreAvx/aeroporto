package com.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.DTO.AereoDTO;
import com.aeroportoEntity.Aereo;
import com.exception.ValidazioneException;
import com.serviceInt.AereoService;
import com.utility.CostantiErrori;
import com.utility.MessaggioErr;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Named;

@Named("aereoController")
@SessionScoped
public class AereoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(AereoBean.class.getName());
	private MessaggioErr messaggio;
	private AereoDTO aereoDTO;
	private Aereo aereo;
	private Long id;
	private List<AereoDTO> listaAereo;

	@EJB
	private AereoService aereoService;

	@PostConstruct
	public void init() {
		aereo = new Aereo();
		aereoDTO = new AereoDTO();
		listaAereo = new ArrayList<>();
		caricaAerei();
		id = null;
		messaggio = new MessaggioErr();
	}

	public void caricaAerei() {
		try {
			listaAereo = aereoService.getAll();
			logger.info("Caricati " + listaAereo.size() + " aerei");

		} catch (Exception e) {
			logger.severe("Errore nel caricamento degli aerei: " + e.getMessage());
		}
	}

	public void registrazione() {

		try {
			aereoService.insertAereo(aereoDTO);
			aereoDTO = new AereoDTO();
			messaggio.addMessage(FacesMessage.SEVERITY_INFO, CostantiErrori.CREAZIONE_AEREO);

		} catch (ValidazioneException ve) {
			logger.warning("Validazione fallita durante la registrazione: " + ve.getMessage());

			// Gestione dei messaggi di errore di validazione
			List<String> errori = ve.getErrori();
			if (errori != null && !errori.isEmpty()) {
				for (String errore : errori) {
					messaggio.addMessage(FacesMessage.SEVERITY_ERROR, errore);
				}
			} else {
				// Se non ci sono errori dettagliati, mostra il messaggio generale
				messaggio.addMessage(FacesMessage.SEVERITY_ERROR, ve.getMessage());
			}

		} catch (Exception e) {
			logger.severe(CostantiErrori.ERRORE_CREA_AEREO + " " + e.getMessage());
			messaggio.addMessage(FacesMessage.SEVERITY_FATAL, CostantiErrori.ERRORE_CREA_AEREO);

		}
	}

	public void trovaAereo() {
		try {
			
			if (id == null) {
				throw new Exception("id aereo non valido");
			}
			
			setAereo(aereoService.findyByIdAereo(id));
			
			logger.info("Successo nel recupero aereo");
			logger.info("ID: " + aereo.getId());
			logger.info("modello: " + aereo.getModello());
			logger.info("capienza: " + aereo.getCapienza());

		} catch (Exception e) {
			logger.severe("Errore nel recupero del aereo" + " " + e.getMessage());
		}
	}

	
	public Aereo getAereo() {
		return aereo;
	}

	public void setAereo(Aereo aereo) {
		this.aereo = aereo;
	}

	public AereoDTO getAereoDTO() {
		return aereoDTO;
	}

	public void setAereoDTO(AereoDTO aereoDTO) {
		this.aereoDTO = aereoDTO;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<AereoDTO> getListaAereo() {
		return listaAereo;
	}

	public void setListaAereo(List<AereoDTO> listaAereo) {
		this.listaAereo = listaAereo;
	}

}
