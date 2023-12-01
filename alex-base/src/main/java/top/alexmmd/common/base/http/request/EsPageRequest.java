package top.alexmmd.common.base.http.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangyonghui
 * @since 2023年05月18日 10:48:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema
public class EsPageRequest extends BaseRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -5416906095051213469L;

    @Schema(description = "分页大小")
    Integer size = 20;

    @Schema(description = "当前页数")
    Integer from = 0;

    public static Integer getEsCurrent(Integer current, Integer pageSize) {
        if (current == 1) {
            return 0;
        }
        current = current - 1;
        return (current * pageSize);
    }

}

