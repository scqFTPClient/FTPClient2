package com.lzw.ftp.panel.queue;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import com.lzw.ftp.FTP_Client_Frame;
import com.lzw.ftp.extClass.FtpClient;
import com.lzw.ftp.extClass.FtpFile;

/**
 * @author Li Zhong Wei ������п������
 */
public class QueuePanel extends JPanel implements ActionListener {
	private JTable queueTable = new JTable(); // ��ʾ������еı�����
	private JScrollPane scrollPane = new JScrollPane();
	private FTP_Client_Frame frame; // ����������ö���
	private String[] columns;
	private FtpClient ftpClient; // FTPЭ��Ŀ�����
	private Timer queueTimer; // ���еĶ�ʱ��
	private LinkedList<Object[]> localQueue; // ���������ϴ�����
	private LinkedList<Object[]> ftpQueue; // FTP�������ض���
	private JToggleButton stopButton;
	private boolean stop = false; // ���еĿ��Ʊ���

	/**
	 * Ĭ�ϵĹ��췽��
	 */
	public QueuePanel() {
		initComponent();
	}

	/**
	 * �Զ���Ĺ��췽��
	 * 
	 * @param frame
	 *            ������
	 */
	public QueuePanel(FTP_Client_Frame frame) {
		this.frame = frame;
		// ���������ȡ���������ϴ�����
		localQueue = (LinkedList<Object[]>) frame.getLocalPanel().getQueue();
		// ���������ȡFTP�������ض���
		ftpQueue = (LinkedList<Object[]>) frame.getFtpPanel().getQueue();
		initComponent(); // ��ʼ���������
		// ������ʱ����ÿ���1��ִ�ж���ˢ������
		queueTimer = new Timer(1000, new ActionListener() {
			/**
			 * ��ʱ�����¼�������
			 * 
			 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				if (localQueue.size() + ftpQueue.size() == queueTable
						.getRowCount()) // ������д�Сû�иı�
					return; // �����������������κβ���
				refreshQueue(); // ����ˢ����ʾ������Ϣ�ı������
			}
		});
	}

	private void initComponent() {
		BorderLayout cardLayout = new BorderLayout();
		setLayout(cardLayout);
		columns = new String[] { "��������", "����", "����", "ִ��״̬" };
		queueTable.setModel(new DefaultTableModel(new Object[][] {}, columns));
		queueTable.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(queueTable);
		cardLayout.layoutContainer(scrollPane);
		add(scrollPane, CENTER);

		JToolBar controlTool = new JToolBar(JToolBar.VERTICAL);
		controlTool.setRollover(true);
		controlTool.setFloatable(false);
		JButton upButton = new JButton("����");
		upButton.setActionCommand("up");
		upButton.addActionListener(this);
		JButton downButton = new JButton("����");
		downButton.setActionCommand("down");
		downButton.addActionListener(this);
		stopButton = new JToggleButton("��ͣ");
		stopButton.setActionCommand("stop&start");
		stopButton.addActionListener(this);
		JButton delButton = new JButton("ɾ��");
		delButton.setActionCommand("del");
		delButton.addActionListener(this);
		JButton clearButton = new JButton("���");
		clearButton.setActionCommand("clear");
		clearButton.addActionListener(this);
		controlTool.setLayout(new BoxLayout(controlTool, BoxLayout.Y_AXIS));
		controlTool.add(upButton);
		controlTool.add(downButton);
		controlTool.add(stopButton);
		controlTool.add(delButton);
		controlTool.add(clearButton);
		add(controlTool, EAST);
	}

	public void startQueue() {
		ftpClient = frame.getFtpClient();
		queueTimer.start();
	}

	/**
	 * �����ϰ�ť���¼�������
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		final String command = e.getActionCommand();
		if (command.equals("stop&start")) {// ������ͣ��ť�¼�
			if (stopButton.isSelected()) {
				stop = true;
				stopButton.setText("����");
			} else {
				stop = false;
				stopButton.setText("��ͣ");
			}
		}
		// �������ƺ����ư�ť�¼�
		if (command.equals("up") || command.equals("down")) {
			up_Down_Action(command); // ���ô������ƺ����ƶ����ķ���
		}
		if (command.equals("del")) {// ����ɾ����ť���¼�
			int row = queueTable.getSelectedRow(); // ��ȡ��ʾ���еı��ĵ�ǰѡ����
			if (row < 0)
				return;
			// ��ȡѡ���еĵ�һ�����Ԫֵ
			Object valueAt = queueTable.getValueAt(row, 0);
			// ���ѡ��������File��Ķ���
			if (valueAt instanceof File) {
				File file = (File) valueAt;
				int size = localQueue.size(); // ��ȡ�ϴ����д�С
				for (int i = 0; i < size; i++) { // �����ϴ�����
					if (localQueue.get(i)[0].equals(file)) {
						localQueue.remove(i); // ���ϴ�������ɾ���ļ�����
					}
				}
			} else if (valueAt instanceof String) { // ���ѡ������ַ�������
				String fileStr = (String) valueAt;
				int size = ftpQueue.size(); // ��ȡ�ϴ����еĴ�С
				for (int i = 0; i < size; i++) { // �����ϴ�����
					// ��ȡ�ϴ������е��ļ�����
					FtpFile ftpFile = (FtpFile) ftpQueue.get(i)[0];
					if (ftpFile.getAbsolutePath().equals(fileStr)) {
						ftpQueue.remove(i); // ���ϴ�������ɾ�����ļ�����
					}
				}
			}
			refreshQueue(); // ˢ�¶����б�
		}
		if (command.equals("clear")) { // ������հ�ť���¼�
			localQueue.clear(); // ���ñ������Ķ��е�clear()����
			ftpQueue.clear(); // ����FTP���Ķ��е�clear()����
		}
	}

	/**
	 * ������������ƺ����ƶ���������
	 * 
	 * @param command
	 *            ���ƻ���������
	 */
	private void up_Down_Action(final String command) {
		int row = queueTable.getSelectedRow(); // ��ȡ���б��ĵ�ǰѡ���к�
		if (row < 0)
			return;
		// ��ȡ��ǰѡ���еĵ�һ����Ԫֵ
		Object valueAt = queueTable.getValueAt(row, 0);
		// ��ȡ��ǰѡ���еĵڶ�����Ԫֵ��Ϊ�ж��ϴ������ط��������
		String orientation = (String) queueTable.getValueAt(row, 1);
		if (orientation.equals("�ϴ�")) { // ������ϴ�����
			String value = ((File) valueAt).getAbsolutePath();
			int size = localQueue.size();
			for (int i = 0; i < size; i++) { // �����ϴ�����
				Object[] que = localQueue.get(i);
				File file = (File) que[0];
				// �Ӷ����У�������ѡ����ϴ�������ļ�����
				if (file.getAbsolutePath().equals(value)) {
					ListSelectionModel selModel = queueTable
							.getSelectionModel(); // ��ȡ����ѡ��ģ��
					selModel // ����ѡ��ģ�͵ĵ�ѡģʽ
							.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					int dsize = localQueue.size(); // ��ȡ�����ϴ����еĴ�С
					int drow = queueTable.getSelectedRow();// ��ȡ���б��ĵ�ǰѡ���к�
					int queueVal = localQueue.size() - dsize;

					int next = -1;
					int selIndex = -1;
					if (command.equals("up")) {
						if (i < 1) // ����ѡ��Χ
							return;
						selIndex = drow - queueVal - 1;
						next = i - 1;
					} else {
						if (i >= size - 1) // ����ѡ��Χ
							return;
						selIndex = drow - queueVal + 1;
						next = i + 1;
					}
					// ������������λ�õ�����
					Object[] temp = localQueue.get(next);
					localQueue.set(next, que);
					localQueue.set(i, temp);
					refreshQueue(); // ˢ�¶��б����б�
					// ���ñ��ѡ���һ��
					selModel.setSelectionInterval(0, selIndex);
					break;
				}
			}
		} else if (orientation.equals("����")) { // �������������
			String value = (String) valueAt;
			int size = ftpQueue.size(); // ��ȡFTP���ض��еĴ�С
			for (int i = 0; i < size; i++) { // �������ض���
				Object[] que = ftpQueue.get(i);
				FtpFile file = (FtpFile) que[0]; // ��ȡÿ�����������FTP�ļ�����
				if (file.getAbsolutePath().equals(value)) {// 
					ListSelectionModel selModel = queueTable
							.getSelectionModel(); // ��ȡ������б���ѡ��ģ��
					// ����ģ��ʹ�õ�ѡģʽ
					selModel.setSelectionMode(SINGLE_SELECTION);
					int dsize = ftpQueue.size();
					int drow = queueTable.getSelectedRow();
					int queueVal = ftpQueue.size() - dsize;

					int next = -1;
					int selIndex = -1;
					if (command.equals("up")) {
						if (i < 1) // ����ѡ��Χ
							return;
						selIndex = drow - queueVal - 1;
						next = i - 1;
					} else {
						if (i >= size - 1) // ����ѡ��Χ
							return;
						selIndex = drow - queueVal + 1;
						next = i + 1;
					}
					// ������������λ�õ���������
					Object[] temp = ftpQueue.get(next);
					ftpQueue.set(next, que);
					ftpQueue.set(i, temp);
					refreshQueue(); // ˢ��������еı���б�
					// ѡ����ĵ�һ��
					selModel.setSelectionInterval(0, selIndex);
					break;
				}
			}
		}
	}

	/**
	 * ˢ�¶��еķ���
	 */
	private synchronized void refreshQueue() {
		// ����ػ���ť�����²����ϴ������صĶ��ж�������
		if (frame.getShutdownButton().isSelected() && localQueue.isEmpty()
				&& ftpQueue.isEmpty()) {
			try {
				// ִ��ϵͳ�ػ�����ӳ�30����
				Runtime.getRuntime().exec("shutdown -s -t 30");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// ������������ģ�Ͷ���
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		// ��ȡ�����ϴ������е�����
		Object[] localQueueArray = localQueue.toArray();
		// ���������ϴ�����
		for (int i = 0; i < localQueueArray.length; i++) {
			Object[] queueValue = (Object[]) localQueueArray[i];
			if (queueValue == null)
				continue;
			File localFile = (File) queueValue[0];
			// ���ϴ����е�������ӵ�������������ģ����
			model.addRow(new Object[] { localFile.getAbsoluteFile(), "�ϴ�",
					ftpClient.getServer(), i == 0 ? "�����ϴ�" : "�ȴ��ϴ�" });
		}
		// ��ȡ���ض��е�����
		Object[] ftpQueueArray = ftpQueue.toArray();
		// �������ض���
		for (int i = 0; i < ftpQueueArray.length; i++) {
			Object[] queueValue = (Object[]) ftpQueueArray[i];
			if (queueValue == null)
				continue;
			FtpFile ftpFile = (FtpFile) queueValue[0];
			// �����ض��е�������ӵ�������������ģ����
			model.addRow(new Object[] { ftpFile.getAbsolutePath(), "����",
					ftpClient.getServer(), i == 0 ? "��������" : "�ȴ�����" });
		}
		queueTable.setModel(model); // ���ñ��ʹ�ñ������ı������ģ��
	}

	public boolean isStop() {
		return stop;
	}
}
