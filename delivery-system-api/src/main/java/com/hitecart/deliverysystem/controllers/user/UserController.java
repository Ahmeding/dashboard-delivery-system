package com.hitecart.deliverysystem.controllers.user;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.UserApi;
import com.hitecart.deliverysystem.dto.role.RoleRequest;
import com.hitecart.deliverysystem.dto.user.UserRequest;
import com.hitecart.deliverysystem.dto.user.UserResponse;
import com.hitecart.deliverysystem.dto.user.UserUpdateRequest;
import com.hitecart.deliverysystem.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponse> saveUser(UserRequest userRequest) {
        return ResponseEntity.ok(userService.save(userRequest));
    }

    /**
     * Partially updates an existing user's information.
     * Only fields provided in the UserUpdateRequest will be considered for update.
     * Fields not provided (or null) in the request will retain their current values.
     *
     * @param userId The ID of the user to update.
     * @param userUpdateRequest DTO containing the fields to update.
     * @return ResponseEntity containing the updated UserResponse.
     */
    @Override
    public ResponseEntity<UserResponse> updateUserInfo(Integer userId, UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(userService.updateUserInfo(userId, userUpdateRequest));
    }

    @Override
    public ResponseEntity<UserResponse> findUserById(Integer id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Override
    public ResponseEntity<UserResponse> findUserByEmail(String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @Override
    public ResponseEntity<List<UserResponse>> findAllUser() {
        return ResponseEntity.ok(userService.findAll());
    }

    @Override
    public  ResponseEntity<?> deleteUser(Integer id) {
        try {
            userService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "User deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "User not found with ID: " + id
                    ));
        }
    }

    @Override
    public ResponseEntity<PageResponse<UserResponse>> findAllUsersPageable(int page, int size) {
        return ResponseEntity.ok(userService.findAllUsers(page, size));
    }

    @Override
    public ResponseEntity<UserResponse> updateUserRoles(Integer id, List<RoleRequest> roleRequests) {

        UserResponse updated = userService.updateUserRoles(id, roleRequests);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<UserResponse> updateUserPassword(Integer id, UserRequest userRequest) {
        UserResponse updated = userService.updateUserPassword(id, userRequest);
        return ResponseEntity.ok(updated);
    }
}
