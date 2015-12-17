package com.lzw.ftp.extClass;

/**
 * @author Li Zhong Wei վ����Ϣ��JavaBean�࣬���ڴ洢FTPվ����Ϣ
 */
public class SiteInfoBean {
	private String siteName; // վ������
	private String server; // ��������ַ
	private String userName; // ��¼�û���
	private int port; // FTP�������˿�
	private String id; // ID���

	/**
	 * Ϊ���ֱ�����ֵ�Ĺ��췽��
	 * 
	 * @param siteName
	 *            վ������
	 * @param server
	 *            ��������ַ
	 * @param port
	 *            �˿ں�
	 * @param userName
	 *            �û���
	 */
	public SiteInfoBean(String siteName, String server, int port,
			String userName) {
		id = System.currentTimeMillis() + ""; // ��ֵID���
		this.siteName = siteName; // ��ֵ����������
		this.server = server; // ��ֵ��������ַ
		this.port = port; // ��ֵ�˿ں�
		this.userName = userName; // ��ֵ�û���
	}

	/**
	 * ���������ַ������и�ֵ�Ĺ��췽��
	 * 
	 * @param id
	 *            ���
	 * @param info
	 *            ������Ϣ�ַ���
	 */
	public SiteInfoBean(String id, String info) {
		this.id = id; // ��ֵID���
		String[] infos = info.split(","); // ����������Ϣ�ַ���
		this.siteName = infos[0]; // ����վ������
		this.server = infos[1]; // ������������ַ
		this.port = Integer.valueOf(infos[2]); // �����˿ں�
		this.userName = infos[3]; // �����û���
	}

	/**
	 * ��ȡվ�����Ƶķ���
	 * 
	 * @return վ������
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * ����վ�����Ƶķ���
	 * 
	 * @param siteName
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * ��д����toString()����
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return siteName; // �������ֵ����ʾ���б������
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
