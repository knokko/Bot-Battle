package nl.knokko.connection;

public final class ConnectionCode {
	
	public static class CtS {
		
		public static class Auth {
			
			public static final byte CODE = -128;
			
			public static final byte CODE_LOGOUT = -128;
			public static final byte CODE_LOGIN_1 = -127;
			public static final byte CODE_LOGIN_2 = -126;
			public static final byte CODE_REGISTER = -125;
		}
	}
	
	public static class StC {
		
		public static class Auth {
			
			public static final byte CODE = -128;
			
			public static final byte CODE_LOGIN_1 = -128;
			public static final byte CODE_LOGIN_2 = -127;
			public static final byte CODE_REGISTER = -126;
			public static final byte CODE_LOGIN_FAIL = -125;
			public static final byte CODE_REGISTER_FAIL = -124;
			
			public static class LoginFail {
				
				public static final byte CODE_NO_NAME = -128;
				public static final byte CODE_WRONG_PASSWORD = -127;
				public static final byte CODE_ALREADY = -126;
				
				public static final String ERROR_NO_NAME = "There is no account with that name.";
				public static final String ERROR_WRONG_PASSWORD = "The password is incorrect.";
				public static final String ERROR_ALREADY = "This account is already logged in.";
				
				public static String asString(byte reason){
					if(reason == CODE_NO_NAME)
						return ERROR_NO_NAME;
					if(reason == CODE_WRONG_PASSWORD)
						return ERROR_WRONG_PASSWORD;
					if(reason == CODE_ALREADY)
						return ERROR_ALREADY;
					throw new IllegalArgumentException("Unknown login fail reason: " + reason);
				}
			}
			
			public static class RegisterFail {
				
				public static final byte CODE_NAME_IN_USE = -128;
				
				public static final String ERROR_NAME_IN_USE = "This name is already in use.";
				
				public static String asString(byte reason){
					if(reason == CODE_NAME_IN_USE)
						return ERROR_NAME_IN_USE;
					throw new IllegalArgumentException("Unknown register fail reason: " + reason);
				}
			}
		}
		
		public static class Kick {
			
			public static final byte CODE = -127;
			
			public static final byte CODE_CORRUPT = -128;
			public static final byte CODE_LOGGED_OUT = -127;
			public static final byte CODE_OP_KICK = -126;
			public static final byte CODE_IO_ERROR = -125;
			public static final byte CODE_CLOSED = -124;
		}
	}
	
	public static final String MAIN_HOST = "192.168.2.131";
	public static final int MAIN_PORT = 46392;
}