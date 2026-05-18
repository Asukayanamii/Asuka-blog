package com.asuka.backend.mapper;

import com.asuka.backend.pojo.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
    Admin getByUsername(String username);
    Admin getById(Integer id);
}
