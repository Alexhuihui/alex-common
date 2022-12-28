package top.alexmmd.common.base.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wangyonghui
 * @date 2022年12月28日 15:19:00
 */
@Getter
@AllArgsConstructor
public enum BaseStatusCode implements StatusCode {

    /**
     * 成功
     */
    SUCCESS(200, "请求已经成功处理"),
    /**
     * 服务内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    BAD_REQUEST(400, "请求错误，请修正请求"),

    PARAM_INVALID(120401, "请求参数无效"),
    NOT_FOUND(404, "资源未找到"),

    UNAUTHORIZED(401,"未认证（签名错误）"),

    PERMISSION_ERROR(403,"没有访问权限"),
    ;


    private final Integer code;

    private final String message;

}

