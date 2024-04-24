package cn.cutepikachu.yangtuyunju.model.dto.order;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class OrderAddRequest implements Serializable {

    @Min(1)
    private Long commodityId;

    @Min(1)
    private Long quantity;

    @Serial
    private static final long serialVersionUID = 1L;
}
