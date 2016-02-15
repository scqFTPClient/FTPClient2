package com.lzw.ftp.panel.ftp;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

class CreateFolderAction extends AbstractAction {
	private FtpPanel ftpPanel;

	public CreateFolderAction(FtpPanel ftpPanel, String name, Icon icon) {
		super(name, icon);
		this.ftpPanel = ftpPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String folderName = JOptionPane.showInputDialog("请输入文件夹名称：");
		if (folderName == null)
			return;
		int read = -1;
		try {
			ftpPanel.ftpClient.sendServer("MKD " + folderName + "\r\n");
			read = ftpPanel.ftpClient.readServerResponse();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (read == 257)
			JOptionPane.showMessageDialog(ftpPanel, folderName + "文件夹创建成功",
					"创建文件夹", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(ftpPanel, folderName + "文件夹无法创建",
					"创建文件夹", JOptionPane.ERROR_MESSAGE);
		this.ftpPanel.refreshCurrentFolder();
	}
}