package cn.cutepikachu.yangtuyunju.mapper;

import cn.cutepikachu.yangtuyunju.model.entity.Post;
import cn.cutepikachu.yangtuyunju.model.entity.PostFavour;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * @author 28944
 * @description 针对表【post_favour(帖子收藏)】的数据库操作Mapper
 * @createDate 2024-02-20 20:02:41
 * @Entity com.pikachu.so.model.entity.PostFavour
 */
public interface PostFavourMapper extends BaseMapper<PostFavour> {
    /**
     * 分页查询收藏帖子列表
     *
     * @param page
     * @param userId
     * @return
     */
    Page<Post> listFavourPostByPage(IPage<Post> page, @Param("userId") long userId);
}




