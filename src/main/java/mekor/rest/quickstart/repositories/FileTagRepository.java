/**
 * 
 */
package mekor.rest.quickstart.repositories;

import javax.enterprise.context.RequestScoped;

import org.apache.deltaspike.data.api.Repository;

import com.querydsl.core.types.dsl.EntityPathBase;

import mekor.rest.quickstart.model.entities.file.FileTag;
import mekor.rest.quickstart.model.entities.file.QFileTag;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;

/**
 * 
 * Repository for the {@link FileTag} entity
 * 
 * @author mekor
 *
 */
@Repository
@RequestScoped
public abstract class FileTagRepository extends GenericRepostiory<FileTag, Long> {

	@Override
	protected EntityPathBase<FileTag> getQBean() {
		return QFileTag.fileTag;
	}

}
