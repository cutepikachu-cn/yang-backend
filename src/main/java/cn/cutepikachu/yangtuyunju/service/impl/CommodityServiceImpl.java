package cn.cutepikachu.yangtuyunju.service.impl;

import cn.cutepikachu.yangtuyunju.mapper.CommodityMapper;
import cn.cutepikachu.yangtuyunju.model.entity.Commodity;
import cn.cutepikachu.yangtuyunju.model.entity.User;
import cn.cutepikachu.yangtuyunju.model.vo.CommodityVO;
import cn.cutepikachu.yangtuyunju.model.vo.UserVO;
import cn.cutepikachu.yangtuyunju.service.CommodityService;
import cn.cutepikachu.yangtuyunju.service.UserService;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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




