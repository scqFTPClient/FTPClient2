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
import com.lzw.ftp.extClass.MyFTPClient;
import com.lzw.ftp.extClass.FtpFile;
import com.lzw.ftp.panel.FTPTableCellRanderer;

public class FtpPanel extends javax.swing.JPanel {

	MyFTPClient ftpClient;
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
				"新建文件夹", null));
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

		createFolderButton.setText("新建文件夹");
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

	public synchronized void listFtpFiles(final TelnetInputStream list) {

		final DefaultTableModel model = (DefaultTableModel) ftpDiskTable
				.getModel();
		model.setRowCount(0);

		Runnable runnable = new Runnable() {
			public synchronized void run() {
				ftpDiskTable.clearSelection();
				try {
					String pwd = getPwd();
					model.addRow(new Object[] { new FtpFile(".", pwd, true),
							"", "" }); 
					model.addRow(new Object[] { new FtpFile("..", pwd, true),
							"", "" });
					BufferedReader br = new BufferedReader(
							new InputStreamReader(list));
					String data = null;
			
					while ((data = br.readLine()) != null) {
		
						FtpFile ftpFile = new FtpFile();

						String dateStr = data.substring(0, 17).trim();
						String sizeOrDir = data.substring(18, 39).trim();
						String fileName = data.substring(39, data.length())
								.trim();

						ftpFile.setLastDate(dateStr);
						ftpFile.setSize(sizeOrDir);
						ftpFile.setName(fileName);
						ftpFile.setPath(pwd);
	
						model.addRow(new Object[] { ftpFile, ftpFile.getSize(),
								dateStr });
					}
					br.close(); 
				} catch (IOException ex) {
					Logger.getLogger(FTP_Client_Frame.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		};
		if (SwingUtilities.isEventDispatchThread()) 
			runnable.run();
		else
			SwingUtilities.invokeLater(runnable);
	}


	public void setFtpClient(MyFTPClient ftpClient) {
		this.ftpClient = ftpClient;

		final Timer timer = new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					final MyFTPClient ftpClient = FtpPanel.this.ftpClient;
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

	public void refreshCurrentFolder() {
		try {
			TelnetInputStream list = ftpClient.list();
			listFtpFiles(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void startDownThread() {
		if (thread != null)
			thread.stopThread();
		thread = new DownThread(this);
		thread.start();
	}

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

	public void clearTable() {
		FtpTableModel model = (FtpTableModel) ftpDiskTable.getModel();
		model.setRowCount(0);
	}
}
