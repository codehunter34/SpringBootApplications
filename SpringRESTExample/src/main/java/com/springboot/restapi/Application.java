package com.springboot.restapi;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Application.class,
				args);

		/**
		 * This method call will create the folder "C://UploadRepo". This folder
		 * will be used to save the content of the uploaded files.
		 */
		((FileUploadUtil) context.getBean("fileUploadUtil"))
				.createUploadRepository();

		System.out
				.println("Important: For testing purposes, I assume that files in 'C://UploadRepo' and 'C://UploadRepo' folder itself will not be removed manually while the application is running.");

		System.out
				.println("Important: Please run RestClientTest.java to run the unit tests. The tests are connected to each other. Please make sure you run the whole class(not single methods) for the convenience of tests.");

		System.out
				.println("Important: Please run RestClientTest.java once for each run on Application.java. Otherwise, the test results will be unexpected due to uploading duplicate name files.");
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	/**
	 * This part may need update due to the network settings in the machine that
	 * code is executed.
	 *
	 * @return
	 */
	@Bean
	public JavaMailSenderImpl createJavaMailSender() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost("smtp.gmail.com");
		javaMailSender.setPort(587);
		Properties properties = new Properties();
		properties.put("spring.mail.username", "*****");
		properties.put("spring.mail.password", "*****");

		properties.put("spring.mail.properties.mail.smtp.auth", true);
		properties
				.put("spring.mail.properties.mail.smtp.starttls.enable", true);
		return javaMailSender;
	}

}
