/**
 * 
 */
package mekor.rest.quickstart.model.mappers;

import javax.inject.Inject;

import org.mapstruct.Mapper;

import mekor.rest.quickstart.model.dtos.file.FileTagDTO;
import mekor.rest.quickstart.model.entities.file.FileTag;
import mekor.rest.quickstart.model.mappers.utils.SingleIDMapper;
import mekor.rest.quickstart.model.mappers.utils.SingleIDMapperToDTO;
import mekor.rest.quickstart.repositories.FileTagRepository;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;

/**
 * Map from {@link FileTag} to {@link FileTagDTO} and backwards
 * 
 * @author mekor
 *
 */
@Mapper
public abstract class FileTagMapper implements SingleIDMapperToDTO<FileTag, FileTagDTO>, SingleIDMapper<FileTag, FileTagDTO> {

	@Inject
	private FileTagRepository fileTagRepo;

	@Override
	public abstract FileTagDTO entityToDTO(FileTag entity);

	@Override
	public GenericRepostiory<FileTag, Long> getRepo() {
		return fileTagRepo;
	}
}
