package org.apache.commons.mail;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class EmailTest {
	
	private static final String [] TEST_EMAILS = {"ab@cd.com",  "a.b@c.org", 
			"abcdefjhjkmnoprstuof@com.bd", "ghknb@inet.com"};
	
	private EmailConcrete email;
	
	
	@Before
	public void setUpEmailTest () throws Exception {
		email = new EmailConcrete();
		
		
	}
	
	@After
	public void tearDownEmailTest() throws Exception {
		
	}
	
	@Test
	public void testAddBcc() throws Exception {
	    email.addBcc(TEST_EMAILS);
	    assertEquals(4, email.getBccAddresses().size(), "It should be 4 email addresses");
	}
	
	
	@Test
	public void testAddCcWithNull() {
	    try {
	        email.addCc((String[]) null);
	        fail("Not null CC address");
	    } catch (EmailException e) {
	        assertEquals("Address List invalid", e.getMessage(), "message:");
	    }
	}
	

	@Test 
	public void testAddCcWithEmails () throws Exception {
		email.addCc(TEST_EMAILS);
		assertEquals ( 4, email.getCcAddresses().size(),"It has to be the same amouth of addresses as amouth of emails");
		
	}
	
	@Test
	public void testAddCcOneEmailCorrectly () throws Exception {
		
		email.addCc("oneEmail@abc.com");
		assertEquals (1, email.getCcAddresses().size(),"One email is added correctly");
		
		
	}
	
	 @Test
	    public void testAddHeaderInputs() {
	        String name = "Content";
	        String value = "text/plain";
	        email.addHeader(name, value);
	        assertEquals( value, email.getHeaders().get(name), "Invalid format of header");
	    }
	 
	 
	 
	 @Test
	    public void testAddHeaderNull() {
	        String value = "text/plain";
	        try {
	            email.addHeader(null, value);	            
	        } catch (IllegalArgumentException e) {
	            assertEquals("name can not be null or empty", e.getMessage());
	        }
	    
	 }

	 @Test
	    public void testAddHeaderEmptyName() {
	        String name = "";
	        String value = "text/plain";
	        try {
	            email.addHeader(name, value);	          
	        } catch (IllegalArgumentException e) {
	            assertEquals("name can not be null or empty", e.getMessage());
	        }
	 }
	 
	        @Test
	        public void testAddHeaderNullValue() {
	            String name = "Content-Type";
	            try {
	                email.addHeader(name, null);
	                fail("error for null value");
	            } catch (IllegalArgumentException e) {
	                assertEquals(" Value can not be null or empty", e.getMessage());
	            }
	        }

	        @Test
	        public void testAddHeaderEmptyValue() {
	            String name = "Content-Type";
	            String value = "";
	            try {
	                email.addHeader(name, value);
	                fail("Error for empty value");
	            } catch (IllegalArgumentException e) {
	                assertEquals("Value can not be empty", e.getMessage());
	            }
	        }
	        
	        @Test
	        public void testAddReplyToValidEmails() {
	            for (String emailStr : TEST_EMAILS) {
	                try {
	                    email.addReplyTo(emailStr);
	                } catch (EmailException e) {
	                    fail("Its a valid email: " + emailStr);
	                }
	            }
	         
	            assertEquals(TEST_EMAILS.length, email.getReplyToAddresses().size(), "All valid emails are added.");
	        }

	        
	        @Test
	        public void testAddReplyToInvalid() throws Exception {
	           String replyName = "Name";
			   email.addReplyTo(TEST_EMAILS[0],replyName);
	           assertEquals(TEST_EMAILS[0], email.getReplyToAddresses());
	           assertEquals (replyName, email.getReplyToAddresses());    
	                
	        }      
	            
	                  
	                       

	          @Test
	        public void testMailSession() throws EmailException {	            
	            email.setHostName("smtp.gmmail.com");
	            email.setSmtpPort(587);
	            email.setAuthenticator(new DefaultAuthenticator("user@gmmail.com", "password"));
	            email.setSSLOnConnect(true);
	            email.setStartTLSEnabled(true);
	            
	            Session session = email.getMailSession(); 
	            Properties sessionProps = session.getProperties();
	            
	            assertEquals("smtp.example.com", sessionProps.getProperty(Email.MAIL_HOST));
	            assertEquals("587", sessionProps.getProperty(Email.MAIL_PORT));
	            assertEquals("true", sessionProps.getProperty(Email.MAIL_SMTP_AUTH));
	            
	            
	        }
	        
	        
	        @Test
	        public void testMailSessionWithInvalidHostname() {
	            email.setHostName(""); 
	            
	            try {
	                Session session = email.getMailSession();
	                fail("Expected EmailException was not thrown");
	            } catch (EmailException e) {
	                assertTrue(e.getMessage().contains("Invalid hostname"), "Message for invalid hostname:");
	            }
	        }
	        
	        
	        @Test
	        public void testBuildMimeMessage() throws EmailException {
	            email.setHostName("host");
	            email.setSmtpPort(1234);
	            email.setAuthentication("username", "password");
	            email.setFrom("from@gmail.com");
	            email.setSubject("test subject");
	            email.setCharset("UTF-8");
	            email.addTo("to@example.com");
	            
	            
	             email.buildMimeMessage();
	             
	               
	        }
	        
	        @Test
	        public void testBuildMimeMessageWithSSL() throws Exception {
	            email.setHostName("host");	           
	            email.setSslSmtpPort("465");
	            email.setSSLOnConnect(true);
	            email.setFrom("from@example.com");
	            email.addTo("to@example.com");
	            email.buildMimeMessage();

	            
	            Session session = email.getMailSession();
	            assertEquals("465", session.getProperty("mail.smtp.port"));
	        }
	        
	        
	        
	        
	        @Test
	        public void testSetMsgValid() throws EmailException, MessagingException, IOException {
	            String validMsg = "Hi there!";
	            email.setHostName("smtp.gmail.com"); 
	            email.setSmtpPort(587); 
	            email.setAuthentication("yourEmail@gmail.com", "yourPassword"); 
	            email.setSSLOnConnect(false); 
	            email.setStartTLSEnabled(true); 
	            email.setFrom("test@gmail.com"); 
	            email.setMsg(validMsg); 
	            email.addTo("recipient@example.com"); 
	            email.buildMimeMessage();
	            
	            MimeMessage mimeMessage = email.getMimeMessage();	           
	            String content = mimeMessage.getContent() instanceof String ? mimeMessage.getContent().toString() : "Content type not plain text";
	            assertEquals(validMsg, content.trim(), "The message should be set correctly.");
	        }
	        		
	        @Test
	        public void testGetSentDateWithNoSetDate() {	           
	            Date beforeCall = new Date();
	            Date sentDate = email.getSentDate();
	            Date afterCall = new Date();
	           
	            assertTrue(!sentDate.before(beforeCall) && !sentDate.after(afterCall),
	                    "The sent date should be the current date/time");
	        }
	        
	        
	        @Test
	        public void testGetSentDateWhenSet() throws Exception {
	           
	            Date expectedSentDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");
	            email.setSentDate(expectedSentDate); 	          
	            Date actualSentDate = email.getSentDate();	            
	            assertEquals(expectedSentDate.getTime(), actualSentDate.getTime(),
	                    "The date should match set date");	           
	            actualSentDate.setTime(new Date().getTime()); 	           
	            Date verifySentDate = email.getSentDate();
	            assertEquals(expectedSentDate.getTime(), verifySentDate.getTime(),
	                    "Modify in a right way");
	        } 
	        
	        @Test
	        public void testSocketConnectionTimeout() {	            
	            int testTimeout = 2000; 
	            email.setSocketConnectionTimeout(testTimeout);	           
	            assertEquals(testTimeout, email.getSocketConnectionTimeout(), "The timeout should match the value");
	        } 
	        
	        
	        @Test
	        public void testSetFromWithValidEmail() throws EmailException {
	            String validEmail = "test@example.com";
	            email.setCharset("UTF-8"); 
	            email.setFrom(validEmail);            

	            assertEquals("The FROM address should match the set value", validEmail, email.getFromAddress().getAddress());
	        }
	        
	        @Test
	        public void testSetFromWithInvalidEmail() {
	            String invalidEmail = "invalidEmail";
	            EmailException caughtException = null;

	            try {
	                email.setFrom(invalidEmail);
	            } catch (EmailException e) {
	                caughtException = e;
	            }
	            assertNotNull(caughtException, "An EmailException should have been thrown for an invalid email address");
	            
	            if (caughtException != null) {
	                assertFalse(caughtException.getMessage().isEmpty(), "The exception message should not be empty");
	            }
	        }
	        
	        @Test
	        public void testBuildMimeMessageWithCCAndBCC() throws EmailException {
	            email.setHostName("smtp.example.com");
	            email.setSmtpPort(587);
	            email.setFrom("from@example.com");
	            email.addTo("to@example.com");
	            email.addCc("cc@example.com");
	            email.addBcc("bcc@example.com");
	            email.setSubject("Test Subject");
	            email.setMsg("Test message");

	            email.buildMimeMessage();
	            
	        }
	        
	        
	        @Test
	        public void testBuildMimeMessageWithSubjectAndBody() throws EmailException, MessagingException, IOException {
	            email.setHostName("smtp.example.com");
	            email.setSmtpPort(587);
	            email.setFrom("from@example.com");
	            email.addTo("to@example.com");
	            email.setSubject("Test Subject");
	            email.setMsg("This is a test message.");

	            email.buildMimeMessage();

	            MimeMessage mimeMessage = email.getMimeMessage();
	            assertEquals("Test Subject", mimeMessage.getSubject());
	            assertEquals("This is a test message.", mimeMessage.getContent().toString().trim());
	        }
	        
	        
	        
}

	        		
	        		
	        		
	        		
	        				
	        				
	        		
	        	
	        	
	        	
        
    
	        
	        
	        
	        
	        
	        
	        
	        
	        