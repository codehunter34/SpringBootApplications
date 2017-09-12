package com.springboot.restapi;

import java.util.List;

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
	public String getAllFiles() {
		String result = restTemplate.getForObject(
				"http://localhost:8080/files", String.class);

		return result;
	}

	/**
	 * REST Client to make the request call "http://localhost:8080/searchFile"
	 * with the related parameters
	 *
	 * @param fileMetaDataSearchCriteria
	 * @return
	 */
	public String searchFiles(
			FileMetaDataSearchCriteria fileMetaDataSearchCriteria) {
		HttpEntity<FileMetaDataSearchCriteria> request = new HttpEntity<>(
				fileMetaDataSearchCriteria);
		return restTemplate.postForObject("http://localhost:8080/searchFile",
				request, List.class).toString();
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
	public String uploadFile(String filePath, String name, String descr) {
		try {
			MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
			parameters.add("file", new FileSystemResource(filePath));
			parameters.add("name", name);
			parameters.add("descr", descr);

			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", "multipart/form-data");
			headers.set("Accept", "text/plain");

			String result = restTemplate.postForObject(
					"http://localhost:8080/upload",
					new HttpEntity<MultiValueMap<String, Object>>(parameters,
							headers), String.class);

			return result;
		} catch (Exception e) {
			return new ResponseEntity<Object>("uploadFile('" + filePath
					+ "', '" + name + "', '" + descr + "') has failed.",
					HttpStatus.BAD_REQUEST).toString();
		}
	}

	/**
	 * REST Client to make the request call
	 * "http://localhost:8080/metadata/{id}"
	 *
	 * @param id
	 * @return
	 */
	public String getFileMetaDataById(Integer id) {
		try {
			String result = restTemplate.getForObject(
					"http://localhost:8080/metadata/" + id, String.class);

			return result;
		} catch (Exception e) {
			return new ResponseEntity<Object>("getFileMetaDataById(" + id
					+ ") has failed.", HttpStatus.BAD_REQUEST).toString();

		}
	}

	/**
	 * REST Client to make the request call
	 * "http://localhost:8080/download/{id}"
	 *
	 * @param id
	 * @return
	 */
	public String downloadFile(Integer id) {
		try {
			restTemplate.getMessageConverters().add(
					new ByteArrayHttpMessageConverter());

			return restTemplate.getForObject("http://localhost:8080/download/"
					+ id, String.class);
		}

		catch (Exception e) {
			return new ResponseEntity<Object>("downloadFile(" + id
					+ ") has failed.", HttpStatus.BAD_REQUEST).toString();
		}
	}
}
