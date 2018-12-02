package email.service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import email.configuration.MessageBean;

@Service
public class EmailService {
    
	private static final Logger log = LoggerFactory.getLogger(EmailService.class);
	
	@Autowired
	private MessageBean messageBean;
	
    static final String SUBJECT = "Este é um e-mail de teste enviado agora";
    
    static final String BODY = String.join(
    	    System.getProperty("line.separator"),
    	    "<h1>Este é um e-mail de teste enviado via AWS</h1>",
    	    "<p>Este e-mail apenas foi enviado pois soliciatamos a confirmação.",
    	    "<strong>Testando tags html</>",
    	    "<br /><b>Anderson Babinski<b>"
    	);
    
    public Boolean send() {
    	String format = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm:ss").format(new Date());
    	StringBuilder strBuilder = new StringBuilder();
    	strBuilder.append(BODY);
    	strBuilder.append(String.format("<br />%s", format));
    	
    	try {
    		messageBean.message(SUBJECT, strBuilder.toString());
    		return Boolean.TRUE;
		} catch (MessagingException | UnsupportedEncodingException e) {
			log.error(e.getMessage());
			return Boolean.FALSE;
		}
    }
}
