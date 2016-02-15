package com.lzw.ftp.panel.ftp;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

class RefreshAction extends AbstractAction {
	private FtpPanel ftpPanel;

	public RefreshAction(FtpPanel ftpPanel, String name, Icon icon) {
		super(name, icon);
		this.ftpPanel = ftpPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ftpPanel.refreshCurrentFolder();
	}
}