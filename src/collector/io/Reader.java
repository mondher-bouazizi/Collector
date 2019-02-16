package collector.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import collector.engine.Loader;
import collector.items.User;
import collector.security.Security;

public class Reader {
	
	//======================================//
	//               READERS                //
	//======================================//
	
	public static HashMap<String, User> getUsers(String file) {
		
		HashMap<String, User> users = new HashMap<>();
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = reader.readLine()) != null) {
				
				String[] map = line.split("\t");
				if (map.length==6) {
					
					String username = Security.decrypt(Loader.getKey(), Loader.getInitvector(), map[0]);
					String passwordHash = Security.decrypt(Loader.getKey(), Loader.getInitvector(), map[1]);
					String consumerKey = Security.decrypt(Loader.getKey(), Loader.getInitvector(), map[2]);
					String consumerSecret = Security.decrypt(Loader.getKey(), Loader.getInitvector(), map[3]);
					String token = Security.decrypt(Loader.getKey(), Loader.getInitvector(), map[4]);
					String tokenSecret = Security.decrypt(Loader.getKey(), Loader.getInitvector(), map[5]);
					
					User user = new User(username, passwordHash, consumerKey, consumerSecret, token, tokenSecret);
					
					users.put(username, user);
				}
			}

		} catch (FileNotFoundException e1) {
			// Load default Users
			System.out.println("Couldn't load the users!");
			
		} catch (IOException e) {
			// Load default Users
			System.out.println("Couldn't load the users!");

		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (users.isEmpty()) {
			// Load default Users
			System.out.println("User file is empty..");

		}
		
		return users;
	}
	
	
	//======================================//
	//               CHECKERS               //
	//======================================//
	
	/**
	 * Checks whether a directory is valid or not
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isValidDirectory(String file) {
		File dir = new File(file);
		if (dir.exists() && dir.isDirectory()) {
			return true;
		}
		return false;
	}
	
	
	
}
