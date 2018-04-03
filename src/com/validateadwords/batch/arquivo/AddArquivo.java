package com.validateadwords.batch.arquivo;

import java.io.File;
import java.io.FileInputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import javax.mail.AuthenticationFailedException;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.validateadwords.batch.bean.HistoricoBean;
import com.validateadwords.batch.dao.ArquivoDao;
import com.validateadwords.batch.dao.ClienteDao;
import com.validateadwords.batch.dao.EmailEnviarDao;
import com.validateadwords.batch.dao.HistoricoProdutoDao;
import com.validateadwords.batch.dao.ProdutoDao;
import com.validateadwords.batch.dao.UsuarioDao;
import com.validateadwords.batch.entitie.Arquivo;
import com.validateadwords.batch.entitie.Cliente;
import com.validateadwords.batch.entitie.EmailEnviar;
import com.validateadwords.batch.entitie.HistoricoProduto;
import com.validateadwords.batch.entitie.Produto;
import com.validateadwords.batch.entitie.Usuario;
import com.validateadwords.batch.util.Utils;

public class AddArquivo implements Runnable{

	private int prdNovo, prdExcluido, prdAlterado, prdPausado, prdAtivado, prdSubiu, prdBaixou;

	Usuario user = new Usuario("Batch",1); //fazer metodo para recupera o id de magazine luiza
	
	
	protected Logger logger = Logger.getLogger(AddArquivo.class);
	
	private long idArquivoAnt;

	public long getIdArquivoAnt() {
		return idArquivoAnt;
	}
	public void setIdArquivoAnt(long idArquivoAnt) {
		this.idArquivoAnt = idArquivoAnt;
	}
	private Arquivo arquivo;
	private List<Arquivo> arqs;

	public Arquivo getArquivo() {
		return arquivo;
	}
	public void setArquivo(Arquivo arquivo) {
		this.arquivo = arquivo;
	}
	public List<Arquivo> getArqs() {
		return arqs;
	}
	public void setArqs(List<Arquivo> arqs) {
		this.arqs = arqs;
	}
	private File insert;
	
	public File getInsert() {
		return insert;
	}

	public void setInsert(File insert) {
		this.insert = insert;
	}


	private void atualizarArquivos(Usuario u){
		try{
			
			ArquivoDao dao = new ArquivoDao();
			arqs = dao.buscarArquivos(u.getIdCliente());
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
	

	public void carregarProdutos(){
		try{
			ProdutoDao pDao = new ProdutoDao();
				MyXppDriver xppDriver = new MyXppDriver();
				XStream xstream = new XStream(xppDriver);
//					xstream.registerConverter((Converter) new DoubleConverter(Locale.getDefault()));
			//System.out.println("Vai carregar os produtos do arquivo "+arquivo.getNome());
				logger.debug("Vai carregar os produtos do arquivo "+arquivo.getNome());
			//depois deparar em outro metodo
			//carregando cada produto do arquivo e inserindo na tabela produto
//			XStream xstream = new XStream(new StaxDriver());
			xstream.alias("PRODUTO", Produto.class);
			xstream.alias("PRODUTOS", ArrayList.class);
			//Object obj = xstream.fromXML(file.getInputstream());
			FileInputStream fipunts = new FileInputStream(getInsert());
			ArrayList produtos = (ArrayList) xstream.fromXML(fipunts);

			for(int i=0;i < produtos.size();i++){
				Produto p = (Produto) produtos.get(i);
				p.setIdArquivo(arquivo.getId());
				p.setHasChange(false);
				//TESTE
				if(p.getNPARCELA().equals("")){p.setNPARCELA(new String("0"));}
				if(p.getVPARCELA().equals("")){p.setVPARCELA(new String("0.0"));}
				if(p.getPRECO().equals("")){p.setPRECO(new String("0.0"));}
				if(p.getPRECO_DE().equals("")){p.setPRECO_DE(new String("0.0"));}
			}
			pDao.adicionarProdutos(produtos);
		}catch(Throwable e){
			e.printStackTrace();
		}
	}

	public void verificarProdutos(Usuario pu){
		
		//zerando todos comtadores;
		prdExcluido = prdAlterado = prdNovo = prdAtivado = prdPausado = prdSubiu = prdBaixou = 0;
		
		ArrayList<HistoricoProduto> alHp = new ArrayList<HistoricoProduto>();
		HistoricoProdutoDao hpDao = new HistoricoProdutoDao();
		ProdutoDao pDao = new ProdutoDao();
		
		//System.out.println("verificarProdutos");
		logger.debug("verificarProdutos");
		try {
			List<Produto> produtosAnt = pDao.buscarProdutos(getIdArquivoAnt());
			List<Produto> produtos = pDao.buscarProdutos(arquivo.getId());

			logger.debug("Produtos ANT SIZE"+produtosAnt.size());
			logger.debug("Produtos SIZE:"+produtos.size());
			
			Produto pAnt = null;
			for(int i=0;i < produtosAnt.size();i++){
				pAnt = (Produto) produtosAnt.get(i);
				if(!produtos.contains(pAnt)){
					alHp.add(gerarHistorico(null,pAnt, "Excluido",pu));
					prdExcluido = prdExcluido + 1;
				}
				
			}
			
			Produto p = null;
			
			for(int i=0;i < produtos.size();i++){
				p = (Produto) produtos.get(i);
				if(!produtosAnt.contains(p)){
					alHp.add(gerarHistorico(null,p, "Novo",pu));
					prdNovo = prdNovo +1;
				}else{
					int intPosAnt = produtosAnt.indexOf(p);
					pAnt = produtosAnt.get(intPosAnt);
					if(!pAnt.igual(p)){
						if(!pAnt.getPRECO().equals(p.getPRECO())){
							if(Double.parseDouble(pAnt.getPRECO()) > Double.parseDouble(p.getPRECO())){
								prdBaixou = prdBaixou + 1;
							}else{
								prdSubiu = prdSubiu + 1;
							}
						}
						alHp.add(gerarHistorico(pAnt,p, "Alterado",pu));
						prdAlterado = prdAlterado + 1;
					}else if(pAnt.isChangeDisponivel(p)){
						if(p.getDISPONIBILIDADE() == 1){
							alHp.add(gerarHistorico(pAnt,p, "Ativado",pu));
							prdAtivado = prdAtivado + 1;
						}
						else{
							alHp.add(gerarHistorico(pAnt,p, "Pausado",pu));
							prdPausado = prdPausado + 1;
						}
					}
				}
			}
			
			//System.out.println("vai inserir os historicos");
			logger.debug("vai inserir os historicos");
			hpDao.adiCionarHistoricoProdutos(alHp);
			//liberando recursos 
			produtos = null;
			produtosAnt = null;
			p = null;
			pAnt = null;
			//Colocando o garbage para ser executado o quanto antes possível
			System.gc();
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}


	public HistoricoProduto gerarHistorico(Produto prdAnt,Produto prd,String pTipo,Usuario pU){
		try{
			Usuario u = pU;//(Usuario) Utils.buscarSessao("usuario");
			
			HistoricoProduto hp =new HistoricoProduto();
			hp.setCODIGO(prd.getCODIGO());
			hp.setNOME(prd.getNOME());
			hp.setDEPARTAMENTO(prd.getDEPARTAMENTO());
			hp.setDESCRICAO(prd.getDESCRICAO());
			hp.setDISPONIBILIDADE(prd.getDISPONIBILIDADE());
			hp.setEAN(prd.getEAN());
			hp.setEDITORA(prd.getEDITORA());
			hp.setNPARCELA(Integer.parseInt(prd.getNPARCELA()));
			hp.setVPARCELA(Double.parseDouble(prd.getVPARCELA()));
			hp.setPRECO(Double.parseDouble(prd.getPRECO()));
			if(prdAnt == null)
				hp.setPRECO_DE(Double.parseDouble(prd.getPRECO()));
			else
				hp.setPRECO_DE(Double.parseDouble(prdAnt.getPRECO()));
			
			hp.setTipo(pTipo);
			hp.setURL(prd.getURL());
			hp.setUsuario(u.getNome());
			hp.setIdArquivo(arquivo.getId());
//			hpDao.adicionar(hp);
			return hp;
		}catch(Throwable e){
			e.printStackTrace();
			return new HistoricoProduto();
		}
		
	}

	public void enviarEmail(Usuario pU){

		try {
//			Usuario u = pU;//(Usuario) Utils.buscarSessao("usuario");
			EmailEnviarDao eeDao = new EmailEnviarDao();
			//TEMPORARIO -> colocar em uma tela com permissao(criar permissao para recuperar o email admin e usar para enviar o email)
			UsuarioDao uDao = new UsuarioDao();
			Usuario userAdmin = uDao.buscarUsuario("admin");
			EmailEnviar ee = new EmailEnviar();
			ee.setDestinatario(userAdmin.getEmail());
			ee.setTitulo("Historico Consolidado Validate Adwords ML");
			ee.setIdArquivo(arquivo.getId());
			String strMessage = "Hist&oacute;rico Consolidado <br><br> "+prdExcluido+" Excluidos <br>"+prdAlterado+" Alterados <br>" +
					prdAtivado+" Ativados <br>"+prdNovo+" Novos <br>"+prdPausado+" Pausados <br>"+prdSubiu+" Subiram <br>"+prdBaixou+" Baixaram<br>" ;
			strMessage = strMessage + "<br> Acesse o link a baixo e click no menu Hist&oacute;rico e depois em Procurar<br>" +
					"<a href=\"http://177.71.246.197/validateadwords/pages/home/login.jsf\" >http://177.71.246.197/validateadwords/pages/home/login.jsf</a>";
			ee.setMensagem(strMessage);
			ee.setSituacao(1); //1 = tem que enviar
			eeDao.adicionarEnvial(ee);
			
		} catch (AuthenticationFailedException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
	

	
	@Override
	public void run() {
			try{
//				Usuario u = (Usuario) Utils.buscarSessao("usuario");
				ArquivoDao dao = new ArquivoDao();
				setIdArquivoAnt(dao.getLastIdArquivo());
				//setando o fileLocation do arquivo pela configuração do CLIENTE
				Cliente cliente = new ClienteDao().buscarCliente(user.getIdCliente());
				//arquivo do mesmo cliente
				arquivo = new Arquivo();
				arquivo.setIdCliente(user.getIdCliente());
				arquivo.setNome(getInsert().getName());
				arquivo.setCaminhoCompleto(cliente.getDiretorioArquivos()+getInsert().getName());
				dao.adicionar(arquivo);
//				Utils.addMessageSucesso("Arquivo "+getInsert().getName() + " adicionado com sucesso.");
				atualizarArquivos(user);
//				System.out.println("run arquivo:"+arquivo.getId());
				logger.debug("run arquivo:"+arquivo.getId());
				//testando metodo carga de produtos
				carregarProdutos();
				verificarProdutos(user);
				enviarEmail(user);
				
				logger.debug("vai chamar batch.bean.HistoricoBean.updateAdwords");
				//renomear arquivo
				HistoricoBean hb = new HistoricoBean();
				hb.setIdArquivo(arquivo.getId());
				hb.updateAdwords(user);
				
				//renomear arquivo antes de reprocessar			//ver porq esta deletando	
				File nFile = new File(getInsert().getName()+".prd");
				getInsert().renameTo(nFile);
				
				logger.debug("liberando o processamento");
				dao.updateStatus(arquivo.getId(),"Processado");
				
			}catch(Throwable e){
				e.printStackTrace();
			}
		
		
	}


}

class CompactCdataWriter extends CompactWriter {
    public CompactCdataWriter(Writer writer) {
        super(writer);
    }
    @Override
    protected void writeText(QuickWriter writer, String text) {
        if (useCdata(text)) {
            writer.write("<[CDATA[");
            writer.write(text);
            writer.write("]]>");
        } else {
            super.writeText(writer, text);
        }
    }
    private boolean useCdata(String text) {
        if (text.indexOf("]]>") < 0) {
            for (int i = 0; i < text.length(); i++) {
                switch (text.charAt(i)) {
                case '<':
                case '>':
                case '&':
                case '"':
                case '\'':
                case '\r':
                    return true;
                }
            }
        }
        return false;
    }
}


class MyXppDriver extends DomDriver{
	
	
	public HierarchicalStreamWriter createWriter(Writer out){
	   return new CompactCdataWriter(out);
	}
	
}
