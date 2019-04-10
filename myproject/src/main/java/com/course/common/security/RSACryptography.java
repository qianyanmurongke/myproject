package com.course.common.security;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

/**
 * 
 * RSACryptography加密和解密
 * 
 * 创 建 人：Ben.Fang 日 期：2018年2月13日上午9:39:07 描 述： 2018年2月13日上午9:39:07
 * 增加RSACryptography管理类方法 by Ben.Fang
 * 
 * 版 本 号：1.0
 */
public class RSACryptography {
	public static String data = "hello world";
    
	public static String modulusString="95701876885335270857822974167577168764621211406341574477817778908798408856077334510496515211568839843884498881589280440763139683446418982307428928523091367233376499779842840789220784202847513854967218444344438545354682865713417516385450114501727182277555013890267914809715178404671863643421619292274848317157";  
    public static String publicExponentString="65537";  
    public static String privateExponentString="15118200884902819158506511612629910252530988627643229329521452996670429328272100404155979400725883072214721713247384231857130859555987849975263007110480563992945828011871526769689381461965107692102011772019212674436519765580328720044447875477151172925640047963361834004267745612848169871802590337012858580097";  
	
    public static String publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCISLP98M/56HexX/9FDM8iuIEQozy6kn2JMcbZS5/BhJ+U4PZIChJfggYlWnd8NWn4BYr2kxxyO8Qgvc8rpRZCkN0OSLqLgZGmNvoSlDw80UXq90ZsVHDTOHuSFHw8Bv//B4evUNJBB8g9tpVxr6P5EJ6FMoR/kY2dVFQCQM4+5QIDAQAB";
	public static String privateKeyString = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIhIs/3wz/nod7Ff/0UMzyK4gRCjPLqSfYkxxtlLn8GEn5Tg9kgKEl+CBiVad3w1afgFivaTHHI7xCC9zyulFkKQ3Q5IuouBkaY2+hKUPDzRRer3RmxUcNM4e5IUfDwG//8Hh69Q0kEHyD22lXGvo/kQnoUyhH+RjZ1UVAJAzj7lAgMBAAECgYAVh26vsggY0Yl/Asw/qztZn837w93HF3cvYiaokxLErl/LVBJz5OtsHQ09f2IaxBFedfmy5CB9R0W/aly851JxrI8WAkx2W2FNllzhha01fmlNlOSumoiRF++JcbsAjDcrcIiR8eSVNuB6ymBCrx/FqhdX3+t/VUbSAFXYT9tsgQJBALsHurnovZS1qjCTl6pkNS0V5qio88SzYP7lzgq0eYGlvfupdlLX8/MrSdi4DherMTcutUcaTzgQU20uAI0EMyECQQC6il1Kdkw8Peeb0JZMHbs+cMCsbGATiAt4pfo1b/i9/BO0QnRgDqYcjt3J9Ux22dPYbDpDtMjMRNrAKFb4BJdFAkBMrdWTZOVc88IL2mcC98SJcII5wdL3YSeyOZto7icmzUH/zLFzM5CTsLq8/HDiqVArNJ4jwZia/q6Fg6e8KO2hAkB0EK1VLF/ox7e5GkK533Hmuu8XGWN6I5bHnbYd06qYQyTbbtHMBrFSaY4UH91Qwd3u9gAWqoCZoGnfT/o03V5lAkBqq8jZd2lHifey+9cf1hsHD5WQbjJKPPIb57CK08hn7vUlX5ePJ02Q8AhdZKETaW+EsqJWpNgsu5wPqsy2UynO";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		KeyPair keyPair = genKeyPair(1024);

		// 获取公钥，并以base64格式打印出来
		PublicKey publicKey = keyPair.getPublic();
		System.out.println("公钥：" + new String(Base64.getEncoder().encode(publicKey.getEncoded())));
		// RSAPublicKey publicKey = (RSAPublicKey)
		// getPublicKey(publicKeyString);
		// BigInteger modulus1 = publicKey.getModulus();
		// BigInteger exponent1 = publicKey.getPublicExponent();

		// 获取私钥，并以base64格式打印出来
		PrivateKey privateKey = keyPair.getPrivate();
		System.out.println("私钥：" + new String(Base64.getEncoder().encode(privateKey.getEncoded())));

		// 公钥加密
		byte[] encryptedBytes = encrypt(data.getBytes(), publicKey);
		System.out.println("加密后：" + new String(encryptedBytes));

		String encryptedStrs = encrypt(data, publicKey);
		System.out.println("加密后1：" +  encryptedStrs);

		// 私钥解密
		byte[] decryptedBytes = decrypt(encryptedBytes, privateKey);
		System.out.println("解密后：" + new String(decryptedBytes));
		
		String decryptedStrs = decrypt(encryptedStrs, privateKey);
		System.out.println("解密后1：" +  decryptedStrs);

		// //获取公钥
		// PublicKey publicKey=getPublicKey(publicKeyString);
		//
		// //获取私钥
		// PrivateKey privateKey=getPrivateKey(privateKeyString);

	}

	/**
	 * 将base64编码后的公钥字符串转成PublicKey实例
	 * 
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String publicKey) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(publicKey.getBytes());
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(keySpec);
	}

	/**
	 * 将base64编码后的私钥字符串转成PrivateKey实例
	 * 
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String privateKey) throws Exception {
		byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes());
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}

	/**
	 * 将base64编码后的公钥字符串转成PublicKey实例
	 * 
	 * @param modulusStr
	 * @param exponentStr
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String modulusStr, String exponentStr) throws Exception {
		BigInteger modulus = new BigInteger(modulusStr);
		BigInteger exponent = new BigInteger(exponentStr);
		RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(publicKeySpec);
	}

	/**
	 * 将base64编码后的私钥字符串转成PrivateKey实例
	 * 
	 * @param modulusStr
	 * @param exponentStr
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(String modulusStr, String exponentStr) throws Exception {
		BigInteger modulus = new BigInteger(modulusStr);
		BigInteger exponent = new BigInteger(exponentStr);
		RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(privateKeySpec);
	}

	/**
	 * 生成密钥对
	 * 
	 * @param keyLength
	 * @return
	 * @throws Exception
	 */
	public static KeyPair genKeyPair(int keyLength) throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(1024);
		return keyPairGenerator.generateKeyPair();
	}

	/**
	 * 公钥加密
	 * 
	 * @param content
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] content, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");// java默认"RSA"="RSA/ECB/PKCS1Padding"
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(content);
	}

	/**
	 * 私钥解密
	 * 
	 * @param content
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] content, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(content);
	}

	
	/**
	 * 公钥加密，并转换成十六进制字符串打印出来
	 * 
	 * @param content
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String content, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");// java默认"RSA"="RSA/ECB/PKCS1Padding"
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		int splitLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8 - 11;
		byte[][] arrays = splitBytes(content.getBytes(), splitLength);
		StringBuffer sb = new StringBuffer();
		for (byte[] array : arrays) {
			sb.append(bytesToHexString(cipher.doFinal(array)));
		}
		return sb.toString();
	}

	
	/**
	 * 私钥解密，并转换成十六进制字符串打印出来
	 * 
	 * @param content
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String content, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		int splitLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
		byte[] contentBytes = hexString2Bytes(content);
		byte[][] arrays = splitBytes(contentBytes, splitLength);
		StringBuffer sb = new StringBuffer();
		for (byte[] array : arrays) {
			sb.append(new String(cipher.doFinal(array)));
		}
		return sb.toString();
	}

	// 拆分byte数组
	public static byte[][] splitBytes(byte[] bytes, int splitLength) {
		int x; // 商，数据拆分的组数，余数不为0时+1
		int y; // 余数
		y = bytes.length % splitLength;
		if (y != 0) {
			x = bytes.length / splitLength + 1;
		} else {
			x = bytes.length / splitLength;
		}
		byte[][] arrays = new byte[x][];
		byte[] array;
		for (int i = 0; i < x; i++) {

			if (i == x - 1 && bytes.length % splitLength != 0) {
				array = new byte[bytes.length % splitLength];
				System.arraycopy(bytes, i * splitLength, array, 0, bytes.length % splitLength);
			} else {
				array = new byte[splitLength];
				System.arraycopy(bytes, i * splitLength, array, 0, splitLength);
			}
			arrays[i] = array;
		}
		return arrays;
	}

	// byte数组转十六进制字符串
	public static String bytesToHexString(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length);
		String sTemp;
		for (int i = 0; i < bytes.length; i++) {
			sTemp = Integer.toHexString(0xFF & bytes[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	// 十六进制字符串转byte数组
	public static byte[] hexString2Bytes(String hex) {
		int len = (hex.length() / 2);
		hex = hex.toUpperCase();
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}
}
