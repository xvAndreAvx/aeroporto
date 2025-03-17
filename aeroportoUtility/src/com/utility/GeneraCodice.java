package com.utility;

import java.util.Random;

public class GeneraCodice {
	
	public static String generaCodiceBiglietto() {
		Random random = new Random();
		StringBuilder codice = new StringBuilder();

		// Genera 2 lettere maiuscole (A-Z)
		for (int i = 0; i < 2; i++) {
			// Le lettere maiuscole in ASCII vanno da 65 (A) a 90 (Z)
			char lettera = (char) (random.nextInt(26) + 65);
			codice.append(lettera);
		}

		// Genera 10 numeri (0-9)
		for (int i = 0; i < 10; i++) {
			int numero = random.nextInt(10);
			codice.append(numero);
		}

		return codice.toString();
	}
	
	public static String generaCodiceVolo() {
		Random random = new Random();
		StringBuilder codice = new StringBuilder();

		// Genera 2 lettere maiuscole (A-Z)
		for (int i = 0; i < 2; i++) {
			// Le lettere maiuscole in ASCII vanno da 65 (A) a 90 (Z)
			char lettera = (char) (random.nextInt(26) + 65);
			codice.append(lettera);
		}

		// Genera 4 numeri (0-9)
		for (int i = 0; i < 4; i++) {
			int numero = random.nextInt(10);
			codice.append(numero);
		}

		return codice.toString();
	}

}
