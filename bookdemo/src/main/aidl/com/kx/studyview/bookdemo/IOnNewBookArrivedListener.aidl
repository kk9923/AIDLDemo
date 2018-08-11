// IOnNewBookArrivedListener.aidl
package com.kx.studyview.bookdemo;

import com.kx.studyview.bookdemo.bean.Book;
interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book book);
}
