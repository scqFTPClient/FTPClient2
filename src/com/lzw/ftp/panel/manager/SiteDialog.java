package com.lzw.ftp.panel.manager;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.lzw.ftp.extClass.SiteInfoBean;

/**
 * @author Li Zhong Wei ����ά��FTPվ��ĶԻ���
 */
public class SiteDialog extends JDialog implements ActionListener {

	private FtpSiteDialog dialog; // ���游��������ö���
	private JTextField siteNameField; // վ�������ı������
	private JTextField siteAddressField;// վ���ַ�ı������
	private JTextField portField; // �˿ں��ı������
	private JTextField loginUserField; // ��¼�û��ı������
	private SiteInfoBean siteBean; // վ����Ϣ��JavaBean����

	/**
	 * �������FTPվ��Ի���
	 * 
	 * @param frame
	 *            �Ի���ĸ�����
	 */
	public SiteDialog(FtpSiteDialog frame) {
		super(frame); // ���ø���Ĺ��췽��
		dialog = frame; // ��ֵ���������
		initComponents(); // ���ó�ʼ���Ի������ķ���
	}

	/**
	 * �����༭FTPվ��Ի���
	 * 
	 * @param frame
	 *            �����������
	 * @param siteBean
	 *            FTPվ����Ϣ��JavaBean
	 */
	public SiteDialog(FtpSiteDialog frame, SiteInfoBean siteBean) {
		this(frame);
		dialog = frame; // ��ֵ���������ö���
		this.siteBean = siteBean; // ��ֵFTPվ�����ö���
		initInput(); // ��ʼ���������������
		setTitle("�༭FTPվ��"); // ���öԻ���ı���
	}

	/**
	 * �����༭FTPվ��Ի���
	 * 
	 * @param frame
	 *            �����������
	 * @param bean
	 *            FTPվ����Ϣ��JavaBean
	 */
	public SiteDialog(FtpLinkDialog frame, SiteInfoBean bean) {
		super(frame); // ���ø��๹�췽��
		this.siteBean = bean; // ��ֵFTPվ����Ϣ������
		initComponents(); // ���ó�ʼ���������ķ���
		initInput(); // ��ʼ�����������ı������������
		setReadOnly(); // �������ý������ֻ���ķ���
		setTitle("�鿴FTPվ��"); // ���öԻ���ı���
	}

	/**
	 * ���ô��������ı������ֻ���ķ���
	 */
	private void setReadOnly() {
		siteNameField.setEditable(false); // ����վ�������ı���ֻ��
		siteAddressField.setEditable(false); // ����FTP��ַֻ��
		loginUserField.setEditable(false); // ���õ�¼�û���ֻ��
		portField.setEditable(false); // ���ö˿ں��ı���ֻ��
	}

	/**
	 * ��ʼ���������ķ���
	 */
	public void initComponents() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setTitle("���FTPվ��");
		JPanel content = new JPanel();
		content.setLayout(new GridLayout(0, 1, 4, 3));

		content.add(new JLabel("վ�����ƣ�"));
		siteNameField = new JTextField();
		content.add(siteNameField);
		content.add(new JLabel("վ���ַ��"));
		siteAddressField = new JTextField();
		content.add(siteAddressField);
		content.add(new JLabel("�˿ںţ�"));
		portField = new JTextField();
		portField.setText("21");
		content.add(portField);
		content.add(new JLabel("��¼�û���"));
		loginUserField = new JTextField();
		content.add(loginUserField);

		JPanel panel = new JPanel();
		final FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
		layout.setHgap(20);
		panel.setLayout(layout);
		final Insets insets = new Insets(0, 8, 0, 8);
		JButton okButton = new JButton("ȷ��");
		okButton.setActionCommand("ok");
		okButton.addActionListener(this);
		JButton cancelButton = new JButton("����");
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);
		cancelButton.setMargin(insets);
		okButton.setMargin(insets);
		panel.add(okButton);
		panel.add(cancelButton);
		content.add(panel);
		content.setBorder(new EmptyBorder(4, 6, 4, 6));
		add(content);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - 200) / 2, (screenSize.height - 400) / 2,
				317, 383);
		setVisible(true);
	}

	/**
	 * ���水ť���¼�������
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand(); // ��ȡ��ť��command����
		if (command.equals("ok")) { // �����ȷ����ť
			try {
				if (dialog == null) {
					dispose();
					return;
				}
				// ��ȡ���������ı��������
				String siteName = siteNameField.getText().trim();
				String server = siteAddressField.getText().trim();
				String userName = loginUserField.getText().trim();
				String portStr = portField.getText().trim();
				// �ж��Ƿ���д��ȫ���ı���
				if (siteName.isEmpty() || server.isEmpty()
						|| userName.isEmpty() || portStr.isEmpty()) {
					JOptionPane.showMessageDialog(this, "����дȫ����Ϣ");
					return;
				}
				int port = Integer.valueOf(portStr);
				// ����FTPվ����Ϣ��JavaBean����
				SiteInfoBean bean = new SiteInfoBean(siteName, server, port,
						userName);
				// ����Ի����siteBean��Ϊ��
				if (siteBean != null)
					bean.setId(siteBean.getId()); // ����FTPվ���ID���
				dialog.addSite(bean); // ���ø������ addSite�������վ��
				dialog.loadSiteList(); // ���ø������loadSiteList��������վ���б�
				dispose();
			} catch (NullPointerException ex) {
				ex.printStackTrace();
				return;
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "����ȷ��д�˿ں���Ϣ");
				ex.printStackTrace();
				return;
			}
		}
		if (command.equals("cancel")) { // ��������ð�ť
			if (siteBean == null) // ����Ի����siteBean����Ϊ��
				clearInput(); // ��������ı������ݵķ���
			else
				// ����
				initInput(); // ��ʼ�������ı�������
		}
	}

	/**
	 * ������������ı������ݵķ���
	 */
	private void clearInput() {
		siteNameField.setText(""); // ���վ������
		siteAddressField.setText(""); // ���������ַ
		loginUserField.setText(""); // �����¼�û�
		portField.setText(""); // ����˿ں�
	}

	/**
	 * ��ʼ������������ݵķ���
	 */
	private void initInput() {
		siteNameField.setText(siteBean.getSiteName()); // ����վ������
		siteAddressField.setText(siteBean.getServer()); // ����FTP��ַ
		loginUserField.setText(siteBean.getUserName()); // ���õ�¼�û�
		portField.setText(siteBean.getPort() + ""); // ���ö˿ں�
	}
}
