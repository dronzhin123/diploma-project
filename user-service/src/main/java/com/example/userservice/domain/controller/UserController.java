package com.example.userservice.domain.controller;

import com.example.userservice.domain.dto.UserCreateDto;
import com.example.userservice.domain.dto.UserDto;
import com.example.userservice.domain.entity.User;
import com.example.userservice.domain.mapper.UserMapper;
import com.example.userservice.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserCreateDto dto) {
        User user = userService.createUser(
                dto.firstname(), dto.lastname(),
                dto.email(), dto.phoneNumber()
        );
        return userMapper.toUserDto(user);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return userMapper.toUserDto(user);
    }

}
