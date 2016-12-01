import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Search Tree for From Addresses
 * 
 * @author HemanthKumarTirupati
 *
 */

public class FromSearchTree {

	AVLTree<FromMail> fromSearchTree;
	Database database;

	public FromSearchTree(Database database) {
		this.database = database;

		if (Database.lastFromUID != 0) {
			getFromSearchTree();
		} else {
			fromSearchTree = new AVLTree<FromMail>();
		}

	}

	/**
	 * Adds nodes to the given from search tree from the updated database
	 */

	public void updateSearchTree() {
		if (database.lastFromUID.equals(database.lastUID)) {
			return;
		}
		ArrayList<String> mails = database.getAllLaterMailForFrom(
				database.lastFromUID + 1, database.latestUID);
		FromMail mail;
		int i = 0;
		// System.out.println(mails.size());
		while (i < mails.size()) {
			if (mails.get(i + 2).length() == 6) {
				i += 5;
				continue;
			}
			mail = new FromMail(mails.get(i + 2), mails.get(i + 1),
					mails.get(i), mails.get(i + 3).substring(9), mails.get(
							i + 4).substring(6));
			i += 5;
			fromSearchTree.insert(mail);
		}

		database.lastFromUID = database.latestUID;

	}

	/**
	 * search for a given string in all the from address of the inbox
	 * 
	 * @param str
	 * @return
	 */
	public ArrayList<FromMail> search(String str) {

		ArrayList<Node<FromMail>> nodes = new ArrayList<Node<FromMail>>();
		fromSearchTree.search(new FromMail("From: " + str + ";", "valid: 1",
				"027", "subject", "date"), nodes);
		ArrayList<FromMail> foundItems = new ArrayList<FromMail>();
		for (Node<FromMail> node : nodes) {
			for (FromMail mail : node.getMail()) {
				foundItems.add(mail);
			}
		}
		return foundItems;
	}

	/**
	 * Get the search tree from a serialized file instead of constructing tree
	 * each time
	 */

	private void getFromSearchTree() {
		BufferedInputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(
					Include.CURDIR + "/mailDB/fromSearchTree.srlz"));
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
			fromSearchTree = (AVLTree<FromMail>) objectStream.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * writes about the lastest state of the tree for easier resume when
	 * restarted
	 */
	public void writeStateChanges() {

		if (!database.isUpdated) {
			// return;
		}
		BufferedOutputStream outputStream = null;
		try {
			outputStream = new BufferedOutputStream(new FileOutputStream(
					Include.CURDIR + "/mailDB/fromSearchTree.srlz", false));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(fromSearchTree);
			objectOutputStream.flush();
			outputStream.close();
			objectOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
