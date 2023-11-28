package top.alexmmd.common.db.assembler;

import lombok.extern.slf4j.Slf4j;
import top.alexmmd.common.base.constant.BaseStatusCode;
import top.alexmmd.common.base.exception.BaseException;
import top.alexmmd.common.base.http.response.ObjectResponse;

@Slf4j
public class ObjectResponseAssembler {

    public static <T> T convert(ObjectResponse<T> response) {
        if (!BaseStatusCode.SUCCESS.getCode().equals(response.getCode())) {
            String message = String.format("class:%s rpc happen Exception:%s",
                    Thread.currentThread().getStackTrace()[2].getClass().getName(),
                    response.getMessage());
            log.error("Exception:{}, Response:{}", message, response);
            throw new BaseException(message);
        }
        return response.getData();
    }

}
