package com.validateadwords.batch.entitie;

import java.io.Serializable;
import java.util.Date;

public class Cliente implements Serializable{

	private static final long serialVersionUID = -6495501848870675920L;

	private long id;
	
	private String nome;
	
	private String diretorioArquivos;
	
	private Date dataCadastro;
	
	public String getDiretorioArquivos() {
		return diretorioArquivos;
	}

	public void setDiretorioArquivos(String diretorioArquivos) {
		this.diretorioArquivos = diretorioArquivos;
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

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
}
