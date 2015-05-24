package com.ondrejruttkay.weather.entity.event;

/**
 * Created by Onko on 5/24/2015.
 */
public abstract class ErrorEventBase {
    private String mMessage;
    private boolean mIsNetworkError;

    protected ErrorEventBase(String message, boolean isNetworkError) {
        mMessage = message;
    }


    protected String getMessage() {
        return mMessage;
    }


    protected boolean isNetworkError() {
        return mIsNetworkError;
    }
}
