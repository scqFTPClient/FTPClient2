package com.lzw.ftp.panel.ftp;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import org.apache.commons.net.ftp.FTPFile;

/*
 * 下载按钮，从远程下载文件到本地
 */
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
			final String fileName =  (String) ftpPanel.ftpDiskTable.getValueAt(selRows[i], 0);
			if (!fileName.equals(".") && !fileName.equals("..")) {
				String localFolder = ftpPanel.frame.getLocalPanel()
						.getCurrentFolder().toString();
				System.out.println(localFolder);
				ftpPanel.queue.offer(new Object[] { fileName, localFolder });
			}
		}
	}
}