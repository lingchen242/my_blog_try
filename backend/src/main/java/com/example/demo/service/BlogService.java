package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.Blog;
import com.example.demo.entity.BlogVO;
import com.example.demo.entity.User;
import com.example.demo.mapper.BlogMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BlogService {

    private final BlogMapper blogMapper;
    private final UserMapper userMapper;

    public BlogService(BlogMapper blogMapper, UserMapper userMapper) {
        this.blogMapper = blogMapper;
        this.userMapper = userMapper;
    }

    public List<Blog> listAll() {
        return blogMapper.selectList(
                new LambdaQueryWrapper<Blog>()
                        .orderByDesc(Blog::getCreateTime)
        );
    }

    public Blog getById(Long id) {
        return blogMapper.selectById(id);
    }

    public void save(Blog blog) {
        blogMapper.insert(blog);
    }

    public void update(Blog blog) {
        blogMapper.updateById(blog);
    }

    public void delete(Long id) {
        blogMapper.deleteById(id);
    }

    /** 查询指定用户的博客 */
    public List<Blog> getByAuthorId(Long authorId) {
        return blogMapper.selectList(
                new LambdaQueryWrapper<Blog>()
                        .eq(Blog::getAuthorId, authorId)
                        .orderByDesc(Blog::getCreateTime)
        );
    }

    /** 管理员查询博客（支持按用户名或标题模糊搜索）*/
    public List<BlogVO> searchBlogsWithAuthor(String username, String title) {
        LambdaQueryWrapper<Blog> wrapper = new LambdaQueryWrapper<>();
        if (username != null && !username.isBlank()) {
            List<User> users = userMapper.selectList(
                    new LambdaQueryWrapper<User>()
                            .like(User::getUsername, username)
            );
            if (users.isEmpty()) return Collections.emptyList();
            wrapper.in(Blog::getAuthorId,
                    users.stream().map(User::getId).collect(Collectors.toList()));
        }
        if (title != null && !title.isBlank()) {
            wrapper.like(Blog::getTitle, title);
        }
        wrapper.orderByDesc(Blog::getCreateTime);
        List<Blog> blogs = blogMapper.selectList(wrapper);
        return enrichWithAuthorName(blogs);
    }

    /** 给博客列表附上作者昵称 */
    private List<BlogVO> enrichWithAuthorName(List<Blog> blogs) {
        Map<Long, String> userMap = userMapper.selectList(null).stream()
                .collect(Collectors.toMap(User::getId, User::getNickname));
        return blogs.stream().map(b -> {
            BlogVO vo = new BlogVO();
            BeanUtils.copyProperties(b, vo);
            vo.setAuthorName(userMap.getOrDefault(b.getAuthorId(), "未知"));
            return vo;
        }).collect(Collectors.toList());
    }
}