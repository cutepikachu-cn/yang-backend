package cn.cutepikachu.yangtuyunju.model.vo;

import cn.cutepikachu.yangtuyunju.model.entity.CourseType;
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
public class CourseTypeVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 课程类型名
     */
    private String name;

    /**
     * 课程类型图片
     */
    private String imgUrl;

    /**
     * 课程类型描述
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
     * @param courseTypeVO
     * @return
     */
    public static CourseType voToObj(CourseTypeVO courseTypeVO) {
        if (courseTypeVO == null) {
            return null;
        }
        CourseType courseType = new CourseType();
        BeanUtil.copyProperties(courseTypeVO, courseType);
        return courseType;
    }

    /**
     * 对象转包装类
     *
     * @param courseType
     * @return
     */
    public static CourseTypeVO objToVo(CourseType courseType) {
        if (courseType == null) {
            return null;
        }
        CourseTypeVO courseTypeVO = new CourseTypeVO();
        BeanUtil.copyProperties(courseType, courseTypeVO);
        return courseTypeVO;
    }
}
