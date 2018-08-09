// MessageSender.aidl
package com.kx.studyview.aidl;
import com.kx.studyview.aidl.bean.MessageModel ;
interface MessageReceiver {
  void onMessageReceived(in MessageModel receivedMessage);
}
