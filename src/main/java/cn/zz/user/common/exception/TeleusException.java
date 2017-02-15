package cn.zz.user.common.exception;


import cn.zz.user.exception.BaseErrorCode;

public abstract class TeleusException {

    private String exceptMessage;

    private String traceInfo;

    private BaseErrorCode errorCode;

    public TeleusException(String exceptMessage, String traceInfo, BaseErrorCode errorCode) {
        this.exceptMessage = exceptMessage;
        this.traceInfo = traceInfo;
        this.errorCode = errorCode;
    }

    abstract void RecordExceptionLog();

    public String getExceptMessage() {
        return exceptMessage;
    }

    public String getTraceInfo() {
        return traceInfo;
    }

    public BaseErrorCode getErrorCode() {
        return errorCode;
    }
}
