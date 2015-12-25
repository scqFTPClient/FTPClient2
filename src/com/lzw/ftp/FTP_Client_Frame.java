package com.lzw.ftp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
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

import com.lzw.ftp.extClass.MyFTPClient;
import com.lzw.ftp.extClass.SiteInfoBean;
import com.lzw.ftp.panel.ftp.FtpPanel;
import com.lzw.ftp.panel.local.LocalPanel;
import com.lzw.ftp.panel.manager.FtpSiteDialog;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;

public class FTP_Client_Frame extends javax.swing.JFrame {
	MyFTPClient ftpClient;
	private JPasswordField PassField;
	private JButton cutLinkButton;
	FtpPanel ftpPanel;
	LocalPanel localPanel;
	private JTextField portTextField;
	private JTextField serverTextField;
	private JTextField userTextField;
	private JSplitPane jSplitPane1;
	private JButton linkButton;
	private final LinkToAction LINK_TO_ACTION; 
	private final CutLinkAction CUT_LINK_ACTION; 
	private JToggleButton shutdownButton;


	public FTP_Client_Frame() {
		LINK_TO_ACTION = new LinkToAction(this, "连接到", null);
		CUT_LINK_ACTION = new CutLinkAction(this, "断开", null);
		initComponents();
	}

	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		JPanel jPanel1 = new JPanel();
		JToolBar jToolBar1 = new JToolBar();
		JButton linkTo = new JButton();
		cutLinkButton = new JButton();
		JPanel loginPanel = new JPanel();
		JLabel ipLabel = new JLabel();
		serverTextField = new JTextField();
		JLabel usernameLabel = new JLabel();
		userTextField = new JTextField();
		JLabel passwordLabel = new JLabel();
		PassField = new JPasswordField();
		JLabel portLabel = new JLabel();
		portTextField = new JTextField();
		linkButton = new JButton();
		JSplitPane jSplitPane2 = new JSplitPane();
		jSplitPane1 = new JSplitPane();
		ftpPanel = new FtpPanel(this);
		localPanel = new LocalPanel(this); 

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

		setTitle("基于RSA与DEC的FTP客户端");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowOpened(java.awt.event.WindowEvent evt) {
//				formWindowOpened(evt);
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

		cutLinkButton.setText("断开连接");
		cutLinkButton.setEnabled(false);
		cutLinkButton.setFocusable(false);
		cutLinkButton.setAction(CUT_LINK_ACTION);
		jToolBar1.add(cutLinkButton);

		jPanel1.add(jToolBar1);

		shutdownButton = new JToggleButton();
		shutdownButton.setText("关闭");
		shutdownButton.setToolTipText("关闭");
		shutdownButton.setFocusable(false);
		jToolBar1.add(shutdownButton);

		loginPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		loginPanel.setLayout(new javax.swing.BoxLayout(loginPanel,
				javax.swing.BoxLayout.LINE_AXIS));

		ipLabel.setText("主机IP地址：");
		loginPanel.add(ipLabel);

		serverTextField.setText("192.168.1.128");
		serverTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				LinkFTPKeyPressed(evt);
			}
		});
		loginPanel.add(serverTextField);

		usernameLabel.setText("用户名:");
		loginPanel.add(usernameLabel);

		userTextField.setText("mr");
		userTextField.setMaximumSize(new java.awt.Dimension(200, 2147483647));
		userTextField.setPreferredSize(new java.awt.Dimension(100, 21));
		userTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				LinkFTPKeyPressed(evt);
			}
		});
		loginPanel.add(userTextField);

		passwordLabel.setText("密码：");
		loginPanel.add(passwordLabel);

		PassField.setText("mrsoft");
		PassField.setMaximumSize(new java.awt.Dimension(200, 2147483647));
		PassField.setPreferredSize(new java.awt.Dimension(100, 21));
		PassField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				LinkFTPKeyPressed(evt);
			}
		});
		loginPanel.add(PassField);

		portLabel.setText("端口：");
		loginPanel.add(portLabel);

		portTextField.setText("21");
		portTextField.setMaximumSize(new java.awt.Dimension(100, 2147483647));
		portTextField.setPreferredSize(new java.awt.Dimension(50, 21));
		portTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				LinkFTPKeyPressed(evt);
			}
		});
		loginPanel.add(portTextField);

		linkButton.setText("连接");
		linkButton.setFocusable(false);
		linkButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		linkButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		linkButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				linkButtonActionPerformed(evt);
			}
		});
		loginPanel.add(linkButton);

		
		
		
		
		jPanel1.add(loginPanel);

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


		jSplitPane2.setBottomComponent(jTabbedPane1);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		getContentPane().add(jSplitPane2, gridBagConstraints);

		fileMenu.setMnemonic('f');
		fileMenu.setText("文件(F)");

		ftpManageMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S,
				java.awt.event.InputEvent.CTRL_MASK));
		ftpManageMenuItem.setText("FTP站点管理(S)");
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
		linkToMenuItem.setText("连接中...(C)");
		linkToMenuItem.setAction(LINK_TO_ACTION);
		fileMenu.add(linkToMenuItem);

		cutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_Z,
				java.awt.event.InputEvent.CTRL_MASK));
		cutMenuItem.setText("断开(Z)");
		cutMenuItem.setAction(CUT_LINK_ACTION);
		fileMenu.add(cutMenuItem);
		fileMenu.add(jSeparator2);

		exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_X,
				java.awt.event.InputEvent.CTRL_MASK));
		exitMenuItem.setText("推出(X)");
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
		localMenu.setText("本地(L)");

		uploadMenuItem.setMnemonic('U');
		uploadMenuItem.setText("上传(U)");
		uploadMenuItem.setAction(localPanel.getActionMap().get("uploadAction"));

		localMenu.add(uploadMenuItem);
		localMenu.add(jSeparator3);

		createFolderMenuItem.setMnemonic('C');
		createFolderMenuItem.setText("新建文件夹(C)");
		createFolderMenuItem.setAction(localPanel.getActionMap().get(
				"createFolderAction"));
		localMenu.add(createFolderMenuItem);

		renameMenuItem.setMnemonic('R');
		renameMenuItem.setText("重命名文件夹(R)");
		renameMenuItem.setAction(localPanel.getActionMap().get("renameAction"));
		localMenu.add(renameMenuItem);

		delMenuItem.setMnemonic('D');
		delMenuItem.setText("删除(D)");
		delMenuItem.setAction(localPanel.getActionMap().get("delAction"));
		localMenu.add(delMenuItem);

		JMenuItem localrefreshMenuItem = new JMenuItem();
		localrefreshMenuItem.setMnemonic('R');
		localrefreshMenuItem.setText("本地刷新(R)");
		localrefreshMenuItem.setAction(localPanel.getActionMap().get(
				"refreshAction"));
		localMenu.add(localrefreshMenuItem);

		MenuBar.add(localMenu);

		ftpMenu.setMnemonic('r');
		ftpMenu.setText("远程(R)");

		downMenuItem.setMnemonic('U');
		downMenuItem.setText("下载(U)");
		downMenuItem.setAction(ftpPanel.getActionMap().get("downAction"));
		ftpMenu.add(downMenuItem);
		ftpMenu.add(jSeparator6);

		ftpDelMenuItem.setMnemonic('D');
		ftpDelMenuItem.setText("删除(D)");
		ftpDelMenuItem.setAction(ftpPanel.getActionMap().get("delAction"));
		ftpMenu.add(ftpDelMenuItem);

		ftpRenameMenuItem.setMnemonic('R');
		ftpRenameMenuItem.setText("重命名(R)");
		ftpRenameMenuItem
				.setAction(ftpPanel.getActionMap().get("renameAction"));
		ftpMenu.add(ftpRenameMenuItem);

		newFolderMenuItem.setMnemonic('C');
		newFolderMenuItem.setText("新建文件夹(C)");
		newFolderMenuItem.setAction(ftpPanel.getActionMap().get(
				"createFolderAction"));
		ftpMenu.add(newFolderMenuItem);

		JMenuItem refreshMenuItem = new JMenuItem();
		refreshMenuItem.setMnemonic('R');
		refreshMenuItem.setText("刷新(R)");
		refreshMenuItem.setAction(ftpPanel.getActionMap().get("refreshAction"));
		ftpMenu.add(refreshMenuItem);

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		setBounds((screenSize.width - 800) / 2, (screenSize.height - 600) / 2,
				800, 700);
	}

	public JToggleButton getShutdownButton() {
		return shutdownButton;
	}

//	private void formWindowOpened(java.awt.event.WindowEvent evt) {
//		jSplitPane1.setDividerLocation(0.50);
//		localPanel.getLocalDiskComboBox().setSelectedIndex(1);
//		localPanel.getLocalDiskComboBox().setSelectedIndex(0);
//	}

	private void formComponentResized(java.awt.event.ComponentEvent evt) {
		jSplitPane1.setDividerLocation(0.50);
	}

	
	
	//连接按钮触发
	private void linkButtonActionPerformed(java.awt.event.ActionEvent evt){
		try {
			String server = serverTextField.getText(); 
			if (server == null) {
				return;
			}
			String portStr = portTextField.getText();
			if (portStr == null) {
				portStr = "21";
			}
			int port = Integer.parseInt(portStr.trim());
			String userStr = userTextField.getText(); 
			userStr = userStr == null ? "" : userStr.trim();
			String passStr = PassField.getText();
			passStr = passStr == null ? "" : passStr.trim();
			cutLinkButton.doClick();
			ftpClient = new MyFTPClient();
			ftpClient.openServer(server.trim(), port); 
			ftpClient.login(userStr, passStr);
			ftpClient.binary(); 
			if (ftpClient.serverIsOpen()) { 
				CUT_LINK_ACTION.setEnabled(true);
			} else {
				CUT_LINK_ACTION.setEnabled(false); 
				return;
			}

			localPanel.setFtpClient(server, port, userStr, passStr);

			localPanel.getActionMap().get("uploadAction").setEnabled(true);
			ftpPanel.setFtpClient(ftpClient);

			ftpPanel.getActionMap().get("downAction").setEnabled(true);
			ftpPanel.refreshCurrentFolder();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
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

	public MyFTPClient getFtpClient() {
		return ftpClient;
	}

	public void setLinkInfo(SiteInfoBean bean) {
		serverTextField.setText(bean.getServer());
		portTextField.setText(bean.getPort() + "");
		userTextField.setText(bean.getUserName());
		PassField.setText("");
		PassField.requestFocus();
	}
}
