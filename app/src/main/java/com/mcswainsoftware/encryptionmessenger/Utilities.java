package com.mcswainsoftware.encryptionmessenger;

public class Utlities {

    public static String trimNumber(String num) {
            String trimnum = num.replaceAll("\\D+", "").trim();
			if (trimnum.charAt(0) == '1') trimnum = trimnum.substring(1).trim();
			return trimnum;
    }
    
    public static String getContactName(Context context, String number) {
    	Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, number));
	Cursor c = context.getContentResolver().query(uri, new String[] {
				ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI
			}, null, null, null);
			String name = "";
			String uri2 = "";
			if (c.moveToFirst()) {
				name = c.getString(0);
				c.moveToNext();
			}
			return name;
    }
    
    public static String getContactIconURI(Context context, String number) {
    		Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, number));
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
    }
}
