import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

/**
 * This class have all the configuration values to be set
 * 
 * @author HemanthKumarTirupati
 *
 *         Cannot instantiate just use the functions
 */
public class Include {

	protected static String myEmail = "";
	private static String userName = "";
	private static String password = "";
	private static String smtpServer = "smtp.gmail.com";
	private static String smtpPort = "465";
	private static String imapServer = "imap.gmail.com";
	private static String imapPort = "993";
	protected static Session imapSession;
	protected static Session smtpSession;
	protected final static String CURDIR = System.getProperty("user.dir");

	private Include() {

	}

	protected static void setSMTPSession() {
		Include.smtpSession = getSMTPSession();
	}

	/**
	 * Get an SMTPSession required by @SendMail.java
	 * 
	 * @return
	 */
	private static Session getSMTPSession() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", smtpServer);
		properties.put("mail.smtp.port", smtpPort);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.enable", "true");

		Session session = Session.getDefaultInstance(properties,

		new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(userName, password);
			}
		});
		session.setDebug(true);
		return session;
	}

	protected static void setIMAPSession() {
		Include.imapSession = getIMAPSession();
	}

	/**
	 * Get an IMAPSession required by @ReadMail.java
	 * 
	 * @return
	 */
	private static Session getIMAPSession() {
		Properties properties = new Properties();
		Session session = Session.getInstance(properties);

		session.setDebug(true);
		return session;
	}

	protected static String parseName(String str, int offset) {
		if (str.contains("<")) {
			str = str.substring(offset, str.length() - 2);
			String temp[] = str.split("<");
			return temp[0];
		} else {
			return "";
		}
	}

	protected static String parseAddress(String str, int offset) {
		if (str.contains("<")) {
			str = str.substring(offset, str.length() - 2);
			String temp[] = str.split("<");
			return temp[1];
		} else {
			if (str.length() == offset) {
				return null;
			}
			return str.substring(offset, str.length() - 1);
		}
	}

	protected static String[] parseSubject(String str) {
		return str.substring(10).trim().split(" ");
	}

	protected static String[] parseRecepientsAddress(String str) {
		String to[] = str.split(";");
		String address[] = new String[to.length];
		address[0] = parseAddress(to[0] + ";", 4);
		for (int i = 1; i < to.length; i++) {
			address[i] = parseAddress(to[i] + ";", 0);
		}
		return address;
	}

	protected static String[] parseRecepientsName(String str) {
		String to[] = str.split(";");
		String names[] = new String[to.length];
		names[0] = parseName(to[0] + ";", 4);
		for (int i = 1; i < to.length; i++) {
			names[i] = parseName(to[i] + ";", 0);
		}
		return names;
	}

	protected static Long parseUID(String UID) {
		return Long.parseLong(UID.substring(5));
	}

	/*
	 * Getters and Setters for various reference variables
	 */

	public static String getMyEmail() {
		return myEmail;
	}

	public static void setMyEmail(String myEmail) {
		Include.myEmail = myEmail;
	}

	public static String getUserName() {
		return userName;
	}

	public static void setUserName(String userName) {
		Include.userName = userName;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		Include.password = password;
	}

	public static String getSmtpServer() {
		return smtpServer;
	}

	public static void setSmtpServer(String smtpServer) {
		Include.smtpServer = smtpServer;
	}

	public static String getSmtpPort() {
		return smtpPort;
	}

	public static void setSmtpPort(String smtpPort) {
		Include.smtpPort = smtpPort;
	}

	public static String getImapServer() {
		return imapServer;
	}

	public static void setImapServer(String imapServer) {
		Include.imapServer = imapServer;
	}

	public static String getImapPort() {
		return imapPort;
	}

	public static void setImapPort(String imapPort) {
		Include.imapPort = imapPort;
	}
}
