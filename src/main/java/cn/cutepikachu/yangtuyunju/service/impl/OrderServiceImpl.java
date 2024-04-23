package cn.cutepikachu.yangtuyunju.service.impl;

import cn.cutepikachu.yangtuyunju.mapper.OrderMapper;
import cn.cutepikachu.yangtuyunju.model.dto.order.OrderQueryRequest;
import cn.cutepikachu.yangtuyunju.model.entity.Commodity;
import cn.cutepikachu.yangtuyunju.model.entity.Order;
import cn.cutepikachu.yangtuyunju.model.enums.SortOrder;
import cn.cutepikachu.yangtuyunju.model.vo.CommodityVO;
import cn.cutepikachu.yangtuyunju.model.vo.OrderVO;
import cn.cutepikachu.yangtuyunju.service.CommodityService;
import cn.cutepikachu.yangtuyunju.service.OrderService;
import cn.cutepikachu.yangtuyunju.util.SqlUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 28944
 * @description 针对表【order(订单表)】的数据库操作Service实现
 * @createDate 2024-04-23 10:44:32
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {

    @Resource
    CommodityService commodityService;

    @Override
    public LambdaQueryWrapper<Order> getLambdaQueryWrapper(OrderQueryRequest orderQueryRequest) {
        LambdaQueryWrapper<Order> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        Long userId = orderQueryRequest.getUserId();
        Long commodityId = orderQueryRequest.getCommodityId();
        String status = orderQueryRequest.getStatus();
        Date createTimeBegin = orderQueryRequest.getCreateTimeBegin();
        Date createTimeEnd = orderQueryRequest.getCreateTimeEnd();
        Date updateTimeBegin = orderQueryRequest.getUpdateTimeBegin();
        Date updateTimeEnd = orderQueryRequest.getUpdateTimeEnd();
        String sortField = orderQueryRequest.getSortField();
        String sortOrder = orderQueryRequest.getSortOrder();

        lambdaQueryWrapper.eq(ObjectUtil.isNotEmpty(userId), Order::getUserId, userId);
        lambdaQueryWrapper.eq(ObjectUtil.isNotEmpty(commodityId), Order::getCommodityId, commodityId);
        lambdaQueryWrapper.eq(ObjectUtil.isNotEmpty(status), Order::getCommodityId, commodityId);

        if (ObjectUtil.isAllNotEmpty(createTimeBegin, createTimeEnd)) {
            lambdaQueryWrapper.between(Order::getCreateTime, createTimeBegin, createTimeEnd);
        }
        if (ObjectUtil.isAllNotEmpty(updateTimeBegin, updateTimeEnd)) {
            lambdaQueryWrapper.between(Order::getUpdateTime, updateTimeBegin, updateTimeEnd);
        }

        if (SqlUtils.validSortField(sortField)) {
            boolean isAsc = sortOrder.equals(SortOrder.SORT_ORDER_ASC.getValue());
            switch (sortField.toLowerCase()) {
                case "userid" -> lambdaQueryWrapper.orderBy(true, isAsc, Order::getUserId);
                case "commodityid" -> lambdaQueryWrapper.orderBy(true, isAsc, Order::getCommodityId);
                case "status" -> lambdaQueryWrapper.orderBy(true, isAsc, Order::getStatus);
                case "createtime" -> lambdaQueryWrapper.orderBy(true, isAsc, Order::getCreateTime);
                case "updatetime" -> lambdaQueryWrapper.orderBy(true, isAsc, Order::getUpdateTime);
            }
        }

        return lambdaQueryWrapper;
    }

    @Override
    public OrderVO getOrderVO(Order order) {
        OrderVO orderVO = OrderVO.objToVo(order);
        // 1.关联查询商品（包含商家）信息
        Long commodityId = orderVO.getCommodityId();
        Commodity commodity = commodityService.getById(commodityId);
        CommodityVO commodityVO = commodityService.getCommodityVO(commodity);
        orderVO.setCommodity(commodityVO);
        return orderVO;
    }

    @Override
    public Page<OrderVO> getOrderVOPage(Page<Order> orderPage) {
        List<Order> orderList = orderPage.getRecords();
        Page<OrderVO> orderVOPage = new Page<>(orderPage.getCurrent(), orderPage.getSize(), orderPage.getTotal());
        if (CollUtil.isEmpty(orderList)) {
            return orderVOPage;
        }
        // 1.关联查询商品（包含商家）信息
        Set<Long> commodityIdSet = orderList.stream().map(Order::getCommodityId).collect(Collectors.toSet());
        Map<Long, List<Commodity>> commodityIdCommodityListMap = commodityService.listByIds(commodityIdSet).stream()
                .collect(Collectors.groupingBy(Commodity::getId));
        // 填充信息
        List<OrderVO> orderVOList = orderList.stream().map(order -> {
            OrderVO orderVO = OrderVO.objToVo(order);
            Long commodityId = orderVO.getCommodityId();
            Commodity commodity = null;
            if (commodityIdCommodityListMap.containsKey(commodityId)) {
                commodity = commodityIdCommodityListMap.get(commodityId).get(0);
            }
            orderVO.setCommodity(commodityService.getCommodityVO(commodity));
            return orderVO;
        }).toList();
        orderVOPage.setRecords(orderVOList);
        return orderVOPage;
    }
}




