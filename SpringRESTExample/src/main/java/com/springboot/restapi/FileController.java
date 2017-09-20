package com.springboot.restapi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

	@Autowired
	private FileRepository repo;

	@Autowired
	private FileUploadUtil fileUploadUtil;

	@Autowired
	private FileMetaDataDao fileMetaDataDao;

	/**
	 * This controller method is for fetching all uploaded files in the system
	 *
	 * @return
	 */
	@RequestMapping(value = "/files", method = RequestMethod.GET)
	public List<FileMetaData> getAllFiles() {
		return repo.findAll();
	}

	/**
	 * This controller method is for searching all uploaded files with a search
	 * criteria
	 *
	 * @param searchCriteria
	 * @return
	 */
	@RequestMapping(value = "/searchFile", method = RequestMethod.POST)
	public List<FileMetaData> searchFiles(
			@RequestBody FileMetaDataSearchCriteria searchCriteria) {
		return fileMetaDataDao.searchFileMetaData(searchCriteria);
	}

	/**
	 * This controller method is for finding a FileMetaData entity with the
	 * given id
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/metadata/{id}", method = RequestMethod.GET)
	public FileMetaData getFileMetaData(@PathVariable Integer id) {
		FileMetaData fileMetaData = repo.findOne(id);

		if (fileMetaData != null) {
			/**
			 * The reason I update some metadata fileds here is that since we
			 * are storing the file content in disk, I thought it might be
			 * helpful to update the last update and last access times and some
			 * other fields when we make a request to get a metadata by id.
			 *
			 * But honestly, I think we can do the same thing by a scheduled job
			 * which runs every 15 minutes to update some attributes of the
			 * related FileMetaData according to the metadata of the file on
			 * disk
			 */
			try {
				Path path = Paths.get(fileMetaData.getPath());
				BasicFileAttributes attr = Files.readAttributes(path,
						BasicFileAttributes.class);
				if (attr != null) {
					fileMetaData.setLastUpdatedDate(fileUploadUtil
							.createLocalDateTimeFrom(attr.lastModifiedTime()));
					fileMetaData.setLastAccessDate(fileUploadUtil
							.createLocalDateTimeFrom(attr.lastAccessTime()));
					fileMetaData.setDirectory(attr.isDirectory());
					fileMetaData.setOther(attr.isOther());
					fileMetaData.setRegularFile(attr.isRegularFile());
					fileMetaData.setSymbolicLink(attr.isSymbolicLink());
					fileMetaData.setSize(attr.size());
					repo.saveAndFlush(fileMetaData);
					return fileMetaData;
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * This controller method is for uploading the given file with the given
	 * meta data information
	 *
	 * @param file
	 * @param name
	 * @param descr
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = "multipart/form-data")
	public String uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "descr", required = false) String descr) {

		if (file.isEmpty()) {
			return "Empty file: Please select a file to upload.";
		}

		String fileName = StringUtils.isEmpty(name) ? file
				.getOriginalFilename() : name;

		if (!fileUploadUtil.isFilenameValid(fileName)) {
			return "Invalid file name : " + fileName;
		}

		try {
			if (!StringUtils.isEmpty(fileName)) {
				List<FileMetaData> metaDataList = repo.findByName(fileName);
				if (metaDataList != null && metaDataList.size() > 0) {
					return "Duplicate file name: An existing FileMetaData found for the given name "
							+ fileName;
				}
			}

			fileUploadUtil.saveFileToDisk(file, fileName);

			FileMetaData fileMetaData = fileUploadUtil.createFileMetaDataFrom(
					fileName, descr, file);
			repo.saveAndFlush(fileMetaData);
		}

		catch (Exception e) {
			return "Failed to upload " + fileName + " -- Exception: "
					+ e.toString();
		}

		return "You have successfully uploaded " + fileName;
	}

	/**
	 * This controller method is for downloading the content of the file saved
	 * in FileMetaData table with the given id
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
	public String downloadFile(@PathVariable("id") Integer id) {
		FileMetaData fileMetaData = repo.findOne(id);
		if (fileMetaData != null) {
			try {
				Path path = Paths.get(fileMetaData.getPath());
				byte[] data = Files.readAllBytes(path);

				Files.write(
						Paths.get("src/main/resources/DownloadRepository/"
								+ fileMetaData.getName()), data);

				return "You have successfully downloaded the file ID: " + id
						+ ", Name: " + fileMetaData.getName();
			}

			catch (Exception e) {
				e.printStackTrace();
				return "Failed to download the file " + id + " --- Exception: "
						+ e.toString();
			}
		}

		return "No FileMetaData found with ID: " + id;
	}

}
