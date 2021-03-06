package com.mcswainsoftware.encryptionmessenger;

import android.content.*;
import android.provider.*;
import android.telephony.*;
import android.app.*;
import android.graphics.drawable.*;
import android.graphics.*;
import android.os.*;
import android.util.*;
import android.database.*;
import android.net.*;

public class SMSReciever extends BroadcastReceiver { 
    @Override 
    public void onReceive(Context context, Intent intent) { 
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        Intent resultIntent = new Intent(context, ConversationActivity.class);
        resultIntent.putExtra("number", Utilities.trimNumber(messages[0].getOriginatingAddress()));
		resultIntent.putExtra("fromNot", true);
		resultIntent.putExtra("fullnum", messages[0].getOriginatingAddress());
		resultIntent.putExtra("fullbody", messages[0].getMessageBody());
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | 
        Intent.FLAG_ACTIVITY_CLEAR_TASK);
    
// Because clicking the notification launches a new ("special") activity, 
// there's no need to create an artificial back stack.
PendingIntent resultPendingIntent =
         PendingIntent.getActivity(
         context,
         0,
         resultIntent,
         PendingIntent.FLAG_UPDATE_CURRENT
);

// This sets the pending intent that should be fired when the user clicks the
// notification. Clicking the notification launches a new activity

Intent dismissIntent = new Intent(context, QuickReplyActivity.class);
		Bundle bun = new Bundle();
		bun.putString("number", messages[0].getOriginatingAddress());
		dismissIntent.putExtras(bun);
		
dismissIntent.setAction(Intent.ACTION_VIEW);
		PendingIntent piDismiss = PendingIntent.getActivity(context, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT, bun);

          for(SmsMessage i : messages) {
              String name = Utilities.getContactName(context, i.getOriginatingAddress());
              boolean boo=true;
              if(name.isEmpty()) boo=false;
              
            Notification.Builder builder = new Notification.Builder(context)
         .setSmallIcon(R.drawable.ic_send_white_18dp)
        .setContentIntent(resultPendingIntent)
        .setContentTitle((boo) ? name:i.getOriginatingAddress())
        .setContentText(i.getMessageBody())
        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
        .setStyle(new Notification.BigTextStyle()
                .bigText(i.getDisplayMessageBody()))
		.setAutoCancel(true)
		
        .addAction (R.drawable.ic_send_white_18dp,
                context.getString(R.string.reply), piDismiss);
                NotificationManager nM=(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                nM.notify(001, builder.build());
          }
         
    }
	
}
