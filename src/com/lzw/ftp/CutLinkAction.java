/**
 * 
 */
package com.lzw.ftp;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * @author Li Zhong Wei �Ͽ���ť�Ķ���������
 */
class CutLinkAction extends AbstractAction {
	private FTP_Client_Frame frame; // ����������ö���

	/**
	 * ���췽��
	 * 
	 * @param client_Frame
	 *            �����������
	 * @param string
	 *            ��������ƣ�����ʾ�ڰ�ť��˵��������
	 * @param icon
	 *            ������ͼ�꣬����ʾ�ڰ�ť��˵��������
	 */
	public CutLinkAction(FTP_Client_Frame client_Frame, String string, Icon icon) {
		super(string, icon); // ���ø���Ĺ��췽��
		frame = client_Frame; // ��ֵ���������ö���
		setEnabled(false); // ���ò�����״̬
	}

	/**
	 * ���?ť�����¼��ķ���
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			frame.ftpPanel.stopDownThread(); // ֹͣ�����߳�
			frame.localPanel.stopUploadThread(); // ֹͣ�ϴ��߳�
			frame.getFtpPanel().getQueue().clear(); // ����������
			frame.getFtpPanel().clearTable(); // ���FTP��Դ�������
			frame.getLocalPanel().getQueue().clear(); // �������Ķ���
			// ���FTP���Ӷ�����ڣ������Ѿ�����FTP������
			if (frame.ftpClient != null && frame.ftpClient.serverIsOpen()) {
				frame.ftpClient.sendServer("quit\r\n"); // ���ͶϿ����ӵ�FTPЭ�������
				frame.ftpClient.readServerResponse(); // ��ȡ���ر���
				frame.ftpClient = null;
			}
			// �����ϴ���ť������
			frame.localPanel.getActionMap().get("uploadAction").setEnabled(
					false);
			// �������ذ�ť������
			frame.ftpPanel.getActionMap().get("downAction").setEnabled(false);
			setEnabled(false); // ���ñ���ť���Ͽ���������
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}