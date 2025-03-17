package com.bean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.DTO.AeroportoDTO;
import com.serviceInt.AeroportoService;
import com.utility.MessaggioErr;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.inject.Named;

@Named("aeroportoBean")
@SessionScoped
public class AereoportoBean implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(AereoportoBean.class.getName());
    private AeroportoDTO aeroportoDTO;
    private List<AeroportoDTO> listaAeroporti;
    private MessaggioErr messaggio ;

    @EJB
    private AeroportoService aeroportoService;

    @PostConstruct
    public void init() {
        aeroportoDTO = new AeroportoDTO();
        listaAeroporti = new ArrayList<>();
        caricaAeroporti(); 
        messaggio = new MessaggioErr();
    }

    public void registrazione() {
        try {
            aeroportoService.insertAeroporto(aeroportoDTO);
            messaggio.addMessage(FacesMessage.SEVERITY_INFO, "Aeroporto registrato con successo!");
            logger.info("Successo nella registrazione");
            init();
            
        } catch (Exception e) {
        	messaggio.addMessage(FacesMessage.SEVERITY_FATAL, "Si è verificato un errore durante la registrazione!");
        }
    }

    public void caricaAeroporti() {
        try {
            listaAeroporti = aeroportoService.getAll();
            logger.info("Caricati " + listaAeroporti.size() + " aeroporti");
        } catch (Exception e) {
            logger.severe("Errore nel caricamento degli aeroporti: " + e.getMessage());
            messaggio.addMessage(FacesMessage.SEVERITY_ERROR, "Errore nel caricamento degli aeroporti!");
        }
    }

    public String tornaHome() {
        return "home?faces-redirect=true";
    }
    
    public AeroportoDTO getAeroportoDTO() {
        return aeroportoDTO;
    }

    public void setAeroportoDTO(AeroportoDTO aeroportoDTO) {
        this.aeroportoDTO = aeroportoDTO;
    }
    
    public List<AeroportoDTO> getListaAeroporti() {
        return listaAeroporti;
    }
    
    public void setListaAeroporti(List<AeroportoDTO> listaAeroporti) {
        this.listaAeroporti = listaAeroporti;
    }

    
}