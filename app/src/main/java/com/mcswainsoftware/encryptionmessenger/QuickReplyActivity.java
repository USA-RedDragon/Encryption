package com.mcswainsoftware.encryptionmessenger;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.telephony.*;
import android.provider.*;

public class QuickReplyActivity extends Activity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		setContentView(R.layout.quick_reply);
		getActionBar().hide();
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(001);
		((TextView)findViewById(R.id.label)).setText("Reply To " + Utilities.getContactName(this, getIntent().getExtras().getString("number")) + " <"+ getIntent().getExtras().getString("number") +">");
		ImageButton btn = (ImageButton) findViewById(R.id.newmsgsend);
		btn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View p1) {
					try {
					if(!((EditText) findViewById(R.id.newmsg)).getText().toString().trim().isEmpty()) {
						SmsManager smsManager = SmsManager.getDefault();
						smsManager.sendTextMessage(QuickReplyActivity.this.getIntent().getExtras().getString("number"), null, ((EditText) findViewById(R.id.newmsg)).getText().toString(), null, null);
						
						
						((EditText) findViewById(R.id.newmsg)).setText("");
						QuickReplyActivity.this.finish();
					}
				} catch(Exception x){
					Toast.makeText(QuickReplyActivity.this, x.getMessage(), Toast.LENGTH_LONG).show();
				}
				} 
			});
		super.onCreate(savedInstanceState);
	}
	
}
