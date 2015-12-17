/**
 * 
 */
package com.lzw.ftp.panel.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Queue;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import com.lzw.ftp.extClass.FtpClient;
import com.lzw.ftp.extClass.FtpFile;
import com.lzw.ftp.extClass.ProgressArg;
import com.lzw.ftp.panel.ftp.FtpPanel;
import com.lzw.ftp.panel.queue.UploadPanel;

/**
 * @author Li Zhong Wei FTP�ļ�����ģ��ı����ļ��ϴ����е��߳�
 */
class UploadThread extends Thread {
	private LocalPanel localPanel;
	String path = "";// �ϴ��ļ��ı������·��
	String selPath;// ѡ��ı����ļ���·��
	private boolean conRun = true; // �̵߳Ŀ��Ʊ���
	private FtpClient ftpClient; // FTP������
	private Object[] queueValues; // ������������

	/**
	 * �����ϴ������̵߳Ĺ��췽��
	 * 
	 * @param localPanel
	 *            - ������Դ�������
	 * @param server
	 *            - FTP��������ַ
	 * @param port
	 *            - FTP�������˿ں�
	 * @param userStr
	 *            - ��¼FTP���������û���
	 * @param passStr
	 *            - ��¼FTP������������
	 */
	public UploadThread(LocalPanel localPanel, String server, int port,
			String userStr, String passStr) {
		try {
			ftpClient = new FtpClient(server, port);
			ftpClient.login(userStr, passStr);
			ftpClient.binary();
			path = ftpClient.pwd();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.localPanel = localPanel;
		new Thread() { // �������ַ�����ͨѶ���߳�
			public void run() {
				while (conRun) {
					try {
						Thread.sleep(30000);
						// ��ʱ�������������Ϣ����������
						UploadThread.this.ftpClient.noop();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public void stopThread() { // ֹͣ�̵߳ķ���
		conRun = false;
	}

	private void copyFile(File file, FtpFile ftpFile) { // �ݹ�����ļ��еķ���
		// �ж϶�������Ƿ�ִ����ͣ����
		while (localPanel.frame.getQueuePanel().isStop()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		Object[] args = localPanel.queue.peek();
		// �ж϶��ж��ǲ�����һ�����������
		if (queueValues == null || args == null
				|| !queueValues[0].equals(args[0]))
			return;
		try {
			path = file.getParentFile().getPath().replace(selPath, "");
			ftpFile.setName(path.replace("\\", "/"));
			path = ftpFile.getAbsolutePath();
			System.out.println(path);
			if (file.isFile()) {
				UploadPanel uploadPanel = localPanel.frame.getUploadPanel();
				String remoteFile = path + "/" + file.getName(); // Զ��FTP���ļ�������·��
				System.out.println("remoteFile:" + remoteFile);
				double fileLength = file.length() / Math.pow(1024, 2);
				ProgressArg progressArg = new ProgressArg(
						(int) (file.length() / 1024), 0, 0);
				String size = String.format("%.4f MB", fileLength);
				Object[] row = new Object[] { file.getAbsoluteFile(), size,
						remoteFile, ftpClient.getServer(), progressArg };
				uploadPanel.addRow(row);
				OutputStream put = ftpClient.put(remoteFile); // ��ȡ�������ļ��������
				FileInputStream fis = null; // �����ļ���������
				try {
					fis = new FileInputStream(file); // ��ʼ���ļ���������
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				int readNum = 0;
				byte[] data = new byte[1024]; // �����С
				while ((readNum = fis.read(data)) > 0) { // ��ȡ�����ļ�������
					Thread.sleep(0, 30); // �߳�����
					put.write(data, 0, readNum); // �����������
					progressArg.setValue(progressArg.getValue() + 1);// �ۼӽ�����
				}
				progressArg.setValue(progressArg.getMax()); // ����������
				fis.close(); // �ر��ļ�������
				put.close(); // �رշ����������
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

	/**
	 * �̵߳����巽��
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() { // �̵߳����巽��
		while (conRun) {
			try {
				Thread.sleep(1000); // �߳�����1��
				Queue<Object[]> queue = localPanel.queue; // ��ȡ�������Ķ��ж���
				queueValues = queue.peek(); // ��ȡ�����׵Ķ���
				if (queueValues == null) { // ����ö���Ϊ��
					continue; // ������һ��ѭ��
				}
				File file = (File) queueValues[0]; // ��ȡ�����еı����ļ�����
				FtpFile ftpFile = (FtpFile) queueValues[1]; // ��ȡ�����е�FTP�ļ�����
				if (file != null) {
					selPath = file.getParent();
					copyFile(file, ftpFile); // ���õݹ鷽���ϴ��ļ�
					FtpPanel ftpPanel = localPanel.frame.getFtpPanel();
					ftpPanel.refreshCurrentFolder(); // ˢ��FTP����е���Դ
				}
				Object[] args = queue.peek();
				// �ж϶��ж��Ƿ�Ϊ�������һ������
				if (queueValues == null || args == null
						|| !queueValues[0].equals(args[0])) {
					continue;
				}
				queue.remove(); // �Ƴ�������Ԫ��
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}