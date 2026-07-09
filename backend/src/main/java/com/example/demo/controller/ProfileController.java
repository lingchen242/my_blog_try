package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.Profile;
import com.example.demo.service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /** 公开：获取个人介绍 */
    @GetMapping
    public Result<Profile> get() {
        Profile profile = profileService.getProfile();
        if (profile == null) {
            return Result.error(404, "暂无个人介绍");
        }
        return Result.success(profile);
    }

    /** 管理员：更新个人介绍 */
    @PutMapping
    public Result<Void> update(@RequestBody Profile profile, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"admin".equals(role)) {
            return Result.error(403, "无权限");
        }
        profileService.update(profile);
        return Result.success();
    }
}
