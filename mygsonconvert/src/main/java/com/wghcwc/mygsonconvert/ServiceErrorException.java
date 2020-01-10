package com.wghcwc.mygsonconvert;


import java.io.IOException;

/**
 * @author wghcwc
 */

public class ServiceErrorException extends IOException {
    public int errorCode;

    public ServiceErrorException(int errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
    }

    public ServiceErrorException(Exception e) {
        super(e);
    }
}

