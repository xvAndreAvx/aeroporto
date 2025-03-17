package com.bean;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.DTO.BigliettoDTO;
import com.DTO.VoloDTO;
import com.aeroportoEntity.Aereo;
import com.aeroportoEntity.Tratta;
import com.aeroportoEntity.Volo;
import com.serviceInt.AereoService;
import com.serviceInt.BigliettoService;
import com.serviceInt.TrattaService;
import com.serviceInt.VoloService;
import com.utility.CostantiErrori;
import com.utility.MessaggioErr;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Named;

@Named("voloBean")
@SessionScoped
public class VoloBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(VoloBean.class.getName());
	private VoloDTO voloDTO;
	private BigliettoDTO bigliettoDTO;
	private List<VoloDTO> listaVoli;
	private Long idTrattaSelezionata;
	private Long idAereoSelezionato;
	private Boolean postiDisponibili;
	private MessaggioErr messaggio;

	@EJB
	private VoloService voloService;

	@EJB
	private TrattaService trattaService;

	@EJB
	private AereoService aereoService;

	@EJB
	private BigliettoService bigliettoService;

	@PostConstruct
	public void init() {
		voloDTO = new VoloDTO();
		bigliettoDTO = new BigliettoDTO();
		idTrattaSelezionata = null;
		idAereoSelezionato = null;
		listaVoli = new ArrayList<>();
		postiDisponibili = true;
		caricaVoli();
		messaggio = new MessaggioErr();

	}

	public void sceltaAereo() {
		if (idAereoSelezionato != null) {
			try {
				Aereo aereo = aereoService.findyByIdAereo(idAereoSelezionato);
				voloDTO.setAereo(aereo);
			} catch (Exception e) {
				logger.info(CostantiErrori.ERR_REC_AEREO + e.getMessage());
				messaggio.addMessage(FacesMessage.SEVERITY_FATAL, CostantiErrori.ERR_REC_AEREO);
			}
		}
	}

	public void sceltaTratta() {
		if (idTrattaSelezionata != null) {
			try {
				Tratta tratta = trattaService.findTrattaById(idTrattaSelezionata);
				voloDTO.setTratta(tratta);
			} catch (Exception e) {
				logger.info(CostantiErrori.ERR_REC_TRATTA + e.getMessage());
				messaggio.addMessage(FacesMessage.SEVERITY_FATAL, CostantiErrori.ERR_REC_TRATTA);
			}
		}
	}

//	Al momento della creazione del volo 
//	vado a creare anche un biglietto di quel volo con il relativo costo
//	lasciando gli altri camoi null
	public void registrazione() {
		try {
			if (voloDTO.getTratta() == null) {
				messaggio.addMessage(FacesMessage.SEVERITY_ERROR, CostantiErrori.TRATTA_MANCANTE);
			} else if (verificaDataVolo(voloDTO)) {
				messaggio.addMessage(FacesMessage.SEVERITY_ERROR, CostantiErrori.DATA_NON_VALIDA);
			} else if (voloDTO.getAereo() == null) {
				messaggio.addMessage(FacesMessage.SEVERITY_ERROR, CostantiErrori.AEREO_NON_VALIDO);
			} else if (bigliettoDTO.getCosto() == null || bigliettoDTO.getCosto() <= 0) {
				messaggio.addMessage(FacesMessage.SEVERITY_ERROR, CostantiErrori.COSTO_NON_VALIDO);
			} else {
				logger.info("dataPartenza: "
						+ (voloDTO.getDataPartenza() != null ? voloDTO.getDataPartenza().toString() : "null"));
				logger.info("dataArrivo: "
						+ (voloDTO.getDataArrivo() != null ? voloDTO.getDataArrivo().toString() : "null"));

				Volo voloNew = voloService.insertVolo(voloDTO);

				bigliettoDTO.setVolo(voloNew);
				bigliettoService.insertBiglietto(bigliettoDTO);
				messaggio.addMessage(FacesMessage.SEVERITY_INFO, CostantiErrori.SUCCESSO_CREAZIONE_VOLO);
				init();
			}
		} catch (Exception e) {
			messaggio.addMessage(FacesMessage.SEVERITY_FATAL, CostantiErrori.ERR_CREA_VOLO);
			logger.info(CostantiErrori.ERR_CREA_VOLO + e.getMessage());
		}
	}

	public void caricaVoli() {
		try {
			List<VoloDTO> tuttiVoli = voloService.getAll();
			listaVoli = new ArrayList<>();

			LocalDateTime now = LocalDateTime.now();

			for (VoloDTO volo : tuttiVoli) {
				if (volo.getDataPartenza().isAfter(now)) {
					listaVoli.add(volo);
				}
			}

			listaVoli.sort((volo1, volo2) -> volo1.getDataPartenza().compareTo(volo2.getDataPartenza()));

			logger.info("Caricati " + listaVoli.size() + " voli futuri, ordinati per data di partenza");
		} catch (Exception e) {
			logger.severe(CostantiErrori.ERR_LISTA_VOLI + e.getMessage());
			messaggio.addMessage(FacesMessage.SEVERITY_FATAL, CostantiErrori.ERR_LISTA_VOLI);
		}
	}

	public Boolean verificaDataVolo(VoloDTO volo) {
		LocalDateTime now = LocalDateTime.now();

		if (voloDTO.getDataArrivo() == null || voloDTO.getDataPartenza() == null
				|| voloDTO.getDataPartenza().isBefore(now)
				|| voloDTO.getDataArrivo().isBefore(voloDTO.getDataPartenza())) {
			return true;
		}

		return false;
	}

	public Boolean isDisponibile(VoloDTO volo) {
		return volo.getPostiDisponibili() > 0;
	}

	public Boolean getPostiDisponibili() {
		return postiDisponibili;
	}

	public void setPostiDisponibili(Boolean postiDisponibili) {
		this.postiDisponibili = postiDisponibili;
	}

	public VoloDTO getVoloDTO() {
		return voloDTO;
	}

	public void setVoloDTO(VoloDTO voloDTO) {
		this.voloDTO = voloDTO;
	}

	public List<VoloDTO> getListaVoli() {
		return listaVoli;
	}

	public void setListaVoli(List<VoloDTO> listaVoli) {
		this.listaVoli = listaVoli;
	}

	public Long getIdTrattaSelezionata() {
		return idTrattaSelezionata;
	}

	public void setIdTrattaSelezionata(Long idTrattaSelezionata) {
		this.idTrattaSelezionata = idTrattaSelezionata;
	}

	public Long getIdAereoSelezionato() {
		return idAereoSelezionato;
	}

	public void setIdAereoSelezionato(Long idAereoSelezionato) {
		this.idAereoSelezionato = idAereoSelezionato;
	}

	public BigliettoDTO getBigliettoDTO() {
		return bigliettoDTO;
	}

	public void setBigliettoDTO(BigliettoDTO bigliettoDTO) {
		this.bigliettoDTO = bigliettoDTO;
	}

}
