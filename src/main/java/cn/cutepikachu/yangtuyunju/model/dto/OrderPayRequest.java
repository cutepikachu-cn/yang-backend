package cn.cutepikachu.yangtuyunju.model.dto;

import cn.cutepikachu.yangtuyunju.model.enums.OrderPayMethod;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class OrderPayRequest implements Serializable {

    /**
     * 订单ID
     */
    @NotNull
    @Min(1)
    private Long orderId;

    /**
     * 支付方式
     */
    @NotNull
    private String orderPayMethod;

    @AssertTrue
    boolean isValidOrderPayMethod() {
        return OrderPayMethod.getEnumByValue(orderPayMethod) != null;
    }

    @Serial
    private static final long serialVersionUID = 1L;
}
