package nl.knokko.server.connection.code;

public class RealmConnectionCode {
	
	public static final byte MESSAGE_CREATE_REALM = 127;
	public static final byte MESSAGE_ENABLE_REALM = 126;
	public static final byte MESSAGE_DISABLE_REALM = 125;
	public static final byte MESSAGE_TESTED_REALM = 124;
	public static final byte MESSAGE_FAILED_REALM_TEST = 123;
	
	public static final byte ENABLE_NO_NAME = -128;
	public static final byte ENABLE_WRONG_PASSWORD = -127;
	public static final byte ENABLE_ALREADY = -126;
	public static final byte ENABLE_NEW_HOST = -125;
	public static final byte ENABLE_SUCCEED = -124;
	public static final byte ENABLE_MOVE_AUTO_DENIED = -123;
	public static final byte ENABLE_MOVE_BUSY = -122;
	
	public static final byte REQUEST_DENIED = -128;
	public static final byte REQUEST_APPROVED = -127;
	
	public static final byte CREATE_NAME_EXISTS = -128;
	public static final byte CREATE_WAITING = -127;
	public static final byte CREATE_AUTO_DENIED = -126;
	public static final byte CREATE_BUSY = -125;
	
	public static final byte KICK_CODE_REALM_DISABLED = 127;
}
