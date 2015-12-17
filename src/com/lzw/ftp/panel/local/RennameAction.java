package com.lzw.ftp.panel.local;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

/**
 * ��������ť�Ķ���������
 */
class RennameAction extends AbstractAction {
	private LocalPanel localPanel; // ������Դ������������

	/**
	 * ���췽��
	 * 
	 * @param localPanel
	 *            ������Դ�������
	 * @param name
	 *            ����������������
	 * @param icon
	 *            ������������ͼ��
	 */
	public RennameAction(LocalPanel localPanel, String name, Icon icon) {
		super(name, icon); // ���ø���Ĺ��췽��
		this.localPanel = localPanel; // ��ֵ������Դ������������
	}

	/**
	 * �������������¼�������
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// ��ȡ������Դ����ѡ���к�
		int selRow = this.localPanel.localDiskTable.getSelectedRow();
		if (selRow < 0)
			return;
		// ��ȡѡ���еĵ�һ����Ԫֵ
		Object value = this.localPanel.localDiskTable.getValueAt(selRow, 0);
		if (!(value instanceof File))
			return;
		// ���õ�Ԫֵת��ΪFile��Ķ���
		File file = (File) value;
		// ʹ�öԻ�������û���������ļ���
		String fileName = JOptionPane
				.showInputDialog("���������ļ���", file.getName());
		if (fileName == null)
			return;
		// ���������Ƶ��ļ�
		File renFile = new File(file.getParentFile(), fileName);
		System.out.println(renFile);
		boolean isRename = file.renameTo(renFile); // ��ԭ�ļ�������
		// ˢ���ļ���
		this.localPanel.refreshFolder(file.getParentFile());
		if (isRename) {
			JOptionPane.showMessageDialog(this.localPanel, "������Ϊ" + fileName
					+ "�ɹ���");
		} else {
			JOptionPane.showMessageDialog(this.localPanel, "�޷�������Ϊ" + fileName
					+ "��", "�ļ�������", JOptionPane.ERROR_MESSAGE);
		}
	}
}