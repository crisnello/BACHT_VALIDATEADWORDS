package com.validateadwords.batch.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.validateadwords.batch.utils.Conexao;

public class BaseDao {
	
	protected Connection con;
	
	protected PreparedStatement pstm;
	
	protected ResultSet rs;
	
	protected ResultSet rs2;
	
	protected Logger logger = Logger.getLogger(BaseDao.class);
	
	/**
	 * criar um data source do MySql no glassfish com as configuracoes abaixo
	 * 
	 * portNumber = 3306
	 * databaseName = rastreamento
	 * URL = jdbc:mysql://localhost:3306/rastreamento 
	 * serverName = localhost
	 * user = root
	 * password = root
	 * 
	 * @throws Exception
	 */
	
	protected void conectar() throws Exception{
		try {
            if(con == null || con.isClosed())
            {
            	con = Conexao.getConexao("root", "root@123");
        		con.setAutoCommit(false); 
            }
               
            }catch (SQLException e) {
                String respBd = e.getMessage();
                if(respBd.indexOf("Communications link failure") >= 0 ||
                    respBd.indexOf("No operations allowed after connection closed") >= 0){
                	con = null;
                	//throw new Exception(e.getMessage());
                }
                else
                    throw e;

           }
		
	}
	
//	protected void conectar() throws Exception{
//		Context ctx = new InitialContext();
//		DataSource ds = (DataSource) ctx.lookup("validateadwords");
//		con = ds.getConnection();
//		con.setAutoCommit(false);
//	}
	
	protected void desconectar(){
		
		if(rs!=null){
			try {
				rs.close();
			} catch (Exception e) {
				//logger.debug(e);
			}
		}
		if(rs2!=null){
			try {
				rs2.close();
			} catch (Exception e) {
				//logger.debug(e);
			}
		}
		if(pstm!=null){
			try {
				pstm.close();
			} catch (Exception e) {
				//logger.debug(e);
			}
		}
		
		try {
        	con.commit();
        	con.close();
        	//System.out.println("con = null");
        	con = null;
        } catch (Exception e) {
        	//logger.debug(e);
        }
		
	}

}
