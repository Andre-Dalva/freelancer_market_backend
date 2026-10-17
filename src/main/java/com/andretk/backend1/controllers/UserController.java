package com.andretk.backend1.controllers;

import com.andretk.backend1.dtos.*;
import com.andretk.backend1.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Tag(name = "Users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Returns all the users")
    public ResponseEntity<List<UserDto>> getAllUsers(
            @RequestParam(required = false, defaultValue = "id", name = "sort") String sort
    ){
        var users = userService.getAllUsers(sort);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Returns just one user")
    public ResponseEntity<CompleteUserDto> getUserById(@Parameter(description = "The Id of the user") @PathVariable Long id){

        var user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @Operation(summary = "Registers a user")
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder
            ){
        var newUser = userService.registerUser(request);

        var uri = uriBuilder.path("/users/{id}")
                .buildAndExpand(newUser.getId()).toUri();

        return ResponseEntity.created(uri).body(newUser);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a user")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "The Id of the user") @PathVariable Long id
    ){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me")
    @Operation(summary = "Updates a user")
    public  ResponseEntity<UserDto> updateUser(
            @RequestBody UpdateUserRequest request
    ){
        var user = userService.updateUser(request);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/change-password")
    @Operation(summary = "Changes the password of the user")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
            ){
        userService.changePassword(request);
        return ResponseEntity.noContent().build();
    }
}
