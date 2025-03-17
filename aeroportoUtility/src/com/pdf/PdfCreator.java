package com.pdf;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pdflib.pdflib;

public class PdfCreator {
	private static final Logger logger = Logger.getLogger(PdfCreator.class.getName());

	// Meglio usare una proprietà configurabile esternamente
	private String pdfBaseDir = "C:/Sviluppo/progetti/pdf";

	// Costanti per il layout del documento
	private static final int PAGE_WIDTH = 595;
	private static final int PAGE_HEIGHT = 842;
	private static final int MARGIN = 40;
	private static final int HEADER_HEIGHT = 100;

	public PdfCreator() {
		// Costruttore di default
	}

	/**
	 * Costruttore che permette di specificare la directory base
	 * 
	 * @param pdfBaseDir directory base per i PDF
	 */
	public PdfCreator(String pdfBaseDir) {
		this.pdfBaseDir = pdfBaseDir;
	}

	/**
	 * Genera un PDF per un biglietto aereo
	 * 
	 * @param pdfBiglietto oggetto contenente tutte le informazioni necessarie
	 * @return il percorso del file PDF generato o null in caso di errore
	 * @throws PdfGenerationException in caso di errori nella generazione del PDF
	 */
	public String generaBigliettoPDF(PdfBiglietto pdfBiglietto) {
		String outputPath = null;
		PDFLibWrapper p = null;

		try {
			// Verifica che l'oggetto pdfBiglietto non sia null
			if (pdfBiglietto == null) {
				logger.severe("Impossibile generare PDF: pdfBiglietto è null");
				return null;
			}

			// Creazione delle directory necessarie e del nome file
			try {
				outputPath = creaDirectoryEFilename(pdfBiglietto);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Errore nella creazione delle directory: " + e.getMessage(), e);
				return null;
			}

			// Inizializzazione PDFLib
			p = new PDFLibWrapper();

			try {
				// Inizializzazione del documento
				p.inizializzaDocumento(outputPath);

				// Creazione delle varie sezioni del biglietto
				try {
					creaTesiataPDF(p, pdfBiglietto);
					creaBloccoInfoVoloPDF(p, pdfBiglietto);
					creaDettagliPasseggeroPDF(p, pdfBiglietto);
					creaDettagliAcquistoPDF(p, pdfBiglietto);
					creaFooterPDF(p, pdfBiglietto);
				} catch (Exception e) {
					logger.log(Level.SEVERE, "Errore nella creazione delle sezioni del PDF: " + e.getMessage(), e);
					throw new PdfGenerationException("Errore nella creazione delle sezioni del PDF", e);
				}

				// Finalizzazione del documento
				p.finalizzaDocumento();

				logger.info("PDF generato con successo per prenotazione: " + pdfBiglietto.getPrenotazione().getId());
				return outputPath;

			} catch (Exception e) {
				logger.log(Level.SEVERE, "Errore nell'inizializzazione del documento PDF: " + e.getMessage(), e);
				return null;
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Errore imprevisto nella generazione del PDF: " + e.getMessage(), e);
			return null;
		} finally {
			// Chiusura sicura della risorsa PDFLib
			if (p != null) {
				try {
					p.close();
				} catch (Exception e) {
					logger.log(Level.WARNING, "Errore nella chiusura delle risorse PDFLib: " + e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * Crea le directory necessarie e genera il nome del file
	 */
	private String creaDirectoryEFilename(PdfBiglietto pdfBiglietto) throws IOException {
		File pdfDirectory = new File(pdfBaseDir);
		File userDirectory = new File(pdfDirectory, "utente_" + pdfBiglietto.getUtente().getId());

		if (!pdfDirectory.exists() && !pdfDirectory.mkdirs()) {
			throw new IOException("Impossibile creare la directory: " + pdfDirectory.getAbsolutePath());
		}

		if (!userDirectory.exists() && !userDirectory.mkdirs()) {
			throw new IOException("Impossibile creare la directory utente: " + userDirectory.getAbsolutePath());
		}

		String fileName = String.format("biglietto_%d_%s.pdf", pdfBiglietto.getPrenotazione().getId(),
				pdfBiglietto.getPrenotazione().getDataAcquisto().format(DateTimeFormatter.ofPattern("yyyyMMdd")));

		return new File(userDirectory, fileName).getAbsolutePath();
	}

	/**
	 * Crea l'intestazione del PDF
	 * 
	 * @throws PdfGenerationException
	 */
	private void creaTesiataPDF(PDFLibWrapper p, PdfBiglietto pdfBiglietto) throws PdfGenerationException {
		// Header blu
		p.setColor("fillstroke", "rgb", 0.23, 0.38, 0.85, 1.0);
		p.drawRect(0, PAGE_HEIGHT - HEADER_HEIGHT, PAGE_WIDTH, HEADER_HEIGHT);

		// Titolo in bianco
		p.setColor("fill", "rgb", 1.0, 1.0, 1.0, 1.0);
		p.setFont(p.getBoldFont(), 32.0);
		p.showTextAt(50, PAGE_HEIGHT - 57, "AirLine Frontier");

		p.setFont(p.getFont(), 25.0);
		p.showTextAt(50, PAGE_HEIGHT - 87, "BIGLIETTO AEREO");

		// Codice volo
		p.setFont(p.getFont(), 16.0);
		String codiceText = "Codice: " + pdfBiglietto.getPasseggero().getBiglietto().getCodice();
		// Lo posizioniamo in alto a destra nell'intestazione
		showTextAlignedRight(p, PAGE_WIDTH - (MARGIN + 15), PAGE_HEIGHT - 57, codiceText, 16.0);
	}

	/**
	 * Visualizza testo allineato a destra
	 * 
	 * @param p        Wrapper PDFLib
	 * @param xRight   Coordinata x dell'estremità destra
	 * @param y        Coordinata y
	 * @param text     Testo da visualizzare
	 * @param fontSize Dimensione del font
	 * @throws PdfGenerationException
	 */
	private void showTextAlignedRight(PDFLibWrapper p, int xRight, int y, String text, double fontSize)
			throws PdfGenerationException {
		try {
			double textWidth = p.getTextWidth(text, fontSize);
			p.showTextAt((int) (xRight - textWidth), y, text);
		} catch (Exception e) {
			throw new PdfGenerationException("Errore nel posizionamento del testo allineato a destra", e);
		}
	}

	/**
	 * Crea il blocco con le informazioni del volo
	 * 
	 * @throws PdfGenerationException
	 */
	private void creaBloccoInfoVoloPDF(PDFLibWrapper p, PdfBiglietto pdfBiglietto) throws PdfGenerationException {
		// Rettangolo sfondo grigio chiaro - AUMENTATO IN ALTEZZA
		p.setColor("fillstroke", "rgb", 0.95, 0.95, 0.95, 1.0);
		p.drawRect(MARGIN, PAGE_HEIGHT - 250, PAGE_WIDTH - 2 * MARGIN, 140);

		// Impostazione colore testo nero
		p.setColor("fill", "rgb", 0.2, 0.2, 0.2, 1.0);

		// From
		p.setFont(p.getBoldFont(), 10.0);
		p.setColor("fill", "rgb", 0.0, 0.0, 0.0, 1.0);
		p.showTextAt(55, PAGE_HEIGHT - 132, "FROM");
		p.setColor("fill", "rgb", 0.2, 0.2, 0.2, 1.0);

		// Aeroporto partenza
		p.setFont(p.getBoldFont(), 18.0);
		p.showTextAt(58, PAGE_HEIGHT - 152,
				pdfBiglietto.getBiglietto().getVolo().getTratta().getAeroportoInPartenza().getNome());

		p.setFont(p.getBoldFont(), 14.0);
		p.showTextAt(58, PAGE_HEIGHT - 172,
				pdfBiglietto.getBiglietto().getVolo().getTratta().getAeroportoInPartenza().getCitta());

		// To
		p.setFont(p.getBoldFont(), 10.0);
		p.setColor("fill", "rgb", 0.0, 0.0, 0.0, 1.0);
		p.showTextAt(55, PAGE_HEIGHT - 198, "TO");
		p.setColor("fill", "rgb", 0.2, 0.2, 0.2, 1.0);

		// Aeroporto arrivo
		p.setFont(p.getBoldFont(), 18.0);
		p.showTextAt(58, PAGE_HEIGHT - 218,
				pdfBiglietto.getBiglietto().getVolo().getTratta().getAeroportoInArrivo().getNome());

		p.setFont(p.getBoldFont(), 14.0);
		p.showTextAt(58, PAGE_HEIGHT - 238,
				pdfBiglietto.getBiglietto().getVolo().getTratta().getAeroportoInArrivo().getCitta());

		// Data e ora partenza - MARGINE CORRETTO PER EVITARE TAGLI
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm");
		p.setFont(p.getFont(), 14.0);
		String dataOraText = pdfBiglietto.getBiglietto().getVolo().getDataPartenza().format(formatter);
		// Utilizziamo un margine di sicurezza (MARGIN + 15) per evitare il taglio
		showTextAlignedRight(p, PAGE_WIDTH - (MARGIN + 15), PAGE_HEIGHT - 162, dataOraText, 14.0);

		// Codice biglietto - MARGINE CORRETTO PER EVITARE TAGLI
		p.setFont(p.getFont(), 14.0);
		String codiceText = "Codice volo: " + pdfBiglietto.getPasseggero().getBiglietto().getVolo().getCodiceVolo();
		// Utilizziamo un margine di sicurezza (MARGIN + 15) per evitare il taglio
		showTextAlignedRight(p, PAGE_WIDTH - (MARGIN + 15), PAGE_HEIGHT - 218, codiceText, 14.0);

		// Linea separatrice
		p.setColor("stroke", "rgb", 0.8, 0.8, 0.8, 1.0);
		p.drawLine(MARGIN, PAGE_HEIGHT - 260, PAGE_WIDTH - MARGIN, PAGE_HEIGHT - 260);
	}

	/**
	 * Crea la sezione con i dettagli del passeggero
	 * 
	 * @throws PdfGenerationException
	 */
	private void creaDettagliPasseggeroPDF(PDFLibWrapper p, PdfBiglietto pdfBiglietto) throws PdfGenerationException {
		p.setColor("fill", "rgb", 0.2, 0.2, 0.2, 1.0);
		p.setFont(p.getBoldFont(), 16.0);
		p.showTextAt(MARGIN, PAGE_HEIGHT - 292, "DETTAGLI PASSEGGERO");

		p.setFont(p.getFont(), 14.0);
		p.showTextAt(MARGIN, PAGE_HEIGHT - 322,
				"Nome: " + pdfBiglietto.getUtente().getNome() + " " + pdfBiglietto.getUtente().getCognome());
		p.showTextAt(MARGIN, PAGE_HEIGHT - 342, "Email: " + pdfBiglietto.getUtente().getEmail());
		p.showTextAt(MARGIN, PAGE_HEIGHT - 362, "Posto prenotato: " + pdfBiglietto.getPasseggero().getPosto());
	}

	/**
	 * Crea la sezione con i dettagli dell'acquisto
	 * 
	 * @throws PdfGenerationException
	 */
	private void creaDettagliAcquistoPDF(PDFLibWrapper p, PdfBiglietto pdfBiglietto) throws PdfGenerationException {
		p.setColor("fill", "rgb", 0.2, 0.2, 0.2, 1.0);
		p.setFont(p.getBoldFont(), 16.0);
		p.showTextAt(MARGIN, PAGE_HEIGHT - 392, "DETTAGLI ACQUISTO");

		p.setFont(p.getFont(), 14.0);
		p.showTextAt(MARGIN, PAGE_HEIGHT - 422, "Codice prenotazione: " + pdfBiglietto.getPrenotazione().getCodice());
		p.showTextAt(MARGIN, PAGE_HEIGHT - 442, "Data acquisto: "
				+ pdfBiglietto.getPrenotazione().getDataAcquisto().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
	}

	/**
	 * Crea il footer del PDF
	 * 
	 * @throws PdfGenerationException
	 */
	private void creaFooterPDF(PDFLibWrapper p, PdfBiglietto pdfBiglietto) throws PdfGenerationException {
		// Rettangolo sfondo grigio chiaro per il footer
		p.setColor("fillstroke", "rgb", 0.95, 0.95, 0.95, 1.0);
		p.drawRect(0, 0, PAGE_WIDTH, 60);

		// Prezzo totale
		p.setColor("fill", "rgb", 0.2, 0.2, 0.2, 1.0);
		p.setFont(p.getBoldFont(), 16.0);
		p.showTextAt(MARGIN, 35, "PREZZO TOTALE");
		p.setFont(p.getBoldFont(), 20.0);
		p.showTextAt(MARGIN, 15, "€ " + String.format("%.2f", pdfBiglietto.getBiglietto().getCosto()));
	}

	/**
	 * Wrapper per la libreria PDFLib per migliorare la gestione delle risorse e
	 * semplificare le operazioni comuni
	 */
	private class PDFLibWrapper implements AutoCloseable {
		private final pdflib p;
		private int font;
		private int boldFont;
		private boolean initialized = false;

		public PDFLibWrapper() throws PdfGenerationException {
			try {
				p = new pdflib();
				initialized = true;
			} catch (Exception e) {
				throw new PdfGenerationException("Errore nell'inizializzazione di PDFLib", e);
			}
		}

		public void inizializzaDocumento(String outputPath) throws Exception {
			if (p.begin_document(outputPath, "") == -1) {
				throw new Exception("Errore nella creazione del PDF: " + p.get_errmsg());
			}

			try {
				p.set_info("Creator", "AirLine Frontier");
				p.set_info("Title", "Biglietto Aereo");
				p.begin_page_ext(PAGE_WIDTH, PAGE_HEIGHT, "");

				try {
					font = p.load_font("Helvetica", "unicode", "");
					if (font == -1) {
						throw new Exception("Errore nel caricamento del font Helvetica: " + p.get_errmsg());
					}
				} catch (Exception e) {
					logger.log(Level.WARNING, "Errore nel caricamento del font normale. Utilizzo font di fallback.", e);
					font = 0; // Font di fallback
				}

				try {
					boldFont = p.load_font("Helvetica-Bold", "unicode", "");
					if (boldFont == -1) {
						throw new Exception("Errore nel caricamento del font Helvetica-Bold: " + p.get_errmsg());
					}
				} catch (Exception e) {
					logger.log(Level.WARNING, "Errore nel caricamento del font grassetto. Utilizzo font normale.", e);
					boldFont = font; // Utilizza il font normale come fallback
				}
			} catch (Exception e) {
				// Se c'è un errore dopo aver iniziato il documento, tentiamo di annullarlo
				try {
					p.end_document("");
				} catch (Exception ex) {
					// Ignoriamo errori nella pulizia
				}
				throw e;
			}
		}

		public void finalizzaDocumento() throws PdfGenerationException {
			try {
				p.end_page_ext("");
			} catch (Exception e) {
				throw new PdfGenerationException("Errore nella chiusura della pagina PDF", e);
			}

			try {
				p.end_document("");
			} catch (Exception e) {
				throw new PdfGenerationException("Errore nella finalizzazione del documento PDF", e);
			}
		}

		public void setColor(String type, String colorspace, double c1, double c2, double c3, double c4)
				throws PdfGenerationException {
			try {
				p.setcolor(type, colorspace, c1, c2, c3, c4);
			} catch (Exception e) {
				throw new PdfGenerationException("Errore nell'impostazione del colore", e);
			}
		}

		public void drawRect(int x, int y, int width, int height) throws PdfGenerationException {
			try {
				p.rect(x, y, width, height);
				p.fill();
			} catch (Exception e) {
				throw new PdfGenerationException("Errore nel disegno di un rettangolo", e);
			}
		}

		public void drawLine(int x1, int y1, int x2, int y2) throws PdfGenerationException {
			try {
				p.moveto(x1, y1);
				p.lineto(x2, y2);
				p.stroke();
			} catch (Exception e) {
				throw new PdfGenerationException("Errore nel disegno di una linea", e);
			}
		}

		public void setFont(int fontHandle, double size) throws PdfGenerationException {
			try {
				if (fontHandle <= 0) {
					logger.warning("Tentativo di utilizzare un font non valido. Utilizzo del font predefinito.");
					return;
				}
				p.setfont(fontHandle, size);
			} catch (Exception e) {
				throw new PdfGenerationException("Errore nell'impostazione del font", e);
			}
		}

		public void showTextAt(int x, int y, String text) throws PdfGenerationException {
			try {
				if (text == null) {
					logger.warning("Tentativo di visualizzare testo null. Ignorato.");
					return;
				}
				p.set_text_pos(x, y);
				p.show(text);
			} catch (Exception e) {
				throw new PdfGenerationException("Errore nella visualizzazione del testo: " + text, e);
			}
		}

		public double getTextWidth(String text, double fontSize) throws Exception {
			if (text == null || text.isEmpty()) {
				return 0;
			}

			// Imposta il font corrente per ottenere la larghezza corretta
			double width = p.stringwidth(text, font, fontSize);
			if (width <= 0) {
				// Fallback se stringwidth non funziona correttamente
				return text.length() * fontSize * 0.6; // Approssimazione
			}
			return width;
		}

		public int getFont() {
			return font;
		}

		public int getBoldFont() {
			return boldFont;
		}

		@Override
		public void close() {
			if (initialized) {
				try {
					p.delete();
				} catch (Exception e) {
					logger.log(Level.WARNING, "Errore nella chiusura di PDFLib", e);
				}
			}
		}
	}

	/**
	 * Eccezione specifica per errori nella generazione dei PDF
	 */
	public static class PdfGenerationException extends Exception {
		private static final long serialVersionUID = 1L;

		public PdfGenerationException(String message) {
			super(message);
		}

		public PdfGenerationException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}