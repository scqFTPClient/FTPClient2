package com.lzw.ftp.panel.ftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;

import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.lzw.ftp.extClass.MyFTPClient;
import com.lzw.ftp.extClass.ProgressArg;

import sun.net.TelnetInputStream;

public class DownThread extends Thread {
	private final FtpPanel ftpPanel;
	private final MyFTPClient ftpClient;
	private boolean conRun = true;
	private String path;
	private Object[] queueValues;

	public DownThread(FtpPanel ftpPanel) {
		this.ftpPanel = ftpPanel;
		ftpClient = new MyFTPClient();
		MyFTPClient ftp = ftpPanel.ftpClient;
		try {
			ftpClient.openServer(ftp.getServer(), ftp.getPort());
			ftpClient.login(ftp.getName(), ftp.getPass());
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	/*
	 * 下载选中文件到本地文件夹中
	 */
	private void downFile(FTPFile file, File localFolder) {

//		Object[] args = ftpPanel.queue.peek();
//		if (queueValues == null || args == null || !queueValues[0].equals(args[0]))
//			return;
//		try {
//			String ftpFileStr = file.getAbsolutePath().replaceFirst(path + "/", "");
//			if (file.isFile()) {
//				TelnetInputStream ftpIs = ftpClient.get(file.getName());
//				if (ftpIs == null) {
//					JOptionPane.showMessageDialog(this.ftpPanel, file.getName() + "�޷�����");
//					return;
//				}
//				File downFile = new File(localFolder, ftpFileStr);
//				FileOutputStream fout = new FileOutputStream(downFile, true);
//				double fileLength = file.getLongSize() / Math.pow(1024, 2);
//				ProgressArg progressArg = new ProgressArg((int) (file.getLongSize() / 1024), 0, 0);
//				String size = String.format("%.4f MB", fileLength);
//				Object[] row = new Object[] { ftpFileStr, size, downFile.getAbsolutePath(), ftpClient.getServer(),
//						progressArg };
//
//				byte[] data = new byte[1024];
//				int read = -1;
//				while ((read = ftpIs.read(data)) > 0) {
//					Thread.sleep(0, 30);
//					fout.write(data, 0, read);
//					progressArg.setValue(progressArg.getValue() + 1);
//				}
//				progressArg.setValue(progressArg.getMax());
//				fout.close();
//				ftpIs.close();
//			} else if (file.isDirectory()) {
//				File directory = new File(localFolder, ftpFileStr);
//				directory.mkdirs();
//				//切换目录，完成下载
//				ftpClient.cd(file.getName());
//				InputStreamReader list = new InputStreamReader(ftpClient.list());
//				BufferedReader br = new BufferedReader(list);
//				String nameStr = null;
//				while ((nameStr = br.readLine()) != null) {
//					Thread.sleep(0, 50);
//					String name = nameStr.substring(39);
//					String size = nameStr.substring(18, 39);
//					FtpFile ftpFile = new FtpFile();
//					ftpFile.setName(name);
//					ftpFile.setPath(file.getAbsolutePath());
//					ftpFile.setFileSize(size);
//					downFile(ftpFile, localFolder);
//				}
//				list.close();
//				br.close();
//				ftpClient.cdUp();
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
	}

	
	
	public void run() {
		while (conRun) {
			try {
				Thread.sleep(1000);

				queueValues = ftpPanel.queue.peek();
				if (queueValues == null) {
					continue;
				}
				String fileName = (String) queueValues[0];
				String localFolder = (String) queueValues[1];
				if (fileName != null) {
					System.out.println("本地文件夹: " + localFolder);
					String remoteFolder = ftpPanel.getPwd();
					System.out.println("remoteFolder:" + remoteFolder); // data/ftp
					System.out.println("fileNmae:" + fileName);
					System.out.println("localFolder" + localFolder);
//					ftpClient.download(fileName, remoteFolder, localFolder);
					ftpClient.download(fileName, remoteFolder, localFolder);
					path = null;
					ftpPanel.frame.getLocalPanel().refreshCurrentFolder();
				}
				Object[] args = ftpPanel.queue.peek();
				if (queueValues == null || args == null || !queueValues[0].equals(args[0]))
					continue;
				ftpPanel.queue.poll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("thread is UnAlive!");
	}
}