package com.kx.studyview.aidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.kx.studyview.aidl.MessageSender;
import com.kx.studyview.aidl.bean.MessageModel;
import com.kx.studyview.aidl.utils.LogUtils;

/**
 * Created by admin  on 2018/8/9.
 */
public class MessageService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messageSender;
    }

    IBinder messageSender = new MessageSender.Stub() {
        @Override
        public void sendMessage(MessageModel messageModel) throws RemoteException {
            LogUtils.e("MessageService 进程收到消息了  "  +  messageModel.toString());
        }
    };
}
