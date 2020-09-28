/**
 * 
 */
package mekor.rest.quickstart.mappers;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.runner.RunWith;

import mekor.rest.quickstart.mappers.utils.TestGenericMapper;
import mekor.rest.quickstart.model.dtos.file.FileTagDTO;
import mekor.rest.quickstart.model.entities.file.FileTag;
import mekor.rest.quickstart.model.mappers.FileTagMapper;
import mekor.rest.quickstart.repositories.FileTagRepository;

/**
 * @author mekor
 *
 */
@RunWith(Arquillian.class)
public class TestFileTagMapper extends TestGenericMapper<FileTag, FileTagDTO> {

	@Inject
	private FileTagRepository fileTagRepo;

	@Inject
	private FileTagMapper fileTagMapper;

	@Override
	public boolean mustTestEntityToDTO() {
		return true;
	}

	@Override
	public FileTag findEntity() {
		return fileTagRepo.findBy(1L);
	}

	@Override
	public void assertDTO(FileTagDTO dto) {
		Assert.assertEquals(Long.valueOf(1), dto.id);
		Assert.assertEquals("tag1", dto.value);

	}

	@Override
	public Object getMapper() {
		return fileTagMapper;
	}

	@Override
	public boolean mustTestDTOToEntity() {
		return false;
	}

	@Override
	public FileTagDTO createDTO() {
		return null;
	}

	@Override
	public void assertEntity(FileTag entity) {

	}

	@Override
	public Class<FileTagDTO> getDTOClass() {
		return FileTagDTO.class;
	}

}
