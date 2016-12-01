import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ToSearchTree {

	Database database;
	AVLTree<ToMail> toSearchTree;

	public ToSearchTree(Database database) {
		this.database = database;
		if (database.lastToUID != 0) {
			getToSearchTree();
		} else {
			toSearchTree = new AVLTree<ToMail>();
		}

	}

	public void updateSearchTree() {
		Database database = new Database();
		ArrayList<String> mails = database.getAllLaterMailForTo(
				database.lastToUID + 1, database.latestUID);
		ToMail mail;
		String toAddress[];
		int i = 0;
		// System.out.println(mails.size());
		while (i < mails.size()) {

			toAddress = Include.parseRecepientsAddress(mails.get(i + 3));
			for (String recepient : toAddress) {
				mail = new ToMail(recepient, mails.get(i + 1), mails.get(i),
						mails.get(i + 2), mails.get(i + 4).substring(9), mails
								.get(i + 5).substring(6));
				toSearchTree.insert(mail);
			}
			i += 6;
		}

		database.lastToUID = database.lastUID;
	}

	public ArrayList<ToMail> search(String str) {

		ArrayList<Node<ToMail>> nodes = new ArrayList<Node<ToMail>>();
		toSearchTree.search(new ToMail(str, "valid: 1", "027", "From: from;",
				"subject", "date"), nodes);
		ArrayList<ToMail> foundItems = new ArrayList<ToMail>();
		for (Node<ToMail> node : nodes) {
			for (ToMail mail : node.getMail()) {
				foundItems.add(mail);
			}
		}
		return foundItems;
	}

	private void getToSearchTree() {
		BufferedInputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(
					Include.CURDIR + "/mailDB/toSearchTree.srlz"));
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
			toSearchTree = (AVLTree<ToMail>) objectStream.readObject();
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
					Include.CURDIR + "/mailDB/toSearchTree.srlz", false));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(this.toSearchTree);
			outputStream.close();
			objectOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
