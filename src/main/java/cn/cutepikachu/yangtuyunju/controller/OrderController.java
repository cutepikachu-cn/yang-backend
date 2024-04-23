package cn.cutepikachu.yangtuyunju.controller;

import cn.cutepikachu.yangtuyunju.annotation.AuthCheck;
import cn.cutepikachu.yangtuyunju.common.BaseResponse;
import cn.cutepikachu.yangtuyunju.common.ResponseCode;
import cn.cutepikachu.yangtuyunju.exception.BusinessException;
import cn.cutepikachu.yangtuyunju.model.dto.order.OrderQueryRequest;
import cn.cutepikachu.yangtuyunju.model.entity.Order;
import cn.cutepikachu.yangtuyunju.model.entity.User;
import cn.cutepikachu.yangtuyunju.model.enums.UserRole;
import cn.cutepikachu.yangtuyunju.model.vo.OrderVO;
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

    @PostMapping("/page")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<Page<Order>> pageOrder(@RequestBody @Valid OrderQueryRequest orderQueryRequest) {
        int current = orderQueryRequest.getCurrent();
        int pageSize = orderQueryRequest.getPageSize();
        LambdaQueryWrapper<Order> lambdaQueryWrapper = orderService.getLambdaQueryWrapper(orderQueryRequest);
        Page<Order> orderPage = orderService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        return ResultUtils.success(orderPage);
    }

    @PostMapping("/page/vo/farm")
    @AuthCheck(mustRole = {UserRole.FARM, UserRole.ADMIN})
    public BaseResponse<Page<OrderVO>> pageOrderVOFarm(@RequestBody @Valid OrderQueryRequest orderQueryRequest, HttpServletRequest request) {
        int current = orderQueryRequest.getCurrent();
        int pageSize = orderQueryRequest.getPageSize();
        User loginUser = userService.getLoginUser(request);
        orderQueryRequest.setShopId(loginUser.getId());

        LambdaQueryWrapper<Order> lambdaQueryWrapper = orderService.getLambdaQueryWrapper(orderQueryRequest);
        Page<Order> orderPage = orderService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        Page<OrderVO> orderVOPage = orderService.getOrderVOPage(orderPage);
        return ResultUtils.success(orderVOPage);
    }

    @PostMapping("/page/vo/user")
    @AuthCheck(mustRole = {UserRole.USER, UserRole.ADMIN})
    public BaseResponse<Page<OrderVO>> pageOrderVOUser(@RequestBody @Valid OrderQueryRequest orderQueryRequest, HttpServletRequest request) {
        int current = orderQueryRequest.getCurrent();
        int pageSize = orderQueryRequest.getPageSize();
        User loginUser = userService.getLoginUser(request);
        orderQueryRequest.setUserId(loginUser.getId());

        // 不允许查询的字段
        orderQueryRequest.setCommodityId(null);
        orderQueryRequest.setShopId(null);

        LambdaQueryWrapper<Order> lambdaQueryWrapper = orderService.getLambdaQueryWrapper(orderQueryRequest);
        Page<Order> orderPage = orderService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        Page<OrderVO> orderVOPage = orderService.getOrderVOPage(orderPage);
        return ResultUtils.success(orderVOPage);
    }

    @GetMapping("/get/vo/farm")
    @AuthCheck(mustRole = {UserRole.FARM, UserRole.ADMIN})
    public BaseResponse<OrderVO> getOrderVOByIdFarm(@RequestParam long id, HttpServletRequest request) {
        Order order = orderService.getById(id);
        ThrowUtils.throwIf(order == null, ResponseCode.NOT_FOUND_ERROR, "订单不存在");
        User loginUser = userService.getLoginUser(request);
        // 只能查询本商店的订单
        if (!order.getShopId().equals(loginUser.getId())) {
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        }
        OrderVO orderVO = orderService.getOrderVO(order);
        return ResultUtils.success(orderVO);
    }

    @GetMapping("/get/vo/user")
    @AuthCheck(mustRole = {UserRole.USER, UserRole.ADMIN})
    public BaseResponse<OrderVO> getOrderVOByIdUser(@RequestParam long id, HttpServletRequest request) {
        Order order = orderService.getById(id);
        ThrowUtils.throwIf(order == null, ResponseCode.NOT_FOUND_ERROR, "订单不存在");
        User loginUser = userService.getLoginUser(request);
        // 只能查询用户自己创建的订单
        if (!order.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ResponseCode.NOT_FOUND_ERROR);
        }
        OrderVO orderVO = orderService.getOrderVO(order);
        return ResultUtils.success(orderVO);
    }
}
