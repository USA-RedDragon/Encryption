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
import android.net.Uri;
import java.io.*;
import java.net.*;

public class MainActivity extends ListActivity {
	private ArrayList < String > list;
	private StableArrayAdapter adapter;
	String longitem, phoneNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Drawable wallpaper = WallpaperManager.getInstance(this).getDrawable();
		wallpaper.setColorFilter(Color.parseColor("#88000000"), PorterDuff.Mode.DARKEN);
		this.getWindow().setBackgroundDrawable(wallpaper);
        
        View addButton = findViewById(R.id.add_button);
        addButton.setOutlineProvider(new ViewOutlineProvider() {
                
                @Override
                public void getOutline(View view, Outline outline) {
                    int diameter = getResources().getDimensionPixelSize(R.dimen.diameter);
                    outline.setOval(0, 0, diameter, diameter);
                }
            });
        addButton.setClipToOutline(true);
		final ListView listview = (ListView) findViewById(android.R.id.list);

		list = new ArrayList < String > ();

		adapter = new StableArrayAdapter(this,
		R.layout.conversation_item, getAllSmsConversations());
		listview.setAdapter(adapter);


ImageView fab = (ImageView) findViewById(R.id.add_button);
fab.setOnClickListener(new View.OnClickListener(){
    public void onClick(View v){
        Intent pickContactIntent = new Intent( Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI );
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, 1337);
        
    }
});
		final Dialog dialog = new Dialog(MainActivity.this);
		dialog.setContentView(R.layout.convolongclick);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

		TextView deletetxt = (TextView) dialog.findViewById(R.id.deletetxt);

		deletetxt.setOnClickListener(new View.OnClickListener() {@Override
			public void onClick(View v) {
				dialog.dismiss();
				new AlertDialog.Builder(MainActivity.this)
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

		listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView <? > parent, final View view,
			int position, long id) {
				longitem = (String) parent.getItemAtPosition(position);
				dialog.show();

				return true;
			}

		});
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView <? > parent, final View view,
			int position, long id) {
				longitem = (String) parent.getItemAtPosition(position);
				Intent mIntent = new Intent(MainActivity.this, ConversationActivity.class);
				Bundle bun = new Bundle();
				bun.putString("number", getAllSmsConversations().get(position));
				mIntent.putExtras(bun);
				MainActivity.this.startActivity(mIntent);
			}

		});
	}

    @Override
    protected void onResume()
    {
        list=getAllSmsConversations();
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);  startActivityForResult(intent, 1);

        if(data != null){
        Uri uri = data.getData();
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();

        int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        phoneNo = cursor.getString(phoneIndex);
            Intent mIntent = new Intent(MainActivity.this, ConversationActivity.class);
            Bundle bun = new Bundle();
            bun.putString("number", phoneNo);
            mIntent.putExtras(bun);
            MainActivity.this.startActivity(mIntent);
        } else {
            phoneNo = "";
        }
        
        super.onActivityResult(requestCode, resultCode, data);
    }

	private class StableArrayAdapter extends ArrayAdapter < String > {

		HashMap < String, Integer > mIdMap = new HashMap < String, Integer > ();

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
			for (int i = 0; i < MainActivity.this.list.size(); ++i) {
				mIdMap.put(MainActivity.this.list.get(i), i);
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
			LayoutInflater inflater = (LayoutInflater) MainActivity.this.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.conversation_item, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
			ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);


			Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(getAllSmsConversations().get(position)));
			Cursor c = MainActivity.this.getContentResolver().query(uri, new String[] {
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
			textView.setText((!goof) ? getAllSmsConversations().get(position) : name);
			if(goof) {
                 
                Uri urii = Uri.parse(uri2);
                imageView.setImageURI(null);
                try{
                imageView.setImageBitmap(getRoundedShape(MediaStore.Images.Media.getBitmap(getContentResolver(), urii)));
                } catch(Exception x){}
            }
            
            

			c.close();
			return rowView;
		}


	}

	private void addItem() {

		list.add("Conversation With Jacob");
        
		adapter.notifyDataSetChanged();

	}

	public ArrayList < String > getAllSmsConversations() {
		ArrayList < String > lstSms = new ArrayList < String > ();
		ContentResolver cr = MainActivity.this.getContentResolver();

		/*Cursor c = cr.query(Telephony.Sms.Inbox.CONTENT_URI, // Official CONTENT_URI from docs
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
        c.close();*/
        Cursor c = cr.query(Telephony.Sms.Conversations.CONTENT_URI, // Official CONTENT_URI from docs
                            new String[] {
                                Telephony.Sms.Conversations.ADDRESS+ " as " +Telephony.Sms.Conversations.ADDRESS
                            }, // Select body text
                            null,
                            null,
                            Telephony.Sms.Conversations.DEFAULT_SORT_ORDER); // Default sort order
        int totalSMS = c.getCount();
            if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                if (!lstSms.contains((c.getString(0)))) {
                   // if(!lstSms.contains(c.getString(0)))
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
        int targetWidth = scaleBitmapImage.getWidth();
        int targetHeight = scaleBitmapImage.getHeight();
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
    
    

}
