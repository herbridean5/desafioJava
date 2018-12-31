package com.bruno.desafio;

public class Usuario {
	
	 private int id_usuario;
	 private String email;
	 private double valor_disponivel;
	 
	 public Usuario(int id_usuario, String email, double valor_disponivel) {
		super();
		this.id_usuario = id_usuario;
		this.email = email;
		this.valor_disponivel = valor_disponivel;
	}
	 
	public int getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public double getValor_disponivel() {
		return valor_disponivel;
	}
	public void setValor_disponivel(double valor_disponivel) {
		this.valor_disponivel = valor_disponivel;
	}
	
	 
	
}
