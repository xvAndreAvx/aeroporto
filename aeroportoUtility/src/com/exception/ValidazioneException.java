package com.exception;
import java.util.List;

public class ValidazioneException extends AeroportoException {
    private static final long serialVersionUID = 1L;
    private final List<String> messaggiErrore;

    public ValidazioneException(String messaggio) {
        super("ERRORE_VALIDAZIONE", messaggio);
        this.messaggiErrore = List.of(messaggio);
    }

    public ValidazioneException(List<String> messaggiErrore) {
        super("ERRORE_VALIDAZIONE", String.join(", ", messaggiErrore));
        this.messaggiErrore = messaggiErrore;
    }

    public List<String> getErrori() {
        return messaggiErrore;
    }
}