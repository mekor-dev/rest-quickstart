/**
 * 
 */
package mekor.rest.quickstart.mappers;

import javax.inject.Inject;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.runner.RunWith;

import mekor.rest.quickstart.mappers.utils.TestGenericMapper;
import mekor.rest.quickstart.model.dtos.file.FileEntityDTO;
import mekor.rest.quickstart.model.dtos.file.FileTagDTO;
import mekor.rest.quickstart.model.entities.file.FileEntity;
import mekor.rest.quickstart.model.entities.file.FileType;
import mekor.rest.quickstart.model.mappers.FileEntityMapper;
import mekor.rest.quickstart.repositories.FileEntityRepository;

/**
 * @author mekor
 *
 */
@RunWith(Arquillian.class)
public class TestFileEntityMapper extends TestGenericMapper<FileEntity, FileEntityDTO> {

	@Inject
	private FileEntityRepository fileRepo;

	@Inject
	private FileEntityMapper fileMapper;

	@Override
	public boolean mustTestEntityToDTO() {
		return true;
	}

	@Override
	public FileEntity findEntity() {
		return fileRepo.findBy(1L);
	}

	@Override
	public void assertDTO(FileEntityDTO dto) {
		Assert.assertEquals(Long.valueOf(1), dto.id);
		Assert.assertEquals(FileType.IMAGE, dto.type);

		Assert.assertEquals(2, dto.tags.size());

		int found = 0;
		for (FileTagDTO tag : dto.tags) {
			if (tag.id.equals(1L)) {
				found++;
				Assert.assertEquals("tag1", tag.value);
			}
			if (tag.id.equals(2L)) {
				found++;
				Assert.assertEquals("tag2", tag.value);
			}
		}
		Assert.assertEquals(2, found);
	}

	@Override
	public Object getMapper() {
		return fileMapper;
	}

	@Override
	public boolean mustTestDTOToEntity() {
		return false;
	}

	@Override
	public FileEntityDTO createDTO() {
		return null;
	}

	@Override
	public void assertEntity(FileEntity entity) {

	}

	@Override
	public Class<FileEntityDTO> getDTOClass() {
		return FileEntityDTO.class;
	}

}
