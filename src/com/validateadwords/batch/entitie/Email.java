package com.validateadwords.batch.entitie;

import java.io.Serializable;
import java.util.Date;

public class Email implements Serializable{
	
	private static final long serialVersionUID = 80942617149L;

	private long id;
	
	private String destinatario;
	
	private Date dataCadastro;
	
	
	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	private int idCliente;
	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

}
