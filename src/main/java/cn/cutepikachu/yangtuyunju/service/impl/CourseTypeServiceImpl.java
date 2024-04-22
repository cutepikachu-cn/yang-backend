package cn.cutepikachu.yangtuyunju.service.impl;

import cn.cutepikachu.yangtuyunju.common.PageRequest;
import cn.cutepikachu.yangtuyunju.mapper.CourseTypeMapper;
import cn.cutepikachu.yangtuyunju.model.entity.CourseType;
import cn.cutepikachu.yangtuyunju.model.enums.SortOrder;
import cn.cutepikachu.yangtuyunju.model.vo.CourseTypeVO;
import cn.cutepikachu.yangtuyunju.service.CourseTypeService;
import cn.cutepikachu.yangtuyunju.util.SqlUtils;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 28944
 * @description 针对表【course_type(课程类型)】的数据库操作Service实现
 * @createDate 2024-04-22 16:45:06
 */
@Service
public class CourseTypeServiceImpl extends ServiceImpl<CourseTypeMapper, CourseType>
        implements CourseTypeService {

    @Override
    public LambdaQueryWrapper<CourseType> getLambdaQueryWrapper(PageRequest pageRequest) {
        LambdaQueryWrapper<CourseType> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (pageRequest == null) {
            return lambdaQueryWrapper;
        }

        String sortField = pageRequest.getSortField();
        String sortOrder = pageRequest.getSortOrder();
        if (SqlUtils.validSortField(sortField)) {
            boolean isAsc = sortOrder.equals(SortOrder.SORT_ORDER_ASC.getValue());
            switch (sortField.toLowerCase()) {
                case "createtime" -> lambdaQueryWrapper.orderBy(true, isAsc, CourseType::getCreateTime);
            }
        }
        return lambdaQueryWrapper;
    }

    @Override
    public CourseTypeVO getCourseTypeVO(CourseType courseType) {
        CourseTypeVO courseTypeVO = CourseTypeVO.objToVo(courseType);
        return courseTypeVO;
    }

    @Override
    public Page<CourseTypeVO> getCourseTypeVOPage(Page<CourseType> courseTypePage) {
        List<CourseType> courseTypeList = courseTypePage.getRecords();
        Page<CourseTypeVO> courseTypeVOPage = new Page<>(courseTypePage.getCurrent(), courseTypePage.getSize(), courseTypePage.getTotal());
        if (CollUtil.isEmpty(courseTypeList)) {
            return courseTypeVOPage;
        }
        List<CourseTypeVO> courseTypeVOList = courseTypeList.stream().map(CourseTypeVO::objToVo).toList();
        courseTypeVOPage.setRecords(courseTypeVOList);
        return courseTypeVOPage;
    }
}




