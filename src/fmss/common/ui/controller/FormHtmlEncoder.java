package fmss.common.ui.controller;

import org.apache.log4j.Logger;

/**
 * 
 * ��˵�������ַ�������HTML���룬������ҳ������ʾ��ȷ���ַ���ֵ<br>
 * ����: &  �ᱻ����Ϊ &amp;
 * ����ʱ�䣺2006-2-22<br>
 * @author ����<br>
 * @email:oofrank@163.com<br>
 * Copyright 1999,2004 The Apache Software Foundation.
 */
public class FormHtmlEncoder {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FormHtmlEncoder.class);

    private static final String[] htmlCode = new String[256];

    static {
                for (int i = 0; i < 10; i++) {
                        htmlCode[i] = "&#00" + i + ";";
                }

                for (int i = 10; i < 32; i++) {
                        htmlCode[i] = "&#0" + i + ";";
                }

                for (int i = 32; i < 128; i++) {
                        htmlCode[i] = String.valueOf((char)i);
                }

                // Special characters
                //htmlCode[' '] = "&nbsp;";   //oofrank �������α���Bugȥ���ո�
                htmlCode['\n'] = "<BR>\n";
                htmlCode['\"'] = "&quot;"; // double quote
                htmlCode['&'] = "&amp;"; // ampersand
                htmlCode['<'] = "&lt;"; // lower than
                htmlCode['>'] = "&gt;"; // greater than

                for (int i = 128; i < 256; i++) {
                        htmlCode[i] = "&#" + i + ";";
                }
    }

    /**
     * <p>Encode the given text into html.</p>
     *
     * @param string the text to encode
     * @return the encoded string
     *
     */
    public static String encode(String string) {
                int n = string.length();
                char character;
                StringBuffer buffer = new StringBuffer();
        // loop over all the characters of the String.
                for (int i = 0; i < n; i++) {
                        character = string.charAt(i);
                        // the Htmlcode of these characters are added to a StringBuffer one by one
                        try {
                                buffer.append(htmlCode[character]);
                        }
                        catch(ArrayIndexOutOfBoundsException aioobe) {
                                buffer.append(character);
                        }
            }
        return buffer.toString();
    }

    public static void main(String[] args)
    {
		if (logger.isDebugEnabled()) {
			logger.debug("main(String[]) - " + FormHtmlEncoder.encode("<dd>&&")); //$NON-NLS-1$ //$NON-NLS-2$
		}
    }

}
