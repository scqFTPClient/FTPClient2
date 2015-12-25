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
import com.lzw.ftp.extClass.MyFTPClient;
import com.lzw.ftp.extClass.FtpFile;
import com.lzw.ftp.panel.local.LocalPanel;

class DelFileAction extends AbstractAction {
	private FtpPanel ftpPanel;

	public DelFileAction(FtpPanel ftpPanel, String name, Icon icon) {
		super(name, icon);
		this.ftpPanel = ftpPanel;
	}

	public void actionPerformed(ActionEvent e) {
		final int[] selRows = ftpPanel.ftpDiskTable.getSelectedRows();
		if (selRows.length < 1)
			return;
		int confirmDialog = JOptionPane.showConfirmDialog(ftpPanel, "确定要删除吗？");
		if (confirmDialog == JOptionPane.YES_OPTION) {
			Runnable runnable = new Runnable() {
				private void delFile(FtpFile file) {
					MyFTPClient ftpClient = ftpPanel.ftpClient; 
					try {
						if (file.isFile()) {
							ftpClient.sendServer("DELE " + file.getName()
									+ "\r\n"); 
							ftpClient.readServerResponse(); 
						} else if (file.isDirectory()) { 
							ftpClient.cd(file.getName());
							InputStreamReader list = new InputStreamReader(
									ftpClient.list());
							BufferedReader br = new BufferedReader(list);
							String nameStr = null;
							while ((nameStr = br.readLine()) != null) {
								Thread.sleep(0, 100);
								String name = nameStr.substring(39); 
								String size = nameStr.substring(18, 39);
								FtpFile ftpFile = new FtpFile();
								ftpFile.setName(name);
								ftpFile.setPath(file.getAbsolutePath());
								ftpFile.setSize(size);
								delFile(ftpFile);
							}
							list.close();
							br.close();
							ftpClient.cdUp();
							ftpClient.sendServer("RMD " + file.getName()
									+ "\r\n");
							ftpClient.readServerResponse();
						}
					} catch (Exception ex) {
						Logger.getLogger(LocalPanel.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				}

				public void run() {
					for (int i = 0; i < selRows.length; i++) {
						final FtpFile file = (FtpFile) ftpPanel.ftpDiskTable
								.getValueAt(selRows[i], 0);
						if (file != null) {
							delFile(file);
							try {
								ftpPanel.ftpClient.sendServer("RMD "
										+ file.getName() + "\r\n");
								ftpPanel.ftpClient.readServerResponse();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					DelFileAction.this.ftpPanel.refreshCurrentFolder();
					JOptionPane.showMessageDialog(ftpPanel, "删除成功");
				}
			};
			new Thread(runnable).start();
		}
	}
}