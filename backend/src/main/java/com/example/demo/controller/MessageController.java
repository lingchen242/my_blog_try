package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.Blog;
import com.example.demo.entity.Message;
import com.example.demo.service.BlogService;
import com.example.demo.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final BlogService blogService;

    public MessageController(MessageService messageService, BlogService blogService) {
        this.messageService = messageService;
        this.blogService = blogService;
    }

    /** 获取留言：支持按 blogId 过滤，无 blogId 则返回全部 */
    @GetMapping
    public Result<List<Message>> list(
            @RequestParam(required = false) Long blogId,
            @RequestParam(required = false) String nickname) {
        if (blogId != null) {
            return Result.success(messageService.getByBlogId(blogId));
        }
        if (nickname != null && !nickname.isBlank()) {
            return Result.success(messageService.searchByNickname(nickname));
        }
        return Result.success(messageService.listAll());
    }

    /** 提交留言 */
    @PostMapping
    public Result<Void> add(@RequestBody Message message, HttpServletRequest request) {
        if (message.getNickname() == null || message.getNickname().isBlank()) {
            return Result.error(400, "昵称不能为空");
        }
        if (message.getContent() == null || message.getContent().isBlank()) {
            return Result.error(400, "留言内容不能为空");
        }
        if (message.getBlogId() == null) {
            return Result.error(400, "请指定留言所属文章");
        }
        // 已登录用户自动记录 userId
        Long userId = (Long) request.getAttribute("userId");
        if (userId != null && userId > 0) {
            message.setUserId(userId);
        }
        messageService.save(message);
        return Result.success();
    }

    /** 删除留言：管理员可删全部 / 博客作者可删自己文章下的留言 / 游客不可删 */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        Long userId = (Long) request.getAttribute("userId");

        if (role == null || "guest".equals(role)) {
            return Result.error(403, "无权限");
        }

        Message msg = messageService.getById(id);
        if (msg == null) return Result.error(404, "留言不存在");

        if ("admin".equals(role)) {
            messageService.delete(id);
            return Result.success();
        }

        // 博客作者可删自己文章下的留言
        if (msg.getBlogId() != null) {
            Blog blog = blogService.getById(msg.getBlogId());
            if (blog != null && userId != null && userId.equals(blog.getAuthorId())) {
                messageService.delete(id);
                return Result.success();
            }
        }

        return Result.error(403, "无权限");
    }
}