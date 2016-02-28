package com.lzw.ftp.extClass;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

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
	public synchronized FTPFile[] list(String pathname) throws IOException {
		FTPFile[] files = ftpClient.listFiles(pathname);
		
		return files;
	}

	//在远程服务器创建文件夹
	public synchronized boolean mkdir(String pathname) throws IOException {
		return ftpClient.makeDirectory(pathname);
	}
	
	public synchronized void noop() throws IOException {

	}
	
	//删除远程文件
	public boolean deleteFile(String pathName) throws IOException {  
	    return ftpClient.deleteFile(pathName);  
	}  
	
	/**
     * 上传文件到ftp服务器
     * 在进行上传和下载文件的时候，设置文件的类型最好是：
     * ftpUtil.setFileType(FtpUtil.BINARY_FILE_TYPE)
     * localFilePath:本地文件路径和名称
     * remoteFileName:服务器文件名称
     */
    public boolean uploadFile(String localFilePath, String remoteFileName)  
            throws IOException {  
        boolean flag = false;  
        InputStream iStream = null;  
        try {  
            iStream = new FileInputStream(localFilePath);  
            //我们可以使用BufferedInputStream进行封装
            //BufferedInputStream bis=new BufferedInputStream(iStream);
            //flag = ftpClient.storeFile(remoteFileName, bis); 
            flag = ftpClient.storeFile(remoteFileName, iStream);  
        } catch (IOException e) {  
            flag = false;  
            return flag;  
        } finally {  
            if (iStream != null) {  
                iStream.close();  
            }  
        }  
        return flag;  
    } 
    /**
     * 上传文件到ftp服务器，上传新的文件名称和原名称一样
     * @param fileName：文件名称
     * @return
     * @throws IOException
     */
    public boolean uploadFile(String fileName) throws IOException {  
        return uploadFile(fileName, fileName);  
    }  
    
    /**
     * 从ftp服务器上下载文件到本地
     * @param remoteFileName：ftp服务器上文件名称
     * @param localFileName：本地文件名称
     * @return
     * @throws IOException
     */

    public boolean download(String remoteFileName,String remoteFolder, String localFolder)  
            throws IOException {  
    	
        boolean flag = false;  
        try {
        	ftpClient.setControlEncoding("UTF-8");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            System.out.println("remote" + remoteFolder);
            System.out.println("local" + localFolder);
            ftpClient.changeWorkingDirectory(remoteFolder);
            File file = new File(localFolder + "/" + remoteFileName);
            FileOutputStream fos = new FileOutputStream(file);
            boolean a = ftpClient.retrieveFile(remoteFolder + "/" + remoteFileName, fos);
            System.out.println("a" + a);
            fos.close();
            flag = true;
        }catch (SocketException e) {
        	e.printStackTrace();
        }catch (IOException e) {
        	e.printStackTrace();
        }
        
        return flag;  
    }  
      
    /**
     * 从ftp服务器上下载文件到本地
     * @param sourceFileName：服务器资源文件名称
     * @return InputStream 输入流
     * @throws IOException
     */
    public InputStream downFile(String sourceFileName) throws IOException {  
        return ftpClient.retrieveFileStream(sourceFileName);  
    }  
    

	//获取当前的目录
	public synchronized String pwd(){
		String pwd = "";
		try {
			pwd = ftpClient.printWorkingDirectory();
			pwd = new String(pwd.getBytes("iso-8859-1"), "GBK");
		} catch (IOException e) {
			e.printStackTrace();
		}
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
