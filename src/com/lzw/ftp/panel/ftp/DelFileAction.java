package com.lzw.ftp.panel.ftp;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import com.lzw.ftp.extClass.FtpClient;
import com.lzw.ftp.extClass.FtpFile;
import com.lzw.ftp.panel.local.LocalPanel;

/**
 * FTP����ɾ����ť�Ķ���������
 */
class DelFileAction extends AbstractAction {
	private FtpPanel ftpPanel;

	/**
	 * ɾ�������������Ĺ��췽��
	 * 
	 * @param ftpPanel
	 *            - FTP��Դ�������
	 * @param name
	 *            - ��������
	 * @param icon
	 *            - ͼ��
	 */
	public DelFileAction(FtpPanel ftpPanel, String name, Icon icon) {
		super(name, icon);
		this.ftpPanel = ftpPanel;
	}

	public void actionPerformed(ActionEvent e) {
		// ��ȡ��ʾFTP��Դ�б�ı�������ǰѡ���������
		final int[] selRows = ftpPanel.ftpDiskTable.getSelectedRows();
		if (selRows.length < 1)
			return;
		int confirmDialog = JOptionPane.showConfirmDialog(ftpPanel, "ȷ��Ҫɾ����");
		if (confirmDialog == JOptionPane.YES_OPTION) {
			Runnable runnable = new Runnable() {
				/**
				 * ɾ���������ļ��ķ���
				 * 
				 * @param file
				 *            - �ļ�����
				 */
				private void delFile(FtpFile file) {
					FtpClient ftpClient = ftpPanel.ftpClient; // ��ȡftpClientʵ��
					try {
						if (file.isFile()) { // ���ɾ�������ļ�
							ftpClient.sendServer("DELE " + file.getName()
									+ "\r\n"); // ����ɾ���ļ�������
							ftpClient.readServerResponse(); // ���շ��ر���
						} else if (file.isDirectory()) { // ���ɾ�������ļ���
							ftpClient.cd(file.getName()); // ���뵽���ļ���
							InputStreamReader list = new InputStreamReader(
									ftpClient.list()); // ��ȡ�ļ��б�
							BufferedReader br = new BufferedReader(list);
							String nameStr = null;
							while ((nameStr = br.readLine()) != null) {// ����ÿ���ļ�
								Thread.sleep(0, 100); // �߳�����
								String name = nameStr.substring(39); // �����ļ���
								String size = nameStr.substring(18, 39);// �����ļ���С
								FtpFile ftpFile = new FtpFile(); // �����ļ�����
								ftpFile.setName(name); // �����ļ���
								ftpFile.setPath(file.getAbsolutePath());// �����ļ�·��
								ftpFile.setSize(size); // �����ļ���С
								delFile(ftpFile); // �ݹ�ɾ���ļ����ļ���
							}
							list.close();// �رն�ȡ�ļ��б��������
							br.close();
							ftpClient.cdUp(); // �����ϲ��ļ���
							ftpClient.sendServer("RMD " + file.getName()
									+ "\r\n"); // ����ɾ���ļ���ָ��
							ftpClient.readServerResponse(); // ���շ�����
						}
					} catch (Exception ex) {
						Logger.getLogger(LocalPanel.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				}

				/**
				 * �̵߳����巽��
				 * 
				 * @see java.lang.Runnable#run()
				 */
				public void run() {
					// ������ʾFTP��Դ�ı�������ѡ����
					for (int i = 0; i < selRows.length; i++) {
						// ��ȡÿ�еĵ�һ����Ԫֵ����ת��ΪFtpFile����
						final FtpFile file = (FtpFile) ftpPanel.ftpDiskTable
								.getValueAt(selRows[i], 0);
						if (file != null) {
							delFile(file); // ����ɾ���ļ��ĵݹ鷽��
							try {
								// ���������ɾ���ļ��еķ���
								ftpPanel.ftpClient.sendServer("RMD "
										+ file.getName() + "\r\n");
								// ��ȡFTP�������ķ�����
								ftpPanel.ftpClient.readServerResponse();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					// ˢ��FTP��������Դ�б�
					DelFileAction.this.ftpPanel.refreshCurrentFolder();
					JOptionPane.showMessageDialog(ftpPanel, "ɾ���ɹ���");
				}
			};
			new Thread(runnable).start();
		}
	}
}