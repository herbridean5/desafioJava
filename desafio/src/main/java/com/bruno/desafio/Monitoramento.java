package com.bruno.desafio;

public class Monitoramento {
	String nome_empresa;
	double valor_venda, valor_compra;
	int id_monit;
	
	public Monitoramento(int id_monit ,String nome_empresa, double valor_venda, double valor_compra) {
		super();
		this.nome_empresa = nome_empresa;
		this.valor_venda = valor_venda;
		this.valor_compra = valor_compra;
		this.id_monit = id_monit;
	}
	
	public String getNome_empresa() {
		return nome_empresa;
	}
	public void setNome_empresa(String nome_empresa) {
		this.nome_empresa = nome_empresa;
	}
	public double getValor_venda() {
		return valor_venda;
	}
	public void setValor_venda(double valor_venda) {
		this.valor_venda = valor_venda;
	}
	public double getValor_compra() {
		return valor_compra;
	}
	public void setValor_compra(double valor_compra) {
		this.valor_compra = valor_compra;
	}
	public int getId_monit() {
		return id_monit;
	}
	public void setId_monit(int id_monit) {
		this.id_monit = id_monit;
	}
}
