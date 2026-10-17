package com.andretk.backend1.services;

import com.andretk.backend1.dtos.*;
import com.andretk.backend1.enums.UserRole;
import com.andretk.backend1.exceptions.BadRequestException;
import com.andretk.backend1.exceptions.ResourceConflictException;
import com.andretk.backend1.exceptions.ResourceNotFoundException;
import com.andretk.backend1.mappers.UserMapper;
import com.andretk.backend1.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UtilityService utilityService;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers(String sort){
        if (!Set.of("name","email","id").contains(sort)) sort = "id";

        return userRepository.findAll(Sort.by(sort)).stream()
                .map(userMapper::toUserDto).toList();
    }

    @Transactional(readOnly = true)
    public CompleteUserDto getUserById(Long id){
        var user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with id: " + id + " doesn't exits")
        );

        return userMapper.toCompleteUserDto(user);
    }

    @Transactional
    public UserDto registerUser(RegisterUserRequest request){

        if (userRepository.existsByEmail(request.getEmail()))
            throw new ResourceConflictException("User with email: " + request.getEmail() + " already exits");

        if (request.getRole() == null ) request.setRole(UserRole.FREELANCER);

        var user = userMapper.toUser(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return userMapper.toUserDto(user);
    }


    @Transactional
    public void deleteUser(Long id){
        var user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with " + id + " not found"));

        utilityService.validateOwnerOrAdmin(id);

        userRepository.delete(user);
    }


    @Transactional
    public UserDto updateUser(UpdateUserRequest request){
        Long id = utilityService.getCurrentUserId();

        var user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with " + id + " not found"));

        if (request.getEmail() != null && !request.getEmail().equalsIgnoreCase(user.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceConflictException("Email already in use");
        }

        userMapper.update(request, user);

        return userMapper.toUserDto(user);
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request){
        Long id = utilityService.getCurrentUserId();

        var user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with " + id + " not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash()))
            throw new BadRequestException("Password doesn't match");

        if (passwordEncoder.matches(request.getNewPassword(), user.getPasswordHash()))
            throw new BadRequestException("New password cannot be the same as current password");

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
    }

    @Transactional
    public CompleteUserDto currentUser(){
        var userId = utilityService.getCurrentUserId();
        return getUserById(userId);
    }
}
