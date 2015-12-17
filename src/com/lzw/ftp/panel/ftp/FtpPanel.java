/*
 * FtpPanel.java
 *
 * Created on 2008年6月20日, 下午3:02
 */

package com.lzw.ftp.panel.ftp;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ActionMap;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableStringConverter;

import sun.net.TelnetInputStream;

import com.lzw.ftp.FTP_Client_Frame;
import com.lzw.ftp.extClass.FtpClient;
import com.lzw.ftp.extClass.FtpFile;
import com.lzw.ftp.panel.FTPTableCellRanderer;

public class FtpPanel extends javax.swing.JPanel {

	FtpClient ftpClient;
	private javax.swing.JButton createFolderButton;
	private javax.swing.JButton delButton;
	private javax.swing.JButton downButton;
	javax.swing.JTable ftpDiskTable;
	private javax.swing.JLabel ftpSelFilePathLabel;
	private javax.swing.JScrollPane scrollPane;
	private javax.swing.JToolBar toolBar;
	private javax.swing.JButton refreshButton;
	private javax.swing.JButton renameButton;
	FTP_Client_Frame frame = null;
	Queue<Object[]> queue = new LinkedList<Object[]>();
	private DownThread thread;

	public FtpPanel() {
		initComponents();
	}

	public FtpPanel(FTP_Client_Frame client_Frame) {
		frame = client_Frame;
		initComponents();
	}

	private void initComponents() {
		ActionMap actionMap = getActionMap();
		actionMap.put("createFolderAction", new CreateFolderAction(this,
				"创建文件夹", null));
		actionMap.put("delAction", new DelFileAction(this, "删除", null));
		actionMap.put("refreshAction", new RefreshAction(this, "刷新", null));
		actionMap.put("renameAction", new RenameAction(this, "重命名", null));
		actionMap.put("downAction", new DownAction(this, "下载", null));

		java.awt.GridBagConstraints gridBagConstraints;

		toolBar = new javax.swing.JToolBar();
		delButton = new javax.swing.JButton();
		renameButton = new javax.swing.JButton();
		createFolderButton = new javax.swing.JButton();
		downButton = new javax.swing.JButton();
		refreshButton = new javax.swing.JButton();
		scrollPane = new JScrollPane();
		ftpDiskTable = new JTable();
		ftpDiskTable.setDragEnabled(true);
		ftpSelFilePathLabel = new javax.swing.JLabel();

		setBorder(javax.swing.BorderFactory.createTitledBorder(null, "远程",
				javax.swing.border.TitledBorder.CENTER,
				javax.swing.border.TitledBorder.ABOVE_TOP));
		setLayout(new java.awt.GridBagLayout());

		toolBar.setRollover(true);
		toolBar.setFloatable(false);

		delButton.setText("删除");
		delButton.setFocusable(false);
		delButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		delButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		delButton.setAction(actionMap.get("delAction"));
		toolBar.add(delButton);

		renameButton.setText("重命名");
		renameButton.setFocusable(false);
		renameButton.setAction(actionMap.get("renameAction"));
		toolBar.add(renameButton);

		createFolderButton.setText("新文件夹");
		createFolderButton.setFocusable(false);
		createFolderButton.setAction(actionMap.get("createFolderAction"));
		toolBar.add(createFolderButton);

		downButton.setText("下载");
		downButton.setFocusable(false);
		downButton.setAction(actionMap.get("downAction"));
		toolBar.add(downButton);

		refreshButton.setText("刷新");
		refreshButton.setFocusable(false);
		refreshButton.setAction(actionMap.get("refreshAction"));
		toolBar.add(refreshButton);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		add(toolBar, gridBagConstraints);

		ftpDiskTable.setModel(new FtpTableModel());
		ftpDiskTable.setShowHorizontalLines(false);
		ftpDiskTable.setShowVerticalLines(false);
		ftpDiskTable.getTableHeader().setReorderingAllowed(false);
		ftpDiskTable.setDoubleBuffered(true);
		ftpDiskTable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				ftpDiskTableMouseClicked(evt);
			}
		});
		scrollPane.setViewportView(ftpDiskTable);
		scrollPane.getViewport().setBackground(Color.WHITE);
		ftpDiskTable.getColumnModel().getColumn(0).setCellRenderer(
				FTPTableCellRanderer.getCellRanderer());
		TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(
				ftpDiskTable.getModel());
		TableStringConverter converter = new TableConverter();
		sorter.setStringConverter(converter);
		ftpDiskTable.setRowSorter(sorter);
		sorter.toggleSortOrder(0);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		add(scrollPane, gridBagConstraints);

		ftpSelFilePathLabel.setBorder(javax.swing.BorderFactory
				.createEtchedBorder());
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		add(ftpSelFilePathLabel, gridBagConstraints);
	}

	/**
	 * 表格单击或双击事件的处理方法。
	 */
	private void ftpDiskTableMouseClicked(java.awt.event.MouseEvent evt) {
		int selectedRow = ftpDiskTable.getSelectedRow();
		Object value = ftpDiskTable.getValueAt(selectedRow, 0);
		if (value instanceof FtpFile) {
			FtpFile selFile = (FtpFile) value;
			ftpSelFilePathLabel.setText(selFile.getAbsolutePath());
			if (evt.getClickCount() >= 2) {
				if (selFile.isDirectory()) {
					try {
						ftpClient.cd(selFile.getAbsolutePath());
						listFtpFiles(ftpClient.list());
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * 读取FTP文件到表格的方法
	 * 
	 * @param list
	 *            读取FTP服务器资源列表的输入流
	 */
	public synchronized void listFtpFiles(final TelnetInputStream list) {
		// 获取表格的数据模型
		final DefaultTableModel model = (DefaultTableModel) ftpDiskTable
				.getModel();
		model.setRowCount(0);
		// 创建一个线程类
		Runnable runnable = new Runnable() {
			public synchronized void run() {
				ftpDiskTable.clearSelection();
				try {
					String pwd = getPwd(); // 获取FTP服务器的当前文件夹
					model.addRow(new Object[] { new FtpFile(".", pwd, true),
							"", "" }); // 添加“.”符号
					model.addRow(new Object[] { new FtpFile("..", pwd, true),
							"", "" }); // 添加“..”符号
					BufferedReader br = new BufferedReader(
							new InputStreamReader(list)); // 创建字符输入流
					String data = null;
					// 读取输入流中的文件目录
					while ((data = br.readLine()) != null) {
						// 创建FTP文件对象
						FtpFile ftpFile = new FtpFile();
						// 获取FTP服务器目录信息
						String dateStr = data.substring(0, 17).trim();
						String sizeOrDir = data.substring(18, 39).trim();
						String fileName = data.substring(39, data.length())
								.trim();
						// 将FTP目录信息初始化到FTP文件对象中
						ftpFile.setLastDate(dateStr);
						ftpFile.setSize(sizeOrDir);
						ftpFile.setName(fileName);
						ftpFile.setPath(pwd);
						// 将文件信息添加到表格中
						model.addRow(new Object[] { ftpFile, ftpFile.getSize(),
								dateStr });
					}
					br.close(); // 关闭输入流
				} catch (IOException ex) {
					Logger.getLogger(FTP_Client_Frame.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		};
		if (SwingUtilities.isEventDispatchThread()) // 启动线程对象
			runnable.run();
		else
			SwingUtilities.invokeLater(runnable);
	}

	/**
	 * 设置FTP连接，并启动下载队列线程的方法
	 */
	public void setFtpClient(FtpClient ftpClient) {
		this.ftpClient = ftpClient;
		// 以30秒为间隔与服务器保持通讯
		final Timer timer = new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					final FtpClient ftpClient = FtpPanel.this.ftpClient;
					if (ftpClient != null && ftpClient.serverIsOpen()) {
						ftpClient.noop();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		timer.start();
		startDownThread();
	}

	/**
	 * 刷新FTP资源管理面板的当前文件夹
	 */
	public void refreshCurrentFolder() {
		try {
			TelnetInputStream list = ftpClient.list(); // 获取服务器文件列表
			listFtpFiles(list); // 调用解析方法
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开始下载队列线程
	 */
	private void startDownThread() {
		if (thread != null)
			thread.stopThread();
		thread = new DownThread(this);
		thread.start();
	}

	/**
	 * 停止下载队列线程
	 */
	public void stopDownThread() {
		if (thread != null) {
			thread.stopThread();
			ftpClient = null;
		}
	}

	public String getPwd() {
		String pwd = null;
		try {
			pwd = ftpClient.pwd();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pwd;
	}

	public Queue<Object[]> getQueue() {
		return queue;
	}

	/**
	 * 清除FTP资源表格内容的方法
	 */
	public void clearTable() {
		FtpTableModel model = (FtpTableModel) ftpDiskTable.getModel();
		model.setRowCount(0);
	}
}
