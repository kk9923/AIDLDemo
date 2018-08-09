// MessageSender.aidl
package com.kx.studyview.aidl;
import com.kx.studyview.aidl.bean.MessageModel ;
interface MessageSender {
  void onMessageReceived(in MessageModel receivedMessage);
}
