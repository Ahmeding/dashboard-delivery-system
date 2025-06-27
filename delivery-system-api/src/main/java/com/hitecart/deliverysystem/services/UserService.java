package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.role.RoleRequest;
import com.hitecart.deliverysystem.dto.user.UserRequest;
import com.hitecart.deliverysystem.dto.user.UserResponse;
import com.hitecart.deliverysystem.dto.user.UserUpdateRequest;

import java.util.List;

public interface UserService {
    UserResponse save(UserRequest request);

    UserResponse updateUserInfo(Integer userId, UserUpdateRequest userUpdateRequest);

    UserResponse findById(Integer id);

    UserResponse findByEmail(String email);

    List<UserResponse> findAll();

    void delete(Integer id);

    PageResponse<UserResponse> findAllUsers(int page, int size);

    UserResponse updateUserRoles(Integer id, List<RoleRequest> rolesRequest);

    UserResponse updateUserPassword(Integer userId, UserRequest request);
}
