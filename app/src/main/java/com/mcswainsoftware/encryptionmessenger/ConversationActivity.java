package com.mcswainsoftware.encryptionmessenger;

import android.app.*;
import android.os.*;
import android.view.*;
import android.content.*;
import android.widget.*;
import android.graphics.drawable.*;
import android.util.*;
import java.util.*;
import android.graphics.*;
import android.database.*;
import android.provider.*;
import android.telephony.*;
import android.net.*;

public class ConversationActivity extends ListActivity {
	private ArrayList < String > list = new ArrayList < String > (), dateSent, dateRecv;
	private StableArrayAdapter adapter;
	String longitem;
    String number;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Drawable wallpaper = WallpaperManager.getInstance(this).getDrawable();
		wallpaper.setColorFilter(Color.parseColor("#88000000"), PorterDuff.Mode.DARKEN);
		this.getWindow().setBackgroundDrawable(wallpaper);
		final ListView listview = (ListView) findViewById(android.R.id.list);
		adapter = new StableArrayAdapter(this,
		R.layout.list_item, list);
		listview.setAdapter(adapter);
    	if(getIntent().getExtras().getBoolean("fromNot", false)){
			markMessageRead(this, getIntent().getExtras().getString("fullnum"), getIntent().getExtras().getString("fullbody"));
			
		}
        
		ImageButton btn = (ImageButton) findViewById(R.id.newmsgsend);
		btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View p1) {
				if(!((EditText) findViewById(R.id.newmsg)).getText().toString().trim().isEmpty()) {
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(getIntent().getExtras().getString("number"), null, ((EditText) findViewById(R.id.newmsg)).getText().toString(), null, null);

				addItem(((EditText) findViewById(R.id.newmsg)).getText().toString(), true);
				((EditText) findViewById(R.id.newmsg)).setText("");
				}
			}
		});

		getAllSmsConversations(getIntent().getExtras().getString("number"));




		final Dialog dialog = new Dialog(ConversationActivity.this);
		dialog.setContentView(R.layout.longclick);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

		TextView deletetxt = (TextView) dialog.findViewById(R.id.deletetxt);
		TextView copytxt = (TextView) dialog.findViewById(R.id.copytxt);

		deletetxt.setOnClickListener(new View.OnClickListener() {@Override
			public void onClick(View v) {
				dialog.dismiss();
				new AlertDialog.Builder(ConversationActivity.this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.delete)
					.setMessage(R.string.really_delete)
					.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						list.remove(longitem);
						adapter.notifyDataSetChanged();
					}

				})
					.setNegativeButton(R.string.no, null)
					.show();



			}
		});

		copytxt.setOnClickListener(new View.OnClickListener() {@Override
			public void onClick(View v) {
				dialog.dismiss();
				ClipData cpy = ClipData.newPlainText("text", longitem);
				ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				clipboard.setPrimaryClip(cpy);
			}
		});
		listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView <? > parent, final View view,
			int position, long id) {
				longitem = (String) parent.getItemAtPosition(position);
				dialog.show();

				return true;
			}

		});

	}

	private class StableArrayAdapter extends ArrayAdapter < String > {

		HashMap < String, Integer > mIdMap = new HashMap < String, Integer > ();
		ArrayList < Boolean > isMe = new ArrayList < Boolean > ();
		public StableArrayAdapter(Context context, int textViewResourceId,
		List < String > objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			mIdMap.clear();
			for (int i = 0; i < ConversationActivity.this.list.size(); ++i) {
				mIdMap.put(ConversationActivity.this.list.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) ConversationActivity.this.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate((isMe.get(position)) ? R.layout.list_item_me : R.layout.list_item, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, ConversationActivity.this.getIntent().getExtras().getString("number"));
            Cursor c = ConversationActivity.this.getContentResolver().query(uri, new String[] {
                                                                        ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI
                                                                    }, null, null, null);
            boolean goof = false;
            String name = "";
            String uri2 = "";
            if (c.moveToFirst()) {
                goof = true;
                name = c.getString(0);
                uri2 = c.getString(1);
                c.moveToNext();
            }
            
            if(goof) {
                if(imageView.getContentDescription().toString().contains("Him")){
                Uri urii = Uri.parse(uri2);
                imageView.setImageURI(null);
                try{
                    imageView.setImageBitmap(getRoundedShape(MediaStore.Images.Media.getBitmap(getContentResolver(), urii)));
                } catch(Exception x){}
                }
            }



			c.close();
			textView.setText(list.get(position));

			return rowView;
		}
		public void setLastIndexMe(boolean me) {
			isMe.add(me);
		}

	}

	private void addItem(String msg, boolean from) {

		list.add(msg);
		adapter.setLastIndexMe(from);
		adapter.notifyDataSetChanged();

	}

	public void getAllSmsConversations(String num) {
		ArrayList < SMS > lstSms = new ArrayList < SMS > ();
		ContentResolver cr = ConversationActivity.this.getContentResolver();

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
		dateRecv = new ArrayList < String > ();
		ArrayList < String > sntSms = new ArrayList < String > ();
		dateSent = new ArrayList < String > ();
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
		for (SMS i: lstSms) {
			addItem(i.message, i.me);
		}

	}
    public ArrayList < String > getAllSmsConversations() {
        ArrayList < String > lstSms = new ArrayList < String > ();
        ContentResolver cr = ConversationActivity.this.getContentResolver();

        Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI, // Official CONTENT_URI from docs
                            new String[] {
                                Telephony.Sms.Inbox.ADDRESS
                            }, // Select body text
                            null,
                            null,
                            Telephony.Sms.Inbox.DEFAULT_SORT_ORDER); // Default sort order

        int totalSMS = c.getCount();

        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                if (!lstSms.contains((c.getString(0)))) {
                    lstSms.add(c.getString(0));
                }
                c.moveToNext();
            }
        } else {
            throw new RuntimeException("You have no SMS in Inbox");
        }
        c.close();
        return lstSms;
	}
    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = scaleBitmapImage.getWidth()-15;
        int targetHeight = scaleBitmapImage.getHeight()-15;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
                                                  targetHeight,Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                       ((float) targetHeight - 1) / 2,
                       (Math.min(((float) targetWidth), 
                                 ((float) targetHeight)) / 2),
                       Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap, 
                          new Rect(0, 0, sourceBitmap.getWidth(),
                                   sourceBitmap.getHeight()), 
                          new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }
	
	private void markMessageRead(Context context, String number, String body) {
		Uri uri = Uri.parse("content://sms/inbox");
		Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
		try{

            while (cursor.moveToNext()) {
				if ((cursor.getString(cursor.getColumnIndex("address")).equals(number)) && (cursor.getInt(cursor.getColumnIndex("read")) == 0)) {
					if (cursor.getString(cursor.getColumnIndex("body")).startsWith(body)) {
						String SmsMessageId = cursor.getString(cursor.getColumnIndex("_id"));
						ContentValues values = new ContentValues();
						values.put("read", true);
						context.getContentResolver().update(Uri.parse("content://sms/inbox"), values, "_id=" + SmsMessageId, null);
						return;
					}
				}
			}
		}catch(Exception e)
		{
			Log.e("Mark Read", "Error in Read: "+e.toString());
		}
	}
}
