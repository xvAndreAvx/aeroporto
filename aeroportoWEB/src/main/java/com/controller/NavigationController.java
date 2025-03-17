package com.controller;
import java.io.Serializable;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@Named("navigationController")
@SessionScoped
public class NavigationController implements Serializable {
    private static final long serialVersionUID = 1L;
    private String currentView = "HOME";
    public static final String MODEL_AEROPORTO = "MODEL_AEROPORTO";
    public static final String MODEL_TRATTA = "MODEL_TRATTA";
    public static final String MODEL_VOLO = "MODEL_VOLO";
    public static final String MODEL_ACQUISTO = "MODEL_ACQUISTO";
    public static final String REGISTRAZIONE_AEREO = "REGISTRAZIONE_AEREO";
    public static final String MODEL_BIGLIETTI = "MODEL_BIGLIETTI";
    //public static final String MODEL_PASSEGGERO = "MODEL_PASSEGGERO";
    
//    public String getMODEL_PASSEGGERO() {
//        return MODEL_PASSEGGERO;
//    }
    
    public String getMODEL_BIGLIETTI() {
        return MODEL_BIGLIETTI;
    }
    
    public String getREGISTRAZIONE_AEREO() {
        return REGISTRAZIONE_AEREO;
    }
    
    public String getMODEL_ACQUISTO() {
    	return MODEL_ACQUISTO;
    }
    
    public String getMODEL_AEROPORTO() {
        return MODEL_AEROPORTO;
    }
    
    public String getMODEL_TRATTA() {
		return MODEL_TRATTA;
	}
    
    public String getMODEL_VOLO() {
		return MODEL_VOLO;
	}

	public void setCurrentView(String view) {
        System.out.println("Cambiando vista a: " + view);
        this.currentView = view;
    }
    
    public String getCurrentView() {
        return currentView;
    }
 
    public boolean isBigliettiView() {
        return MODEL_BIGLIETTI.equals(currentView);
    }
    
    public boolean isAeroportoView() {
        return MODEL_AEROPORTO.equals(currentView);
    }
    
    public boolean isTrattaView() {
        return MODEL_TRATTA.equals(currentView);
    }
    
    public boolean isVoloView() {
        return MODEL_VOLO.equals(currentView);
    }
    
    public boolean isAcquistoView() {
    	return MODEL_ACQUISTO.equals(currentView);
    }
    public boolean isAereoView() {
    	return REGISTRAZIONE_AEREO.equals(currentView);
    }
//    public boolean isPasseggeroView() {
//    	return MODEL_PASSEGGERO.equals(currentView);
//    }
}