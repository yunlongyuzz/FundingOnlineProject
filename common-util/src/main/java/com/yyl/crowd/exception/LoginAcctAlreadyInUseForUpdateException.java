package com.yyl.crowd.exception;

/**
 * 保存或者更新Admin时，如果检测到登陆账号重复抛出这个异常
 */
public class LoginAcctAlreadyInUseForUpdateException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public LoginAcctAlreadyInUseForUpdateException() {
    }

    public LoginAcctAlreadyInUseForUpdateException(String message) {
        super(message);
    }

    public LoginAcctAlreadyInUseForUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctAlreadyInUseForUpdateException(Throwable cause) {
        super(cause);
    }

    public LoginAcctAlreadyInUseForUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
