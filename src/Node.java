
import java.io.Serializable;
import java.util.ArrayList;


/**
 * A Generic node for any search Tree implemented
 * @author HemanthKumarTirupati
 *
 * @param <T>
 */
public class Node <T extends MailTree> implements Comparable<Node>, Serializable{

	private static final long serialVersionUID = -8465116787318420577L;
	private ArrayList<T> mail;
	private int height;
	Node<T> left, right, parent;
	
	public Node(T mail){
		this.mail = new ArrayList<T>();
		this.mail.add(mail);
		this.height = 1;
	}

	public ArrayList<T> getMail() {
		return this.mail;
	}

	public void addMail(T mail) {
		this.mail.add(mail);
	}
	
	public void setMail(ArrayList<T> mail){
		this.mail = mail;
	}
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Node<T> getLeft() {
		return left;
	}

	public void setLeft(Node<T> left) {
		this.left = left;
	}

	public Node<T> getRight() {
		return right;
	}

	public void setRight(Node<T> right) {
		this.right = right;
	}

	public Node<T> getParent() {
		return parent;
	}

	public void setParent(Node<T> parent) {
		this.parent = parent;
	}

	@Override
	public int compareTo(Node node) {
		MailTree mailTree = (MailTree) node.getMail().get(0);
		return this.getMail().get(0).compareTo(mailTree);
	}
	
}
