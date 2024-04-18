package cn.cutepikachu.yangtuyunju.mapper;

import cn.cutepikachu.yangtuyunju.model.entity.Post;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Date;
import java.util.List;

/**
 * @author 28944
 * @description 针对表【post(帖子)】的数据库操作Mapper
 * @createDate 2024-02-20 20:02:34
 * @Entity com.pikachu.so.model.entity.Post
 */
public interface PostMapper extends BaseMapper<Post> {
    /**
     * 查询帖子列表（包括已被删除的数据）
     */
    List<Post> listPostWithDelete(Date minUpdateTime);
}




