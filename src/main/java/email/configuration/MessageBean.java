package email.configuration;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final Logger log = LoggerFactory.getLogger(MessageBean.class);
	
	@Autowired
	private MessageModel builder;
	
	/*
	 * Create a message with the specified information.
	 * @Params subject
	 * @Params body
	 */
	public void message(String subject, String body) throws MessagingException, UnsupportedEncodingException {
		Session session = getSession();
		
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(builder.getFrom(), builder.getFromName()));
		msg.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(builder.getTo()));
		msg.setSubject(subject);
		msg.setContent(body, "text/html");
		
		send(msg, session);
	}
	
	/*
	 * Create a message with the specified information.
	 * @Params subject
	 * @Params body
	 * @Params type
	 */ 
	public void message(String subject, String body, String type) throws UnsupportedEncodingException, MessagingException {
		Session session = getSession();
		
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(builder.getFrom(), builder.getFromName()));
		msg.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(builder.getTo()));
		msg.setSubject(subject);
		msg.setContent(body, type);
		
		send(msg, session);
	}

	/*
	 * Create a Session object to represent a mail session with the specified
	 * properties.
	 */
	private Session getSession() {
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.port", builder.getPort());
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");

		return Session.getDefaultInstance(props);
	}
	
	/*
	 * Add a configuration set header. Comment or delete the next line if you
	 * are not using a configuration set msg.setHeader("X-SES-CONFIGURATION-SET",
	 * CONFIGSET);
	 */
	private void send(MimeMessage message, Session session) throws MessagingException  {
		try(Transport transport = session.getTransport()) {
			log.info("Initialize sendindg...");
			
			transport.connect(builder.getHost(), builder.getSmtpUsername(), builder.getSmtpPassword());
			transport.sendMessage(message, message.getAllRecipients());
			log.info("Email sent!");
		} catch (MessagingException e) {
			log.error("The email was not sent.");
			throw new MessagingException(e.getMessage(), e);
		}
	}
}
