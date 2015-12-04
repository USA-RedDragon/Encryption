package com.mcswainsoftware.encryptionmessenger;

public class Utlities {

    public static String trimNumber(String num) {
            String trimnum = num.replaceAll("\\D+", "").trim();
			if (trimnum.charAt(0) == '1') trimnum = trimnum.substring(1).trim();
			return trimnum;
    }
}
