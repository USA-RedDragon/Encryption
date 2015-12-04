package com.mcswainsoftware.encryptionmessenger;

import android.content.*;
import android.provider.*;
import android.telephony.*;

public class SMSReciever extends BroadcastReceiver { 
    @Override 
    public void onReceive(Context context, Intent intent) { 
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        Intent resultIntent = new Intent(this, ConversationActivity.class);
        resultIntent.putExtra("number", Utilities.trimNumber(i.getOriginatingAddress()));
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | 
        Intent.FLAG_ACTIVITY_CLEAR_TASK);
     
// Because clicking the notification launches a new ("special") activity, 
// there's no need to create an artificial back stack.
PendingIntent resultPendingIntent =
         PendingIntent.getActivity(
         this,
         0,
         resultIntent,
         PendingIntent.FLAG_UPDATE_CURRENT
);

// This sets the pending intent that should be fired when the user clicks the
// notification. Clicking the notification launches a new activity.
builder.setContentIntent(resultPendingIntent);

          for(SmsMessage i : messages) {
             System.out.println("MCSWAIN: MESSAGE BODY: " + i.getMessageBody());
              System.out.println("MCSWAIN: MESSAGE FROM: "+ i.getOriginatingAddress());
          }
    }
}
