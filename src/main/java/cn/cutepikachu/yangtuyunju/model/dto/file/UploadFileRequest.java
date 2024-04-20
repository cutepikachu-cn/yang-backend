package cn.cutepikachu.yangtuyunju.model.dto.file;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 文件上传请求
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Data
public class UploadFileRequest {
    /**
     * 业务
     */
    @NotNull
    private String biz;

    private static final long serialVersionUID = 1L;

}
