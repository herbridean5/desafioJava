package com.bruno.desafio;

import java.sql.Date;

public class HistoricoVenda {
	private int id_venda, id_usuario;
	private String nome_empresa,hora_venda;
	private double preco_venda, quantidade_venda;
	private Date data_venda;
	
	public HistoricoVenda(int id_venda, String nome_empresa, double preco_venda, double quantidade_venda,
			Date data_venda, String hora_venda, int id_usuario) {
		super();
		this.id_venda = id_venda;
		this.nome_empresa = nome_empresa;
		this.preco_venda = preco_venda;
		this.quantidade_venda = quantidade_venda;
		this.data_venda = data_venda;
		this.hora_venda = hora_venda;
		this.id_usuario = id_usuario;
	}
	public int getId_venda() {
		return id_venda;
	}
	public void setId_venda(int id_venda) {
		this.id_venda = id_venda;
	}
	public int getId_usuario() {
		return id_usuario;
	}
	public void setId_usuario(int id_usuario) {
		this.id_usuario = id_usuario;
	}
	public String getNome_empresa() {
		return nome_empresa;
	}
	public void setNome_empresa(String nome_empresa) {
		this.nome_empresa = nome_empresa;
	}
	public String getHora_venda() {
		return hora_venda;
	}
	public void setHora_venda(String hora_venda) {
		this.hora_venda = hora_venda;
	}
	public double getPreco_venda() {
		return preco_venda;
	}
	public void setPreco_venda(double preco_venda) {
		this.preco_venda = preco_venda;
	}
	public double getQuantidade_venda() {
		return quantidade_venda;
	}
	public void setQuantidade_venda(double quantidade_venda) {
		this.quantidade_venda = quantidade_venda;
	}
	public Date getData_venda() {
		return data_venda;
	}
	public void setData_venda(Date data_venda) {
		this.data_venda = data_venda;
	}
	
	
}
