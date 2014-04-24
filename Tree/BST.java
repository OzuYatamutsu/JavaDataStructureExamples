import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The BST class implements a generic Binary Spanning Tree.
 * 
 * @author Sean Collins
 * @param <T> The Object to initialize this BST as.
 */
@SuppressWarnings("unchecked")
public class BST<T extends Comparable<T>> implements BSTInterface<T> {
	private Node<T> root;
	private int size = 0;

	@Override
	public void add(T data) {
		if (data == null) {
			throw new IllegalArgumentException("Error: Data cannot be null!");
		}
		
		if (root == null) {
			root = new Node<T>(data);
		} else {
			add(root, data); // If data exists in tree (data == rootData), don't add
		}
		
		size++;
	}
	
	/**
	 * A helper method which recursively adds to this BST.
	 *
	 * @param current The node to start from
	 * @param data The data to add to this BST
	 */
	private void add(Node<T> current, T data) {
		if (data.compareTo(current.getData()) < 0) {			
			if (current.getLeft() == null) {
				current.setLeft(new Node<T>(data));
			} else {
				add(current.getLeft(), data);
			}
		} else if (data.compareTo(root.getData()) > 0) {
			if (current.getRight() == null) {
				current.setRight(new Node<T>(data));
			} else {
				add(current.getRight(), data);
			}
		}
	}

	@Override
	public void addAll(Collection<T> c) {
		if (c == null) {
			throw new IllegalArgumentException("Error: Data cannot be null!");
		}
		
		Object[] data = c.toArray();
		
		for (int i = 0; i < data.length; i++) {
			add((T) data[i]); // What if Collection includes null entries?
		}
	}

	@Override
	public T remove(T data) {
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
			returnedData = remove(root, data);
		}
		
		return returnedData;
	}
	
	/**
	 * A helper method which recursively removes the given data 
	 * from this BST, if possible.
	 * 
	 * @param current The Node to start searching from
	 * @param data The data to remove
	 * @return The data removed, or null if the data was not found
	 */
	private T remove(Node<T> current, T data) {
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
					}
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
	 * A helper method which recursively searches for data in this BST
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
	 * a given starting point in this BST.
	 *
	 * @param current The Node to start searching from
	 * @param data The data to search for
	 * @return true if this BST contains data; else false
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
		// (ArrayList<T> b/c can't instantiate List<T> interface)
	}
	
	/**
	 * A helper method which recursively executes a pre-order BST traversal
	 * from the current Node location in this BST.
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
	 * A helper method which recursively executes an in-order BST traversal
	 * from the current Node location in this BST.
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
	 * A helper method which recursively executes a post-order BST traversal
	 * from the current Node location in this BST.
	 *
	 * @param current The Node location to start post-order traversal from
	 * @param result The List where traversal result is stored
	 * @return The provided List, with post-order elements appended to the end
	 */
	private List<T> postOrderHelper(Node<T> current, List<T> result) {
		if (current != null) {
			result = inOrderHelper(current.getLeft(), result);
			result = inOrderHelper(current.getRight(), result);
			result.add(current.getData());
		}
		
		return result;
	}

	@Override
	public List<T> levelOrder() {
		// Not implemented.
		return null;
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
	
	@Override
	public String toString() {
		if (root == null) {
			return "()";
		}
		
		return root.toString();
	}

}
