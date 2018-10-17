package collector.items;

public class Tweet {
	
	//====================================//
	//             ATTRIBUTES             //
	//====================================//
	
	protected String time;
	protected String id;
	protected String message;
	protected String username;
	protected String user;
	protected String location;
	
	
	//====================================//
	//        GETTERS AND SETTERS         //
	//====================================//
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	
	//====================================//
	//            CONSTRUCTORS            //
	//====================================//
	
	public Tweet() {
		super();
	}
	
	/**
	 * Constructor with parameters
	 * @param time
	 * @param id
	 * @param username
	 * @param user
	 * @param location
	 */
	public Tweet(String time, String id, String message, String username, String user, String location) {
		super();
		this.time = time;
		this.id = id;
		this.message = message;
		this.username = username;
		this.user = user;
		this.location = location;
	}
}
