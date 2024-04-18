package cn.cutepikachu.yangtuyunju.common;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class DeleteRequest implements Serializable {

    @Min(1)
    private Long id;

    @Serial
    private static final long serialVersionUID = 1L;
}
