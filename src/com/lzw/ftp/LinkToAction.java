package com.lzw.ftp;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import com.lzw.ftp.panel.manager.FtpLinkDialog;

/**
 * @author Li Zhong Wei ���ӵ���ť�Ķ�����
 */
class LinkToAction extends AbstractAction {
	private final FTP_Client_Frame client_Frame;

	/**
	 * ���췽��
	 * 
	 * @param frame
	 *            �����������
	 * @param string
	 *            ����������
	 * @param icon
	 *            ������ͼ��
	 */
	public LinkToAction(FTP_Client_Frame frame, String string, Icon icon) {
		super(string, icon); // ���ø��๹�췽��
		client_Frame = frame; // ��ֵ�����������
	}

	/**
	 * ���������¼�����
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// �������ӵ�FTPվ��Ի���
		FtpLinkDialog dialog = new FtpLinkDialog(this.client_Frame);
	}
}