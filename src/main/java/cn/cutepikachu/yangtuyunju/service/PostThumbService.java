package cn.cutepikachu.yangtuyunju.service;

import cn.cutepikachu.yangtuyunju.model.entity.PostThumb;
import cn.cutepikachu.yangtuyunju.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 28944
 * @description 针对表【post_thumb(帖子点赞)】的数据库操作Service
 * @createDate 2024-02-20 20:02:41
 */
public interface PostThumbService extends IService<PostThumb> {
    /**
     * 点赞
     *
     * @param postId
     * @param loginUser
     * @return
     */
    int doPostThumb(long postId, User loginUser);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    int doPostThumbInner(long userId, long postId);
}
