package cn.cutepikachu.yangtuyunju.service.impl;

import cn.cutepikachu.yangtuyunju.common.ResponseCode;
import cn.cutepikachu.yangtuyunju.exception.BusinessException;
import cn.cutepikachu.yangtuyunju.mapper.PostThumbMapper;
import cn.cutepikachu.yangtuyunju.model.entity.Post;
import cn.cutepikachu.yangtuyunju.model.entity.PostThumb;
import cn.cutepikachu.yangtuyunju.model.entity.User;
import cn.cutepikachu.yangtuyunju.service.PostService;
import cn.cutepikachu.yangtuyunju.service.PostThumbService;
import cn.cutepikachu.yangtuyunju.util.ThrowUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

/**
 * @author 28944
 * @description 针对表【post_thumb(帖子点赞)】的数据库操作Service实现
 * @createDate 2024-02-20 20:02:41
 */
@Service
public class PostThumbServiceImpl extends ServiceImpl<PostThumbMapper, PostThumb>
        implements PostThumbService {

    @Resource
    private PostService postService;

    /**
     * 点赞
     *
     * @param postId
     * @param loginUser
     * @return
     */
    @Override
    public int doPostThumb(long postId, User loginUser) {
        // 判断实体是否存在，根据类别获取实体
        Post post = postService.getById(postId);
        ThrowUtils.throwIf(post == null, ResponseCode.NOT_FOUND_ERROR);
        // 是否已点赞
        long userId = loginUser.getId();
        // 每个用户串行点赞
        // 锁必须要包裹住事务方法
        PostThumbService postThumbService = (PostThumbService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return postThumbService.doPostThumbInner(userId, postId);
        }
    }

    /**
     * 封装了事务的方法
     *
     * @param userId
     * @param postId
     * @return
     */
    @Override
    public int doPostThumbInner(long userId, long postId) {
        PostThumb postThumb = new PostThumb();
        postThumb.setUserId(userId);
        postThumb.setPostId(postId);
        QueryWrapper<PostThumb> thumbQueryWrapper = new QueryWrapper<>(postThumb);
        PostThumb oldPostThumb = this.getOne(thumbQueryWrapper);
        boolean result;
        // 已点赞
        if (oldPostThumb != null) {
            result = this.remove(thumbQueryWrapper);
            if (result) {
                // 点赞数 - 1
                result = postService.lambdaUpdate()
                        .eq(Post::getId, postId)
                        .gt(Post::getThumbNum, 0)
                        .setSql("thumb_num = thumb_num - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ResponseCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(postThumb);
            if (result) {
                // 点赞数 + 1
                result = postService.lambdaUpdate()
                        .eq(Post::getId, postId)
                        .setSql("thumb_num = thumb_num + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ResponseCode.SYSTEM_ERROR);
            }
        }
    }
}




