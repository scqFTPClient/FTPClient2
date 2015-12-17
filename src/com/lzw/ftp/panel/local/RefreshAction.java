/**
 * 
 */
package com.lzw.ftp.panel.local;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * @author Li Zhong Wei ˢ�±�����Դ�б�Ķ���������
 */
class RefreshAction extends AbstractAction {
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
	public RefreshAction(LocalPanel localPanel, String name, Icon icon) {
		super(name, icon); // ִ�и���Ĺ��췽��
		this.localPanel = localPanel; // ��ֵ������Դ������������
	}

	/**
	 * �������¼�������
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.localPanel.refreshCurrentFolder(); // ���ù�������ˢ�·���
	}
}