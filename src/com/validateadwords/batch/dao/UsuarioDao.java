package com.validateadwords.batch.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.validateadwords.batch.entitie.Permissao;
import com.validateadwords.batch.entitie.Usuario;
import com.validateadwords.batch.exceptions.DaoException;

public class UsuarioDao extends BaseDao{

	
	public Usuario atualizarEnderecoCep(String cep, Usuario u) throws Throwable{
		
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from cep where cep='"+cep+"'";
			
			rs = con.createStatement().executeQuery(query);
			if(rs.next()){
				u.setBairro(rs.getString("bairro"));
				u.setCidade(rs.getString("cidade"));
				u.setUf(rs.getString("uf"));
				u.setEndereco(rs.getString("tipo")+" "+rs.getString("logradouro"));
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado atualizarEnderecoCep", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		return u;
	}
	
	public void excluir(long idUsuario) throws Throwable{
		try{
			conectar();
			
			String q1 = "delete from usuario where id = ?" ;
			
			pstm = con.prepareStatement(q1);
			pstm.setLong(1, idUsuario);
			
			pstm.executeUpdate();
			
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado excluir", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
	}

	public Usuario buscarUsuario(String nome) throws Throwable{
		Usuario u = null;
		try{
			conectar();
			
			String query = "select * from usuario where nome='"+nome+"'";
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new Usuario();
				u.setBairro(rs.getString("bairro"));
				u.setCelular(rs.getString("celular"));
				u.setCep(rs.getString("cep"));
				u.setCidade(rs.getString("cidade"));
				u.setComplemento(rs.getString("complemento"));
				u.setCpf(rs.getString("cpf"));
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setEmail(rs.getString("email"));
				u.setEndereco(rs.getString("endereco"));
				u.setId(rs.getLong("id"));
				u.setIdCliente(rs.getInt("id_cliente"));
				u.setIdPerfil(rs.getInt("id_perfil"));
				u.setNome(rs.getString("nome"));
				u.setNumero(rs.getLong("numero"));
				u.setSenha(rs.getString("acesso"));
				u.setTelefone(rs.getString("telefone"));
				u.setUf(rs.getString("uf"));
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar usuario", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return u;
		
	}

//	public String getSenhaUsuario(long idUsuario) throws Throwable{
//		
//
//	}
	
	public Usuario buscarUsuario(String email, String senha) throws Throwable{
		Usuario u = null;
		try{
			conectar();
			
			String query = "select * from usuario where email='"+email+"' and senha = md5('"+senha+"')";
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new Usuario();
				u.setBairro(rs.getString("bairro"));
				u.setCelular(rs.getString("celular"));
				u.setCep(rs.getString("cep"));
				u.setCidade(rs.getString("cidade"));
				u.setComplemento(rs.getString("complemento"));
				u.setCpf(rs.getString("cpf"));
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setEmail(rs.getString("email"));
				u.setEndereco(rs.getString("endereco"));
				u.setId(rs.getLong("id"));
				u.setIdCliente(rs.getInt("id_cliente"));
				u.setIdPerfil(rs.getInt("id_perfil"));
				u.setNome(rs.getString("nome"));
				u.setNumero(rs.getLong("numero"));
				u.setSenha(senha);
				u.setTelefone(rs.getString("telefone"));
				u.setUf(rs.getString("uf"));
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar usuario", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return u;
		
	}
	
		public void alterar(Usuario u, List<Permissao> permissoes) throws Throwable{
		try{
			conectar();
			
			String q1 = "update usuario set bairro=?,celular=?,cep=?,cidade=?,complemento=?,cpf=?,email=?,endereco=?,id_perfil=?,nome=?,numero=?,telefone=?,uf=? where id = ?";
			
			logger.debug(ReflectionToStringBuilder.toString(u, ToStringStyle.MULTI_LINE_STYLE));
			
			pstm = con.prepareStatement(q1);
			
			pstm.setString(1, u.getBairro());
			pstm.setString(2, u.getCelular());
			pstm.setString(3, u.getCep());
			pstm.setString(4, u.getCidade());
			pstm.setString(5, u.getComplemento());
			pstm.setString(6, u.getCpf());
			pstm.setString(7, u.getEmail());
			pstm.setString(8, u.getEndereco());
			pstm.setLong(9, u.getIdPerfil());
			pstm.setString(10, u.getNome());
			pstm.setLong(11, u.getNumero());
			pstm.setString(12, u.getTelefone());
			pstm.setString(13, u.getUf());
			
			
			pstm.setLong(14, u.getId());
			
			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("Updating usuario failed, no rows affected.");
	        }
			
			//remove permissoes
			String q2 = "delete from usuario_permissao where id_usuario=?";
			pstm = con.prepareStatement(q2);
			pstm.setLong(1, u.getId());
			pstm.executeUpdate();
			
			//atualiza lista de permissoes
			for (Iterator iterator = permissoes.iterator(); iterator.hasNext();) {
				Permissao p = (Permissao) iterator.next();
				String q3 = "insert into usuario_permissao(id_permissao,id_usuario) values (?,?)";
				pstm = con.prepareStatement(q3);
				pstm.setInt(1, p.getId());
				pstm.setLong(2, u.getId());
				pstm.executeUpdate();
			}
		
			
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado atualizar usuario", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		
	}

	
		public void adicionar(Usuario u, List<Permissao> permissoes) throws Throwable{
		try{
			conectar();
			
			String q1 = "insert into usuario(bairro,celular,cep,cidade,complemento,cpf,data_cadastro,email,endereco,id_cliente,id_perfil,nome,numero,telefone,uf,senha,acesso)" +
					" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,md5(?),?)" ;
			
			logger.debug(ReflectionToStringBuilder.toString(u, ToStringStyle.MULTI_LINE_STYLE));
			
			pstm = con.prepareStatement(q1, Statement.RETURN_GENERATED_KEYS);
			
			pstm.setString(1, u.getBairro());
			pstm.setString(2, u.getCelular());
			pstm.setString(3, u.getCep());
			pstm.setString(4, u.getCidade());
			pstm.setString(5, u.getComplemento());
			pstm.setString(6, u.getCpf());
			pstm.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
			pstm.setString(8, u.getEmail());
			pstm.setString(9, u.getEndereco());
			pstm.setLong(10, u.getIdCliente());
			pstm.setLong(11, u.getIdPerfil());
			pstm.setString(12, u.getNome());
			pstm.setLong(13, u.getNumero());
			pstm.setString(14, u.getTelefone());
			pstm.setString(15, u.getUf());
			pstm.setString(16, u.getSenha());
			pstm.setString(17, u.getSenha());
			
			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("Creating usuario failed, no rows affected.");
	        }
			rs = pstm.getGeneratedKeys();
	        if (rs.next()) {
	            u.setId(rs.getLong(1));
	        } else {
	            throw new SQLException("Creating usuario failed, no generated key obtained.");
	        }
	        
	        
	        //persistir lista de permissoes
	        for (Iterator iterator = permissoes.iterator(); iterator.hasNext();) {
				Permissao p = (Permissao) iterator.next();
				String q2 = "insert into usuario_permissao(id_permissao,id_usuario) values (?,?)";
				pstm = con.prepareStatement(q2);
				pstm.setInt(1, p.getId());
				pstm.setLong(2, u.getId());
				pstm.executeUpdate();
			}
	        
	        
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado adicionar usuario", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		
	}

	
	public boolean existeEmail(String email) throws Throwable{
		boolean retorno = false;
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select count(id) from usuario where email='"+email+"'";
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				int total = rs.getInt(1);
				if(total>0){
					retorno = true;
				}
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado existeEmail", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return retorno;
		
	}
	
	public Usuario buscarUsuario(long id) throws Throwable{
		Usuario u = null;
		try{
			conectar();
			
			String query = "select * from usuario where id="+id;
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new Usuario();
				u.setBairro(rs.getString("bairro"));
				u.setCelular(rs.getString("celular"));
				u.setCep(rs.getString("cep"));
				u.setCidade(rs.getString("cidade"));
				u.setComplemento(rs.getString("complemento"));
				u.setCpf(rs.getString("cpf"));
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setEmail(rs.getString("email"));
				u.setEndereco(rs.getString("endereco"));
				u.setId(rs.getLong("id"));
				u.setIdCliente(rs.getInt("id_cliente"));
				u.setIdPerfil(rs.getInt("id_perfil"));
				u.setNome(rs.getString("nome"));
				u.setNumero(rs.getLong("numero"));
				u.setSenha(rs.getString("senha"));
				u.setTelefone(rs.getString("telefone"));
				u.setUf(rs.getString("uf"));
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar usuario", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return u;
		
	}
	
	
	public List<Permissao> listaTotasPermissoes() throws Throwable{
		List<Permissao> ps = new ArrayList<Permissao>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from permissao p order by p.id asc";
			
			pstm = con.prepareStatement(query);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				Permissao p = new Permissao();
				p.setId(rs.getInt("id"));
				p.setNome(rs.getString("nome"));
				ps.add(p);
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar listaTotasPermissoes", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return ps;
		
	}
	
	public List<Permissao> listaPermissoes(Usuario u) throws Throwable{
		List<Permissao> ps = new ArrayList<Permissao>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from permissao p ";
			query+="where p.id in (select cp.id_permissao from cliente_permissao cp where cp.id_cliente="+u.getIdCliente()+") ";
			
			query+="order by p.id asc";
			
			pstm = con.prepareStatement(query);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				Permissao p = new Permissao();
				p.setId(rs.getInt("id"));
				p.setNome(rs.getString("nome"));
				ps.add(p);
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar listaPermissoes", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return ps;
		
	}
	
	public List<Permissao> listaPermissoesUsuario(long idUsuario) throws Throwable{
		List<Permissao> ps = new ArrayList<Permissao>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select p.id,p.nome from permissao p inner join usuario_permissao up on up.id_permissao=p.id where up.id_usuario="+idUsuario;
			
			pstm = con.prepareStatement(query);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				Permissao p = new Permissao();
				p.setId(rs.getInt("id"));
				p.setNome(rs.getString("nome"));
				ps.add(p);
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar listaPermissoesUsuario", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return ps;
		
	}
	
	
	public int permissaoLink(String link) throws Throwable{
		int permissao = 0;
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "SELECT id_permissao FROM  permissao_link WHERE link LIKE ?";
			
			pstm = con.prepareStatement(query);
			
			pstm.setString(1, link);
			
			rs = pstm.executeQuery();
			
			if(rs.next()){
				permissao = rs.getInt("id_permissao");
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar permissaoLink", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return permissao;
		
	}
	
	public List<Usuario> buscarUsuarios(long idCliente) throws Throwable{
		List<Usuario> us = new ArrayList<Usuario>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from usuario where id_cliente=?";
			
			pstm = con.prepareStatement(query);
			
			pstm.setLong(1, idCliente);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				Usuario u = new Usuario();
				u.setBairro(rs.getString("bairro"));
				u.setCelular(rs.getString("celular"));
				u.setCep(rs.getString("cep"));
				u.setCidade(rs.getString("cidade"));
				u.setComplemento(rs.getString("complemento"));
				u.setCpf(rs.getString("cpf"));
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setEmail(rs.getString("email"));
				u.setEndereco(rs.getString("endereco"));
				u.setId(rs.getLong("id"));
				u.setIdCliente(rs.getInt("id_cliente"));
				u.setIdPerfil(rs.getInt("id_perfil"));
				u.setNome(rs.getString("nome"));
				u.setNumero(rs.getLong("numero"));
				u.setSenha(rs.getString("senha"));
				u.setTelefone(rs.getString("telefone"));
				u.setUf(rs.getString("uf"));
				us.add(u);
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar usuario", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return us;
		
	}
	
	public void alterarSenha(long idUsuario, String novaSenha) throws Throwable{
		try{
			conectar();
			
			String query = "update usuario set senha=md5(?) where id=?";
			
			pstm = con.prepareStatement(query);
			
			pstm.setString(1, novaSenha);
			pstm.setLong(2, idUsuario);
			
			pstm.executeUpdate();
			
		}catch (Throwable e) {
			con.rollback();
			logger.error("erro atualizando senha", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			
			desconectar();
		}
	}
	
}
