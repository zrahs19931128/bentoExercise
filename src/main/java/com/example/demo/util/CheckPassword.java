package com.example.demo.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

@Service
public class CheckPassword {

	//數字
	public static final String reg_number = ".*\\d+.*";
	
	//大寫字母
	public static final String reg_uppercase = ".*[A-Z]+.*";
	
	public static boolean checkPasswordRule(String password) {
		
		if(password == null || password.length() < 8) {
			return false;
		}
		
		int i =0;
		
		if(password.matches(reg_number)) {
			i++;
		}
		
		if(password.matches(reg_uppercase)) {
			i++;
		}
		
		if(i < 2) {
			return false;
		}
		return true;
	}
	
	public static String PasswordEncrypt(String password) {
		
		String encryptPassword = "";
		Base64.Encoder encoder = Base64.getEncoder();
		byte[] textByte;
		
		try {
			textByte = password.getBytes("UTF-8");
			encryptPassword = encoder.encodeToString(textByte);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptPassword;
	}
	
	public static String PasswordDecrypt(String password) {
		
		String decryptPassword = "";
		Base64.Decoder decoder = Base64.getDecoder();
		
		try {
			decryptPassword = new String (decoder.decode(decryptPassword), "UTF-8");
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decryptPassword;
	}
}
