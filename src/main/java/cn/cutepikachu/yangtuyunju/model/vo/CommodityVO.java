package cn.cutepikachu.yangtuyunju.model.vo;

import cn.cutepikachu.yangtuyunju.model.entity.Commodity;
import cn.hutool.core.bean.BeanUtil;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    private List<String> imgUrl;

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
     * 商家信息
     */
    private UserVO shop;

    /**
     * 创建时间
     */
    private Date createTime;

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param commodityVO
     * @return
     */
    public static Commodity voToObj(CommodityVO commodityVO) {
        if (commodityVO == null) {
            return null;
        }
        Commodity commodity = new Commodity();
        BeanUtil.copyProperties(commodityVO, commodity);
        return commodity;
    }

    /**
     * 对象转包装类
     *
     * @param commodity
     * @return
     */
    public static CommodityVO objToVo(Commodity commodity) {
        if (commodity == null) {
            return null;
        }
        CommodityVO commodityVO = new CommodityVO();
        BeanUtil.copyProperties(commodity, commodityVO);
        return commodityVO;
    }
}
