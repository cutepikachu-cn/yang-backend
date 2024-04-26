package cn.cutepikachu.yangtuyunju.controller;

import cn.cutepikachu.yangtuyunju.annotation.AuthCheck;
import cn.cutepikachu.yangtuyunju.common.BaseResponse;
import cn.cutepikachu.yangtuyunju.common.DeleteRequest;
import cn.cutepikachu.yangtuyunju.common.ResponseCode;
import cn.cutepikachu.yangtuyunju.exception.BusinessException;
import cn.cutepikachu.yangtuyunju.model.dto.commodity.CommodityAddRequest;
import cn.cutepikachu.yangtuyunju.model.dto.commodity.CommodityQueryRequest;
import cn.cutepikachu.yangtuyunju.model.dto.commodity.CommodityUpdateRequest;
import cn.cutepikachu.yangtuyunju.model.entity.Commodity;
import cn.cutepikachu.yangtuyunju.model.entity.User;
import cn.cutepikachu.yangtuyunju.model.enums.UserRole;
import cn.cutepikachu.yangtuyunju.model.vo.CommodityVO;
import cn.cutepikachu.yangtuyunju.service.CommodityService;
import cn.cutepikachu.yangtuyunju.service.UserService;
import cn.cutepikachu.yangtuyunju.util.ResultUtils;
import cn.cutepikachu.yangtuyunju.util.ThrowUtils;
import cn.hutool.core.bean.BeanUtil;
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
@RequestMapping("/commodity")
@Slf4j
public class CommodityController {

    @Resource
    CommodityService commodityService;


    @Resource
    UserService userService;

    /**
     * 创建（仅羊场主）
     *
     * @param commodityAddRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = UserRole.FARM)
    @PostMapping("/add")
    public BaseResponse<Long> addCommodity(@RequestBody @Valid CommodityAddRequest commodityAddRequest, HttpServletRequest request) {
        Commodity commodity = new Commodity();
        User loginUser = userService.getLoginUser(request);
        commodity.setUserId(loginUser.getId());
        BeanUtil.copyProperties(commodityAddRequest, commodity);
        boolean result = commodityService.save(commodity);
        ThrowUtils.throwIf(!result, ResponseCode.OPERATION_ERROR, "添加商品失败");
        Long commodityId = commodity.getId();
        return ResultUtils.success(commodityId);
    }

    /**
     * 删除（仅羊场主）
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = {UserRole.FARM, UserRole.ADMIN})
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCommodity(@RequestBody @Valid DeleteRequest deleteRequest, HttpServletRequest request) {
        long commodityId = deleteRequest.getId();
        Commodity oldCommodity = commodityService.getById(commodityId);
        // 商品是否存在
        ThrowUtils.throwIf(oldCommodity == null, ResponseCode.NOT_FOUND_ERROR, "商品不存在");
        User user = userService.getLoginUser(request);
        // 仅羊场主自己和管理员可删除
        if (!oldCommodity.getUserId().equals(user.getId())) {
            throw new BusinessException(ResponseCode.NO_AUTH_ERROR);
        }
        boolean result = commodityService.removeById(commodityId);
        ThrowUtils.throwIf(!result, ResponseCode.OPERATION_ERROR, "删除商品失败");
        return ResultUtils.success(true);
    }

    /**
     * 更新（仅羊场主）
     *
     * @param commodityUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = {UserRole.FARM, UserRole.ADMIN})
    public BaseResponse<Boolean> updateCommodity(@RequestBody @Valid CommodityUpdateRequest commodityUpdateRequest, HttpServletRequest request) {
        // 是否存在
        long commodityId = commodityUpdateRequest.getId();
        Commodity oldCommodity = commodityService.getById(commodityId);
        ThrowUtils.throwIf(oldCommodity == null, ResponseCode.NOT_FOUND_ERROR, "商品不存在");
        // 仅羊场主自己和管理员可更新
        User user = userService.getLoginUser(request);
        if (!oldCommodity.getUserId().equals(user.getId())) {
            throw new BusinessException(ResponseCode.NO_AUTH_ERROR);
        }

        Commodity commodity = new Commodity();
        BeanUtil.copyProperties(commodityUpdateRequest, commodity);
        boolean result = commodityService.updateById(commodity);
        ThrowUtils.throwIf(!result, ResponseCode.OPERATION_ERROR, "更新商品信息失败");
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<CommodityVO> getCommodityVOById(@RequestParam long id) {
        Commodity commodity = commodityService.getById(id);
        ThrowUtils.throwIf(commodity == null, ResponseCode.NOT_FOUND_ERROR, "商品不存在");
        CommodityVO commodityVO = commodityService.getCommodityVO(commodity);
        return ResultUtils.success(commodityVO);
    }

    /**
     * 分页获取列表（仅羊场主/管理员）
     *
     * @param commodityQueryRequest
     * @return
     */
    @PostMapping("/page")
    @AuthCheck(mustRole = {UserRole.FARM, UserRole.ADMIN})
    public BaseResponse<Page<Commodity>> pageCommodity(@RequestBody @Valid CommodityQueryRequest commodityQueryRequest) {
        long current = commodityQueryRequest.getCurrent();
        long pageSize = commodityQueryRequest.getPageSize();
        LambdaQueryWrapper<Commodity> lambdaQueryWrapper = commodityService.getLambdaQueryWrapper(commodityQueryRequest);
        Page<Commodity> commodityPage = commodityService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        return ResultUtils.success(commodityPage);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param commodityQueryRequest
     * @return
     */
    @PostMapping("/page/vo")
    public BaseResponse<Page<CommodityVO>> pageCommodityVO(@RequestBody @Valid CommodityQueryRequest commodityQueryRequest) {
        long current = commodityQueryRequest.getCurrent();
        long pageSize = commodityQueryRequest.getPageSize();
        LambdaQueryWrapper<Commodity> lambdaQueryWrapper = commodityService.getLambdaQueryWrapper(commodityQueryRequest);
        Page<Commodity> commodityPage = commodityService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        Page<CommodityVO> commodityVOPage = commodityService.getCommodityVOPage(commodityPage);
        return ResultUtils.success(commodityVOPage);
    }

    /**
     * 分页获取当前羊场主创建的商品列表
     *
     * @param commodityQueryRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = UserRole.FARM)
    @PostMapping("/page/vo/self")
    public BaseResponse<Page<CommodityVO>> pageSelfCommodityVO(@RequestBody @Valid CommodityQueryRequest commodityQueryRequest,
                                                               HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        commodityQueryRequest.setShopId(loginUser.getId());
        long current = commodityQueryRequest.getCurrent();
        long pageSize = commodityQueryRequest.getPageSize();
        Page<Commodity> commodityPage = commodityService.page(new Page<>(current, pageSize), commodityService.getLambdaQueryWrapper(commodityQueryRequest));
        Page<CommodityVO> commodityVOPage = commodityService.getCommodityVOPage(commodityPage);
        return ResultUtils.success(commodityVOPage);
    }
}
