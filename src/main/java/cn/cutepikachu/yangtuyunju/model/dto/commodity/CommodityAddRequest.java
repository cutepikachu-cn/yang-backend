package cn.cutepikachu.yangtuyunju.model.dto.commodity;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class CommodityAddRequest implements Serializable {

    @NotBlank
    @Length(max = 32)
    private String name;

    @NotNull
    @Range(min = 0, max = 1)
    private Integer isSale;

    @NotBlank
    @Length(max = 512)
    private String imgUrl;

    @NotBlank
    @Length(min = 10, max = 2048)
    private String detail;

    @NotNull
    @Min(1)
    private Long stock;

    @NotNull
    @DecimalMin("0")
    private BigDecimal price;

    @Serial
    private static final long serialVersionUID = 1L;
}
