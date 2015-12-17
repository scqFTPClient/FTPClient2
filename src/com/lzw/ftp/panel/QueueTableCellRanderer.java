/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lzw.ftp.panel;

import java.awt.Component;

import com.lzw.ftp.extClass.FileInterface;
import com.lzw.ftp.extClass.ProgressArg;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * @author lzwJava
 */
public class QueueTableCellRanderer extends JProgressBar implements
		TableCellRenderer {
	public QueueTableCellRanderer() {
		setStringPainted(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value instanceof ProgressArg) {
			ProgressArg arg = (ProgressArg) value;
			setMinimum(arg.getMin());
			setMaximum(arg.getMax());
			setValue(arg.getValue());
			table.setRowSelectionInterval(row, row);
			table.setColumnSelectionInterval(column, column);
		}
		if (getValue() < getMaximum())
			return this;
		else {
			if (getMaximum() == -1)
				return new JLabel();
			return new JLabel("Íê³É");
		}
	}
}
