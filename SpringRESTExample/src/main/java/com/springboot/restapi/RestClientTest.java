package com.springboot.restapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * RestTemplate tests in Application.java need to be implemented here.
 *
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class RestClientTest {

	@Autowired
	private RESTClient restClient;

	@Test
	public void testFindByName() {
	}
}