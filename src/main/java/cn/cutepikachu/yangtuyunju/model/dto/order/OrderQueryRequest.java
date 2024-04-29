package cn.cutepikachu.yangtuyunju.model.dto.order;

import cn.cutepikachu.yangtuyunju.common.PageRequest;
import cn.cutepikachu.yangtuyunju.model.enums.OrderStatus;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderQueryRequest extends PageRequest implements Serializable {

    /**
     * 下单用户ID
     */
    private Long userId;

    /**
     * 店铺ID
     */
    private Long shopId;

    /**
     * 商品ID
     */
    private Long commodityId;

    /**
     * 订单状态: unpaid-未支付, paid-已支付, shipped-已发货, completed-已完成, cancelled-已取消, after_sale-售后中
     */
    private String status;

    /**
     * 订单创建时间范围-开始
     */
    @PastOrPresent
    private Date createTimeBegin;
    /**
     * 订单创建时间范围-结束
     */
    @PastOrPresent
    private Date createTimeEnd;

    /**
     * 订单更新时间范围-开始
     */
    @PastOrPresent
    private Date updateTimeBegin;
    /**
     * 订单更新时间范围-结束
     */
    @PastOrPresent
    private Date updateTimeEnd;

    @AssertTrue
    boolean isValidOrderStatus() {
        return status == null || OrderStatus.getEnumByValue(status) != null;
    }

    @AssertTrue
    boolean isValidCreateTimeRange() {
        if (createTimeBegin == null || createTimeEnd == null) {
            createTimeBegin = createTimeEnd = null;
            return true;
        }
        return createTimeEnd.after(createTimeBegin);
    }

    @AssertTrue
    boolean isValidUpdateTimeRange() {
        if (updateTimeBegin == null || updateTimeEnd == null) {
            updateTimeBegin = updateTimeEnd = null;
            return true;
        }
        return updateTimeEnd.after(updateTimeBegin);
    }

    @Serial
    private static final long serialVersionUID = 1L;
}
