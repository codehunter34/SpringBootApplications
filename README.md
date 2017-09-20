# SpringBootApplications

1) All the RestTemplate tests were implemented in RestClientTest.java using Junit.
After you start the application, you can run RestClientTest.java.

Please remember that test methods have to be executed with the given order because results of one method is affecting the result of another one.
For example; we need to upload first to be able to make a download, get by id etc.

2) CommonUtils class was added to define a few utility methods.

3) Email, EmailSender, ScheduledCronJobTests classes were created to send emails

4) Application.java, FileController.java, FileMetaDataSearchCriteria.java, FileUploadUtil.java, RESTClient.java, RestClientTest.java,
ScheduledCronJobs.java were updated.


Important notes:

For testing purposes, I assume that files in 'C://UploadRepo' and 'C://UploadRepo' folder itself will not be removed manually while the application is running.

Please run RestClientTest.java to run the unit tests. The tests are connected to each other. Please make sure you run the whole class(not single methods) for the convenience of tests.

Please run RestClientTest.java once for each run on Application.java. Otherwise, the test results will be unexpected due to uploading duplicate name files.

Please replace the email addresses in application.properties with your own email addresses to see whether the cron job is sending an email. It would be a better practice to keep these type of values in a SYSTEM_PARAMETERS table.
Application.createJavaMailSender needs update in terms of SMTP settings.

Please check 'src/main/resourcec/DownloadRepository' and 'C://UploadRepo' for downloaded and uploaded files.
