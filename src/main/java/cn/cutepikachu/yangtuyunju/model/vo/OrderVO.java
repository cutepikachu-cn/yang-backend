package cn.cutepikachu.yangtuyunju.model.vo;

import cn.cutepikachu.yangtuyunju.model.entity.Order;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class OrderVO implements Serializable {
    /**
     * 订单ID
     */
    private Long id;

    /**
     * 下单用户ID
     */
    private Long userId;

    /**
     * 商品ID
     */
    private Long commodityId;

    /**
     * 订单状态: unpaid-未支付, paid-已支付, shipped-已发货, completed-已完成, cancelled-已取消, after_sale-售后中
     */
    private String status;

    /**
     * 订单支付时间
     */
    private Date payTime;

    /**
     * 订单退款时间
     */
    private Date refundTime;

    /**
     * 订单支付方式: alipay-支付宝, wechat-微信支付, bank_card-银行卡
     */
    private String paymentMethod;

    /**
     * 订单创建时间
     */
    private Date createTime;

    /**
     * 订单更新时间
     */
    private Date updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param orderVO
     * @return
     */
    public static Order voToObj(OrderVO orderVO) {
        if (orderVO == null) {
            return null;
        }
        Order order = new Order();
        BeanUtil.copyProperties(orderVO, order);
        return order;
    }

    /**
     * 对象转包装类
     *
     * @param order
     * @return
     */
    public static OrderVO objToVo(Order order) {
        if (order == null) {
            return null;
        }
        OrderVO orderVO = new OrderVO();
        BeanUtil.copyProperties(order, orderVO);
        return orderVO;
    }
}
