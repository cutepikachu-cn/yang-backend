package cn.cutepikachu.yangtuyunju.controller;

import cn.cutepikachu.yangtuyunju.common.BaseResponse;
import cn.cutepikachu.yangtuyunju.common.ResponseCode;
import cn.cutepikachu.yangtuyunju.constant.FileConstant;
import cn.cutepikachu.yangtuyunju.exception.BusinessException;
import cn.cutepikachu.yangtuyunju.manager.CosManager;
import cn.cutepikachu.yangtuyunju.model.entity.User;
import cn.cutepikachu.yangtuyunju.model.enums.FileUploadBizEnum;
import cn.cutepikachu.yangtuyunju.service.UserService;
import cn.cutepikachu.yangtuyunju.util.ResultUtils;
import cn.cutepikachu.yangtuyunju.util.ThrowUtils;
import cn.hutool.core.io.FileUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;

/**
 * 文件接口
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    /**
     * 文件上传
     *
     * @param multipartFile
     * @param fileBiz
     * @param request
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestPart("file") MultipartFile multipartFile,
                                           String fileBiz,
                                           HttpServletRequest request) {
        FileUploadBizEnum biz = FileUploadBizEnum.getEnumByValue(fileBiz);
        ThrowUtils.throwIf(biz == null, new BusinessException(ResponseCode.PARAMS_ERROR));
        validFile(multipartFile, biz);
        User loginUser = userService.getLoginUser(request);
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format(FileConstant.PATH_FORMAT, biz.getValue(), loginUser.getId(), filename);
        File file = null;
        try {
            // 上传文件
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
            cosManager.putObject(filepath, file);
            // 返回可访问地址
            return ResultUtils.success("上传成功", FileConstant.COS_HOST + filepath);
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filepath, e);
            throw new BusinessException(ResponseCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

    /**
     * 校验文件
     *
     * @param multipartFile
     * @param fileUploadBizEnum 文件上传业务类型
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > ONE_M) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "文件大小不能超过 1M");
            }
            if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ResponseCode.PARAMS_ERROR, "文件类型错误");
            }
        }
    }


}
