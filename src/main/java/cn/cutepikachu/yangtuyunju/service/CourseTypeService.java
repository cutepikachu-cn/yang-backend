package cn.cutepikachu.yangtuyunju.service;

import cn.cutepikachu.yangtuyunju.common.PageRequest;
import cn.cutepikachu.yangtuyunju.model.entity.CourseType;
import cn.cutepikachu.yangtuyunju.model.vo.CourseTypeVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 28944
 * @description 针对表【course_type(课程类型)】的数据库操作Service
 * @createDate 2024-04-22 16:45:06
 */
public interface CourseTypeService extends IService<CourseType> {
    /**
     * 获取查询条件
     *
     * @param pageRequest
     * @return
     */
    LambdaQueryWrapper<CourseType> getLambdaQueryWrapper(PageRequest pageRequest);


    /**
     * 获取课程类型封装
     *
     * @param courseType
     * @return
     */
    CourseTypeVO getCourseTypeVO(CourseType courseType);

    /**
     * 分页获取课程类型封装
     *
     * @param courseTypePage
     * @return
     */
    Page<CourseTypeVO> getCourseTypeVOPage(Page<CourseType> courseTypePage);
}
