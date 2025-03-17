package com.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.DTO.AeroportoDTO;
import com.DTO.TrattaDTO;
import com.aeroportoEntity.Aeroporto;
import com.serviceInt.AeroportoService;
import com.serviceInt.TrattaService;
import com.utility.CostantiErrori;
import com.utility.MessaggioErr;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Named;

@Named("trattaBean")
@SessionScoped
public class TrattaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(TrattaBean.class.getName());
	private TrattaDTO trattaDTO;
	private String programma;
	private Long idAeroportoPartenza;
	private Long idAeroportoArrivo;
	private List<AeroportoDTO> listaAeroportiFiltrata;
	private List<TrattaDTO> listaTratta;
    private MessaggioErr messaggio ;


	@EJB
	private TrattaService trattaService;

	@EJB
	private AeroportoService aeroportoService;

	@PostConstruct
	public void init() {
		trattaDTO = new TrattaDTO();
		programma = null;
		idAeroportoPartenza = null;
		idAeroportoArrivo = null;
		listaAeroportiFiltrata = new ArrayList<AeroportoDTO>();
		listaTratta = new ArrayList<>();
		caricaTratte();
        messaggio = new MessaggioErr();

	}

	public void caricaTratte() {
	    try {
	        listaTratta = trattaService.getAll();
	        logger.info("Caricati " + listaTratta.size() + " tratte");
	        for (TrattaDTO tratta : listaTratta) {
	            if (tratta.getAeroportoArrivo() != null) {
	                logger.info("Aeroporto Arrivo : " + tratta.getAeroportoArrivo().getNome()
	                    + tratta.getAeroportoArrivo().getCitta());
	            } else {
	                logger.warning("Aeroporto Arrivo è null per una tratta");
	            }
	            
	            if (tratta.getAeroportoPartenza() != null) {
	                logger.info("Aeroporto Partenza : " + tratta.getAeroportoPartenza().getNome()
	                    + tratta.getAeroportoPartenza().getCitta());
	            } else {
	                logger.warning("Aeroporto Partenza è null per una tratta");
	            }
	        }
	    } catch (Exception e) {
	        logger.severe("Errore nel caricamento delle tratte: " + e.getMessage());
	        messaggio.addMessage(FacesMessage.SEVERITY_FATAL, CostantiErrori.ERR_LISTA_TRATTE);
	    }
	}
	public void onAeroportoPartenzaChange() {
		if (idAeroportoPartenza != null && idAeroportoPartenza.equals(idAeroportoArrivo)) {
			idAeroportoArrivo = null;
			trattaDTO.setAeroportoArrivo(null);
		}
	}

	public void onAeroportoArrivoChange() {
		if (idAeroportoArrivo != null && idAeroportoArrivo.equals(idAeroportoPartenza)) {
			idAeroportoPartenza = null;
			trattaDTO.setAeroportoPartenza(null);
		}
	}

	public List<AeroportoDTO> filtraLista(List<AeroportoDTO> listaAeroporti, Long idAeroportoDaEscludere) {
		listaAeroportiFiltrata.clear();

		if (idAeroportoDaEscludere == null) {
			listaAeroportiFiltrata.addAll(listaAeroporti);
			return listaAeroportiFiltrata;
		}

		try {
			for (AeroportoDTO aeroporto : listaAeroporti) {
				if (!aeroporto.getId().equals(idAeroportoDaEscludere)) {
					listaAeroportiFiltrata.add(aeroporto);
				}
			}
		} catch (Exception e) {
			logger.info("Errore nel filtraggio della lista aeroporti" + e.getMessage());
			listaAeroportiFiltrata.clear();
			listaAeroportiFiltrata.addAll(listaAeroporti);
		}

		return listaAeroportiFiltrata;
	}

	public boolean isTrattaCompleta() {
		return idAeroportoPartenza != null && idAeroportoArrivo != null
				&& !idAeroportoPartenza.equals(idAeroportoArrivo);
	}

	public void setDestinazioni(Long id, String programma) {
		try {
			setProgramma(programma);
			Aeroporto aeroporto = aeroportoService.findById(id);

			if (programma.equals("partenza")) {
				if (getIdAeroportoArrivo() != null && id.equals(getIdAeroportoArrivo())) {
					messaggio.addMessage(FacesMessage.SEVERITY_ERROR, CostantiErrori.AEROPORTI_UGUALI);
				}
				trattaDTO.setAeroportoPartenza(aeroporto);
			} else if (programma.equals("arrivo")) {
				if (getIdAeroportoPartenza() != null && id.equals(getIdAeroportoPartenza())) {
					messaggio.addMessage(FacesMessage.SEVERITY_ERROR, CostantiErrori.AEROPORTI_UGUALI);
				}
				trattaDTO.setAeroportoArrivo(aeroporto);
			}
		} catch (Exception e) {
			logger.info(CostantiErrori.ERR_AIR_SELECT + e.getMessage());
			messaggio.addMessage(FacesMessage.SEVERITY_FATAL, CostantiErrori.ERR_AIR_SELECT);
		}
	}

	public void registrazione() {
		try {
			if (trattaDTO.getAeroportoArrivo() == null || trattaDTO.getAeroportoPartenza() == null) {
				messaggio.addMessage(FacesMessage.SEVERITY_ERROR, CostantiErrori.MANCATA_SELEZIONE_AIR);
			} else {
				trattaService.insertTratta(trattaDTO);
				messaggio.addMessage(FacesMessage.SEVERITY_INFO, CostantiErrori.SUCCESSO_CREAZIONE_TRATTA);
			}
			init();

		} catch (Exception e) {
			messaggio.addMessage(FacesMessage.SEVERITY_FATAL, CostantiErrori.ERR_CREA_TRATTA);
			logger.info(CostantiErrori.ERR_CREA_TRATTA + e.getMessage());
		}
	}

	public Long getIdAeroportoPartenza() {
		return idAeroportoPartenza;
	}

	public void setIdAeroportoPartenza(Long idAeroportoPartenza) {
		this.idAeroportoPartenza = idAeroportoPartenza;
		if (idAeroportoPartenza != null) {
			setDestinazioni(idAeroportoPartenza, "partenza");
		}
	}

	public Long getIdAeroportoArrivo() {
		return idAeroportoArrivo;
	}

	public void setIdAeroportoArrivo(Long idAeroportoArrivo) {
		this.idAeroportoArrivo = idAeroportoArrivo;
		if (idAeroportoArrivo != null) {
			setDestinazioni(idAeroportoArrivo, "arrivo");
		}
	}

	public TrattaDTO getTrattaDTO() {
		return trattaDTO;
	}

	public void setTrattaDTO(TrattaDTO trattaDTO) {
		this.trattaDTO = trattaDTO;
	}

	public String getProgramma() {
		return programma;
	}

	public void setProgramma(String programma) {
		this.programma = programma;
	}

	public List<AeroportoDTO> getListaAeroportiFiltrata() {
		return listaAeroportiFiltrata;
	}

	public void setListaAeroportiFiltrata(List<AeroportoDTO> listaAeroportiFiltrata) {
		this.listaAeroportiFiltrata = listaAeroportiFiltrata;
	}

	public List<AeroportoDTO> getListaAeroportiPerPartenza(List<AeroportoDTO> listaAeroporti) {
		return filtraLista(listaAeroporti, idAeroportoArrivo);
	}

	public List<AeroportoDTO> getListaAeroportiPerArrivo(List<AeroportoDTO> listaAeroporti) {
		return filtraLista(listaAeroporti, idAeroportoPartenza);
	}

	public List<TrattaDTO> getListaTratta() {
		return listaTratta;
	}

	public void setListaTratta(List<TrattaDTO> listaTratta) {
		this.listaTratta = listaTratta;
	}

}