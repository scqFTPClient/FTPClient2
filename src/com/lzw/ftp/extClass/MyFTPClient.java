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
	private final FTPClient ftpClient;

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
    public boolean uploadFile(String localFileName, String remoteFolder)  
            throws IOException {  
    	 boolean flag = false;  
         
    	String remoteFileName  = new File(localFileName).getName();
    	System.out.println("wo sji" +  remoteFileName);
 		try {
 			ftpClient.setControlEncoding("UTF-8");
 			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
 			FileInputStream fis = new FileInputStream(localFileName);
 			
 			System.out.println("fis is " + fis);
 			
 			ftpClient.storeFile(remoteFolder + "/" + remoteFileName, fis);
 			fis.close();
 		} catch (SocketException e) {
 			e.printStackTrace();
 		} catch (IOException e) {
 			e.printStackTrace();
 		}
 		
         return flag;  
    } 
    /**
     * 上传文件到ftp服务器，上传新的文件名称和原名称一样
     * @param fileName：文件名称
     * @return
     * @throws IOException
     */
    public boolean uploadDirectory(String localFolder, String remoteFolder){
		String fileName = new File(localFolder).getName();
		remoteFolder = remoteFolder + "/" + fileName;
		try {
			ftpClient.mkd(remoteFolder);
			File[] allFiles = new File(localFolder).listFiles();
			
			
			for(File currentFile: allFiles) {
				System.out.println("uploadDire   " + currentFile.getName());
				if(!currentFile.isDirectory()) {
					System.out.println("remoteFolder is " + remoteFolder);
					System.out.println("localFolder is " + localFolder);
					this.uploadFile(localFolder + "/" + currentFile.getName(), remoteFolder);
				}else {
					String strLocalFolder = localFolder + "/" + currentFile.getName();
					this.uploadDirectory(strLocalFolder, remoteFolder);
				}
			}
			
			
//            String fileName = new File(remoteDirectory).getName();  // /data/ftp/a
//            localDirectoryPath = localDirectoryPath + "/" + fileName; // */a 
//            new File(localDirectoryPath).mkdirs();  
//            FTPFile[] allFile = this.ftpClient.listFiles(remoteDirectory);  
//            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {  
//                if (!allFile[currentFile].isDirectory()) {  
//                    download(allFile[currentFile].getName(), remoteDirectory, localDirectoryPath);  
//                }  
//            }  
//            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {  
//                if (allFile[currentFile].isDirectory()) {  
//                    String strremoteDirectoryPath = remoteDirectory + "/"+ allFile[currentFile].getName();  
//                    downLoadDirectory(localDirectoryPath,strremoteDirectoryPath);  
//                }  
//            }  
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;  
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
			FTPFile[] files = ftpClient.listFiles(remoteFolder);
			for (int i = 0; i < files.length; i++) {
				System.out.println(files[i].getName());
			}
			File file = new File(localFolder+ "/" + remoteFileName);
			FileOutputStream fos = new FileOutputStream(file);
			ftpClient.retrieveFile(remoteFolder + "/" + remoteFileName, fos);
			fos.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        return flag;  
    }  
      
    public boolean downLoadDirectory(String localDirectoryPath,String remoteDirectory) {  
        try {  
            String fileName = new File(remoteDirectory).getName();  // /data/ftp/a
            localDirectoryPath = localDirectoryPath + "/" + fileName; // */a 
            new File(localDirectoryPath).mkdirs();  
            FTPFile[] allFile = this.ftpClient.listFiles(remoteDirectory);  
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {  
                if (!allFile[currentFile].isDirectory()) {  
                    download(allFile[currentFile].getName(), remoteDirectory, localDirectoryPath);  
                }  
            }  
            for (int currentFile = 0; currentFile < allFile.length; currentFile++) {  
                if (allFile[currentFile].isDirectory()) {  
                    String strremoteDirectoryPath = remoteDirectory + "/"+ allFile[currentFile].getName();  
                    downLoadDirectory(localDirectoryPath,strremoteDirectoryPath);  
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
            System.out.println("下载文件失败");
            return false;  
        }  
        return true;  
    } 
    
    

	//获取当前的目录
	public synchronized String pwd(){
		String pwd = "";
		try {
			pwd = this.ftpClient.printWorkingDirectory();
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

	public void setPort(int port) {
		this.port = port;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
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
