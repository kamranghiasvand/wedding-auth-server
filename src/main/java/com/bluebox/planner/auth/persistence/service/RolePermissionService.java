package com.bluebox.planner.auth.persistence.service;

import com.bluebox.planner.auth.common.Constants;
import com.bluebox.planner.auth.common.exception.ResourceNotFoundException;
import com.bluebox.planner.auth.persistence.entity.PermissionEntity;
import com.bluebox.planner.auth.persistence.entity.regular.RegularUserEntity;
import com.bluebox.planner.auth.persistence.entity.regular.RoleEntity;
import com.bluebox.planner.auth.persistence.repository.PermissionRepository;
import com.bluebox.planner.auth.persistence.repository.RegularUserRepository;
import com.bluebox.planner.auth.persistence.repository.RoleRepository;
import io.jsonwebtoken.lang.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static java.text.MessageFormat.format;

/**
 * @author by kamran ghiasvand
 */
@Service
public class RolePermissionService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RolePermissionService.class);

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RegularUserRepository userRepository;

    @Autowired
    public RolePermissionService(RoleRepository roleRepository, PermissionRepository permissionRepository, RegularUserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void assignUserRole(Long userId, Long roleId) throws ResourceNotFoundException {
        LOGGER.debug(format("assigning role: {0} to user: {1} "), roleId, userId);
        var user = getUser(userId);
        var role = getRole(roleId);
        addRoleToUser(user, role);
        addUserToRole(user, role);
        roleRepository.save(role);
        userRepository.save(user);
        LOGGER.debug(format("role: {0} assigned to user: {1} "), roleId, userId);
    }

    @Transactional
    public void unAssignUserRole(Long userId, Long roleId) throws ResourceNotFoundException {
        LOGGER.debug(format("revoking role: {0} from user: {1} "), roleId, userId);
        var user = getUser(userId);
        var role = getRole(roleId);
        removeRoleFromUser(roleId, user);
        removeUserFromRole(userId, role);
        roleRepository.save(role);
        userRepository.save(user);
        LOGGER.debug(format("role: {0} revoked from user: {1}."), roleId, userId);
    }

    @Transactional
    public void assignRolePermission(Long roleId, Long permissionId) throws ResourceNotFoundException {
        LOGGER.debug(format("assigning permission: {0} to role: {1} "), permissionId, roleId);
        var role = getRole(roleId);
        var permission = getPermission(permissionId);
        addPermissionToRole(role, permission);
        addRoleToPermission(role, permission);
        roleRepository.save(role);
        permissionRepository.save(permission);
        LOGGER.debug(format("permission: {0} assigned to role: {1} "), permissionId, roleId);
    }

    @Transactional
    public void unAssignRolePermission(Long roleId, Long permissionId) throws ResourceNotFoundException {
        LOGGER.debug(format("revoking permission: {0} from role: {1} "), permissionId, roleId);
        var role = getRole(roleId);
        var permission = getPermission(permissionId);
        removePermissionFromRole(permissionId, role);
        removeRoleFromPermission(roleId, permission);
        roleRepository.save(role);
        permissionRepository.save(permission);
        LOGGER.debug(format("permission: {0} revoked from role: {1} "), permissionId, roleId);
    }

    private void removeRoleFromUser(Long roleId, RegularUserEntity user) {
        if (user.getRoles() != null)
            user.getRoles().removeIf(p -> p.getId().equals(roleId));
    }

    private void removeUserFromRole(Long userId, RoleEntity role) {
        if (role.getUsers() != null)
            role.getUsers().removeIf(p -> p.getId().equals(userId));
    }

    private void removePermissionFromRole(Long permissionId, RoleEntity role) {
        if (role.getPermissions() != null)
            role.getPermissions().removeIf(p -> p.getId().equals(permissionId));
    }

    private void removeRoleFromPermission(Long roleId, PermissionEntity permission) {
        if (permission.getRoles() != null)
            permission.getRoles().removeIf(p -> p.getId().equals(roleId));
    }

    private void addRoleToPermission(RoleEntity role, PermissionEntity permission) {
        if (Collections.isEmpty(permission.getRoles()))
            permission.setRoles(new ArrayList<>());
        permission.getRoles().add(role);
    }

    private void addRoleToUser(RegularUserEntity user, RoleEntity role) {
        if (Collections.isEmpty(user.getRoles()))
            user.setRoles(new ArrayList<>());
        user.getRoles().add(role);
    }

    private void addUserToRole(RegularUserEntity user, RoleEntity role) {
        if (Collections.isEmpty(role.getUsers()))
            role.setUsers(new ArrayList<>());
        role.getUsers().add(user);
    }

    private void addPermissionToRole(RoleEntity role, PermissionEntity permission) {
        if (Collections.isEmpty(role.getPermissions()))
            role.setPermissions(new ArrayList<>());
        role.getPermissions().add(permission);
    }

    private PermissionEntity getPermission(Long id) throws ResourceNotFoundException {
        LOGGER.debug(format("finding permission with id {0}"),id);
        var permission = permissionRepository.findById(id);
        if (permission.isEmpty())
            throw new ResourceNotFoundException(format(Constants.VALIDATION_NOT_FOUND_MSG, Constants.PERMISSION, id));
        LOGGER.debug(format("found permission with id {0}"),id);
        return permission.get();
    }

    private RoleEntity getRole(Long id) throws ResourceNotFoundException {
        LOGGER.debug(format("finding role with id {0}"),id);
        var role = roleRepository.findById(id);
        if (role.isEmpty())
            throw new ResourceNotFoundException(format(Constants.VALIDATION_NOT_FOUND_MSG, Constants.ROLE, id));
        LOGGER.debug(format("found role with id {0}"),id);
        return role.get();
    }

    private RegularUserEntity getUser(Long id) throws ResourceNotFoundException {
        LOGGER.debug(format("finding user with id {0}"),id);
        var user = userRepository.findById(id);
        if (user.isEmpty())
            throw new ResourceNotFoundException(format(Constants.VALIDATION_NOT_FOUND_MSG, Constants.USER, id));
        LOGGER.debug(format("found user with id {0}"),id);
        return user.get();
    }
}
