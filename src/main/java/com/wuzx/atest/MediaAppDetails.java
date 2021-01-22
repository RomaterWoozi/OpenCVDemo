package com.wuzx.atest;

import android.content.ComponentName;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.session.MediaSessionCompat;

public class MediaAppDetails implements Parcelable {

    public MediaSessionCompat.Token sessionToken;
    public ComponentName componentName;

    protected MediaAppDetails(Parcel in) {
        sessionToken = in.readParcelable(MediaSessionCompat.Token.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(sessionToken, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaAppDetails> CREATOR = new Creator<MediaAppDetails>() {
        @Override
        public MediaAppDetails createFromParcel(Parcel in) {
            return new MediaAppDetails(in);
        }

        @Override
        public MediaAppDetails[] newArray(int size) {
            return new MediaAppDetails[size];
        }
    };
}
