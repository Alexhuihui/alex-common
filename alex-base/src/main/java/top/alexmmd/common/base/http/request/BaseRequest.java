package top.alexmmd.common.base.http.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
@Schema
public class BaseRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = -5416906095051213469L;

    @Schema(description = "分页大小")
    Integer pageSize = 20;

    @Schema(description = "当前页数")
    Integer current = 1;

}
