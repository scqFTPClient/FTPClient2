package com.lzw.ftp.panel.queue;

import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.lzw.ftp.extClass.ProgressArg;
import com.lzw.ftp.panel.QueueTableCellRanderer;

public class UploadPanel extends JPanel {
	private JTable uploadTable = new JTable(); // ������
	private JScrollPane scrollPane = new JScrollPane();
	private DefaultTableModel model; // ��������ģ��

	/**
	 * ���췽�������ڳ�ʼ���������
	 */
	public UploadPanel() {
		CardLayout cardLayout = new CardLayout();
		setLayout(cardLayout);
		ProgressArg progressArg = new ProgressArg(-1, -1, -1);
		model = new DefaultTableModel(new Object[][] { new Object[] { "", "",
				"", "", progressArg } }, new String[] { "�ļ���", "��С", "Զ���ļ���",
				"����", "״̬" });
		uploadTable.setModel(model);
		uploadTable.getTableHeader().setReorderingAllowed(false);
		uploadTable.setRowSelectionAllowed(false);
		TableColumn column = uploadTable.getColumn("״̬");
		column.setCellRenderer(new QueueTableCellRanderer());
		scrollPane.setViewportView(uploadTable);
		cardLayout.layoutContainer(scrollPane);
		add(scrollPane, "queue");
	}

	/**
	 * ���ϴ����еı��������������ķ���
	 * 
	 * @param values
	 *            - ��ӵ�����һ�����ݵ��������
	 */
	public void addRow(final Object[] values) {
		Runnable runnable = new Runnable() {
			public void run() {
				model.insertRow(0, values); // ���������ģ���������
			}
		};
		if (SwingUtilities.isEventDispatchThread())
			runnable.run(); // ���¼�����ִ��
		else
			SwingUtilities.invokeLater(runnable); // �����¼����е���
	}
}
