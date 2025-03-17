package com.bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import com.DTO.UtenteDTO;
import com.aeroportoEntity.Utente;
import com.controller.NavigationController;
import com.controller.Navigazione;
import com.exception.ValidazioneException;
import com.serviceInt.UtenteService;
import com.utility.CalcolaEta;
import com.utility.CostantiErrori;
import com.utility.MessaggioErr;
import com.validazioni.UtenteValidazione;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("utenteBean")
@SessionScoped
public class UtenteBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(UtenteValidazione.class.getName());
	private Navigazione navigazione;
	private MessaggioErr messaggio;
	private CalcolaEta calcEta;
	private UtenteDTO utenteDTO;
	private LocalDateTime eta;

	@EJB
	private UtenteService utenteService;

	@Inject
	private NavigationController navigationController;

	@PostConstruct
	public void init() {
		if (utenteDTO == null) {
			utenteDTO = new UtenteDTO();
		}
		eta = null;
		calcEta = new CalcolaEta();
		messaggio = new MessaggioErr();
		navigazione = new Navigazione();
	}

	public String registrazione() {
		try {
			if (eta == null) {
				messaggio.addMessage(FacesMessage.SEVERITY_ERROR, "La data di nascita è obbligatoria");
				return navigazione.rimaniQui("registrazioneUtente");
			}

			utenteDTO.setEta(calcEta.calcolo(eta));
			utenteService.insertUtente(utenteDTO);
			logger.info("Successo nella registrazione");

			// Pulisci i campi dopo la registrazione
			pulisciCampiRegistrazione();

			return navigazione.reindirizza("login");
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

			return navigazione.rimaniQui("registrazioneUtente");
		} catch (Exception e) {
			logger.warning("Errore nella registrazione: " + e.getMessage());
			messaggio.addMessage(FacesMessage.SEVERITY_FATAL, "Errore durante la registrazione: " + e.getMessage());
			return navigazione.rimaniQui("registrazioneUtente");
		}
	}

	public String login() {
		try {
			setUtenteDTO(utenteService.login(utenteDTO));

			logger.info("Successo nel login");
			logger.info("ID: " + utenteDTO.getId());
			logger.info("Email: " + utenteDTO.getEmail());
			logger.info("Nome: " + utenteDTO.getNome() + " " + utenteDTO.getCognome());

			navigationController.setCurrentView("HOME");
			return navigazione.reindirizza("index");
		} catch (ValidazioneException ve) {
			logger.warning("Validazione fallita: " + ve.getMessage());

			List<String> errori = ve.getErrori();
			if (errori != null && !errori.isEmpty()) {
				for (String errore : errori) {
					logger.warning(errore);
				}
			}

			String emailTemp = utenteDTO.getEmail();
			pulisciCampiLogin();
			utenteDTO.setEmail(emailTemp);

			messaggio.addMessage(FacesMessage.SEVERITY_ERROR, CostantiErrori.NON_AUTORIZZATO);

			return navigazione.rimaniQui("login");

		} catch (Exception e) {
			logger.warning("Errore nel login");
			logger.warning(e.getMessage());

			pulisciCampiLogin();

			messaggio.addMessage(FacesMessage.SEVERITY_FATAL, CostantiErrori.LOGIN_ERR);
			return navigazione.rimaniQui("login");
		}
	}

	public String verificaLogin() {
		FacesContext context = FacesContext.getCurrentInstance();

		if (utenteDTO == null || utenteDTO.getId() == null) {
			logger.info(CostantiErrori.NON_AUTENTICATO);

			try {
				context.getExternalContext()
						.redirect(context.getExternalContext().getRequestContextPath() + "/login.xhtml");
				context.responseComplete();

				pulisciCampiLogin();
			} catch (Exception e) {
				logger.severe("Errore durante il redirect: " + e.getMessage());
			}
			return null;
		}
		return null;
	}

	public UtenteDTO verificaUtente(UtenteDTO utente) {
		try {
			Utente utenteProva = utenteService.verificaUtente(utente);
			if (utenteProva.getId().equals(utenteDTO.getId())) {
				return utenteDTO;
			}
		} catch (Exception e) {
			logger.info(CostantiErrori.NON_AUTENTICATO);
		}
		return null;
	}

	public Boolean isAdmin() {
		if (utenteDTO != null && "admin".equals(utenteDTO.getRuolo())) {
			return true;
		}
		return false;
	}

	public String logout() {
		pulisciCampiLogin();
		navigationController.setCurrentView(null);
		return navigazione.reindirizza("login");
	}

	public void pulisciCampiRegistrazione() {
		utenteDTO = new UtenteDTO();
		eta = null;
	}

	public void pulisciCampiLogin() {
		utenteDTO = new UtenteDTO();
	}


	public UtenteDTO getUtenteDTO() {
		return utenteDTO;
	}

	public void setUtenteDTO(UtenteDTO utenteDTO) {
		this.utenteDTO = utenteDTO;
	}

	public LocalDateTime getEta() {
		return eta;
	}

	public void setEta(LocalDateTime eta) {
		this.eta = eta;
	}
}