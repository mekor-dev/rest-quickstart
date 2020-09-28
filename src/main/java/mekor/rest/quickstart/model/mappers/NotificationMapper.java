/**
 * 
 */
package mekor.rest.quickstart.model.mappers;

import javax.inject.Inject;

import org.mapstruct.Mapper;

import mekor.rest.quickstart.model.dtos.notification.NotificationDTO;
import mekor.rest.quickstart.model.entities.notification.Notification;
import mekor.rest.quickstart.model.mappers.utils.LocalizedStringMapper;
import mekor.rest.quickstart.model.mappers.utils.SingleIDMapper;
import mekor.rest.quickstart.model.mappers.utils.SingleIDMapperToDTO;
import mekor.rest.quickstart.repositories.NotificationRepository;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;

/**
 * Maps {@link Notification} to {@link NotificationDTO}
 * 
 * @author mekor
 *
 */
@Mapper(uses = { UserMapper.class, LocalizedStringMapper.class })
public abstract class NotificationMapper implements SingleIDMapperToDTO<Notification, NotificationDTO>, SingleIDMapper<Notification, NotificationDTO> {

	@Inject
	private NotificationRepository notifRepo;

	@Override
	public abstract NotificationDTO entityToDTO(Notification entity);

	@Override
	public GenericRepostiory<Notification, Long> getRepo() {
		return notifRepo;
	}

}
