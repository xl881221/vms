package fmss.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.StringTokenizer;

public abstract class BeanUtil {

	private static String GET = "get";
	private static String SET = "set";
	private static String IS = "is";

	// ~ Methods
	// ////////////////////////////////////////////////////////////////

	public static boolean setProperty(Object object, String property,
			Object value) {
		if ((property == null) || (object == null)) {
			return false;
		}

		// Split out property on dots ( "person.name.first" ->
		// "person","name","first" -> getPerson().getName().getFirst() )
		StringTokenizer st = new StringTokenizer(property, ".");

		if (st.countTokens() == 0) {
			return false;
		}

		// Holder for Object at current depth along chain.
		Object current = object;

		try {
			// Loop through properties in chain.
			for (int i = 0; st.hasMoreTokens(); i++) {
				String currentPropertyName = st.nextToken();

				if (i < st.countTokens()) {
					// This is a getter
					current = invokeProperty(current, currentPropertyName);
				} else {
					// Final property in chain, hence setter
					try {
						// Call setter
						Class cls = current.getClass();
						PropertyDescriptor pd = new PropertyDescriptor(
								currentPropertyName, current.getClass());
						pd.getWriteMethod().invoke(current,
								new Object[] { value });

						return true;
					} catch (Exception e) {
						return false;
					}
				}
			}

			// Return holder Object
			return true;
		} catch (NullPointerException e) {
			// It is very likely that one of the properties returned null. If
			// so, catch the exception and return null.
			return false;
		}
	}

	public static Object getProperty(Object object, String property) {
		if ((property == null) || (object == null)) {
			return null;
		}

		// Split out property on dots ( "person.name.first" ->
		// "person","name","first" -> getPerson().getName().getFirst() )
		StringTokenizer st = new StringTokenizer(property, ".");

		if (st.countTokens() == 0) {
			return null;
		}

		// Holder for Object at current depth along chain.
		Object result = object;

		try {
			// Loop through properties in chain.
			while (st.hasMoreTokens()) {
				String currentPropertyName = st.nextToken();

				// Assign to holder the next property in the chain.
				result = invokeProperty(result, currentPropertyName);
			}

			// Return holder Object
			return result;
		} catch (NullPointerException e) {
			// It is very likely that one of the properties returned null. If
			// so, catch the exception and return null.
			return null;
		}
	}

	/**
	 * Convert property name into getProperty name ( "something" ->
	 * "getSomething" )
	 */
	private static String createMethodName(String prefix, String propertyName) {
		return prefix + propertyName.toUpperCase().charAt(0)
				+ propertyName.substring(1);
	}

	/**
	 * Invoke the method/field getter on the Object. It tries (in order)
	 * obj.getProperty(), obj.isProperty(), obj.property(), obj.property.
	 */
	private static Object invokeProperty(Object obj, String property) {
		if ((property == null) || (property.length() == 0)) {
			return null; // just in case something silly happens.
		}

		Class cls = obj.getClass();
		Object[] oParams = {};
		Class[] cParams = {};

		try {
			// First try object.getProperty()
			Method method = cls.getMethod(createMethodName(GET, property),
					cParams);

			return method.invoke(obj, oParams);
		} catch (Exception e1) {
			try {
				// First try object.isProperty()
				Method method = cls.getMethod(createMethodName(IS, property),
						cParams);

				return method.invoke(obj, oParams);
			} catch (Exception e2) {
				try {
					// Now try object.property()
					Method method = cls.getMethod(property, cParams);

					return method.invoke(obj, oParams);
				} catch (Exception e3) {
					try {
						// Now try object.property()
						Field field = cls.getField(property);

						return field.get(obj);
					} catch (Exception e4) {
						// oh well
						return null;
					}
				}
			}
		}
	}

	public static String[] getDeclaredFields(Class cls) {
		Field[] fields = cls.getDeclaredFields();
		String[] fieldNames = new String[fields != null ? fields.length : 0];
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			fieldNames[i] = field.getName();
		}
		return fieldNames;

	}

	public static String[] getDeclaredFields(Object o) {
		return getDeclaredFields(o.getClass());
	}

	public static Object reflectToFillValue(Class cls, Map map,
			String[] attrituteFields, String[] columnFields)
			throws InstantiationException, IllegalAccessException {
		Object object = cls.newInstance();
		map.get("id");
		for (int i = 0; i < attrituteFields.length; i++) {
			String field = attrituteFields[i];
			String columnField = columnFields[i];
			if (map.get(columnField) != null) {
				if (map.get(columnField) instanceof java.math.BigDecimal
						|| map.get(columnField) instanceof Integer
						|| map.get(columnField) instanceof Long)
					setProperty(object, field, new Long(map.get(columnField)
							.toString()));
			}
			setProperty(object, field, map.get(columnField));
		}
		return object;
	}

}
