package cn.cutepikachu.yangtuyunju.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class CommodityVO implements Serializable {
    /**
     * id
     */
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

    @Serial
    private static final long serialVersionUID = 1L;
}
