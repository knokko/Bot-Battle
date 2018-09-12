package nl.knokko.client.connection.state;

public class ClientMainConnectionState extends ClientConnectionState {
	
	private final Auth auth;
	
	private byte[] queuedMessage;
	
	public ClientMainConnectionState(){
		auth = new Auth();
	}
	
	public void queueMessage(byte[] message){
		queuedMessage = message;
	}
	
	/**
	 * This method clears the queued message!
	 * @return The queued message or null
	 */
	public byte[] getQueuedMessage(){
		byte[] message = queuedMessage;
		queuedMessage = null;
		return message;
	}
	
	public Auth auth(){
		return auth;
	}
	
	public static class Auth {
		
		public static final byte AUTH_STATE_NOTHING = 0;
		public static final byte AUTH_STATE_LOGIN_1 = 1;
		public static final byte AUTH_STATE_LOGIN_2 = 2;
		public static final byte AUTH_STATE_REGISTER_1 = 3;
		public static final byte AUTH_STATE_LOGGED_IN = 4;
		
		private byte authState = AUTH_STATE_NOTHING;
		
		private String password;//don't store longer than necessary
		private String salt;
		private String username;
		
		private long temp1;
		private long temp2;
		
		public byte getAuthState(){
			return authState;
		}
		
		public void setAuthState(byte state){
			authState = state;
		}
		
		public boolean isLoggingIn(){
			return authState == AUTH_STATE_LOGIN_1 || authState == AUTH_STATE_LOGIN_2;
		}
		
		public boolean isRegistering(){
			return authState == AUTH_STATE_REGISTER_1;
		}
		
		public boolean isLoggedIn(){
			return authState == AUTH_STATE_LOGGED_IN;
		}
		
		public void setLogin1(){
			authState = AUTH_STATE_LOGIN_1;
		}
		
		public void setLogin2(){
			authState = AUTH_STATE_LOGIN_2;
		}
		
		public void setLoggedIn(){
			authState = AUTH_STATE_LOGGED_IN;
		}
	
		public void setPassword(String password){
			this.password = password;
		}
	
		public String getPassword(){
			return password;
		}
	
		public void clearPassword(){
			password = null;
		}
	
		public void setSalt(String salt){
			this.salt = salt;
		}
	
		public String getSalt(){
			return salt;
		}
	
		public void clearSalt(){
			salt = null;
		}
		
		public void setTempHasher(long tempHasher1, long tempHasher2){
			temp1 = tempHasher1;
			temp2 = tempHasher2;
		}
		
		public long getTempHasher1(){
			return temp1;
		}
		
		public long getTempHasher2(){
			return temp2;
		}
		
		public void clearTempHasher(){
			temp1 = -1;
			temp2 = -1;
		}
		
		public void setUsername(String name){
			username = name;
		}
		
		public String getUsername(){
			return username;
		}
		
		public void clearUsername(){
			username = null;
		}
	}
}