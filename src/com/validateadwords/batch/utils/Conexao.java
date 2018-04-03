package com.validateadwords.batch.utils;
 
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexao
{
	
	/**
	 * Usado para fechar a conexao com o banco de dados
	 * 
	 * @param pConexao conexao a ser finalizada
	 */
	public static void dropConexao (Connection pConexao)
	{
		PrintWriter      tLog = null;
		try
		{
			tLog = DriverManager.getLogWriter ();
			
			DriverManager.println ("|-------> Fechando a conex???o.....");
				
			if (pConexao != null)	
				pConexao.close();
		}
		catch (SQLException tExcept)
		{
			mostraErro ("Exce??????o SQL Capturada", tExcept, false);
		}
		finally 
		{
			if (tLog != null)
				tLog.close();
		}
	}
	public static void AddLog(String str)
	{
		DriverManager.println(str);
	}
	
	/**
	 * metodo utilizado para fazer a conexao com o banco de dados
	 * 
	 * @param pUser
	 * @param pSenha
	 * @return  conexao com o banco de dados
	 * @throws SQLException
	 */
	public static Connection getConexao (String pUser, String pSenha) 
	throws SQLException
	{
		Connection      tConexao = null;
		PrintWriter		tLog     = null;
		String			tNomeLog = null;
		String			tDriver  = null;
		String			tURL     = null;
		FileInputStream tArqConf = null;
		Properties      tProps   = null;
		
		try
		{

//			tArqConf = new FileInputStream("c:/temp/jdbc_conf.txt");
//			tProps = new Properties ();
//			tProps.load (tArqConf);
//			
//			tDriver = tProps.getProperty ("driver");
//			tURL = tProps.getProperty ("url");
//			tNomeLog = tProps.getProperty ("log");

			tDriver 	= "com.mysql.jdbc.Driver";
			tURL 		= "jdbc:mysql://localhost:3306/validateadwords";
			tNomeLog 	= "/home/ubuntu/db_avalidate.log";
			
			if (tDriver == null || tDriver.equals ("") || 
			    tURL == null    || tURL.equals (""))
				throw new Exception("Arquivo de configura??????o inv???lido");
			
			if (tNomeLog != null && ! tNomeLog.equals (""))
			{
				tLog = new PrintWriter (new FileWriter (tNomeLog));
				DriverManager.setLogWriter (tLog);
				DriverManager.println("|-------> Iniciando log.....");
			}
			
			DriverManager.println ("|-------> Registrando o driver.....");
			Class.forName(tDriver);
	
			DriverManager.println ("|-------> Realizando a conex???o.....");
			tConexao = DriverManager.getConnection(tURL, pUser, pSenha);
		
			DriverManager.println ("|-------> Conex???o estabelecida.....");
		}
		catch (ClassNotFoundException tExcept)
		{
			mostraErro("Driver n???o localizado", tExcept, false);
		}
		catch (IOException tExcept)
		{
			mostraErro ("Erro na abertura de arquivo", tExcept, false);
		}
		catch (SQLException tExcept)
		{
			mostraErro ("Exce??????o SQL Capturada", tExcept, false);
			throw tExcept;
		}
		catch (Exception tExcept)  // OBS. Sempre deve ser o ???ltimo catch
		{
			mostraErro ("Erro na configura??????o", tExcept, false);
		}
		
		return tConexao;
	}
	
	
	/**
	 * metodo utilizado para gerar log de erros com o banco de dados
	 * 
	 * @param pErro
	 * @param pExcept
	 * @param pTrace
	 */
	public static void mostraErro(String pErro, Exception pExcept, 
	                              boolean pTrace)
	{
		String           tMessage;
		int              tErrorCode;
		String           tSQLState;
		
		if (pErro != null & ! pErro.equals (""))
		{
			DriverManager.println ("\n|-ERRO--ERRO--ERRO--ERRO--ERRO-|");
			DriverManager.println ("|-ERRO--> " + pErro);
//			System.out.println ("\n|-ERRO--ERRO--ERRO--ERRO--ERRO-|");
//			System.out.println ("|-ERRO--> " + pErro);
		}
		
		while (pExcept != null)
		{
			DriverManager.println ("|-ERRO--> Exce??????o capturada");
			DriverManager.println ("|-ERRO--> " + pExcept.getMessage());
			DriverManager.println ("|-ERRO--> Exce??????o    : " + 
			                       pExcept.getClass().getName());
//			System.out.println ("|-ERRO--> Exce??????o capturada");
//			System.out.println ("|-ERRO--> " + pExcept.getMessage());
//			System.out.println ("|-ERRO--> Exce??????o    : " + 
//			                    pExcept.getClass().getName());
		
			if (pExcept instanceof SQLException)
			{
				tErrorCode = ((SQLException)pExcept).getErrorCode();
				tSQLState = ((SQLException)pExcept).getSQLState();
		
				DriverManager.println ("|-ERRO--> Error Code : " + 
				                       tErrorCode);
				DriverManager.println ("|-ERRO--> SQL State  : " + 
				                       tSQLState);
//				System.out.println ("|-ERRO--> Error Code : " + 
//				                    tErrorCode);
//				System.out.println ("|-ERRO--> SQL State  : " +  
//				                    tSQLState);
			}
					
			if (pTrace)
			{
				pExcept.printStackTrace (DriverManager.getLogWriter());
				pExcept.printStackTrace ();
			}
	
			if (pExcept instanceof SQLException)
				pExcept = ((SQLException)pExcept).getNextException();
			else
				pExcept = null;
		}
	}
}
