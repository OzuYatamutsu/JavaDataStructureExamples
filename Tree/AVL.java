import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * The AVL class implements a generic AVL tree - a self-balancing BST.
 *
 * @author Sean Collins
 * @param <T> The Object to initialize this AVL tree as.
 */
@SuppressWarnings("unchecked")
public class AVL<T extends Comparable<? super T>> implements BSTInterface<T> {
	private Node<T> root;
	private int size = 0;

	@Override
	public void add(T data) {
		ArrayList<Node<T>> affectedNodes = new ArrayList<Node<T>>();
		if (data == null) {
			throw new IllegalArgumentException("Error: Data cannot be null!");
		}
		
		if (root == null) {
			root = new Node<T>(data);
		} else {
			affectedNodes = add(root, data, affectedNodes); 
		}
		
		if (affectedNodes != null) {
			size++;
			updateHeightAndBalanceFactor(affectedNodes);
			// Check balance along path and rebalance if necessary
			rebalance(affectedNodes);
			// Now need to update height/balance factor again
			updateHeightAndBalanceFactor(affectedNodes);
			rebalance(affectedNodes);
		}
	}
	
	/**
	 * A helper method which recursively adds to this AVL tree.
	 *
	 * @param current The node to start from
	 * @param data The data to add to this AVL tree
	 * @param rightPath The boolean list which contains the number of right-paths taken
	 */
	private ArrayList<Node<T>> add(Node<T> current, T data, ArrayList<Node<T>> affectedNodes) {
		affectedNodes.add(current);
		if (data.compareTo(current.getData()) < 0) {			
			if (current.getLeft() == null) {
				current.setLeft(new Node<T>(data));
				current.getLeft().setHeight(1);
				affectedNodes.add(current.getLeft());
			} else {
				affectedNodes = add(current.getLeft(), data, affectedNodes);
			}
		} else if (data.compareTo(current.getData()) > 0) {
			if (current.getRight() == null) {
				current.setRight(new Node<T>(data));
				current.getRight().setHeight(1);
				affectedNodes.add(current.getRight());
			} else {
				affectedNodes = add(current.getRight(), data, affectedNodes);
			}
		} else {
			// If data exists in tree (data == rootData), don't add
			// Also return nothing for the path
			return null;
		}
		
		return affectedNodes;
	}
	
	/**
	 * A helper method which updates the height and balance factor of affected nodes, post-add.
	 * 
	 * @param affectedNodes The Nodes to update
	 */
	private void updateHeightAndBalanceFactor(ArrayList<Node<T>> affectedNodes) {
		for (Node<T> node : affectedNodes) {
			if (node.getLeft() == null && node.getRight() == null) {
				node.setHeight(1);
			} else if (node.getLeft() == null && node.getRight() != null) {
				node.setHeight(node.getRight().getHeight() + 1);
			} else if (node.getLeft() != null && node.getRight() == null) {
				node.setHeight(node.getLeft().getHeight() + 1);
			} else {
				node.setHeight((
					node.getLeft().getHeight() > node.getRight().getHeight() 
					? node.getLeft().getHeight() : node.getRight().getHeight()
					) + 1);
			}
		} // After height is updated, update balance factor
		
		for (Node<T> node : affectedNodes) {
			if (node.getLeft() == null) {
				if (node.getRight() == null) {
					node.setBalanceFactor(0);
				} else {
					node.setBalanceFactor(0 - node.getRight().getHeight());
				}
			} else if (node.getRight() == null) {
				if (node.getLeft() == null) {
					node.setBalanceFactor(0);
				} else {
					node.setBalanceFactor(node.getLeft().getHeight());
				}
			} else {
				node.setBalanceFactor(node.getLeft().getHeight() - node.getRight().getHeight());
			}
		}
	}

	/**
	 * A helper method which determines whether a rebalance is needed along a given path.
	 * 
	 * @param affectedNodes The Nodes to check.
	 */
	private void rebalance(ArrayList<Node<T>> affectedNodes) {
		for (Node<T> node : affectedNodes) {
			if (node.getBalanceFactor() < -1) {
				if (node.getRight() != null && node.getRight().getBalanceFactor() > 0) {
					// Double rotate: Right + Left
					//rotateRight(node.getRight(), node.getRight().getLeft(), node, true);
					node.setRight(rotateRight(node.getRight()));
				}
				
				// Rotate: Left
				rotateLeft(node);
				

			} else if (node.getBalanceFactor() > 1) {
				if (node.getLeft() != null && node.getLeft().getBalanceFactor() < 0) {
					// Double rotate: Left + Right
					node.setLeft(rotateLeft(node.getLeft()));
				}
				
				// Rotate: Right
				rotateRight(node);
			}
			
			
		}
	}
	
	/**
	 * Rotates an unbalanced subtree to the left.
	 *
	 * @param top The current root of the subtree (will be leaf)
	 * @return The new root of this subtree
	 */
	private Node<T> rotateLeft(Node<T> top) {
		Node<T> mid = top.getRight();
		top.setRight(mid.getLeft());
		mid.setLeft(top);
		if (root.getData().equals(top.getData())) {
			root = mid;
		}
		
		// Mid is new root
		return mid;
	}
	
	/**
	 * Rotates an unbalanced subtree to the right.
	 *
	 * @param top The current root of the subtree (will be leaf)
	 * @return The new root of this subtree
	 */
	private Node<T> rotateRight(Node<T> top) {
		Node<T> mid = top.getLeft();
		top.setRight(mid.getRight());
		mid.setRight(top);
		top.setLeft(null);
		if (root.getData().equals(top.getData())) {
			root = mid;
		}
		
		// Mid is new root
		return mid;
	}
	
	
	@Override
	public void addAll(Collection<T> c) {
		if (c == null) {
			throw new IllegalArgumentException("Error: Data cannot be null!");
		}
		
		Object[] data = c.toArray();
		
		for (int i = 0; i < data.length; i++) {
			add((T) data[i]);
		}
	}

	@Override
	public T remove(T data) {
		ArrayList<Node<T>> affectedNodes = new ArrayList<Node<T>>();
		// 3 cases: Removing with no children, removing with one child, removing with 2 children
		// If no children, remove node
		// If one child, set current node to child
		T returnedData = null;
		if (data == null) {
			throw new IllegalArgumentException("Error: Data cannot be null!");
		}
		
		if (root != null && root.getData().equals(data)) {
			returnedData = root.getData();
			
			if (root.getLeft() != null && root.getRight() == null) {
				root.setData(root.getLeft().getData());
				root.setLeft(null);
			} else if (root.getRight() != null && root.getLeft() == null) {
				root.setData(root.getRight().getData());
				root.setRight(null);
			} else if (root.getLeft() != null && root.getRight() != null) {
				Node<T> currentNode = root.getRight();
				if (currentNode.getLeft() == null) {
					root.setData(currentNode.getData());
					root.setRight(currentNode.getRight());
				} else {
					while (currentNode.getLeft().getLeft() != null) {
						currentNode = currentNode.getLeft();
					}
					
					root.setData(currentNode.getLeft().getData());
					currentNode.setLeft(null);
				}
				
			} else {
				root = null;
			}
			
			size--;
		} else {
			returnedData = removeHelper(root, data);
		}
		
		// Whole tree is affected by remove!
		affectedNodes = addAll(root, affectedNodes);
		// Rebalance!
		updateHeightAndBalanceFactor(affectedNodes);
		// Check balance along path and rebalance if necessary
		rebalance(affectedNodes);
		// Now need to update height/balance factor again
		updateHeightAndBalanceFactor(affectedNodes);
		rebalance(affectedNodes);
		
		return returnedData;
	}
	
	/**
	 * Recursively converts the entire AVL to an ArrayList.
	 * @param current The Node to start from
	 * @param list The list to add to
	 * @return The completed ArrayList
	 */
	private ArrayList<Node<T>> addAll(Node<T> current, ArrayList<Node<T>> list) {
		list.add(current);
		if (current.getLeft() != null) {
			list = addAll(current.getLeft(), list);
		}
		
		if (current.getRight() != null) {
			list = addAll(current.getRight(), list);
		}
		
		return list;
	}
	
	/**
	 * Recursively removes given data from the given position in this AVL.
	 * @param current The Node to start searching from
	 * @param data The data to remove
	 * @return The removed data, or null if it was not found
	 */
	private T removeHelper(Node<T> current, T data) {
		T removedData = null;
		
		if (current != null) {
			if (current.getLeft() != null 
					&& data.compareTo(current.getLeft().getData()) <= 0) {
				if (data.equals(current.getLeft().getData())) {
					// Found data is to the left of this one
					removedData = current.getLeft().getData();
					size--;
					if (current.getLeft().getLeft() != null 
							&& current.getLeft().getRight() == null) {
						// Found data (left) has one child (left), promote it
						current.setLeft(current.getLeft().getLeft());
					} else if (current.getLeft().getRight() != null 
							&& current.getLeft().getLeft() == null) {
						// Found data (left) has one child (right), promote it
						current.setLeft(current.getLeft().getRight());
					} else if (current.getLeft().getLeft() != null
							&& current.getLeft().getRight() != null) {
						// Found data (left) has two children
						// Use successor method (smallest on right subtree)
						Node<T> currentSuccessor = current.getLeft().getRight();
						if (currentSuccessor.getLeft() == null) {
							current.getLeft().setData(currentSuccessor.getData());
							current.getLeft().setRight(null);
						} else {
							while (currentSuccessor.getLeft().getLeft() != null) {
								currentSuccessor = currentSuccessor.getLeft();
							}
							
							current.getLeft().setData(currentSuccessor.getLeft().getData());
							current.getLeft().setRight(currentSuccessor.getRight());
							currentSuccessor.setLeft(null);	
						}
					} else {
						// Found data (left) has no children, remove it
						current.setLeft(null);
					} 
				} else {
					removedData = removeHelper(current.getLeft(), data);
				}
			} else if (current.getRight() != null 
						&& data.compareTo(current.getRight().getData()) >= 0) {
					if (data.equals(current.getRight().getData())) {
						// Found data is to the right of this one
						removedData = current.getRight().getData();
						size--;
						if (current.getRight().getLeft() != null 
								&& current.getRight().getRight() == null) {
							// Found data (right) has one child (left), promote it
							current.setRight(current.getRight().getLeft());
						} else if (current.getRight().getRight() != null 
								&& current.getRight().getLeft() == null) {
							// Found data (left) has one child (right), promote it
							current.setRight(current.getRight().getRight());
						} else if (current.getRight().getLeft() != null
								&& current.getRight().getRight() != null) {
							// Found data (right) has two children
							// Use successor method (smallest on right subtree)
							Node<T> currentSuccessor = current.getLeft().getRight();
							if (currentSuccessor.getLeft() == null) {
								current.getRight().setData(currentSuccessor.getData());
								current.getRight().setRight(null);
							} else {
								while (currentSuccessor.getRight().getLeft() != null) {
									currentSuccessor = currentSuccessor.getLeft();
								}
								
								current.getRight().setData(currentSuccessor.getLeft().getData());
								current.getRight().setRight(currentSuccessor.getRight());
								currentSuccessor.setLeft(null);
							}
						} else {
							// Found data (right) has no children, remove it
							current.setRight(null);
						}
					} else {
						removedData = removeHelper(current.getRight(), data);
					}
				}
			}
		
		return removedData;
	}

	@Override
	public T get(T data) {
		if (data == null) {
			throw new IllegalArgumentException("Error: Data cannot be null!");
		}
		
		return getHelper(root, data);
	}
	
	/**
	 * A helper method which recursively searches for data in this AVL tree
	 * and attempts to return the data which it finds.
	 * 
	 * @param current The Node to start searching from
	 * @param data The data to search for
	 * @return The data contained in the Node, if found; else null
	 */
	private T getHelper(Node<T> current, T data) {
		T foundData = null;
		if (current != null) {
			if (data.equals(current.getData())) {
				foundData = current.getData();
			} else if (data.compareTo(current.getData()) < 0) {
				foundData = getHelper(current.getLeft(), data);
			} else if (data.compareTo(current.getData()) > 0) {
				foundData = getHelper(current.getRight(), data);
			}
		} 
		
		return foundData;
	}

	@Override
	public boolean contains(T data) {
		if (data == null) {
			throw new IllegalArgumentException("Error: Data cannot be null!");
		}
		
		return contains(root, data);
	}
	
	/**
	 * A helper method which recursively searches for data from
	 * a given starting point in this AVL tree.
	 *
	 * @param current The Node to start searching from
	 * @param data The data to search for
	 * @return true if this AVL tree contains data; else false
	 */
	private boolean contains(Node<T> current, T data) {
		boolean found = false;
		
		if (current != null) {
			if (data.equals(current.getData())) {
				found = true;
			} else if (data.compareTo(current.getData()) < 0) {
				found = contains(current.getLeft(), data);
			} else if (data.compareTo(current.getData()) > 0) {
				found = contains(current.getRight(), data);
			}
		}
		
		return found;
	}

	@Override
	public List<T> preOrder() {
		return preOrderHelper(root, new ArrayList<T>()); 
	}
	
	/**
	 * A helper method which recursively executes a pre-order AVL tree traversal
	 * from the current Node location in this AVL tree.
	 *
	 * @param current The Node location to start pre-order traversal from
	 * @param result The List where traversal result is stored
	 * @return The provided List, with pre-order elements appended to the end
	 */
	private List<T> preOrderHelper(Node<T> current, List<T> result) {
		if (current != null) {
			result.add(current.getData());
			result = preOrderHelper(current.getLeft(), result);
			result = preOrderHelper(current.getRight(), result);
		}
		
		return result;
	}

	@Override
	public List<T> inOrder() {
		return inOrderHelper(root, new ArrayList<T>()); 
	}
	
	/**
	 * A helper method which recursively executes an in-order AVL tree traversal
	 * from the current Node location in this AVL tree.
	 *
	 * @param current The Node location to start in-order traversal from
	 * @param result The List where traversal result is stored
	 * @return The provided List, with in-order elements appended to the end
	 */
	private List<T> inOrderHelper(Node<T> current, List<T> result) {
		if (current != null) {
			result = inOrderHelper(current.getLeft(), result);
			result.add(current.getData());
			result = inOrderHelper(current.getRight(), result);
		}
		
		return result;
	}

	@Override
	public List<T> postOrder() {
		return postOrderHelper(root, new ArrayList<T>()); 
	}
	
	/**
	 * A helper method which recursively executes a post-order AVL tree traversal
	 * from the current Node location in this AVL tree.
	 *
	 * @param current The Node location to start post-order traversal from
	 * @param result The List where traversal result is stored
	 * @return The provided List, with post-order elements appended to the end
	 */
	private List<T> postOrderHelper(Node<T> current, List<T> result) {
		if (current != null) {
			result = postOrderHelper(current.getLeft(), result);
			result = postOrderHelper(current.getRight(), result);
			result.add(current.getData());
		}
		
		return result;
	}

	@Override
	public List<T> levelOrder() {
		Queue<Node<T>> traversalQueue = new LinkedList<Node<T>>();
		ArrayList<T> levelOrder = new ArrayList<T>();
		Node<T> current;
		traversalQueue.add(root);
		while (!traversalQueue.isEmpty()) {
			current = traversalQueue.remove();
			levelOrder.add(current.getData());
			
			if (current.getLeft() != null) {
				traversalQueue.add(current.getLeft());
			}
			
			if (current.getRight() != null) {
				traversalQueue.add(current.getRight());
			}
			
		}
		
		return levelOrder;
	}

	@Override
	public boolean isEmpty() {
		return (root == null);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void clear() {
		root = null;
		size = 0;
	}

	//DO NOT MODIFY OR USE ANY OF THE METHODS BELOW IN YOUR IMPLEMENTATION
	//These methods are for testing purposes only
	public Node<T> getRoot() {
		return root;
	}

	public void setRoot(Node<T> root) {
		this.root = root;
	}
}
