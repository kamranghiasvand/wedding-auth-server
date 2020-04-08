package com.bluebox.planner.auth.rest.controller.permission;

import com.bluebox.planner.auth.common.exception.GlobalException;
import com.bluebox.planner.auth.common.util.ConvertUtil;
import com.bluebox.planner.auth.common.viewModel.permission.PermissionDto;
import com.bluebox.planner.auth.common.viewModel.regular.RegularUserDto;
import com.bluebox.planner.auth.persistence.entity.PermissionEntity;
import com.bluebox.planner.auth.persistence.entity.regular.RegularUserEntity;
import com.bluebox.planner.auth.rest.Converter;
import org.springframework.stereotype.Service;

/**
 * @author by kamran ghiasvand
 */
@Service
public class PermissionConverter implements Converter<PermissionEntity, PermissionDto,Long> {
    @Override
    public PermissionEntity convert(PermissionDto dto, Object... args) throws GlobalException {
        return ConvertUtil.to(dto, PermissionEntity.class);
    }

    @Override
    public PermissionDto convert(PermissionEntity entity, Object... args) throws GlobalException {
        return ConvertUtil.to(entity,PermissionDto.class);
    }
}
