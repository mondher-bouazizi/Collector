package collector.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import collector.engine.Loader;
import collector.extra.windows.AlertBox;
import collector.items.User;
import collector.items.Tweet;
import collector.security.Security;

public class Writer {
	
	//====================================//
	//     SAVE USERS AND RECENT FILES    //
	//====================================//
	
	/**
	 * Save the set of {@link User} in case a new user is added
	 * @param file
	 * @param users
	 * @return
	 */
	public static boolean saveUsers(String file, HashMap<String, User> users) {
		
		boolean saved = false;
		
		if (users == null || users.isEmpty()) {
			System.out.println("No user to save!");
			return saved;
		} else {
			File usersFile = new File(file);
			File projectDir = new File(usersFile.getParent());

			boolean folderCreated = false;
			if (!projectDir.exists()) {
				try {
					projectDir.mkdir();
					folderCreated = true;
				} catch (SecurityException se) {
					AlertBox.display("Error", "The folder you want to save in cannot be accessed!", "OK");
					return false;
				}
			} else {
				folderCreated = true;
			}

			BufferedWriter writer = null;

			if (folderCreated) {
				try {
					if (!usersFile.exists())
						usersFile.createNewFile();
					writer = new BufferedWriter(new FileWriter(usersFile));
					
					String t = "\t";

					for (String user : Loader.getUsers().keySet()) {
						writer.write(Security.encrypt(Loader.getKey(), Loader.getInitvector(), Loader.getUsers().get(user).getUsername()) + t
								+ Security.encrypt(Loader.getKey(), Loader.getInitvector(), Loader.getUsers().get(user).getPasswordHash()) + t
								+ Security.encrypt(Loader.getKey(), Loader.getInitvector(), Loader.getUsers().get(user).getConsumerKey()) + t
								+ Security.encrypt(Loader.getKey(), Loader.getInitvector(), Loader.getUsers().get(user).getConsumerSecret()) + t
								+ Security.encrypt(Loader.getKey(), Loader.getInitvector(), Loader.getUsers().get(user).getToken()) + t
								+ Security.encrypt(Loader.getKey(), Loader.getInitvector(), Loader.getUsers().get(user).getTokenSecret())
								+ "\n");
					}

					saved = true;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (writer != null) {
						try {
							writer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			return saved;
		}

	}

	public static void saveTweets(String file, ArrayList<Tweet> tweets) {
		File outputFile = new File(file);
		File projectDir = new File(outputFile.getParent());

		boolean folderCreated = false;
		if (!projectDir.exists()) {
			try {
				projectDir.mkdir();
				folderCreated = true;
			} catch (SecurityException se) {
				AlertBox.display("Error", "The folder you want to save in cannot be accessed!", "OK");
			}
		} else {
			folderCreated = true;
		}

		BufferedWriter writer = null;

		if (folderCreated) {
			try {
				if (!outputFile.exists())
					outputFile.createNewFile();
				writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
				
				String t = "\t";
				String n = "\n";
				
				for (Tweet tweet : tweets) {
					writer.write(tweet.getId() + t + tweet.getTime() + t + tweet.getUser() + t + tweet.getUsername() + t + tweet.getLocation() + t + tweet.getMessage() + n);
				}
				AlertBox.display("Tweets Saved Successfully", "The tweets collected so far have been saved successfully", "OK");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
}
