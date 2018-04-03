package com.validateadwords.batch.entitie;

import java.io.Serializable;
import java.util.Date;

public class Usuario implements Serializable{
	
	private static final long serialVersionUID = 8023901815942617149L;

	private long id;
	
	private String nome;
	
	private String email;
	
	private String senha;
	
	private String cpf;
	
	private String cep;
	
	private String endereco;
	
	private long numero;
	
	private String complemento;
	
	private String bairro;
	
	private String uf;
	
	private String cidade;
	
	private String telefone;
	
	private String celular;
	
	private Date dataCadastro;
	
	private int idPerfil;
	
	private int idCliente;
	
	public Usuario(){
		
	}
	
	public Usuario(String nome,int idCliente){
		setNome(nome);
		setIdCliente(idCliente);
	}
	
	//campos de controle
	private String senhaConfirm;
	private String horaAcesso;
	private String emailOld;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public long getNumero() {
		return numero;
	}

	public void setNumero(long numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public int getIdPerfil() {
		return idPerfil;
	}

	public void setIdPerfil(int idPerfil) {
		this.idPerfil = idPerfil;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public String getHoraAcesso() {
		return horaAcesso;
	}

	public void setHoraAcesso(String horaAcesso) {
		this.horaAcesso = horaAcesso;
	}

	public String getSenhaConfirm() {
		return senhaConfirm;
	}

	public void setSenhaConfirm(String senhaConfirm) {
		this.senhaConfirm = senhaConfirm;
	}

	public String getEmailOld() {
		return emailOld;
	}

	public void setEmailOld(String emailOld) {
		this.emailOld = emailOld;
	}

}
