package com.validateadwords.batch.entitie;

import java.io.Serializable;

public class Produto implements Serializable {
	
	private static final long serialVersionUID = 2026672588045619647L;
	private long id;
	private String CODIGO;
	private String NOME;
	private String DESCRICAO;
	private String PRECO_DE;
	private String PRECO;
	private String VPARCELA;
	private String NPARCELA;
	private String URL;
	private String URL_IMAGEM;
	private String DEPARTAMENTO;
	private String SUBDEPARTAMENTO;
	private String EDITORA;
	private String EAN;
	private String SKU;
	private int DISPONIBILIDADE;
	private long idArquivo;
	private boolean hasChange;
	
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
	
	public String getPRECO_DE() {
		return PRECO_DE;
	}
	public void setPRECO_DE(String pRECO_DE) {
		PRECO_DE = pRECO_DE;
	}
	public String getPRECO() {
		return PRECO;
	}
	public void setPRECO(String pRECO) {
		PRECO = pRECO;
	}
	public String getVPARCELA() {
		return VPARCELA;
	}
	public void setVPARCELA(String vPARCELA) {
		VPARCELA = vPARCELA;
	}
	
	public String getNPARCELA() {
		return NPARCELA;
	}
	public void setNPARCELA(String nPARCELA) {
		NPARCELA = nPARCELA;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getURL_IMAGEM() {
		return URL_IMAGEM;
	}
	public void setURL_IMAGEM(String uRL_IMAGEM) {
		URL_IMAGEM = uRL_IMAGEM;
	}
	public String getDEPARTAMENTO() {
		return DEPARTAMENTO;
	}
	public void setDEPARTAMENTO(String dEPARTAMENTO) {
		DEPARTAMENTO = dEPARTAMENTO;
	}
	public String getSUBDEPARTAMENTO() {
		return SUBDEPARTAMENTO;
	}
	public void setSUBDEPARTAMENTO(String sUBDEPARTAMENTO) {
		SUBDEPARTAMENTO = sUBDEPARTAMENTO;
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
	public String getSKU() {
		return SKU;
	}
	public void setSKU(String sKU) {
		SKU = sKU;
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
	public boolean isHasChange() {
		return hasChange;
	}
	public void setHasChange(boolean hasChange) {
		this.hasChange = hasChange;
	}
	@Override
	public String toString() {
		return getCODIGO()+" \n "+getNOME()+" \n "+getPRECO_DE();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		Produto p = (Produto)obj;
		if(p.getCODIGO().equals(this.getCODIGO())){
			return true;
		}else
			return false;
		
//		return super.equals(obj);
	}
	public boolean igual(Object obj) {
		Produto p = (Produto) obj;
		if(!p.NOME.equals(this.NOME) ||
			!p.CODIGO.equals(this.CODIGO) ||
			!p.DEPARTAMENTO.equals(this.DEPARTAMENTO) ||
			!p.DESCRICAO.equals(this.DESCRICAO) ||
			!p.EAN.equals(this.EAN) ||
			!p.EDITORA.equals(this.EDITORA) ||
			!p.NPARCELA.equals(this.NPARCELA )||
			!p.VPARCELA.equals(this.VPARCELA) ||
			!p.PRECO.equals(this.PRECO) ||
			!p.URL.equals(this.URL))
			return false;
		else
			return true;
			
	}
	public boolean isChangeDisponivel(Produto p){
		
		if(this.DISPONIBILIDADE != p.DISPONIBILIDADE)
			return true;
		else
			return false;
		
	}
	
	
}
