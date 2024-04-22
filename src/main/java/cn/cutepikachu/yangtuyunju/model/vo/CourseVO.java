package cn.cutepikachu.yangtuyunju.model.vo;

import cn.cutepikachu.yangtuyunju.model.entity.Course;
import cn.hutool.core.bean.BeanUtil;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class CourseVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 课程类型id
     */
    private Long courseTypeId;

    /**
     * 课程类型
     */
    private CourseTypeVO courseType;

    /**
     * 课程名
     */
    private String name;

    /**
     * 课程图片
     */
    private String imgUrl;

    /**
     * 课程描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param courseVO
     * @return
     */
    public static Course voToObj(CourseVO courseVO) {
        if (courseVO == null) {
            return null;
        }
        Course course = new Course();
        BeanUtil.copyProperties(courseVO, course);
        return course;
    }

    /**
     * 对象转包装类
     *
     * @param course
     * @return
     */
    public static CourseVO objToVo(Course course) {
        if (course == null) {
            return null;
        }
        CourseVO courseVO = new CourseVO();
        BeanUtil.copyProperties(course, courseVO);
        return courseVO;
    }
}
