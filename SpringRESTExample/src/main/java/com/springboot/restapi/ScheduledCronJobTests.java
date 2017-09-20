package com.springboot.restapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ScheduledCronJobTests {

	@Autowired
	private ScheduledCronJobs scheduledCronJobs;


	@Test
	public void pollForNewItemsTest(){
		scheduledCronJobs.pollForNewItems();
	}
}
