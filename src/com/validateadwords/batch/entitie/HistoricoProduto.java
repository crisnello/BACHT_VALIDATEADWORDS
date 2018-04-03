package com.validateadwords.batch.entitie;

import java.io.Serializable;
import java.util.Date;

public class HistoricoProduto implements Serializable {
	

	private static final long serialVersionUID = -4552902886809565641L;
	private long id;
	private Date data;
	private String tipo;
	private String usuario;
	private String CODIGO;
	private String NOME;
	private String DESCRICAO;
	private Double PRECO_DE;
	private Double PRECO;
	private Double VPARCELA;
	private int NPARCELA;
	private String URL;
	private String DEPARTAMENTO;
	private String EDITORA;
	private String EAN;
	private int DISPONIBILIDADE;
	private long idArquivo;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public String getCODIGO() {
		return CODIGO;
	}
	public void setCODIGO(String cODIGO) {
		CODIGO = cODIGO;
	}
	public String getNOME() {
		return NOME;
	}
	public void setNOME(String nOME) {
		NOME = nOME;
	}
	
	
	public String getDESCRICAO() {
		return DESCRICAO;
	}
	public void setDESCRICAO(String dESCRICAO) {
		DESCRICAO = dESCRICAO;
	}
	public Double getPRECO_DE() {
		return PRECO_DE;
	}
	public void setPRECO_DE(Double pRECO_DE) {
		PRECO_DE = pRECO_DE;
		
	}
	public Double getPRECO() {
		return PRECO;
	}
	public void setPRECO(Double pRECO) {
		PRECO = pRECO;
		
	}
	public Double getVPARCELA() {
		return VPARCELA;
	}
	public void setVPARCELA(Double vPARCELA) {
		VPARCELA = vPARCELA;
	}
	public int getNPARCELA() {
		return NPARCELA;
	}
	public void setNPARCELA(int nPARCELA) {
		NPARCELA = nPARCELA;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getDEPARTAMENTO() {
		return DEPARTAMENTO;
	}
	public void setDEPARTAMENTO(String dEPARTAMENTO) {
		DEPARTAMENTO = dEPARTAMENTO;
	}
	public String getEDITORA() {
		return EDITORA;
	}
	public void setEDITORA(String eDITORA) {
		EDITORA = eDITORA;
	}
	public String getEAN() {
		return EAN;
	}
	public void setEAN(String eAN) {
		EAN = eAN;
	}
	public int getDISPONIBILIDADE() {
		return DISPONIBILIDADE;
	}
	public void setDISPONIBILIDADE(int dISPONIBILIDADE) {
		DISPONIBILIDADE = dISPONIBILIDADE;
	}
	
	public long getIdArquivo() {
		return idArquivo;
	}
	public void setIdArquivo(long idArquivo) {
		this.idArquivo = idArquivo;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	

	@Override
	public boolean equals(Object obj) {
		HistoricoProduto hp = (HistoricoProduto)obj;
		if(hp.getId() == this.getId()){
			return true;
		}else
			return false;
	}
	
	
}
