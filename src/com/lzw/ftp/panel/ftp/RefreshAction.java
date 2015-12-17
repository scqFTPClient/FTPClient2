package com.lzw.ftp.panel.ftp;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * ˢ�°�ť�Ķ���������
 */
class RefreshAction extends AbstractAction {
	private FtpPanel ftpPanel;

	/**
	 * ���췽��
	 * 
	 * @param ftpPanel
	 *            - FTP��Դ�������
	 * @param name
	 *            - ����������
	 * @param icon
	 *            - ������ͼ��
	 */
	public RefreshAction(FtpPanel ftpPanel, String name, Icon icon) {
		super(name, icon); // ���ø��๹�췽��
		this.ftpPanel = ftpPanel; // ��ֵFTP������������
	}

	/**
	 * �������¼�������
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		ftpPanel.refreshCurrentFolder(); // ����ˢ��FTP��Դ�б�ķ���
	}
}