package com.springboot.restapi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<?> getAllFiles() {
		List<FileMetaData> resultList = null;
		try {
			resultList = repo.findAll();
		} catch (Exception e) {
			return new ResponseEntity<String>(
					"Exception: getAllFiles has failed.",
					HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Collection<FileMetaData>>(resultList,
				HttpStatus.OK);
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
	public ResponseEntity<?> getFileMetaData(@PathVariable Integer id) {
		FileMetaData fileMetaData = repo.findOne(id);

		if (fileMetaData != null) {
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

					return new ResponseEntity<FileMetaData>(fileMetaData,
							HttpStatus.OK);
				}

			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<String>(
						"IOException: Get file meta-data process has failed.",
						HttpStatus.BAD_REQUEST);
			}

			catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<String>(
						"Exception: Request has failed.",
						HttpStatus.BAD_REQUEST);
			}
		}

		return new ResponseEntity<String>("No FileMetaData found with id: "
				+ id, HttpStatus.BAD_REQUEST);
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
	public ResponseEntity<?> uploadFile(
			@RequestParam("file") MultipartFile file,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "descr", required = false) String descr) {

		if (file.isEmpty()) {
			return new ResponseEntity<Object>(
					"Please select a file to upload.", HttpStatus.BAD_REQUEST);
		}

		String fileName = StringUtils.isEmpty(name) ? file
				.getOriginalFilename() : name;

		try {

			if (!StringUtils.isEmpty(fileName)) {
				List<FileMetaData> metaDataList = repo.findByName(fileName);
				if (metaDataList != null && metaDataList.size() > 0) {
					return new ResponseEntity<Object>(
							"Duplicate file name - FileMetaData found for the given name: "
									+ fileName, HttpStatus.BAD_REQUEST);
				}
			}

			fileUploadUtil.saveFileToDisk(file, fileName);

			FileMetaData fileMetaData = fileUploadUtil.createFileMetaDataFrom(
					fileName, descr, file);
			repo.saveAndFlush(fileMetaData);
		}

		catch (Exception e) {
			return new ResponseEntity<Object>(e.getClass()
					+ ": File upload process has failed for the file "
					+ fileName, HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<Object>(fileName + " successfully uploaded",
				new HttpHeaders(), HttpStatus.OK);
	}

	/**
	 * This controller method is for downloading the content of the file saved
	 * in FileMetaData table with the given id
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> downloadFile(@PathVariable("id") Integer id) {
		FileMetaData fileMetaData = repo.findOne(id);
		if (fileMetaData != null) {
			try {
				Path path = Paths.get(fileMetaData.getPath());
				byte[] data = Files.readAllBytes(path);

				Files.write(
						Paths.get("src/main/resources/DownloadRepository/"
								+ fileMetaData.getName()), data);

				return new ResponseEntity<Object>(
						"File successfully downloaded.", new HttpHeaders(),
						HttpStatus.OK);
			}

			catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<Object>(e.getClass()
						+ ": Download file process has failed",
						HttpStatus.BAD_REQUEST);
			}
		}

		return new ResponseEntity<Object>("File not found to download",
				HttpStatus.BAD_REQUEST);
	}

}
