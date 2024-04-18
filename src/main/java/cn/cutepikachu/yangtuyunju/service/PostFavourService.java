package cn.cutepikachu.yangtuyunju.service;

import cn.cutepikachu.yangtuyunju.model.entity.Post;
import cn.cutepikachu.yangtuyunju.model.entity.PostFavour;
import cn.cutepikachu.yangtuyunju.model.entity.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 28944
 * @description 针对表【post_favour(帖子收藏)】的数据库操作Service
 * @createDate 2024-02-20 20:02:41
 */
public interface PostFavourService extends IService<PostFavour> {
    /**
     * 帖子收藏
     *
     * @param postId
     * @param loginUser
     * @return
     */
    int doPostFavour(long postId, User loginUser);

    /**
     * 分页获取用户收藏的帖子列表
     *
     * @param userId
     * @return
     */
    Page<Post> listFavourPostByPage(long current, long pageSize,
                                    long userId);

    /**
     * 帖子收藏（内部服务）
     *
     * @param userId
     * @param postId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    int doPostFavourInner(long userId, long postId);
}
