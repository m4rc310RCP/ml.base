package com.m4rc310.ml.ui;

import java.io.ByteArrayOutputStream;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author marcelo
 */
public class MD5Utils {

	public static String getChecksum(Serializable object) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(baos.toByteArray());
			oos.close();
			baos.close();
			return DatatypeConverter.printHexBinary(thedigest);
		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}

	public static String getHashMD5(File file) {
		try (InputStream is = new FileInputStream(file)) {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			// byte[] buffer = new byte[80192];
			byte[] buffer = new byte[(int) file.length()];
			int read;
			while ((read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}

			byte[] md5sum = digest.digest();
			BigInteger bi = new BigInteger(1, md5sum);
			String out = bi.toString(16);
			return out;
		} catch (Exception ex) {
			return null;
		}

	}

	public static String getHashMD5(String string) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			BigInteger bi = new BigInteger(1, md.digest(string.getBytes()));
			return bi.toString(16);
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(MD5Utils.class.getName()).log(Level.SEVERE, null, ex);

			return "";
		}
	}
}
