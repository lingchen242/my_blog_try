package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.Profile;
import com.example.demo.mapper.ProfileMapper;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileMapper profileMapper;

    public ProfileService(ProfileMapper profileMapper) {
        this.profileMapper = profileMapper;
    }

    public Profile getProfile() {
        // 只有一条个人介绍记录，取第一条
        return profileMapper.selectOne(
                new LambdaQueryWrapper<Profile>().last("LIMIT 1")
        );
    }

    public void update(Profile profile) {
        Profile existing = getProfile();
        if (existing != null) {
            profile.setId(existing.getId());
            profileMapper.updateById(profile);
        } else {
            profileMapper.insert(profile);
        }
    }
}
