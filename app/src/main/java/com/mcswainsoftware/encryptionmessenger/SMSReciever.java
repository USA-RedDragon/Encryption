package com.mcswainsoftware.encryptionmessenger;

import android.content.*;
import android.provider.*;
import android.telephony.*;

public class SMSReciever extends BroadcastReceiver { 
    @Override 
    public void onReceive(Context context, Intent intent) { 
          SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
          System.out.println("MCSWAIN: Recieved SMS");    
          for(SmsMessage i : messages) {
             System.out.println("MCSWAIN: MESSAGE BODY: " + i.getMessageBody());
              System.out.println("MCSWAIN: MESSAGE FROM: "+ i.getOriginatingAddress());
          }
    }
}
