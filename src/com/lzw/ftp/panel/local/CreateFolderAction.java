package com.lzw.ftp.panel.local;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

/**
 * �����ļ��а�ť�Ķ���������
 */
class CreateFolderAction extends AbstractAction {
	private LocalPanel localPanel; // ������Դ������������

	/**
	 * ���췽��
	 * 
	 * @param localPanel
	 *            ������Դ���
	 * @param name
	 *            ����������
	 * @param icon
	 *            ������ͼ��
	 */
	public CreateFolderAction(LocalPanel localPanel, String name, Icon icon) {
		super(name, icon); // ���ø��๹�췽��
		this.localPanel = localPanel; // ��ֵ������Դ������������
	}

	/**
	 * �������¼��ķ���
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// ʹ������Ի�������û�������ļ�������
		String folderName = JOptionPane.showInputDialog("�������ļ������ƣ�");
		if (folderName == null)
			return;
		File curFolder = null;
		// ��ȡ������Դ���ĵ�ǰѡ���к�
		int selRow = localPanel.localDiskTable.getSelectedRow();
		if (selRow < 0) {
			// ������ǰ�ļ��ж���
			curFolder = new File(localPanel.localSelFilePathLabel.getText());
		} else {
			// ��ȡ���ѡ���еĵ�һ����Ԫֵ
			Object value = localPanel.localDiskTable.getValueAt(selRow, 0);
			if (value instanceof File) { // �����Ԫֵ���ļ������ȡ�ļ����ϼ��ļ���
				curFolder = (File) value;
				if (curFolder.getParentFile() != null)
					curFolder = curFolder.getParentFile();
			} else
				// ������ݽ����·����ǩ������ǰ�ļ��ж���
				curFolder = new File(localPanel.localSelFilePathLabel.getText());
		}
		// ������ǰ�ļ����µ����ļ��ж���
		File tempFile = new File(curFolder, folderName);
		if (tempFile.exists()) {// ���������ͬ�ļ����ļ���
			JOptionPane.showMessageDialog(localPanel, folderName
					+ "����ʧ�ܣ��Ѿ����ڴ����Ƶ��ļ��л��ļ���", "�����ļ���",
					JOptionPane.ERROR_MESSAGE);// ��ʾ�û������Ѵ���
			return; // ����������
		}
		if (tempFile.mkdir()) // �����ļ���
			JOptionPane.showMessageDialog(localPanel, folderName + "�ļ��У������ɹ���",
					"�����ļ���", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(localPanel, folderName + "�ļ����޷���������",
					"�����ļ���", JOptionPane.ERROR_MESSAGE);
		this.localPanel.refreshFolder(curFolder);// ˢ���ļ���
	}
}