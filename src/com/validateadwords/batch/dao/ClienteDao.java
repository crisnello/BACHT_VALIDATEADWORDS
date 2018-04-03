package com.validateadwords.batch.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.validateadwords.batch.entitie.Cliente;
import com.validateadwords.batch.entitie.Permissao;
import com.validateadwords.batch.exceptions.DaoException;

public class ClienteDao extends BaseDao{

	public List<Cliente> buscarClientes() throws Throwable{
		List<Cliente> cs = new ArrayList<Cliente>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from cliente";
			
			rs = con.createStatement().executeQuery(query);
			
			while(rs.next()){
				Cliente c = new Cliente();
				c.setId(rs.getLong("id"));
				c.setNome(rs.getString("nome"));
				c.setDataCadastro(rs.getDate("data_cadastro"));
				c.setDiretorioArquivos(rs.getString("diretorio_arquivos"));
				cs.add(c);
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscarClientes", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return cs;
		
	}
	
	
	public Cliente buscarCliente(long id) throws Throwable{
		Cliente c = null;
		try{
			conectar();
			
			String query = "select * from cliente where id="+id;
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				c = new Cliente();
				c.setId(rs.getLong("id"));
				c.setNome(rs.getString("nome"));
				c.setDataCadastro(rs.getDate("data_cadastro"));
				c.setDiretorioArquivos(rs.getString("diretorio_arquivos"));
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscarCliente", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return c;
		
	}
	
	
	public void adicionar(Cliente c, List<Permissao> permissoes) throws Throwable{
		try{
			conectar();
			
			String query = "insert into cliente (nome,data_cadastro,diretorio_arquivos) values (?,?,?)";
			
			pstm = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			
			pstm.setString(1, c.getNome());
			pstm.setDate(2, new Date(System.currentTimeMillis()));
			pstm.setString(3, c.getDiretorioArquivos());
			
			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("Creating Cliente failed, no rows affected.");
	        }
			rs = pstm.getGeneratedKeys();
	        if (rs.next()) {
	            c.setId(rs.getLong(1));
	        } else {
	            throw new SQLException("Creating Cliente failed, no generated key obtained.");
	        }
	        
	        for (Iterator iterator = permissoes.iterator(); iterator.hasNext();) {
				Permissao p = (Permissao) iterator.next();
				
				query = "insert into cliente_permissao (id_cliente,id_permissao) values (?,?)";
				
				pstm = con.prepareStatement(query);
				pstm.setLong(1, c.getId());
				pstm.setInt(2, p.getId());
				
				pstm.executeUpdate();
			}
	        
	        
	        
		}catch (Throwable e) {
			logger.error("Erro processado adicionar cliente", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
	}
	
	
	public void atualizar(Cliente c, List<Permissao> permissoes) throws Throwable{
		try{
			conectar();
			
			String query = "update cliente set nome=?,diretorio_arquivos=? where id=?";
			
			pstm = con.prepareStatement(query);
			
			pstm.setString(1, c.getNome());
			pstm.setString(2, c.getDiretorioArquivos());
			pstm.setLong(3, c.getId());
			
			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("atualizar Cliente failed, no rows affected.");
	        }
	        
			query = "delete from cliente_permissao where id_cliente="+c.getId();
			con.createStatement().executeUpdate(query);
			
	        for (Iterator iterator = permissoes.iterator(); iterator.hasNext();) {
				Permissao p = (Permissao) iterator.next();
				
				query = "insert into cliente_permissao (id_cliente,id_permissao) values (?,?)";
				
				pstm = con.prepareStatement(query);
				pstm.setLong(1, c.getId());
				pstm.setInt(2, p.getId());
				
				pstm.executeUpdate();
			}
	        
	        
	        
		}catch (Throwable e) {
			logger.error("Erro processado adicionar cliente", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
	}
	
	
	public List<Permissao> listaPermissoesCliente(long idCliente) throws Throwable{
		List<Permissao> ps = new ArrayList<Permissao>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select p.id,p.nome from permissao p inner join cliente_permissao cp on cp.id_permissao=p.id where cp.id_cliente="+idCliente;
			
			pstm = con.prepareStatement(query);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				Permissao p = new Permissao();
				p.setId(rs.getInt("id"));
				p.setNome(rs.getString("nome"));
				ps.add(p);
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar listaPermissoesCliente", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return ps;
		
	}
	
}
