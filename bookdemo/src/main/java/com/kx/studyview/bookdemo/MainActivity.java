package com.kx.studyview.bookdemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kx.studyview.bookdemo.bean.Book;
import com.kx.studyview.bookdemo.service.BookManagerService;
import com.kx.studyview.bookdemo.utils.LogUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private IBookManager  mBookManager ;
    private int index = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService();
    }

    private void startService() {
        Intent intent = new Intent(this, BookManagerService.class);
        bindService(intent,mBookManagerConnection,BIND_AUTO_CREATE);
        startService(intent);
    }

    public void getAllBook(View view) {
        try {
            List<Book> allBook = mBookManager.getAllBook();
            if (allBook!=null){
                int size = allBook.size();
                System.out.println(allBook.toString());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mBookManager !=null ){
                mBookManager.asBinder().unlinkToDeath(mDeathRecipient,0);
                mBookManager =null;
            }
            startService();
        }
    };
    public void addBook(View view) {
        if (mBookManager !=null){
            try {
                Book book = new Book("我是第" + index + "本书", "价格为" + index + "元");
                LogUtils.e("客户端新增Book的地址值 "  + book.getClass().getName() + "@" + Integer.toHexString(book.hashCode()));
                mBookManager.addBook(book);
                index++;
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    ServiceConnection mBookManagerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBookManager =  IBookManager.Stub.asInterface(iBinder);
            try {
                mBookManager.registerListener(mIOnNewBookArrivedListener);
                mBookManager.asBinder().linkToDeath(mDeathRecipient,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    /**
     * 接受新书到来的回调,
     */
    IOnNewBookArrivedListener mIOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            LogUtils.e("新书到来了  " + book.toString());
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( mBookManager !=null && mBookManager.asBinder().isBinderAlive()){
            try {
                if (mIOnNewBookArrivedListener !=null){
                    mBookManager.unregisterListener(mIOnNewBookArrivedListener);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mBookManagerConnection);
    }

}
