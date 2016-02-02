package com.wellingtonmb88.aprovado.entity;


import android.net.Uri;

public class User {

    private String mName;
    private String mEmail;
    private String mPictureUri;

    public User(String name, String email, String pictureUri){
        this.mName = name;
        this.mEmail = email;
        this.mPictureUri = pictureUri;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPictureUri() {
        return mPictureUri;
    }

    public void setPictureUri(String mPictureUri) {
        this.mPictureUri = mPictureUri;
    }
}
