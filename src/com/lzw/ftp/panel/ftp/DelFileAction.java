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

import org.apache.commons.net.ftp.FTPFile;

import com.lzw.ftp.extClass.MyFTPClient;
import com.lzw.ftp.panel.local.LocalPanel;

//删除远程文件
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
				private void delFile(String pathName) {
					
				}
				
				public void run() {
					String path = DelFileAction.this.ftpPanel.ftpClient.pwd();
					for (int i = 0; i < selRows.length; i++) {
						String filename = (String) ftpPanel.ftpDiskTable.getValueAt(selRows[0], 0);
						if (filename != null) {
							try {
								DelFileAction.this.ftpPanel.ftpClient.deleteFile(path + "/" + filename);
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