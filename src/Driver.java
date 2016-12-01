
import java.util.ArrayList;

/**
 * Driver class for the mailing application
 * 
 * @author HemanthKumarTirupati *** First look at documentation in other classes
 *         before using this class***
 */
public class Driver {

	/**
	 * main function for the mailing application
	 * 
	 * @param args
	 */

	public static void main(String[] args) {

		/**
		 * TODO: Segregate all the below tasks into threads later
		 */

		/**
		 * Establishes connection with smtpServer and imapServer and keeps the
		 * sessions ready
		 */
		
		
		

		Include.setMyEmail("tirupatihemanthkumar@gmail.com");
		Include.setPassword("buddigadu");
		Include.setUserName("tirupatihemanthkumar");
		Include.setIMAPSession();
		Include.setSMTPSession();

		/**
		 * Initialize all the housekeeping stuff required by the application
		 */

		Database database = new Database();

		/**
		 * Establishes connection with the Store and fills up the boxes
		 */

		ReadMail readMail = new ReadMail();

		database.updateDB("inbox");
		database.updateDB("outbox");

		

		FromSearchTree fromSearchTree = new FromSearchTree(database);

		fromSearchTree.updateSearchTree();
		ArrayList<FromMail> fromMail = fromSearchTree.search("cs13b010@smail.iitm.ac.in");
		System.out.println("FromMail: "+fromMail.size());
		for (FromMail mail : fromMail) {
			System.out.println("fromUID: " + mail.getUID() + " "
					+ mail.getFromAddress());
		}

		ToSearchTree toSearchTree = new ToSearchTree(database);
		toSearchTree.updateSearchTree();
		ArrayList<ToMail> toMail = toSearchTree.search("kumudkumartirupati@gmail.com");
		System.out.println("toMail: " + toMail.size());
		for (ToMail mail : toMail) {
			System.out.println("toUID: " + mail.getUID() + " "
					+ mail.getToAddress());
		}

		SubjectSearchTree subjectSearchTree = new SubjectSearchTree(database);
		subjectSearchTree.updateSearchTree();
		ArrayList<SubjectMail> subjectMail = subjectSearchTree
				.search("hemanth kumar");

		for (SubjectMail mail : subjectMail) {
			System.out.println("subjectUID: " + mail.getUID() + " "
					+ mail.getSubSubject());
		}

		FromNameSearchTree fromNameSearchTree = new FromNameSearchTree(database);
		fromNameSearchTree.updateSearchTree();
		ArrayList<FromNameMail> fromNameMail = fromNameSearchTree
				.search("kumar");

		for (FromNameMail mail : fromNameMail) {
			System.out.println("fromNameUID: " + mail.getUID() + " "
					+ mail.getFromAddress());
		}

		ToNameSearchTree toNameSearchTree = new ToNameSearchTree(database);
		toNameSearchTree.updateSearchTree();
		ArrayList<ToNameMail> toNameMail = toNameSearchTree.search("vamshi");


		for (ToNameMail mail : toNameMail) {
			System.out.println("toNameUID: " + mail.getUID() + " "
					+ mail.getSubject());
		}
	
		database.writeStateChanges();
		fromSearchTree.writeStateChanges();
		toSearchTree.writeStateChanges();
		subjectSearchTree.writeStateChanges();
		fromNameSearchTree.writeStateChanges();
		toNameSearchTree.writeStateChanges();


		
		for(ToNameMail mail: toNameMail){
			System.out.println("toNameUID: " + mail.getUID() +" "+ mail.getSubject());
		}
		
		fromSearchTree.writeStateChanges();
		toSearchTree.writeStateChanges();
		subjectSearchTree.writeStateChanges();
		fromNameSearchTree.writeStateChanges();
		toNameSearchTree.writeStateChanges();

		database.writeStateChanges();
		}
		
	}

