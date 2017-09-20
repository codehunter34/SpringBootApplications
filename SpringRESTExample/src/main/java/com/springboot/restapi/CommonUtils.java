package com.springboot.restapi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonUtils {

	@Value("${run_scheduled_jobs}")
	private String runScheduleJobs;

	@Value("${from_address}")
	private String mailFromAddress;

	@Value("${to_address}")
	private String mailToAddress;

	@Value("${reply_to_address}")
	private String replyToAddress;

	/**
	 * This method is to convert a JSONArray to JSONObject list
	 *
	 * @param array
	 * @return
	 * @throws JSONException
	 */
	public List<JSONObject> getJSONObjectListFromJSONArray(JSONArray array)
			throws JSONException {
		ArrayList<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		for (int i = 0; i < (array != null ? array.length() : 0); jsonObjects
				.add(array.getJSONObject(i++)))
			;
		return jsonObjects;
	}

	/**
	 * Returns whether or not a scheduled job is set to run based on the
	 * property value "run_scheduled_jobs"
	 *
	 * @return
	 */
	public boolean runScheduledJob() {
		return Boolean.parseBoolean(runScheduleJobs);
	}

	/**
	 * Returns the to_address entry in defined application.properties
	 *
	 * @return
	 */
	public String getMailToAddress() {
		return mailToAddress;
	}

	/**
	 * Returns the reply_to_address in defined application.properties
	 *
	 * @return
	 */
	public String getMailReplyToAddress() {
		return replyToAddress;
	}

	/**
	 * Returns the from_address in defined application.properties
	 *
	 * @return
	 */
	public String getMailFromAddress() {
		return mailFromAddress;
	}

	/**
	 * This method is to create an Email object from a list of FileMetaData .
	 *
	 * @param fileMetaDatas
	 * @return
	 */
	public Email generateEmailFrom(List<FileMetaData> fileMetaDatas) {
		Email email = new Email();
		if (fileMetaDatas == null || fileMetaDatas.size() == 0) {
			email.setSubject("No FileMetaData records added in last one hour");
		} else {
			email.setSubject("New FileMetaData records added in last one hour: "
					+ fileMetaDatas.size());
		}
		email.setFromAddress(getMailFromAddress());
		email.setToAddresses(getMailToAddress());
		email.setReplyTo(getMailReplyToAddress());

		StringBuilder body = new StringBuilder();

		for (FileMetaData fileMetaData : fileMetaDatas) {
			body.append("<p>");
			body.append(fileMetaData.toString());
			body.append("</p>");
		}
		email.setBody(body.toString());
		return email;
	}
}
