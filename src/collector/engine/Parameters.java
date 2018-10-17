package collector.engine;

import java.util.ArrayList;

import collector.items.Tweet;
import collector.items.User;
import javafx.beans.property.IntegerProperty;

public class Parameters {
	
	//=================================//
	//           ATTRIBUTES            //
	//=================================//
	
	protected static User currentUser;
	
	protected static ArrayList<String> keywords;
	protected static int numberOfTweetsToCollect;
	protected static IntegerProperty countOfTweets;
	protected static String outputFile;
	
	protected static ArrayList<Tweet> tweets;
	
	protected static Tweet activeTweet;
	
	protected static boolean stop = false;
	
	protected static String requiredLanguage = "all";
	
	
	//=================================//
	//       GETTERS AND SETTERS       //
	//=================================//
	
	public static User getCurrentUser() {
		return currentUser;
	}
	public static void setCurrentUser(User currentUser) {
		Parameters.currentUser = currentUser;
	}

	public static ArrayList<String> getKeywords() {
		return keywords;
	}
	public static void setKeywords(ArrayList<String> keywords) {
		Parameters.keywords = keywords;
	}
	
	public static int getNumberOfTweetsToCollect() {
		return numberOfTweetsToCollect;
	}
	public static void setNumberOfTweetsToCollect(int numberOfTweetsToCollect) {
		Parameters.numberOfTweetsToCollect = numberOfTweetsToCollect;
	}
	
	public static IntegerProperty getCountOfTweets() {
		return countOfTweets;
	}
	public static void setCountOfTweets(IntegerProperty countOfTweets) {
		Parameters.countOfTweets = countOfTweets;
	}
	public static void setCountOfTweets(int countOfTweets) {
		Parameters.countOfTweets.set(countOfTweets);
	}
	
	public static String getOutputFile() {
		return outputFile;
	}
	public static void setOutputFile(String outputFile) {
		Parameters.outputFile = outputFile;
	}
	
	public static ArrayList<Tweet> getTweets() {
		if (tweets == null) {
			tweets = new ArrayList<>();
		}
		return tweets;
	}
	public static void setTweets(ArrayList<Tweet> tweets) {
		Parameters.tweets = tweets;
	}
	
	public static Tweet getActiveTweet() {
		return activeTweet;
	}
	public static void setActiveTweet(Tweet activeTweet) {
		Parameters.activeTweet = activeTweet;
	}
	public static boolean isStop() {
		return stop;
	}
	public static void setStop(boolean stop) {
		Parameters.stop = stop;
	}
	
	public static void setLanguage(String lang){
		if(lang.equals("French")) requiredLanguage = "fr";
		else if(lang.equals("English")) requiredLanguage = "en"; 
	}
	public static String getLanguage(){
		return requiredLanguage;
	}
	
	

}
