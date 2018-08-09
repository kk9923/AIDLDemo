// MessageSender.aidl
package com.kx.studyview.aidl;
import com.kx.studyview.aidl.bean.MessageModel ;
import com.kx.studyview.aidl.MessageReceiver ;
interface MessageSender {
  void sendMessage(in MessageModel messageModel);
  void registerReceiveListener(MessageReceiver messageReceiver);
  void unregisterReceiveListener(MessageReceiver messageReceiver);
}
