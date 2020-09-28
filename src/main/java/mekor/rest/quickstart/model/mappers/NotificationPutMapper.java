/**
 * 
 */
package mekor.rest.quickstart.model.mappers;

import javax.inject.Inject;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import mekor.rest.quickstart.exceptions.MappingException;
import mekor.rest.quickstart.model.dtos.notification.NotificationPutDTO;
import mekor.rest.quickstart.model.entities.notification.Notification;
import mekor.rest.quickstart.model.mappers.utils.SingleIDMapper;
import mekor.rest.quickstart.model.mappers.utils.SingleIDMapperToEntity;
import mekor.rest.quickstart.repositories.NotificationRepository;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;

/**
 * @author mekor
 *
 */
@Mapper
public abstract class NotificationPutMapper implements SingleIDMapperToEntity<Notification, NotificationPutDTO>, SingleIDMapper<Notification, NotificationPutDTO> {

	@Inject
	private NotificationRepository notifRepo;

	@Override
	@Mapping(target = "sender", ignore = true)
	@Mapping(target = "title", ignore = true)
	@Mapping(target = "redirection", ignore = true)
	@Mapping(target = "type", ignore = true)
	@Mapping(target = "user", ignore = true)
	public abstract Notification DTOToEntity(NotificationPutDTO dto) throws MappingException;

	@Override
	public GenericRepostiory<Notification, Long> getRepo() {
		return notifRepo;
	}

}
