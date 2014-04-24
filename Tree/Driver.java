import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Implements a theoretical scalable structure for 
 * social networking via a BST.
 * 
 * @author Sean Collins
 */
public class Driver {

	/**
	 * The entry point to the application.
	 * 
	 * @param args Optional arguments
	 */
	public static void main(String[] args) {
		BST<User> network = new BST<User>();
		boolean inMenu = true;
		Scanner inputScanner = new Scanner(System.in);
		String userInput = "";
		String username, name;
		
		while (inMenu) {
			System.out.println("\nPlease enter a command: ");
			userInput = inputScanner.nextLine().toLowerCase();
			
			switch (userInput) {
			case "add":
				System.out.println("\nPlease enter a username: ");
				username = inputScanner.nextLine();
				System.out.println("\nPlease enter a name: ");
				name = inputScanner.nextLine();
				network.add(new User(username, name));
				break;
			case "remove":
				System.out.println("\nPlease enter username you would like to remove: ");
				username = inputScanner.nextLine();
				if (network.remove(new User(username, "")) != null) {
					System.out.println("Remove was successful!");
				} else {
					System.out.println("User does not exist!");
				}
				break;
			case "find":
				System.out.println("\nPlease enter a username you would like to find: ");
				username = inputScanner.nextLine();
				if (network.contains(new User(username, ""))) {
					System.out.println(network.get(
							new User(username, "")).toString());
				} else {
					System.out.println("User does not exist!");
				}
				break;
			case "list":
				List<User> userList = network.preOrder();
				Collections.sort(userList);
				System.out.println("\nList of current users: ");
				if (userList.size() > 0) {
					for (int i = 0; i < userList.size() - 1; i++) {
						System.out.print(userList.get(i).toString() + ", ");
					}
					
					System.out.print(userList.get(userList.size() - 1).toString());
				}
				break;
			case "debug":
				System.out.println("\nString representation of tree: ");
				System.out.println(network.toString());
				break;
			case "exit":
				inMenu = false;
				break;
			default:
				System.out.println("\nPlease enter a valid command: " 
						+ "add, remove, find, list, debug, exit");
			}
		}
		
		inputScanner.close();
	}

}
