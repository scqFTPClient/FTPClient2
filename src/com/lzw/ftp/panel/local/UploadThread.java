package com.lzw.ftp.panel.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Queue;

import com.lzw.ftp.extClass.MyFTPClient;
import com.lzw.ftp.extClass.FtpFile;
import com.lzw.ftp.extClass.ProgressArg;
import com.lzw.ftp.panel.ftp.FtpPanel;


class UploadThread extends Thread {
	private LocalPanel localPanel;
	String path = "";
	String selPath;
	private boolean conRun = true;
	private MyFTPClient ftpClient;
	private Object[] queueValues;

	public UploadThread(LocalPanel localPanel, String server, int port,
			String userStr, String passStr) {
		try {
			ftpClient = new MyFTPClient(server, port);
			ftpClient.openServer(server, port);
			ftpClient.login(userStr, passStr);
			System.out.println("a");
			ftpClient.binary();
			path = ftpClient.pwd();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.localPanel = localPanel;
		new Thread() { 
			public void run() {
				while (conRun) {
					try {
						Thread.sleep(30000);

						UploadThread.this.ftpClient.noop();
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

	private void copyFile(File file, FtpFile ftpFile) {


		Object[] args = localPanel.queue.peek();
		if (queueValues == null || args == null
				|| !queueValues[0].equals(args[0]))
			return;
		try {
			path = file.getParentFile().getPath().replace(selPath, "");
			ftpFile.setName(path.replace("\\", "/"));
			path = ftpFile.getAbsolutePath();
			System.out.println(path);
			if (file.isFile()) {
				String remoteFile = path + "/" + file.getName();
				System.out.println("remoteFile:" + remoteFile);
				double fileLength = file.length() / Math.pow(1024, 2);
				ProgressArg progressArg = new ProgressArg(
						(int) (file.length() / 1024), 0, 0);
				String size = String.format("%.4f MB", fileLength);
				Object[] row = new Object[] { file.getAbsoluteFile(), size,
						remoteFile, ftpClient.getServer(), progressArg };
				OutputStream put = ftpClient.put(remoteFile); 
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(file); 
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				int readNum = 0;
				byte[] data = new byte[1024]; 
				while ((readNum = fis.read(data)) > 0) { 
					Thread.sleep(0, 30);
					put.write(data, 0, readNum); 
					progressArg.setValue(progressArg.getValue() + 1);
				}
				progressArg.setValue(progressArg.getMax()); 
				fis.close();
				put.close(); 
			} else if (file.isDirectory()) {
				// path = ftpFile.getAbsolutePath();
				path = file.getPath().replace(selPath, "");
				ftpFile.setName(path.replace("\\", "/"));
				ftpClient.sendServer("MKD " + path + "\r\n");
				ftpClient.readServerResponse();
				File[] listFiles = file.listFiles();
				for (File subFile : listFiles) {
					Thread.sleep(0, 50);
					copyFile(subFile, ftpFile);
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.exit(0);
			// JOptionPane.showMessageDialog(localPanel, e1.getMessage());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

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
				FtpFile ftpFile = (FtpFile) queueValues[1]; 
				if (file != null) {
					selPath = file.getParent();
					copyFile(file, ftpFile);
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