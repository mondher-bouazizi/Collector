package collector.engine;

import java.util.HashMap;

import collector.io.Reader;
import collector.io.Writer;
import collector.items.User;

public class Loader {
	
	//====================================//
	//        PROGRAM-RELATED DATA        //
	//====================================//
	
	protected static HashMap<String, User> users;
	
	//====================================//
	//       SECURITY-RELATED ITEMS       //
	//====================================//
	
	protected static final String KEY = "x245d64D4754sdf1"; // 128 bit key
	protected static final String INITVECTOR = "Skj4sd4p47dflm47"; // 16 bytes IV
	
	
	//====================================//
	//         GETTERS AND SETTERS        //
	//====================================//
	

	public static HashMap<String, User> getUsers() {
		return users;
	}
	public static void setUsers(HashMap<String, User> users) {
		Loader.users = users;
	}
	public static void addUser(User user) {
		if (!users.containsKey(user.getUsername())) {
			users.put(user.getUsername(), user);
			saveUsers();
		}
	}
	
	public static String getKey() {
		return KEY;
	}
	
	public static String getInitvector() {
		return INITVECTOR;
	}
	
	
	//====================================//
	//               LOADER               //
	//====================================//

	/**
	 * Load the users and the recent files
	 */
	public static void load() {
		loadUsers();
	}

	//====================================//
	//         LOADERS AND SAVERS         //
	//====================================//

	/**
	 * Save the list of users
	 */
	public static void loadUsers() {
		users = Reader.getUsers(Constants.USERSFILE);
	}
	
	/**
	 * Load the list of users
	 */
	public static void saveUsers() {
		Writer.saveUsers(Constants.USERSFILE, users);
	}
	
}
