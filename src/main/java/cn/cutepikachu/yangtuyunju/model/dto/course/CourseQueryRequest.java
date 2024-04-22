package cn.cutepikachu.yangtuyunju.model.dto.course;

import cn.cutepikachu.yangtuyunju.common.PageRequest;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CourseQueryRequest extends PageRequest implements Serializable {

    @Min(1)
    private Long courseTypeId;

    @Serial
    private static final long serialVersionUID = 1L;
}
