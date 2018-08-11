// BookManager.aidl
package com.kx.studyview.bookdemo;

import com.kx.studyview.bookdemo.bean.Book ;
import com.kx.studyview.bookdemo.IOnNewBookArrivedListener;

interface IBookManager {
    void addBook(in Book book);
    List<Book> getAllBook();
    void registerListener(IOnNewBookArrivedListener listener);
    void unregisterListener(IOnNewBookArrivedListener listener);
}
