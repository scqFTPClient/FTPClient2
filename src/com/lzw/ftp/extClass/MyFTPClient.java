package com.lzw.ftp.extClass;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

import sun.net.TelnetInputStream;
import sun.net.TelnetOutputStream;

public class MyFTPClient{
	private String server;
	private int port;
	private String name;
	private String pass;
	private FTPClient ftpClient;

	public MyFTPClient() {
		ftpClient = new FTPClient();
	}
	
	public MyFTPClient(String server, int port) throws IOException {
		this.server = server;
		this.port = port;
		ftpClient = new FTPClient();
	}

	//打开连接
	public synchronized void openServer(String server, int port)
			throws IOException {
		this.server = server;
		this.port = port;
		ftpClient.connect(server,port);
		System.out.println("Connected to " + server + ".");
		System.out.print(ftpClient.getReplyString());
	}
	
	//登录服务器
	public synchronized void login(String username, String password) throws IOException {
		boolean isSuccess = ftpClient.login(username, password);
		if(isSuccess) {
			System.out.println("登录成功！");
		}
	}
	
	public synchronized void binary() throws IOException {
		
	}

	//更改目录
	public synchronized void cd(String path) throws IOException {
		ftpClient.changeWorkingDirectory(path);
	}

	//返回上一级
	public synchronized void cdUp() throws IOException {
		ftpClient.changeToParentDirectory();
	}

	//下载文件
	public synchronized TelnetInputStream get(String file) throws IOException {
		return new TelnetInputStream(null, false);
	}

	//列出目录中文件
	public synchronized TelnetInputStream list() throws IOException {
		return new TelnetInputStream(null, false);
	}

	public synchronized void noop() throws IOException {

	}
	
	//上传文件
	public synchronized TelnetOutputStream put(String arg0) throws IOException {
		return new TelnetOutputStream(null, false);
	}

	//现在的目录
	public synchronized String pwd() throws IOException {
		String pwd = ftpClient.printWorkingDirectory();
		pwd = new String(pwd.getBytes("iso-8859-1"), "GBK");
		return pwd;
	}

	//获取服务器响应码
	public synchronized int readServerResponse() throws IOException {
		return ftpClient.getReplyCode();
	}

	//向服务器发送命令
	public synchronized void sendServer(String command) throws IOException {
		ftpClient.sendCommand(command);
	}

	//判断服务器是否打开
	public synchronized boolean serverIsOpen() {
		return ftpClient.isConnected();
	}


	public int getPort() {
		return port;
	}

	public String getName() {
		return name;
	}

	public String getPass() {
		return pass;
	}
	
	public String getServer() {
		return server;
	}
}
