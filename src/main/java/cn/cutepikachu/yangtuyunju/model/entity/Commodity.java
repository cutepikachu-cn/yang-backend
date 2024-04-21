package cn.cutepikachu.yangtuyunju.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品
 * @TableName commodity
 */
@TableName(value = "commodity")
@Data
public class Commodity implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商品用户（羊场主）id
     */
    private Long userId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 是否上架：0 未上架/1 已上架
     */
    private Integer isSale;

    /**
     * 商品图url
     */
    private String imgUrl;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 商品库存
     */
    private Long stock;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 商品访问次数
     */
    private Long visitNum;

    /**
     * 商品分享次数
     */
    private Long shareNum;

    /**
     * 商品热度
     */
    private BigDecimal hot;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
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
