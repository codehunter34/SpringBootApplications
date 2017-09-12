package com.springboot.restapi;

import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.scheduling.annotation.Scheduled;

public class ScheduledCronJobs {

	private AtomicBoolean isRunning = new AtomicBoolean(false);

	/**
	 * Scheduled job to poll for new items in the last hour and send an email
	 */
	@Scheduled(cron = "0 0/60 * * *")
	public void pollForNewItems() {

		if (isRunning.get()) {
			return;
		}

		try {
			isRunning.set(true);
			/**
			 * Add the business logic here to fetch the items in the last hour
			 */
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			isRunning.set(false);
			/**
			 * Send email if there are any new items added in the last hour
			 */
		}
	}

}
