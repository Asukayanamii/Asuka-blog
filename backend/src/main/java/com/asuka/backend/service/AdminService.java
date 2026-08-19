package com.asuka.backend.service;

import com.asuka.backend.pojo.dto.LoginDTO;
import com.asuka.backend.pojo.vo.LoginVO;

/**
 * 管理员相关业务接口。
 */
public interface AdminService {

    /**
     * 校验管理员登录信息并生成登录凭证。
     *
     * @param loginDTO 登录请求参数
     * @return 登录成功后的管理员信息和 JWT 令牌
     */
    LoginVO login(LoginDTO loginDTO);
}
