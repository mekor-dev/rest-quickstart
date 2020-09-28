/**
 * 
 */
package mekor.rest.quickstart.model.mappers;

import javax.inject.Inject;

import org.mapstruct.Mapper;

import mekor.rest.quickstart.model.dtos.file.FileEntityDTO;
import mekor.rest.quickstart.model.entities.file.FileEntity;
import mekor.rest.quickstart.model.mappers.utils.SingleIDMapper;
import mekor.rest.quickstart.model.mappers.utils.SingleIDMapperToDTO;
import mekor.rest.quickstart.repositories.FileEntityRepository;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;

/**
 * Map from {@link FileEntity} to {@link FileEntityDTO} and backwards
 * 
 * @author mekor
 *
 */
@Mapper(uses = { FileTagMapper.class })
public abstract class FileEntityMapper implements SingleIDMapperToDTO<FileEntity, FileEntityDTO>, SingleIDMapper<FileEntity, FileEntityDTO> {

	@Inject
	private FileEntityRepository fileRepo;

	@Override
	public abstract FileEntityDTO entityToDTO(FileEntity entity);

	@Override
	public GenericRepostiory<FileEntity, Long> getRepo() {
		return fileRepo;
	}
}
