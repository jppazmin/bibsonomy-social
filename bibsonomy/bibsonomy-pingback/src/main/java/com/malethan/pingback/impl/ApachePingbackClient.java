package com.malethan.pingback.impl;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import com.malethan.pingback.Link;
import com.malethan.pingback.PingbackClient;
import com.malethan.pingback.PingbackException;

public class ApachePingbackClient implements PingbackClient {

	public String sendPingback(String articleUrl, Link link) {
		try {
			XmlRpcClient client = configureXmlRcpClient(link);
			Object[] params = { articleUrl, link.getUrl() };
			return (String) client.execute("pingback.ping", params);
		} catch (MalformedURLException e) {
			throw new PingbackException(
					"It was not possible to send a pingback, one or more URLs was invalid",
					e, link.getPingbackUrl(), link.getUrl());
		} catch (XmlRpcException e) {
			throw new PingbackException("Error: " + e.code + ", "
					+ e.getMessage(), e.code, link.getPingbackUrl(),
					link.getUrl());
		}
	}

	private XmlRpcClient configureXmlRcpClient(Link link)
			throws MalformedURLException {
		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL(link.getPingbackUrl()));
		XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);
		return client;
	}
}