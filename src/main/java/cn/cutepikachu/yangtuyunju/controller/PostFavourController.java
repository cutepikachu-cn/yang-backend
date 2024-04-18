package cn.cutepikachu.yangtuyunju.controller;


import cn.cutepikachu.yangtuyunju.common.BaseResponse;
import cn.cutepikachu.yangtuyunju.common.PageRequest;
import cn.cutepikachu.yangtuyunju.model.dto.postfavour.PostFavourAddRequest;
import cn.cutepikachu.yangtuyunju.model.dto.postfavour.PostFavourQueryRequest;
import cn.cutepikachu.yangtuyunju.model.entity.Post;
import cn.cutepikachu.yangtuyunju.model.entity.User;
import cn.cutepikachu.yangtuyunju.model.vo.PostVO;
import cn.cutepikachu.yangtuyunju.service.PostFavourService;
import cn.cutepikachu.yangtuyunju.service.PostService;
import cn.cutepikachu.yangtuyunju.service.UserService;
import cn.cutepikachu.yangtuyunju.util.ResultUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 帖子收藏接口
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@RestController
@RequestMapping("/post_favour")
@Slf4j
public class PostFavourController {

    @Resource
    private PostFavourService postFavourService;

    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    /**
     * 收藏 / 取消收藏
     *
     * @param postFavourAddRequest
     * @param request
     * @return
     */
    @PostMapping("/")
    public BaseResponse<Integer> doPostFavour(@RequestBody @Valid PostFavourAddRequest postFavourAddRequest,
                                              HttpServletRequest request) {
        // 登录才能操作
        final User loginUser = userService.getLoginUser(request);
        long postId = postFavourAddRequest.getPostId();
        int result = postFavourService.doPostFavour(postId, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取我收藏的帖子列表
     *
     * @param pageRequest
     * @param request
     */
    @PostMapping("/self/list/page")
    public BaseResponse<Page<PostVO>> listSelfFavourPostByPage(@RequestBody @Valid PageRequest pageRequest,
                                                               HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        long current = pageRequest.getCurrent();
        long size = pageRequest.getPageSize();
        Page<Post> postPage = postFavourService.listFavourPostByPage(current, size, loginUser.getId());
        Page<PostVO> postVOPage = postService.getPostVOPage(postPage, request);
        return ResultUtils.success(postVOPage);
    }

    /**
     * 获取用户收藏的帖子列表
     *
     * @param postFavourQueryRequest
     * @param request
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<PostVO>> listFavourPostByPage(@RequestBody @Valid PostFavourQueryRequest postFavourQueryRequest,
                                                           HttpServletRequest request) {
        long current = postFavourQueryRequest.getCurrent();
        long pageSize = postFavourQueryRequest.getPageSize();
        Long userId = postFavourQueryRequest.getUserId();
        Page<Post> postPage = postFavourService.listFavourPostByPage(current, pageSize, userId);
        return ResultUtils.success(postService.getPostVOPage(postPage, request));
    }
}
