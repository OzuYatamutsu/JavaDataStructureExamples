/**
 * Represents a comparable User object. A User is compared 
 * with its username, but stores both name and username.
 * 
 * @author Sean Collins
 */
public class User implements Comparable<User> {
	private String username;
	private String name;
	
	/**
	 * Constructs a new User with a provided username and name.
	 * 
	 * @param username The username of this User
	 * @param name The name of this User
	 */
	public User(String username, String name) {
		this.username = username;
		this.name = name;
	}
	
	/**
	 * Returns this User's username.
	 * @return This User's username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Returns this User's name.
	 * @return This User's name
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return username + "-" + name;
	}

	/**
	 * Compares two Users together by username only. A User is less than another
	 * User if alphabetically earlier, greater than another User if 
	 * alphabetically later, and equal if usernames are the same.
	 * 
	 * @return The result of the User comparison
	 */
	public int compareTo(User user) {
		return username.toLowerCase().compareTo(user.getUsername().toLowerCase());
	}
	
	@Override
	public boolean equals(Object user) {
		return username.toLowerCase().equals(((User) user).getUsername().toLowerCase());
	}
}
