package com.bean;

import java.io.Serializable;
import java.util.logging.Logger;

import com.DTO.PrenotazioneDTO;
import com.DTO.UtenteDTO;
import com.aeroportoEntity.Prenotazione;
import com.convertitore.PrenotazioneConvertitore;
import com.serviceInt.PrenotazioneService;
import com.serviceInt.UtenteService;
import com.utility.CostantiErrori;
import com.utility.MessaggioErr;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("prenotazioneBean")
@SessionScoped
public class PrenotazioneBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(PrenotazioneBean.class.getName());
	private PrenotazioneDTO prenotazioneDTO;
	private UtenteDTO utente;
	private boolean utenteConfermato = false;
	private MessaggioErr messaggio;

	@EJB
	private PrenotazioneService prenotazioneService;

	@EJB
	private UtenteService utenteService;

	@Inject
	private UtenteBean utenteBean;

	@PostConstruct
	public void init() {
		prenotazioneDTO = new PrenotazioneDTO();
		utente = new UtenteDTO();
		utenteConfermato = false;
		messaggio = new MessaggioErr();
	}

	public void registrazione() {
		try {
			prenotazioneDTO.setUtente(utenteService.verificaUtente(utente));
			logger.warning(utente.getNome());

			if (prenotazioneDTO.getUtente() == null || (prenotazioneDTO.getUtente() != null
					&& !prenotazioneDTO.getUtente().getId().equals(utenteBean.getUtenteDTO().getId()))) {
				logger.warning(CostantiErrori.UTENTE_NON_VALIDO);
				messaggio.addMessage(FacesMessage.SEVERITY_ERROR, CostantiErrori.UTENTE_NON_VALIDO);
				utenteConfermato = false;
			} else {
				PrenotazioneDTO prenotazioneInserita = prenotazioneService.insertPrenotazione(prenotazioneDTO);
				prenotazioneDTO = prenotazioneInserita;
				utenteConfermato = true;
				logger.warning(CostantiErrori.SUCCESSO + CostantiErrori.SUCCESSO_CREAZIONE_PRENOTAZIONE);
			}

			if (!utenteConfermato) {
				init();
			}

		} catch (Exception e) {
			logger.warning("Errore nella registrazione: " + e.getMessage());
			messaggio.addMessage(FacesMessage.SEVERITY_FATAL, CostantiErrori.UTENTE_NON_VALIDO);
			utenteConfermato = false;
		}
	}

	public PrenotazioneDTO findPrenotazioneByCode() {
		try {
			if (prenotazioneDTO != null && prenotazioneDTO.getCodice() != null
					&& !prenotazioneDTO.getCodice().isEmpty()) {
				Prenotazione prenotazione = prenotazioneService.findByCodice(prenotazioneDTO.getCodice());
				if (prenotazione != null) {
					prenotazioneDTO = PrenotazioneConvertitore.toDTO(prenotazione);
					return prenotazioneDTO;
				}
			}
			return null;
		} catch (Exception e) {
			logger.warning("Errore nella ricerca della prenotazione: " + e.getMessage());
			messaggio.addMessage(FacesMessage.SEVERITY_FATAL, "Errore nella ricerca della prenotazione");
			return null;
		}
	}

	public boolean isUtenteConfermato() {
		return utenteConfermato;
	}

	public void setUtenteConfermato(boolean utenteConfermato) {
		this.utenteConfermato = utenteConfermato;
	}

	public PrenotazioneDTO getPrenotazioneDTO() {
		return prenotazioneDTO;
	}

	public void setPrenotazioneDTO(PrenotazioneDTO prenotazioneDTO) {
		this.prenotazioneDTO = prenotazioneDTO;
	}

	public UtenteDTO getUtenteDTO() {
		return utente;
	}

	public void setUtenteDTO(UtenteDTO utenteDTO) {
		this.utente = utenteDTO;
	}
}