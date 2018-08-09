package com.kx.studyview.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kx.studyview.aidl.bean.MessageModel;
import com.kx.studyview.aidl.service.MessageService;
import com.kx.studyview.aidl.utils.LogUtils;

public class MainActivity extends AppCompatActivity {
    private  MessageSender mMessageSender ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       setupService();
    }

    /**
     * bindService
     */
    private void setupService() {
        Intent intent = new Intent(this, MessageService.class);
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName com4ponentName, IBinder iBinder) {
            // 获得AIDL的操作接口
            mMessageSender =  MessageSender.Stub.asInterface(iBinder);
            MessageModel messageModel = new MessageModel();
            messageModel.setContent("我是消息内容");
            messageModel.setTo("服务端接受");
            messageModel.setFrom("客户端发送");
            LogUtils.e("客户端发送消息  "  +  messageModel.toString());
            try {
                mMessageSender.sendMessage(messageModel);
            } catch (RemoteException e) {
                e.printStackTrace();
                LogUtils.e("RemoteException  "  +  e.toString());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    /**
     * unbindService
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }
}
