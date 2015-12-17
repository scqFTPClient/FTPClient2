package com.lzw.ftp.panel.local;

import java.util.Queue;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import com.lzw.ftp.extClass.DiskFile;
import com.lzw.ftp.extClass.FtpFile;

/**
 * �ϴ��ļ��Ķ���������
 */
class UploadAction extends AbstractAction {
	private LocalPanel localPanel; // ������Դ������������

	/**
	 * ���췽��
	 * 
	 * @param localPanel
	 *            ������Դ�������
	 * @param name
	 *            ����������
	 * @param icon
	 *            ������ͼ��
	 */
	public UploadAction(LocalPanel localPanel, String name, Icon icon) {
		super(name, icon); // ���ø��๹�췽��
		this.localPanel = localPanel; // ��ֵ������Դ������������
		setEnabled(false); // ���ð�ť������
	}

	/**
	 * �ϴ��ļ��������¼�������
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(java.awt.event.ActionEvent evt) {
		// ��ȡ�û�ѡ��Ķ���ļ����ļ���
		int[] selRows = this.localPanel.localDiskTable.getSelectedRows();
		if (selRows.length < 1) {
			JOptionPane.showMessageDialog(this.localPanel, "��ѡ���ϴ����ļ����ļ���");
			return;
		}
		// ��ȡFTP�������ĵ�ǰ·��
		String pwd = this.localPanel.frame.getFtpPanel().getPwd();
		// ����FTP��ǰ·�����ļ��ж���
		FtpFile ftpFile = new FtpFile("", pwd, true);
		// ����������Դ�ı��
		for (int i = 0; i < selRows.length; i++) {
			Object valueAt = this.localPanel.localDiskTable.getValueAt(
					selRows[i], 0); // ��ȡ���ѡ���еĵ�һ������
			if (valueAt instanceof DiskFile) {
				final DiskFile file = (DiskFile) valueAt;
				// ��ȡ����������еĶ��У��ö�����LinkedList���ʵ������
				Queue<Object[]> queue = this.localPanel.queue;
				queue.offer(new Object[] { file, ftpFile });// ִ��offer���������β��Ӷ���
			}
		}
	}
}