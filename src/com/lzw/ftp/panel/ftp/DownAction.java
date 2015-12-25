package com.lzw.ftp.panel.ftp;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.lzw.ftp.extClass.FtpFile;

class DownAction extends AbstractAction {
	private final FtpPanel ftpPanel;

	public DownAction(FtpPanel ftpPanel, String name, Icon icon) {
		super(name, icon);
		this.ftpPanel = ftpPanel;
		setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final int[] selRows = ftpPanel.ftpDiskTable.getSelectedRows();
		if (selRows.length < 1)
			return;
		for (int i = 0; i < selRows.length; i++) {
			final FtpFile file = (FtpFile) ftpPanel.ftpDiskTable.getValueAt(
					selRows[i], 0);
			if (file != null) {
				File currentFolder = ftpPanel.frame.getLocalPanel()
						.getCurrentFolder();
				ftpPanel.queue.offer(new Object[] { file, currentFolder });
			}
		}
	}
}