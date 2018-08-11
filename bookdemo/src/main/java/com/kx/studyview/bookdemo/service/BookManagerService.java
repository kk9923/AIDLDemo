package com.kx.studyview.bookdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.kx.studyview.bookdemo.IBookManager;
import com.kx.studyview.bookdemo.IOnNewBookArrivedListener;
import com.kx.studyview.bookdemo.bean.Book;
import com.kx.studyview.bookdemo.utils.LogUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by admin on 2018/8/11.
 */
public class BookManagerService extends Service{
    private static final String TAG = "BookManagerService";
    private AtomicBoolean serviceStop = new AtomicBoolean(false);
    private CopyOnWriteArrayList<Book> mBooks = new CopyOnWriteArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> mListenerList = new RemoteCallbackList<>();
    private int index = 100;

     private IBinder mIBinder = new IBookManager.Stub() {
         @Override
         public void addBook(Book book) throws RemoteException {
             LogUtils.e("服务端收到Book的地址值 "  + book.getClass().getName() + "@" + Integer.toHexString(book.hashCode()));
            mBooks.add(book);
         }

         @Override
         public List<Book> getAllBook() throws RemoteException {
             return mBooks;
         }

         @Override
         public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListenerList.register(listener);
         }

         @Override
         public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
             mListenerList.unregister(listener);
         }
     };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new NewAddBookThread()).start();
    }
     class NewAddBookThread implements Runnable{
         @Override
         public void run() {
             while (!serviceStop.get()){
                 SystemClock.sleep(5000);
                 Book book = new Book("我是第"+index+"本书","价格为"+ index +"元" );
                 index++;
                 mBooks.add(book);

                 int listenerSize = mListenerList.beginBroadcast();
                 for (int i = 0; i < listenerSize; i++) {
                     IOnNewBookArrivedListener listener = mListenerList.getBroadcastItem(i);
                     if (listener!=null){
                         try {
                             listener.onNewBookArrived(book);
                         } catch (RemoteException e) {
                             e.printStackTrace();
                         }
                     }
                 }
                 mListenerList.finishBroadcast();
             }
         }
     }
}
