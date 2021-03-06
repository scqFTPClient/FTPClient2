package com.lzw.ftp.panel.ftp;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.FTPFile;
/*
 * 重命名按钮,修改远程文件名
 */
class RenameAction extends AbstractAction {
	private FtpPanel ftpPanel;

	public RenameAction(FtpPanel ftpPanel, String name, Icon icon) {
		super(name, icon); 
		this.ftpPanel = ftpPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int selRow = ftpPanel.ftpDiskTable.getSelectedRow();
		if (selRow < 0)
			return;
		//获取选中文件名
		Object fileName =  ftpPanel.ftpDiskTable.getValueAt(selRow, 0);
		String newName = JOptionPane.showInputDialog(ftpPanel, "请输入新文件名：");
		if (fileName.equals(".") || fileName.equals("..")
				|| newName == null)
			return;
		try {
			ftpPanel.ftpClient.sendServer("RNFR " + fileName + "\r\n");
			ftpPanel.ftpClient.readServerResponse();
			ftpPanel.ftpClient.sendServer("RNTO " + newName + "\r\n");
			ftpPanel.ftpClient.readServerResponse();
			ftpPanel.refreshCurrentFolder();
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
		
	}
}