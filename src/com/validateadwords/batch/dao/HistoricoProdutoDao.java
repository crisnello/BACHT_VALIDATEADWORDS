package com.validateadwords.batch.dao;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.validateadwords.batch.entitie.HistoricoProduto;
import com.validateadwords.batch.exceptions.DaoException;

public class HistoricoProdutoDao extends BaseDao {

	
	
	public void excluir(long idHistoricoProduto) throws Throwable{
		try{
			conectar();
			
			String q1 = "delete from historico_produto where id = ?" ;
			
			pstm = con.prepareStatement(q1);
			pstm.setLong(1, idHistoricoProduto);
			
			pstm.executeUpdate();
			
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado excluir", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
	}
	
	public HistoricoProduto buscarHistoricoProduto(long id) throws Throwable{
		HistoricoProduto u = null;
		try{
			conectar();
			
			String query = "select * from historico_produto where id="+id;
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new HistoricoProduto();
				u.setId(rs.getLong("id"));
				u.setData(rs.getDate("data"));
				u.setTipo(rs.getString("tipo"));
				u.setUsuario(rs.getString("usuario"));
				u.setCODIGO(rs.getBigDecimal("codigo").toString());
				u.setNOME(rs.getString("nome"));
				u.setPRECO_DE(rs.getDouble("preco_de"));
				u.setPRECO(rs.getDouble("preco"));
				u.setNPARCELA(rs.getInt("n_parcela"));
				u.setVPARCELA(rs.getDouble("v_parcela"));
				u.setDESCRICAO(rs.getString("descricao"));
				u.setDEPARTAMENTO(rs.getString("departamento"));
				u.setDISPONIBILIDADE(rs.getInt("disponibilidade"));
				u.setEAN(rs.getString("ean"));
				u.setEDITORA(rs.getString("editora"));

				u.setIdArquivo(rs.getInt("id_arquivo"));
				
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar HistoricoProduto", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return u;
		
	}

//	 public void listarDados(VLHInfo vlhInfo) {
//		  
//		  vlhInfo.setTotalNumberOfEntries(entidades.size());
//		  
//		  Integer firstResult = vlhInfo.getPagingPage() * vlhInfo.getPagingNumberPer();
//		  Integer maxResult = vlhInfo.getPagingNumberPer();
//		  
//		  Integer fromIndex = firstResult;
//		  Integer toIndex = firstResult+maxResult;
//		  
//		  if(toIndex > entidades.size()) {
//		   toIndex = entidades.size();
//		  }
//		  
//		  List<Entidade> result = entidades.subList(fromIndex, toIndex);
//		  vlhInfo.setDataList(result);i0-9
//		 }



	public int buscarTotalhistoricosEstado(long idArquivo,String tipo) throws Throwable{
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			StringBuilder query = new StringBuilder();
			query.append("select count(id) as total from historico_produto where tipo=");
			query.append(tipo);
					query.append(" and id_arquivo=");
			query.append(idArquivo);
			
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
	

	
	
	public int buscarTotalhistoricos(long idArquivo) throws Throwable{
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			StringBuilder query = new StringBuilder();
			query.append("select count(id) as total from historico_produto where id_arquivo=");
			query.append(idArquivo);
			
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
	
	public List<HistoricoProduto> buscarHistoricos(java.util.Date de,java.util.Date ate,long idArquivo, int first, int pageSize) throws Throwable{
		List<HistoricoProduto> us = new ArrayList<HistoricoProduto>();
		try{
			conectar();
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);

			String query = "select * from historico_produto where data >= ? and data <= ? and id_arquivo = ? LIMIT ? , ?";
			
			pstm = con.prepareStatement(query);
			
			pstm.setDate(1, new Date(de.getTime()));
			pstm.setDate(2, new Date(ate.getTime()));
			pstm.setLong(3, idArquivo);
			pstm.setInt(4, first);
			pstm.setInt(5, pageSize);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				
				HistoricoProduto u = new HistoricoProduto();
				u = new HistoricoProduto();
				u.setId(rs.getLong("id"));
				u.setData(rs.getDate("data"));
				u.setTipo(rs.getString("tipo"));
				u.setUsuario(rs.getString("usuario"));				
				u.setCODIGO(rs.getBigDecimal("codigo").toString());
				u.setNOME(rs.getString("nome"));
				u.setPRECO_DE(rs.getDouble("preco_de"));
				u.setPRECO(rs.getDouble("preco"));
				u.setNPARCELA(rs.getInt("n_parcela"));
				u.setVPARCELA(rs.getDouble("v_parcela"));
				u.setDESCRICAO(rs.getString("descricao"));
				u.setDEPARTAMENTO(rs.getString("departamento"));
				u.setDISPONIBILIDADE(rs.getInt("disponibilidade"));
				u.setEAN(rs.getString("ean"));
				u.setEDITORA(rs.getString("editora"));
				u.setIdArquivo(rs.getInt("id_arquivo"));
				u.setURL(rs.getString("url"));
				
				us.add(u);
			}
			
			
			
		}catch(Throwable t){
			logger.error(t, t);
		}finally{
			desconectar();
		}
		
		return us;
	}

	
	public List<HistoricoProduto> buscarProdutos(java.util.Date de,java.util.Date ate, long idArquivo,long sP) throws Throwable{
		List<HistoricoProduto> us = new ArrayList<HistoricoProduto>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from historico_produto where data >= ? and data <= ? and id_arquivo = ?";
			
			pstm = con.prepareStatement(query);
			
			pstm.setDate(1, new Date(de.getTime()));
			pstm.setDate(2, new Date(ate.getTime()));
			pstm.setLong(3, idArquivo);
			
			rs = pstm.executeQuery();
			long sai = sP*6;
			while(rs.next()){
				if(sai == 0){
					HistoricoProduto u = new HistoricoProduto();
					u = new HistoricoProduto();
					u.setId(rs.getLong("id"));
					u.setData(rs.getDate("data"));
					u.setTipo(rs.getString("tipo"));
					u.setUsuario(rs.getString("usuario"));				
					u.setCODIGO(rs.getBigDecimal("codigo").toString());
					u.setNOME(rs.getString("nome"));
					u.setPRECO_DE(rs.getDouble("preco_de"));
					u.setPRECO(rs.getDouble("preco"));
					u.setNPARCELA(rs.getInt("n_parcela"));
					u.setVPARCELA(rs.getDouble("v_parcela"));
					u.setDESCRICAO(rs.getString("descricao"));
					u.setDEPARTAMENTO(rs.getString("departamento"));
					
					u.setDISPONIBILIDADE(rs.getInt("disponibilidade"));
					u.setEAN(rs.getString("ean"));
					u.setEDITORA(rs.getString("editora"));
					
					u.setIdArquivo(rs.getInt("id_arquivo"));
					u.setURL(rs.getString("url"));
					
					us.add(u);
				}else{
					sai = sai-1;
				}
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar HistoricoProduto", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return us;
		
		
	}
	
	public List<HistoricoProduto> buscarProdutos(java.util.Date de,java.util.Date ate, long idArquivo) throws Throwable{
		List<HistoricoProduto> us = new ArrayList<HistoricoProduto>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from historico_produto where data >= ? and data <= ? and id_arquivo = ?";
			
			pstm = con.prepareStatement(query);
			
			pstm.setDate(1, new Date(de.getTime()));
			pstm.setDate(2, new Date(ate.getTime()));
			pstm.setLong(3, idArquivo);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				
				HistoricoProduto u = new HistoricoProduto();
				u = new HistoricoProduto();
				u.setId(rs.getLong("id"));
				u.setData(rs.getDate("data"));
				u.setTipo(rs.getString("tipo"));
				u.setUsuario(rs.getString("usuario"));				
				u.setCODIGO(rs.getBigDecimal("codigo").toString());
				u.setNOME(rs.getString("nome"));
				u.setPRECO_DE(rs.getDouble("preco_de"));
				u.setPRECO(rs.getDouble("preco"));
				u.setNPARCELA(rs.getInt("n_parcela"));
				u.setVPARCELA(rs.getDouble("v_parcela"));
				u.setDESCRICAO(rs.getString("descricao"));
				u.setDEPARTAMENTO(rs.getString("departamento"));
				
				u.setDISPONIBILIDADE(rs.getInt("disponibilidade"));
				u.setEAN(rs.getString("ean"));
				u.setEDITORA(rs.getString("editora"));
				
				u.setIdArquivo(rs.getInt("id_arquivo"));
				u.setURL(rs.getString("url"));
				
				us.add(u);
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar HistoricoProduto", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return us;
		
	}

	
	public List<HistoricoProduto> buscarProdutos(java.util.Date de,java.util.Date ate) throws Throwable{
		List<HistoricoProduto> us = new ArrayList<HistoricoProduto>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from historico_produto where data >= ? and data <= ?";
			
			pstm = con.prepareStatement(query);
			
			pstm.setDate(1, new Date(de.getTime()));
			pstm.setDate(2, new Date(ate.getTime()));
			rs = pstm.executeQuery();
			
			while(rs.next()){
				HistoricoProduto u = new HistoricoProduto();
				u = new HistoricoProduto();
				u.setId(rs.getLong("id"));
				u.setData(rs.getDate("data"));
				u.setTipo(rs.getString("tipo"));
				u.setUsuario(rs.getString("usuario"));				
				u.setCODIGO(rs.getBigDecimal("codigo").toString());
				u.setNOME(rs.getString("nome"));
				u.setPRECO_DE(rs.getDouble("preco_de"));
				u.setPRECO(rs.getDouble("preco"));
				u.setNPARCELA(rs.getInt("n_parcela"));
				u.setVPARCELA(rs.getDouble("v_parcela"));
				u.setDESCRICAO(rs.getString("descricao"));
				u.setDEPARTAMENTO(rs.getString("departamento"));
				u.setDISPONIBILIDADE(rs.getInt("disponibilidade"));
				u.setEAN(rs.getString("ean"));
				u.setEDITORA(rs.getString("editora"));
				
				u.setIdArquivo(rs.getInt("id_arquivo"));
				u.setURL(rs.getString("url"));
				
				us.add(u);
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar HistoricoProduto", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return us;
		
	}
	
	
	public List<HistoricoProduto> buscarProdutos(long idArquivo) throws Throwable{
		List<HistoricoProduto> us = new ArrayList<HistoricoProduto>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from historico_produto where id_arquivo=?";
			
			pstm = con.prepareStatement(query);
			pstm.setLong(1, idArquivo);
			rs = pstm.executeQuery();
			
			while(rs.next()){
				HistoricoProduto u = new HistoricoProduto();
				u = new HistoricoProduto();
				u.setId(rs.getLong("id"));
				u.setData(rs.getDate("data"));
				u.setTipo(rs.getString("tipo"));
				u.setUsuario(rs.getString("usuario"));				
				u.setCODIGO(rs.getBigDecimal("codigo").toString());
				u.setNOME(rs.getString("nome"));
				u.setPRECO_DE(rs.getDouble("preco_de"));
				u.setPRECO(rs.getDouble("preco"));
				u.setNPARCELA(rs.getInt("n_parcela"));
				u.setVPARCELA(rs.getDouble("v_parcela"));
				u.setDESCRICAO(rs.getString("descricao"));
				u.setDEPARTAMENTO(rs.getString("departamento"));
				
				u.setDISPONIBILIDADE(rs.getInt("disponibilidade"));
				u.setEAN(rs.getString("ean"));
				u.setEDITORA(rs.getString("editora"));
				
				u.setIdArquivo(rs.getInt("id_arquivo"));
				u.setURL(rs.getString("url"));

				us.add(u);
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar HistoricoProduto", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return us;
		
	}

	
	public void adiCionarHistoricoProdutos(ArrayList<HistoricoProduto> al) throws Throwable{
		try{
			conectar();
			
			for(int i=0;i<al.size();i++){
				
			String str = "insert into historico_produto(codigo,nome,preco_de,preco,v_parcela,n_parcela,descricao,departamento,data," +
					"disponibilidade,ean,editora,url,tipo,usuario,id_arquivo)" + 
			" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
//			logger.debug(ReflectionToStringBuilder.toString(a, ToStringStyle.MULTI_LINE_STYLE));
			
			pstm = con.prepareStatement(str, Statement.RETURN_GENERATED_KEYS);
			HistoricoProduto a = (HistoricoProduto)al.get(i);
			pstm.setBigDecimal(1, new BigDecimal(a.getCODIGO()));
			pstm.setString(2, a.getNOME());
			pstm.setDouble(3, a.getPRECO_DE());
			pstm.setDouble(4, a.getPRECO());
			pstm.setDouble(5, a.getVPARCELA());
			pstm.setInt(6, a.getNPARCELA());
			pstm.setString(7, a.getDESCRICAO());
			pstm.setString(8, a.getDEPARTAMENTO());
			pstm.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
			pstm.setInt(10, a.getDISPONIBILIDADE());
			pstm.setString(11, a.getEAN());
			pstm.setString(12, a.getEDITORA());
			pstm.setString(13, a.getURL());
			pstm.setString(14, a.getTipo());
			pstm.setString(15, a.getUsuario());
			pstm.setLong(16, a.getIdArquivo());
			
			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("Creating  HistoricoProduto failed, no rows affected.");
	        }
			}
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado adicionar HistoricoProduto", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
			
	}
	
	public void adicionar(HistoricoProduto a) throws Throwable{
	try{
		conectar();
		
		String str = "insert into historico_produto(codigo,nome,preco_de,preco,v_parcela,n_parcela,descricao,departamento,data," +
				"disponibilidade,ean,editora,url,tipo,usuario,id_arquivo)" + 
		" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		logger.debug(ReflectionToStringBuilder.toString(a, ToStringStyle.MULTI_LINE_STYLE));
		
		pstm = con.prepareStatement(str, Statement.RETURN_GENERATED_KEYS);
		
		
		pstm.setBigDecimal(1, BigDecimal.valueOf(Long.parseLong(a.getCODIGO())));
		pstm.setString(2, a.getNOME());
		pstm.setDouble(3, a.getPRECO_DE());
		pstm.setDouble(4, a.getPRECO());
		pstm.setDouble(5, a.getVPARCELA());
		pstm.setInt(6, a.getNPARCELA());
		pstm.setString(7, a.getDESCRICAO());
		pstm.setString(8, a.getDEPARTAMENTO());
		pstm.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
		pstm.setInt(10, a.getDISPONIBILIDADE());
		pstm.setString(11, a.getEAN());
		pstm.setString(12, a.getEDITORA());
		pstm.setString(13, a.getURL());
		pstm.setString(14, a.getTipo());
		pstm.setString(15, a.getUsuario());
		pstm.setLong(16, a.getIdArquivo());
		
		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
            throw new SQLException("Creating  HistoricoProduto failed, no rows affected.");
        }
		
	}catch (Throwable e) {
		con.rollback();
		logger.error("Erro processado adicionar HistoricoProduto", e);
		throw new DaoException(e.getMessage(), e.getCause());
	}finally{
		desconectar();
	}
		
  }

}
