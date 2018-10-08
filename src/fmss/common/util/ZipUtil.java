package fmss.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import common.crms.util.zip.ZipEntry;
import common.crms.util.zip.ZipFile;
import common.crms.util.zip.ZipOutputStream;




public class ZipUtil {
	public static final int BUFFER = 2048;

	public static void zip(String[] filename, String destFile) throws Exception {
		BufferedInputStream origin = null;
		ZipOutputStream out = null;
		try {
			FileOutputStream dest = new FileOutputStream(destFile);
			out = new ZipOutputStream(new BufferedOutputStream(dest));
			byte data[] = new byte[BUFFER];
			for (int i = 0; i < filename.length; i++) {
				File file = new File(filename[i]);
				FileInputStream fi = new FileInputStream(file);
				origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(file.getName());
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
			}
		}
		catch (Exception e) {
			throw e;
		} finally {
			if (origin != null)
				origin.close();
			if (out != null)
				out.close();
		}
	}
	public static void zip(InputStream[] ins,String[] fileNames, OutputStream o) throws Exception {
		BufferedInputStream origin = null;
		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(o);
			byte data[] = new byte[BUFFER];
			for (int i = 0; i < ins.length; i++) {
				origin = new BufferedInputStream(ins[i], BUFFER);
				ZipEntry entry = new ZipEntry(fileNames[i]);
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
			}
		}
		catch (Exception e) {
			throw e;
		} finally {
			if (origin != null)
				origin.close();
			if (out != null)
				out.close();
		}
	}
	
	
	public static Map getZipEntriesStreamMap(ZipFile zipFile) throws IOException {
		Map resultMap = new HashMap();
		Enumeration enu = zipFile.getEntries();
		while (enu.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) enu.nextElement();
			String fileName = entry.getName();
			if ((fileName.indexOf(File.separator) != -1)
					|| (fileName.indexOf("/") != -1)) {
				if (fileName.indexOf(".") != -1) {
					String fileNameTemp = entry
							.getName()
							.substring(
									entry.getName().lastIndexOf(File.separator) != -1 ? entry
											.getName().lastIndexOf(
													File.separator) + 1
											: entry.getName().lastIndexOf("/") + 1,
									entry.getName().length());
					resultMap.put(entry, fileNameTemp);
				}
			} else {
				resultMap.put(entry, fileName);
			}
		}
		return resultMap;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		
		
		
		
	}
	
	public static void map(Map map){
		Iterator it = map.keySet().iterator();
		while(it.hasNext()){
			String key=(String) it.next();
			Object value=map.get(key);	
		}
	}

}
