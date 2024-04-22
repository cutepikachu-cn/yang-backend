package cn.cutepikachu.yangtuyunju.service.impl;

import cn.cutepikachu.yangtuyunju.mapper.CourseMapper;
import cn.cutepikachu.yangtuyunju.model.dto.course.CourseQueryRequest;
import cn.cutepikachu.yangtuyunju.model.entity.Course;
import cn.cutepikachu.yangtuyunju.model.entity.CourseType;
import cn.cutepikachu.yangtuyunju.model.enums.SortOrder;
import cn.cutepikachu.yangtuyunju.model.vo.CourseTypeVO;
import cn.cutepikachu.yangtuyunju.model.vo.CourseVO;
import cn.cutepikachu.yangtuyunju.service.CourseService;
import cn.cutepikachu.yangtuyunju.service.CourseTypeService;
import cn.cutepikachu.yangtuyunju.util.SqlUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author 28944
 * @description 针对表【course(课程)】的数据库操作Service实现
 * @createDate 2024-04-22 16:45:06
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course>
        implements CourseService {

    @Resource
    CourseTypeService courseTypeService;

    @Override
    public LambdaQueryWrapper<Course> getLambdaQueryWrapper(CourseQueryRequest courseQueryRequest) {
        LambdaQueryWrapper<Course> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (courseQueryRequest == null) {
            return lambdaQueryWrapper;
        }

        Long courseTypeId = courseQueryRequest.getCourseTypeId();
        lambdaQueryWrapper.eq(ObjectUtil.isNotEmpty(courseTypeId), Course::getCourseTypeId, courseTypeId);

        String sortField = courseQueryRequest.getSortField();
        String sortOrder = courseQueryRequest.getSortOrder();
        if (SqlUtils.validSortField(sortField)) {
            boolean isAsc = sortOrder.equals(SortOrder.SORT_ORDER_ASC.getValue());
            switch (sortField.toLowerCase()) {
                case "createtime" -> lambdaQueryWrapper.orderBy(true, isAsc, Course::getCreateTime);
                case "coursetypeid" -> lambdaQueryWrapper.orderBy(true, isAsc, Course::getCourseTypeId);
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public CourseVO getCourseVO(Course course) {
        CourseVO courseVO = CourseVO.objToVo(course);
        // 1.关联查询课程类型
        Long courseTypeId = course.getCourseTypeId();
        CourseType courseType = null;
        if (courseTypeId != null) {
            courseType = courseTypeService.getById(courseTypeId);
        }
        CourseTypeVO courseTypeVO = courseTypeService.getCourseTypeVO(courseType);
        courseVO.setCourseType(courseTypeVO);
        return courseVO;
    }

    @Override
    public Page<CourseVO> getCourseVOPage(Page<Course> coursePage) {
        List<Course> courseList = coursePage.getRecords();
        Page<CourseVO> courseVOPage = new Page<>(coursePage.getCurrent(), coursePage.getSize(), coursePage.getTotal());
        if (CollUtil.isEmpty(courseList)) {
            return courseVOPage;
        }
        // 1.关联查询课程类型信息
        Set<Long> courseTypeIdSet = courseList.stream().map(Course::getCourseTypeId).collect(Collectors.toSet());
        Map<Long, List<CourseType>> courseTypeIdCourseTypeListMap = courseTypeService.listByIds(courseTypeIdSet).stream()
                .collect(Collectors.groupingBy(CourseType::getId));
        // 填充信息
        List<CourseVO> courseVOList = courseList.stream().map(course -> {
            CourseVO courseVO = CourseVO.objToVo(course);
            Long courseTypeId = courseVO.getCourseTypeId();
            CourseType courseType = null;
            if (courseTypeIdCourseTypeListMap.containsKey(courseTypeId)) {
                courseType = courseTypeIdCourseTypeListMap.get(courseTypeId).get(0);
            }
            courseVO.setCourseType(courseTypeService.getCourseTypeVO(courseType));
            return courseVO;
        }).collect(Collectors.toList());
        courseVOPage.setRecords(courseVOList);
        return courseVOPage;
    }
}




