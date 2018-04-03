package com.validateadwords.batch.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.validateadwords.batch.entitie.EmailEnviar;
import com.validateadwords.batch.exceptions.DaoException;

public class EmailEnviarDao extends BaseDao{

	
	public void excluir(long idEmail) throws Throwable{
		try{
			conectar();
			
			String q1 = "delete from email_enviar where id = ?" ;
			
			pstm = con.prepareStatement(q1);
			pstm.setLong(1, idEmail);
			
			pstm.executeUpdate();
			
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado excluir", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
	}
	
	public EmailEnviar buscarEmailEnviar(long idArquivo,int situacao) throws Throwable{
		EmailEnviar u = null;
		try{
			conectar();
			
			String query = "select * from email_enviar where id_arquivo="+idArquivo+" and situacao="+situacao;
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new EmailEnviar();
				u.setId(rs.getLong("id"));
//				u.setIdCliente(rs.getInt("id_cliente"));
				u.setDestinatario(rs.getString("destinatario"));
				u.setIdArquivo(rs.getLong("id_arquivo"));
				u.setMensagem(rs.getString("mensagem"));
				u.setTitulo(rs.getString("titulo"));
				u.setSituacao(rs.getInt("situacao"));
				u.setDataCadastro(rs.getDate("data_cadastro"));
				
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar email_enviar", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return u;
		
	}
	
	
	
	public EmailEnviar buscarEmailEnviar(String email) throws Throwable{
		EmailEnviar u = null;
		try{
			conectar();
			
			String query = "select * from email_enviar where destinatario='"+email+"'";
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new EmailEnviar();
				u.setId(rs.getLong("id"));
//				u.setIdCliente(rs.getInt("id_cliente"));
				u.setDestinatario(rs.getString("destinatario"));
				u.setIdArquivo(rs.getLong("id_arquivo"));
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar email_enviar", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return u;
		
	}
	
	
	
		public void alterar(EmailEnviar u) throws Throwable{
		try{
			conectar();
			
			String q1 = "update email_enviar set destinatario=?,situacao=? where id = ?";
			
			logger.debug(ReflectionToStringBuilder.toString(u, ToStringStyle.MULTI_LINE_STYLE));
			
			pstm = con.prepareStatement(q1);
			
			pstm.setString(1, u.getDestinatario());
			pstm.setInt(2,u.getSituacao());
			pstm.setLong(3, u.getId());
			
			
			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("Updating email_enviar failed, no rows affected.");
	        }
				
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado atualizar email_enviar", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		
	}

		public void adicionarEnvial(EmailEnviar u) throws Throwable{
		try{
			conectar();
			
			String q1 = "insert into email_enviar(id,destinatario,titulo,mensagem,situacao,data_cadastro,data_envio,id_arquivo) " +
					" values(0,?,?,?,?,?,?,?)" ;
			
			logger.debug(ReflectionToStringBuilder.toString(u, ToStringStyle.MULTI_LINE_STYLE));
			
			pstm = con.prepareStatement(q1, Statement.RETURN_GENERATED_KEYS);
			
			pstm.setString(1, u.getDestinatario());
			pstm.setString(2, u.getTitulo());
			pstm.setString(3, u.getMensagem());
			pstm.setInt(4, u.getSituacao());
			pstm.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			pstm.setTimestamp(6, null);
			pstm.setLong(7, u.getIdArquivo());
			
			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("Creating email_enviar failed, no rows affected.");
	        }
			rs = pstm.getGeneratedKeys();
	        if (rs.next()) {
	            u.setId(rs.getLong(1));
	        } else {
	            throw new SQLException("Creating email_enviar failed, no generated key obtained.");
	        }
	        
	        
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado adicionar email_enviar", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		
	

		
	}

	
	public boolean existeEmailEnviar(String email) throws Throwable{
		boolean retorno = false;
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select count(id) from email_enviar where destinatario='"+email+"'";
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				int total = rs.getInt(1);
				if(total>0){
					retorno = true;
				}
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado existeEmailEnviar", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return retorno;
		
	}
	
	public EmailEnviar buscarEmailEnviar(long id) throws Throwable{
		EmailEnviar u = null;
		try{
			conectar();
			
			String query = "select * from email_enviar where id="+id;
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new EmailEnviar();
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setId(rs.getLong("id"));
//				u.setIdCliente(rs.getInt("id_cliente"));
				u.setDestinatario(rs.getString("destinatario"));
				u.setIdArquivo(rs.getLong("id_arquivo"));
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar email_enviar", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return u;
		
	}
	
	public Object[] buscarStringsEmailEnviars(long idCliente) throws Throwable{
//		String[] str  = new String[] {};

		ArrayList<String> alStr = new ArrayList<String>();
		
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from email_enviar where id_cliente=?";
			
			pstm = con.prepareStatement(query);
			
			pstm.setLong(1, idCliente);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				alStr.add(rs.getString("destinatario"));
				
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar email_enviar", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return alStr.toArray();
	
		
	}
	
	public List<EmailEnviar> buscarEmailEnviars(long idArquivo,long idSituacao) throws Throwable{
		List<EmailEnviar> us = new ArrayList<EmailEnviar>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from email_enviar where situacao=? and id_arquivo="+idArquivo;
			
			pstm = con.prepareStatement(query);
			
			pstm.setLong(1, idSituacao);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				EmailEnviar u = new EmailEnviar();
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setId(rs.getLong("id"));
//				u.setIdCliente(rs.getInt("id_cliente"));
				u.setMensagem(rs.getString("mensagem"));
				u.setTitulo(rs.getString("titulo"));
				u.setDestinatario(rs.getString("destinatario"));
				u.setSituacao(rs.getInt("situacao"));
				u.setIdArquivo(rs.getLong("id_arquivo"));
				us.add(u);
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar email_enviar", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return us;
	}
	
}
