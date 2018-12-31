package com.bruno.desafio;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import javax.sql.DataSource;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.jdbc.MysqlDataSource;

@RestController
public class ApiDesafio {

	public static DataSource getMysqlDataSource() throws SQLException {
		MysqlDataSource dataSource = new MysqlDataSource();

		// Setando propriedades do dataSource
		// MODIFICAR ESSA PARTE, MUDANDO O setUser E O setPassword
		dataSource.setServerName("localhost");
		dataSource.setPortNumber(3306);
		dataSource.setServerTimezone("UTC");
		dataSource.setDatabaseName("db_desafio_teste");
		dataSource.setUser("root");
		dataSource.setPassword("trocarSenha123");
		return dataSource;
	}

	// lista de usuarios
	List<Usuario> usuarios = new ArrayList<>();

	// lista de monitoramentos
	List<Monitoramento> monits = new ArrayList<>();
	
	// lista de historico de compra
	List<HistoricoCompra> hcompra = new ArrayList<>();
	
	// lista de historico de venda
	List<HistoricoVenda> hvenda = new ArrayList<>();


	// api para consultar usuarios
	@RequestMapping(value = { "/ConsultContas" }, method = RequestMethod.GET)
	public List<Usuario> consultContas(Model model) {

		// limpando lista
		usuarios.clear();

		// criando as variaveis necessaris para criar a conexao com o banco para a
		// consulta de usuarios
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = getMysqlDataSource().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * from usuario");

			while (rs.next()) {
				// adicionando todos os usuários que estáo cadastrados no banco
				usuarios.add(new Usuario(rs.getInt(1), rs.getString(2), rs.getDouble(3)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//retorna apenas a lista de usuarios em forma de JSON, sem nenhum codigo de BADREQUEST ou OK
		return usuarios;
	}

	// api para criar usuarios
	@RequestMapping(value = { "/CriarContas" }, method = RequestMethod.POST, consumes = "application/json")
	public String criarContas(Model model,@RequestBody String dados_usuario) throws IOException{
		//transformando o JSON para TEXTO
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonObject = mapper.readTree(dados_usuario);

		//pegando os valores de email e valor disponivel
		JsonNode json_email = jsonObject.get("email_usuario");
		JsonNode json_valor = jsonObject.get("valor_disponivel");
				
		// criando as variaveis necessaris para criar a conexao com o banco para
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		 
		try {
			conn = getMysqlDataSource().getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate("insert into usuario (email_usuario,valor_disponivel) values ('" + json_email.asText() + "',"
					+ json_valor.asDouble() + ");");
			return String.valueOf(HttpStatus.CREATED);
		} catch (SQLException e) {
			//retornando o codigo de nao aceito
			return String.valueOf(HttpStatus.NOT_ACCEPTABLE);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// api para consultar monitoramentos
	@RequestMapping(value = { "/ConsultMonit" }, method = RequestMethod.GET)
	public List<Monitoramento> consultMonit(Model model) {

		// limpando lista
		monits.clear();

		// criando as variaveis necessaris para criar a conexao com o banco para a
		// consulta de usuarios
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getMysqlDataSource().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * from monitoramento");
			while (rs.next()) {
				// adicionando todos os usuários que estáo cadastrados no banco
				monits.add(new Monitoramento(rs.getInt(1), rs.getString(2), rs.getDouble(3), rs.getDouble(4)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return monits;
	}

	// api para criar monitoramento
	@RequestMapping(value = { "/CriarMonit" }, method = RequestMethod.POST, consumes = "application/json")
	public String criarMonit(Model model,@RequestBody String dados_monit) throws IOException{
		//transformando o JSON para TEXTO
		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonObject = mapper.readTree(dados_monit);
		
		//pegando os valores de email e valor disponivel
		JsonNode json_nome = jsonObject.get("nome_empresa");
		JsonNode json_vcompra = jsonObject.get("preco_venda");
		JsonNode json_vvenda = jsonObject.get("preco_compra");
		
		// criando as variaveis necessaris para criar a conexao com o banco para
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			conn = getMysqlDataSource().getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate("insert into monitoramento (nome_empresa,preco_venda,preco_compra) values ('"
					+ json_nome.asText() + "'," + json_vvenda.asDouble() + "," + json_vcompra.asDouble() + ");");
			//retornando o codigo de criado
			return String.valueOf(HttpStatus.CREATED);
		} catch (SQLException e) {
			//retornando o codigo de nao aceito
			return String.valueOf(HttpStatus.NOT_ACCEPTABLE);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// api para deletar monitoramento
		@RequestMapping(value = "/DeletarMonit/{id}", method = RequestMethod.DELETE)
		public String deleteMonit(@PathVariable("id") int id_monit, Model model) {

			// criando as variaveis necessaris para criar a conexao com o banco
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				conn = getMysqlDataSource().getConnection();
				stmt = conn.createStatement();
				stmt.executeUpdate("delete from monitoramento where id_moni= '" + id_monit + "';");
				//retornando OK se foi deletado
				return String.valueOf(HttpStatus.OK);
			} catch (SQLException e) {
				e.printStackTrace();
				//retornando BAD REQUEST
				return String.valueOf(HttpStatus.BAD_REQUEST);
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// api para alterar monitoramento
		@RequestMapping(value = { "/AlterarMonit/{id}" }, method = RequestMethod.PUT)
		public String alterarMonits(@RequestBody String dados_monit, @PathVariable("id") int id_monit, Model model) throws IOException {

			//transformando o JSON para TEXTO
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonObject = mapper.readTree(dados_monit);
			
			//pegando os valores de email e valor disponivel
			JsonNode json_nome = jsonObject.get("nome_empresa");
			JsonNode json_vcompra = jsonObject.get("preco_venda");
			JsonNode json_vvenda = jsonObject.get("preco_compra");
			
			// criando as variaveis necessaris para criar a conexao com o banco
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;
			try {
				conn = getMysqlDataSource().getConnection();
				stmt = conn.createStatement();
				stmt.executeUpdate("update monitoramento set nome_empresa ='"+json_nome.asText()+"', preco_venda = "+json_vvenda.asDouble()+", preco_compra = "+json_vcompra.asDouble()+" "
						+ "where id_moni= "+id_monit+";");
				//retornando OK se foi deletado
				return String.valueOf(HttpStatus.OK);
			} catch (SQLException e) {
				e.printStackTrace();
				//retornando not found
				return String.valueOf(HttpStatus.NOT_FOUND);
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	// api para consultar historico de compra
	@RequestMapping(value = { "/ConsultCompra" }, method = RequestMethod.GET)
	public List<HistoricoCompra> consultCompra(Model model) {

		// limpando lista
		hcompra.clear();

		// criando as variaveis necessaris para criar a conexao com o banco para a
		// consulta de usuarios
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			conn = getMysqlDataSource().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT * from historico_compra");
			while (rs.next()) {
				// adicionando todos os usuários que estáo cadastrados no banco
				hcompra.add(new HistoricoCompra(rs.getInt(1),rs.getString(2),rs.getDouble(3),rs.getDouble(4),rs.getDate(5),rs.getString(6),rs.getInt(7)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return hcompra;
	}

	// api para consultar historico de venda
		@RequestMapping(value = { "/ConsultVenda" }, method = RequestMethod.GET)
		public List<HistoricoVenda> consultVenda(Model model) {

			// limpando lista
			hvenda.clear();

			// criando as variaveis necessaris para criar a conexao com o banco para a
			// consulta de usuarios
			Connection conn = null;
			Statement stmt = null;
			ResultSet rs = null;

			try {
				conn = getMysqlDataSource().getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT * from historico_venda");
				while (rs.next()) {
					// adicionando todos os usuários que estáo cadastrados no banco
					hvenda.add(new HistoricoVenda(rs.getInt(1),rs.getString(2),rs.getDouble(3),rs.getDouble(4),rs.getDate(5),rs.getString(6),rs.getInt(7)));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null) {
						rs.close();
					}
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return hvenda;
		}
}
