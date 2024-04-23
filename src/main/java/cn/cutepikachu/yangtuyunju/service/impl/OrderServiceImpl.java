package cn.cutepikachu.yangtuyunju.service.impl;

import cn.cutepikachu.yangtuyunju.mapper.OrderMapper;
import cn.cutepikachu.yangtuyunju.model.entity.Order;
import cn.cutepikachu.yangtuyunju.service.OrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 28944
 * @description 针对表【order(订单表)】的数据库操作Service实现
 * @createDate 2024-04-23 10:44:32
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {

}




