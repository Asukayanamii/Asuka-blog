package com.asuka.backend.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录返回VO")
public class LoginVO {
    @Schema(description = "管理员ID")
    private Integer id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "JWT令牌")
    private String token;
}
