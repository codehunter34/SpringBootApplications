package com.springboot.restapi;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * RestTemplate tests in Application.java need to be implemented here.
 *
 * The tests are connected to each other. Please make sure you run the whole
 * class(not single methods) for the convenience of tests.
 *
 * Please run RestClientTest.java once for each run on Application.java.
 * Otherwise, the test results will be unexpected due to uploading duplicate
 * name files.
 *
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RestClientTest {

	@Autowired
	private RESTClient restClient;

	@Autowired
	private CommonUtils commonUtils;

	@Test
	public void createFileTests() {
		Assert.assertEquals(restClient.getAllFiles().getBody(), "[]");
		ResponseEntity<String> uploadResponse_1 = restClient.uploadFile(
				"src/main/resources/UploadFromRepository/TextFile.txt", null,
				null);
		ResponseEntity<String> uploadResponse_2 = restClient.uploadFile(
				"src/main/resources/UploadFromRepository/TextFile.txt",
				"TextFile_1.txt", null);
		ResponseEntity<String> uploadResponse_3 = restClient.uploadFile(
				"src/main/resources/UploadFromRepository/TextFile.txt",
				"TextFile_2.txt", "Test");

		ResponseEntity<String> uploadResponse_4 = restClient.uploadFile(
				"src/main/resources/UploadFromRepository/TextFileee.txt",
				"TextFile_3.txt", "Test");

		ResponseEntity<String> uploadResponse_5 = restClient.uploadFile(
				"src/main/resources//UploadFromRepository////",
				"TextFile_4.txt", "Test");

		// Invalid file name
		ResponseEntity<String> uploadResponse_6 = restClient.uploadFile(
				"src/main/resources/UploadFromRepository/TextFile.txt",
				"Text//File_5.txt", "Test");

		// Invalid file name
		ResponseEntity<String> uploadResponse_7 = restClient.uploadFile(
				"src/main/resources/UploadFromRepository/TextFile.txt",
				"Text/File_5.txt", "Test");

		// Invalid file name
		ResponseEntity<String> uploadResponse_8 = restClient.uploadFile(
				"src/main/resources/UploadFromRepository/TextFile.txt",
				"Text\\File_5.txt", "Test");

		// Duplicate file name
		ResponseEntity<String> uploadResponse_9 = restClient.uploadFile(
				"src/main/resources/UploadFromRepository/TextFile.txt",
				"TextFile_2.txt", "Test");

		ResponseEntity<String> uploadResponse_10 = restClient.uploadFile(
				"src/main/resources/UploadFromRepository/TestPDF.pdf",
				"FirstTestPDF.pdf", "Test");

		ResponseEntity<String> uploadResponse_11 = restClient.uploadFile(
				"src/main/resources/UploadFromRepository/TestExcel.xlsx",
				"FirstTestExcel.xlsx", "Test");

		ResponseEntity<String> uploadResponse_12 = restClient.uploadFile(
				"src/main/resources/UploadFromRepository/TestWord.docx",
				"FirstTestWord.docx", "Test");

		Assert.assertThat(uploadResponse_1.getBody(),
				CoreMatchers.containsString("successfully"));
		Assert.assertThat(uploadResponse_2.getBody(),
				CoreMatchers.containsString("successfully"));
		Assert.assertThat(uploadResponse_3.getBody(),
				CoreMatchers.containsString("successfully"));

		Assert.assertEquals(uploadResponse_4.getStatusCode(),
				HttpStatus.BAD_REQUEST);

		Assert.assertEquals(uploadResponse_5.getStatusCode(),
				HttpStatus.BAD_REQUEST);

		Assert.assertThat(uploadResponse_6.getBody(),
				CoreMatchers.containsString("Invalid file name"));

		Assert.assertThat(uploadResponse_7.getBody(),
				CoreMatchers.containsString("Invalid file name"));

		Assert.assertThat(uploadResponse_8.getBody(),
				CoreMatchers.containsString("Invalid file name"));

		Assert.assertThat(uploadResponse_9.getBody(),
				CoreMatchers.containsString("Duplicate"));

		Assert.assertThat(uploadResponse_10.getBody(),
				CoreMatchers.containsString("successfully"));

		Assert.assertThat(uploadResponse_11.getBody(),
				CoreMatchers.containsString("successfully"));

		Assert.assertThat(uploadResponse_12.getBody(),
				CoreMatchers.containsString("successfully"));
	}

	@Test
	public void downloadFileTests() {
		Assert.assertEquals(restClient.downloadFile(null).getStatusCode(),
				HttpStatus.BAD_REQUEST);

		ResponseEntity<String> response = restClient.getAllFiles();

		JSONArray jsonArray = new JSONArray(response.getBody());
		List<JSONObject> jsonObjectList = commonUtils
				.getJSONObjectListFromJSONArray(jsonArray);
		for (JSONObject jsonObject : jsonObjectList) {
			Assert.assertThat(restClient.downloadFile(jsonObject.getInt("id"))
					.getBody(), CoreMatchers.containsString("successfully"));

		}

		Assert.assertThat(restClient.downloadFile(100).getBody(),
				CoreMatchers.startsWith("No FileMetaData"));
	}

	@Test
	public void getFileMetaDataTests() {
		Assert.assertEquals(restClient.getFileMetaDataById(null)
				.getStatusCode(), HttpStatus.BAD_REQUEST);
		Assert.assertEquals(restClient.getFileMetaDataById(1).getStatusCode(),
				HttpStatus.OK);
		Assert.assertEquals(
				restClient.getFileMetaDataById(100).getStatusCode(),
				HttpStatus.OK);
	}

	@Test
	public void getFilesTests() {
		ResponseEntity<String> response_1 = restClient.getAllFiles();
		Assert.assertEquals(response_1.getStatusCode(), HttpStatus.OK);

		JSONArray jsonArray = new JSONArray(response_1.getBody());
		List<JSONObject> jsonObjectList = commonUtils
				.getJSONObjectListFromJSONArray(jsonArray);
		Assert.assertTrue(jsonObjectList.size() != 0);
	}

	@Test
	public void searchFileTests() {
		FileMetaDataSearchCriteria searchCriteria_1 = new FileMetaDataSearchCriteria();
		searchCriteria_1.setName("XXXX");
		ResponseEntity<String> response_1 = restClient
				.searchFiles(searchCriteria_1);

		JSONArray jsonArray_1 = new JSONArray(response_1.getBody());
		List<JSONObject> jsonObjectList_1 = commonUtils
				.getJSONObjectListFromJSONArray(jsonArray_1);

		Assert.assertEquals(response_1.getStatusCode(), HttpStatus.OK);
		Assert.assertTrue(jsonObjectList_1.size() == 0);

		FileMetaDataSearchCriteria searchCriteria_2 = new FileMetaDataSearchCriteria();
		searchCriteria_2.setName("TextFile");
		ResponseEntity<String> response_2 = restClient
				.searchFiles(searchCriteria_2);

		JSONArray jsonArray_2 = new JSONArray(response_2.getBody());
		List<JSONObject> jsonObjectList_2 = commonUtils
				.getJSONObjectListFromJSONArray(jsonArray_2);

		Assert.assertEquals(response_2.getStatusCode(), HttpStatus.OK);
		Assert.assertTrue(jsonObjectList_2.size() > 1);

		FileMetaDataSearchCriteria searchCriteria_3 = new FileMetaDataSearchCriteria();
		searchCriteria_3.setContentType("WEIRD_CONTENT");
		ResponseEntity<String> response_3 = restClient
				.searchFiles(searchCriteria_3);

		JSONArray jsonArray_3 = new JSONArray(response_3.getBody());
		List<JSONObject> jsonObjectList_3 = commonUtils
				.getJSONObjectListFromJSONArray(jsonArray_3);

		Assert.assertEquals(response_3.getStatusCode(), HttpStatus.OK);
		Assert.assertTrue(jsonObjectList_3.size() == 0);

	}
}