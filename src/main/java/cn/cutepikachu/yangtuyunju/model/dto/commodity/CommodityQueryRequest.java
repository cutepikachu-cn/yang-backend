package cn.cutepikachu.yangtuyunju.model.dto.commodity;

import cn.cutepikachu.yangtuyunju.common.PageRequest;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommodityQueryRequest extends PageRequest implements Serializable {

    /**
     * 商家 id
     */
    @Min(1)
    private Long shopId;

    @Min(0)
    private BigDecimal minPrice;

    @Min(0)
    private BigDecimal maxPrice;

    @Serial
    private static final long serialVersionUID = 1L;
}
