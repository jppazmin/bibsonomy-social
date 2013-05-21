package com.malethan.pingback;

public abstract interface PingbackClient {
	public static final int UNKOWN_ERROR = 0;
	public static final int SOURCE_NOT_FOUND = 16;
	public static final int SOURCE_HAS_NO_LINK_TO_TARGET = 17;
	public static final int TARGET_NOT_FOUND = 32;
	public static final int TARGET_IS_NOT_PINGBACK_RESOURCE = 33;
	public static final int PINGBACK_ALREADY_REGISTERED = 48;
	public static final int ACCESS_DENIED = 49;
	public static final int UPSTREAM_PROBLEM = 50;

	public abstract String sendPingback(String paramString, Link paramLink);
}