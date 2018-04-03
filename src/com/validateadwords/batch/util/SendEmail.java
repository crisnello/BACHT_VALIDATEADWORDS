package com.validateadwords.batch.util;


import java.util.Properties;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SendEmail {

    private String mailSMTPServer;
    private String mailSMTPServerPort;
    private String pUser, pPass;
    public SendEmail() { //Para o GMAIL
        mailSMTPServer = "smtp.gmail.com";
        mailSMTPServerPort = "465";
    }
    SendEmail(String mailSMTPServer, String mailSMTPServerPort) { //Para outro Servidor
        this.mailSMTPServer = mailSMTPServer;
        this.mailSMTPServerPort = mailSMTPServerPort;
    }
    public void sendMail(String from,String pSenhaFrom, Object[] to, String subject, String message) throws AuthenticationFailedException, Exception {
    	pUser = from;
    	pPass = pSenhaFrom;
    	sendMail(from, to, subject, message);
    }
    public void sendMail(String from, Object[] tos, String subject, String message)throws AuthenticationFailedException,Exception {
       Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp"); //define protocolo de envio como SMTP
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host", mailSMTPServer); //server SMTP do GMAIL
        props.put("mail.smtp.auth", "true"); //ativa autenticacao
        props.put("mail.smtp.user", from); //usuario ou seja, a conta que esta enviando o email (tem que ser do GMAIL)
        props.put("mail.debug", "true");
        props.put("mail.mime.charset", "ISO-8859-1");
        props.put("mail.smtp.port", mailSMTPServerPort); //porta
        props.put("mail.smtp.socketFactory.port", mailSMTPServerPort); //mesma porta para o socket
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        SimpleAuth auth = null;
        auth = new SimpleAuth (pUser,pPass);
        
        Session session = null;
        try{
        	session = Session.getDefaultInstance(props, auth);
        	session.setDebug(true); //Habilita o LOG das a��es executadas durante o envio do email
        }catch(SecurityException se){
        	session = Session.getInstance(props, auth);
        	session.setDebug(true);
        }
        Message msg = new MimeMessage(session);
		Multipart mp = new MimeMultipart();
		MimeBodyPart tbp = new MimeBodyPart();

		try{

			tbp.setHeader("MIME-Version", "1.0");
//			tbp.setHeader("Content-Type", "text/plain; charset='iso-8859-1'");
			tbp.setHeader("Content-Type", "text/html; charset='iso-8859-1'");
//			tbp.setText(message);
			tbp.setContent(message, "text/html");

			mp.addBodyPart(tbp);
		}catch(Exception exxx){
			exxx.printStackTrace();
		}

        try {
            //C�pia para o pr�prio email que est� enviando!!!
        	msg.addRecipient(Message.RecipientType.TO, new InternetAddress(from));
        	   for(int i = 0; i < tos.length; i++){
        		   msg.addRecipient(Message.RecipientType.TO, new InternetAddress(tos[i].toString()));
        		    }
            msg.setFrom(new InternetAddress(from));
            msg.setSubject(subject);
            msg.setContent(mp);
        } catch (Exception e) {
            System.out.println(">> Erro: Completar Mensagem");
            e.printStackTrace();
        }

        Transport tr;
        try {
            tr = session.getTransport("smtp"); //define smtp para transporte
            tr.connect(mailSMTPServer, pUser,pPass);
            msg.saveChanges(); // don't forget this
            tr.sendMessage(msg, msg.getAllRecipients());
            tr.close();
        }catch(AuthenticationFailedException afe){
//        	System.out.println("Erro -  AuthenticationFailedException:"+afe.getMessage());
        	throw new Exception("Falha na Autenticacao GMAIL");
        }catch (Exception e) {
            System.out.println(">> Erro: Envio Mensagem");
            e.printStackTrace();
        }
    }
	public String getPPass() {
		return pPass;
	}
	public void setPPass(String pass) {
		pPass = pass;
	}
	public String getPUser() {
		return pUser;
	}
	public void setPUser(String user) {
		pUser = user;
	}
}



//clase que retorna uma autenticacao para ser enviada e verificada pelo servidor smtp
class SimpleAuth extends Authenticator {
  public String username = null;
  public String password = null;


  public SimpleAuth(String user, String pwd) {
      username = user;
      password = pwd;
  }

  protected PasswordAuthentication getPasswordAuthentication() {
      return new PasswordAuthentication (username,password);
  }
}

