package fmss.common.ui.controller;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class StreamUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(StreamUtil.class);

	/**
	 * 功能说明：读取一个Stream并构建成String<br>
	 * 创建者：李涛<br>
	 * 创建时间：2006-4-18<br>
	 * @param stream
	 * @param charsetName   GBK  UTF8 ISO8859-1
	 * @return
	 */
	public static  String read(InputStream stream,String charsetName){
	    byte[] bs=new byte[1024];

	    StringBuffer sb=new StringBuffer();
	    try {
			int i=stream.read(bs);
			while (i>0){
				sb.append(new String(bs,0,i,charsetName));
				i=stream.read(bs);
			}
			
		} catch (IOException e) {
			return "";
		}
		
		return sb.toString();
	}
	
	public static void copy (OutputStream out,InputStream in){
		try {
			int c=in.read();
			while(c!=-1){
				out.write(c);
				c=in.read();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("copy(OutputStream, InputStream)", e); //$NON-NLS-1$
		}
		
	}
	
}
