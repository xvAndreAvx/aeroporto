package com.criptatore;

public class PasswordCript {
	
	private static final int SHIFT = 5;
	
	public static String criptPassword(String password) {
		StringBuilder encrypted = new StringBuilder();

        for (char c : password.toCharArray()) {
            switch (c) {
                case 'a': case 'A': encrypted.append('X'); break;
                case 'e': case 'E': encrypted.append('S'); break;
                case 'i': case 'I': encrypted.append('K'); break;
                case 'o': case 'O': encrypted.append('Z'); break;
                case 'u': case 'U': encrypted.append('Y'); break;
                case 'n': case 'N': encrypted.append('J'); break;
                case 'd': case 'D': encrypted.append('W'); break;
                default: 
                    encrypted.append((char)(c + SHIFT));
            }
        }
        return encrypted.toString();
    }

    
	public static boolean verificaPassword(String inputPassword, String cryptedPassword) {
	    return criptPassword(inputPassword).equals(cryptedPassword);
	}

}
