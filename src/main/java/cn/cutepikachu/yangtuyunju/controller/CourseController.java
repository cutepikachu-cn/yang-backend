package cn.cutepikachu.yangtuyunju.controller;

import cn.cutepikachu.yangtuyunju.annotation.AuthCheck;
import cn.cutepikachu.yangtuyunju.common.BaseResponse;
import cn.cutepikachu.yangtuyunju.common.ResponseCode;
import cn.cutepikachu.yangtuyunju.model.dto.course.CourseQueryRequest;
import cn.cutepikachu.yangtuyunju.model.entity.Course;
import cn.cutepikachu.yangtuyunju.model.enums.UserRole;
import cn.cutepikachu.yangtuyunju.model.vo.CourseVO;
import cn.cutepikachu.yangtuyunju.service.CourseService;
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
@RequestMapping("/course")
@Slf4j
public class CourseController {

    @Resource
    CourseService courseService;

    @PostMapping("/page")
    @AuthCheck(mustRole = UserRole.ADMIN)
    public BaseResponse<Page<Course>> pageCourse(@RequestBody @Valid CourseQueryRequest courseQueryRequest) {
        int current = courseQueryRequest.getCurrent();
        int pageSize = courseQueryRequest.getPageSize();
        LambdaQueryWrapper<Course> lambdaQueryWrapper = courseService.getLambdaQueryWrapper(courseQueryRequest);
        Page<Course> coursePage = courseService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        return ResultUtils.success(coursePage);
    }

    @PostMapping("/page/vo")
    public BaseResponse<Page<CourseVO>> pageCourseVO(@RequestBody @Valid CourseQueryRequest courseQueryRequest) {
        int current = courseQueryRequest.getCurrent();
        int pageSize = courseQueryRequest.getPageSize();
        LambdaQueryWrapper<Course> lambdaQueryWrapper = courseService.getLambdaQueryWrapper(courseQueryRequest);
        Page<Course> coursePage = courseService.page(new Page<>(current, pageSize), lambdaQueryWrapper);
        Page<CourseVO> courseVOPage = courseService.getCourseVOPage(coursePage);
        return ResultUtils.success(courseVOPage);
    }

    @GetMapping("/get/vo")
    public BaseResponse<CourseVO> getCourseVOById(@RequestParam long id) {
        Course course = courseService.getById(id);
        ThrowUtils.throwIf(course == null, ResponseCode.NOT_FOUND_ERROR, "课程不存在");
        CourseVO courseVO = courseService.getCourseVO(course);
        return ResultUtils.success(courseVO);
    }

}
