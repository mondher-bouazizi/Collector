package collector.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.JSONObject;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import collector.items.Tweet;

public class Collector{

	//=================================//
	//     COLLECTOR MAIN METHODS      //
	//=================================//
	
	/**
	 * Collect a set of tweets online using a set of keywords and display them on a screen
	 */
	public static void collectAndDisplay() {
		
		String consumer_key = Parameters.getCurrentUser().getConsumerKey();
		String consumer_secret = Parameters.getCurrentUser().getConsumerSecret();
		String token = Parameters.getCurrentUser().getToken();
		String token_secret = Parameters.getCurrentUser().getTokenSecret();
		
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
		StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
		
		// Track keywords
		endpoint.trackTerms(Parameters.getKeywords());
		
		//Authentication
		Authentication auth = new OAuth1(consumer_key, consumer_secret, token, token_secret);
		
		// Setting up Twitter End points
		Client client = new ClientBuilder().hosts(Constants.STREAM_HOST).endpoint(endpoint).authentication(auth)
				.processor(new StringDelimitedProcessor(queue)).build();

		// Establish a connection
		client.connect();
		
		// load language detector
		try {
			MyLanguageDetector.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Start collecting the tweets
		int i = 0;
		
		Boolean allFlag = Parameters.getLanguage().equals("all");
		
		while (i < Parameters.getNumberOfTweetsToCollect() && !Parameters.isStop()) {
			String JsonTweet;
			Tweet tweet = null;
			try {
				JsonTweet = queue.take();
				tweet = parseTweet(JsonTweet);
				
				String langTag = "";
				if(!allFlag) langTag = MyLanguageDetector.detect(tweet.getMessage());
				
	
				if(allFlag || langTag.equals(Parameters.getLanguage())){
					
					if (tweet.getMessage()!= null && !tweet.getMessage().equals("")) {
						Parameters.getTweets().add(tweet);
						Parameters.setCountOfTweets(Parameters.getCountOfTweets().get() + 1);
						System.out.println("Saved!");
						i++;
					}
				}
				else System.out.println("Rejected: "+langTag+ " "+tweet.getMessage());
				
				
			} catch (InterruptedException e) {
				System.out.println("Collection of tweets interrupted!");
				e.printStackTrace();
			}
		}
		Parameters.setStop(true);
		client.stop();
	}

	/**
	 * Collect a set of tweets and write them on a file
	 */
	public static void collectAndSave() {

		File outputFile = new File(Parameters.getOutputFile());
		
		String consumer_key = Parameters.getCurrentUser().getConsumerKey();
		String consumer_secret = Parameters.getCurrentUser().getConsumerSecret();
		String token = Parameters.getCurrentUser().getToken();
		String token_secret = Parameters.getCurrentUser().getTokenSecret();
		
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
		StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
		
		// Track keywords
		endpoint.trackTerms(Parameters.getKeywords());
		
		//Authentication
		Authentication auth = new OAuth1(consumer_key, consumer_secret, token, token_secret);
		
		// Setting up Twitter End points
		Client client = new ClientBuilder().hosts(Constants.STREAM_HOST).endpoint(endpoint).authentication(auth)
				.processor(new StringDelimitedProcessor(queue)).build();

		// Establish a connection
		client.connect();
		
		try {
			
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, true)));
						
			String t = "\t";

			writer.println("Time" + t + "Tweet ID" + t + "Tweete Message" + t + "Username" + t + "User Shown Name" + t + "User Location");
			
			int i = 0;
			// Start collecting the tweets
			while (i < Parameters.getNumberOfTweetsToCollect() && !Parameters.isStop()) {
				String JsonTweet;
				Tweet tweet = null;
				try {
					JsonTweet = queue.take();
					tweet = parseTweet(JsonTweet);
					Parameters.getTweets().add(tweet);
					Parameters.countOfTweets.set(Parameters.countOfTweets.get() + 1);
					writer.write(tweet.getTime() + t + tweet.getId() + t + tweet.getMessage() + t + tweet.getUsername() + t + tweet.getUser() + t + tweet.getLocation() +"\n");
				} catch (InterruptedException e) {
					System.out.println("Collection of tweets interrupted!");
					e.printStackTrace();
				}
				i++;
			}
			
			Parameters.setStop(true);
			
			writer.close();
			
		} catch (IOException e1) {
			
		}
		

		client.stop();
	}

	
	//=================================//
	//        PRIVATE  METHODS         //
	//=================================//
	
	/**
	 * Parse a tweet and store it in an object of type {@link Tweet}
	 * @param JsonTweet
	 * @return
	 */
	private static Tweet parseTweet(String JsonTweet) {
		
		JSONObject twitterContent = new JSONObject(JsonTweet);

		// Time
		String time = "";
		try {
			if (!twitterContent.isNull("created_at")){
				time = twitterContent.getString("created_at");
			}
		} catch (org.json.JSONException e1) {
			System.out.println("######   Error in the Time   #####" );
		}
		
		// Tweet ID
		String tweetId = "";
		try {
			if (!twitterContent.isNull("id_str")){
				tweetId = twitterContent.getString("id_str");
			}
		} catch (org.json.JSONException e1) {
			System.out.println("######   Error in the Tweet ID   #####" );
		}
		
		// Message
		String tweetMessage = "";
		try {
			if (!twitterContent.isNull("text")){
				tweetMessage = twitterContent.getString("text").replaceAll("\t", " ").replaceAll("\n", " ").replaceAll("\n", " ");
				while (tweetMessage.contains("\t") || tweetMessage.contains("\r") || tweetMessage.contains("\n")) {
					// TODO check if this fixes the empty lines issue
					tweetMessage = tweetMessage.replaceAll("\t", " ").replaceAll("\r", " ").replaceAll("\n", " ");
				}
			}
		} catch (org.json.JSONException e1) {
			System.out.println("######   Error in the Message   #####" );
		}
		
		// Username
		String username = "";
		try {
			if (!twitterContent.getJSONObject("user").isNull("name")){
				username = twitterContent.getJSONObject("user").getString("name");
			}
		} catch (org.json.JSONException e1) {
			System.out.println("######   Error in the Username   #####" );
		}
		
		// User ID
		String userScreenName = "";
		try {
			if (!twitterContent.getJSONObject("user").isNull("screen_name")){
				userScreenName = twitterContent.getJSONObject("user").getString("screen_name");
			}
		} catch (org.json.JSONException e1) {
			System.out.println("######   Error in the User screen name   #####" );
		}
				
		// Location
		String userLocation = "";
		try {
			if (!twitterContent.getJSONObject("user").isNull("location")){
				userLocation = twitterContent.getJSONObject("user").getString("location");
				}
		} catch (org.json.JSONException e1) {
			System.out.println("######   Error in the location   #####" );
		}

		Tweet tweet = new Tweet(time, tweetId, tweetMessage, username, userScreenName, userLocation);

		System.out.println("\nTime: " + time
				+ "\nTwitter Message : " + tweetMessage
				+ "\nTweet ID : " + tweetId
				+ "\nUser Name: " + username
				+ "\nUser Screen name: " + userScreenName
				+ "\nUser Location: " + userLocation);
		
		return tweet;
	}
	
	
	//=================================//
	//             BACKUP              //
	//=================================//

	/**
	 * Collect a set of tweets online using a set of keywords and display them on a screen
	 */
	public static void collectAndDisplayBackup() {
		
		String consumer_key = Parameters.getCurrentUser().getConsumerKey();
		String consumer_secret = Parameters.getCurrentUser().getConsumerSecret();
		String token = Parameters.getCurrentUser().getToken();
		String token_secret = Parameters.getCurrentUser().getTokenSecret();
		
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
		StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
		
		// Track keywords
		endpoint.trackTerms(Parameters.getKeywords());
		
		//Authentication
		Authentication auth = new OAuth1(consumer_key, consumer_secret, token, token_secret);
		
		// Setting up Twitter End points
		Client client = new ClientBuilder().hosts(Constants.STREAM_HOST).endpoint(endpoint).authentication(auth)
				.processor(new StringDelimitedProcessor(queue)).build();

		// Establish a connection
		client.connect();
		
		// Start collecting the tweets
		for (int i = 0; i < Parameters.getNumberOfTweetsToCollect(); i++) {
			String JsonTweet;
			Tweet tweet = null;
			try {
				JsonTweet = queue.take();
				tweet = parseTweet(JsonTweet);
				Parameters.getTweets().add(tweet);
				Parameters.setCountOfTweets(Parameters.getCountOfTweets().get() + 1);
			} catch (InterruptedException e) {
				System.out.println("Collection of tweets interrupted!");
				e.printStackTrace();
			}
		}
		client.stop();
	}
	
	/**
	 * Collect a set of tweets and write them on a file
	 */
	public static void collectAndSaveBackup() {

		File outputFile = new File(Parameters.getOutputFile());
		
		String consumer_key = Parameters.getCurrentUser().getConsumerKey();
		String consumer_secret = Parameters.getCurrentUser().getConsumerSecret();
		String token = Parameters.getCurrentUser().getToken();
		String token_secret = Parameters.getCurrentUser().getTokenSecret();
		
		BlockingQueue<String> queue = new LinkedBlockingQueue<String>(10000);
		StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
		
		// Track keywords
		endpoint.trackTerms(Parameters.getKeywords());
		
		//Authentication
		Authentication auth = new OAuth1(consumer_key, consumer_secret, token, token_secret);
		
		// Setting up Twitter End points
		Client client = new ClientBuilder().hosts(Constants.STREAM_HOST).endpoint(endpoint).authentication(auth)
				.processor(new StringDelimitedProcessor(queue)).build();

		// Establish a connection
		client.connect();
		
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
			
			String t = "\t";

			writer.write("Time" + t
					+ "Tweet ID" + t
					+ "Tweete Message" + t
					+ "Username" + t
					+ "User Shown Name" + t
					+ "User Location"
					+ "\n");
			
			// Start collecting the tweets
			for (int i = 0; i < Parameters.getNumberOfTweetsToCollect(); i++) {
				String JsonTweet;
				Tweet tweet = null;
				try {
					JsonTweet = queue.take();
					tweet = parseTweet(JsonTweet);
					Parameters.getTweets().add(tweet);
					Parameters.countOfTweets.set(Parameters.countOfTweets.get() + 1);
					writer.write(tweet.getTime() + t
							+ tweet.getId() + t
							+ tweet.getMessage() + t
							+ tweet.getUsername() + t
							+ tweet.getUser() + t
							+ tweet.getLocation()
							+"\n");
				} catch (InterruptedException e) {
					System.out.println("Collection of tweets interrupted!");
					e.printStackTrace();
				}
			}
			writer.close();
			
		} catch (IOException e1) {
			
		}
		

		client.stop();
	}
	
	/**
	 * Parse a tweet and store it in an object of type {@link Tweet}
	 * @param JsonTweet
	 * @return
	 */
	public static Tweet parseTweetBackup(String JsonTweet) {
		
		JSONObject twitterContent = new JSONObject(JsonTweet);

		String time = twitterContent.getString("created_at");
		String tweetId = twitterContent.getString("id_str");
		String tweetMessage = twitterContent.getString("text").replaceAll("\t", " ").replaceAll("\n", " ");
		String username = twitterContent.getJSONObject("user").getString("name");
		String userScreenName = twitterContent.getJSONObject("user").getString("screen_name");
		String userLocation = "";
		try {
			if (!twitterContent.getJSONObject("user").isNull("location")){
				userLocation = twitterContent.getJSONObject("user").getString("location");
				}
		} catch (org.json.JSONException e1) {
			System.out.println("######   Error in the location   #####" );
		}

		Tweet tweet = new Tweet(time, tweetId, tweetMessage, username, userScreenName, userLocation);

		System.out.println("\nTime: " + time
				+ "\nTwitter Message : " + tweetMessage
				+ "\nTweet ID : " + tweetId
				+ "\nUser Name: " + username
				+ "\nUser Screen name: " + userScreenName
				+ "\nUser Location: " + userLocation);
		
		return tweet;
	}

}
