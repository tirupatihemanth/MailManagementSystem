import java.io.IOException;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.swing.JOptionPane;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

/**
 *   *** Remember that this class is incomplete and has to be improved ***
 */

/**
 * class to Readmail
 * 
 * @author HemanthKumarTirupati
 *
 */

public class ReadMail {

	protected static IMAPStore mailStore;
	protected static IMAPFolder outbox;
	protected static IMAPFolder inbox;
	protected static boolean textIsHtml = false;

	/**
	 * public constructor to Read mail All the bootstrap required to receive
	 * mail will be setup
	 */

	public ReadMail() {

		try {
			mailStore = (IMAPStore) Include.imapSession.getStore("imaps");

			try {
				mailStore.connect(Include.getImapServer(),
						Include.getUserName(), Include.getPassword());
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null,
						"Please check your internet connection");
				System.exit(0);
				e.printStackTrace();
			}
		} catch (NoSuchProviderException e) {

			e.printStackTrace();
		}
		try {
			inbox = (IMAPFolder) mailStore.getFolder("INBOX");
			outbox = (IMAPFolder) mailStore.getFolder("[Gmail]/Sent Mail");
			inbox.open(Folder.READ_ONLY);
			outbox.open(Folder.READ_ONLY);

		} catch (MessagingException e) {
			JOptionPane.showMessageDialog(null,
					"Please check your Internet Connection");
			e.printStackTrace();
		}
	}

	/**
	 * *** Need to be Improved *** Recursive function to iterate over all the
	 * parts over the message and display the content appropriately
	 * 
	 * @param mailPart
	 */

	/**
	 * Return the primary text content of the message.
	 */
	public String getText(Part p) throws MessagingException, IOException {
		if (p.isMimeType("text/*")) {
			String s = (String) p.getContent();
			textIsHtml = p.isMimeType("text/html");
			return s;
		}

		if (p.isMimeType("multipart/alternative")) {
			Multipart mp = (Multipart) p.getContent();
			String text = null;

			for (int i = 0; i < mp.getCount(); i++) {
				Part bp = mp.getBodyPart(i);
				if (bp.isMimeType("text/plain")) {
					if (text == null)
						text = getText(bp);
					continue;
				} else if (bp.isMimeType("text/html")) {
					String s = getText(bp);
					if (s != null)
						return s;
				} else {
					return getText(bp);
				}
			}
			return text;
		} else if (p.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) p.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				String s = getText(mp.getBodyPart(i));
				if (s != null)
					return s;
			}
		}

		return null;
	}

	public boolean hasAttachment(Part mailPart) {
		Boolean b = false;

		try {

			if (mailPart.isMimeType("text/*")) {

				return false;
			} else if (mailPart.isMimeType("multipart/*")) {
				Multipart multipart = (Multipart) mailPart.getContent();
				for (int partIdx = 0; partIdx < multipart.getCount(); partIdx++) {
					b = b || hasAttachment(multipart.getBodyPart(partIdx));
				}
			} else if (mailPart.isMimeType("message/rfc822")) {
				return hasAttachment((Part) mailPart.getContent());

			} else if (mailPart.ATTACHMENT.equalsIgnoreCase(mailPart
					.getDisposition())) {
				return true;
			} else if (mailPart.isMimeType("image/*")) {
				return true;
			} else if (mailPart.INLINE.equalsIgnoreCase(mailPart
					.getDisposition())) {
				return true;
			} else if (mailPart.getFileName() != null
					&& mailPart.getDisposition() == null) {
				return true;
			}

		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}

		return b;

	}

	public void downloadAttachments(Part mailPart) {

		try {

			if (mailPart.isMimeType("text/*")) {
				return;
			} else if (mailPart.isMimeType("multipart/*")) {
				Multipart multipart = (Multipart) mailPart.getContent();
				for (int partIdx = 0; partIdx < multipart.getCount(); partIdx++) {
					downloadAttachments(multipart.getBodyPart(partIdx));
				}
			} else if (mailPart.isMimeType("message/rfc822")) {
				downloadAttachments((Part) mailPart.getContent());

			} else if (mailPart.ATTACHMENT.equalsIgnoreCase(mailPart
					.getDisposition())) {
				getAttachment((MimeBodyPart) mailPart, "ATTACHMENT");
			} else if (mailPart.isMimeType("image/*")) {
				getAttachment((MimeBodyPart) mailPart, "INLINE");
			} else if (mailPart.INLINE.equalsIgnoreCase(mailPart
					.getDisposition())) {
				getAttachment((MimeBodyPart) mailPart, "INLINE");
			} else if (mailPart.getFileName() != null
					&& mailPart.getDisposition() == null) {
				getAttachment((MimeBodyPart) mailPart, "INLINE");
			}

		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Get Inline Images and attachments to the appropriate folders
	 * 
	 * @param mailPart
	 * @param type
	 */
	public void getAttachment(MimeBodyPart mailPart, String type) {

		try {
			if (type.equals("ATTACHEMENT")) {
				mailPart.saveFile(System.getProperty("user.dir")
						+ "/attachments/" + mailPart.getFileName());
			} else {
				mailPart.saveFile(System.getProperty("user.dir")
						+ "/attachments/" + mailPart.getFileName());
			}

		} catch (IOException | MessagingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get the basic stuff of a mail. Get the from, to and subject and others of
	 * a mail
	 * 
	 * @param mail
	 */

	protected static String getHeaders_GUI(Message mail) {
		if (mail == null) {
			return "";
		}
		String header = "";
		try {
			header += "<font size='4'>From:       ";
			// System.out.println("From: ");
			if (mail.getFrom().length != 0) {
				header += mail.getFrom()[0] + ";";
			}

			header += "</font><br /><font size='4'>To:         ";
			try {
				for (Address toAddress : mail
						.getRecipients(Message.RecipientType.TO)) {
					header += toAddress.toString() + ";";
				}
			} catch (NullPointerException exception) {

			}
			header += "</font><br />";
			/** TODO: ***Will work on CC: BCC: later*** **/

			header += "<font size= '4'> Subject:    " + mail.getSubject()
					+ "</font><br />";
			header += "<font size = '4'>Date:       " + mail.getReceivedDate()
					+ "</font><br/><hr><br />";
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return header;
	}

	protected static String getHeaders(Message mail, String folder) {

		if (mail == null) {
			return "";
		}
		String header = "";
		try {
			if (folder.equals("INBOX")) {
				header += inbox.getUID(mail) + "\n";
			} else {
				header += outbox.getUID(mail) + "\n";
			}

			header += "valid: 1\n";
			header += "From: ";
			// System.out.println("From: ");
			if (mail.getFrom().length != 0) {
				header += mail.getFrom()[0] + ";";
			}

			header += "\nTo: ";
			try {
				for (Address toAddress : mail
						.getRecipients(Message.RecipientType.TO)) {
					header += toAddress.toString() + ";";
				}
			} catch (NullPointerException exception) {

			}

			/** TODO: ***Will work on CC: BCC: later*** **/
			header += "\nSubject: " + mail.getSubject();
			header += "\nDate: " + mail.getReceivedDate() + "\n\n";
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return header;
	}

}
