package com.lzw.ftp.panel.local;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import com.lzw.ftp.extClass.DiskFile;

class DelFileAction extends AbstractAction {
	private LocalPanel localPanel;

	public DelFileAction(LocalPanel localPanel, String name, Icon icon) {
		super(name, icon);
		this.localPanel = localPanel;
	}

	public void actionPerformed(ActionEvent e) {
		final int[] selRows = this.localPanel.localDiskTable.getSelectedRows();
		if (selRows.length < 1)
			return;
		int confirmDialog = JOptionPane.showConfirmDialog(localPanel,
				"确定要删除吗？");
		if (confirmDialog == JOptionPane.YES_OPTION) { 
			Runnable runnable = new Runnable() {
				private void delFile(File file) {
					try {
						if (file.isFile()) {
							boolean delete = file.delete();
							if (!delete) {
								JOptionPane.showMessageDialog(localPanel, file
										.getAbsoluteFile()
										+ "文件无法删除", "删除文件",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
						} else if (file.isDirectory()) {
							File[] listFiles = file.listFiles();
							if (listFiles.length > 0) {
								for (File subFile : listFiles) {
									delFile(subFile);
								}
							}
							boolean delete = file.delete();
							if (!delete) { 
								JOptionPane.showMessageDialog(localPanel, file
										.getAbsoluteFile()
										+ "文件夹无法删除", "删除文件",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
					} catch (Exception ex) {
						Logger.getLogger(LocalPanel.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				}

				public void run() {
					File parent = null;
					for (int i = 0; i < selRows.length; i++) {
						Object value = DelFileAction.this.localPanel.localDiskTable
								.getValueAt(selRows[i], 0);
						if (!(value instanceof DiskFile))
							continue;
						DiskFile file = (DiskFile) value;
						if (parent == null)
							parent = file.getParentFile();
						if (file != null) {
							delFile(file);
						}
					}
					DelFileAction.this.localPanel.refreshFolder(parent);
					JOptionPane.showMessageDialog(localPanel, "删除成功");
				}
			};
			new Thread(runnable).start();
		}
	}
}