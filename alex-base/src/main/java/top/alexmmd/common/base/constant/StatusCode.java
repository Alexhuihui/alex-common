package top.alexmmd.common.base.constant;

import top.alexmmd.common.base.exception.BaseException;

/**
 * @author wangyonghui
 * @since 2022年12月28日 15:17:00
 */
public interface StatusCode {

    Integer getCode();

    String getMessage();

    default BaseException toException() {
        return new BaseException(this);
    }

}
