package com.mcswainsoftware.encryptionmessenger;
import java.util.*;

public class SMS implements Comparable < SMS > {
	public long date;
	public String message;
	public boolean me;

	@Override
	public int compareTo(SMS o) {
		if (o.date > date) return -1;
		if (o.date == date) return 0;
		if (o.date < date) return 1;
		return -2;
	}

	public SMS(String message, String date, boolean me) {
		this.message = message;
		try {
			this.date = Long.parseLong(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.me = me;
	}
}
