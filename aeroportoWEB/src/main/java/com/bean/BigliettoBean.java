package com.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.DTO.BigliettoDTO;
import com.DTO.PrenotazioneDTO;
import com.aeroportoEntity.Prenotazione;
import com.controller.NavigationController;
import com.pdf.PdfBiglietto;
import com.pdf.PdfCreator;
import com.serviceInt.BigliettoService;
import com.serviceInt.PasseggeroService;
import com.serviceInt.PrenotazioneService;
import com.serviceInt.VoloService;
import com.utility.CostantiErrori;
import com.utility.MessaggioErr;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("bigliettoBean")
@SessionScoped
public class BigliettoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(BigliettoBean.class.getName());
	private BigliettoDTO bigliettoDTO;
	private Long idVoloScelto;
	private Double costoVolo;
	private List<BigliettoDTO> listaBiglietti;
	private MessaggioErr messaggio;

	@EJB
	private BigliettoService bigliettoService;

	@EJB
	private VoloService voloService;

	@EJB
	private PrenotazioneService prenotazioneService;

	@EJB
	private PasseggeroService passeggeroService;

	@Inject
	private PrenotazioneBean prenotazioneBean;

	@Inject
	private UtenteBean utenteBean;

	@Inject
	private NavigationController navigazione;

	@Inject
	private PasseggeroBean passeggeroBean;

	@PostConstruct
	public void init() {
		bigliettoDTO = new BigliettoDTO();
		idVoloScelto = null;
		costoVolo = null;
		listaBiglietti = new ArrayList<BigliettoDTO>();
		caricaBiglietti();
		messaggio = new MessaggioErr();

	}

	public void registrazione() {
		try {
			if (bigliettoDTO.getVolo() == null) {
				messaggio.addMessage(FacesMessage.SEVERITY_ERROR, CostantiErrori.VOLO_MANCANTE);
				logger.info(CostantiErrori.ERRORE + CostantiErrori.VOLO_MANCANTE);
			} else {
				bigliettoService.insertBiglietto(bigliettoDTO);
				messaggio.addMessage(FacesMessage.SEVERITY_INFO, CostantiErrori.SUCCESSO_CREAZIONE_BIGLIETTO);
				logger.info(CostantiErrori.SUCCESSO + CostantiErrori.SUCCESSO_CREAZIONE_BIGLIETTO);
			}

			init();

		} catch (Exception e) {
			logger.warning("Errore nella creazione del biglietto: " + e.getMessage());
			messaggio.addMessage(FacesMessage.SEVERITY_FATAL, CostantiErrori.ERR_CREA_BIGLIETTO);
		}
	}

//	vado ad esegure l'update al biglietto creato precedentemente durante la creazione del volo
//	vado ad aggiungere tutti i campi mancante
//	inoltre mi creo un nuovo biglietto sempre con solo i dati del volo appena comprato e il suo costo
	public String confermaBiglietto() {
		try {
			PrenotazioneDTO prenotazioneAttuale = prenotazioneBean.getPrenotazioneDTO();

			if (prenotazioneAttuale == null || prenotazioneAttuale.getCodice() == null
					|| prenotazioneAttuale.getCodice().isEmpty()) {
				messaggio.addMessage(FacesMessage.SEVERITY_ERROR, "Nessuna prenotazione valida disponibile");
				return null;
			}

			Long idVoloAttuale = bigliettoDTO.getVolo().getId();
			Double costoAttuale = bigliettoDTO.getCosto();

			prenotazioneAttuale = prenotazioneService.inserisciData(prenotazioneAttuale);
			bigliettoService.updateBiglietto(bigliettoDTO, prenotazioneAttuale);

			messaggio.addMessage(FacesMessage.SEVERITY_INFO, "Biglietto confermato con successo");

			init();
			creaProssimoBiglietto(idVoloAttuale, costoAttuale);

			navigazione.setCurrentView("HOME");
			passeggeroBean.setPostoConfermato(false);
			return "index?faces-redirect=true";
		} catch (Exception e) {
			logger.warning("Errore nella conferma del biglietto: " + e.getMessage());
			messaggio.addMessage(FacesMessage.SEVERITY_FATAL, "Errore nella conferma del biglietto");
			return "index?faces-redirect=false";
		}
	}

	public void caricaVoloSelezionato() {
		logger.info("Caricamento volo con ID: " + idVoloScelto);
		if (idVoloScelto != null) {
			try {
				BigliettoDTO bigliettoVolo = null;
				Prenotazione prenotazione = null;
				Double costo = bigliettoService.findByIdVoloEPrenotazione(idVoloScelto, prenotazione).getCosto();
				try {

					bigliettoVolo = bigliettoService.findByIdVoloEPrenotazione(idVoloScelto, prenotazione);
				} catch (Exception e) {
					logger.info("Nessun biglietto esistente trovato per il volo ID: " + idVoloScelto);
				}

				if (bigliettoVolo == null || bigliettoVolo.getId() == null) {
					bigliettoVolo = new BigliettoDTO();
					try {
						bigliettoVolo.setVolo(voloService.findVoloById(idVoloScelto));
						bigliettoVolo.setCosto(costo);
						setBigliettoDTO(bigliettoVolo);
						registrazione();
						logger.info(
								"Volo trovato e nuovo biglietto creato: " + bigliettoVolo.getVolo().getCodiceVolo());
					} catch (Exception e) {
						logger.warning("Errore nel caricamento diretto del volo: " + e.getMessage());
					}
				}
				passeggeroBean.setIdBiglietto(bigliettoVolo.getId());
				setBigliettoDTO(bigliettoVolo);

				if (bigliettoDTO != null && bigliettoDTO.getVolo() != null) {
					costoVolo = bigliettoDTO.getCosto();
				}

			} catch (Exception e) {
				logger.warning("Errore nel caricamento del volo: " + e.getMessage());
				messaggio.addMessage(FacesMessage.SEVERITY_FATAL, CostantiErrori.ERR_REC_TRATTA);
			}
		} else {
			logger.info("Nessun ID volo selezionato");
			bigliettoDTO = new BigliettoDTO();
			bigliettoDTO.setVolo(null);
			costoVolo = null;
		}
	}

	public void caricaBiglietti() {
		try {
			List<BigliettoDTO> tuttiBiglietti = bigliettoService.getAll();
			listaBiglietti = new ArrayList<>();

			tuttiBiglietti.forEach(biglietto -> {
				if (verificaBiglietto(biglietto)) {
					logger.info(biglietto.getPrenotazione().getUtente().getId().toString());
					logger.info(utenteBean.getUtenteDTO().getId().toString());

					listaBiglietti.add(biglietto);
				}
			});

			listaBiglietti.sort((biglietto1, biglietto2) -> biglietto1.getVolo().getDataPartenza()
					.compareTo(biglietto2.getVolo().getDataPartenza()));

			logger.info(
					"Caricati " + listaBiglietti.size() + " voli futuri dell'utente, ordinati per data di partenza");
		} catch (Exception e) {
			logger.warning(CostantiErrori.ERR_LISTA_VOLI + e.getMessage());
			messaggio.addMessage(FacesMessage.SEVERITY_FATAL, CostantiErrori.ERR_LISTA_VOLI);
		}
	}

	public Boolean verificaBiglietto(BigliettoDTO biglietto) {
		LocalDateTime now = LocalDateTime.now();

		if (biglietto.getPrenotazione() != null && biglietto.getPrenotazione().getUtente() != null
				&& biglietto.getPrenotazione().getUtente().getId().equals(utenteBean.getUtenteDTO().getId())
				&& biglietto.getVolo().getDataPartenza().isAfter(now)) {
			return true;
		}
		return false;
	}

	public void caricaAcquisto(Long idVolo) {
		setIdVoloScelto(idVolo);
		navigazione.setCurrentView(navigazione.getMODEL_ACQUISTO());
	}

	public StreamedContent getPdfStreamedContent(String posto, Long idBiglietto) {
		try {
			// Genera il PDF
			PdfBiglietto pdf = new PdfBiglietto();
			logger.info(posto + " " + idBiglietto.toString());
			pdf.setBiglietto(bigliettoService.findByPosto(posto, idBiglietto));
			pdf.setPasseggero(passeggeroService.findByPosto(posto, idBiglietto));
			pdf.setPrenotazione(pdf.getBiglietto().getPrenotazione());
			pdf.setUtente(pdf.getBiglietto().getPrenotazione().getUtente());

			PdfCreator pdfCreator = new PdfCreator();
			String percorsoFile = pdfCreator.generaBigliettoPDF(pdf);
			logger.info(percorsoFile);
			if (percorsoFile != null) {
				File file = new File(percorsoFile);
				InputStream stream = new FileInputStream(file);
				return DefaultStreamedContent.builder().name(file.getName()).contentType("application/pdf")
						.stream(() -> stream).build();
			}
		} catch (Exception e) {
			logger.warning("Errore nella generazione del PDF: " + e.getMessage());
			messaggio.addMessage(FacesMessage.SEVERITY_FATAL, "Errore nella generazione del PDF");
		}

		return null;
	}

	public void creaProssimoBiglietto(Long idVoloAttuale, Double costoAttuale) {
		BigliettoDTO nuovoBiglietto = new BigliettoDTO();
		try {
			nuovoBiglietto.setVolo(voloService.findVoloById(idVoloAttuale));
			nuovoBiglietto.setCosto(costoAttuale);
			nuovoBiglietto.setPrenotazione(null);
			nuovoBiglietto.setCodiceBiglietto(null);
			bigliettoService.insertBiglietto(nuovoBiglietto);

			logger.info("Nuovo biglietto creato automaticamente per il volo ID: " + idVoloAttuale);
		} catch (Exception e) {
			logger.warning("Errore nella creazione automatica del biglietto: " + e.getMessage());
		}
	}

	public List<BigliettoDTO> getListaBiglietti() {
		return listaBiglietti;
	}

	public void setListaBiglietti(List<BigliettoDTO> listaBiglietti) {
		this.listaBiglietti = listaBiglietti;
	}

	public BigliettoDTO getBigliettoDTO() {
		return bigliettoDTO;
	}

	public void setBigliettoDTO(BigliettoDTO bigliettoDTO) {
		this.bigliettoDTO = bigliettoDTO;
	}

	public Long getIdVoloScelto() {
		return idVoloScelto;
	}

	public void setIdVoloScelto(Long idVoloScelto) {
		this.idVoloScelto = idVoloScelto;
	}

	public Double getCostoVolo() {
		return costoVolo;
	}

	public void setCostoVolo(Double costoVolo) {
		this.costoVolo = costoVolo;
	}
}