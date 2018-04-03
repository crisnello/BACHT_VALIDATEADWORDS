package com.validateadwords.batch.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.mail.AuthenticationFailedException;

import org.apache.log4j.Logger;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.v201109.cm.CampaignStatus;
import com.google.api.adwords.v201209.cm.Ad;
import com.google.api.adwords.v201209.cm.AdGroup;
import com.google.api.adwords.v201209.cm.AdGroupAd;
import com.google.api.adwords.v201209.cm.AdGroupAdOperation;
import com.google.api.adwords.v201209.cm.AdGroupAdPage;
import com.google.api.adwords.v201209.cm.AdGroupAdReturnValue;
import com.google.api.adwords.v201209.cm.AdGroupAdServiceInterface;
import com.google.api.adwords.v201209.cm.AdGroupAdStatus;
import com.google.api.adwords.v201209.cm.AdGroupCriterion;
import com.google.api.adwords.v201209.cm.AdGroupCriterionPage;
import com.google.api.adwords.v201209.cm.AdGroupCriterionServiceInterface;
import com.google.api.adwords.v201209.cm.AdGroupPage;
import com.google.api.adwords.v201209.cm.AdGroupServiceInterface;
import com.google.api.adwords.v201209.cm.AdGroupStatus;
import com.google.api.adwords.v201209.cm.AdParam;
import com.google.api.adwords.v201209.cm.AdParamOperation;
import com.google.api.adwords.v201209.cm.AdParamServiceInterface;
import com.google.api.adwords.v201209.cm.Campaign;
import com.google.api.adwords.v201209.cm.CampaignPage;
import com.google.api.adwords.v201209.cm.CampaignServiceInterface;
import com.google.api.adwords.v201209.cm.Keyword;
import com.google.api.adwords.v201209.cm.Operator;
import com.google.api.adwords.v201209.cm.OrderBy;
import com.google.api.adwords.v201209.cm.Predicate;
import com.google.api.adwords.v201209.cm.PredicateOperator;
import com.google.api.adwords.v201209.cm.Selector;
import com.google.api.adwords.v201209.cm.SortOrder;
import com.google.api.adwords.v201209.cm.TextAd;
import com.google.api.adwords.v201209.mcm.ManagedCustomer;
import com.google.api.adwords.v201209.mcm.ManagedCustomerPage;
import com.google.api.adwords.v201209.mcm.ManagedCustomerServiceInterface;
import com.validateadwords.batch.dao.ArquivoDao;
import com.validateadwords.batch.dao.EmailDao;
import com.validateadwords.batch.dao.EmailEnviarDao;
import com.validateadwords.batch.dao.HistoricoProdutoDao;
import com.validateadwords.batch.dao.UsuarioDao;
import com.validateadwords.batch.entitie.EmailEnviar;
import com.validateadwords.batch.entitie.HistoricoProduto;
import com.validateadwords.batch.entitie.Usuario;
import com.validateadwords.batch.exceptions.BeanException;
import com.validateadwords.batch.util.SendEmail;
import com.validateadwords.batch.util.Utils;

public class HistoricoBean implements Serializable{
	
	private static final String ACTIVE = "ACTIVE";
	
	private static final long serialVersionUID = 6539090559512795004L;
	
	public SimpleDateFormat dataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	protected Logger logger = Logger.getLogger(HistoricoBean.class);
	
	private Date dataMaxima;
	
	private Date dataDe;
	
	private Date dataAte;
	
	private long idArquivo;
	
	private String posicionamentos;

	private long scrollerPage;
	
	private HistoricoProduto historico;
	
	public HistoricoProduto getHistorico() {
		return historico;
	}

	public void setHistorico(HistoricoProduto historico) {
		this.historico = historico;
	}

	public long getScrollerPage() {
		return scrollerPage;
	}

	public void setScrollerPage(long scrollerPage) {
		this.scrollerPage = scrollerPage;
	}

	private boolean vazio = true;
	
	public boolean isVazio() {
		return vazio;
	}

	public void setVazio(boolean vazio) {
		this.vazio = vazio;
	}


	public int getTotalHistoricos() {
		try{
			
//			Usuario u = (Usuario) Utils.buscarSessao("usuario");
			HistoricoProdutoDao dao = new HistoricoProdutoDao();
			totalHistoricos = dao.buscarTotalhistoricos(idArquivo);
			
		}catch(Throwable e){
			logger.debug(e,e);
		}
		return totalHistoricos;
		
	}

	public void setTotalHistoricos(int totalHistoricos) {
		this.totalHistoricos = totalHistoricos;
	}

	private int totalHistoricos = 0;
	
	private List<HistoricoProduto> historicos;

	public List<HistoricoProduto> getHistoricos() {
		return historicos;
	}
	public void setHistoricos(List<HistoricoProduto> historicos) {
		this.historicos = historicos;
	}
	
	ArrayList<HistoricoProduto> hpOk ;
	
	public HistoricoBean() {
		setDataAte(new Date());
		setDataDe(new Date());
		setDataMaxima(new Date());
		
		historicos = new ArrayList<HistoricoProduto>();
	}
	
	public void consultarHistorico(Usuario u){
		historicos = new ArrayList<HistoricoProduto>();
		logger.debug("consultando historico");
		try{
			
			//Usuario u = (Usuario) Utils.buscarSessao("usuario");
			
			Calendar calAte = Calendar.getInstance();
			calAte.setTime(getDataAte());
			calAte.set(Calendar.HOUR_OF_DAY, 23);
			calAte.set(Calendar.MINUTE, 59);
			calAte.set(Calendar.SECOND, 59);
			calAte.set(Calendar.MILLISECOND, 999);
			
			Calendar calDe = Calendar.getInstance();
			calDe.setTime(getDataDe());
			calDe.set(Calendar.HOUR_OF_DAY, 0);
			calDe.set(Calendar.MINUTE, 0);
			calDe.set(Calendar.SECOND, 0);
			calDe.set(Calendar.MILLISECOND, 1);
			
			if(dataAte.before(dataDe)){
				logger.debug("Intervalo de datas inv?lido, 'Data de' maior que 'Data at?.'");
				return;
			}
			
			if(Utils.intervaloDias(dataDe, dataAte) > 7){
				logger.debug("Intervalo de datas n?o deve ser maior que 7 dias.");
				return;
			}
			
//			setVazio(false);
			HistoricoProdutoDao hpDao = new HistoricoProdutoDao();
			if(idArquivo != 0)
				historicos = hpDao.buscarProdutos(dataDe,dataAte,idArquivo);
			else
				historicos = hpDao.buscarProdutos(dataDe,dataAte);
			
			setScrollerPage(0);
			
		}catch(Throwable t){
			logger.error("erro consultando historico", t);
		}
	}

	
	public String updateAdwords(Usuario u){
		String resp = "";
		try{
			
			HistoricoProdutoDao hpDao = new HistoricoProdutoDao();
			historicos = hpDao.buscarProdutos(idArquivo);
			HistoricoProduto hp = null;
			logger.debug("vai iniciar update adwords.");
//			for(int i=0;i<historicos.size();i++){
//				hp = historicos.get(i);
//				//Vou atualizar somente o pre?o no description
//				updateAdword(hp);
//			}
			updateAdword(historicos);
			logger.debug("acabou.");
			
			enviarEmail(u);
			
			resp = "Adwords atualizado com sucesso.";
		}catch(Throwable t){
			t.printStackTrace();
			logger.error("Erro ao atualizar historico no adwords", t);
			
		}
		return resp;
	}


	private void updateAdword(List<HistoricoProduto> hps) throws Throwable{
		try {
		      // Log SOAP XML request and response.
		      AdWordsServiceLogger.log();

		     hpOk = new ArrayList<HistoricoProduto>();
		      
		      //verificar sem passar o caminho do arquivo
		      // Get AdWordsUser from "~/adwords.properties".
		      AdWordsUser user = new AdWordsUser();
		      //System.out.println("user"+user.toString());
		      // Get the ServicedAccountService.
		      ManagedCustomerServiceInterface managedCustomerService =
		          user.getService(AdWordsService.V201209.MANAGED_CUSTOMER_SERVICE);
		      // Create selector.
		      Selector selector = new Selector();
		      selector.setFields(new String[] {"Login", "CustomerId"});
		      // Get results.
		      ManagedCustomerPage page = managedCustomerService.get(selector);
//		      int ret = -1;
		      if (page.getEntries() != null) {
		       HistoricoProduto hp = null;
		        // Create account tree nodes for each customer.
		        for (ManagedCustomer customer : page.getEntries()) {
		        	System.out.println("Client:"+customer.getLogin()+" ID:"+customer.getCustomerId());
		        	updateAdword(hps,String.valueOf(customer.getCustomerId()));
		        }
		      }
//		      else {
//		        System.out.println("No serviced accounts were found.");
//		      }
		    } catch (Exception e) {
		      e.printStackTrace();
		    }

	}

	
//	private void updateAdword(HistoricoProduto hp) throws Throwable{
//		try {
//		      // Log SOAP XML request and response.
//		      AdWordsServiceLogger.log();
//		      
//		      //verificar sem passar o caminho do arquivo
//		      // Get AdWordsUser from "~/adwords.properties".
//		      AdWordsUser user = new AdWordsUser();
//		      //System.out.println("user"+user.toString());
//		      // Get the ServicedAccountService.
//		      ManagedCustomerServiceInterface managedCustomerService =
//		          user.getService(AdWordsService.V201209.MANAGED_CUSTOMER_SERVICE);
//		      // Create selector.
//		      Selector selector = new Selector();
//		      selector.setFields(new String[] {"Login", "CustomerId"});
//		      // Get results.
//		      ManagedCustomerPage page = managedCustomerService.get(selector);
//		      int ret = -1;
//		      if (page.getEntries() != null) {
//		       
//		        // Create account tree nodes for each customer.
//		        for (ManagedCustomer customer : page.getEntries()) {
////		        	System.out.println("Client:"+customer.getLogin()+" ID:"+customer.getCustomerId());
//		        	ret= updateAdword(hp,String.valueOf(customer.getCustomerId()));
//		        	if(ret == 1)
//		        		break;
//		        }
//		      }
////		      else {
////		        System.out.println("No serviced accounts were found.");
////		      }
//		    } catch (Exception e) {
//		      e.printStackTrace();
//		    }
//
//	}


	private int updateAdword(List<HistoricoProduto> hps,String clientCustomerId) throws Throwable{
		try {
		      AdWordsServiceLogger.log();
//		      AdWordsUser user = new AdWordsUser();
		      AdWordsUser user = new AdWordsUser().generateClientAdWordsUser(clientCustomerId);
		      CampaignServiceInterface campaignService = 
		          user.getService(AdWordsService.V201209.CAMPAIGN_SERVICE);
		      Selector selector = new Selector();
		      selector.setFields(new String[] {"Id", "Name", "Status"});
		      selector.setOrdering(new OrderBy[] {new OrderBy("Name", com.google.api.adwords.v201209.cm.SortOrder.ASCENDING)});
		      CampaignPage page = campaignService.get(selector);
		      
		      int resp = -1;
		      
		      if (page.getEntries() != null) {
		        for (Campaign campaign : page.getEntries()) {
		        	if(campaign.getName().indexOf("Produto") != -1 && campaign.getStatus().toString().equals(ACTIVE)){
		        			//System.out.println("Campaign with name " + campaign.getName() + " and id "+ campaign.getId() + " Status :"+campaign.getStatus()+" was found.");
		        			resp = printAdGroups(campaign.getId(), user, hps);
		        			if(resp == 1)
		        				break;
		        	}
		        }
		      } 
//		      else {
//		        System.out.println("No campaigns were found.");
//		      }
		     return resp;
		    } catch (Exception e) {
		      e.printStackTrace();
		      throw new BeanException(e.getMessage(),e.getCause());
		    }
	}

//	
//	private int updateAdword(HistoricoProduto hp,String clientCustomerId) throws Throwable{
//		try {
//		      AdWordsServiceLogger.log();
////		      AdWordsUser user = new AdWordsUser();
//		      AdWordsUser user = new AdWordsUser().generateClientAdWordsUser(clientCustomerId);
//		      CampaignServiceInterface campaignService = 
//		          user.getService(AdWordsService.V201209.CAMPAIGN_SERVICE);
//		      Selector selector = new Selector();
//		      selector.setFields(new String[] {"Id", "Name", "Status"});
//		      selector.setOrdering(new OrderBy[] {new OrderBy("Name", com.google.api.adwords.v201209.cm.SortOrder.ASCENDING)});
//		      CampaignPage page = campaignService.get(selector);
//		      
//		      int resp = -1;
//		      
//		      if (page.getEntries() != null) {
//		        for (Campaign campaign : page.getEntries()) {
//		        	if(campaign.getName().indexOf("Produto") != -1){
////		        			System.out.println("Campaign with name " + campaign.getName() + " and id "+ campaign.getId() + " Status :"+campaign.getStatus()+" was found.");
//		        			resp = printAdGroups(campaign.getId(), user, hp);
//		        			if(resp == 1)
//		        				break;
//		        	}
//		        }
//		      } 
////		      else {
////		        System.out.println("No campaigns were found.");
////		      }
//		     return resp;
//		    } catch (Exception e) {
//		      e.printStackTrace();
//		      throw new BeanException(e.getMessage(),e.getCause());
//		    }
//	}


	private int printAdGroups(Long idCampaign,AdWordsUser user, List<HistoricoProduto> hps){
	    try {
	        AdWordsServiceLogger.log();
	        AdGroupServiceInterface adGroupService = user.getService(AdWordsService.V201209.ADGROUP_SERVICE);
	        Long campaignId = idCampaign; 
	        Selector selector = new Selector();
	        selector.setFields(new String[] {"Id", "Name", "Status"});
	        selector.setOrdering(new OrderBy[] {new OrderBy("Name", com.google.api.adwords.v201209.cm.SortOrder.ASCENDING)});
	        Predicate campaignIdPredicate =
	            new Predicate("CampaignId", PredicateOperator.IN, new String[] {campaignId.toString()});
	        selector.setPredicates(new Predicate[] {campaignIdPredicate});
	        AdGroupPage page = adGroupService.get(selector);
	        HistoricoProduto hp = null;
	        if (page.getEntries() != null) {
	          for (AdGroup adGroup : page.getEntries()) {
//		            System.out.println("AdGroup with name " + adGroup.getName() + " and id " + adGroup.getId() + " Status: "+adGroup.getStatus()+" was found.");
	        	  for(int x=0;x < hps.size();x++){
	        		  hp = hps.get(x);
			        	  if(adGroup.getName().indexOf(hp.getCODIGO()) != -1 && adGroup.getStatus().equals(AdGroupStatus.ENABLED)){ //encontrou o grupo de anuncios.
				            System.out.println("Encontrou ...... name " + adGroup.getName()
				                + " and id " + adGroup.getId() + " Status: "+adGroup.getStatus()+" was found.");
			        		//printAds(adGroup.getId(), user);
				            //System.out.println(hp.getPRECO());
				            disableAds(adGroup.getId(), user, hp);
				            printAdGroupCriterios(adGroup.getId(), user, hp);
			        		//return 1;
				            hpOk.add(hp);
			        	  }
	        		  
	        	  }
	          }
	        }
//	        else {
//	          System.out.println("No ad groups were found.");
//	        }
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	    return 0;
	}

	
//	private int printAdGroups(Long idCampaign,AdWordsUser user, HistoricoProduto hp){
//	    try {
//	        AdWordsServiceLogger.log();
//	        AdGroupServiceInterface adGroupService = user.getService(AdWordsService.V201209.ADGROUP_SERVICE);
//	        Long campaignId = idCampaign; 
//	        Selector selector = new Selector();
//	        selector.setFields(new String[] {"Id", "Name", "Status"});
//	        selector.setOrdering(new OrderBy[] {new OrderBy("Name", com.google.api.adwords.v201209.cm.SortOrder.ASCENDING)});
//	        Predicate campaignIdPredicate =
//	            new Predicate("CampaignId", PredicateOperator.IN, new String[] {campaignId.toString()});
//	        selector.setPredicates(new Predicate[] {campaignIdPredicate});
//	        AdGroupPage page = adGroupService.get(selector);
//	        if (page.getEntries() != null) {
//	          for (AdGroup adGroup : page.getEntries()) {
////		            System.out.println("AdGroup with name " + adGroup.getName()
////			                + " and id " + adGroup.getId() + " Status: "+adGroup.getStatus()+" was found.");
//	        	  if(adGroup.getName().indexOf(hp.getCODIGO()) != -1){ //encontrou o grupo de anuncios.
//		            System.out.println("Encontrou ...... name " + adGroup.getName()
//		                + " and id " + adGroup.getId() + " Status: "+adGroup.getStatus()+" was found.");
//	        		//printAds(adGroup.getId(), user);
//	        		  //recuperando todos anuncios, removendo e criando novos com novo description 2
//	        		
////		            System.out.println(hp.getPRECO());
//		            disableAds(adGroup.getId(), user, hp);
////		            printAdGroupCriterios(adGroup.getId(), user, hp);
//	        		return 1;
//	        	  }
//	          }
//	        }
////	        else {
////	          System.out.println("No ad groups were found.");
////	        }
//	      } catch (Exception e) {
//	        e.printStackTrace();
//	      }
//	    return 0;
//	}

	public boolean addAnuncio(Long idGroup, AdWordsUser user, TextAd tAd, HistoricoProduto hp){
	    try {
	        AdWordsServiceLogger.log();
	        AdGroupAdServiceInterface adGroupAdService = user.getService(AdWordsService.V201209.ADGROUP_AD_SERVICE);
	        long adGroupId =  idGroup.longValue(); 
	        
	        //mantendo texto adicional
	        String textd2 = tAd.getDescription2();

			String parte1 = "";
			String parte2 = "";
			try{
				parte1 = textd2.substring(0, textd2.indexOf("{param1:"));
				parte2 = textd2.substring(textd2.indexOf("}")+1, textd2.length());
			}catch(Exception e){
				//System.out.println("Erro ao recupear parte1 e parte2 do anuncio antigo \n "+textd2);
			}

	        
	        // Create text ad.
	        TextAd textAd = new TextAd();
	        textAd.setHeadline(tAd.getHeadline());
	        
	        textAd.setDescription1(tAd.getDescription1());
	        String str = String.format(new Locale("pt", "BR"), "%.2f", hp.getPRECO().doubleValue());
	        
	        if(parte1.equals("") && parte2.equals(""))
	        	parte1 = "R$";
	        textAd.setDescription2(parte1+"{param1: "+str+"}" + parte2);
	        
	        textAd.setDisplayUrl(tAd.getDisplayUrl());
	        textAd.setUrl(tAd.getUrl());

	        // Create ad group ad.
	        AdGroupAd textAdGroupAd = new AdGroupAd();
	        textAdGroupAd.setAdGroupId(adGroupId);
	        textAdGroupAd.setAd(textAd);

	        // Create operations.
	        AdGroupAdOperation textAdGroupAdOperation = new AdGroupAdOperation();
	        textAdGroupAdOperation.setOperand(textAdGroupAd);
	        textAdGroupAdOperation.setOperator(Operator.ADD);

	        AdGroupAdOperation[] operations = new AdGroupAdOperation[] {textAdGroupAdOperation};

	        AdGroupAdReturnValue result = adGroupAdService.mutate(operations);
	        // Display ads.
	        if (result != null && result.getValue() != null) {
	          for (AdGroupAd adGroupAdResult : result.getValue()) {
	            System.out.println("Ad with id  \"" + adGroupAdResult.getAd().getId() + "\""
	                + " and type \"" + adGroupAdResult.getAd().getAdType() + "\" was added.");
	          }
	          return true;
	        }
	        else {
	          System.out.println("No ads were added.");
	        	return false;
	        }
	      } catch (Exception e) {
	    	  
	    	  System.out.println("ERRO: "+e.getMessage());
	    	  e.printStackTrace(System.out);
	    	  return false;
	      }


	}


	public void printAdGroupCriterios(Long idGroup, AdWordsUser user,HistoricoProduto hp){
	    try {
	        // Log SOAP XML request and response.
	        AdWordsServiceLogger.log();
	        // Get the AdGroupCriterionService.
	        AdGroupCriterionServiceInterface adGroupCriterionService =
	            user.getService(AdWordsService.V201209.ADGROUP_CRITERION_SERVICE);

	        Long adGroupId = idGroup; //Long.parseLong("INSERT_AD_GROUP_ID_HERE");

	        // Create selector.
	        Selector selector = new Selector();
	        selector.setFields(new String[] {"Id", "AdGroupId"});
	        selector.setOrdering(new OrderBy[] {new OrderBy("AdGroupId", SortOrder.ASCENDING)});

	        // Create predicates.
	        Predicate adGroupIdPredicate =
	            new Predicate("AdGroupId", PredicateOperator.IN, new String[] {adGroupId.toString()});
	        selector.setPredicates(new Predicate[] {adGroupIdPredicate});

	        // Get all ad group criteria.
	        AdGroupCriterionPage page = adGroupCriterionService.get(selector);

	        // Display ad group criteria.
	        if (page.getEntries() != null && page.getEntries().length > 0) {
	          for (AdGroupCriterion adGroupCriterion : page.getEntries()) {
	            updateAdParam(adGroupId,adGroupCriterion.getCriterion().getId(),user,hp);
	            
	          }
	        } 
//	        else {
//	          System.out.println("No ad group criteria were found.");
//	        }
	      } catch (Exception e) {
	        e.printStackTrace();
	      }

	}
	

	public  void updateAdParam(Long adGroup, Long idKeyword, AdWordsUser user, HistoricoProduto hp){
	    try {
	        // Log SOAP XML request and response.
	        AdWordsServiceLogger.log();

	        // Get the AdParamService.
	        AdParamServiceInterface adParamService =
	            user.getService(AdWordsService.V201209.AD_PARAM_SERVICE);

	        long adGroupId = adGroup.longValue(); //Long.parseLong("INSERT_AD_GROUP_ID_HERE");
	        long keywordId = idKeyword.longValue(); //Long.parseLong("INSERT_KEYWORD_ID_HERE");

	        // Create ad params.
	        AdParam adParam1 = new AdParam();
	        adParam1.setAdGroupId(adGroupId);
	        adParam1.setCriterionId(keywordId);
	        String str = String.format(new Locale("pt", "BR"), "%.2f", hp.getPRECO().doubleValue());
	        adParam1.setInsertionText(str);
	        adParam1.setParamIndex(1);

	        // Create operations.
	        AdParamOperation adParamOperation1 = new AdParamOperation();
	        adParamOperation1.setOperand(adParam1);
	        adParamOperation1.setOperator(Operator.SET);

	        AdParamOperation[] operations = new AdParamOperation[] {adParamOperation1};

	        // Set ad parameters.
	        AdParam[] adParams = adParamService.mutate(operations);

	        // Display ad parameters.
	        if (adParams != null) {
	          for (AdParam adParam : adParams) {
	            logger.debug("Ad parameter with ad group id \"" + adParam.getAdGroupId()
	                + "\", criterion id \"" + adParam.getCriterionId()
	                + "\", insertion text \"" + adParam.getInsertionText()
	                + "\", and parameter index \"" + adParam.getParamIndex()
	                + "\" was set.");
	          }
	        } 
//	        else {
//	          System.out.println("No ad parameters were set.");
//	        }
	      } catch (Exception e) {
	        e.printStackTrace();
	      }

	}

	public void disableAds(Long idGroup,AdWordsUser user,HistoricoProduto hp){
	    try {
	    	boolean xyz=false;
	        AdWordsServiceLogger.log();
	        AdGroupAdServiceInterface adGroupAdService = user.getService(AdWordsService.V201209.ADGROUP_AD_SERVICE);
	        Long adGroupId =  idGroup; 
	        // Create selector.
	        Selector selector = new Selector();
	        selector.setFields(new String[] {"Id", "AdGroupId", "Status"});
	        selector.setOrdering(new OrderBy[] {new OrderBy("Id", com.google.api.adwords.v201209.cm.SortOrder.ASCENDING)});
	        // Create predicates.
	        Predicate adGroupIdPredicate =new Predicate("AdGroupId", PredicateOperator.IN, new String[] {adGroupId.toString()});
	        Predicate statusPredicate = new Predicate("Status", PredicateOperator.IN, new String[] {"ENABLED", "PAUSED","DISABLED"});
	        selector.setPredicates(new Predicate[] {adGroupIdPredicate, statusPredicate});
	        // Get all ads.
	        AdGroupAdPage page = adGroupAdService.get(selector);
	        TextAd tpAd = null;
	  	  System.out.println("CODIGO:"+hp.getCODIGO()+" DISPONIBILIDADE :"+hp.getDISPONIBILIDADE()+" Tipo :"+hp.getTipo());
	        if (page.getEntries() != null && page.getEntries().length > 0) {
	          for (AdGroupAd adGroupAd : page.getEntries()) {
	        	  try{//validar esse typeCast
	        	  TextAd tAd = (TextAd)adGroupAd.getAd();
	        	  
	        	         if(hp.getTipo().trim().toUpperCase().equals("ATIVADO") || 
			        			  hp.getTipo().trim().toUpperCase().equals("PAUSADO")){
	        	         if(hp.getDISPONIBILIDADE() == 0){
			        			  if(adGroupAd.getStatus().equals(AdGroupAdStatus.PAUSED)){
				        			  System.out.println("ATIVANDO..");
			        				  updateAdAtivar(idGroup, tAd.getId(), user);
			        			  }
			        		  }else{
			        			  if(adGroupAd.getStatus().equals(AdGroupAdStatus.ENABLED)){
				        			  System.out.println("PAUSANDO..");
			        				  updateAdStop(idGroup, tAd.getId(), user);
			        			  }
			        		  }
			        	  }else if(hp.getTipo().trim().toUpperCase().equals("EXCLUIDO")){
			        		  if(adGroupAd.getStatus().equals(AdGroupAdStatus.ENABLED))
			        			  updateAdStop(idGroup, tAd.getId(), user);
			        	  }else if(hp.getTipo().trim().toUpperCase().equals("ALTERADO") || 
			        			  hp.getTipo().trim().toUpperCase().equals("NOVO")){
			        		  if(tAd.getDescription2().indexOf("param1") != -1 && adGroupAd.getStatus().equals(AdGroupAdStatus.ENABLED)){
			        			  if(addAnuncio(idGroup, user, tAd, hp)){
			        				  updateAdStop(idGroup, tAd.getId(), user);
			        				  xyz = true;
			        			  }
			        		  }else{
		        				  if(tAd.getDescription2().indexOf("param1") != -1){
		        					  tpAd = tAd;
		        				  }
		        			  }
			        	  }
	        	          	  
	        	  	        	 	        	    
	        	  }catch(Exception e){
	        		  logger.error("Provavelmente o anuncio n?o ? de texto", e);
	        	  }
	          }
	        }
	        
	        if(hp.getTipo().trim().toUpperCase().equals("ALTERADO") || 
      			  hp.getTipo().trim().toUpperCase().equals("NOVO")){
		        if(!xyz && tpAd != null){
		        	  System.out.println("vai adicionar--> CODIGO"+hp.getCODIGO()+" DISPONIBILIDADE :"+
  			  				hp.getDISPONIBILIDADE()+" Tipo :"+hp.getTipo());
		  		    addAnuncio(idGroup, user, tpAd, hp);
		        }

	        }
	        
	        
//	        else {
//	          System.out.println("No ads were found.");
//	        }
	      } catch (Exception e) {
	        e.printStackTrace(System.out);
	        
	      }

	}

	public  void updateAdAtivar(Long idGroup, Long idAd, AdWordsUser user){
		try {
		      AdWordsServiceLogger.log();
		      AdGroupAdServiceInterface adGroupAdService = user.getService(AdWordsService.V201209.ADGROUP_AD_SERVICE);

		      long adGroupId = idGroup.longValue(); 
		      long adId =  idAd.longValue();
		      Ad ad = new Ad();
		      ad.setId(adId);
		      
		      AdGroupAd adGroupAd = new AdGroupAd();
		      adGroupAd.setAdGroupId(adGroupId);
		      adGroupAd.setAd(ad);
		      adGroupAd.setStatus(AdGroupAdStatus.ENABLED);

		      AdGroupAdOperation operation = new AdGroupAdOperation();
		      operation.setOperand(adGroupAd);
		      operation.setOperator(Operator.SET);
		      AdGroupAdOperation[] operations = new AdGroupAdOperation[] {operation};

		      AdGroupAdReturnValue result = adGroupAdService.mutate(operations);
		      if (result != null && result.getValue() != null) {
		        for (AdGroupAd adGroupAdResult : result.getValue()) {
		        	//colocar na LOG!!!
		        	System.out.println("ATIVADO - Ad with id \"" + adGroupAdResult.getAd().getId()
		              + "\", type \"" + adGroupAdResult.getAd().getAdType()
		              + "\", and status \"" + adGroupAdResult.getStatus() + "\" was updated.");
		        }
		      }
//		      else {
//		        System.out.println("No ads were updated.");
//		      }
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	}


	public  void updateAdDisable(Long idGroup, Long idAd, AdWordsUser user){
		try {
		      AdWordsServiceLogger.log();
		      AdGroupAdServiceInterface adGroupAdService = user.getService(AdWordsService.V201209.ADGROUP_AD_SERVICE);

		      long adGroupId = idGroup.longValue(); 
		      long adId =  idAd.longValue();
		      Ad ad = new Ad();
		      ad.setId(adId);
		      
		      AdGroupAd adGroupAd = new AdGroupAd();
		      adGroupAd.setAdGroupId(adGroupId);
		      adGroupAd.setAd(ad);
		      adGroupAd.setStatus(AdGroupAdStatus.DISABLED);

		      AdGroupAdOperation operation = new AdGroupAdOperation();
		      operation.setOperand(adGroupAd);
		      operation.setOperator(Operator.SET);
		      AdGroupAdOperation[] operations = new AdGroupAdOperation[] {operation};

		      AdGroupAdReturnValue result = adGroupAdService.mutate(operations);
		      if (result != null && result.getValue() != null) {
		        for (AdGroupAd adGroupAdResult : result.getValue()) {
		        	//colocar na LOG!!!
		        	System.out.println("Removed - Ad with id \"" + adGroupAdResult.getAd().getId()
		              + "\", type \"" + adGroupAdResult.getAd().getAdType()
		              + "\", and status \"" + adGroupAdResult.getStatus() + "\" was updated.");
		        }
		      }
//		      else {
//		        System.out.println("No ads were updated.");
//		      }
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	}

	public  void updateAdStop(Long idGroup, Long idAd, AdWordsUser user){
		try {
		      AdWordsServiceLogger.log();
		      AdGroupAdServiceInterface adGroupAdService = user.getService(AdWordsService.V201209.ADGROUP_AD_SERVICE);

		      long adGroupId = idGroup.longValue(); 
		      long adId =  idAd.longValue();
		      Ad ad = new Ad();
		      ad.setId(adId);
		      
		      AdGroupAd adGroupAd = new AdGroupAd();
		      adGroupAd.setAdGroupId(adGroupId);
		      adGroupAd.setAd(ad);
		      adGroupAd.setStatus(AdGroupAdStatus.PAUSED);

		      AdGroupAdOperation operation = new AdGroupAdOperation();
		      operation.setOperand(adGroupAd);
		      operation.setOperator(Operator.SET);
		      AdGroupAdOperation[] operations = new AdGroupAdOperation[] {operation};

		      AdGroupAdReturnValue result = adGroupAdService.mutate(operations);
		      if (result != null && result.getValue() != null) {
		        for (AdGroupAd adGroupAdResult : result.getValue()) {
		        	//colocar na LOG!!!
		        	System.out.println("PAUSED - Ad with id \"" + adGroupAdResult.getAd().getId()
		              + "\", type \"" + adGroupAdResult.getAd().getAdType()
		              + "\", and status \"" + adGroupAdResult.getStatus() + "\" was updated.");
		        }
		      }
//		      else {
//		        System.out.println("No ads were updated.");
//		      }
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	}


	public void enviarEmail(Usuario u){

		try {
//			Usuario u = (Usuario) Utils.buscarSessao("usuario");
			EmailDao eDao = new EmailDao();
			Object[] strObjects =  eDao.buscarStringsEmails(u.getIdCliente());
			HistoricoProdutoDao hpDao = new HistoricoProdutoDao();
			//TEMPORARIO -> colocar em uma tela com permissao(criar permissao para recuperar o email admin e usar para enviar o email)
			UsuarioDao uDao = new UsuarioDao();
			Usuario userAdmin = uDao.buscarUsuario("admin");
			SendEmail sm = new SendEmail();
			
			EmailEnviarDao eeDao = new EmailEnviarDao();
			
			EmailEnviar ee = eeDao.buscarEmailEnviar(idArquivo, 1);
			
			
//			List<EmailEnviar> lee = eeDao.buscarEmailEnviars(1);
//			
//			for(int i=0;i < lee.size();i++){
//				EmailEnviar ee = lee.get(i);
//			
				//String strMessage = ee.getMensagem()+"<br>Adwords Atualizado com sucesso.<br> Acesse o link a baixo e click em Procurar<br>" + "http://servidor/validateadwords/pages/historico/historico.jsf";
//
//				
				sm.sendMail(userAdmin.getEmail(),userAdmin.getSenha(),strObjects,"Adwords atualizado com sucesso.",ee.getMensagem());
				ee.setSituacao(2);
				eeDao.alterar(ee);
//			}
			
			
		} catch (AuthenticationFailedException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
	
	public String getPosicionamentos() {
		return posicionamentos;
	}


	public void setPosicionamentos(String posicionamentos) {
		this.posicionamentos = posicionamentos;
	}


	public Date getDataDe() {
		return dataDe;
	}


	public void setDataDe(Date dataDe) {
		this.dataDe = dataDe;
	}


	public Date getDataAte() {
		return dataAte;
	}


	public void setDataAte(Date dataAte) {
		this.dataAte = dataAte;
	}


	public Date getDataMaxima() {
		return dataMaxima;
	}


	public void setDataMaxima(Date dataMaxima) {
		this.dataMaxima = dataMaxima;
	}
	public long getIdArquivo() {
		return idArquivo;
	}

	public void setIdArquivo(long idArquivo) {
		this.idArquivo = idArquivo;
	}
	


}
