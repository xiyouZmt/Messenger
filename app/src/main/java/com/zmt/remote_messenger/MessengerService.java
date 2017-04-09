package com.zmt.remote_messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by zmt on 2017/4/8.
 */

public class MessengerService extends Service {

    public static final int SERVICE = 2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MainActivity.CLIENT :
                    Log.e("service", msg.getData().getString("client"));
                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null, SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString("service", "hello this is service");
                    replyMessage.setData(bundle);
                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        Log.e("REmoteException", e.toString());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private final Messenger mMessenger = new Messenger(new MessengerHandler());
}
