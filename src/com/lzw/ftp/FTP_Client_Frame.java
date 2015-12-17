/*
 * FTP_Client_Frame.java
 *
 * Created on 2008��6��17��, ����3:36
 */
package com.lzw.ftp;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.lzw.ftp.extClass.FtpClient;
import com.lzw.ftp.extClass.SiteInfoBean;
import com.lzw.ftp.panel.ftp.FtpPanel;
import com.lzw.ftp.panel.local.LocalPanel;
import com.lzw.ftp.panel.manager.FtpSiteDialog;
import com.lzw.ftp.panel.queue.DownloadPanel;
import com.lzw.ftp.panel.queue.QueuePanel;
import com.lzw.ftp.panel.queue.UploadPanel;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * @author lzwJava
 */
public class FTP_Client_Frame extends javax.swing.JFrame {
	FtpClient ftpClient;
	private JPasswordField PassField;
	private JButton cutLinkButton;
	FtpPanel ftpPanel;
	LocalPanel localPanel;
	private JTextField portTextField;
	private JTextField serverTextField;
	private JTextField userTextField;
	private QueuePanel queuePanel;
	private UploadPanel uploadPanel;
	private DownloadPanel downloadPanel;
	private JSplitPane jSplitPane1;
	private JButton linkButton;
	private final LinkToAction LINK_TO_ACTION; // ���ӵ� ��ť�Ķ���������
	private final CutLinkAction CUT_LINK_ACTION; // �Ͽ� ��ť�Ķ���������
	private SystemTray systemTray;
	private JToggleButton shutdownButton;
	private final ImageIcon icon = new ImageIcon(getClass().getResource(
			"/com/lzw/ftp/res/trayIcon.png"));

	public FTP_Client_Frame() {
		LINK_TO_ACTION = new LinkToAction(this, "���ӵ�", null);
		CUT_LINK_ACTION = new CutLinkAction(this, "�Ͽ�", null);
		initComponents();
		initSystemTray();
	}

	/**
	 * ��ʼ��ϵͳ���̵ķ���
	 */
	private void initSystemTray() {
		if (SystemTray.isSupported())
			systemTray = SystemTray.getSystemTray();
		TrayIcon trayIcon = new TrayIcon(icon.getImage());
		PopupMenu popupMenu = new PopupMenu("���̲˵�");

		// ������ʾ������˵���
		MenuItem showMenuItem = new MenuItem("��ʾ������");
		showMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FTP_Client_Frame.this.setExtendedState(JFrame.NORMAL);
				FTP_Client_Frame.this.setVisible(true);
			}
		});

		// �����˳��˵���
		MenuItem exitMenuItem = new MenuItem("�˳�");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		popupMenu.add(showMenuItem);
		popupMenu.addSeparator();
		popupMenu.add(exitMenuItem);
		trayIcon.setPopupMenu(popupMenu);
		try {
			systemTray.add(trayIcon);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ʼ���������ķ���
	 */
	private void initComponents() {
		setIconImage(icon.getImage());
		java.awt.GridBagConstraints gridBagConstraints;

		JPanel jPanel1 = new JPanel();
		JToolBar jToolBar1 = new JToolBar();
		JButton linkTo = new JButton();
		cutLinkButton = new JButton();
		JPanel jPanel4 = new JPanel();
		JLabel jLabel1 = new JLabel();
		serverTextField = new JTextField();
		JLabel jLabel2 = new JLabel();
		userTextField = new JTextField();
		JLabel jLabel3 = new JLabel();
		PassField = new JPasswordField();
		JLabel jLabel6 = new JLabel();
		portTextField = new JTextField();
		linkButton = new JButton();
		JSplitPane jSplitPane2 = new JSplitPane();
		jSplitPane1 = new JSplitPane();
		ftpPanel = new FtpPanel(this); // ��ʼ��FTPԶ����Դ���
		localPanel = new LocalPanel(this); // ��ʼ��������Դ�������
		uploadPanel = new UploadPanel(); // ��ʼ���ϴ��������
		downloadPanel = new DownloadPanel(); // ��ʼ�����ض������
		queuePanel = new QueuePanel(this); // ��ʼ���������

		JTabbedPane jTabbedPane1 = new JTabbedPane();
		JMenuBar MenuBar = new JMenuBar();
		JMenu fileMenu = new JMenu();
		JMenuItem ftpManageMenuItem = new JMenuItem();
		JSeparator jSeparator1 = new JSeparator();
		JMenuItem linkToMenuItem = new javax.swing.JMenuItem();
		JMenuItem cutMenuItem = new javax.swing.JMenuItem();
		JSeparator jSeparator2 = new javax.swing.JSeparator();
		JMenuItem exitMenuItem = new javax.swing.JMenuItem();
		JMenuItem uploadMenuItem = new javax.swing.JMenuItem();
		JSeparator jSeparator3 = new javax.swing.JSeparator();
		JMenuItem createFolderMenuItem = new javax.swing.JMenuItem();
		JMenuItem renameMenuItem = new javax.swing.JMenuItem();
		JMenuItem delMenuItem = new javax.swing.JMenuItem();
		JMenu ftpMenu = new javax.swing.JMenu();
		JMenuItem downMenuItem = new javax.swing.JMenuItem();
		JSeparator jSeparator6 = new javax.swing.JSeparator();
		JMenuItem ftpDelMenuItem = new javax.swing.JMenuItem();
		JMenuItem ftpRenameMenuItem = new javax.swing.JMenuItem();
		JMenuItem newFolderMenuItem = new javax.swing.JMenuItem();
		JMenu helpMenu = new javax.swing.JMenu();
		JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
		JMenuItem bugMenuItem = new javax.swing.JMenuItem();

		setTitle("FTP�ϴ�����");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowOpened(java.awt.event.WindowEvent evt) {
				formWindowOpened(evt);
			}

			public void windowIconified(final WindowEvent e) {
				setVisible(false);
			}
		});
		addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentResized(java.awt.event.ComponentEvent evt) {
				formComponentResized(evt);
			}
		});
		getContentPane().setLayout(new java.awt.GridBagLayout());

		jPanel1.setLayout(new java.awt.GridLayout(0, 1));

		jToolBar1.setRollover(true);
		jToolBar1.setFloatable(false);

		linkTo.setText("���ӵ�");
		linkTo.setFocusable(false);
		linkTo.setAction(LINK_TO_ACTION);
		jToolBar1.add(linkTo);

		cutLinkButton.setText("�Ͽ�");
		cutLinkButton.setEnabled(false);
		cutLinkButton.setFocusable(false);
		cutLinkButton.setAction(CUT_LINK_ACTION);
		jToolBar1.add(cutLinkButton);

		jPanel1.add(jToolBar1);

		shutdownButton = new JToggleButton();
		shutdownButton.setText("�Զ��ػ�");
		shutdownButton.setToolTipText("������ɺ��Զ��رռ����");
		shutdownButton.setFocusable(false);
		jToolBar1.add(shutdownButton);

		jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4,
				javax.swing.BoxLayout.LINE_AXIS));

		jLabel1.setText("�����ַ��");
		jPanel4.add(jLabel1);

		serverTextField.setText("192.168.1.128");
		serverTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				LinkFTPKeyPressed(evt);
			}
		});
		jPanel4.add(serverTextField);

		jLabel2.setText("�û���");
		jPanel4.add(jLabel2);

		userTextField.setText("mr");
		userTextField.setMaximumSize(new java.awt.Dimension(200, 2147483647));
		userTextField.setPreferredSize(new java.awt.Dimension(100, 21));
		userTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				LinkFTPKeyPressed(evt);
			}
		});
		jPanel4.add(userTextField);

		jLabel3.setText("���룺");
		jPanel4.add(jLabel3);

		PassField.setText("mrsoft");
		PassField.setMaximumSize(new java.awt.Dimension(200, 2147483647));
		PassField.setPreferredSize(new java.awt.Dimension(100, 21));
		PassField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				LinkFTPKeyPressed(evt);
			}
		});
		jPanel4.add(PassField);

		jLabel6.setText("�˿ڣ�");
		jPanel4.add(jLabel6);

		portTextField.setText("21");
		portTextField.setMaximumSize(new java.awt.Dimension(100, 2147483647));
		portTextField.setPreferredSize(new java.awt.Dimension(50, 21));
		portTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				LinkFTPKeyPressed(evt);
			}
		});
		jPanel4.add(portTextField);

		linkButton.setText("����");
		linkButton.setFocusable(false);
		linkButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		linkButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		linkButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				linkButtonActionPerformed(evt);
			}
		});
		jPanel4.add(linkButton);

		jPanel1.add(jPanel4);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		getContentPane().add(jPanel1, gridBagConstraints);

		jSplitPane2.setBorder(null);
		jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
		jSplitPane2.setResizeWeight(1.0);
		jSplitPane2.setContinuousLayout(true);

		jSplitPane1.setDividerLocation(400);
		jSplitPane1.setDividerSize(10);
		jSplitPane1.setOneTouchExpandable(true);
		jSplitPane1.setRightComponent(ftpPanel);
		jSplitPane1.setLeftComponent(localPanel);

		jSplitPane2.setLeftComponent(jSplitPane1);

		jTabbedPane1.setMinimumSize(new java.awt.Dimension(40, 170));

		jTabbedPane1.addTab("����", queuePanel);// ��Ӷ������
		jTabbedPane1.addTab("���ϴ�", uploadPanel);// ����ϴ����
		jTabbedPane1.addTab("������", downloadPanel);// ����������

		jSplitPane2.setBottomComponent(jTabbedPane1);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		getContentPane().add(jSplitPane2, gridBagConstraints);

		fileMenu.setMnemonic('f');
		fileMenu.setText("�ļ�(F)");

		ftpManageMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S,
				java.awt.event.InputEvent.CTRL_MASK));
		ftpManageMenuItem.setText("FTPվ�����(S)");
		ftpManageMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("action");
				FtpSiteDialog dialog = new FtpSiteDialog(FTP_Client_Frame.this);
				dialog.setVisible(true);
			}
		});
		fileMenu.add(ftpManageMenuItem);
		fileMenu.add(jSeparator1);

		linkToMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_C,
				java.awt.event.InputEvent.CTRL_MASK));
		linkToMenuItem.setText("���ӵ�...(C)");
		linkToMenuItem.setAction(LINK_TO_ACTION);
		fileMenu.add(linkToMenuItem);

		cutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_Z,
				java.awt.event.InputEvent.CTRL_MASK));
		cutMenuItem.setText("�Ͽ�(Z)");
		cutMenuItem.setAction(CUT_LINK_ACTION);
		fileMenu.add(cutMenuItem);
		fileMenu.add(jSeparator2);

		exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_X,
				java.awt.event.InputEvent.CTRL_MASK));
		exitMenuItem.setText("�˳�(X)");
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);

		MenuBar.add(fileMenu);

		JMenu localMenu = new JMenu();
		localMenu.setMnemonic('l');
		localMenu.setText("����(L)");

		uploadMenuItem.setMnemonic('U');
		uploadMenuItem.setText("�ϴ�(U)");
		uploadMenuItem.setAction(localPanel.getActionMap().get("uploadAction"));

		localMenu.add(uploadMenuItem);
		localMenu.add(jSeparator3);

		createFolderMenuItem.setMnemonic('C');
		createFolderMenuItem.setText("�½��ļ���(C)");
		createFolderMenuItem.setAction(localPanel.getActionMap().get(
				"createFolderAction"));
		localMenu.add(createFolderMenuItem);

		renameMenuItem.setMnemonic('R');
		renameMenuItem.setText("������(R)");
		renameMenuItem.setAction(localPanel.getActionMap().get("renameAction"));
		localMenu.add(renameMenuItem);

		delMenuItem.setMnemonic('D');
		delMenuItem.setText("ɾ��(D)");
		delMenuItem.setAction(localPanel.getActionMap().get("delAction"));
		localMenu.add(delMenuItem);

		JMenuItem localrefreshMenuItem = new JMenuItem();
		localrefreshMenuItem.setMnemonic('R');
		localrefreshMenuItem.setText("ˢ��(R)");
		localrefreshMenuItem.setAction(localPanel.getActionMap().get(
				"refreshAction"));
		localMenu.add(localrefreshMenuItem);

		MenuBar.add(localMenu);

		ftpMenu.setMnemonic('r');
		ftpMenu.setText("Զ��(R)");

		downMenuItem.setMnemonic('U');
		downMenuItem.setText("����(U)");
		downMenuItem.setAction(ftpPanel.getActionMap().get("downAction"));
		ftpMenu.add(downMenuItem);
		ftpMenu.add(jSeparator6);

		ftpDelMenuItem.setMnemonic('D');
		ftpDelMenuItem.setText("ɾ��(D)");
		ftpDelMenuItem.setAction(ftpPanel.getActionMap().get("delAction"));
		ftpMenu.add(ftpDelMenuItem);

		ftpRenameMenuItem.setMnemonic('R');
		ftpRenameMenuItem.setText("������(R)");
		ftpRenameMenuItem
				.setAction(ftpPanel.getActionMap().get("renameAction"));
		ftpMenu.add(ftpRenameMenuItem);

		newFolderMenuItem.setMnemonic('C');
		newFolderMenuItem.setText("�½��ļ���(C)");
		newFolderMenuItem.setAction(ftpPanel.getActionMap().get(
				"createFolderAction"));
		ftpMenu.add(newFolderMenuItem);

		JMenuItem refreshMenuItem = new JMenuItem();
		refreshMenuItem.setMnemonic('R');
		refreshMenuItem.setText("ˢ��(R)");
		refreshMenuItem.setAction(ftpPanel.getActionMap().get("refreshAction"));
		ftpMenu.add(refreshMenuItem);

		MenuBar.add(ftpMenu);

		helpMenu.setText("����(H)");
		aboutMenuItem.setMnemonic('a');
		aboutMenuItem.setText("����(A)");
		aboutMenuItem.addActionListener(new AboutItemAction(this));
		helpMenu.add(aboutMenuItem);

		bugMenuItem.setMnemonic('u');
		bugMenuItem.setText("���󱨸�(U)");
		bugMenuItem.addActionListener(new BugItemAction());
		helpMenu.add(bugMenuItem);

		MenuBar.add(helpMenu);

		setJMenuBar(MenuBar);

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		setBounds((screenSize.width - 800) / 2, (screenSize.height - 600) / 2,
				800, 700);
	}

	public JToggleButton getShutdownButton() {
		return shutdownButton;
	}

	/**
	 * ����װ�ص��¼����?��
	 */
	private void formWindowOpened(java.awt.event.WindowEvent evt) {
		jSplitPane1.setDividerLocation(0.50);
		localPanel.getLocalDiskComboBox().setSelectedIndex(1);
		localPanel.getLocalDiskComboBox().setSelectedIndex(0);
	}

	/**
	 * �����С������¼����?��
	 */
	private void formComponentResized(java.awt.event.ComponentEvent evt) {
		jSplitPane1.setDividerLocation(0.50);
	}

	/**
	 * ���Ӱ�ť���¼����?��
	 */
	private void linkButtonActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			String server = serverTextField.getText(); // ��ȡ��������ַ
			if (server == null) {
				return;
			}
			String portStr = portTextField.getText(); // ��ȡ�˿ں�
			if (portStr == null) {
				portStr = "21";
			}
			int port = Integer.parseInt(portStr.trim());
			String userStr = userTextField.getText(); // ��ȡ�û���
			userStr = userStr == null ? "" : userStr.trim();
			String passStr = PassField.getText(); // ��ȡ����
			passStr = passStr == null ? "" : passStr.trim();
			cutLinkButton.doClick();
			ftpClient = new FtpClient();
			ftpClient.openServer(server.trim(), port); // ���ӷ�����
			ftpClient.login(userStr, passStr); // ��¼������
			ftpClient.binary(); // ʹ�ö����ƴ���ģʽ
			if (ftpClient.serverIsOpen()) { // ������ӳɹ�
				CUT_LINK_ACTION.setEnabled(true); // ���öϿ���ť����
			} else { // ����
				CUT_LINK_ACTION.setEnabled(false); // ���öϿ���ť������
				return; // �������¼�����
			}
			// ���ñ�����Դ��������FTP������Ϣ
			localPanel.setFtpClient(server, port, userStr, passStr);
			// �����ϴ���ť����
			localPanel.getActionMap().get("uploadAction").setEnabled(true);
			ftpPanel.setFtpClient(ftpClient);// ����FTP��Դ��������FTP������Ϣ
			// �������ذ�ť����
			ftpPanel.getActionMap().get("downAction").setEnabled(true);
			ftpPanel.refreshCurrentFolder();// ˢ��FTP��Դ�������ĵ�ǰ�ļ���
			queuePanel.startQueue(); // ������������߳�
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * ����FTP��ص��ı��� �������Ļس��¼�
	 */
	private void LinkFTPKeyPressed(java.awt.event.KeyEvent evt) {
		if (evt.getKeyChar() == '\n') {
			linkButton.doClick();
		}
	}

	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(new NimbusLookAndFeel());
					FTP_Client_Frame client_Frame = new FTP_Client_Frame();
					client_Frame.setVisible(true);
				} catch (Exception ex) {
					Logger.getLogger(FTP_Client_Frame.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		});
	}

	public LocalPanel getLocalPanel() {
		return localPanel;
	}

	public FtpPanel getFtpPanel() {
		return ftpPanel;
	}

	public QueuePanel getQueuePanel() {
		return queuePanel;
	}

	public UploadPanel getUploadPanel() {
		return uploadPanel;
	}

	public DownloadPanel getDownloadPanel() {
		return downloadPanel;
	}

	public FtpClient getFtpClient() {
		return ftpClient;
	}

	/**
	 * ����FTP������Ϣ�ķ�������FTPվ�����������
	 */
	public void setLinkInfo(SiteInfoBean bean) {
		serverTextField.setText(bean.getServer()); // ���������ַ
		portTextField.setText(bean.getPort() + ""); // ���ö˿ں�
		userTextField.setText(bean.getUserName()); // �����û���
		PassField.setText("");
		PassField.requestFocus();
	}
}
