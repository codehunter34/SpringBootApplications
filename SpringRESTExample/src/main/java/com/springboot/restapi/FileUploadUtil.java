package com.springboot.restapi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component("fileUploadUtil")
public class FileUploadUtil {

	/**
	 * Contents of uploaded files will be saved in this folder
	 */
	private static String DEFAULT_UPLOADED_FOLDER = "C://UploadRepo//";

	public void createUploadRepository() {
		File file = new File(DEFAULT_UPLOADED_FOLDER);
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Default upload directory "
						+ DEFAULT_UPLOADED_FOLDER + " is created!");
			} else {
				System.out
						.println("Failed to create the default upload directory "
								+ DEFAULT_UPLOADED_FOLDER + "!");
			}
		}

		System.out
				.println("Please see the 'C:' directory for the new created UploadRepo folder that will be used as the default upload repository.");

	}

	/**
	 * This method is for writing the file content to the disk
	 *
	 * @param file
	 * @param name
	 * @throws IOException
	 */
	public void saveFileToDisk(MultipartFile file, String name)
			throws IOException {
		if (!file.isEmpty()) {
			byte[] bytes = file.getBytes();

			String fileName = StringUtils.isEmpty(name) ? file
					.getOriginalFilename() : name;

			String filePath = DEFAULT_UPLOADED_FOLDER + fileName;

			Path path = Paths.get(filePath);
			Files.write(path, bytes);
		}
	}

	/**
	 * This helper method is for creating a FileMetaData object from the given
	 * parameters
	 *
	 * @param name
	 * @param descr
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 * @throws SQLException
	 */
	public FileMetaData createFileMetaDataFrom(String name, String descr,
			MultipartFile multipartFile) throws IOException, SQLException {

		String fileName = StringUtils.isEmpty(name) ? multipartFile
				.getOriginalFilename() : name;

		String filePath = DEFAULT_UPLOADED_FOLDER + fileName;

		Path path = Paths.get(filePath);

		FileMetaData fileMetaData = new FileMetaData();
		fileMetaData.setName(fileName);
		fileMetaData.setDescription(descr);
		fileMetaData.setContentType(multipartFile.getContentType());
		fileMetaData.setCreatedDate(LocalDateTime.now());
		fileMetaData.setPath(DEFAULT_UPLOADED_FOLDER + fileName);
		fileMetaData.setOriginalName(multipartFile.getOriginalFilename());
		fileMetaData.setSize(multipartFile.getSize());

		BasicFileAttributes attr = Files.readAttributes(path,
				BasicFileAttributes.class);

		if (attr != null) {
			fileMetaData.setLastUpdatedDate(createLocalDateTimeFrom(attr
					.lastModifiedTime()));
			fileMetaData.setLastAccessDate(createLocalDateTimeFrom(attr
					.lastAccessTime()));
			fileMetaData.setDirectory(attr.isDirectory());
			fileMetaData.setOther(attr.isOther());
			fileMetaData.setRegularFile(attr.isRegularFile());
			fileMetaData.setSymbolicLink(attr.isSymbolicLink());
		}

		return fileMetaData;
	}

	public static LocalDateTime createLocalDateTimeFrom(FileTime fileTime) {
		if (fileTime != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(fileTime.toMillis());

			LocalDateTime ofInstant = LocalDateTime.ofInstant(cal.toInstant(),
					ZoneId.systemDefault());

			return ofInstant;
		}
		return null;
	}

}
