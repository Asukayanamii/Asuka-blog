package com.asuka.backend.service.impl;

import com.asuka.backend.mapper.AdminMapper;
import com.asuka.backend.exception.BaseException;
import com.asuka.backend.pojo.dto.LoginDTO;
import com.asuka.backend.pojo.entity.Admin;
import com.asuka.backend.pojo.vo.LoginVO;
import com.asuka.backend.service.AdminService;
import com.asuka.backend.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员业务实现，负责登录认证和登录凭证生成。
 */
@Service
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public LoginVO login(LoginDTO loginDTO) {
        log.info("管理员登录: {}", loginDTO.getUsername());

        // 先按用户名查询管理员，用户不存在时统一返回登录失败，避免暴露账号是否存在。
        Admin admin = adminMapper.getByUsername(loginDTO.getUsername());
        if (admin == null) {
            throw new BaseException("用户名或密码错误");
        }

        // 数据库保存的是 BCrypt 哈希，不能直接比较明文密码。
        if (!passwordEncoder.matches(loginDTO.getPassword(), admin.getPassword())) {
            throw new BaseException("用户名或密码错误");
        }

        // 仅将令牌校验所需的管理员标识写入 JWT，避免在令牌中保存敏感信息。
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", admin.getId());
        claims.put("username", admin.getUsername());
        String token = JwtUtil.createToken(claims);

        // 组装前端登录态所需数据，密码等敏感字段不会返回给客户端。
        return LoginVO.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .token(token)
                .build();
    }
}
