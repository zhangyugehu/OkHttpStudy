package com.thssh.okhttpstudy.listener;

/**
 * Created by zhang on 2016/11/14.
 */

public class DisposeDataHandler {
    public DisposeDataListener listener;
    public Class<?> clazz;

    public DisposeDataHandler(DisposeDataListener listener) {
        this.listener = listener;
    }

    public DisposeDataHandler(DisposeDataListener listener, Class<?> clazz) {
        this.listener = listener;
        this.clazz = clazz;
    }
}
