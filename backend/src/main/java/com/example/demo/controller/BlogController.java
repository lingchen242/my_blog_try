package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.Blog;
import com.example.demo.entity.BlogVO;
import com.example.demo.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping
    public Result<List<Blog>> list() {
        return Result.success(blogService.listAll());
    }

    @GetMapping("/{id}")
    public Result<Blog> getById(@PathVariable Long id) {
        Blog blog = blogService.getById(id);
        if (blog == null) return Result.error(404, "博客不存在");
        return Result.success(blog);
    }

    /** 获取当前用户的博客 */
    @GetMapping("/my")
    public Result<List<Blog>> getMyBlogs(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.error(401, "未登录");
        return Result.success(blogService.getByAuthorId(userId));
    }

    /** 管理员：获取所有博客（含作者名，支持模糊搜索）*/
    @GetMapping("/admin/all")
    public Result<List<BlogVO>> getAllBlogs(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String title,
            HttpServletRequest request) {
        if (!"admin".equals(request.getAttribute("role"))) {
            return Result.error(403, "无权限");
        }
        return Result.success(blogService.searchBlogsWithAuthor(username, title));
    }

    @PostMapping
    public Result<Void> add(@RequestBody Blog blog, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) return Result.error(403, "无权限");
        Long userId = (Long) request.getAttribute("userId");
        blog.setAuthorId(userId);
        blogService.save(blog);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Blog blog, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Blog existing = blogService.getById(id);
        String role = (String) request.getAttribute("role");
        boolean isOwner = existing != null && userId != null && userId.equals(existing.getAuthorId());
        if (!"admin".equals(role) && !isOwner) return Result.error(403, "无权限");
        blog.setId(id);
        blogService.update(blog);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) return Result.error(403, "无权限");
        blogService.delete(id);
        return Result.success();
    }
}