package top.alexmmd.common.base.http.response;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import top.alexmmd.common.base.constant.BaseStatusCode;
import top.alexmmd.common.base.constant.StatusCode;

/**
 * @author wangyonghui
 * @date 2022年12月28日 15:39:00
 */
@Data
public class ObjectResponse<T> extends BaseResponse {

    private T data;

    public String toString() {
        return JSONUtil.toJsonStr(this);
    }

    public static <T> ObjectResponse<T> success(T data, String message) {
        ObjectResponse<T> result = new ObjectResponse<>();
        result.setCode(BaseStatusCode.SUCCESS.getCode());
        result.setData(data);
        result.setMessage(message);
        return result;
    }

    public static <T> ObjectResponse<T> success(T data) {
        ObjectResponse<T> result = new ObjectResponse<>();
        result.setCode(BaseStatusCode.SUCCESS.getCode());
        result.setData(data);
        result.setMessage(BaseStatusCode.SUCCESS.getMessage());
        return result;
    }

    public static ObjectResponse<String> success() {
        ObjectResponse<String> result = new ObjectResponse<>();
        result.setCode(BaseStatusCode.SUCCESS.getCode());
        result.setMessage(BaseStatusCode.SUCCESS.getMessage());
        return result;
    }

    /**
     * 无数据失败返回
     *
     * @param code    返回码
     * @param message 消息
     * @return ignore
     */
    public static ObjectResponse<String> failed(StatusCode code, String message) {
        ObjectResponse<String> result = new ObjectResponse<>();
        result.setCode(code.getCode());
        result.setData(code.getMessage());
        result.setMessage(message);

        return result;
    }

    public static ObjectResponse<String> failed(StatusCode code) {
        ObjectResponse<String> result = new ObjectResponse<>();
        result.setCode(code.getCode());
        result.setData(code.getMessage());
        result.setMessage(code.getMessage());

        return result;
    }

    public static <T> ObjectResponse<T> failed(StatusCode code, T data, String message) {
        ObjectResponse<T> result = new ObjectResponse<>();
        result.setCode(code.getCode());
        result.setData(data);
        result.setMessage(message);
        return result;
    }

    public static <T> ObjectResponse<T> failed(StatusCode code, T data) {
        ObjectResponse<T> result = new ObjectResponse<>();
        result.setCode(code.getCode());
        result.setData(data);
        result.setMessage(code.getMessage());
        return result;
    }
}
