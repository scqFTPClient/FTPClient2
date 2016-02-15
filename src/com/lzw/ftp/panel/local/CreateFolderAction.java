package com.lzw.ftp.panel.local;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

class CreateFolderAction extends AbstractAction {
	private LocalPanel localPanel;

	public CreateFolderAction(LocalPanel localPanel, String name, Icon icon) {
		super(name, icon); 
		this.localPanel = localPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String folderName = JOptionPane.showInputDialog("请输入文件夹名称：");
		if (folderName == null)
			return;
		File curFolder = null;
		int selRow = localPanel.localDiskTable.getSelectedRow();
		if (selRow < 0) {
			curFolder = new File(localPanel.localSelFilePathLabel.getText());
		} else {
			Object value = localPanel.localDiskTable.getValueAt(selRow, 0);
			if (value instanceof File) {
				curFolder = (File) value;
				if (curFolder.getParentFile() != null)
					curFolder = curFolder.getParentFile();
			} else
				curFolder = new File(localPanel.localSelFilePathLabel.getText());
		}

		File tempFile = new File(curFolder, folderName);
		if (tempFile.exists()) {
			JOptionPane.showMessageDialog(localPanel, folderName
					+ "创建失败，已经存在此名称的文件夹或文件", "创建文件夹",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (tempFile.mkdir())
			JOptionPane.showMessageDialog(localPanel, folderName + "文件夹，创建成功",
					"创建文件夹", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(localPanel, folderName + "文件夹无法创建",
					"创建文件夹", JOptionPane.ERROR_MESSAGE);
		this.localPanel.refreshFolder(curFolder);
	}
}