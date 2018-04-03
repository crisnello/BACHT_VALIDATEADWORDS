package com.validateadwords.batch.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.validateadwords.batch.entitie.Notificacao;
import com.validateadwords.batch.exceptions.DaoException;



public class NotificacaoDao extends BaseDao{
	public int totalNotificacoes(long idUsuario) throws Throwable{
		int total = 0;
		try {
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select count(n.id) as total from notificacoes as n " +
						   "inner join notificacoes_usuario as nu where nu.id_notificacao=n.id and id_status=1 and id_usuario=?"; 
			
			pstm = con.prepareStatement(query);
			
			pstm.setLong(1, idUsuario);
			
			rs = pstm.executeQuery();
			
			if(rs.next()){
				total = rs.getInt("total");
			}
			
		} catch (Throwable e) {
			logger.error("erro buscando totalNotificacoes", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		return total;
	}

	
	public void enviarEmMassa(Notificacao n) throws Throwable{
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select id from usuario";
			
			rs = con.createStatement().executeQuery(query);
			
			while(rs.next()){
				long idUsuario = rs.getLong("id");
				
				String q = "insert into notificacoes (titulo,conteudo,data_cadastro,id_status) values(?,?,?,1)";
				pstm = con.prepareStatement(q,Statement.RETURN_GENERATED_KEYS);
				pstm.setString(1,n.getTitulo());
				pstm.setString(2,n.getConteudo());
				pstm.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
				
				int affectedRows = pstm.executeUpdate();
				if (affectedRows == 0) {
		            throw new SQLException("Creating enviarEmMassa failed, no rows affected.");
		        }
				
				long id = 0;
				rs = pstm.getGeneratedKeys();
		        if (rs.next()) {
		        	id = rs.getLong(1);
		        } else {
		            throw new SQLException("Creating enviarEmMassa failed, no generated key obtained.");
		        }
				
		        q = "insert into notificacoes_usuario (id_usuario,id_notificacao) values(?,?)";
		        pstm = con.prepareStatement(q);
		        pstm.setLong(1, idUsuario);
		        pstm.setLong(2, id);
		        
		        pstm.executeUpdate();
				
			}
		}catch(Throwable t){
			logger.error(t, t);
		}finally{
			desconectar();
		}
	}
	
	public int buscarTotalNotificacoes(long idUsuario) throws Throwable{
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			StringBuilder query = new StringBuilder();
			query.append("select count(nn.id) as total from notificacoes nn inner join notificacoes_usuario nnu on nn.id=nnu.id_notificacao and nnu.id_usuario=");
			query.append(idUsuario);
			
			rs = con.createStatement().executeQuery(query.toString());
			
			if (rs.next()) {
				return rs.getInt("total");
			}
			
		}catch(Throwable t){
			logger.error(t, t);
		}finally{
			desconectar();
		}
		
		return 0;
	}
	
	
	public List<Notificacao> buscarNotificacoes(long idUsuario, int first, int pageSize) throws Throwable{
		List<Notificacao> ns = new ArrayList<Notificacao>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			StringBuilder query = new StringBuilder();
			query.append("select id,titulo,data_cadastro,id_status from notificacoes n inner join notificacoes_usuario nu on n.id=nu.id_notificacao and nu.id_usuario=");
			query.append(idUsuario);
			query.append(" ORDER BY data_cadastro DESC");
			query.append(" LIMIT ");
			query.append(first);
			query.append(",");
			query.append(pageSize);
			
			rs = con.createStatement().executeQuery(query.toString());
			
			while (rs.next()) {
				Notificacao n = new Notificacao();
				n.setTitulo(rs.getString("titulo"));
				n.setIdStatus(rs.getInt("id_status"));
				n.setId(rs.getLong("id"));
				n.setDataCadastro(rs.getTimestamp("data_cadastro"));
				ns.add(n);
			}
			
			
			
		}catch(Throwable t){
			logger.error(t, t);
		}finally{
			desconectar();
		}
		
		return ns;
	}
	
	public Notificacao buscarNotificacao(long idUsuario, long idNotificacao) throws Throwable{
		Notificacao ns = null;
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			StringBuilder query = new StringBuilder();
			query.append("select * from notificacoes n inner join notificacoes_usuario nu on n.id=nu.id_notificacao and nu.id_usuario=");
			query.append(idUsuario);
			query.append(" and n.id=");
			query.append(idNotificacao);
			
			logger.debug(query.toString());
			rs = con.createStatement().executeQuery(query.toString());
			
			if (rs.next()) {
				ns = new Notificacao();
				ns.setConteudo(rs.getString("conteudo"));
				ns.setTitulo(rs.getString("titulo"));
				ns.setIdStatus(rs.getInt("id_status"));
				ns.setId(rs.getLong("id"));
				ns.setDataCadastro(rs.getTimestamp("data_cadastro"));
				
				String q = "update notificacoes set id_status = 0 where id = "+ns.getId();
				con.createStatement().executeUpdate(q);
				
			}
			
		}catch(Throwable t){
			
		}finally{
			desconectar();
		}
		return ns;
	}
	
}

