/**
 * 
 */
package com.lzw.ftp;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

class AboutItemAction implements ActionListener {
	private final FTP_Client_Frame frame;
	private JLabel imgLabel;
	private JPanel topPane;

	AboutItemAction(FTP_Client_Frame client_Frame) {
		frame = client_Frame;
		URL url = frame.getClass().getResource("/com/lzw/ftp/res/about.jpg");
		ImageIcon aboutImage = new ImageIcon(url);
		imgLabel = new JLabel(aboutImage);
		topPane = (JPanel) frame.getRootPane().getGlassPane();
		topPane.setLayout(new BorderLayout());
		topPane.add(imgLabel, BorderLayout.CENTER);
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