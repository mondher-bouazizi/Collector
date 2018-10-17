package collector.items;

public class User {
	
	
	//====================================//
	//             ATTRIBUTES             //
	//====================================//
	
	protected String username;
	protected String passwordHash;
	
	
	//====================================//
	//                KEYS                //
	//====================================//
	
	protected String consumerKey;
	protected String consumerSecret;
	protected String token;
	protected String tokenSecret;
	
	
	//====================================//
	//             CONSTRUCTOR            //
	//====================================//
	
	public User(String username, String passwordHash, String consumerKey, String consumerSecret, String token,
			String tokenSecret) {
		super();
		this.username = username;
		this.passwordHash = passwordHash;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.token = token;
		this.tokenSecret = tokenSecret;
	}
	
	
	//====================================//
	//         GETTERS AND SETTERS        //
	//====================================//
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPasswordHash() {
		return passwordHash;
	}
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public String getConsumerKey() {
		return consumerKey;
	}
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}
	
	public String getConsumerSecret() {
		return consumerSecret;
	}
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getTokenSecret() {
		return tokenSecret;
	}
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

}
