package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.Message;
import com.example.demo.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageMapper messageMapper;

    public MessageService(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    public List<Message> listAll() {
        return messageMapper.selectList(
                new LambdaQueryWrapper<Message>()
                        .orderByDesc(Message::getCreateTime)
        );
    }

    public List<Message> getByBlogId(Long blogId) {
        return messageMapper.selectList(
                new LambdaQueryWrapper<Message>()
                        .eq(Message::getBlogId, blogId)
                        .orderByDesc(Message::getCreateTime)
        );
    }

    public List<Message> searchByNickname(String nickname) {
        return messageMapper.selectList(
                new LambdaQueryWrapper<Message>()
                        .like(nickname != null && !nickname.isBlank(), Message::getNickname, nickname)
                        .orderByDesc(Message::getCreateTime)
        );
    }

    public Message getById(Long id) {
        return messageMapper.selectById(id);
    }

    public void save(Message message) {
        messageMapper.insert(message);
    }

    public void delete(Long id) {
        messageMapper.deleteById(id);
    }
}