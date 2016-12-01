
import java.util.ArrayList;
import java.util.Date;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Class to send mail
 * 
 * @author HemanthKumarTirupati
 * 
 *         Usage: Instantiate first to use, usage of different functions are
 *         explained inline
 */
public class SendMail {

	private ArrayList<String> recipients;
	private Message message;
	private Multipart multipartMessage = new MimeMultipart();
	private Transport mailTransport;

	/**
	 * Public Constructor to get all the bootStrap stuff such as sessions
	 */
	public SendMail() {

		message = new MimeMessage(Include.smtpSession);
		recipients = new ArrayList<String>();

		try {
			mailTransport = Include.smtpSession.getTransport("smtp");
			mailTransport.connect();
			message.setFrom(new InternetAddress(Include.myEmail));
			message.setSubject("No Subject");
			message.setSentDate(new Date());
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Add email address of recipients Use this function multiple times to add
	 * multiple recepients
	 * 
	 * @param recepientAddress
	 */
	public void addRecepient(String recepientAddress) {
		recipients.add(recepientAddress);
	}

	/**
	 * Use this function to set the subject of mail
	 * 
	 * @param subject
	 */
	public void setSubject(String subject) {
		try {
			message.setSubject(subject);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Use this function If you would like to add text to the body of mail
	 * 
	 * @param content
	 */
	public void addTextContent(String content) {
		BodyPart textBodyPart = new MimeBodyPart();
		try {
			textBodyPart.setText(content);
			multipartMessage.addBodyPart(textBodyPart);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Use this function if you would like to add html to the body of mail
	 * 
	 * @param content
	 */
	public void addHTMLContent(String content) {
		BodyPart htmlBodyPart = new MimeBodyPart();
		try {
			htmlBodyPart.setContent(content, "text/html");
			multipartMessage.addBodyPart(htmlBodyPart);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Use this function if you would like to add attachments to your mail Use
	 * this function multiple times if you have multiple attachments to be
	 * attached
	 * 
	 * @param filePath
	 */
	public void attachFile(String filePath) {
		BodyPart fileBodyPart = new MimeBodyPart();
		DataSource fileDataSource = new FileDataSource(filePath);

		try {
			fileBodyPart.setDataHandler(new DataHandler(fileDataSource));
			String temp[] = filePath.split("\\\\");
			String fileName = temp[temp.length-1];
			fileBodyPart.setFileName(fileName);
			multipartMessage.addBodyPart(fileBodyPart);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Use this function when all the necessary of above details are given Used
	 * to send the mail
	 */
	public boolean sendMail(String recepient) {

		try {

			message.setContent(multipartMessage);
			message.setRecipient(Message.RecipientType.TO,
					new InternetAddress(recepient));

			this.mailTransport.send(message);
			mailTransport.close();
			return true;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

}
