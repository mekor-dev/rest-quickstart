/**
 * 
 */
package mekor.rest.quickstart.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import mekor.rest.quickstart.model.entities.notification.Notification;
import mekor.rest.quickstart.repositories.NotificationRepository;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;
import mekor.rest.quickstart.services.utils.GenericEntityService;

/**
 * Service for managing {@link Notification}s. Methods in this service create
 * the notification in database and also send them by SSE.
 * 
 * @author mekor
 *
 */
@ApplicationScoped
public class NotificationService extends GenericEntityService<Notification, Long> {

	@Inject
	private Logger log;

	@Inject
	private NotificationRepository notifRepo;

//	/**
//	 * Create a notification that must be sent when a user answer to an interview.
//	 * 
//	 * @param interview   The answered interview
//	 * @param currentUser The current user who answered to the interview
//	 * @param currentOrga The current orga in which this interview has been answered
//	 * @return The newly created notification
//	 */
//	public Notification createInterviewAnsweredNotification(Interview interview, User currentUser, Organization currentOrga) {
//		log.debug("Entering createInterviewAnsweredNotification({}, {}, {})", interview, currentUser, currentOrga);
//		try {
//			CampaignSubscription sub = interview.getCampaignSubscriptionStep().getSubscription();
//
//			Notification notif = new Notification();
//			notif.setType(NotificationType.INTERVIEW_ANSWERED);
//			notif.setUser(sub.getUser());
//			notif.setSender(currentUser);
//			notif.setOrga(currentOrga);
//			// Title
//			Map<String, Object> titleParams = new HashMap<>();
//			titleParams.put("userName", userService.getFullName(currentUser));
//			titleParams.put("campaignName", sub.getCampaign().getTitle());
//			notif.setTitle(generateTitle(NotificationType.INTERVIEW_ANSWERED, titleParams));
//
//			// Redirection
//			Map<String, String> redirectionParams = new HashMap<>();
//			redirectionParams.put("campaignID", sub.getCampaign().getId().toString());
//			redirectionParams.put("userID", sub.getUser().getId().toString());
//			redirectionParams.put("interviewID", interview.getId().toString());
//			notif.setRedirection(generateRedirect(ApplicationPage.INTERVIEW_RH, redirectionParams));
//
//			saveNotif(notif);
//			log.debug("Leaving createInterviewAnsweredNotification() -> {}", notif);
//			return notif;
//		}
//		catch (Exception e) {
//			throw new NotificationCreationException("Error while creating INTERVIEW_ANSWERED notification", e);
//		}
//	}
//
//	/**
//	 * Save the notification in database and send it by SSE.
//	 * 
//	 * @param notif The notification to save and send.
//	 */
//	private void saveNotif(Notification notif) {
//		notifRepo.persist(notif);
//		sseAPI.sendNotification(notif.getUser().getId(), notif);
//	}
//
//	/**
//	 * Generate the title of the notification by getting the corresponding
//	 * {@link NotificationTemplate} and replacing every occurrence of the params by
//	 * their values.
//	 * 
//	 * @param type   The type of the notification to generate title from.
//	 * @param params The list of params to replace in the title.
//	 * @return The generated title.
//	 */
//	private LocalizedString generateTitle(NotificationType type, Map<String, Object> params) {
//		NotificationTemplate template = notifTemplateRepo.findBy(type);
//		if (template == null) {
//			throw new NotificationCreationException("No NotificationTemplate found for type " + type.toString());
//		}
//		template.getTitle().init();
//		LocalizedString title = new LocalizedString();
//		for (Entry<String, String> entry : template.getTitle().getMap().entrySet()) {
//			Locale locale = LocaleUtils.parseLocale(entry.getKey(), false);
//			if (locale != null) {
//				String titleString = entry.getValue();
//				for (Entry<String, Object> param : params.entrySet()) {
//					String replace = null;
//					if (param.getValue() instanceof String) {
//						replace = (String) param.getValue();
//					}
//					else if (param.getValue() instanceof LocalizedString) {
//						replace = ((LocalizedString) param.getValue()).getText(locale);
//					}
//					else {
//						throw new NotificationCreationException("Error while generating Title. All params should be String or LocalizedString");
//					}
//					titleString = titleString.replaceAll("\\{" + param.getKey() + "\\}", replace);
//				}
//				title.setText(titleString, locale);
//			}
//		}
//		return title;
//	}
//
//	/**
//	 * Generate the redirection of the notification by getting the page url and
//	 * replacing every occurrence of the params by their values.
//	 * 
//	 * @param page   The page containing path of the redirection url
//	 * @param params The params to replace in the redirection url
//	 * @return
//	 */
//	private String generateRedirect(ApplicationPage page, Map<String, String> params) {
//		String redirection = page.getPath();
//		for (Entry<String, String> param : params.entrySet()) {
//			redirection = redirection.replaceAll("\\{" + param.getKey() + "\\}", param.getValue());
//		}
//		return redirection;
//	}

	@Override
	protected GenericRepostiory<Notification, Long> getEntityRepo() {
		return notifRepo;
	}

	@Override
	protected Logger getLogger() {
		return log;
	}

}
