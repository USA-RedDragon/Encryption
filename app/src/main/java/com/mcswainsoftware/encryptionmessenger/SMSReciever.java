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
Intent dismissIntent = new Intent(this, QuickReplyActivity.class);
dismissIntent.setAction(Intent.ACTION_VIEW);
PendingIntent piDismiss = PendingIntent.getService(this, 0, dismissIntent, 0);

          for(SmsMessage i : messages) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_stat_notification)
        .setContentTitle(Utilities.getContactName(context));
        .setContentText(i.getMessageBody())
        .setDefaults(Notification.DEFAULT_ALL) // requires VIBRATE permission
        .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(msg))
        .addAction (R.drawable.ic_stat_dismiss,
                getString(R.string.dismiss), piDismiss);
          }
    }
}
