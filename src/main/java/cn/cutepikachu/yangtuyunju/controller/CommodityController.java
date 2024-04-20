package cn.cutepikachu.yangtuyunju.controller;

import cn.cutepikachu.yangtuyunju.annotation.AuthCheck;
import cn.cutepikachu.yangtuyunju.common.BaseResponse;
import cn.cutepikachu.yangtuyunju.common.DeleteRequest;
import cn.cutepikachu.yangtuyunju.model.dto.commodity.CommodityAddRequest;
import cn.cutepikachu.yangtuyunju.model.dto.commodity.CommodityQueryRequest;
import cn.cutepikachu.yangtuyunju.model.dto.commodity.CommodityUpdateRequest;
import cn.cutepikachu.yangtuyunju.model.entity.Commodity;
import cn.cutepikachu.yangtuyunju.model.enums.UserRole;
import cn.cutepikachu.yangtuyunju.model.vo.CommodityVO;
import cn.cutepikachu.yangtuyunju.service.CommodityService;
import cn.cutepikachu.yangtuyunju.util.ResultUtils;
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
        return ResultUtils.success(-1L);
    }

    /**
     * 删除（仅羊场主）
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @AuthCheck(mustRole = UserRole.FARM)
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCommodity(@RequestBody @Valid DeleteRequest deleteRequest, HttpServletRequest request) {
        return ResultUtils.success(false);
    }

    /**
     * 更新（仅羊场主）
     *
     * @param commodityUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserRole.FARM)
    public BaseResponse<Boolean> updatePost(@RequestBody @Valid CommodityUpdateRequest commodityUpdateRequest) {
        return ResultUtils.success(false);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<CommodityVO> getCommodityVOById(@RequestParam long id, HttpServletRequest request) {
        return ResultUtils.success(new CommodityVO());
    }

    /**
     * 分页获取列表（仅羊场主）
     *
     * @param commodityQueryRequest
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserRole.FARM)
    public BaseResponse<Page<Commodity>> listCommodityByPage(@RequestBody @Valid CommodityQueryRequest commodityQueryRequest) {
        return ResultUtils.success(new Page<>());
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param commodityQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<CommodityVO>> listCommodityVOByPage(@RequestBody CommodityQueryRequest commodityQueryRequest,
                                                                 HttpServletRequest request) {
        return ResultUtils.success(new Page<>());
    }

    /**
     * 分页获取当前羊场主创建的商品列表
     *
     * @param commodityQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/self/list/page/vo")
    public BaseResponse<Page<CommodityVO>> listSelfCommodityVOByPage(@RequestBody @Valid CommodityQueryRequest commodityQueryRequest,
                                                                     HttpServletRequest request) {
        return ResultUtils.success(new Page<>());
    }
}
