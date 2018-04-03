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
import com.validateadwords.batch.entitie.Email;
import com.validateadwords.batch.exceptions.DaoException;

public class EmailDao extends BaseDao{

	
	public void excluir(long idEmail) throws Throwable{
		try{
			conectar();
			
			String q1 = "delete from email where id = ?" ;
			
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
	
	
	public Email buscarEmail(String email) throws Throwable{
		Email u = null;
		try{
			conectar();
			
			String query = "select * from email where email='"+email+"'";
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new Email();
				u.setId(rs.getLong("id"));
				u.setIdCliente(rs.getInt("id_cliente"));
				u.setDestinatario(rs.getString("destinatario"));
				
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar email", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return u;
		
	}
	
		public void alterar(Email u) throws Throwable{
		try{
			conectar();
			
			String q1 = "update email set destinatario=? where id = ?";
			
			logger.debug(ReflectionToStringBuilder.toString(u, ToStringStyle.MULTI_LINE_STYLE));
			
			pstm = con.prepareStatement(q1);
			
			pstm.setString(1, u.getDestinatario());
			pstm.setLong(2, u.getId());
			
			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("Updating email failed, no rows affected.");
	        }
				
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado atualizar email", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		
	}

	
		public void adicionar(Email u) throws Throwable{
		try{
			conectar();
			
			String q1 = "insert into email(destinatario,data_cadastro,id_cliente)" +
					" values(?,?,?)" ;
			
			logger.debug(ReflectionToStringBuilder.toString(u, ToStringStyle.MULTI_LINE_STYLE));
			
			pstm = con.prepareStatement(q1, Statement.RETURN_GENERATED_KEYS);
			
			pstm.setString(1, u.getDestinatario());
			pstm.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			pstm.setInt(3, u.getIdCliente());
			
			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("Creating email failed, no rows affected.");
	        }
			rs = pstm.getGeneratedKeys();
	        if (rs.next()) {
	            u.setId(rs.getLong(1));
	        } else {
	            throw new SQLException("Creating email failed, no generated key obtained.");
	        }
	        
	        
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado adicionar email", e);
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
			
			String query = "select count(id) from email where destinatario='"+email+"'";
			
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
	
	public Email buscarEmail(long id) throws Throwable{
		Email u = null;
		try{
			conectar();
			
			String query = "select * from email where id="+id;
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new Email();
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setId(rs.getLong("id"));
				u.setIdCliente(rs.getInt("id_cliente"));
				u.setDestinatario(rs.getString("destinatario"));
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar email", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return u;
		
	}
	
	public Object[] buscarStringsEmails(long idCliente) throws Throwable{
//		String[] str  = new String[] {};

		ArrayList<String> alStr = new ArrayList<String>();
		
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from email where id_cliente=?";
			
			pstm = con.prepareStatement(query);
			
			pstm.setLong(1, idCliente);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				alStr.add(rs.getString("destinatario"));
				
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar email", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return alStr.toArray();
	
		
	}
	
	public List<Email> buscarEmails(long idCliente) throws Throwable{
		List<Email> us = new ArrayList<Email>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from email where id_cliente=?";
			
			pstm = con.prepareStatement(query);
			
			pstm.setLong(1, idCliente);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				Email u = new Email();
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setId(rs.getLong("id"));
				u.setIdCliente(rs.getInt("id_cliente"));
				u.setDestinatario(rs.getString("destinatario"));
				us.add(u);
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar email", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return us;
	}
	
}
