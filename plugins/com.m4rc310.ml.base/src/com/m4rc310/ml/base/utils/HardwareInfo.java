package com.m4rc310.ml.base.utils;

public class HardwareInfo {

	private static String OS = System.getProperty("os.name").toLowerCase();
	
	public static String getSerialNumber() {
		if(isWindows()) {
			return Hardware4Win.getSerialNumber();
		}
		
		if(isMac()) {
			return Hardware4Mac.getSerialNumber();
		}
		
		if(isSolaris()||isUnix()) {
			return Hardware4Nix.getSerialNumber();
		}
		
		throw new UnsupportedOperationException();
		
	}
	
	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
		
	}

	public static boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

	}
	
}
