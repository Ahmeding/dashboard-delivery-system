package com.hitecart.deliverysystem.services.impl;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dao.RoleRepository;
import com.hitecart.deliverysystem.dto.role.RoleRequest;
import com.hitecart.deliverysystem.dto.user.UserRequest;
import com.hitecart.deliverysystem.dto.user.UserResponse;
import com.hitecart.deliverysystem.dto.user.UserUpdateRequest;
import com.hitecart.deliverysystem.exception.EntityNotFoundException;
import com.hitecart.deliverysystem.exception.ErrorCodes;
import com.hitecart.deliverysystem.exception.InvalidEntityException;
import com.hitecart.deliverysystem.exception.InvalidOperationException;
import com.hitecart.deliverysystem.mapper.UserMapper;
import com.hitecart.deliverysystem.model.Role;
import com.hitecart.deliverysystem.model.User;
import com.hitecart.deliverysystem.dao.UserRepository;
import com.hitecart.deliverysystem.services.UserService;
import com.hitecart.deliverysystem.utils.CapitalizeFirstLetter;
import com.hitecart.deliverysystem.utils.CheckParmasUtil;
import com.hitecart.deliverysystem.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.hitecart.deliverysystem.utils.CapitalizeFirstLetter.*;
import static com.hitecart.deliverysystem.utils.CheckDuplicat.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserResponse save(UserRequest userRequest) {
        checkRequest(userRequest, "User n'est pas valide",
                "l'User n'est pas valide", ErrorCodes.USER_NOT_VALID_OPERATION);

        User userToSave = userMapper.toEntity(userRequest);
        checkDuplicatUser(userToSave, userRepository);

        return savingUser(userRequest);
    }

    private UserResponse savingUser(UserRequest userRequest) {
        User userToSave = userMapper.toEntity(userRequest);
        // Fetch existing roles from the database
        List<Role> managedRoles = userToSave.getRoles().stream()
                .map(role -> roleRepository.findByName(role.getName())) // Returns Optional<Role>
                .flatMap(optionalRole -> optionalRole.stream())         // Unwrap Optional<Role> to Stream<Role>
                .collect(Collectors.toList());                           // Collect into Set<Role>
        userToSave.setRoles(managedRoles);
        userToSave.setFirstname(capitalizeFirstLetter(userRequest.firstname()));
        userToSave.setLastname(capitalizeFirstLetter(userRequest.lastname()));
        userToSave.setEmail(userRequest.email().toLowerCase());

        userToSave.setPassword(passwordEncoder.encode(userRequest.password()));
        return userMapper.toResponse(userRepository.save(userToSave));
    }

    @Override
    @Transactional
    public UserResponse updateUserInfo(Integer userId, UserUpdateRequest userUpdateRequest) {
      /*  checkRequestForUpdate(userUpdateRequest, "User n'est pas valide",
                "l'User n'est pas valide", ErrorCodes.USER_NOT_VALID_OPERATION);*/
        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with ::ID : " + userId +
                        " not found", ErrorCodes.USER_NOT_FOUND)
        );

        userMapper.updateUserFromRequest(userUpdateRequest, existingUser);
        // Capitalize first/last name if they were updated (i.e., not null in DTO)
        if (StringUtils.hasText(userUpdateRequest.firstname())) { // checks for not null and not empty/whitespace
            existingUser.setFirstname(CapitalizeFirstLetter.capitalizeFirstLetter(existingUser.getFirstname()));
        }
        if (StringUtils.hasText(userUpdateRequest.lastname())) {
            existingUser.setLastname(CapitalizeFirstLetter.capitalizeFirstLetter(existingUser.getLastname()));
        }
        if (StringUtils.hasText(userUpdateRequest.email())) {
            String newEmail = userUpdateRequest.email().toLowerCase();
            if (!newEmail.equals(existingUser.getEmail())) {
                // Optional: Check if the new email is already taken by another user
                 userRepository.findByEmail(newEmail).ifPresent(u -> {
                 if (!u.getId().equals(existingUser.getId())) {
                 throw new InvalidOperationException("Email already in use: " + newEmail, ErrorCodes.USER_NOT_VALID_OPERATION);
                 }
                 });
                existingUser.setEmail(newEmail);
            }
        }
        updateRole(userUpdateRequest, existingUser);


        User updatedUser = userRepository.save(existingUser);

        return userMapper.toResponse(updatedUser);
    }

    private void updateRole(UserUpdateRequest userUpdateRequest, User existingUser) {
        if (userUpdateRequest.roles() != null) {
            List<Role> managedRoles = userUpdateRequest.roles().stream()
                    .map(roleRequest -> {
                        if (roleRequest.name() == null) {
                            throw new IllegalArgumentException("Role name/type cannot be null in role update request.");
                        }
                        return roleRepository.findByName(roleRequest.name()) // Assuming RoleRequest.name() gives RoleType
                                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleRequest.name(), ErrorCodes.ROLE_NOT_FOUND));
                    })
                    .collect(Collectors.toList()); // Use toSet() if you prefer Set<Role> and want to ensure uniqueness
            existingUser.setRoles(managedRoles);
        }
    }

    @Override
    @Cacheable("users")
    public UserResponse findById(Integer id) {
        CheckParmasUtil.checkID(id,
                "Le parametre ID User est null",
                "Impossible de trouver une utilisateur avec un ID null",
                ErrorCodes.USER_NOT_VALID_OPERATION);
        User userById = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Aucune User avec l'id :"
                        + id + "n'a ete trouvé dans la base deonnée", ErrorCodes.USER_NOT_FOUND)
        );
        return userMapper.toResponse(userById);
    }

    @Override
    public UserResponse findByEmail(String email) {
        CheckParmasUtil.checkStringParma(email,
                "ERREUR: Le parametre email est null",
                "Impossible de trouver un utilisateur avec email null",
                ErrorCodes.USER_NOT_VALID_OPERATION);

        User userByMail = userRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(
                        "Aucun User avec l'address email <<" + email + ">> n'a ete trouver dans la base de donnée",
                        ErrorCodes.USER_NOT_FOUND
                )
        );
        return userMapper.toResponse(userByMail);
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        CheckParmasUtil.checkID(id, "l'ID User  à supprimer est null",
                "Impossible de supprimer un frais de Mission  avec un ID null", ErrorCodes.USER_NOT_VALID_OPERATION);
        userRepository.deleteById(id);
    }

    @Override
    public PageResponse<UserResponse> findAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> utilisateurs = userRepository.findAllUsers(pageable);
        List<UserResponse> userBasicDtos = utilisateurs.stream()
                .map(userMapper::toResponse)
                .toList();

        return new PageResponse<>(
                userBasicDtos,
                utilisateurs.getNumber(),
                utilisateurs.getSize(),
                utilisateurs.getTotalElements(),
                utilisateurs.getTotalPages(),
                utilisateurs.isFirst(),
                utilisateurs.isLast()
        );
    }

    @Override
    public UserResponse updateUserRoles(Integer userId, List<RoleRequest> rolesRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId, ErrorCodes.USER_NOT_FOUND));


        List<Role> roles = rolesRequest.stream()
                .map(roleRequest -> roleRepository.findByName(roleRequest.name()).orElseThrow(
                        () -> new EntityNotFoundException("Role not found: " + roleRequest.name(), ErrorCodes.ROLE_NOT_FOUND)))
                .collect(Collectors.toList());

        user.setRoles(roles);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse updateUserPassword(Integer userId, UserRequest userRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId, ErrorCodes.USER_NOT_FOUND));
        if (userRequest.password() == null) {
            throw new InvalidOperationException("Not valid password",
                    ErrorCodes.USER_NOT_VALID_OPERATION);
        }
        user.setPassword(passwordEncoder.encode(userRequest.password()));
        User updateUser = userRepository.save(user);
        return userMapper.toResponse(updateUser);
    }

    private void checkRequest(UserRequest userRequest, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = UserValidator.validate(userRequest);
        if (!errors.isEmpty()) {
            log.error(msgLog, userRequest);
            throw new InvalidEntityException(msgException, errorCodes, errors);
        }
    }

    private void checkRequestForUpdate(UserRequest userRequest, String msgLog, String msgException, ErrorCodes errorCodes) {
        List<String> errors = UserValidator.validateForUpdate(userRequest);
        if (!errors.isEmpty()) {
            log.error(msgLog, userRequest);
            throw new InvalidEntityException(msgException, errorCodes, errors);
        }
    }

}
