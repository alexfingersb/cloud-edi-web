package br.com;

import br.com.it3.model.enums.Profile;

public class Main {
	
	
	public static void main(String[] args) {
		String profile = Profile.valueOf("Administrador").toString();
		System.out.println("Profile " + profile);
	}

}
