package cn.cutepikachu.yangtuyunju.service;

import cn.cutepikachu.yangtuyunju.model.dto.course.CourseQueryRequest;
import cn.cutepikachu.yangtuyunju.model.entity.Course;
import cn.cutepikachu.yangtuyunju.model.vo.CourseVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 28944
 * @description 针对表【course(课程)】的数据库操作Service
 * @createDate 2024-04-22 16:45:06
 */
public interface CourseService extends IService<Course> {
    /**
     * 获取查询条件
     *
     * @param courseQueryRequest
     * @return
     */
    LambdaQueryWrapper<Course> getLambdaQueryWrapper(CourseQueryRequest courseQueryRequest);


    /**
     * 获取课程封装
     *
     * @param course
     * @return
     */
    CourseVO getCourseVO(Course course);

    /**
     * 分页获取课程封装
     *
     * @param coursePage
     * @return
     */
    Page<CourseVO> getCourseVOPage(Page<Course> coursePage);
}
