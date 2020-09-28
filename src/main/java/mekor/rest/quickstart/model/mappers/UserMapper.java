package mekor.rest.quickstart.model.mappers;

import javax.inject.Inject;

import org.mapstruct.Mapper;

import mekor.rest.quickstart.model.dtos.user.UserPrivateDTO;
import mekor.rest.quickstart.model.entities.user.User;
import mekor.rest.quickstart.model.mappers.utils.SingleIDMapper;
import mekor.rest.quickstart.model.mappers.utils.SingleIDMapperToDTO;
import mekor.rest.quickstart.repositories.UserRepository;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;

/**
 * Maps {@link User} to {@link UserPrivateDTO}
 * 
 * @author mekor
 *
 */
@Mapper(uses = { UserConfigurationMapper.class })
public abstract class UserMapper implements SingleIDMapperToDTO<User, UserPrivateDTO>, SingleIDMapper<User, UserPrivateDTO> {

	@Inject
	private UserRepository userRepo;

	@Override
	public abstract UserPrivateDTO entityToDTO(User entity);

	@Override
	public GenericRepostiory<User, Long> getRepo() {
		return userRepo;
	}

}
