package com.lzw.ftp.panel.local;

import java.util.Queue;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.FTPFile;

import com.lzw.ftp.extClass.DiskFile;

class UploadAction extends AbstractAction {
	private LocalPanel localPanel;

	public UploadAction(LocalPanel localPanel, String name, Icon icon) {
		super(name, icon);
		this.localPanel = localPanel;
		setEnabled(false);
	}

	public void actionPerformed(java.awt.event.ActionEvent evt) {
		int[] selRows = this.localPanel.localDiskTable.getSelectedRows();
		if (selRows.length < 1) {
			JOptionPane.showMessageDialog(this.localPanel, "请选择上传的文件或文件夹");
			return;
		}

		String pwd = this.localPanel.frame.getFtpPanel().getPwd();

		FTPFile ftpFile = new FTPFile();

		for (int i = 0; i < selRows.length; i++) {
			Object valueAt = this.localPanel.localDiskTable.getValueAt(
					selRows[i], 0); 
			if (valueAt instanceof DiskFile) {
				final DiskFile file = (DiskFile) valueAt;
				Queue<Object[]> queue = this.localPanel.queue;
				queue.offer(new Object[] { file, ftpFile });
			}
		}
	}
}