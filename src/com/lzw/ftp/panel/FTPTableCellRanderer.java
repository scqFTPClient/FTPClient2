package com.lzw.ftp.panel;

import java.io.*;

import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.table.*;

import com.lzw.ftp.extClass.*;

public class FTPTableCellRanderer extends DefaultTableCellRenderer {
	
	private static FTPTableCellRanderer instance = null;

	private FTPTableCellRanderer() {
	}

	public static FTPTableCellRanderer getCellRanderer() {
		if (instance == null)
			instance = new FTPTableCellRanderer();
		return instance;
	}


	@Override
	protected void setValue(Object value) {
		if (value instanceof FileInterface) {
			FileInterface file = (FileInterface) value;
			// ��ȡFileSystemView���ʵ�����
			FileSystemView view = FileSystemView.getFileSystemView();
			if (file.isDirectory()) {
				setText(file.toString());
			} else {
				if (file instanceof File) { // ������ΪFile��
					Icon icon = view.getSystemIcon((File) file);// ��ȡ�ļ���ͼ��
					setIcon(icon); // ���ñ��Ԫͼ��
				} else if (file instanceof FtpFile) { // ������ΪFtpFile��
					FtpFile ftpfile = (FtpFile) file;
					try {
						// ʹ��FtpFile���ļ���ƴ�����ʱ�ļ�
						File tempFile = File.createTempFile("tempfile_",
								ftpfile.getName());
						// ��ȡ��ʱ�ļ���ͼ��
						Icon icon = view.getSystemIcon(tempFile);
						tempFile.delete(); // ɾ����ʱ�ļ�
						setIcon(icon); // ���ñ��Ԫͼ��
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				setText(file.toString());
			}
		} else { 
			setText(value.toString());
		}
	}
}
