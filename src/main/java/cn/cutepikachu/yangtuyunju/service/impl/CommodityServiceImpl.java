package cn.cutepikachu.yangtuyunju.service.impl;

import cn.cutepikachu.yangtuyunju.mapper.CommodityMapper;
import cn.cutepikachu.yangtuyunju.model.dto.commodity.CommodityQueryRequest;
import cn.cutepikachu.yangtuyunju.model.entity.Commodity;
import cn.cutepikachu.yangtuyunju.model.entity.User;
import cn.cutepikachu.yangtuyunju.model.enums.SortOrder;
import cn.cutepikachu.yangtuyunju.model.vo.CommodityVO;
import cn.cutepikachu.yangtuyunju.model.vo.UserVO;
import cn.cutepikachu.yangtuyunju.service.CommodityService;
import cn.cutepikachu.yangtuyunju.service.UserService;
import cn.cutepikachu.yangtuyunju.util.SqlUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 28944
 * @description 针对表【commodity(商品)】的数据库操作Service实现
 * @createDate 2024-04-20 23:24:05
 */
@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity>
        implements CommodityService {

    @Resource
    UserService userService;

    @Override
    public LambdaQueryWrapper<Commodity> getLambdaQueryWrapper(CommodityQueryRequest commodityQueryRequest) {
        LambdaQueryWrapper<Commodity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (commodityQueryRequest == null) {
            return lambdaQueryWrapper;
        }
        Long shopId = commodityQueryRequest.getShopId();
        lambdaQueryWrapper.eq(ObjUtil.isNotEmpty(shopId), Commodity::getUserId, shopId);

        BigDecimal minPrice = commodityQueryRequest.getMinPrice();
        BigDecimal maxPrice = commodityQueryRequest.getMaxPrice();
        lambdaQueryWrapper.ge(ObjUtil.isNotEmpty(minPrice), Commodity::getPrice, minPrice);
        if (ObjUtil.isNotEmpty(maxPrice) && maxPrice.compareTo(minPrice) > 0) {
            lambdaQueryWrapper.le(Commodity::getPrice, maxPrice);
        }

        String sortField = commodityQueryRequest.getSortField();
        String sortOrder = commodityQueryRequest.getSortOrder();
        if (SqlUtils.validSortField(sortField)) {
            boolean isAsc = sortOrder.equals(SortOrder.SORT_ORDER_ASC.getValue());
            switch (sortField.toLowerCase()) {
                case "price" -> lambdaQueryWrapper.orderBy(true, isAsc, Commodity::getPrice);
                case "visitnum" -> lambdaQueryWrapper.orderBy(true, isAsc, Commodity::getVisitNum);
                case "sharenum" -> lambdaQueryWrapper.orderBy(true, isAsc, Commodity::getShareNum);
                case "hot" -> lambdaQueryWrapper.orderBy(true, isAsc, Commodity::getHot);
                case "createtime" -> lambdaQueryWrapper.orderBy(true, isAsc, Commodity::getCreateTime);
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public CommodityVO getCommodityVO(Commodity commodity) {
        CommodityVO commodityVO = CommodityVO.objToVo(commodity);
        // 1.关联查询商家信息
        Long userId = commodity.getUserId();
        User user = null;
        if (userId != null) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        commodityVO.setShop(userVO);
        return commodityVO;
    }

    @Override
    public Page<CommodityVO> getCommodityVOPage(Page<Commodity> commodityPage) {
        List<Commodity> commodityList = commodityPage.getRecords();
        Page<CommodityVO> commodityVOPage = new Page<>(commodityPage.getCurrent(), commodityPage.getSize(), commodityPage.getTotal());
        if (CollUtil.isEmpty(commodityList)) {
            return commodityVOPage;
        }
        // 1.关联查询商家信息
        Set<Long> userIdSet = commodityList.stream().map(Commodity::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<CommodityVO> commodityVOList = commodityList.stream().map(commodity -> {
            CommodityVO commodityVO = CommodityVO.objToVo(commodity);
            Long userId = commodityVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            commodityVO.setShop(userService.getUserVO(user));
            return commodityVO;
        }).collect(Collectors.toList());
        commodityVOPage.setRecords(commodityVOList);
        return commodityVOPage;
    }
}




