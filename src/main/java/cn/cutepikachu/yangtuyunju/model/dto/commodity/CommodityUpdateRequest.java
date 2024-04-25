package cn.cutepikachu.yangtuyunju.model.dto.commodity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class CommodityUpdateRequest implements Serializable {

    @NotNull
    @Min(1)
    private Long id;

    @Length(min = 1, max = 32)
    private String name;

    @Range(min = 0, max = 1)
    private Integer isSale;

    private List<String> imgUrl;

    @Length(min = 10, max = 2048)
    private String detail;

    @DecimalMin("0")
    private BigDecimal price;

    @Serial
    private static final long serialVersionUID = 1L;
}
