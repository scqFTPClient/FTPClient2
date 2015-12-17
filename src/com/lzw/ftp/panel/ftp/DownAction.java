package com.lzw.ftp.panel.ftp;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import com.lzw.ftp.extClass.FtpFile;

/**
 * ���ذ�ť�Ķ���������
 */
class DownAction extends AbstractAction {
	private final FtpPanel ftpPanel;

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
	public DownAction(FtpPanel ftpPanel, String name, Icon icon) {
		super(name, icon); // ���ø��๹�췽��
		this.ftpPanel = ftpPanel; // ��ֵFTP��Դ������������
		setEnabled(false); // ���ö���������
	}

	/**
	 * �������¼�������
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// ��ȡFTP��Դ��������ѡ����
		final int[] selRows = ftpPanel.ftpDiskTable.getSelectedRows();
		if (selRows.length < 1)
			return;
		// ������������ѡ����
		for (int i = 0; i < selRows.length; i++) {
			// ��ȡÿ�еĵ�һ����Ԫֵ��ת����FtpFile��Ķ���
			final FtpFile file = (FtpFile) ftpPanel.ftpDiskTable.getValueAt(
					selRows[i], 0);
			if (file != null) {
				// ��ȡ������Դ�������ĵ�ǰ�ļ���
				File currentFolder = ftpPanel.frame.getLocalPanel()
						.getCurrentFolder();
				// ��FTP�ļ�����ͱ��ص�ǰ�ļ��ж������������ӵ����ض�����
				ftpPanel.queue.offer(new Object[] { file, currentFolder });
			}
		}
	}
}