package com.springboot.restapi;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This class can be turned into an entity to store all email notifications in
 * database. In that case, we will need to add more columns like sent_status,
 * created_date, sent_date etc.Also we can write a separate scheduler to poll
 * the emails that are waiting to be sent.
 */
public class Email {
	private String fromAddress;
	private String replyTo;
	private String toAddresses;
	private String ccAddresses;
	private String bccAddresses;
	private String subject;
	private String body;

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String getToAddresses() {
		return toAddresses;
	}

	public void setToAddresses(String toAddresses) {
		this.toAddresses = toAddresses;
	}

	public String getCcAddresses() {
		return ccAddresses;
	}

	public void setCcAddresses(String ccAddresses) {
		this.ccAddresses = ccAddresses;
	}

	public String getBccAddresses() {
		return bccAddresses;
	}

	public void setBccAddresses(String bccAddresses) {
		this.bccAddresses = bccAddresses;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("fromAddress", fromAddress).append("replyTo", replyTo)
				.append("toAddresses", toAddresses)
				.append("ccAddresses", ccAddresses)
				.append("bccAddresses", bccAddresses)
				.append("subject", subject).append("body", body).toString();
	}
}