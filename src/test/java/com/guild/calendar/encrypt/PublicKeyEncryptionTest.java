package com.guild.calendar.encrypt;

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
import javax.crypto.NoSuchPaddingException;

import org.junit.jupiter.api.Test;

public class PublicKeyEncryptionTest {
	
	@Test
	void publicKeyEncryptionTest() throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		//PublicKey publicKey = new RSA
		KeyPair keyPair = genRSAkeyPair();
		String plainText = "test1234";
		
		String test1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWgkOnycP4rvOkmj6NLbFU70NWrGpIkZn2lcxJFq8giwvNm7IL2sY78uS5SDVfZt5nIV5qMFkN3uDlh2Mn2gjh8KbLCQ/JSxwnwk3lXzsop2pZUMOIQVBZqPH1Qbn0PAz0Yzq8XVcA42I4QH8cydUwAZwM5tEPNnBnQZE+gl0wmwIDAQAB";
		String test2 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWgkOnycP4rvOkmj6NLbFU70NWrGpIkZn2lcxJFq8giwvNm7IL2sY78uS5SDVfZt5nIV5qMFkN3uDlh2Mn2gjh8KbLCQ/JSxwnwk3lXzsop2pZUMOIQVBZqPH1Qbn0PAz0Yzq8XVcA42I4QH8cydUwAZwM5tEPNnBnQZE+gl0wmwIDAQAB";
		
		System.out.println(test1.equals(test2));
		
		//String encrypted = encrypt(plainText,keyPair.getPublic());
		//System.out.println("encrypted : "+encrypted);
		
		//String decrypted = decrypt(encrypted,keyPair.getPrivate());
		//System.out.println("decrypted : "+decrypted);
	}
	
	public String encrypt(String plainText,PublicKey publicKey) {
		
		
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			
			byte[] bytePlain = cipher.doFinal(plainText.getBytes());
			
			return Base64.getEncoder().encodeToString(bytePlain);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public String decrypt(String encrypted, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		
		Cipher cipher = Cipher.getInstance("RSA");
		byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] bytePlain = cipher.doFinal(byteEncrypted);
		
		return new String(bytePlain, "utf-8");
	}
	public KeyPair genRSAkeyPair() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024,new SecureRandom());
			
			return generator.genKeyPair();
		} catch (NoSuchAlgorithmException e) {
			
			
			e.printStackTrace();
		}
		
		return null;
	}
}
