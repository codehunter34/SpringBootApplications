package com.springboot.restapi;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * Utility to send email using JavaMailSender.
 */
@Component
public class EmailSender {

	private static final Logger logger = LoggerFactory
			.getLogger(EmailSender.class);

	private static final char EMAIL_ADDR_SEPERATOR = ';';

	@Inject
	private JavaMailSender javaMailSender;

	/**
	 * Sends email using JavaMailSender.
	 *
	 * @param email
	 *            the email
	 */
	public void sendEmail(Email email) {
		if (logger.isTraceEnabled()) {
			logger.trace("sendEmail(Email) - start");
		}

		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper helper;

			helper = new MimeMessageHelper(mimeMessage, true);

			prepareMimeMessageHelper(helper, email);
			javaMailSender.send(mimeMessage);
			logger.info("Email with subject: {} sent successfully",
					email.getSubject());
		} catch (MessagingException e) {
			logger.error("sendEmail(Email)", e);
		}

		if (logger.isTraceEnabled()) {
			logger.trace("sendEmail(Email) - end");
		}
	}

	/**
	 * Prepare mime message.
	 *
	 * @param helper
	 *            the MimeMessageHelper
	 * @param email
	 *            the email
	 * @throws MessagingException
	 *             the messaging exception
	 */
	private void prepareMimeMessageHelper(MimeMessageHelper helper, Email email)
			throws MessagingException {
		if (logger.isTraceEnabled()) {
			logger.trace("prepareMimeMessageHelper(MimeMessageHelper, Email) - start");
		}

		String[] toAddresses = getEmailAddressesArray(email.getToAddresses());
		String[] ccAddresses = getEmailAddressesArray(email.getCcAddresses());
		String[] bccAddresses = getEmailAddressesArray(email.getBccAddresses());
		String subject = email.getSubject();
		String body = email.getBody();

		helper.setFrom(email.getFromAddress());
		helper.setTo(toAddresses);
		helper.setReplyTo(email.getReplyTo());

		if (!ArrayUtils.isEmpty(ccAddresses)) {
			helper.setCc(ccAddresses);
		}

		if (!ArrayUtils.isEmpty(bccAddresses)) {
			helper.setBcc(bccAddresses);
		}

		helper.setSubject(subject);
		/**
		 * Send as html
		 */
		helper.setText(body, true);

		if (logger.isTraceEnabled()) {
			logger.trace("prepareMimeMessageHelper(MimeMessageHelper, Email) - end");
		}
	}

	/**
	 * Gets the email addresses.
	 *
	 * @param toAddresses
	 *            the to addresses
	 * @return the email addresses array
	 */
	private String[] getEmailAddressesArray(String toAddresses) {
		if (logger.isTraceEnabled()) {
			logger.trace("getEmailAddressesArray(String) - start");
		}

		String[] emailAddresses = StringUtils.split(toAddresses,
				EMAIL_ADDR_SEPERATOR);

		if (logger.isTraceEnabled()) {
			logger.trace("getEmailAddressesArray(String) - end");
		}
		return emailAddresses;
	}

}
