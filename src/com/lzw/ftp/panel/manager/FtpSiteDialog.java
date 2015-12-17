package com.lzw.ftp.panel.manager;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
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
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.BevelBorder;

import com.lzw.ftp.FTP_Client_Frame;
import com.lzw.ftp.extClass.SiteInfoBean;

/**
 * @author lzwJava
 * 
 */
public class FtpSiteDialog extends JDialog implements ActionListener {
	private Properties siteInfo = new Properties(); // վ�����Զ���
	private JList list; // ��ʾվ����б����
	private FTP_Client_Frame frame; // ����������ö���
	private static final File FILE = new File("data/siteInfo.data");// �洢���Ե��ļ�����

	public FtpSiteDialog() {
		super();
		initComponents();
	}

	public FtpSiteDialog(FTP_Client_Frame frame) {
		super(frame);
		this.frame = frame;
		initComponents();
	}

	/**
	 * ��ʼ���������ķ���
	 */
	public void initComponents() {
		loadSiteProperties();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setTitle("FTPվ�����");
		BorderLayout borderLayout = new BorderLayout();
		borderLayout.setHgap(5);
		setLayout(borderLayout);

		list = new JList();
		final BevelBorder bevelBorder = new BevelBorder(BevelBorder.LOWERED);
		list.setBorder(bevelBorder);
		loadSiteList(); // װ��վ������
		JScrollPane scrollPanel = new JScrollPane(list);
		add(scrollPanel, CENTER);

		JPanel controlPanel = new JPanel();
		BoxLayout boxLayout = new BoxLayout(controlPanel, BoxLayout.Y_AXIS);
		controlPanel.setLayout(boxLayout);
		JButton addButton = new JButton("���");
		addButton.setActionCommand("add");
		addButton.addActionListener(this);
		JButton editButton = new JButton("�༭");
		editButton.setActionCommand("edit");
		editButton.addActionListener(this);
		JButton delButton = new JButton("ɾ��");
		delButton.setActionCommand("del");
		delButton.addActionListener(this);
		JButton linkButton = new JButton("����");
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

	/**
	 * װ��վ�����ݵķ���
	 */
	public void loadSiteList() {
		Enumeration<Object> keys = siteInfo.keys(); // ��ȡ���Լ��ϵļ�ֵ����
		DefaultListModel model = new DefaultListModel(); // �����б��������ģ��
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement(); // ��ȡÿ����ֵ
			String value = siteInfo.getProperty(key); // ��ȡÿ����ֵ������
			// ʹ�ü�ֵ�����ݴ���վ����ϢBean
			SiteInfoBean siteInfoBean = new SiteInfoBean(key, value);
			model.addElement(siteInfoBean); // ��վ����Ϣ������ӵ��б������ģ����
		}
		list.setModel(model); // �����б����ʹ�ô�����ģ��
	}

	/**
	 * װ��վ�������ļ��ķ���
	 */
	private void loadSiteProperties() {
		try {
			if (!FILE.exists()) { // ��������ļ�������
				FILE.getParentFile().mkdirs(); // ���������ļ����ļ���
				FILE.createNewFile(); // �����µ������ļ�
			}
			InputStream is = new FileInputStream(FILE); // �����ļ�������
			siteInfo.load(is); // װ�������ļ�
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 4��ά����ť���¼�������
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand(); // ��ȡ������ά����ť
		if (command.equals("add")) { // �������Ӱ�ť
			SiteDialog dialog = new SiteDialog(this); // ��ʾվ��Ի���
		}
		// ��ȡ�б����ѡ���վ��JavaBean����
		SiteInfoBean bean = (SiteInfoBean) list.getSelectedValue();
		if (bean == null) { // ���վ�����ΪNULL
			return; // ��������ִ��
		}
		if (command.equals("link")) { // ��������������Ӱ�ť
			frame.setLinkInfo(bean); // ����setLinkInfo()����
			dispose(); // �ر�FTPվ�����Ի���
		}
		if (command.equals("edit")) { // ����Ǳ༭��ť
			SiteDialog dialog = new SiteDialog(this, bean); // ��ʾվ��Ի�����б༭
		}
		if (command.equals("del")) { // �����ɾ����ť
			delSite(bean); // ����delSite()����
		}
	}

	/**
	 * ���վ����Ϣ�ķ���
	 */
	public void addSite(SiteInfoBean bean) {
		siteInfo.setProperty(bean.getId() + "", bean.getSiteName() + ","
				+ bean.getServer() + "," + bean.getPort() + ","
				+ bean.getUserName());
		try {
			FileOutputStream out = new FileOutputStream(FILE);
			siteInfo.store(out, "FTPվ������");
			loadSiteList();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ɾ��FTPվ��ķ���
	 */
	public void delSite(SiteInfoBean bean) {
		// ��վ�����Լ��϶������Ƴ�ָ��ID��ŵ�վ������
		siteInfo.remove(bean.getId());
		try {
			// ��ȡվ�������ļ��������
			FileOutputStream out = new FileOutputStream(FILE);
			siteInfo.store(out, "FTPվ������"); // ����store�����洢վ������
			loadSiteList(); // ����װ��վ���б�
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
