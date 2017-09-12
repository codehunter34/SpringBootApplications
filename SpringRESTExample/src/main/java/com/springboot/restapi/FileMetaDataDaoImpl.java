package com.springboot.restapi;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPAQuery;

@Transactional
@Repository
public class FileMetaDataDaoImpl implements FileMetaDataDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<FileMetaData> searchFileMetaData(
			FileMetaDataSearchCriteria fileMetaDataSearchCriteria) {

		JPAQuery query = new JPAQuery(entityManager);
		QFileMetaData fileMetaData_ = QFileMetaData.fileMetaData;

		query = query.from(fileMetaData_);

		if (fileMetaDataSearchCriteria != null) {
			if (StringUtils.isNotEmpty(fileMetaDataSearchCriteria.getName())) {
				query = query.where(fileMetaData_.name
						.containsIgnoreCase(fileMetaDataSearchCriteria
								.getName()));
			}

			if (StringUtils.isNotEmpty(fileMetaDataSearchCriteria.getDescr())) {
				query = query.where(fileMetaData_.description
						.containsIgnoreCase(fileMetaDataSearchCriteria
								.getDescr()));
			}

			if (StringUtils.isNotEmpty(fileMetaDataSearchCriteria.getPath())) {
				query = query.where(fileMetaData_.path
						.containsIgnoreCase(fileMetaDataSearchCriteria
								.getPath()));
			}

			if (StringUtils.isNotEmpty(fileMetaDataSearchCriteria
					.getContentType())) {
				query = query.where(fileMetaData_.contentType
						.containsIgnoreCase(fileMetaDataSearchCriteria
								.getContentType()));
			}

			if (fileMetaDataSearchCriteria.getCreatedDateFrom() != null) {
				query = query
						.where(fileMetaData_.createdDate
								.after(fileMetaDataSearchCriteria
										.getCreatedDateFrom()));
			}

			if (fileMetaDataSearchCriteria.getCreatedDateTo() != null) {
				query = query.where(fileMetaData_.createdDate
						.before(fileMetaDataSearchCriteria.getCreatedDateTo()));
			}

			if (fileMetaDataSearchCriteria.getLastUpdateDateFrom() != null) {
				query = query.where(fileMetaData_.lastUpdatedDate
						.after(fileMetaDataSearchCriteria
								.getLastUpdateDateFrom()));
			}

			if (fileMetaDataSearchCriteria.getLastUpdateDateTo() != null) {
				query = query.where(fileMetaData_.lastUpdatedDate
						.before(fileMetaDataSearchCriteria
								.getLastUpdateDateTo()));
			}

			if (fileMetaDataSearchCriteria.getLastAccessDateFrom() != null) {
				query = query.where(fileMetaData_.lastAccessDate
						.after(fileMetaDataSearchCriteria
								.getLastAccessDateFrom()));
			}

			if (fileMetaDataSearchCriteria.getLastAccessDateTo() != null) {
				query = query.where(fileMetaData_.lastAccessDate
						.before(fileMetaDataSearchCriteria
								.getLastAccessDateTo()));
			}
		}

		return query.list(fileMetaData_);
	}

}
