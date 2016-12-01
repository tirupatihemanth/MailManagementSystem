
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Generic AVL Tree which accepts a MailTree
 * @author HemanthKumarTirupati
 *
 * @param <T>
 */
public class AVLTree<T extends MailTree> implements Serializable {

	private static final long serialVersionUID = -8465116787318420577L;
	Node<T> root;

	public AVLTree() {

	}

	/**
	 * search for a node in the {@link AVLTree} similar to BSTree
	 * 
	 * @param data
	 * @return
	 */

	public void search(T mail, ArrayList<Node<T>> nodes) {
		System.out.println("DEBUG "+mail.getFromAddress());
		search(root, mail, nodes);
		
	}

	public void search(Node<T> node, T mail, ArrayList<Node<T>> nodes) {

		while (node != null) {
			if (node.getMail().get(0).compareWith(mail) > 0) {
				node = node.left;
			} else if (node.getMail().get(0).compareWith(mail) < 0) {
				node = node.right;
			} else {
				nodes.add(node);
				search(node.left, mail, nodes);
				search(node.right, mail, nodes);
				break;
			}
		}

	}

	public void printInorder() {
		printInorder(root);
		System.out.println();
	}

	// print the tree in breadthfirst manner
	/*
	 * public void print_breadthfirst() { if (root == null) return; else {
	 * Node[] node = new Node[count(root)]; int i = 0, n = 0; node[n++] = root;
	 * int k=0, bfsLevel=0; while (i < n) { System.out.printf("%d ",
	 * node[i].getData()); if (node[i].left != null) { node[n++] = node[i].left;
	 * } if (node[i].right != null) { node[n++] = node[i].right; } i++; if( i ==
	 * bfsLevel + Math.pow(2, k)){ System.out.printf(" # "); k++; bfsLevel = i;
	 * } }
	 * 
	 * } System.out.println(); }
	 */

	public void printInorder(Node node) {

		if (node == null) {
			return;
		}

		// System.out.println(node.getMail().get(0).toString());
		printInorder(node.getLeft());

		ArrayList<T> mails = (ArrayList<T>) node.getMail();
		for (MailTree temp : mails) {
			System.out.printf("%d ", temp.getUID());
		}

		printInorder(node.getRight());

	}

	// private function used by breadthfirst to count no. of elements in the bst
	/*
	 * private int count(Node node) { if (node == null) return 0; else return
	 * count(node.left) + 1 + count(node.right); }
	 */

	// public counterpart to find no. of nodes in the bst
	/*
	 * public int count() { return count(root); }
	 */

	public void insert(T mail) {
		insertNode(new Node(mail));
	}

	public void insertNode(Node node) {

		if (root == null) {
			root = node;
			return;
		}

		Node temp, temp1;
		temp = temp1 = root;

		while (temp != null) {
			temp1 = temp;
			if (temp.compareTo(node) > 0) {
				temp = temp.getLeft();
			} else if (temp.compareTo(node) < 0) {
				temp = temp.getRight();
			} else {
				temp.getMail().add(node.getMail().get(0));
				return;
			}
		}

		if (temp1.compareTo(node) > 0) {
			temp1.left = node;
			node.parent = temp1;
		} else {
			temp1.right = node;
			node.parent = temp1;
		}

		correctHeights(node.parent);
		correctAVLTree(temp1);

	}

	public void delete(T mail) {
		ArrayList<Node<T>> nodes = new ArrayList<Node<T>>();
		search(mail, nodes);
		for (Node node : nodes) {
			if (node.getMail().size() >= 1) {
				for (T temp : (ArrayList<T>) node.getMail()) {
					if (temp.getUID().equals(mail.getUID())) {
						node.getMail().remove(temp);
					}
				}
			} else if (node.getMail().size() == 1) {
				ArrayList<T> temp = node.getMail();
				if (temp.get(0).getUID().equals(mail.getUID())) {
					delete(node);
				}
			}

		}
	}

	public void delete(Node node) {

		Node removeNode = null;
		if (node.parent == null && node.left == null && node.right == null) {
			root = null;
			return;
		}

		if (!(node.left == null || node.right == null)) {
			removeNode = node.left;

			while (removeNode.right != null) {
				removeNode = removeNode.right;
			}
			swapValues(node, removeNode);
			node = removeNode;
			// System.out.println();
		}

		if (node.left != null && node.right == null) {
			if (node.parent.left == node) {
				node.parent.left = node.left;
				node.left.parent = node.parent;
			} else {
				node.parent.right = node.left;
				node.left.parent = node.parent;
			}
		} else if (node.right != null && node.left == null) {
			if (node.parent.left == node) {
				node.parent.left = node.right;
				node.right.parent = node.parent;
			} else {
				node.parent.right = node.right;
				node.right.parent = node.parent;
			}
		} else {
			if (node.parent.left == node) {
				node.parent.left = null;
			} else {
				node.parent.right = null;
			}
		}

		correctAVLTree(node.parent);

	}

	/**
	 * 
	 * <p>
	 * Used by the insert method to correct all the height values which gets
	 * changed after insertion and does appropriate rotations to make it
	 * balanced
	 * </p>
	 * 
	 * @param node
	 *            <p>
	 *            Here node is nothing but the ancestor to the node which is
	 *            inserted
	 *            </p>
	 * 
	 */

	private void correctAVLTree(Node node) {

		int branchFactor;
		while (node != null) {
			node.setHeight(getHeight(node));
			branchFactor = getBranchFactor(node);

			if (branchFactor == 2) {
				if (getBranchFactor(node.left) == -1) {
					leftRotate(node.left);
				}
				rightRotate(node);

			} else if (branchFactor == -2) {
				if (getBranchFactor(node.right) == 1) {
					rightRotate(node.right);
				}
				leftRotate(node);
			}

			node = node.getParent();
		}

	}

	private void correctHeights(Node node) {
		while (!(node == null)) {
			node.setHeight(getHeight(node));
			node = node.getParent();
		}
	}

	private int max(int n1, int n2) {

		return n1 > n2 ? n1 : n2;

	}

	/*
	 * updates the height of a node
	 */

	private int getHeight(Node node) {

		if (node == null) {
			return 0;
		}

		if (!(node.left == null || node.right == null)) {
			return max(node.left.getHeight(), node.right.getHeight()) + 1;
		} else {
			if (node.left != null) {
				return node.left.getHeight() + 1;
			} else if (node.right != null) {
				return node.right.getHeight() + 1;
			}
		}

		return 1;

	}

	private int getBranchFactor(Node node) {

		if (!(node.left == null || node.right == null)) {
			return node.left.getHeight() - node.right.getHeight();
		} else {
			if (node.left != null) {
				return node.left.getHeight();
			} else if (node.right != null) {
				return -node.right.getHeight();
			}
		}

		return 0;
	}

	private void leftRotate(Node node) {

		Node temp = node.right;
		if (temp.left != null) {
			temp.left.parent = node;
		}
		node.right = node.right.left;
		temp.left = node;
		temp.parent = node.parent;
		if (node.parent != null) {
			if (node.parent.left == node) {
				node.parent.left = temp;
			} else {
				node.parent.right = temp;
			}
		} else {
			root = temp;
		}
		node.parent = temp;
		node.setHeight(getHeight(node));
		temp.setHeight(getHeight(temp));
		// System.out.println();
	}

	private void rightRotate(Node node) {

		Node temp = node.left;
		if (temp.right != null) {
			temp.right.parent = node;
		}
		node.left = node.left.right;
		temp.right = node;
		temp.parent = node.parent;
		if (node.parent != null) {
			if (node.parent.left == node) {
				node.parent.left = temp;
			} else {
				node.parent.right = temp;
			}
		} else {
			root = temp;
		}

		node.parent = temp;

		node.setHeight(getHeight(node));
		temp.setHeight(getHeight(temp));
		// System.out.println();
	}

	/**
	 * <p>
	 * Swaps all fields in node1 object and node2 object
	 * </p>
	 * 
	 * @param node1
	 * @param node2
	 */
	private void swapValues(Node node1, Node node2) {

		ArrayList<T> mail;
		mail = node1.getMail();
		node1.setMail(node2.getMail());
		node2.setMail(mail);

	}

}
