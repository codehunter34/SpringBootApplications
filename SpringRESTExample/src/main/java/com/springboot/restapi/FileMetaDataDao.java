package com.springboot.restapi;

import java.util.List;

public interface FileMetaDataDao {
	public List<FileMetaData> searchFileMetaData(
			FileMetaDataSearchCriteria fileMetaDataSearchCriteria);
}
