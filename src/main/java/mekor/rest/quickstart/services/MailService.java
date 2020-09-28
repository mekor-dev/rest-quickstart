/**
 * 
 */
package mekor.rest.quickstart.services;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;

import mekor.rest.quickstart.configuration.AppConfig;
import mekor.rest.quickstart.exceptions.MailSendException;
import mekor.rest.quickstart.model.entities.mail.MailMessage;
import mekor.rest.quickstart.model.entities.mail.MailParam;
import mekor.rest.quickstart.model.entities.mail.MailType;
import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.utils.ApplicationPage;
import mekor.rest.quickstart.utils.LocaleUtils;

/**
 * Service that allow to send mail via SMTP (via Wildfly config). <br />
 * This service also handle construction of mails.
 * 
 * @author mekor
 *
 */
@ApplicationScoped
public class MailService {

	@Resource(mappedName = "java:jboss/mail/restQuickstart")
	private Session session;

	@Inject
	private AppConfig appConfig;

	@Inject
	private Logger log;

	@Inject
	private MailMessageService mailMessageService;

	/**
	 * Send a mail to a user who forgot his password so he can change it.
	 * 
	 * @param user          The user who lost its password
	 * @param orga          The organization in which this user asked for a password
	 *                      reset.
	 * @param passwordToken The token that allow the user to reset its password.
	 * @param requestLocale Locale of the current request
	 * @throws MailSendException
	 */
	public void sendForgottenPasswordMail(User user, String passwordToken, Locale requestLocale) throws MailSendException {
		Map<MailParam, String> params = new HashMap<>();
		params.put(MailParam.USERNAME, user.getNickname());
		params.put(MailParam.FORGOTTEN_PASSWORD_LINK, ApplicationPage.FORGOTTEN_PASSWORD_CHANGE.getUrl(appConfig).replace("{token}", passwordToken));

		sendMail(MailType.FORGOTTEN_PASSWORD, user, requestLocale, params);
	}

	////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////// GENERIC CREATION AND UTILS
	////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Generate the HTML content of a mail. Select an HTML structure from the
	 * mailMessage with the given locale. Change every params occurence by its
	 * value.
	 * 
	 * @param mailMessage The mailMessage containing info about the mail to send
	 * @param locale      The locale in which we want to write the mail
	 * @param params      The list of params to replace in the mail
	 * @return A definitive HTML content to send.
	 */
	private String generateHTML(MailMessage mailMessage, Locale locale, Map<MailParam, String> params) {
		log.debug("Entering generateHTML(Locale: {}, params: {})", locale.getLanguage(), params);

		String html = mailMessage.getHtml().get(locale.getLanguage());
		for (Entry<MailParam, String> param : params.entrySet()) {
			html = html.replaceAll("\\{" + param.getKey().getName() + "\\}", param.getValue());
		}
		if (log.isDebugEnabled()) {
			log.debug("Leaving generateHTML() -> {}", html.replace("\r", "").replace("\n", ""));
		}
		return html;
	}

	/**
	 * Generate and send a mail
	 * 
	 * @param type   The type of the mail to send
	 * @param orga   The organization sending the mail
	 * @param to     The recipient
	 * @param locale The locale to use if the recipient has no default locale
	 * @param params The list of params to replace during HTML generation
	 * @throws MailSendException
	 */
	private void sendMail(MailType type, User to, Locale locale, Map<MailParam, String> params) throws MailSendException {
		locale = LocaleUtils.getUserLocale(to, locale);
		try {
			MailMessage mailMessage = mailMessageService.findByID(type);

			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress("test@mail.com", "test"));
			message.setSubject(mailMessage.getSubject().getText(locale));
			message.setContent(generateHTML(mailMessage, locale, params), "text/html; charset=utf-8");
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to.getEmail()));

			Transport.send(message);
		}
		catch (Exception e) {
			throw new MailSendException("Error while sending mail", e);
		}
	}

}
