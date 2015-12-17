package com.lzw.ftp.panel.local;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import com.lzw.ftp.extClass.DiskFile;

/**
 * ɾ�������ļ��Ķ���������
 */
class DelFileAction extends AbstractAction {
	private LocalPanel localPanel; // ������Դ�����������ö���

	/**
	 * �����������Ĺ��췽��
	 * 
	 * @param localPanel
	 *            ������Դ�������
	 * @param name
	 *            ��������
	 * @param icon
	 *            ������ͼ��
	 */
	public DelFileAction(LocalPanel localPanel, String name, Icon icon) {
		super(name, icon); // ���ø���Ĺ��췽��
		this.localPanel = localPanel; // ��ֵ������Դ������������
	}

	/**
	 * �������¼��ķ���
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// ��ȡ���ѡ���������
		final int[] selRows = this.localPanel.localDiskTable.getSelectedRows();
		if (selRows.length < 1) // ���û��ѡ��������
			return; // �����÷���
		int confirmDialog = JOptionPane.showConfirmDialog(localPanel,
				"ȷ��Ҫִ��ɾ����"); // �û�ȷ���Ƿ�ɾ��
		if (confirmDialog == JOptionPane.YES_OPTION) { // �������ͬ��ɾ��
			Runnable runnable = new Runnable() { // �����߳�
				/**
				 * ɾ���ļ��ĵݹ鷽��
				 * 
				 * @param file
				 *            Ҫɾ�����ļ�����
				 */
				private void delFile(File file) {
					try {
						if (file.isFile()) { // ���ɾ�������ļ�
							boolean delete = file.delete(); // ����ɾ���ļ��ķ���
							if (!delete) {
								JOptionPane.showMessageDialog(localPanel, file
										.getAbsoluteFile()
										+ "�ļ��޷�ɾ����", "ɾ���ļ�",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
						} else if (file.isDirectory()) { // ���ɾ�������ļ���
							File[] listFiles = file.listFiles();// ��ȡ���ļ��е��ļ��б�
							if (listFiles.length > 0) {
								for (File subFile : listFiles) {
									delFile(subFile); // ���õݹ鷽��ɾ�����б�������ļ����ļ���
								}
							}
							boolean delete = file.delete();// ���ɾ�����ļ���
							if (!delete) { // ����ɹ�ɾ��
								JOptionPane.showMessageDialog(localPanel, file
										.getAbsoluteFile()
										+ "�ļ����޷�ɾ����", "ɾ���ļ�",
										JOptionPane.ERROR_MESSAGE);
								return; // ���ط����ĵ��ô�
							}
						}
					} catch (Exception ex) {
						Logger.getLogger(LocalPanel.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				}

				/**
				 * �̵߳����巽��
				 * 
				 * @see java.lang.Runnable#run()
				 */
				public void run() {
					File parent = null;
					// ��������ѡ������
					for (int i = 0; i < selRows.length; i++) {
						// ��ȡÿ��ѡ���еĵ�һ�е�Ԫ����
						Object value = DelFileAction.this.localPanel.localDiskTable
								.getValueAt(selRows[i], 0);
						// ��������ݲ���DiskFile���ʵ������
						if (!(value instanceof DiskFile))
							continue; // ��������ѭ��
						DiskFile file = (DiskFile) value;
						if (parent == null)
							parent = file.getParentFile(); // ��ȡѡ���ļ����ϼ��ļ���
						if (file != null) {
							delFile(file); // ���õݹ鷽��ɾ��ѡ������
						}
					}
					// ����refreshFolder����ˢ�µ�ǰ�ļ���
					DelFileAction.this.localPanel.refreshFolder(parent);
					JOptionPane.showMessageDialog(localPanel, "ɾ���ɹ���");
				}
			};
			new Thread(runnable).start(); // ��������������߳�
		}
	}
}