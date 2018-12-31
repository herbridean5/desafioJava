package com.bruno.desafio;

import java.sql.Date;

public class HistoricoCompra {
	private int id_compra, id_usuario;
	private String nome_empresa,hora_compra;
	private double preco_compra, quantidade_compra;
	private Date data_compra;
	

	public HistoricoCompra(int id_compra, String nome_empresa, double preco_compra,
			double quantidade_compra, Date data_compra, String hora_compra, int id_usuario) {
		super();
		this.id_compra = id_compra;
		this.id_usuario = id_usuario;
		this.nome_empresa = nome_empresa;
		this.hora_compra = hora_compra;
		this.preco_compra = preco_compra;
		this.quantidade_compra = quantidade_compra;
		this.data_compra = data_compra;
	}

	public int getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}

	public int getId_compra() {
		return id_compra;
	}
	public void setId_compra(int id_compra) {
		this.id_compra = id_compra;
	}
	public String getNome_empresa() {
		return nome_empresa;
	}
	public void setNome_empresa(String nome_empresa) {
		this.nome_empresa = nome_empresa;
	}
	public String getHora_compra() {
		return hora_compra;
	}
	public void setHora_compra(String hora_compra) {
		this.hora_compra = hora_compra;
	}
	public double getPreco_compra() {
		return preco_compra;
	}
	public void setPreco_compra(double preco_compra) {
		this.preco_compra = preco_compra;
	}
	public double getQuantidade_compra() {
		return quantidade_compra;
	}
	public void setQuantidade_compra(double quantidade_compra) {
		this.quantidade_compra = quantidade_compra;
	}
	public Date getData_compra() {
		return data_compra;
	}
	public void setData_compra(Date data_compra) {
		this.data_compra = data_compra;
	}
	
}
