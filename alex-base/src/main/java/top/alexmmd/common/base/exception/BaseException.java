package top.alexmmd.common.base.exception;

import top.alexmmd.common.base.constant.BaseStatusCode;
import top.alexmmd.common.base.constant.StatusCode;

/**
 * @author wangyonghui
 * @date 2022年12月28日 15:18:00
 */
public class BaseException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private StatusCode code = BaseStatusCode.INTERNAL_SERVER_ERROR;

    public int getCode() {
        return code.getCode();
    }

    public void setCode(StatusCode code) {
        this.code = code;
    }

    public StatusCode getStatusCode() {
        return code;
    }

    public BaseException() {
    }

    public BaseException(String message, StatusCode code) {
        super(message);
        this.code = code;
    }

    public BaseException(StatusCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public BaseException(StatusCode code, Throwable cause) {
        super(code.getMessage(), cause);
        this.code = code;
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
