package com.ondrejruttkay.weather.event;

/**
 * Created by Onko on 5/24/2015.
 */
public abstract class ErrorEventBase {
    private String mMessage;
    private boolean mIsNetworkError;

    protected ErrorEventBase(String message, boolean isNetworkError) {
        mMessage = message;
        mIsNetworkError = isNetworkError;
    }


    public String getMessage() {
        return mMessage;
    }


    public boolean isNetworkError() {
        return mIsNetworkError;
    }
}
