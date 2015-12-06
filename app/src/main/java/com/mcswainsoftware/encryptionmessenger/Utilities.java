package com.mcswainsoftware.encryptionmessenger;
import android.net.*;
import android.database.*;
import android.content.*;
import android.provider.*;
import java.util.*;

public class Utilities {

    public static String trimNumber(String num) {
            String trimnum = num.replaceAll("\\D+", "").trim();
			if (trimnum.charAt(0) == '1') trimnum = trimnum.substring(1).trim();
			return trimnum;
    }
    
    public static String getContactName(Context context, String number) {
    	Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, number);
	Cursor c = context.getContentResolver().query(uri, new String[] {
				ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI
			}, null, null, null);
			String name = "";
			if (c.moveToFirst()) {
				name = c.getString(0);
				c.moveToNext();
			}
			return name;
    }
    
    public static String getContactIconURI(Context context, String number) {
    		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, number);
	Cursor c = context.getContentResolver().query(uri, new String[] {
				ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI
			}, null, null, null);
			String uri2 = "";
			if (c.moveToFirst()) {
				uri2 = c.getString(0);
				c.moveToNext();
			}
			return uri2;
    }
	public static String getLastSms(Context cn, String num) {
		
		ArrayList < SMS > lstSms = new ArrayList < SMS > ();
		ContentResolver cr = cn.getContentResolver();

		Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI, // Official CONTENT_URI from docs
							new String[] {
								Telephony.Sms.Inbox.BODY, Telephony.Sms.Inbox.ADDRESS, Telephony.Sms.Inbox.DATE
							}, // Select body text
							null,
							null,
							Telephony.Sms.Inbox.DEFAULT_SORT_ORDER); // Default sort order
		Cursor c2 = cr.query(Telephony.Sms.Sent.CONTENT_URI, // Official CONTENT_URI from docs
							 new String[] {
								 Telephony.Sms.Sent.BODY, Telephony.Sms.Sent.ADDRESS, Telephony.Sms.Sent.DATE
							 }, // Select body text
							 null,
							 null,
							 Telephony.Sms.Sent.DEFAULT_SORT_ORDER); // Default sort order



		int totalSMS = c.getCount();
		ArrayList < String > dateRecv = new ArrayList < String > ();
		ArrayList < String > sntSms = new ArrayList < String > ();
		ArrayList < String > dateSent = new ArrayList < String > ();
		if (c.moveToFirst()) {
			for (int i = 0; i < totalSMS; i++) {
				if (c.getString(1).contains(num)) {

					lstSms.add(new SMS(c.getString(0), c.getString(2), false));
				}
				c.moveToNext();
			}
		} else {
			throw new RuntimeException("You have no SMS in Inbox");
		}
		c.close();
		int totalSent = c2.getCount();

		if (c2.moveToFirst()) {
			for (int i = 0; i < totalSent; i++) {
				String trimnum = c2.getString(1).replaceAll("\\D+", "").trim();
				if (trimnum.charAt(0) == '1') trimnum = trimnum.substring(1).trim();
				String trimoth = num.replaceAll("\\D+", "").trim();
				if (trimoth.charAt(0) == '1') trimoth = trimoth.substring(1).trim();

				if (trimnum.contains(trimoth)) {

					lstSms.add(new SMS(c2.getString(0), c2.getString(2), true));
				}
				c2.moveToNext();
			}
		} else {
			throw new RuntimeException("You have no SMS in Inbox");
		}
		c2.close();
		Collections.sort(lstSms);
		return lstSms.get(lstSms.size()-1).message;

	}
}
