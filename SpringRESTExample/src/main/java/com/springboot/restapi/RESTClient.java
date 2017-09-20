package com.springboot.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component("restClient")
public class RESTClient {
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * REST Client to make the request call "http://localhost:8080/files"
	 *
	 * @return
	 */
	public ResponseEntity<String> getAllFiles() {
		ResponseEntity<String> response = restTemplate.getForEntity(
				"http://localhost:8080/files", String.class);
		System.out.println("http://localhost:8080/files" + " ---> "
				+ response.toString());
		return response;
	}

	/**
	 * REST Client to make the request call "http://localhost:8080/searchFile"
	 * with the related parameters
	 *
	 * @param fileMetaDataSearchCriteria
	 * @return
	 */
	public ResponseEntity<String> searchFiles(
			FileMetaDataSearchCriteria fileMetaDataSearchCriteria) {
		HttpEntity<FileMetaDataSearchCriteria> request = new HttpEntity<>(
				fileMetaDataSearchCriteria);
		ResponseEntity<String> response = restTemplate.postForEntity(
				"http://localhost:8080/searchFile", request, String.class);
		System.out.println("http://localhost:8080/searchFile"
				+ " Search Criteria: " + fileMetaDataSearchCriteria.toString()
				+ " ---> " + response.toString());
		return response;
	}

	/**
	 * REST Client to make the request call "http://localhost:8080/upload" with
	 * the related parameters
	 *
	 * @param filePath
	 * @param name
	 * @param descr
	 * @return
	 */
	public ResponseEntity<String> uploadFile(String filePath, String name,
			String descr) {
		try {
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("file", new FileSystemResource(filePath));
			parameters.add("name", name);
			parameters.add("descr", descr);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "multipart/form-data");
			headers.set("Accept", "text/plain");

			ResponseEntity<String> response = restTemplate.postForEntity(
					"http://localhost:8080/upload",
					new HttpEntity<MultiValueMap<String, Object>>(parameters,
							headers), String.class);
			System.out.println("http://localhost:8080/upload" + " file: "
					+ filePath + ", name: " + name + ", descr: " + descr
					+ " ---> " + response.toString());
			return response;
		} catch (Exception e) {
			ResponseEntity<String> excResponse = new ResponseEntity<String>(
					"uploadFile('" + filePath + "', '" + name + "', '" + descr
							+ "') has failed.", HttpStatus.BAD_REQUEST);
			System.out.println("http://localhost:8080/upload" + " file: "
					+ filePath + ", name: " + name + ", descr: " + descr
					+ " ---> " + excResponse.toString());
			return excResponse;
		}
	}

	/**
	 * REST Client to make the request call
	 * "http://localhost:8080/metadata/{id}"
	 *
	 * @param id
	 * @return
	 */
	public ResponseEntity<String> getFileMetaDataById(Integer id) {
		try {
			ResponseEntity<String> response = restTemplate.getForEntity(
					"http://localhost:8080/metadata/" + id, String.class);
			System.out.println("http://localhost:8080/metadata/" + id
					+ " ---> " + response.toString());

			return response;
		} catch (Exception e) {
			ResponseEntity<String> excResponse = new ResponseEntity<String>(
					"getFileMetaDataById(" + id + ") has failed.",
					HttpStatus.BAD_REQUEST);
			System.out.println("http://localhost:8080/metadata/" + id
					+ " ---> " + excResponse.toString());
			return excResponse;
		}
	}

	/**
	 * REST Client to make the request call
	 * "http://localhost:8080/download/{id}"
	 *
	 * @param id
	 * @return
	 */
	public ResponseEntity<String> downloadFile(Integer id) {
		try {
			restTemplate.getMessageConverters().add(
					new ByteArrayHttpMessageConverter());

			ResponseEntity<String> response = restTemplate.getForEntity(
					"http://localhost:8080/download/" + id, String.class);
			System.out.println("http://localhost:8080/download/" + id
					+ " ---> " + response.toString());
			return response;
		}

		catch (Exception e) {
			ResponseEntity<String> excResponse = new ResponseEntity<String>("downloadFile(" + id
					+ ") has failed.", HttpStatus.BAD_REQUEST);
			System.out.println("http://localhost:8080/download/" + id
					+ " ---> " + excResponse.toString());
			return excResponse;
		}
	}
}
