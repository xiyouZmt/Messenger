package com.zmt.remote_messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    public static final int CLIENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private Messenger getReplyMessenger = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MessengerService.SERVICE:
                    Log.e("messenger", "service" + msg.getData().getString("service"));
                    break;
            }
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger messenger = new Messenger(service);
            Message msg = Message.obtain(null, CLIENT);
            Bundle data = new Bundle();
            data.putString("client", "hello, this is client");
            msg.setData(data);

            /**
             * 把接收服务端收回复的Messenger通过msg.replyTo参数传递给服务端
             */
            msg.replyTo = getReplyMessenger;
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                Log.e("RemoteException", e.toString());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
