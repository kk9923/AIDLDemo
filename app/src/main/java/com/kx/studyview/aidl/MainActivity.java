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
     * 消息监听回调接口
     */
    MessageReceiver mMessageReceiver = new MessageReceiver.Stub() {
        @Override
        public void onMessageReceived(MessageModel receivedMessage) throws RemoteException {
            LogUtils.e("客户端收到服务端发过来的消息了  "  +  receivedMessage.toString());
        }
    };
  //  当服务端进程Crash  binder死亡时，会回调binderDied方法；
    IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mMessageSender!=null){
                mMessageSender.asBinder().unlinkToDeath(mDeathRecipient,0);
                mMessageSender = null ;
            }
            //  重启服务端
            setupService();
        }
    };
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName com4ponentName, IBinder iBinder) {
            // 获得 服务端 AIDL的操作接口  相当于一个中间对象
            mMessageSender =  MessageSender.Stub.asInterface(iBinder);
            MessageModel messageModel = new MessageModel();
            messageModel.setContent("我是消息内容");
            messageModel.setTo("服务端接受");
            messageModel.setFrom("客户端发送");
            LogUtils.e("客户端发送消息  "  +  messageModel.toString());
            try {
                mMessageSender.registerReceiveListener(mMessageReceiver);
                mMessageSender.sendMessage(messageModel);
                //设置 服务端 Binder死亡监听
                mMessageSender.asBinder().linkToDeath(mDeathRecipient,0);
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
     * bindService
     */
    private void setupService() {
        Intent intent = new Intent(this, MessageService.class);
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);
    }
    /**
     * unbindService
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMessageReceiver!=null && mMessageReceiver.asBinder().isBinderAlive()){
            try {
                // TODO: 2018/8/9  解决当MessageService方法onBind方法返回Null时  mMessageSender为空的崩溃
                if (mMessageSender!=null){
                    mMessageSender.unregisterReceiveListener(mMessageReceiver);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mServiceConnection);
    }
}
