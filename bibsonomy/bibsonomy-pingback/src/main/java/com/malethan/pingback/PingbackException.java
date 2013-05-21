package com.malethan.pingback;

public class PingbackException extends RuntimeException {
	private static final long serialVersionUID = 2438222297923707852L;
	private String xmlrpcServer;
	private String targetUrl;
	private int faultCode;

	public PingbackException(String message, int faultCode,
			String xmlrpcServer, String targetUrl) {
		super(message);
		this.xmlrpcServer = xmlrpcServer;
		this.targetUrl = targetUrl;
		this.faultCode = faultCode;
	}

	public PingbackException(String message, Throwable cause,
			String xmlrpcServer, String targetUrl) {
		super(message, cause);
		this.xmlrpcServer = xmlrpcServer;
		this.targetUrl = targetUrl;
		this.faultCode = -1;
	}

	public String getXmlrpcServer() {
		return this.xmlrpcServer;
	}

	public String getTargetUrl() {
		return this.targetUrl;
	}

	public int getFaultCode() {
		return this.faultCode;
	}
}