package cn.cutepikachu.yangtuyunju.controller;

import cn.cutepikachu.yangtuyunju.annotation.AuthCheck;
import cn.cutepikachu.yangtuyunju.common.BaseResponse;
import cn.cutepikachu.yangtuyunju.common.ResponseCode;
import cn.cutepikachu.yangtuyunju.exception.BusinessException;
import cn.cutepikachu.yangtuyunju.model.dto.OrderPayRequest;
import cn.cutepikachu.yangtuyunju.model.dto.order.OrderAddRequest;
import cn.cutepikachu.yangtuyunju.model.dto.order.OrderQueryRequest;
import cn.cutepikachu.yangtuyunju.model.entity.Commodity;
import cn.cutepikachu.yangtuyunju.model.entity.Order;
import cn.cutepikachu.yangtuyunju.model.entity.User;
import cn.cutepikachu.yangtuyunju.model.enums.OrderPayMethod;
import cn.cutepikachu.yangtuyunju.model.enums.OrderStatus;
import cn.cutepikachu.yangtuyunju.model.enums.UserRole;
import cn.cutepikachu.yangtuyunju.model.vo.OrderVO;
import cn.cutepikachu.yangtuyunju.service.CommodityService;
import cn.cutepikachu.yangtuyunju.service.OrderService;
import cn.cutepikachu.yangtuyunju.service.UserService;
import cn.cutepikachu.yangtuyunju.util.ResultUtils;
import cn.cutepikachu.yangtuyunju.util.ThrowUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Resource
    OrderService orderService;

    @Resource
    UserService userService;

    @Resource
    CommodityService commodityService;

    @PostMapping("/add")
    @AuthCheck(mustRole = UserRole.USER)
    public BaseResponse<Long> addOrder(@RequestBody @Valid OrderAddRequest orderAddRequest, HttpServletRequest request) {
        Long commodityId = orderAddRequest.getCommodityId();
        Commodity commodity = commodityService.getById(commodityId);
        ThrowUtils.throwIf(commodity == null, ResponseCode.NOT_FOUND_ERROR, "商品不存在");

        Long quantity = orderAddRequest.getQuantity();
        User loginUser = userService.getLoginUser(request);
        Order order = new Order();

        order.setCommodityId(commodityId);
        order.setShopId(commodity.getUserId());
        order.setQuantity(quantity);
        order.setUserId(loginUser.getId());
        order.setStatus(OrderStatus.UNPAID.getValue());

        boolean result = orderService.save(order);
        ThrowUtils.throwIf(!result, ResponseCode.OPERATION_ERROR, "创建订单失败");

        return ResultUtils.success(order.getId());
    }

    @PostMapping("/pay")
    @AuthCheck(mustRole = UserRole.USER)
    public BaseResponse<Boolean> payOrder(@RequestBody @Valid OrderPayRequest orderPayRequest, HttpServletRequest request) {
        Long orderId = orderPayRequest.getOrderId();
        Order order = orderService.getById(orderId);
        ThrowUtils.throwIf(order == null, ResponseCode.NOT_FOUND_ERROR, "订单不存在");
        User loginUser = userService.getLoginUser(request);
        Long userId = order.getUserId();
        ThrowUtils.throwIf(!userId.equals(loginUser.getId()), ResponseCode.NOT_FOUND_ERROR, "订单不存在");

        String orderPayMethod = orderPayRequest.getOrderPayMethod();
        OrderPayMethod payMethod = OrderPayMethod.getEnumByValue(orderPayMethod);

        order.setStatus(OrderStatus.PAID.getValue());
        ThrowUtils.throwIf(payMethod == null, ResponseCode.OPERATION_ERROR, "支付失败");
        order.setPaymentMethod(payMethod.getValue());
        order.setPayTime(new Date());

        boolean result = orderService.updateById(order);
        ThrowUtils.throwIf(!result, ResponseCode.OPERATION_ERROR, "支付失败");

        return ResultUtils.success("支付成功");
    }

    @PostMapping("/page")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<Page<Order>> pageOrder(@RequestBody @Valid OrderQueryRequest orderQueryRequest) {
        int current = orderQueryRequest.getCurrent();
        int pageSize = orderQueryRequest.getPageSize();
        LambdaQueryWrapper<Order> lambdaQueryWrapper = orderService.getLambdaQueryWrapper(orderQueryRequest);
        Page<Order> orderPage = orderService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        return ResultUtils.success(orderPage);
    }

    @PostMapping("/page/vo")
    public BaseResponse<Page<OrderVO>> pageOrderVO(@RequestBody @Valid OrderQueryRequest orderQueryRequest, HttpServletRequest request) {
        int current = orderQueryRequest.getCurrent();
        int pageSize = orderQueryRequest.getPageSize();
        User loginUser = userService.getLoginUser(request);
        UserRole userRole = UserRole.getEnumByValue(loginUser.getUserRole());
        if (UserRole.FARM.equals(userRole)) {
            orderQueryRequest.setShopId(loginUser.getId());
        } else if (UserRole.USER.equals(userRole)) {
            orderQueryRequest.setUserId(loginUser.getId());
            // 不允许查询的字段
            orderQueryRequest.setCommodityId(null);
            orderQueryRequest.setShopId(null);
        }
        LambdaQueryWrapper<Order> lambdaQueryWrapper = orderService.getLambdaQueryWrapper(orderQueryRequest);
        Page<Order> orderPage = orderService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        Page<OrderVO> orderVOPage = orderService.getOrderVOPage(orderPage);
        return ResultUtils.success(orderVOPage);
    }

    @GetMapping("/get/vo")
    public BaseResponse<OrderVO> getOrderVOByIdFarm(@RequestParam long id, HttpServletRequest request) {
        Order order = orderService.getById(id);
        ThrowUtils.throwIf(order == null, ResponseCode.NOT_FOUND_ERROR, "订单不存在");
        User loginUser = userService.getLoginUser(request);
        UserRole userRole = UserRole.getEnumByValue(loginUser.getUserRole());
        if (UserRole.FARM.equals(userRole)) {
            // 只能查询本商店的订单
            if (!order.getShopId().equals(loginUser.getId())) {
                throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
            }
        } else if (UserRole.USER.equals(userRole)) {
            // 只能查询用户自己创建的订单
            if (!order.getUserId().equals(loginUser.getId())) {
                throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
            }
        } else {

        }

        OrderVO orderVO = orderService.getOrderVO(order);
        return ResultUtils.success(orderVO);
    }
}
