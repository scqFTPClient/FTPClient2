package com.lzw.ftp.extClass;

import org.apache.commons.net.ftp.FTPFile;

public class FtpFile extends FTPFile {
	private String name = ""; 
	private String path = ""; 
	protected boolean directory;
	private boolean file; 
	private String lastDate;
	private String size; 
	private long longSize;
	private final int GB = (int) Math.pow(1024, 3); 
	private final int MB = (int) Math.pow(1024, 2);
	private final int KB = 1024;

	public FtpFile(){
		super();
	}

	public FtpFile(String name, String path, boolean directory) {
		super();
		this.name = name; 
		this.path = path;
		this.directory = directory;
	}

	public String getFileSize() {
		return super.getSize() + "";
	}

	public void setFileSize(String nsize) {
		if (nsize.indexOf("DIR") != -1) {
			this.size = "<DIR>";
			directory = true; 
			file = false;
		} else {
			file = true;
			directory = false;
			this.size = nsize.trim(); 
			longSize = Long.parseLong(size);
			if (longSize > GB) {
				size = longSize / GB + "G ";
			}
			if (longSize > MB) {
				size = longSize / MB + "M ";
			}
			if (longSize > KB) {
				size = longSize / KB + "K ";
			}
		}
	}

	//最后修改日期timestamp
	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public String getAbsolutePath() {
		if (path.lastIndexOf('/') == path.length() - 1)
			return path + name;
		else
			return path + "/" + name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getLongSize() {
		return super.getSize();
	}
}
