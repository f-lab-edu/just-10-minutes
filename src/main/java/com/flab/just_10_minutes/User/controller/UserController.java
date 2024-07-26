package com.flab.just_10_minutes.User.controller;

import com.flab.just_10_minutes.User.domain.User;
import com.flab.just_10_minutes.User.dto.UserDto;
import com.flab.just_10_minutes.User.dto.UserProfileDto;
import com.flab.just_10_minutes.User.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign_up")
    public ResponseEntity<HttpStatus> signUp(@RequestBody @Valid UserDto userDto) {
        userService.save(UserDto.toDomain(userDto));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/profile/{loginId}")
    public ResponseEntity<UserProfileDto> showUserProfile(@PathVariable final String loginId) {
        User user = userService.getUserProfile(loginId);

        return ResponseEntity.ok().body(UserProfileDto.builder()
                .loginId(user.getLoginId())
                .address(user.getAddress())
                .phone(user.getPhone())
                .role(user.getRole())
                .build());
    }
}
