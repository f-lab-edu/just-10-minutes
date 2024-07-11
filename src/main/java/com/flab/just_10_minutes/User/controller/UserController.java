package com.flab.just_10_minutes.User.controller;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.dto.UserDto;
import com.flab.just_10_minutes.User.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<HttpStatus> signUpPublic(@RequestBody @Valid UserDto userDto) {
        userService.save(User.builder()
                .loginId(userDto.getLoginId())
                .password(userDto.getPassword())
                .phone(userDto.getPhone())
                .address(userDto.getAddress())
                .role(User.ROLE.PUBLIC)
                .build());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
