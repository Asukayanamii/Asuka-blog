package com.asuka.backend.controller.admin;

import com.asuka.backend.pojo.dto.LoginDTO;
import com.asuka.backend.pojo.vo.LoginVO;
import com.asuka.backend.result.Result;
import com.asuka.backend.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Slf4j
@Tag(name = "管理端/管理员相关")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        // Controller 只负责接收请求并委托业务层处理登录流程。
        return Result.success(adminService.login(loginDTO));
    }
}
