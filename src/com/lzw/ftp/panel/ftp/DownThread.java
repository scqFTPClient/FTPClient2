/**
 * 
 */
package com.lzw.ftp.panel.ftp;

import java.io.*;

import javax.swing.*;

import sun.net.*;

import com.lzw.ftp.extClass.*;

public class DownThread extends Thread {
	private final FtpPanel ftpPanel; // FTP��Դ�������
	private final FtpClient ftpClient; // FTP������
	private boolean conRun = true; // �̵߳Ŀ��Ʊ���
	private String path; // FTP��·����Ϣ
	private Object[] queueValues; // �������������

	/**
	 * ���췽��
	 * 
	 * @param ftpPanel
	 *            - FTP��Դ�������
	 */
	public DownThread(FtpPanel ftpPanel) {
		this.ftpPanel = ftpPanel;
		ftpClient = new FtpClient(); // �����µ�FTP���ƶ���
		FtpClient ftp = ftpPanel.ftpClient;
		try {
			// ���ӵ�FTP������
			ftpClient.openServer(ftp.getServer(), ftp.getPort());
			ftpClient.login(ftp.getName(), ftp.getPass()); // ��¼������
			ftpClient.binary(); // ʹ�ö����ƴ���
			ftpClient.noop();
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread() { // �������ַ�����ͨѶ���߳�
			public void run() {
				while (conRun) {
					try {
						Thread.sleep(30000);
						ftpClient.noop(); // ��ʱ�������������Ϣ����������
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public void stopThread() {// ֹͣ�̵߳ķ���
		conRun = false;
	}


	private void downFile(FtpFile file, File localFolder) {

		Object[] args = ftpPanel.queue.peek();
		// �ж϶��ж��Ƿ�Ϊ�������һ������
		if (queueValues == null || args == null
				|| !queueValues[0].equals(args[0]))
			return;
		try {
			String ftpFileStr = file.getAbsolutePath().replaceFirst(path + "/",
					"");
			if (file.isFile()) {
				// ��ȡ������ָ���ļ���������
				TelnetInputStream ftpIs = ftpClient.get(file.getName());
				if (ftpIs == null) {
					JOptionPane.showMessageDialog(this.ftpPanel, file.getName()
							+ "�޷�����");
					return;
				}
				// ���������ļ�����
				File downFile = new File(localFolder, ftpFileStr);
				// ���������ļ��������
				FileOutputStream fout = new FileOutputStream(downFile, true);
				// �����ļ���С
				double fileLength = file.getLongSize() / Math.pow(1024, 2);
				ProgressArg progressArg = new ProgressArg((int) (file
						.getLongSize() / 1024), 0, 0);
				String size = String.format("%.4f MB", fileLength);
				Object[] row = new Object[] { ftpFileStr, size,
						downFile.getAbsolutePath(), ftpClient.getServer(),
						progressArg };

				byte[] data = new byte[1024]; // ���建��
				int read = -1;
				while ((read = ftpIs.read(data)) > 0) { // ��ȡFTP�ļ����ݵ�����
					Thread.sleep(0, 30); // �߳�����
					fout.write(data, 0, read); // ���������д�뱾���ļ�
					// �ۼӽ����
					progressArg.setValue(progressArg.getValue() + 1);
				}
				progressArg.setValue(progressArg.getMax());// ��������
				fout.close(); // �ر��ļ������
				ftpIs.close(); // �ر�FTP�ļ�������
			} else if (file.isDirectory()) { // ������ص����ļ���
				// ���������ļ��ж���
				File directory = new File(localFolder, ftpFileStr);
				directory.mkdirs(); // �������ص��ļ���
				ftpClient.cd(file.getName()); // �ı�FTP�������ĵ�ǰ·��
				// ��ȡFTP���������ļ��б���Ϣ
				InputStreamReader list = new InputStreamReader(ftpClient.list());
				BufferedReader br = new BufferedReader(list);
				String nameStr = null;
				while ((nameStr = br.readLine()) != null) {
					Thread.sleep(0, 50);
					String name = nameStr.substring(39);
					String size = nameStr.substring(18, 39);
					FtpFile ftpFile = new FtpFile(); // ����FTP�ļ�����
					ftpFile.setName(name); // �����ļ���
					ftpFile.setPath(file.getAbsolutePath());// �����ļ�·��
					ftpFile.setSize(size); // �����ļ���С
					downFile(ftpFile, localFolder); // �ݹ�ִ�����ļ��е�����
				}
				list.close();
				br.close();
				ftpClient.cdUp(); // ����FTP�ϼ�·��
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void run() { // �߳�ҵ�񷽷�
		while (conRun) {
			try {
				Thread.sleep(1000);
				ftpClient.noop();

				queueValues = ftpPanel.queue.peek();
				if (queueValues == null) {
					continue;
				}
				FtpFile file = (FtpFile) queueValues[0];
				File localFolder = (File) queueValues[1];
				if (file != null) {
					path = file.getPath();
					ftpClient.cd(path);
					downFile(file, localFolder);
					path = null;
					ftpPanel.frame.getLocalPanel().refreshCurrentFolder();
				}
				Object[] args = ftpPanel.queue.peek();
				// �ж϶��ж��Ƿ�Ϊ�������һ������
				if (queueValues == null || args == null
						|| !queueValues[0].equals(args[0]))
					continue;
				ftpPanel.queue.poll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("thread is UnAlive!");
	}
}