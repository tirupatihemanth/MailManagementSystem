import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ToNameSearchTree {

	Database database;
	AVLTree<ToNameMail> toNameSearchTree;

	public ToNameSearchTree(Database database) {
		this.database = database;
		if (database.lastToNameUID != 0) {
			getToNameSearchTree();
		} else {
			toNameSearchTree = new AVLTree<ToNameMail>();
		}

	}

	public void updateSearchTree() {
		Database database = new Database();
		ArrayList<String> mails = database.getAllLaterMailForToName(
				database.lastToNameUID + 1, database.latestUID);
		ToNameMail mail;
		String toNames[];
		int i = 0;

		while (i < mails.size()) {

			toNames = Include.parseRecepientsName(mails.get(i + 3));

			for (String toName : toNames) {
				for (String subToName : toName.split(" ")) {
					if (subToName.length() > 2) {
						mail = new ToNameMail(subToName, mails.get(i + 1),
								mails.get(i), mails.get(i + 2), mails
										.get(i + 4).substring(9), mails.get(
										i + 5).substring(6));
						toNameSearchTree.insert(mail);
					}
				}
			}
			i += 6;

		}
		System.out.println();
		database.lastToNameUID = database.latestUID;
	}

	public ArrayList<ToNameMail> search(String str) {

		ArrayList<Node<ToNameMail>> nodes = new ArrayList<Node<ToNameMail>>();
		toNameSearchTree.search(new ToNameMail(str, "valid: 1", "027",
				"From: from;", "subject", "date"), nodes);

		String[] subStr = str.split(" ");
		if (subStr.length > 1) {
			for (String sub : subStr) {
				toNameSearchTree.search(new ToNameMail(sub, "valid: 1", "027",
						"From: from;", "subject", "date"), nodes);
			}

		}
		ArrayList<ToNameMail> foundItems = new ArrayList<ToNameMail>();

		for (Node<ToNameMail> node : nodes) {
			for (ToNameMail mail : node.getMail()) {
				foundItems.add(mail);
			}
		}
		return foundItems;
	}

	private void getToNameSearchTree() {
		BufferedInputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(
					Include.CURDIR + "/mailDB/ToNameSearchTree.srlz"));
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
			toNameSearchTree = (AVLTree<ToNameMail>) objectStream.readObject();
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
					Include.CURDIR + "/mailDB/ToNameSearchTree.srlz", false));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(this.toNameSearchTree);
			outputStream.close();
			objectOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
