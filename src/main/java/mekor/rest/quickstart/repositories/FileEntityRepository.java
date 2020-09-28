/**
 * 
 */
package mekor.rest.quickstart.repositories;

import javax.enterprise.context.RequestScoped;

import org.apache.deltaspike.data.api.Repository;

import com.querydsl.core.types.dsl.EntityPathBase;

import mekor.rest.quickstart.model.entities.file.FileEntity;
import mekor.rest.quickstart.model.entities.file.QFileEntity;
import mekor.rest.quickstart.repositories.utils.GenericRepostiory;

/**
 * 
 * Repository for the {@link FileEntity} entity
 * 
 * @author mekor
 *
 */
@Repository
@RequestScoped
public abstract class FileEntityRepository extends GenericRepostiory<FileEntity, Long> {

	@Override
	protected EntityPathBase<FileEntity> getQBean() {
		return QFileEntity.fileEntity;
	}

}
