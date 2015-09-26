package br.com.it3.model.enums;

public enum Profile {
	ADMINISTRADOR ("Administrador"), USUARIO("Usuário");
	
	private String profile;
	
	Profile(String profile) {
		this.profile = profile;
	}

	@Override
	public String toString() {
		return this.profile;
	}
	
}
