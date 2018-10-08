package fmss.common.util;

import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

public class JXLTool {

	public static WritableCellFormat getHeader() throws WriteException {
		// ��������
		WritableFont font = new WritableFont(WritableFont.TIMES, 12,
				WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		try {
			// ���Ҿ���
			format.setAlignment(jxl.format.Alignment.CENTRE);
			// ���¾���
			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			// ��ɫ�߿�
			format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			format.setBackground(Colour.YELLOW2);
			format.setWrap(true);
		} catch (WriteException e) {
			throw e;
		}
		return format;
	}

	public static WritableCellFormat getContentFormat() throws WriteException {
		WritableCellFormat format = new WritableCellFormat(NumberFormats.TEXT);
		try {
			format.setWrap(false);
			// �������Ҿ���
			format.setAlignment(jxl.format.Alignment.LEFT);
  			format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); //���ñ߿�   

		} catch (WriteException e) {
			throw e;
		}
		return format;
	}

}
