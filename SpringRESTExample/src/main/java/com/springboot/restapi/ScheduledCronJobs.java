package com.springboot.restapi;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledCronJobs {

	private AtomicBoolean isRunning = new AtomicBoolean(false);

	@Autowired
	private FileMetaDataDao fileMetaDataDao;

	@Autowired
	private CommonUtils commonUtils;

	@Autowired
	private EmailSender emailSender;

	/**
	 * Scheduled job to poll for new items in the last hour and send an email
	 *
	 * Please remember that emailSender may give an error due to the SMTP
	 * configuration settings or any network restrictions in the machine that
	 * the code is executed.
	 */
	@Scheduled(cron = "0 0/60 * * *")
	public void pollForNewItems() {
		if (!commonUtils.runScheduledJob()) {
			System.out.println("Not running scheduled jobs on this container.");
			return;
		}

		if (isRunning.get()) {
			System.out
					.println("pollForNewItems cron job process is already running so exiting");
			return;
		}

		List<FileMetaData> fileMetaDataList = null;
		try {
			isRunning.set(true);
			FileMetaDataSearchCriteria fileMetaDataSearchCriteria = new FileMetaDataSearchCriteria();
			LocalDateTime now = LocalDateTime.now();
			LocalDateTime oneHourAgo = now.minusHours(1);
			fileMetaDataSearchCriteria.setCreatedDateFrom(oneHourAgo);
			fileMetaDataSearchCriteria.setCreatedDateTo(now);

			fileMetaDataList = fileMetaDataDao
					.searchFileMetaData(fileMetaDataSearchCriteria);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			isRunning.set(false);
			if (fileMetaDataList != null) {
				Email email = commonUtils.generateEmailFrom(fileMetaDataList);
				emailSender.sendEmail(email);
			}
		}
	}
}
