package com.example.sms_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class mySmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        processSms(context,intent);
        
    }

    private void processSms(Context context, Intent intent) {
        Bundle mybundle = intent.getExtras();
        String message = "";
        String body = "";
        String address ="";
        if (mybundle != null)
        {
            Object[] mysms = (Object[]) mybundle.get("pdus");
            for (int i = 0; i< mysms.length; i++)
            {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) mysms[i]);
                body = sms.getMessageBody(); // lấy nd tin nhắn
                address= sms.getOriginatingAddress(); // lấy sđt
            }
            message = "có 1 tin nhắn từ "+address+"\n"+" "+body+" vừa gửi tới";
            Toast.makeText(context,message, Toast.LENGTH_LONG).show();
        }
    }
}