package com.validateadwords.batch.dao;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.validateadwords.batch.entitie.Produto;
import com.validateadwords.batch.exceptions.DaoException;

public class ProdutoDao extends BaseDao {

	public void excluirByArquivo(long idArquivo) throws Throwable{
		try{
			conectar();
			
			String q1 = "delete from produto where id_arquivo = ?" ;
			
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
	
	public void excluir(long idProduto) throws Throwable{
		try{
			conectar();
			
			String q1 = "delete from produto where id = ?" ;
			
			pstm = con.prepareStatement(q1);
			pstm.setLong(1, idProduto);
			
			pstm.executeUpdate();
			
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado excluir", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
	}

	public Produto buscarProduto(String codigo,long idArquivo) throws Throwable{
		Produto u = null;
		try{
			conectar();
			
			String query = "select * from produto where codigo = "+codigo+" and id_arquivo = "+idArquivo;
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new Produto();
				u.setId(rs.getLong("id"));
				u.setCODIGO(String.valueOf(rs.getBigDecimal("codigo")));
				u.setNOME(rs.getString("nome"));
				
				u.setPRECO_DE(String.valueOf(rs.getDouble("preco_de")));
				u.setPRECO(String.valueOf(rs.getDouble("preco")));
				u.setNPARCELA(String.valueOf(rs.getInt("n_parcela")));
				u.setVPARCELA(String.valueOf(rs.getDouble("v_parcela")));
				u.setDESCRICAO(rs.getString("descricao"));
				u.setDEPARTAMENTO(rs.getString("departamento"));
				u.setSUBDEPARTAMENTO(rs.getString("subdepartamento"));
				u.setDISPONIBILIDADE(rs.getInt("disponibilidade"));
				u.setEAN(rs.getString("ean"));
				u.setEDITORA(rs.getString("editora"));
				int hasChange = rs.getInt("has_change");
				if(hasChange == 0)
					u.setHasChange(false);
				else
					u.setHasChange(true);
				u.setIdArquivo(rs.getInt("id_arquivo"));
				u.setURL(rs.getString("url"));
				u.setURL_IMAGEM(rs.getString("url_imagem"));
				
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar Produto", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return u;
		
	}
	
	public Produto buscarProduto(long id) throws Throwable{
		Produto u = null;
		try{
			conectar();
			
			String query = "select * from produto where id="+id;
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new Produto();
				u.setId(rs.getLong("id"));
				u.setCODIGO(rs.getBigDecimal("codigo").toString());
				u.setNOME(rs.getString("nome"));
				u.setPRECO_DE(String.valueOf(rs.getDouble("preco_de")));
				u.setPRECO(String.valueOf(rs.getDouble("preco")));
				u.setNPARCELA(String.valueOf(rs.getInt("n_parcela")));
				u.setVPARCELA(String.valueOf(rs.getDouble("v_parcela")));
				u.setDESCRICAO(rs.getString("descricao"));
				u.setDEPARTAMENTO(rs.getString("departamento"));
				u.setSUBDEPARTAMENTO(rs.getString("subdepartamento"));
				u.setDISPONIBILIDADE(rs.getInt("disponibilidade"));
				u.setEAN(rs.getString("ean"));
				u.setEDITORA(rs.getString("editora"));
				int hasChange = rs.getInt("has_change");
				if(hasChange == 0)
					u.setHasChange(false);
				else
					u.setHasChange(true);
				u.setIdArquivo(rs.getInt("id_arquivo"));
				u.setURL(rs.getString("url"));
				u.setURL_IMAGEM(rs.getString("url_imagem"));
				
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar Produto", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return u;
		
	}
//
//	public void listarDados(VLHInfo vlhInfo) {
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
//		  vlhInfo.setDataList(result);
//		 }
//
	

	public int buscarTotalProduto(long idArquivo) throws Throwable{
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			StringBuilder query = new StringBuilder();
			query.append("select count(id) as total from produto where id_arquivo=");
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
	
	
	
	public List<Produto> buscarProdutos(long idArquivo,int first, int pageSize) throws Throwable{
		ArrayList<Produto> us = new ArrayList<Produto>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			StringBuilder query = new StringBuilder(); 
			query.append("select * from produto where id_arquivo=?");
			query.append(" ORDER BY id DESC");
			query.append(" LIMIT ");
			query.append(first);
			query.append(",");
			query.append(pageSize);
			pstm = con.prepareStatement(query.toString());
			
			pstm.setLong(1, idArquivo);
			rs = pstm.executeQuery();
			
			while(rs.next()){
				Produto u = new Produto();
				u = new Produto();
				u.setId(rs.getLong("id"));
				u.setCODIGO(rs.getBigDecimal("codigo").toString());
				u.setNOME(rs.getString("nome"));
				u.setPRECO_DE(String.valueOf(rs.getDouble("preco_de")));
				u.setPRECO(String.valueOf(rs.getDouble("preco")));
				u.setNPARCELA(String.valueOf(rs.getInt("n_parcela")));
				u.setVPARCELA(String.valueOf(rs.getDouble("v_parcela")));
				u.setDESCRICAO(rs.getString("descricao"));
				u.setDEPARTAMENTO(rs.getString("departamento"));
				u.setSUBDEPARTAMENTO(rs.getString("subdepartamento"));
				u.setDISPONIBILIDADE(rs.getInt("disponibilidade"));
				u.setEAN(rs.getString("ean"));
				u.setEDITORA(rs.getString("editora"));
				int hasChange = rs.getInt("has_change");
				if(hasChange == 0)
					u.setHasChange(false);
				else
					u.setHasChange(true);
				u.setIdArquivo(rs.getInt("id_arquivo"));
				u.setURL(rs.getString("url"));
				u.setURL_IMAGEM(rs.getString("url_imagem"));
				us.add(u);
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar Produto", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return us;
		
	}
	
	public List<Produto> buscarProdutos(long idArquivo) throws Throwable{
		ArrayList<Produto> us = new ArrayList<Produto>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from produto where id_arquivo=?";
			
			pstm = con.prepareStatement(query);
			pstm.setLong(1, idArquivo);
			rs = pstm.executeQuery();
			
			while(rs.next()){
				Produto u = new Produto();
				u = new Produto();
				u.setId(rs.getLong("id"));
				u.setCODIGO(rs.getBigDecimal("codigo").toString());
				u.setNOME(rs.getString("nome"));
				u.setPRECO_DE(String.valueOf(rs.getDouble("preco_de")));
				u.setPRECO(String.valueOf(rs.getDouble("preco")));
				u.setNPARCELA(String.valueOf(rs.getInt("n_parcela")));
				u.setVPARCELA(String.valueOf(rs.getDouble("v_parcela")));
				u.setDESCRICAO(rs.getString("descricao"));
				u.setDEPARTAMENTO(rs.getString("departamento"));
				u.setSUBDEPARTAMENTO(rs.getString("subdepartamento"));
				u.setDISPONIBILIDADE(rs.getInt("disponibilidade"));
				u.setEAN(rs.getString("ean"));
				u.setEDITORA(rs.getString("editora"));
				int hasChange = rs.getInt("has_change");
				if(hasChange == 0)
					u.setHasChange(false);
				else
					u.setHasChange(true);
				u.setIdArquivo(rs.getInt("id_arquivo"));
				u.setURL(rs.getString("url"));
				u.setURL_IMAGEM(rs.getString("url_imagem"));
				us.add(u);
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar Produto", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return us;
		
	}

	public void adicionarProdutos(ArrayList<Produto> ap)throws Throwable{
		try{
			conectar();
			Produto a = null;
			for(int i=0;i<ap.size();i++){
				a = (Produto)ap.get(i);
			
				
				String str = "insert into produto(codigo,nome,preco_de,preco,v_parcela,n_parcela,descricao,departamento,subdepartamento," +
						"disponibilidade,ean,editora,url,url_imagem,has_change,id_arquivo)" + 
				" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				//logger.debug(ReflectionToStringBuilder.toString(a, ToStringStyle.MULTI_LINE_STYLE));
				
				pstm = con.prepareStatement(str, Statement.RETURN_GENERATED_KEYS);
				
				pstm.setBigDecimal(1, BigDecimal.valueOf(Long.parseLong(a.getCODIGO())));
				pstm.setString(2, a.getNOME());
				
//				System.out.println("-------------\n"+a.toString()+"\n-----------");
				
				if(a.getPRECO_DE().equals("")){a.setPRECO_DE("0.00");}
				if(a.getPRECO().equals("")){a.setPRECO("0.00");}
				if(a.getVPARCELA().equals("")){a.setVPARCELA("0.00");}
				
//				System.out.println("PRECO_DE:"+a.getPRECO_DE());
				
				String strPreco = a.getPRECO();
				strPreco = strPreco.replaceAll("\\.", "").replace(',', '.');
				String strPrecoDe = a.getPRECO_DE().replaceAll("\\.", "").replace(',', '.');
				String strVParcela = a.getVPARCELA().replaceAll("\\.", "").replace(',','.');
				
//				System.out.println("strPreco:"+strPreco);
//				System.out.println("strPrecoDe:"+strPrecoDe);
//				System.out.println("strVParcela:"+strVParcela);
				
				pstm.setDouble(3, Double.parseDouble(strPrecoDe));
				pstm.setDouble(4, Double.parseDouble(strPreco));
				pstm.setDouble(5, Double.parseDouble(strVParcela));
				
				pstm.setInt(6, Integer.parseInt(a.getNPARCELA()));
				pstm.setString(7, a.getDESCRICAO());
				pstm.setString(8, a.getDEPARTAMENTO());
				pstm.setString(9, a.getSUBDEPARTAMENTO());
				pstm.setInt(10, a.getDISPONIBILIDADE());
				pstm.setString(11, a.getEAN());
				pstm.setString(12, a.getEDITORA());
				pstm.setString(13, a.getURL());
				pstm.setString(14, a.getURL_IMAGEM());
				if(a.isHasChange())
					pstm.setInt(15, 1);
				else
					pstm.setInt(15, 0);
	
				pstm.setLong(16, a.getIdArquivo());
				
				int affectedRows = pstm.executeUpdate();
				if (affectedRows == 0) {
		            throw new SQLException("Creating Produto failed, no rows affected.");
		        }
				
				rs = pstm.getGeneratedKeys();
		        if (rs.next()) {
		            a.setId(rs.getLong(1));
		        } else {
		            throw new SQLException("Creating produto failed, no generated key obtained.");
		        }
			}			
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado adicionar Produto", e);
			e.printStackTrace();
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
	}
	
	public void adicionar(Produto a) throws Throwable{
	try{
		conectar();
		
		String str = "insert into produto(codigo,nome,preco_de,preco,v_parcela,n_parcela,descricao,departamento,subdepartamento," +
				"disponibilidade,ean,editora,url,url_imagem,has_change,id_arquivo)" + 
		" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		//logger.debug(ReflectionToStringBuilder.toString(a, ToStringStyle.MULTI_LINE_STYLE));
		
		pstm = con.prepareStatement(str, Statement.RETURN_GENERATED_KEYS);
		
		pstm.setBigDecimal(1, BigDecimal.valueOf(Long.parseLong(a.getCODIGO())));
		pstm.setString(2, a.getNOME());
		
		if(a.getPRECO_DE().equals("")){a.setPRECO_DE("0.00");}
		if(a.getPRECO().equals("")){a.setPRECO("0.00");}
		if(a.getVPARCELA().equals("")){a.setVPARCELA("0.00");}
		
		String strPreco = a.getPRECO();
		strPreco = strPreco.replaceAll("\\.", "").replace(',', '.');
		String strPrecoDe = a.getPRECO_DE().replaceAll("\\.", "").replace(',', '.');
		String strVParcela = a.getVPARCELA().replaceAll("\\.", "").replace(',','.');
		
		pstm.setDouble(3, Double.parseDouble(strPrecoDe));
		pstm.setDouble(4, Double.parseDouble(strPreco));
		pstm.setDouble(5, Double.parseDouble(strVParcela));
		
		pstm.setInt(6, Integer.parseInt(a.getNPARCELA()));
		pstm.setString(7, a.getDESCRICAO());
		pstm.setString(8, a.getDEPARTAMENTO());
		pstm.setString(9, a.getSUBDEPARTAMENTO());
		pstm.setInt(10, a.getDISPONIBILIDADE());
		pstm.setString(11, a.getEAN());
		pstm.setString(12, a.getEDITORA());
		pstm.setString(13, a.getURL());
		pstm.setString(14, a.getURL_IMAGEM());
		if(a.isHasChange())
			pstm.setInt(15, 1);
		else
			pstm.setInt(15, 0);

		pstm.setLong(16, a.getIdArquivo());
		
		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
            throw new SQLException("Creating Produto failed, no rows affected.");
        }
		
		rs = pstm.getGeneratedKeys();
        if (rs.next()) {
            a.setId(rs.getLong(1));
        } else {
            throw new SQLException("Creating produto failed, no generated key obtained.");
        }
		
	}catch (Throwable e) {
		con.rollback();
		logger.error("Erro processado adicionar Produto", e);
		e.printStackTrace();
		throw new DaoException(e.getMessage(), e.getCause());
	}finally{
		desconectar();
	}
		
  }

}
