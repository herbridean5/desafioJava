package com.bruno.desafio;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

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

	// lista que sera criada a partir do banco de dados
	List<Usuario> usuarios = new ArrayList<>();

	// lista de monitoramentos
	List<Monitoramento> monits = new ArrayList<>();

	// sender que sera utilizado para enviar email
	@Autowired
	private JavaMailSender sender;

	@RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
	public String index(Model model) {
		// limpando listas, em algumas ocasioes quando acontecia um reload da pagina, os
		// valores eram duplicados
		usuarios.clear();
		monits.clear();

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
		// fim da consulta de usuarios

		// criando conexão com o banco para a consulta de monitoramentos

		try {
			conn = getMysqlDataSource().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("Select * from monitoramento");

			while (rs.next()) {
				// adicionando todos os monitoramentos que estáo cadastrados no banco
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
		// fim da consulta de monitoramentos

		// adicionando lista de usuarios ao model
		model.addAttribute("usuarios", usuarios);

		// adicionando lista de monitoramentos ao model
		model.addAttribute("monits", monits);

		return "index";
	}

	@RequestMapping(value = { "/simulacao" }, method = RequestMethod.GET)
	public String simulacao(@RequestParam("usuario") int id_usuario, @RequestParam("monit") int id_monit, Model model)
			throws Exception {
		// criando as variaveis necessaris para criar a conexao com o banco para a
		// consulta de usuarios
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		// criando e inicializando variaveis que serao utilizadas
		String mens_tela = "", email_sim = "", empresa_sim = "";
		Monitoramento monit_sim = null;
		Double valor_sim = 0.0, vcompra_sim = 0.0, vvenda_sim = 0.0, atual_preco = 0.0, qnt_acoes = 0.0;
		// valor maximo e minimo para a funçao do random, o random vai variar de 8 ate 12
		int max = 1, min = -2, registro = 0;

		// checando se o usuario ja não possui açoes que não foram vendidas
		// caso possua, adicionando esse saldo na qnt_acoes, vale lembrar que isso é por
		// monitoramento, porem o saldo é da conta
		try {
			conn = getMysqlDataSource().getConnection();
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select qnt_acoes from usuarioxmonit where fk_usuario = " + id_usuario
					+ " and fk_monit = " + id_monit + ";");
			while (rs.next()) {
				qnt_acoes = rs.getDouble(1);
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
		// esse for passa por todos os objetos da lista e pega apenas o usuario cujo ID
		// foi passado por GET
		for (Usuario u : usuarios) {
			if (u.getId_usuario() == id_usuario)
			// pegando email e valor disponivel para gastar
			email_sim = u.getEmail();
			valor_sim = u.getValor_disponivel();
		}
		// exibindo em tela os dados do usuario
		mens_tela += "Email do Usuario: " + email_sim + "\nQuantidade para gastar: " + valor_sim
				+ ", Quantidade de açoes: " + qnt_acoes + "\n\n";
		// esse for passa por todos os objetos da lista e pega apenas o monitoramento
		// cujo ID foi passado por GET
		for (Monitoramento m : monits) {
			if (m.getId_monit() == id_monit)
				;
			monit_sim = monits.get(id_monit - 1);
			// pegando email e valor disponivel para gastar
			empresa_sim = monit_sim.getNome_empresa();
			vcompra_sim = monit_sim.getValor_compra();
			vvenda_sim = monit_sim.getValor_venda();
		}
		// exibindo em tela os dados do monitoramento
		mens_tela += "Nome empresa: " + empresa_sim + "\nValor compra: " + vcompra_sim + "\nValor venda: " + vvenda_sim;

		// loop gerando 100 valores de preço de ação, em cada loop analisando a compra e
		// venda das açoes disponiveis
		for (int i = 0; i < 10; i++) {
			// gerando randomicamente o preço atual das açoes
			atual_preco = 10 + (1 * (Math.random() * ((max - min) + 1)) + min);

			// caso o preço da ação esteja acima do valor de venda selecionada pelo usuario
			// e a quantidade de açoes disponivel para o usuario seja maior que 0, vender
			// todas as açoes
			if (atual_preco >= vvenda_sim && qnt_acoes > 0) {
				valor_sim = qnt_acoes * atual_preco;

				mens_tela += "\n\npreco atual da ação: " + atual_preco + "\n";
				mens_tela += "\nVENDENDO: " + qnt_acoes + " ações\n";

				// inserindo em banco o historico de venda
				try {
					conn = getMysqlDataSource().getConnection();
					stmt = conn.createStatement();
					stmt.executeUpdate(
							"insert into historico_venda (nome_empresa,preco_venda,quantidade_venda,data_venda,hora_venda,fk_id_usuario) "
									+ "values ('" + empresa_sim + "'," + atual_preco + "," + qnt_acoes
									+ ",CURDATE(),CURRENT_TIME()," + id_usuario + ");");
					mens_tela += "\n\nsalvando historico de venda\n\n";
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

				// enviando email notificando venda
				try {
					SimpleMailMessage message = new SimpleMailMessage();
					message.setTo(email_sim);
					message.setSubject("Venda de ações da empresa "+empresa_sim+" efetuada");
					message.setText(qnt_acoes + " Ações vendidas da empresa: "+empresa_sim+", pelo preço de: " + atual_preco
							+ " Reais,  resultando em: " + valor_sim + " Reais");
					sender.send(message);
					mens_tela += "\nEmail Enviado!\n";
				} catch (Exception ex) {
					mens_tela += "\nError ao enviar email: " + ex + "\n";
				}

				mens_tela += "-------------------------\n";
				// zerando as acoes que foram vendidas
				qnt_acoes = 0.0;
			} else if (atual_preco <= vcompra_sim && valor_sim > 0.0) {
				// caso o preço atual seja menor que o preço de compra
				// e a quantidade em dinheiro do usuario seja maior que 0, comprar todas as
				// açoes possiveis, zerar valor em conta
				qnt_acoes = valor_sim / atual_preco;
				valor_sim = 0.0;

				mens_tela += "\n\npreco atual da ação: " + atual_preco + "\n";
				mens_tela += "\nCOMPRANDO: " + qnt_acoes + " ações\n";

				// inserindo em banco o historico de compra
				try {
					conn = getMysqlDataSource().getConnection();
					stmt = conn.createStatement();
					stmt.executeUpdate(
							"insert into historico_compra (nome_empresa,preco_compra,quantidade_compra,data_compra,hora_compra,fk_id_usuario) "
									+ "values ('" + empresa_sim + "'," + atual_preco + "," + qnt_acoes
									+ ",CURDATE(),CURRENT_TIME()," + id_usuario + ");");
					mens_tela += "\nsalvando historico de compra em banco\n\n";
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

				//enviando email notificando compra
				try {
					SimpleMailMessage message = new SimpleMailMessage();
					message.setTo(email_sim);
					message.setSubject("Compra de ações da empresa "+empresa_sim+" efetuada");
					message.setText(qnt_acoes + " Ações compradas da empresa: "+empresa_sim+", pelo preço de: " + atual_preco + " Reais");
					sender.send(message);
					mens_tela += "\nEmail Enviado!\n";
				} catch (Exception ex) {
					mens_tela += "\nError ao enviar email: " + ex + "\n";
				}

				mens_tela += "-------------------------\n";
			}

			// esse sleep vai causar 5s por iteração de delay ou seja, 8.33 minutos no total
			// como a interface só é atualizada no final (no return) por mais de 8 minutos a
			// tela vai parecer travada
			Thread.sleep(5000);
		}

		// atualizando o banco de dados com as informaçoes da simulaçao
		try {
			conn = getMysqlDataSource().getConnection();
			stmt = conn.createStatement();
			
			//consultando banco para ver se o usuario ja possui registro
			rs = stmt.executeQuery("select count(*) from usuarioxmonit where fk_usuario = "+id_usuario+" and fk_monit = "+id_monit+";");
			while (rs.next()) {
				registro = rs.getInt(1);
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
		try {
			conn = getMysqlDataSource().getConnection();
			stmt = conn.createStatement();
			if(registro == 0) {
				//caso nao possua registro, inserir novo registro
				stmt.executeUpdate("INSERT INTO usuarioxmonit (fk_usuario, fk_monit, qnt_acoes) VALUES ("+id_usuario+", "+id_monit+", "+qnt_acoes+");");
				mens_tela += "\nInserindo saldo em banco\n";
			}else if(registro != 0) {
				//caso possua registro, atualizar ele
				stmt.executeUpdate("UPDATE usuarioxmonit, usuario SET qnt_acoes = " + qnt_acoes + ", valor_disponivel = "
						+ valor_sim + " WHERE fk_usuario = " + id_usuario + " " + "and fk_monit = " + id_monit
						+ " and id_usuario = " + id_usuario + "");
				mens_tela += "\nAtualizando saldo em banco\n";
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
	
		mens_tela += "\nvalor final em dinheiro: " + valor_sim + ", valor final em açoes: " + qnt_acoes;
		model.addAttribute("textot", mens_tela);
		return "simulacao";
	}

}