package com.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.DTO.PasseggeroDTO;
import com.aeroportoEntity.Biglietto;
import com.aeroportoEntity.Prenotazione;
import com.aeroportoEntity.Utente;
import com.aeroportoEntity.Volo;
import com.serviceInt.BigliettoService;
import com.serviceInt.PasseggeroService;
import com.serviceInt.VoloService;
import com.utility.CostantiErrori;
import com.utility.MessaggioErr;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named("passeggeroBean")
@SessionScoped
public class PasseggeroBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(PasseggeroBean.class.getName());
	private MessaggioErr messaggio;
	private PasseggeroDTO passeggeroDTO;
	private List<PasseggeroDTO> listaPasseggeri;
	private List<PasseggeroDTO> listaMieiPosti;
	private Volo voloSelezionato;
	private List<String> posti;
	private String postoScelto;
	private List<String> postiScelti;
	private boolean postoConfermato;
	private List<Integer> righeAereo;
	private List<String> postiOccupati;
	private Long idBiglietto;
	private static final int COLONNE_PER_RIGA = 6;
	private Map<String, PasseggeroDTO> mappaPasseggeri;

	@EJB
	private PasseggeroService passeggeroService;

	@EJB
	private BigliettoService bigliettoService;

	@EJB
	private VoloService voloService;

	@Inject
	private BigliettoBean bigliettoBean;

	@Inject
	private UtenteBean utenteBean;

	@PostConstruct
	public void init() {
		passeggeroDTO = new PasseggeroDTO();
		voloSelezionato = new Volo();
		posti = new ArrayList<String>();
		listaMieiPosti = new ArrayList<PasseggeroDTO>();
		listaPasseggeri = new ArrayList<PasseggeroDTO>();
		postiOccupati = new ArrayList<String>();
		righeAereo = new ArrayList<Integer>();
		postoConfermato = false;
		idBiglietto = null;
		postoScelto = null;
		postiScelti = new ArrayList<String>();
		mappaPasseggeri = new HashMap<String, PasseggeroDTO>();
		messaggio = new MessaggioErr();
	}

	public void initVolo() {
		try {
			if (bigliettoBean.getBigliettoDTO() != null && bigliettoBean.getBigliettoDTO().getVolo() != null) {
				voloSelezionato = bigliettoBean.getBigliettoDTO().getVolo();

				calcolaRigheAereo();

				caricaPostiOccupati();

				generaPosti();

				logger.info("Volo selezionato e posti inizializzati");
			} else {
				logger.warning("Nessun volo selezionato nel biglietto");
			}
		} catch (Exception e) {
			logger.severe("Errore nell'inizializzazione del volo: " + e.getMessage());
			messaggio.addMessage(FacesMessage.SEVERITY_FATAL, "Impossibile caricare lo schema dei posti");
		}
	}

	/**
	 * Calcola il numero delle righe dell'aereo in base alla capienza. Si basa sul
	 * numero di colonne disponibili per ogni riga.
	 */
	private void calcolaRigheAereo() {
		righeAereo = new ArrayList<>();

		Long capienza = 0L;
		try {
			// Recupero della capienza dell'aereo dal volo selezionato
			if (voloSelezionato != null && voloSelezionato.getId() != null) {
				Volo volo = voloService.findVoloById(voloSelezionato.getId());
				capienza = volo.getAereo().getCapienza();
			}
		} catch (Exception e) {
			// In caso di errore, si utilizza un valore predefinito di sicurezza
			logger.warning("Errore nel recupero della capienza dell'aereo: " + e.getMessage());

		}

		// Calcola il numero di righe necessarie arrotondando per eccesso
		int numeroRighe = (int) Math.ceil((double) capienza / COLONNE_PER_RIGA);

		logger.info("Calcolato numero righe: " + numeroRighe + " per capienza: " + capienza);

		// List<Integer> listInteger = IntStream.rangeClosed(1, numeroRighe).collect(()
		// -> new ArrayList<>(), null, null);

		// Riempie l'elenco delle righe
		for (int i = 1; i <= numeroRighe; i++) {
			righeAereo.add(i);
		}

		logger.info("Calcolate " + righeAereo.size() + " righe per l'aereo con capienza " + capienza);
	}

	private void caricaPostiOccupati() {
		try {
			postiOccupati = new ArrayList<>();

			listaPasseggeri = passeggeroService.getAll();
			for (PasseggeroDTO passeggero : listaPasseggeri) {
				if (passeggero.getBiglietto() != null && passeggero.getBiglietto().getVolo() != null
						&& voloSelezionato != null
						&& passeggero.getBiglietto().getVolo().getId().equals(voloSelezionato.getId())
						&& isNotEmpty(passeggero.getPosto())) {

					postiOccupati.add(passeggero.getPosto());
					logger.info(
							"Posto occupato: " + passeggero.getPosto() + " per il volo ID: " + voloSelezionato.getId());
				}
			}

			logger.info("Totale posti occupati per il volo " + voloSelezionato.getId() + ": " + postiOccupati.size());
		} catch (Exception e) {
			logger.severe("Errore nel caricamento dei posti occupati: " + e.getMessage());
		}
	}

	private static boolean isEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

	private static boolean isNotEmpty(String value) {
		return !isEmpty(value);
	}

	public boolean isPostoDisponibile(int riga, char colonna) {
		String posto = riga + "" + colonna;
		return postiOccupati == null || !postiOccupati.contains(posto);
	}

	public void selezionaPosto(int riga, char colonna) {
		String posto = riga + "" + colonna;

		if (isPostoDisponibile(riga, colonna)) {
			// Se il posto è già nei selezionati, lo rimuoviamo (deseleziona)
			if (postiScelti.contains(posto)) {
				postiScelti.remove(posto);
				mappaPasseggeri.remove(posto); // Rimuovi anche i dati del passeggero
				logger.info("Posto deselezionato: " + posto);
				// Se era l'ultimo posto selezionato, resetta postoScelto
				if (postoScelto != null && postoScelto.equals(posto)) {
					postoScelto = postiScelti.isEmpty() ? null : postiScelti.get(postiScelti.size() - 1);
				}
			} else {
				// Altrimenti lo aggiungiamo (seleziona)
				postoScelto = posto;
				postiScelti.add(postoScelto);
				// Inizializza un nuovo PasseggeroDTO per questo posto
				mappaPasseggeri.put(posto, new PasseggeroDTO());
				logger.info("Posto selezionato: " + postoScelto);
			}
		} else {
			logger.warning("Tentativo di selezionare posto occupato: " + posto);
			messaggio.addMessage(FacesMessage.SEVERITY_WARN, "Il posto " + posto + " non è disponibile");
		}
	}

	public boolean mostraUscitaEmergenza(int riga) {
		if (righeAereo.size() <= 3) {
			return false;
		}

		int primaUscita = Math.round(righeAereo.size() / 3.0f);
		int secondaUscita = Math.round(2 * righeAereo.size() / 3.0f);

		return riga == primaUscita || riga == secondaUscita;
	}

	public void registrazione() {
	    try {
	        Biglietto biglietto = bigliettoService.findById(idBiglietto);
	        logger.info("Elaborazione biglietto ID: " + biglietto.getId());
	        
	        if (biglietto == null || biglietto.getId() == null) {
	            logger.warning(CostantiErrori.BIGLIETTO_NON_TROVATO);
	            messaggio.addMessage(FacesMessage.SEVERITY_ERROR, CostantiErrori.BIGLIETTO_NON_TROVATO);
	            return;
	        }

			// Verifica se ci sono posti selezionati
			if (postiScelti.isEmpty()) {
				messaggio.addMessage(FacesMessage.SEVERITY_WARN, "Seleziona almeno un posto");
				return;
			}

			// Verifica preliminare che tutti i posti selezionati siano disponibili
			List<String> postiNonDisponibili = new ArrayList<>();
			for (String posto : postiScelti) {
				if (isPostoOccupato(posto, listaPasseggeri, voloSelezionato)) {
					postiNonDisponibili.add(posto);
				}
			}

			if (!postiNonDisponibili.isEmpty()) {
				String messaggioErrore = "I seguenti posti non sono più disponibili: "
						+ String.join(", ", postiNonDisponibili);
				logger.warning(messaggioErrore);
				messaggio.addMessage(FacesMessage.SEVERITY_ERROR, messaggioErrore);
				caricaPostiOccupati();
				// Rimuovi i posti non disponibili dalla selezione
				postiScelti.removeAll(postiNonDisponibili);
				for (String posto : postiNonDisponibili) {
					mappaPasseggeri.remove(posto);
				}
				postoScelto = postiScelti.isEmpty() ? null : postiScelti.get(postiScelti.size() - 1);
				return;
			}

			// Validazione dei dati dei passeggeri
			boolean datiValidi = true;
			for (String posto : postiScelti) {
				PasseggeroDTO passeggero = mappaPasseggeri.get(posto);
				if (passeggero == null || isEmpty(passeggero.getNome()) || isEmpty(passeggero.getCognome())
						|| isEmpty(passeggero.getCodiceFiscale())) {
					messaggio.addMessage(FacesMessage.SEVERITY_WARN,
							"Inserisci tutti i dati per il passeggero nel posto " + posto);
					datiValidi = false;
				}
			}

			if (!datiValidi) {
				return;
			}

			// Tutti i posti sono disponibili e i dati sono validi, procedi con la
			// registrazione
			List<String> postiRegistrati = new ArrayList<>();
			for (String posto : postiScelti) {
				PasseggeroDTO passeggero = mappaPasseggeri.get(posto);
				passeggero.setBiglietto(biglietto);
				passeggero.setPosto(posto);

				try {
					passeggeroService.insertPasseggero(passeggero);
					postiOccupati.add(posto);
					postiRegistrati.add(posto);
					logger.info("Passeggero registrato con posto: " + posto);
				} catch (Exception e) {
					logger.warning("Errore nella registrazione del posto " + posto + ": " + e.getMessage());
					messaggio.addMessage(FacesMessage.SEVERITY_ERROR,
							"Errore nella registrazione del posto " + posto + ". " + CostantiErrori.ERR_CREA_POSTO);
				}
			}

			if (!postiRegistrati.isEmpty()) {
	            messaggio.addMessage(FacesMessage.SEVERITY_INFO,
	                    "Posti assegnati con successo: " + String.join(", ", postiRegistrati));
	            
	            postoConfermato = true;
	            logger.info("Flag postoConfermato impostato a: " + postoConfermato);
	            
	        }
	    } catch (Exception e) {
	        logger.severe("Errore grave durante la registrazione: " + e.getMessage());
	        e.printStackTrace(); // Stampa stack trace completo
	        messaggio.addMessage(FacesMessage.SEVERITY_FATAL, CostantiErrori.ERR_RECUPERO_BIGLIETTO);
	    }
	}
	
	public void generaPosti() {
		try {
			if (voloSelezionato == null || voloSelezionato.getId() == null) {
				logger.warning(CostantiErrori.AEREO_NON_VALIDO);
				messaggio.addMessage(FacesMessage.SEVERITY_WARN, CostantiErrori.AEREO_NON_VALIDO);
			}
			Long capienza = 0L;
			try {
				capienza = voloService.findVoloById(voloSelezionato.getId()).getAereo().getCapienza();
			} catch (Exception e) {
				logger.warning("Errore nel recupero della capienza dell'aereo: " + e.getMessage());
				capienza = (long) (righeAereo.size() * COLONNE_PER_RIGA);
			}

			posti = new ArrayList<>();

			int postiGenerati = 0;
			for (int riga : righeAereo) {
				for (char colonna = 'A'; colonna < 'A' + COLONNE_PER_RIGA && postiGenerati < capienza; colonna++) {
					String codPosto = riga + "" + colonna;
					if (!postiOccupati.contains(codPosto)) {
						posti.add(codPosto);
					}
					postiGenerati++;
				}
			}

			logger.info("Generati " + posti.size() + " posti disponibili");
		} catch (Exception e) {
			logger.warning("Errore nella generazione dei posti: " + e.getMessage());
			messaggio.addMessage(FacesMessage.SEVERITY_FATAL, CostantiErrori.POSTI_NON_CARICATI);
		}
	}

	public List<PasseggeroDTO> caricaMieiPosti(Long idBiglietto) {
		try {
			listaMieiPosti = new ArrayList<>();

			listaPasseggeri = passeggeroService.getAll();

			for (PasseggeroDTO passeggero : listaPasseggeri) {
				if (isPasseggeroValido(passeggero, idBiglietto, utenteBean)) {
					listaMieiPosti.add(passeggero);
					logger.info("Aggiunto passeggero con posto: " + passeggero.getPosto() + " per il biglietto ID: "
							+ idBiglietto);
				}
			}
			logger.info("Trovati " + listaMieiPosti.size() + " posti per il biglietto ID: " + idBiglietto);
			return listaMieiPosti;
		} catch (Exception e) {
			logger.warning(CostantiErrori.ERR_LISTA_PASSEGGERI + e.getMessage());
			return new ArrayList<>();
		}
	}

	public boolean isPostoSelezionato(int riga, Character colonna) {
		String posto = riga + "" + colonna;
		return postiScelti.contains(posto);
	}

	public List<Character> getColonneDisponibili() {
		List<Character> colonne = new ArrayList<>();
		for (char c = 'A'; c < 'A' + COLONNE_PER_RIGA; c++) {
			colonne.add(c);
		}
		return colonne;
	}

	private boolean isPasseggeroValido(PasseggeroDTO passeggero, Long idBiglietto, UtenteBean utenteBean) {
		if (passeggero.getBiglietto() == null) {
			return false;
		}

		Biglietto biglietto = passeggero.getBiglietto();
		if (!biglietto.getId().equals(idBiglietto)) {
			return false;
		}

		Prenotazione prenotazione = biglietto.getPrenotazione();
		if (prenotazione == null) {
			return false;
		}

		Utente utente = prenotazione.getUtente();
		if (utente == null) {
			return false;
		}

		return utente.getId().equals(utenteBean.getUtenteDTO().getId());
	}

	private boolean isPostoOccupato(String postoScelto, List<PasseggeroDTO> listaPasseggeri, Volo voloSelezionato) {
		if (postoScelto == null || voloSelezionato == null || voloSelezionato.getId() == null) {
			return false;
		}

		for (PasseggeroDTO passeggero : listaPasseggeri) {
			if (passeggero.getPosto() == null || !passeggero.getPosto().equals(postoScelto)) {
				continue;
			}

			if (passeggero.getBiglietto() == null) {
				continue;
			}

			Volo voloBiglietto = passeggero.getBiglietto().getVolo();
			if (voloBiglietto == null || voloBiglietto.getId() == null) {
				continue;
			}

			if (voloBiglietto.getId().equals(voloSelezionato.getId())) {
				return true;
			}
		}

		return false;
	}

	public void selezionaPosto(int riga, Character colonna) {
		selezionaPosto(riga, colonna.charValue());
	}

	// Getter e Setter
	public List<String> getPostiScelti() {
		return postiScelti;
	}

	public void setPostiScelti(List<String> postiScelti) {
		this.postiScelti = postiScelti;
	}

	public PasseggeroDTO getPasseggeroDTO() {
		return passeggeroDTO;
	}

	public void setPasseggeroDTO(PasseggeroDTO passeggeroDTO) {
		this.passeggeroDTO = passeggeroDTO;
	}

	public Volo getVoloSelezionato() {
		return voloSelezionato;
	}

	public void setVoloSelezionato(Volo voloSelezionato) {
		this.voloSelezionato = voloSelezionato;
	}

	public List<String> getPosti() {
		return posti;
	}

	public void setPosti(List<String> posti) {
		this.posti = posti;
	}

	public String getPostoScelto() {
		return postoScelto;
	}

	public void setPostoScelto(String postoScelto) {
		this.postoScelto = postoScelto;
	}

	public List<PasseggeroDTO> getListaPasseggeri() {
		return listaPasseggeri;
	}

	public void setListaPasseggeri(List<PasseggeroDTO> listaPasseggeri) {
		this.listaPasseggeri = listaPasseggeri;
	}

	public List<PasseggeroDTO> getListaMieiPosti() {
		return listaMieiPosti;
	}

	public void setListaMieiPosti(List<PasseggeroDTO> listaMieiPosti) {
		this.listaMieiPosti = listaMieiPosti;
	}

	public boolean isPostoConfermato() {
		return postoConfermato;
	}

	public void setPostoConfermato(boolean postoConfermato) {
		this.postoConfermato = postoConfermato;
	}

	public List<Integer> getRigheAereo() {
		if (righeAereo == null || righeAereo.isEmpty()) {
			calcolaRigheAereo();
		}
		return righeAereo;
	}

	public void setRigheAereo(List<Integer> righeAereo) {
		this.righeAereo = righeAereo;
	}

	public List<String> getPostiOccupati() {
		return postiOccupati;
	}

	public void setPostiOccupati(List<String> postiOccupati) {
		this.postiOccupati = postiOccupati;
	}

	public Long getIdBiglietto() {
		return idBiglietto;
	}

	public void setIdBiglietto(Long idBiglietto) {
		this.idBiglietto = idBiglietto;
	}

	public Map<String, PasseggeroDTO> getMappaPasseggeri() {
		return mappaPasseggeri;
	}

	public void setMappaPasseggeri(Map<String, PasseggeroDTO> mappaPasseggeri) {
		this.mappaPasseggeri = mappaPasseggeri;
	}
}