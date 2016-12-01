import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Subject Search Tree class which is used to search for a string in subjects of
 * all mails
 * 
 * @author HemanthKumarTirupati
 */

public class SubjectSearchTree {

	Database database;
	AVLTree<SubjectMail> subjectSearchTree;

	public SubjectSearchTree(Database database) {
		this.database = database;
		if (database.lastSubjectUID != 0) {
			getSubjectSearchTree();
		} else {
			subjectSearchTree = new AVLTree<SubjectMail>();
		}

	}

	/**
	 * Adds nodes to the given tree from the previous state to the latest update
	 * made in database
	 */
	public void updateSearchTree() {
		Database database = new Database();
		ArrayList<String> mails = database.getAllLaterMailForSubject(
				database.lastSubjectUID + 1, database.latestUID);
		SubjectMail mail;
		String subSubjects[];
		int i = 0;
		// System.out.println(mails.size());

		while (i < mails.size()) {

			subSubjects = Include.parseSubject(mails.get(i + 3));

			for (String subSubject : subSubjects) {
				if (subSubject.length() > 2) {
					mail = new SubjectMail(subSubject, mails.get(i + 1),
							mails.get(i), mails.get(i + 2), mails.get(i + 3),
							mails.get(i + 4).substring(9).substring(6));
					subjectSearchTree.insert(mail);
				}
			}
			i += 5;

		}

		database.lastSubjectUID = database.latestUID;
	}

	/**
	 * search for a string in the Subject Search Tree and gives mails in return
	 * 
	 * @param str
	 * @return
	 */
	public ArrayList<SubjectMail> search(String str) {

		ArrayList<Node<SubjectMail>> nodes = new ArrayList<Node<SubjectMail>>();
		subjectSearchTree.search(new SubjectMail(str, "valid: 1", "027",
				"From: from;", "subject", "date"), nodes);
		String[] subStr = str.split(" ");
		if (subStr.length > 1) {
			for (String sub : subStr) {
				subjectSearchTree.search(new SubjectMail(sub, "valid: 1",
						"027", "From: from;", "subject", "date"), nodes);
			}
		}

		ArrayList<SubjectMail> foundItems = new ArrayList<SubjectMail>();
		for (Node<SubjectMail> node : nodes) {
			for (SubjectMail mail : node.getMail()) {
				foundItems.add(mail);
			}
		}

		Collections.sort(foundItems, new Comparator<SubjectMail>() {

			@Override
			public int compare(SubjectMail o1, SubjectMail o2) {
				// TODO Auto-generated method stub
				return o1.getUID().compareTo(o2.getUID());
			}
		});

		SubjectMail prevMail = null, mail;
		Iterator<SubjectMail> iterator = foundItems.iterator();
		while (iterator.hasNext()) {
			mail = iterator.next();
			if (prevMail != null
					&& prevMail.getUID().compareTo(mail.getUID()) == 0) {
				iterator.remove();
			} else {
				prevMail = mail;
			}
		}
		return foundItems;
	}

	private void getSubjectSearchTree() {
		BufferedInputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(
					Include.CURDIR + "/mailDB/SubjectSearchTree.srlz"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ObjectInputStream objectStream = null;
		try {
			objectStream = new ObjectInputStream(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			subjectSearchTree = (AVLTree<SubjectMail>) objectStream
					.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeStateChanges() {
		if (!database.isUpdated) {
			// return;
		}
		BufferedOutputStream outputStream = null;
		try {
			outputStream = new BufferedOutputStream(new FileOutputStream(
					Include.CURDIR + "/mailDB/SubjectSearchTree.srlz", false));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(this.subjectSearchTree);
			outputStream.close();
			objectOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
