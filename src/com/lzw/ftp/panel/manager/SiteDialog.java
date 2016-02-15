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

public class SiteDialog extends JDialog implements ActionListener {

	private FtpSiteDialog dialog; 
	private JTextField siteNameField;
	private JTextField siteAddressField;
	private JTextField portField;
	private JTextField loginUserField;
	private SiteInfoBean siteBean;

	public SiteDialog(FtpSiteDialog frame) {
		super(frame); 
		dialog = frame;
		initComponents();
	}

	public SiteDialog(FtpSiteDialog frame, SiteInfoBean siteBean) {
		this(frame);
		dialog = frame;
		this.siteBean = siteBean;
		initInput();
		setTitle("编辑FTP站点"); // ���öԻ���ı���
	}

	public SiteDialog(FtpLinkDialog frame, SiteInfoBean bean) {
		super(frame);
		this.siteBean = bean; 
		initComponents();
		initInput();
		setReadOnly(); // �������ý������ֻ���ķ���
		setTitle("编辑FTP站点"); // ���öԻ���ı���
	}

	private void setReadOnly() {
		siteNameField.setEditable(false);
		siteAddressField.setEditable(false); 
		loginUserField.setEditable(false);
		portField.setEditable(false);
	}

	public void initComponents() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		setTitle("编辑FTP站点");
		JPanel content = new JPanel();
		content.setLayout(new GridLayout(0, 1, 4, 3));

		content.add(new JLabel("站点名称"));
		siteNameField = new JTextField();
		content.add(siteNameField);
		content.add(new JLabel("站点地址"));
		siteAddressField = new JTextField();
		content.add(siteAddressField);
		content.add(new JLabel("端口号"));
		portField = new JTextField();
		portField.setText("21");
		content.add(portField);
		content.add(new JLabel("登录用户"));
		loginUserField = new JTextField();
		content.add(loginUserField);

		JPanel panel = new JPanel();
		final FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
		layout.setHgap(20);
		panel.setLayout(layout);
		final Insets insets = new Insets(0, 8, 0, 8);
		JButton okButton = new JButton("确定");
		okButton.setActionCommand("ok");
		okButton.addActionListener(this);
		JButton cancelButton = new JButton("取消");
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

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("ok")) {
			try {
				if (dialog == null) {
					dispose();
					return;
				}

				String siteName = siteNameField.getText().trim();
				String server = siteAddressField.getText().trim();
				String userName = loginUserField.getText().trim();
				String portStr = portField.getText().trim();

				if (siteName.isEmpty() || server.isEmpty()
						|| userName.isEmpty() || portStr.isEmpty()) {
					JOptionPane.showMessageDialog(this, "站点名不能为空");
					return;
				}
				int port = Integer.valueOf(portStr);
				SiteInfoBean bean = new SiteInfoBean(siteName, server, port,
						userName);
				if (siteBean != null)
					bean.setId(siteBean.getId());
				dialog.addSite(bean);
				dialog.loadSiteList();
				dispose();
			} catch (NullPointerException ex) {
				ex.printStackTrace();
				return;
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "添加成功");
				ex.printStackTrace();
				return;
			}
		}
		if (command.equals("cancel")) {
			if (siteBean == null)
				clearInput(); 
			else
				initInput(); 
		}
	}

	private void clearInput() {
		siteNameField.setText("");
		siteAddressField.setText("");
		loginUserField.setText("");
		portField.setText("");
	}

	private void initInput() {
		siteNameField.setText(siteBean.getSiteName());
		siteAddressField.setText(siteBean.getServer());
		loginUserField.setText(siteBean.getUserName());
		portField.setText(siteBean.getPort() + "");
	}
}
