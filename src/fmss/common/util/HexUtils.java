package fmss.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import fmss.common.cache.CacheManager;


public final class HexUtils{

	private static final char DIGITS[] = {'0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

	private static char[] encodeHex(byte data[]){
		int l = data.length;
		char out[] = new char[l << 1];
		int i = 0;
		int j = 0;
		for(; i < l; i++){
			out[j++] = DIGITS[(0xf0 & data[i]) >>> 4];
			out[j++] = DIGITS[0xf & data[i]];
		}
		return out;
	}

	private static byte[] encode(byte array[]){
		return (new String(encodeHex(array))).getBytes();
	}

	static MessageDigest getDigest(String algorithm){
		try{
			return MessageDigest.getInstance(algorithm);
		}catch (NoSuchAlgorithmException e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	private static MessageDigest getMd5Digest(){
		return getDigest("MD5");
	}

	private static MessageDigest getShaDigest(){
		return getDigest("SHA");
	}

	/**
	 * <p>方法名称: md5Hex|描述: md5加密</p>
	 * @param data
	 * @return
	 */
	public static String md5Hex(String data){
		byte[] b = getMd5Digest().digest(data.getBytes());
		return new String(encodeHex(b));
	}

	/**
	 * <p>方法名称: shaHex|描述: sha加密</p>
	 * @param data
	 * @return
	 */
	public static String shaHex(String data, String salt){
		data = isEncodingPasswordWithSalt() ? mergePasswordAndSalt(data,salt) : data;
		byte[] b = getShaDigest().digest(data.getBytes());
		return new String(encodeHex(b));
	}
	
	private static boolean isEncodingPasswordWithSalt(){
		CacheManager cacheManager = (CacheManager) SpringContextUtils.getBean("cacheManager");
		if(cacheManager == null || !"1".equals(cacheManager.getParemerCacheMapValue(PARAM_SYS_PWD_ENCODE_SALT)))
			return false;
		return true;
	}
	
	public static final String PARAM_SYS_PWD_ENCODE_SALT = "PARAM_SYS_PWD_ENCODE_SALT";
	
	public static String mergePasswordAndSalt(String password, Object salt) {
        if (password == null) {
            password = "";
        }
        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }
}
