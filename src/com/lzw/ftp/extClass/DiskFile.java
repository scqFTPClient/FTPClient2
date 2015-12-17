/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lzw.ftp.extClass;

import java.io.File;

/**
 * 
 * @author lzwJava
 */
public class DiskFile extends java.io.File implements FileInterface {

	public DiskFile() {
		super(".");
	}

	public DiskFile(File theFile) {
		super(theFile.toURI());
	}

	@Override
	public String toString() {
		return getName();
	}
}
