package com.lzw.ftp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/*
 * 关于本软件的信息
 */
class AboutItemAction implements ActionListener {
	private final FTP_Client_Frame frame;
	private JPanel topPane;

	AboutItemAction(FTP_Client_Frame client_Frame) {
		frame = client_Frame;
		topPane = (JPanel) frame.getRootPane().getGlassPane();
		topPane.setLayout(new BorderLayout());
		topPane.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				topPane.setVisible(false);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		topPane.setVisible(true);
	}
}