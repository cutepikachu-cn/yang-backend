package cn.cutepikachu.yangtuyunju.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 订单表
 *
 * @TableName order
 */
@TableName(value = "order")
@Data
public class Order implements Serializable {
    /**
     * 订单ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

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
     * 数量
     */
    private Long quantity;

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

    /**
     * 是否删除
     */
    private Integer isDelete;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
