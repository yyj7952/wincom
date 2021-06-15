package kr.co.wincom.imcs.common;

import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESUtil
{
	private static String AES_CBC_NOPADDING ="AES/CBC/NoPadding";
	private static String CIPER_KEY = AES_CBC_NOPADDING;

	public static String encrypt(String password, String msg) throws Exception
	{
		byte[] bKey = new byte[32];    
		byte[] bKeyTemp = password.getBytes("UTF-8");
		
		System.arraycopy(bKeyTemp, 0, bKey, 0, bKeyTemp.length);
		
		SecretKeySpec skeySpec = new SecretKeySpec(bKey, "AES");
		
		Cipher cipher = Cipher.getInstance(CIPER_KEY);
		
		int size = cipher.getBlockSize();
		
		byte[] iv = new byte[size];
		AlgorithmParameterSpec spec = new IvParameterSpec(iv);
		
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, spec);
		
		byte[] srcbytes = msg.getBytes();
		byte[] srcNewBytes = new byte[32];
		
		System.arraycopy(srcbytes, 0, srcNewBytes, 0, srcbytes.length);
		
		byte[] encrypted = cipher.doFinal(srcNewBytes);
		
		String sReturn = Base64.encodeBase64String(encrypted);
		
		bKey = null;
		bKeyTemp = null;
		iv = null;
		srcbytes = null;
		srcNewBytes = null;
		encrypted = null;
		
		return sReturn;
	}
	
	public static String decrypt(String password, String encMsg) throws Exception
	{
		Cipher cipher = Cipher.getInstance(CIPER_KEY);
		
		int size = cipher.getBlockSize();
		
		byte[] bKey = new byte[32];    
		byte[] bKeyTemp = password.getBytes("UTF-8");
		
		System.arraycopy(bKeyTemp, 0, bKey, 0, bKeyTemp.length);
		
		byte[] iv = new byte[size];    
		
		AlgorithmParameterSpec spec = new IvParameterSpec(iv);
		
		SecretKeySpec skeySpec = new SecretKeySpec(bKey, "AES");
		
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, spec);
		
		byte[] original = cipher.doFinal(Base64.decodeBase64(encMsg));
		
		String sReturn = (new String(original)).trim();
		
		bKey = null;
		bKeyTemp = null;
		iv = null;
		original = null;
		
		return sReturn;
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		/*String pwd = "lguplus";
		String src = "338554942220161115145622";
		
		System.out.println("--------------------------------------------------");
		String encrypted = encrypt(pwd, src);
		encrypted = "NoUgvd1qXxC/tuwS3lEnkpqrvjhzi3DcfLzUh1XLKQA=";
		
		String decrypted = decrypt(pwd, encrypted);
		System.out.println("src: [" + src + "]");
		System.out.println("encrypted2: [" + encrypted + "]");
		System.out.println("decrypted2: [" + decrypted + "]");*/
		
		String saId = "500094757756";
		
		//String pwd = "lguplus";
		
//		String pwd = "3db5a8e70f0df992";
		
		String albumId = "M0117B0307PPV00";
		
		
		String strOneTimeKey = "";
		
		
		String strTempSaId = "";
		String strTempMsg = "";
		
		String msg = "";
		
		// APP, CDN 요청으로 인하여 가입번호가 10자리인 경우 가입번호 앞에 00 이라는 숫자 append
		if(saId.trim().length() == 10)
			strTempSaId = ("00" + saId);
		else
			strTempSaId = saId;
		
	
		
		
	
			/* VOD인 경우 가입번호 7~10번째 자리(4byte) + Asset ID 5~10번째자리(6byte) + 현재시각으로 원본 데이터 생성	*/
			strTempMsg = strTempSaId.substring(6, 10) + albumId.substring(4, 10) + 
					String.valueOf(CommonService.getSystemTime(0));
			
			
//			String encrypted = encrypt(pwd, strTempMsg);
			
			
		
		
	}
}
























