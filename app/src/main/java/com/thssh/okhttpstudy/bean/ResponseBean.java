package com.thssh.okhttpstudy.bean;

/**
 * Created by zhang on 2016/11/14.
 */

public class ResponseBean {
    private int status;
    private String message;
    private int code;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResponseBean{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
