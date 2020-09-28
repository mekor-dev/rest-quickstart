/**
 * 
 */
package mekor.rest.quickstart.model.entities.mail;

import java.util.Map;

import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import mekor.rest.quickstart.model.entities.utils.GenericEntity;
import mekor.rest.quickstart.model.entities.utils.LocalizedString;
import mekor.rest.quickstart.model.entities.utils.SQLConstants;

/**
 * Subject and text of a mail for an Organization and an MailType.
 * 
 * @author mekor
 *
 */
@Entity
@Table
public class MailMessage extends GenericEntity {

	private static final long serialVersionUID = -7329744696984609972L;

	/**
	 * Type of the mail for which to use this message.
	 */
	@Id
	@Enumerated(EnumType.STRING)
	private MailType mailType;

	/**
	 * Subject of this message
	 */
	@Embedded
	@AttributeOverride(name = "jsonText", column = @Column(name = "subject", columnDefinition = SQLConstants.TEXT_DEFINITION))
	private LocalizedString subject;

	/**
	 * Map of HTML for each languages.
	 */
	@ElementCollection
	@CollectionTable(name = "MailMessageHTML", joinColumns = { @JoinColumn(name = "messageType", referencedColumnName = "mailType") })
	@MapKeyColumn(name = "language")
	@Column(name = "html", nullable = false, columnDefinition = SQLConstants.TEXT_DEFINITION)
	private Map<String, String> html;

	/**
	 * @see {@link #mailType}
	 */
	public MailType getMailType() {
		return mailType;
	}

	/**
	 * @see {@link #mailType}
	 */
	public void setMailType(MailType mailType) {
		this.mailType = mailType;
	}

	/**
	 * @see {@link #subject}
	 */
	public LocalizedString getSubject() {
		return subject;
	}

	/**
	 * @see {@link #subject}
	 */
	public void setSubject(LocalizedString subject) {
		this.subject = subject;
	}

	/**
	 * @see {@link #html}
	 */
	public Map<String, String> getHtml() {
		return html;
	}

	/**
	 * @see {@link #html}
	 */
	public void setHtml(Map<String, String> html) {
		this.html = html;
	}

}
