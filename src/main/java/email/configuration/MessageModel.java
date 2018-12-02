package email.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MessageModel {
	@Value("${cloud.aws.ses.from.email}")
    private String from;
    @Value("${cloud.aws.ses.from.name}")
    private String fromName;
	@Value("${cloud.aws.ses.to.email}")
	private String to;
    @Value("${cloud.aws.smtp.username}")
    private String smtpUsername;
    @Value("${cloud.aws.smtp.password}")
    private String smtpPassword;
    @Value("${cloud.aws.smtp.host}")
    private String host;
    @Value("${cloud.aws.smtp.port}")
    private int port;
    
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getSmtpUsername() {
		return smtpUsername;
	}
	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}
	public String getSmtpPassword() {
		return smtpPassword;
	}
	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
