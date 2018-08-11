package com.kx.studyview.bookdemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 2018/8/11.
 */

public class Book implements Parcelable{
    private String name;
    private String price;

    public Book(String name, String price) {
        this.name = name;
        this.price = price;
    }

    protected Book(Parcel in) {
        name = in.readString();
        price = in.readString();
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                '}';
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(price);
    }
}
