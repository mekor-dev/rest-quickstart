/**
 * 
 */
package mekor.rest.quickstart.mappers.utils;

import static org.junit.Assume.assumeTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mekor.rest.quickstart.configuration.ArquillianTest;
import mekor.rest.quickstart.exceptions.MappingException;
import mekor.rest.quickstart.model.dtos.utils.GenericDTO;
import mekor.rest.quickstart.model.entities.utils.GenericEntity;
import mekor.rest.quickstart.model.mappers.utils.GenericMapperToDTO;
import mekor.rest.quickstart.model.mappers.utils.GenericMapperToEntity;

/**
 * @author mekor
 *
 */
public abstract class TestGenericMapper<E extends GenericEntity, DTO extends GenericDTO> extends ArquillianTest {

	private static Logger log = LoggerFactory.getLogger(TestGenericMapper.class);

	@SuppressWarnings("unchecked")
	@Test
	public void testEntityToDTO() {
		assumeTrue(mustTestEntityToDTO());
		GenericMapperToDTO<E, DTO> mapper = (GenericMapperToDTO<E, DTO>) getMapper();
		E entity = findEntity();
		log.debug("ENTITY : " + entity);
		DTO dto = mapper.entityToDTO(entity);
		log.debug("DTO : " + dto);
		assertDTO(dto);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDTOToEntity() throws MappingException {
		assumeTrue(mustTestDTOToEntity());
		GenericMapperToEntity<E, DTO> mapper = (GenericMapperToEntity<E, DTO>) getMapper();
		DTO dto = createDTO();
		log.debug("DTO : " + dto);
		E entity = mapper.DTOToEntity(dto);
		log.debug("ENTITY : " + entity);
		assertEntity(entity);

	}

	public abstract boolean mustTestEntityToDTO();

	public abstract E findEntity();

	public abstract void assertDTO(DTO dto);

	public abstract boolean mustTestDTOToEntity();

	public abstract DTO createDTO();

	public abstract void assertEntity(E entity);

	public abstract Object getMapper();

	public abstract Class<DTO> getDTOClass();

}
