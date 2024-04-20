package cn.cutepikachu.yangtuyunju.service;

import cn.cutepikachu.yangtuyunju.model.entity.Commodity;
import cn.cutepikachu.yangtuyunju.model.vo.CommodityVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 28944
 * @description 针对表【commodity(商品)】的数据库操作Service
 * @createDate 2024-04-20 23:24:05
 */
public interface CommodityService extends IService<Commodity> {
    /**
     * 获取商品封装
     *
     * @param commodity
     * @return
     */
    CommodityVO getCommodityVO(Commodity commodity);

    /**
     * 分页获取商品封装
     *
     * @param commodityPage
     * @return
     */
    Page<CommodityVO> getCommodityVOPage(Page<Commodity> commodityPage);
}
