package top.alexmmd.common.base.http.response;

import cn.hutool.core.date.DateUtil;
import lombok.Data;
import top.alexmmd.common.base.constant.StatusCode;

/**
 * @author wangyonghui
 * @date 2022年12月28日 15:27:00
 */
@Data
public class BaseResponse {

    private Integer code = 200;

    private String message = "SUCCESS";

    private String timestamp = DateUtil.now();

    public int getCode() {
        return code;
    }

    public BaseResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public BaseResponse() {
    }

    public BaseResponse code(StatusCode code) {
        this.code = code.getCode();
        return this;
    }

    public BaseResponse message(String message) {
        this.message = message;
        return this;
    }

}

