package ece1779;

public class GlobalValues {
	/**
	 * KEY of class Main for Servlet Initialization Context
	 */
	public static final String CURRENT_USER = "current_user";

	public static final String USERNAME = "user";
	public static final String PASSWORD = "password";

	public static final String regUSERNAME = "user";
	public static final String regPASSWORD = "password";
	public static final String regPASSWORD2 = "password2";

	/**
	 * SQL Connection Statement Tag
	 */
	public static final String Connection_Tag = "conTag";

	/**
	 * SQL Database information
	 */
	public static final String dbLocation_URL = "localhost";
	public static final String dbLocation_Port = "3306";
	public static final String dbLocation_Schema = "dbUsers";
	public static final String dbTable_Users = "users";
	public static final String dbTable_Images = "images";
	public static final String dbAdmin_Name = "root";
	public static final String dbAdmin_Pass = "qwer1234";

	/**
	 * Session inactive time (seconds) = 30 minutes * 60s/min
	 */
	public static final int SESSION_INACTIVE_TIME = 30 * 60;

	/**
	 * session attributes
	 */
	public static final String AWS_CREDENTIALS = "AWScredentials";
	/**
	 * session attributes
	 */
	public static final String CLOUD_WATCHING = "CloudWatching";

	/**
	 * S3 bucket name
	 */
	public static String BUCKET_NAME = "ece1779winter2015group14number1";

	/**
	 * S3 bucket endpoint, we can access an image key=sample_123456 by
	 * 
	 * http://ece1779winter2015group14number1.s3-website-us-west-2.amazonaws.com
	 * /sample_123456
	 */
	public static String BUCKET_ENDPOINT = "http://ece1779winter2015group14number1.s3-website-us-west-2.amazonaws.com/";

	/**
	 * ECE1779_GROUP14
	 */
	public static String SECURITY_GROUP_ID = "sg-36c76d52";

	public static String AMI_ID = "ami-3622045e";

	/**
	 * loadbalancer name
	 */
	public static String LOADBALANCER_NAME = "group14-lb";
	/**
	 * Privilege Tag
	 */
	public static String PRIVILEGE_ADMIN = "1";
	public static String PRIVILEGE_USER = "0";

	public static String PRIVILEGE_TAG = "PRIVILEGE";

	public static String SPECIFIC_IMAGE = "SPECIFIC_IMAGE";

	public static String UPLOAD_RESPONSE = "uploadresponse";

	public static String SUCCESS = "SUCCESS";
	public static String ERROR = "ERROR";
	public static String INVALID_PARAMETER = "INVALID_PARAMETER";

	public static String MANAGER_INSTANCE_ID = "i-2d0c5ed7";

}
