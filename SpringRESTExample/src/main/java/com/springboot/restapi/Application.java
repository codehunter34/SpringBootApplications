package com.springboot.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class,
				args);

		/**
		 * This method call will create a the folder "C://UploadRepo". This
		 * folder will be used to save the content of uploaded files.
		 */
		((FileUploadUtil) context.getBean("fileUploadUtil"))
				.createUploadRepository();

		System.out.println(((RESTClient) context.getBean("restClient"))
				.getAllFiles());

		System.out.println(((RESTClient) context.getBean("restClient"))
				.uploadFile(
						"src/main/resources/UploadFromRepository/TextFile.txt",
						null, null));

		System.out.println(((RESTClient) context.getBean("restClient"))
				.getAllFiles());

		System.out.println(((RESTClient) context.getBean("restClient"))
				.uploadFile(
						"src/main/resources/UploadFromRepository/TextFile.txt",
						"TextFile_1.txt", null));

		System.out.println(((RESTClient) context.getBean("restClient"))
				.uploadFile(
						"src/main/resources/UploadFromRepository/TextFile.txt",
						"TextFile_2.txt", "Test"));

		System.out.println(((RESTClient) context.getBean("restClient"))
				.uploadFile("C://FileRepo/TextFileee.txt", "TextFile_3.txt",
						"Test"));

		System.out.println(((RESTClient) context.getBean("restClient"))
				.uploadFile("C://FileRepo///", "TextFile_4.txt", "Test"));

		System.out.println(((RESTClient) context.getBean("restClient"))
				.uploadFile("C://FileRepo/TextFile.txt", "Text//File_5.txt",
						"Test"));

		System.out.println(((RESTClient) context.getBean("restClient"))
				.getAllFiles());

		System.out.println(((RESTClient) context.getBean("restClient"))
				.getFileMetaDataById(1));

		System.out.println(((RESTClient) context.getBean("restClient"))
				.getFileMetaDataById(null));

		System.out.println(((RESTClient) context.getBean("restClient"))
				.getFileMetaDataById(100));

		System.out.println(((RESTClient) context.getBean("restClient"))
				.downloadFile(1));

		System.out.println(((RESTClient) context.getBean("restClient"))
				.downloadFile(null));

		System.out.println(((RESTClient) context.getBean("restClient"))
				.downloadFile(100));

		FileMetaDataSearchCriteria fileMetaDataSearchCriteria = new FileMetaDataSearchCriteria();

		fileMetaDataSearchCriteria.setName("File_1");

		System.out.println(((RESTClient) context.getBean("restClient"))
				.searchFiles(fileMetaDataSearchCriteria));

	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}
