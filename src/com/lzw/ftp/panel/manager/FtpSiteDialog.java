package com.lzw.ftp.panel.manager;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import com.lzw.ftp.FTP_Client_Frame;
import com.lzw.ftp.extClass.SiteInfoBean;

public class FtpSiteDialog extends JDialog implements ActionListener {
	private Properties siteInfo = new Properties();
	private JList list;
	private FTP_Client_Frame frame;
	private static final File FILE = new File("data/siteInfo.data");

	public FtpSiteDialog() {
		super();
		initComponents();
	}

	public FtpSiteDialog(FTP_Client_Frame frame) {
		super(frame);
		this.frame = frame;
		initComponents();
	}

	public void initComponents() {
		loadSiteProperties();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setTitle("FTP站点");
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHgap(5);
		setLayout(borderLayout);

		list = new JList();
		final BevelBorder bevelBorder = new BevelBorder(BevelBorder.LOWERED);
		list.setBorder(bevelBorder);
		loadSiteList();
		JScrollPane scrollPanel = new JScrollPane(list);
		add(scrollPanel, CENTER);

		JPanel controlPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(controlPanel, BoxLayout.Y_AXIS);
		controlPanel.setLayout(boxLayout);
		JButton addButton = new JButton("添加");
		addButton.setActionCommand("add");
		addButton.addActionListener(this);
		JButton editButton = new JButton("编辑");
		editButton.setActionCommand("edit");
		editButton.addActionListener(this);
		JButton delButton = new JButton("删除");
		delButton.setActionCommand("del");
		delButton.addActionListener(this);
		JButton linkButton = new JButton("连接");
		linkButton.setActionCommand("link");
		linkButton.addActionListener(this);
		controlPanel.add(linkButton);
		controlPanel.add(addButton);
		controlPanel.add(editButton);
		controlPanel.add(delButton);
		add(controlPanel, EAST);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - 200) / 2, (screenSize.height - 430) / 2,
				200, 430);
		setVisible(true);
	}

	public void loadSiteList() {
		Enumeration<Object> keys = siteInfo.keys();
		DefaultListModel model = new DefaultListModel();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			String value = siteInfo.getProperty(key);

			SiteInfoBean siteInfoBean = new SiteInfoBean(key, value);
			model.addElement(siteInfoBean);
		}
		list.setModel(model);
	}

	private void loadSiteProperties() {
		try {
			if (!FILE.exists()) {
				FILE.getParentFile().mkdirs();
				FILE.createNewFile(); 
			}
			InputStream is = new FileInputStream(FILE);
			siteInfo.load(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("add")) {
			SiteDialog dialog = new SiteDialog(this); 
		}

		SiteInfoBean bean = (SiteInfoBean) list.getSelectedValue();
		if (bean == null) {
			return;
		}
		if (command.equals("link")) {
			frame.setLinkInfo(bean);
			dispose();
		}
		if (command.equals("edit")) {
			SiteDialog dialog = new SiteDialog(this, bean);
		}
		if (command.equals("del")) {
			delSite(bean);
		}
	}

	public void addSite(SiteInfoBean bean) {
		siteInfo.setProperty(bean.getId() + "", bean.getSiteName() + ","
				+ bean.getServer() + "," + bean.getPort() + ","
				+ bean.getUserName());
		try {
			FileOutputStream out = new FileOutputStream(FILE);
			siteInfo.store(out, "FTP添加成功");
			loadSiteList();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void delSite(SiteInfoBean bean) {
		siteInfo.remove(bean.getId());
		try {
			FileOutputStream out = new FileOutputStream(FILE);
			siteInfo.store(out, "FTP删除成功");
			loadSiteList();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
