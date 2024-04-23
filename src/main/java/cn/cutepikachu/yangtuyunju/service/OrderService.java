package cn.cutepikachu.yangtuyunju.service;

import cn.cutepikachu.yangtuyunju.model.dto.order.OrderQueryRequest;
import cn.cutepikachu.yangtuyunju.model.entity.Order;
import cn.cutepikachu.yangtuyunju.model.vo.OrderVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 28944
 * @description 针对表【order(订单表)】的数据库操作Service
 * @createDate 2024-04-23 10:44:32
 */
public interface OrderService extends IService<Order> {

    /**
     * 获取查询包装类
     *
     * @param orderQueryRequest
     * @return
     */
    LambdaQueryWrapper<Order> getLambdaQueryWrapper(OrderQueryRequest orderQueryRequest);


    /**
     * 获取帖子封装
     *
     * @param order
     * @return
     */
    OrderVO getOrderVO(Order order);


    /**
     * 分页获取帖子封装
     *
     * @param orderPage
     * @return
     */
    Page<OrderVO> getOrderVOPage(Page<Order> orderPage);
}
