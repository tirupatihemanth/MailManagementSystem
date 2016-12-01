import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import javax.mail.Message;
import javax.mail.MessagingException;
import com.sun.mail.imap.IMAPFolder;

/**
 * Database class handles all the caching and mail saving activities
 * @author HemanthKumarTirupati
 *
 */
public class Database {

	Scanner inputPage;
	protected static Long lastOutboxUID;
	protected static Long latestOutboxUID;
	protected static Long lastUID;
	protected static Long lastFromUID;
	protected static Long lastToUID;
	protected static Long lastSubjectUID;
	protected static Long latestUID;
	protected static Long lastFromNameUID;
	protected static Long lastToNameUID;
	protected static boolean isUpdated;

	/**
	 * Constructor helps in setting all the bootstrap stuff necessary which
	 * describes present state of the database
	 */

	public Database() {

		try {
			inputPage = new Scanner(new File(Include.CURDIR
					+ "/mailDB/lastUID.txt"));
			this.lastUID = inputPage.hasNext() ? inputPage.nextLong() : 0;
			inputPage.close();
			inputPage = new Scanner(new File(Include.CURDIR
					+ "/mailDB/lastSearchUID.txt"));
			this.lastFromUID = this.lastToUID = this.lastFromNameUID = this.lastToNameUID = this.lastSubjectUID = inputPage
					.hasNext() ? inputPage.nextLong() : 0;
			inputPage.close();
			inputPage = new Scanner(new File(Include.CURDIR
					+ "/mailDB/outbox/lastOutboxUID.txt"));
			this.lastOutboxUID = inputPage.hasNext() ? inputPage.nextLong() : 0;
			inputPage.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Synchronises the last state of the database with the present state
	 * @param folder
	 */
	public void updateDB(String folder) {

		long latestUid = 0, lastUid = 0;
		IMAPFolder folderName = null;

		try {
			if (folder.equals("inbox")) {
				lastUid = this.lastUID;
				folderName = ReadMail.inbox;
				latestUid = this.latestUID = ReadMail.inbox
						.getUID(ReadMail.inbox.getMessage(ReadMail.inbox
								.getMessageCount()));

				this.lastUID = new Long(this.latestUID.longValue());
			} else if (folder.equals("outbox")) {
				lastUid = this.lastOutboxUID;
				latestUid = this.latestOutboxUID = ReadMail.outbox
						.getUID(ReadMail.outbox.getMessage(ReadMail.outbox
								.getMessageCount()));
				// System.out.println("latestUID: " + latestUid);
				folderName = ReadMail.outbox;
				this.lastOutboxUID = new Long(this.latestOutboxUID.longValue());
			}

			if (lastUid != latestUid) {
				this.isUpdated = true;
			}

			while (latestUid != lastUid) {
				if (latestUid - 100 * (lastUid / 100) < 100) {
					if ((lastUid) / 100 == ((lastUid - 1) / 100)) {
						updatePage(folderName, (lastUid) / 100, latestUid - 100
								* ((lastUid - 1) / 100), lastUid, true);
					} else {
						updatePage(folderName, (lastUid) / 100, latestUid - 100
								* ((lastUid - 1) / 100), lastUid, true);
					}

					lastUid = latestUid;
				} else {
					updatePage(folderName, (lastUid) / 100, 100
							* (((lastUid) / 100) + 1) - lastUid, lastUid, true);
					lastUid += 100 * ((lastUid / 100) + 1) - lastUid;
				}
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * overwrites a specific page by writing the latest headers from the server
	 * used to update pages
	 * 
	 * @param pageNumber
	 */

	public int updatePage(String folderName, Long pageNumber) {

		Message[] mails = null;
		String headers = "";
		try {
			if (folderName.equals("INBOX")) {
				mails = ReadMail.inbox.getMessagesByUID(pageNumber * 100 + 1,
						(pageNumber + 1) * 100);
			} else {
				mails = ReadMail.outbox.getMessagesByUID(pageNumber * 100 + 1,
						(pageNumber + 1) * 100);
			}

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (int i = 0; i < mails.length; i++) {
			headers += ReadMail.getHeaders(mails[i], folderName);
		}

		BufferedWriter bufferedWriter = null;

		try {

			if (folderName.equals("INBOX")) {
				bufferedWriter = new BufferedWriter(new FileWriter(
						Include.CURDIR + "/mailDB/page" + pageNumber + ".mail",
						false));
			} else {
				bufferedWriter = new BufferedWriter(new FileWriter(
						Include.CURDIR + "/mailDB/outbox/page" + pageNumber
								+ ".mail", false));
			}

			bufferedWriter.write(headers);
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mails.length;

	}

	/**
	 * Refresh the database page reload the entire page of the database and get's any updates
	 * @param folderName
	 * @param uid
	 * @param guiPageSize
	 */
	public void refreshPage(String folderName, Long uid, int guiPageSize) {

		long curPage = 0;
		if (folderName.equals("INBOX")) {
			if (uid.equals(latestUID)) {
				updateDB("inbox");
				uid = latestUID;
			}
		} else {
			if (uid.equals(latestOutboxUID)) {
				updateDB("outbox");
				uid = this.latestOutboxUID;
			}
		}

	}

	/**
	 * Used by the updateDB command to appropriately inject headers into the
	 * database
	 * 
	 * @param inbox
	 * @param pageNumber
	 * @param numToFill
	 * @param append
	 */
	private void updatePage(IMAPFolder folder, long pageNumber, long numToFill,
			long lastUid, boolean append) {

		String headers = "";
		Message[] mails = null;
		try {
			mails = folder.getMessagesByUID((long) (lastUid + 1),
					(long) (lastUid + numToFill));
		} catch (MessagingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (int i = 0; i < mails.length; i++) {
			headers += ReadMail.getHeaders(mails[i], folder.getName());
		}

		BufferedWriter bufferedWriter = null;
		try {
			System.out.println(folder.getName());
			if (folder.getFullName().equals("[Gmail]/Sent Mail")) {
				bufferedWriter = new BufferedWriter(new FileWriter(
						Include.CURDIR + "/mailDB/outbox/page" + pageNumber
								+ ".mail", append));
			} else {
				bufferedWriter = new BufferedWriter(new FileWriter(
						Include.CURDIR + "/mailDB/page" + pageNumber + ".mail",
						append));
			}
			bufferedWriter.write(headers);
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * opens a scanner with a given page of the database as the resource
	 * 
	 * @param pageNumber
	 */
	public void connectToPage(String folderName, Long pageNumber) {
		BufferedInputStream inputStream = null;
		try {
			if (folderName.equals("INBOX")) {
				inputStream = new BufferedInputStream(new FileInputStream(
						Include.CURDIR + "/mailDB/page" + pageNumber + ".mail"));
			} else {
				inputStream = new BufferedInputStream(new FileInputStream(
						Include.CURDIR + "/mailDB/outbox/page" + pageNumber
								+ ".mail"));
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		inputPage = new Scanner(inputStream);
	}

	/**
	 * returns all info with the given UID [validString, fromAddress, toAddress,
	 * Subject, DateString]
	 * 
	 * @param UID
	 * @return
	 */

	public String[] getDetails(String folderName, Long UID) {
		connectToPage(folderName, UID / 100);
		boolean valid = false;
		while (inputPage.hasNext()) {
			if (inputPage.nextLong() == UID) {
				valid = true;
				break;
			}
			for (int i = 0; i < 6; i++) {
				inputPage.nextLine();
			}
		}
		if (valid) {
			inputPage.nextLine();
			String str[] = new String[5];
			for (int i = 0; i < 5; i++) {
				str[i] = inputPage.nextLine();
			}
			closeConnection();
			return str;
		}
		closeConnection();
		return null;
	}

	/**
	 * Returns all info from a page from a given baseUID <UIDString,
	 * validString, fromAddress, toAddress, subject, date
	 * 
	 * @param baseUID
	 * @return
	 */
	public ArrayList<String> getAllFromPage(String folderName, Long pageNumber,
			Long baseUID) {
		connectToPage(folderName, pageNumber);
		boolean valid = false;
		long base = 0;
		while (inputPage.hasNext()) {
			if ((base = inputPage.nextLong()) >= baseUID) {
				valid = true;
				break;
			} else {
				for (int i = 0; i < 6; i++) {
					inputPage.nextLine();
				}
			}
		}

		if (valid) {
			ArrayList<String> str = new ArrayList<String>();
			inputPage.nextLine();
			str.add(Long.toString(base));
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			inputPage.nextLine();
			while (inputPage.hasNext()) {
				for (int i = 0; i < 6; i++) {
					str.add(inputPage.nextLine());
				}
				inputPage.nextLine();
			}
			closeConnection();
			return str;
		}
		closeConnection();
		return null;

	}

	/**
	 * Methods used for Building Searching Trees
	 */

	/**
	 * returns all that is required to Build From Mail from given lastUID
	 * (exclusive) [UID, VALID, FROM, SUBJECT, DATE] all the strings obtained
	 * are unprocessed
	 * 
	 * @param lastUID
	 * @return
	 */

	public ArrayList<String> getAllLaterMailForFrom(Long lastUID, Long latestUID) {
		if (lastUID == latestUID) {
			return null;
		}
		long finalPageNumber = latestUID / 100;
		long pageNumber = lastUID / 100;
		ArrayList<String> str = new ArrayList<String>();
		while (pageNumber <= finalPageNumber) {
			getAllMailFromPageForFrom(pageNumber, lastUID, str);
			pageNumber++;
			lastUID = pageNumber * 100;
		}
		return str;
	}

	private ArrayList<String> getAllMailFromPageForFrom(Long pageNumber,
			Long baseUID, ArrayList<String> str) {
		connectToPage("INBOX", pageNumber);
		boolean valid = false;
		long base = 0;
		while (inputPage.hasNext()) {
			if ((base = inputPage.nextLong()) >= baseUID) {
				valid = true;
				break;
			} else {
				for (int i = 0; i < 6; i++) {
					inputPage.nextLine();
				}
			}
		}

		if (valid) {
			str.add(Long.toString(base));
			inputPage.nextLine();
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			inputPage.nextLine();
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			inputPage.nextLine();
			while (inputPage.hasNext()) {
				for (int i = 0; i < 3; i++) {
					str.add(inputPage.nextLine());
				}
				inputPage.nextLine();
				str.add(inputPage.nextLine());
				str.add(inputPage.nextLine());
				inputPage.nextLine();
			}
			closeConnection();
			return str;
		}
		closeConnection();
		return str;
	}

	/**
	 * returns all that is required to Build To Mail from given lastUID
	 * (exclusive) [UID, VALID, FROM, TO, SUBJECT, DATE] all the strings
	 * obtained are unprocessed
	 * 
	 * @param lastUID
	 * @param latestUID
	 * @return
	 */

	public ArrayList<String> getAllLaterMailForTo(Long lastUID, Long latestUID) {
		if (lastUID == latestUID) {
			return null;
		}
		long finalPageNumber = latestUID / 100;
		long pageNumber = lastUID / 100;
		ArrayList<String> str = new ArrayList<String>();
		while (pageNumber <= finalPageNumber) {
			getAllMailFromPageForTo(pageNumber, lastUID, str);
			pageNumber++;
			lastUID = pageNumber * 100;
		}
		return str;
	}

	private ArrayList<String> getAllMailFromPageForTo(Long pageNumber,
			Long baseUID, ArrayList<String> str) {
		connectToPage("INBOX", pageNumber);
		boolean valid = false;
		long base = 0;
		while (inputPage.hasNext()) {
			if ((base = inputPage.nextLong()) >= baseUID) {
				valid = true;
				break;
			} else {
				for (int i = 0; i < 6; i++) {
					inputPage.nextLine();
				}
			}
		}

		if (valid) {
			str.add(Long.toString(base));
			inputPage.nextLine();
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			inputPage.nextLine();
			while (inputPage.hasNext()) {

				for (int i = 0; i < 6; i++) {
					str.add(inputPage.nextLine());
				}
				inputPage.nextLine();
			}
			closeConnection();
			return str;
		}
		closeConnection();
		return str;
	}

	/**
	 * returns all that is required to build SubjectMailTree from given lastUID
	 * (exclusive) [UID, VALID, FROM, SUBJECT, DATE] all the strings are
	 * unprocessed
	 * 
	 * @param lastUID
	 * @param latestUID
	 * @return
	 */

	public ArrayList<String> getAllLaterMailForSubject(Long lastUID,
			Long latestUID) {
		if (lastUID == latestUID) {
			return null;
		}
		long finalPageNumber = latestUID / 100;
		long pageNumber = lastUID / 100;
		ArrayList<String> str = new ArrayList<String>();
		while (pageNumber <= finalPageNumber) {
			getAllMailFromPageForSubject(pageNumber, lastUID, str);
			pageNumber++;
			lastUID = pageNumber * 100;
		}
		return str;
	}

	private ArrayList<String> getAllMailFromPageForSubject(Long pageNumber,
			Long baseUID, ArrayList<String> str) {
		connectToPage("INBOX", pageNumber);
		boolean valid = false;
		long base = 0;
		while (inputPage.hasNext()) {
			if ((base = inputPage.nextLong()) >= baseUID) {
				valid = true;
				break;
			} else {
				for (int i = 0; i < 6; i++) {
					inputPage.nextLine();
				}
			}
		}

		if (valid) {
			str.add(Long.toString(base));
			inputPage.nextLine();
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			inputPage.nextLine();
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			inputPage.nextLine();
			while (inputPage.hasNext()) {
				str.add(inputPage.nextLine());
				str.add(inputPage.nextLine());
				str.add(inputPage.nextLine());
				inputPage.nextLine();
				str.add(inputPage.nextLine());
				str.add(inputPage.nextLine());
				inputPage.nextLine();
			}
			closeConnection();
			return str;
		}
		closeConnection();
		return str;
	}

	/**
	 * Returns all that is required by FromNameSearchTree from given lastUID
	 * (exclusive) [UID, VALID, FROM, SUBJECT, DATE] All the information is
	 * unprocessed
	 * 
	 * @param lastUID
	 * @param latestUID
	 * @return
	 */
	public ArrayList<String> getAllLaterMailForFromName(Long lastUID,
			Long latestUID) {
		if (lastUID == latestUID) {
			return null;
		}
		long finalPageNumber = latestUID / 100;
		long pageNumber = lastUID / 100;
		ArrayList<String> str = new ArrayList<String>();
		while (pageNumber <= finalPageNumber) {
			getAllMailFromPageForFromName(pageNumber, lastUID, str);
			pageNumber++;
			lastUID = pageNumber * 100;
		}
		return str;
	}

	private ArrayList<String> getAllMailFromPageForFromName(Long pageNumber,
			Long baseUID, ArrayList<String> str) {
		connectToPage("INBOX", pageNumber);
		boolean valid = false;
		long base = 0;
		while (inputPage.hasNext()) {
			if ((base = inputPage.nextLong()) >= baseUID) {
				valid = true;
				break;
			} else {
				for (int i = 0; i < 6; i++) {
					inputPage.nextLine();
				}
			}
		}

		if (valid) {
			str.add(Long.toString(base));
			inputPage.nextLine();
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			inputPage.nextLine();
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			inputPage.nextLine();
			while (inputPage.hasNext()) {
				str.add(inputPage.nextLine());
				str.add(inputPage.nextLine());
				str.add(inputPage.nextLine());
				inputPage.nextLine();
				str.add(inputPage.nextLine());
				str.add(inputPage.nextLine());
				inputPage.nextLine();
			}
			closeConnection();
			return str;
		}
		closeConnection();
		return str;
	}

	/**
	 * returns all that is required by ToNameSearchTree from given lastUID
	 * (exlclusive) [UID, VALID, FROM, TO, SUBJECT, DATE] all the information is
	 * unprocessed
	 * 
	 * @param lastUID
	 * @param latestUID
	 * @return
	 */
	public ArrayList<String> getAllLaterMailForToName(Long lastUID,
			Long latestUID) {
		if (lastUID == latestUID) {
			return null;
		}
		long finalPageNumber = latestUID / 100;
		long pageNumber = lastUID / 100;
		ArrayList<String> str = new ArrayList<String>();
		while (pageNumber <= finalPageNumber) {
			getAllMailFromPageForToName(pageNumber, lastUID, str);
			pageNumber++;
			lastUID = pageNumber * 100;
		}
		return str;
	}

	private ArrayList<String> getAllMailFromPageForToName(Long pageNumber,
			Long baseUID, ArrayList<String> str) {
		connectToPage("INBOX", pageNumber);
		boolean valid = false;
		long base = 0;
		while (inputPage.hasNext()) {
			if ((base = inputPage.nextLong()) >= baseUID) {
				valid = true;
				break;
			} else {
				for (int i = 0; i < 6; i++) {
					inputPage.nextLine();
				}
			}
		}

		if (valid) {
			str.add(Long.toString(base));
			inputPage.nextLine();
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			str.add(inputPage.nextLine());
			inputPage.nextLine();
			while (inputPage.hasNext()) {
				str.add(inputPage.nextLine());
				str.add(inputPage.nextLine());
				str.add(inputPage.nextLine());
				str.add(inputPage.nextLine());
				str.add(inputPage.nextLine());
				str.add(inputPage.nextLine());
				inputPage.nextLine();
			}
			closeConnection();
			return str;
		}
		closeConnection();
		return str;
	}

	/**
	 * Methods Used by GUI
	 * 
	 * @param baseUID
	 * @return
	 */

	/**
	 * returns LinkedList with atleast kmails [date, subject, from, validString,
	 * UID]
	 * 
	 * @param latestUID
	 * @param factor
	 * @return
	 */

	public LinkedList<String> getKmails(String folderName, Long ceilUID, int num) {
		if (num == 0) {
			return null;
		}
		int count = 0, prependPos = 0;
		Long base;
		LinkedList<String> mails = new LinkedList<String>();
		long pageNumber = ceilUID / 100;

		while (count < num && pageNumber >= 0) {
			if (folderName.equals("INBOX")) {
				connectToPage("INBOX", pageNumber);
			} else {
				connectToPage("outbox", pageNumber);
			}

			while (inputPage.hasNext()
					&& (base = inputPage.nextLong()).compareTo(ceilUID) <= 0) {
				mails.add(prependPos, Long.toString(base));
				inputPage.nextLine();
				mails.add(prependPos, inputPage.nextLine()); // validString
				if (folderName.equals("INBOX")) {
					mails.add(prependPos, inputPage.nextLine()); // fromAddress
					inputPage.nextLine();// toAddress
				} else {
					inputPage.nextLine();// fromAddress
					mails.add(prependPos, inputPage.nextLine()); // toAddress
				}

				mails.add(prependPos, inputPage.nextLine());
				mails.add(prependPos, inputPage.nextLine());
				inputPage.nextLine();
				count++;
			}
			closeConnection();
			ceilUID = pageNumber * 100 - 1;
			pageNumber--;
			prependPos = count * 5;
		}

		return mails;

	}

	/**
	 * closes connection with any page of the database
	 */
	public void closeConnection() {
		inputPage.close();
	}

	/**
	 * Writes all the state changes that the application has currently made to
	 * the database so that next time when the application turns on it can
	 * resume from the previous state
	 */

	public void writeStateChanges() {
		try {
			FileWriter fileWriter = new FileWriter(Include.CURDIR
					+ "/mailDB/lastUID.txt");
			fileWriter.write(this.latestUID.toString());
			fileWriter.close();
			fileWriter = new FileWriter(Include.CURDIR
					+ "/mailDB/lastSearchUID.txt");
			fileWriter.write(this.latestUID.toString());
			fileWriter.close();
			fileWriter = new FileWriter(Include.CURDIR
					+ "/mailDB/outbox/lastOutboxUID.txt");
			fileWriter.write(this.latestOutboxUID.toString());
			fileWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
