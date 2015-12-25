package com.lzw.ftp;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import com.lzw.ftp.panel.manager.FtpLinkDialog;

class LinkToAction extends AbstractAction {
	private final FTP_Client_Frame client_Frame;

	public LinkToAction(FTP_Client_Frame frame, String string, Icon icon) {
		super(string, icon);
		client_Frame = frame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		FtpLinkDialog dialog = new FtpLinkDialog(this.client_Frame);
	}
}
