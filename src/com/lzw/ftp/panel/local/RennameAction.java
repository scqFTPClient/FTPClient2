package com.lzw.ftp.panel.local;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

class RennameAction extends AbstractAction {
	private LocalPanel localPanel; 

	public RennameAction(LocalPanel localPanel, String name, Icon icon) {
		super(name, icon); 
		this.localPanel = localPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int selRow = this.localPanel.localDiskTable.getSelectedRow();
		if (selRow < 0)
			return;
		Object value = this.localPanel.localDiskTable.getValueAt(selRow, 0);
		if (!(value instanceof File))
			return;
		File file = (File) value;
		String fileName = JOptionPane
				.showInputDialog("请输入新文件名", file.getName());
		if (fileName == null)
			return;

		File renFile = new File(file.getParentFile(), fileName);
		System.out.println(renFile);
		boolean isRename = file.renameTo(renFile);

		this.localPanel.refreshFolder(file.getParentFile());
		if (isRename) {
			JOptionPane.showMessageDialog(this.localPanel, "重命名为" + fileName
					+ "成功");
		} else {
			JOptionPane.showMessageDialog(this.localPanel, "无法重命名为" + fileName
					+ "。", "文件重命名", JOptionPane.ERROR_MESSAGE);
		}
	}
}