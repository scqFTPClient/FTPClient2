package com.lzw.ftp.panel.ftp;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import com.lzw.ftp.extClass.FtpClient;
import com.lzw.ftp.extClass.FtpFile;
import com.lzw.ftp.panel.local.LocalPanel;

/**
 * FTP面板的删除按钮的动作处理器
 */
class DelFileAction extends AbstractAction {
	private FtpPanel ftpPanel;

	/**
	 * 删除动作处理器的构造方法
	 * 
	 * @param ftpPanel
	 *            - FTP资源管理面板
	 * @param name
	 *            - 动作名称
	 * @param icon
	 *            - 图标
	 */
	public DelFileAction(FtpPanel ftpPanel, String name, Icon icon) {
		super(name, icon);
		this.ftpPanel = ftpPanel;
	}

	public void actionPerformed(ActionEvent e) {
		// 获取显示FTP资源列表的表格组件当前选择的所有行
		final int[] selRows = ftpPanel.ftpDiskTable.getSelectedRows();
		if (selRows.length < 1)
			return;
		int confirmDialog = JOptionPane.showConfirmDialog(ftpPanel, "确定要删除吗？");
		if (confirmDialog == JOptionPane.YES_OPTION) {
			Runnable runnable = new Runnable() {
				/**
				 * 删除服务器文件的方法
				 * 
				 * @param file
				 *            - 文件名称
				 */
				private void delFile(FtpFile file) {
					FtpClient ftpClient = ftpPanel.ftpClient; // 获取ftpClient实例
					try {
						if (file.isFile()) { // 如果删除的是文件
							ftpClient.sendServer("DELE " + file.getName()
									+ "\r\n"); // 发送删除文件的命令
							ftpClient.readServerResponse(); // 接收返回编码
						} else if (file.isDirectory()) { // 如果删除的是文件夹
							ftpClient.cd(file.getName()); // 进入到该文件夹
							InputStreamReader list = new InputStreamReader(
									ftpClient.list()); // 读取文件列表
							BufferedReader br = new BufferedReader(list);
							String nameStr = null;
							while ((nameStr = br.readLine()) != null) {// 解析每个文件
								Thread.sleep(0, 100); // 线程休眠
								String name = nameStr.substring(39); // 解析文件名
								String size = nameStr.substring(18, 39);// 解析文件大小
								FtpFile ftpFile = new FtpFile(); // 创建文件对象
								ftpFile.setName(name); // 设置文件名
								ftpFile.setPath(file.getAbsolutePath());// 设置文件路径
								ftpFile.setSize(size); // 设置文件大小
								delFile(ftpFile); // 递归删除文件或文件夹
							}
							list.close();// 关闭读取文件列表的输入流
							br.close();
							ftpClient.cdUp(); // 返回上层文件夹
							ftpClient.sendServer("RMD " + file.getName()
									+ "\r\n"); // 发送删除文件夹指令
							ftpClient.readServerResponse(); // 接收返回码
						}
					} catch (Exception ex) {
						Logger.getLogger(LocalPanel.class.getName()).log(
								Level.SEVERE, null, ex);
					}
				}

				/**
				 * 线程的主体方法
				 * 
				 * @see java.lang.Runnable#run()
				 */
				public void run() {
					// 遍历显示FTP资源的表格的所有选择行
					for (int i = 0; i < selRows.length; i++) {
						// 获取每行的第一个单元值，并转换为FtpFile类型
						final FtpFile file = (FtpFile) ftpPanel.ftpDiskTable
								.getValueAt(selRows[i], 0);
						if (file != null) {
							delFile(file); // 调用删除文件的递归方法
							try {
								// 向服务器发删除文件夹的方法
								ftpPanel.ftpClient.sendServer("RMD "
										+ file.getName() + "\r\n");
								// 读取FTP服务器的返回码
								ftpPanel.ftpClient.readServerResponse();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					// 刷新FTP服务器资源列表
					DelFileAction.this.ftpPanel.refreshCurrentFolder();
					JOptionPane.showMessageDialog(ftpPanel, "删除成功。");
				}
			};
			new Thread(runnable).start();
		}
	}
}