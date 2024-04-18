package cn.cutepikachu.yangtuyunju.controller;


import cn.cutepikachu.yangtuyunju.common.BaseResponse;
import cn.cutepikachu.yangtuyunju.model.dto.postthumb.PostThumbAddRequest;
import cn.cutepikachu.yangtuyunju.model.entity.User;
import cn.cutepikachu.yangtuyunju.service.PostThumbService;
import cn.cutepikachu.yangtuyunju.service.UserService;
import cn.cutepikachu.yangtuyunju.util.ResultUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 帖子点赞接口
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@RestController
@RequestMapping("/post_thumb")
@Slf4j
public class PostThumbController {

    @Resource
    private PostThumbService postThumbService;

    @Resource
    private UserService userService;

    /**
     * 点赞 / 取消点赞
     *
     * @param postThumbAddRequest
     * @param request
     * @return
     */
    @PostMapping("/")
    public BaseResponse<Integer> doThumb(@RequestBody @Valid PostThumbAddRequest postThumbAddRequest,
                                         HttpServletRequest request) {
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long postId = postThumbAddRequest.getPostId();
        int result = postThumbService.doPostThumb(postId, loginUser);
        return ResultUtils.success(result);
    }

}
