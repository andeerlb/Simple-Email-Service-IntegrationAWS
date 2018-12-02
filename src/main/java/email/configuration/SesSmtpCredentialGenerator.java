package email.configuration;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SesSmtpCredentialGenerator implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LoggerFactory.getLogger(SesSmtpCredentialGenerator.class);

	private static final String KEY_ENV_VARIABLE = "AWS_SECRET_ACCESS_KEY";
	private static final String MESSAGE = "SendRawEmail";
	private static final byte VERSION = 0x02;

	/*
	 * Get the AWS secret access key from environment variable
	 * AWS_SECRET_ACCESS_KEY.
	 */
	@PostConstruct
	private String getEnvVariable() {
		String key = System.getenv(KEY_ENV_VARIABLE);
		if (key == null) {
			log.error("Cannot find environment variable {}", KEY_ENV_VARIABLE);
			System.exit(0);
		}
		return key;
	}
	
	/*
	 * To get the final SMTP password, convert the HMAC signature to base 64.
	 */
	@Bean
	public String generatingSmtpPassword() {
		// Compute the HMAC signature on the input data bytes.
		byte[] rawSignature = null;
		try {
			rawSignature = getSignature();
		} catch (InvalidKeyException | NoSuchAlgorithmException e) {
			log.error("Error generating SMTP password: {}", e.getMessage());
			return null;
		}

		// Prepend the version number to the signature.
		byte[] rawSignatureWithVersion = new byte[rawSignature.length + 1];
		byte[] versionArray = { VERSION };
		System.arraycopy(versionArray, 0, rawSignatureWithVersion, 0, 1);
		System.arraycopy(rawSignature, 0, rawSignatureWithVersion, 1, rawSignature.length);

		String smtpPassword = DatatypeConverter.printBase64Binary(rawSignatureWithVersion);
		log.info("Password successfully generated: {}", smtpPassword);
		return smtpPassword;
	}

	/*
	 * Get an HMAC-SHA256 Mac instance and initialize it with the AWS secret access
	 * key.
	 */
	private byte[] getSignature() throws NoSuchAlgorithmException, InvalidKeyException {
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(getSecretKeySpec());

		return mac.doFinal(MESSAGE.getBytes());
	}

	/*
	 * Create an HMAC-SHA256 key from the raw bytes of the AWS secret access key.
	 */
	private SecretKeySpec getSecretKeySpec() {
		return new SecretKeySpec(getEnvVariable().getBytes(), "HmacSHA256");
	}
}
