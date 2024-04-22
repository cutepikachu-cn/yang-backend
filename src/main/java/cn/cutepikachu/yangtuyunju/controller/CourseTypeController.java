package cn.cutepikachu.yangtuyunju.controller;

import cn.cutepikachu.yangtuyunju.annotation.AuthCheck;
import cn.cutepikachu.yangtuyunju.common.BaseResponse;
import cn.cutepikachu.yangtuyunju.common.PageRequest;
import cn.cutepikachu.yangtuyunju.common.ResponseCode;
import cn.cutepikachu.yangtuyunju.model.entity.CourseType;
import cn.cutepikachu.yangtuyunju.model.enums.UserRole;
import cn.cutepikachu.yangtuyunju.model.vo.CourseTypeVO;
import cn.cutepikachu.yangtuyunju.service.CourseTypeService;
import cn.cutepikachu.yangtuyunju.util.ResultUtils;
import cn.cutepikachu.yangtuyunju.util.ThrowUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@RestController
@RequestMapping("/course-type")
@Slf4j
public class CourseTypeController {

    @Resource
    CourseTypeService courseTypeService;

    @PostMapping("/page")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<Page<CourseType>> pageCourseType(@RequestBody @Valid PageRequest pageRequest) {
        int current = pageRequest.getCurrent();
        int pageSize = pageRequest.getPageSize();
        LambdaQueryWrapper<CourseType> lambdaQueryWrapper = courseTypeService.getLambdaQueryWrapper(pageRequest);
        Page<CourseType> courseTypePage = courseTypeService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        return ResultUtils.success(courseTypePage);
    }

    @PostMapping("/page/vo")
    public BaseResponse<Page<CourseTypeVO>> pageCourseTypeVO(@RequestBody @Valid PageRequest pageRequest) {
        int current = pageRequest.getCurrent();
        int pageSize = pageRequest.getPageSize();
        LambdaQueryWrapper<CourseType> lambdaQueryWrapper = courseTypeService.getLambdaQueryWrapper(pageRequest);
        Page<CourseType> courseTypePage = courseTypeService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        Page<CourseTypeVO> courseTypeVOPage = courseTypeService.getCourseTypeVOPage(courseTypePage);
        return ResultUtils.success(courseTypeVOPage);
    }

    @GetMapping("/get/vo")
    public BaseResponse<CourseTypeVO> getCourseTypeVO(@RequestParam long id) {
        CourseType courseType = courseTypeService.getById(id);
        ThrowUtils.throwIf(courseType == null, ResponseCode.NOT_FOUND_ERROR, "课程类型不存在");
        CourseTypeVO courseTypeVO = courseTypeService.getCourseTypeVO(courseType);
        return ResultUtils.success(courseTypeVO);
    }
}
