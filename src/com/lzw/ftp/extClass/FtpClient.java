package com.lzw.ftp.extClass;

import java.io.IOException;

import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;

public class FtpClient {
	private String server;
	private int port;
	private String name;
	private String pass;

	public int getPort() {
		return port;
	}

	public String getName() {
		return name;
	}

	public String getPass() {
		return pass;
	}

	public FtpClient(String server, int port) throws IOException {
		this.server = server;
		this.port = port;
	}

	public FtpClient() {
		super();
	}

	public synchronized void binary() throws IOException {
		//TODO
	}

	public synchronized void cd(String arg0) throws IOException {

	}

	public synchronized void cdUp() throws IOException {

	}

	public synchronized TelnetInputStream get(String file) throws IOException {
		return new TelnetInputStream(null, false);
	}

	public synchronized TelnetInputStream list() throws IOException {
		return new TelnetInputStream(null, false);
	}

	public synchronized void login(String arg0, String arg1) throws IOException {
		name = arg0;
		pass = arg1;

	}

	public synchronized void noop() throws IOException {

	}

	public synchronized void openServer(String arg0, int port)
			throws IOException {
		this.server = arg0;
		this.port = port;

	}

	public synchronized TelnetOutputStream put(String arg0) throws IOException {
		return new TelnetOutputStream(null, false);
	}

	public synchronized String pwd() throws IOException {
		String pwd = pwd();
		pwd = new String(pwd.getBytes("iso-8859-1"), "GBK");
		return pwd;
	}

	public synchronized int readServerResponse() throws IOException {
		return 1;

	}

	public synchronized void sendServer(String command) {

	}

	public synchronized boolean serverIsOpen() {
		return true;
	}

	public String getServer() {
		return server;
	}
}
