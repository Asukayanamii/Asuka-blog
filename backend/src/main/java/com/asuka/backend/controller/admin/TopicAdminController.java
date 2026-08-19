package com.asuka.backend.controller.admin;

import com.asuka.backend.pojo.dto.TopicSaveDTO;
import com.asuka.backend.pojo.vo.TopicVO;
import com.asuka.backend.result.Result;
import com.asuka.backend.service.TopicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/admin/topics")
@Slf4j
@Tag(name = "管理端/管理端专题管理")
public class TopicAdminController {

    @Autowired
    private TopicService topicService;

    @GetMapping("/list")
    @Operation(summary = "获取所有专题")
    public Result<ArrayList<TopicVO>> list() {
        return Result.success(topicService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取专题详情")
    public Result<TopicVO> detail(@PathVariable Integer id) {
        return Result.success(topicService.getTopicById(id));
    }

    @PostMapping
    @Operation(summary = "新增专题")
    public Result<Integer> create(@RequestBody TopicSaveDTO dto) {
        // 新增后的主键返回给前端，便于刷新或跳转到专题详情。
        Integer id = topicService.createTopic(dto);
        return Result.success(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新专题")
    public Result<Void> update(@PathVariable Integer id, @RequestBody TopicSaveDTO dto) {
        // 资源 ID 来自路径，交由 service 执行更新。
        topicService.updateTopic(id, dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除专题")
    public Result<Void> delete(@PathVariable Integer id) {
        // 删除操作按路径中的专题 ID 执行。
        topicService.deleteTopic(id);
        return Result.success();
    }
}
