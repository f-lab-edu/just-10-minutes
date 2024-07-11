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
    public ResponseEntity<HttpStatus> signUp(@RequestBody @Valid UserDto userDto) {
        userService.save(UserDto.toDomain(userDto));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
