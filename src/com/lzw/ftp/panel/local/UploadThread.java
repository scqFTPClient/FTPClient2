package com.lzw.ftp.panel.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Queue;

import org.apache.commons.net.ftp.FTPFile;

import com.lzw.ftp.extClass.MyFTPClient;
import com.lzw.ftp.extClass.ProgressArg;
import com.lzw.ftp.panel.ftp.FtpPanel;


class UploadThread extends Thread {
	private LocalPanel localPanel;
	String path = "";
	String selPath;
	private boolean conRun = true;
	private final MyFTPClient ftpClient;
	private Object[] queueValues;
	private FtpPanel ftpPanel;

	public UploadThread(LocalPanel localPanel, FtpPanel ftpPanel,String server, int port,
			String userStr, String passStr) {
		ftpClient = new MyFTPClient();
		
		try {
			ftpClient.openServer(server, port);
			ftpClient.login(userStr, passStr);
			System.out.println("登录成功，准备上传！");
			path = ftpClient.pwd();
			this.ftpPanel = ftpPanel;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.localPanel = localPanel;
		new Thread() { 
			public void run() {
				while (conRun) {
					try {
						Thread.sleep(30000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public void stopThread() { 
		conRun = false;
	}

//	private void copyFile(File file, FTPFile ftpFile) {
//
//
//		Object[] args = localPanel.queue.peek();
//		if (queueValues == null || args == null
//				|| !queueValues[0].equals(args[0]))
//			return;
//		try {
//			path = file.getParentFile().getPath().replace(selPath, "");
//			ftpFile.setName(path.replace("\\", "/"));
//			//绝对地址
//			System.out.println("本地地址" + path);
//			if (file.isFile()) {
//				String remoteFile = ftpClient.pwd() + "/" + file.getName();
//				System.out.println("remoteFile:" + remoteFile);
//				double fileLength = file.length() / Math.pow(1024, 2);
//				ProgressArg progressArg = new ProgressArg(
//						(int) (file.length() / 1024), 0, 0);
//				String size = String.format("%.4f MB", fileLength);
//				Object[] row = new Object[] { file.getAbsoluteFile(), size,
//						remoteFile, ftpClient.getServer(), progressArg };
//				OutputStream put = ftpClient.put(file.getName(), path);
//				FileInputStream fis = null;
//				try {
//					fis = new FileInputStream(file); 
//				} catch (Exception e) {
//					e.printStackTrace();
//					return;
//				}
//				int readNum = 0;
//				byte[] data = new byte[1024]; 
//				while ((readNum = fis.read(data)) > 0) { 
//					Thread.sleep(0, 30);
//					put.write(data, 0, readNum); 
//					progressArg.setValue(progressArg.getValue() + 1);
//				}
//				progressArg.setValue(progressArg.getMax()); 
//				fis.close();
//				put.close(); 
//			} else if (file.isDirectory()) {
//				// path = ftpFile.getAbsolutePath();
//				path = file.getPath().replace(selPath, "");
//				ftpFile.setName(path.replace("\\", "/"));
//				ftpClient.sendServer("MKD " + path + "\r\n");
//				ftpClient.readServerResponse();
//				File[] listFiles = file.listFiles();
//				for (File subFile : listFiles) {
//					Thread.sleep(0, 50);
//					copyFile(subFile, ftpFile);
//				}
//			}
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//			System.exit(0);
//			// JOptionPane.showMessageDialog(localPanel, e1.getMessage());
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}

//	 public void upload(File file) throws Exception {  
//	        if (file.isDirectory()) {  
//	            ftp.makeDirectory(file.getName());  
//	            ftp.changeWorkingDirectory(file.getName());  
//	            String[] files = file.list();  
//	            for (int i = 0; i < files.length; i++) {  
//	                File file1 = new File(file.getPath() + "\\" + files[i]);  
//	                if (file1.isDirectory()) {  
//	                    upload(file1);  
//	                    ftp.changeToParentDirectory();  
//	                } else {  
//	                    File file2 = new File(file.getPath() + "\\" + files[i]);  
//	                    FileInputStream input = new FileInputStream(file2);  
//	                    ftp.storeFile(file2.getName(), input);  
//	                    input.close();  
//	                }  
//	            }  
//	        } else {  
//	            File file2 = new File(file.getPath());  
//	            FileInputStream input = new FileInputStream(file2);  
//	            ftp.storeFile(file2.getName(), input);  
//	            input.close();  
//	        }  
//	    }  
	
	public void run() {
		while (conRun) {
			try {
				Thread.sleep(1000);
				Queue<Object[]> queue = localPanel.queue; 
				queueValues = queue.peek(); 
				if (queueValues == null) { 
					continue;
				}
				File file = (File) queueValues[0]; 
				
				FTPFile ftpFile = (FTPFile) queueValues[1]; 
				if (file != null) {
					selPath = file.getParent(); //当前本地目录
					String localFileName = file.getName();

					//获取远程路径
					String remoteFolder = this.ftpPanel.getPwd();
					System.out.println("remote" + remoteFolder + " local" + localFileName);
					
					final int[] selRows = localPanel.localDiskTable.getSelectedRows();
					final String fileType =  (String) localPanel.localDiskTable.getValueAt(selRows[0], 1);

					
					String localFolder = selPath;
					if(fileType.equals("<DIR>")){
						localFolder = localFolder + "/" + file.getName();
						ftpClient.uploadDirectory(localFolder, remoteFolder);
					}else {
						ftpClient.uploadFile(localFileName, remoteFolder);
					}
					
//					ftpClient.uploadFile(localFileName, remoteFolder);
//					copyFile(file, ftpFile);
					FtpPanel ftpPanel = localPanel.frame.getFtpPanel();
					ftpPanel.refreshCurrentFolder();
				}
				Object[] args = queue.peek();
				if (queueValues == null || args == null
						|| !queueValues[0].equals(args[0])) {
					continue;
				}
				queue.remove();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}