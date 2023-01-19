package com.guild.calendar.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor.CipherAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;


@Component
public class DesEncryption {
	
	private String encryptKey;
	
	
	public DesEncryption(@Value("${des.key}") String key) {
		this.encryptKey = key;
	}
	
	public String encryptURL(String url) {
		
		try {
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(encryptKey.getBytes());
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			Key key = keyFactory.generateSecret(desKeySpec);
			
			cipher.init(Cipher.ENCRYPT_MODE,key);
			
			
			
			
			return Base64Utils.encodeToUrlSafeString(cipher.doFinal(url.getBytes()));
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	public String decrypttURL(String decodeURL) {
		
		try {
			
			
			
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(encryptKey.getBytes());
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			Key key = keyFactory.generateSecret(desKeySpec);
			
			cipher.init(Cipher.DECRYPT_MODE,key);
			Decoder decoder = Base64.getDecoder();
			
			byte textBytes[] = Base64Utils.decodeFromUrlSafeString(decodeURL);
			
			return new String (cipher.doFinal(textBytes),"UTF-8");
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
}
