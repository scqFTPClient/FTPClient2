/**
 * 
 */
package com.lzw.ftp;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;

class BugItemAction implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				URI uri = new URI("mailto:mingrisoft@mingrisoft.com");
				desktop.mail(uri);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}