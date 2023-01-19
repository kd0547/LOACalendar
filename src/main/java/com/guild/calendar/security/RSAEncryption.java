package com.guild.calendar.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;



public class RSAEncryption {
	
	public static final String Encrypt_Type = "RSA";
	public static final int Key_Length = 1024;
	
	private Cipher cipher;
	
	public RSAEncryption(Cipher cipher) {
		this.cipher = cipher;
	}
	
	public String encrypt(String plainText,PublicKey publicKey) {
		
		try {
			//Cipher cipher = Cipher.getInstance("RSA");
			
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			
			byte[] bytePlain = cipher.doFinal(plainText.getBytes());
			
			return Base64.getEncoder().encodeToString(bytePlain);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return null;
	}
	public String decrypt(String encrypted, PrivateKey privateKey) {
		
		try {
			byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			
			byte[] bytePlain = cipher.doFinal(byteEncrypted);
			
			return new String(bytePlain, "utf-8");
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (BadPaddingException e) {
			e.printStackTrace();
			
			return "-106";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public KeyPair genRSAkeyPair() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance(Encrypt_Type);
			generator.initialize(Key_Length,new SecureRandom());
			
			return generator.genKeyPair();
		} catch (NoSuchAlgorithmException e) {
			
			
			e.printStackTrace();
		}
		
		return null;
	}
}
