package com.lzw.ftp.panel.ftp;

import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import com.lzw.ftp.extClass.FtpFile;

/**
 * ��������ť�Ķ���������
 */
class RenameAction extends AbstractAction {
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
	public RenameAction(FtpPanel ftpPanel, String name, Icon icon) {
		super(name, icon); // ���ø���Ĺ��췽��
		this.ftpPanel = ftpPanel; // ��ֵFTP��Դ������������
	}

	/**
	 * ������FTP�ļ����¼�������
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// ��ȡ��ʾFTP��Դ�ı��ǰѡ���к�
		int selRow = ftpPanel.ftpDiskTable.getSelectedRow();
		if (selRow < 0)
			return;
		// ��ȡ��ǰ�еĵ�һ�����Ԫֵ����ת����FtpFile���͵Ķ���
		FtpFile file = (FtpFile) ftpPanel.ftpDiskTable.getValueAt(selRow, 0);
		// ʹ�öԻ�������û���������ļ����ļ�������
		String newName = JOptionPane.showInputDialog(ftpPanel, "�����������ơ�");
		if (file.getName().equals(".") || file.getName().equals("..")
				|| newName == null)
			return;
		try {
			// �������������������ָ��
			ftpPanel.ftpClient.sendServer("RNFR " + file.getName() + "\r\n");
			ftpPanel.ftpClient.readServerResponse();
			ftpPanel.ftpClient.sendServer("RNTO " + newName + "\r\n");
			ftpPanel.ftpClient.readServerResponse();
			ftpPanel.refreshCurrentFolder(); // ˢ�µ�ǰ�ļ���
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}