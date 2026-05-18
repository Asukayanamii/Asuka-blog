package com.asuka.backend.controller.admin;

import com.asuka.backend.mapper.AdminMapper;
import com.asuka.backend.pojo.dto.LoginDTO;
import com.asuka.backend.pojo.entity.Admin;
import com.asuka.backend.pojo.vo.LoginVO;
import com.asuka.backend.result.Result;
import com.asuka.backend.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@Slf4j
@Tag(name = "管理员登录")
public class AdminController {

    @Autowired
    private AdminMapper adminMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public Result login(@RequestBody LoginDTO loginDTO) {
        Admin admin = adminMapper.getByUsername(loginDTO.getUsername());
        if (admin == null) {
            return Result.error("用户名或密码错误");
        }
        if (!passwordEncoder.matches(loginDTO.getPassword(), admin.getPassword())) {
            return Result.error("用户名或密码错误");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("id", admin.getId());
        claims.put("username", admin.getUsername());
        String token = JwtUtil.createToken(claims);

        LoginVO loginVO = LoginVO.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .token(token)
                .build();

        return Result.success(loginVO);
    }
}
