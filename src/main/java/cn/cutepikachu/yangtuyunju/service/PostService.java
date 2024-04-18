package cn.cutepikachu.yangtuyunju.service;

import cn.cutepikachu.yangtuyunju.model.dto.post.PostQueryRequest;
import cn.cutepikachu.yangtuyunju.model.entity.Post;
import cn.cutepikachu.yangtuyunju.model.vo.PostVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author 28944
 * @description 针对表【post(帖子)】的数据库操作Service
 * @createDate 2024-02-20 20:02:34
 */
public interface PostService extends IService<Post> {

    /**
     * 获取查询条件
     *
     * @param postQueryRequest
     * @return
     */
    LambdaQueryWrapper<Post> getLambdaQueryWrapper(PostQueryRequest postQueryRequest);

    /**
     * 获取帖子封装
     *
     * @param post
     * @param request
     * @return
     */
    PostVO getPostVO(Post post, HttpServletRequest request);

    /**
     * 分页获取帖子封装
     *
     * @param postPage
     * @param request
     * @return
     */
    Page<PostVO> getPostVOPage(Page<Post> postPage, HttpServletRequest request);
}
