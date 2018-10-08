package fmss.common.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;

/**
 * <p>
 * 版权所有:(C)2003-2010 
 * </p>
 * 
 * @作者: sunzhan
 * @日期: 2010-6-24 下午05:17:14
 * @描述: [ArrayUtil]请在此简要描述类的功能
 */
public abstract class ArrayUtil extends ArrayUtils{

	public static final int INDEX_NOT_FOUND = -1;

	public static String[] intersect(String[] a, String[] b) {
		List list = new ArrayList();
		if (isEmpty(a) || isEmpty(b)) {
			return new String[] {};
		}
		for (int i = 0; i < a.length; i++) {
			if (indexOf(b, a[i]) > INDEX_NOT_FOUND) {
				list.add(a[i]);
			}
		}
		String[] c = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			c[i] = (String) list.get(i);
		}
		return c;
	}

	public static String[] minus(String[] a, String[] b) {
		List list = new ArrayList();
		if (isEmpty(a)) {
			a = new String[] {};
		}
		if (isEmpty(b)) {
			b = new String[] {};
		}
		for (int i = 0; i < a.length; i++) {
			if (indexOf(b, a[i]) == INDEX_NOT_FOUND) {
				list.add(a[i]);
			}
		}
		String[] c = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			c[i] = (String) list.get(i);
		}
		return c;
	}

	public static String[] union(String[] a, String[] b) {
		List list = new ArrayList();
		if (isEmpty(a) || isEmpty(b)) {
			return new String[] {};
		}
		for (int i = 0; i < a.length; i++) {
			if (!list.contains(a[i])) {
				list.add(a[i]);
			}
		}
		for (int i = 0; i < b.length; i++) {
			if (!list.contains(b[i])) {
				list.add(b[i]);
			}
		}
		String[] c = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			c[i] = (String) list.get(i);
		}
		return c;
	}
	
	public static String[] concat(String[] a, String[] b) {
		String[] c = new String[a.length + b.length];
		for (int i = 0; i < a.length; i++) {
			c[i] = a[i];
		}
		for (int i = 0; i < b.length; i++) {
			c[i + a.length] = b[i];
		}
		return c;
	}

	public static int indexOf(String[] array, String valueToFind, int startIndex) {
		if (array == null) {
			return INDEX_NOT_FOUND;
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
		for (int i = startIndex; i < array.length; i++) {
			if (valueToFind.equals(array[i])) {
				return i;
			}
		}
		return INDEX_NOT_FOUND;
	}

	public static int indexOf(String[] array, String valueToFind) {
		return indexOf(array, valueToFind, 0);
	}

	public static void main(String[] args) {
		String[] a = new String[] { "1", "2", "3", "55", "7", "6", "8", "18" };
		String[] b = new String[] { "100", "2", "3", "60", "7", "56", "4" };
		String[] c = intersect(a, b);
		for (int i = 0; i < c.length; i++) {
			System.out.println(c[i]);
		}
		System.out.println("-----------------------------");
		String[] d = union(a, b);
		for (int i = 0; i < d.length; i++) {
			System.out.println(d[i]);
		}
		System.out.println("-----------------------------");
		String[] e = minus(a, b);
		for (int i = 0; i < e.length; i++) {
			System.out.println(e[i]);
		}
		System.out.println("-----------------------------");
		String[] f = minus(b, a);
		for (int i = 0; i < f.length; i++) {
			System.out.println(f[i]);
		}
	}
}