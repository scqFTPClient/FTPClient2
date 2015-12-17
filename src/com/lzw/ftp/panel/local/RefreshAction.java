/**
 * 
 */
package com.lzw.ftp.panel.local;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * @author Li Zhong Wei 刷新本地资源列表的动作处理器
 */
class RefreshAction extends AbstractAction {
	private LocalPanel localPanel; // 本地资源管理面板的引用

	/**
	 * 构造方法
	 * 
	 * @param localPanel
	 *            本地资源管理面板
	 * @param name
	 *            动作的名称
	 * @param icon
	 *            动作的图标
	 */
	public RefreshAction(LocalPanel localPanel, String name, Icon icon) {
		super(name, icon); // 执行父类的构造方法
		this.localPanel = localPanel; // 赋值本地资源管理面板的引用
	}

	/**
	 * 动作的事件处理方法
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		this.localPanel.refreshCurrentFolder(); // 调用管理面板的刷新方法
	}
}