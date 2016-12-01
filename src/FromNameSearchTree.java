
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

/**
 * Similar to FromSearchTree but searches for the given string amongst or in the names of from addresses
 * @author HemanthKumarTirupati
 *
 */

public class FromNameSearchTree {

	Database database;
	AVLTree<FromNameMail> fromNameSearchTree;
	public FromNameSearchTree(Database database){
		this.database = database;
		if(database.lastFromNameUID!=0){
			getFromNameSearchTree();
		}
		else{
			fromNameSearchTree = new AVLTree<FromNameMail>();
		}
		
	}
	
	public void updateSearchTree(){
		Database database= new Database();
		ArrayList<String> mails = database.getAllLaterMailForFromName(database.lastFromNameUID+1, database.latestUID);
		FromNameMail mail;
		String subFromNames[];
		int i=0;
		//System.out.println(mails.size());
		
		while(i<mails.size()){
			
			subFromNames = Include.parseName(mails.get(i+2),6).split(" ");
			
			for(String subFromName: subFromNames){
				if(subFromName.length() > 2){
					mail = new FromNameMail(subFromName, mails.get(i+1), mails.get(i), mails.get(i+2), mails.get(i+3).substring(9), mails.get(i+4).substring(6));
					fromNameSearchTree.insert(mail);
				}
			}
			i+=5;
			
		}
		System.out.println();
		database.lastFromNameUID = database.latestUID;
	}
	
	public ArrayList<FromNameMail> search(String str){
		
		ArrayList<Node<FromNameMail>> nodes = new ArrayList<Node<FromNameMail>>();
		fromNameSearchTree.search(new FromNameMail(str, "valid: 1", "027", "From: from;", "subject", "date"), nodes);
		
		String[] subStr = str.split(" ");
		if(subStr.length>1){
			for(String sub : subStr){
				fromNameSearchTree.search(new FromNameMail(sub, "valid: 1", "027", "From: from;", "subject", "date"), nodes);
			}
		}
		
		ArrayList<FromNameMail> foundItems = new ArrayList<FromNameMail>();
		
		for(Node<FromNameMail> node: nodes){
			for(FromNameMail mail: node.getMail()){
				foundItems.add(mail);
			}
		}
		
		return foundItems;
	}
	
	
	private void getFromNameSearchTree(){
		BufferedInputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream(Include.CURDIR+"/mailDB/FromNameSearchTree.srlz"));
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
			fromNameSearchTree = (AVLTree<FromNameMail>) objectStream.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeStateChanges(){
		if(!database.isUpdated){
			//return;
		}
		BufferedOutputStream outputStream = null;
		try {
			outputStream = new BufferedOutputStream(new FileOutputStream(Include.CURDIR+"/mailDB/FromNameSearchTree.srlz", false));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectOutputStream objectOutputStream;
		try {
			objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(this.fromNameSearchTree);
			outputStream.close();
			objectOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}
