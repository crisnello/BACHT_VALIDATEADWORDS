package com.validateadwords.batch.dao;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.validateadwords.batch.entitie.Arquivo;
import com.validateadwords.batch.exceptions.DaoException;

public class ArquivoDao extends BaseDao {
	
	public void excluir(long idArquivo) throws Throwable{
		try{
			conectar();
			
			String q1 = "delete from arquivo where id = ?" ;
			
			pstm = con.prepareStatement(q1);
			pstm.setLong(1, idArquivo);
			
			pstm.executeUpdate();

			
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado excluir", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
	}
	
    public long getLastIdArquivo() throws Throwable{
        long resp = -1;
        try{
        	conectar();

        	String query = "select max(id) from arquivo";
            
        	rs = con.createStatement().executeQuery(query);

			if(rs != null && rs.next()){
				resp = rs.getLong(1);
			}
        	
			}catch (Throwable e) {
				logger.error("Erro processado buscar id arquivo", e);
//				throw new DaoException(e.getMessage(), e.getCause());
			}finally{
				desconectar();
			}

        return resp;
    }
	
	public Arquivo buscarArquivo(long id) throws Throwable{
		Arquivo u = null;
		try{
			conectar();
			
			String query = "select * from arquivo where id="+id;
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new Arquivo();
				u.setId(rs.getLong("id"));
				u.setIdCliente(rs.getInt("id_cliente"));
				u.setNome(rs.getString("nome"));
				u.setCaminhoCompleto(rs.getString("caminho_completo"));
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setEmail(rs.getInt("email"));
				u.setStatus(rs.getString("status"));
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar arquivo", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return u;
		
	}
	
	public boolean allProcessado(){
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			String query = "select * from arquivo where status='Novo'";
			pstm = con.prepareStatement(query);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				return false;
			}
			
		}catch (Throwable e) {
			//e.printStackTrace();
			//logger.error("Erro allProcessado arquivo", e);
//			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return true;
		
		
	}

	
	public List<Arquivo> buscarArquivos(long idCliente) throws Throwable{
		List<Arquivo> us = new ArrayList<Arquivo>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from arquivo where id_cliente=? and status='Processado' ORDER BY id DESC";
			
			pstm = con.prepareStatement(query);
			
			pstm.setLong(1, idCliente);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				Arquivo u = new Arquivo();
				u.setId(rs.getLong("id"));
				u.setIdCliente(rs.getInt("id_cliente"));
				u.setNome(rs.getString("nome"));
				u.setCaminhoCompleto(rs.getString("caminho_completo"));
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setEmail(rs.getInt("email"));
				u.setStatus(rs.getString("status"));				
				us.add(u);
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar arquivo", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return us;
		
	}

	public void updateEmail(long idArquivo,int pEmail) throws Throwable{
		try{
			conectar();
			
			String q1 = "update arquivo set email=" +pEmail+
					" where id = ?" ;
			
			pstm = con.prepareStatement(q1);
			pstm.setLong(1, idArquivo);
			
			pstm.executeUpdate();

			
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado update email", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
	}
	
	public void updateStatus(long idArquivo,String pStatus) throws Throwable{
		try{
			conectar();
			
			String q1 = "update arquivo set status='" +pStatus+"'"+
					" where id = ?" ;
			
			pstm = con.prepareStatement(q1);
			pstm.setLong(1, idArquivo);
			
			pstm.executeUpdate();

			
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado update status", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
	}
	
	public void adicionar(Arquivo a) throws Throwable{
	try{
		conectar();
		
		String str = "insert into arquivo(nome,caminho_completo,data_cadastro,id_cliente,email,status)" + 
		" values(?,?,?,?,?,?)";
		
		logger.debug(ReflectionToStringBuilder.toString(a, ToStringStyle.MULTI_LINE_STYLE));
		
		pstm = con.prepareStatement(str, Statement.RETURN_GENERATED_KEYS);
		
		pstm.setString(1, a.getNome());
		pstm.setString(2, a.getCaminhoCompleto());
		pstm.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
		pstm.setInt(4, a.getIdCliente());
		pstm.setInt(5,1);
		pstm.setString(6,"Novo");

		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
            throw new SQLException("Creating arquivo failed, no rows affected.");
        }

		rs = pstm.getGeneratedKeys();
        if (rs.next()) {
            a.setId(rs.getLong(1));
        } else {
            throw new SQLException("Creating arquivo failed, no generated key obtained.");
        }
		
	}catch (Throwable e) {
		con.rollback();
		logger.error("Erro processado adicionar arquivo", e);
		throw new DaoException(e.getMessage(), e.getCause());
	}finally{
		desconectar();
	}
		
  }

}
