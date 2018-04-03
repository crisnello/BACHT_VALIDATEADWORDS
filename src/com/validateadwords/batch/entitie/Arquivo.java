package com.validateadwords.batch.entitie;

import java.io.Serializable;
import java.util.Date;

public class Arquivo implements Serializable {

	private static final long serialVersionUID = 7735902553533001907L;

	private long id;
	
	private String nome;

	private String caminhoCompleto;
	
	private Date dataCadastro;

	private int idCliente;

	private int email;
	
	private String Status;
	
	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public int getEmail() {
		return email;
	}

	public void setEmail(int email) {
		this.email = email;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCaminhoCompleto() {
		return caminhoCompleto;
	}

	public void setCaminhoCompleto(String caminhoCompleto) {
		this.caminhoCompleto = caminhoCompleto;
	}


	
	
}
