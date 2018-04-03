package com.validateadwords.batch.entitie;

import java.io.Serializable;
import java.util.Date;

public class Notificacao implements Serializable{

	private static final long serialVersionUID = 1L;

	
	private long id;
	
	private String titulo;
	
	private String conteudo;
	
	private Date dataCadastro;
	
	/**
	 * 1 - nao visualizado, 0 - visualizado
	 */
	private int idStatus;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getConteudo() {
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public int getIdStatus() {
		return idStatus;
	}

	public void setIdStatus(int idStatus) {
		this.idStatus = idStatus;
	}
	
	
}
