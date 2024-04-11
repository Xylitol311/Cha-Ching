package com.example.chaching.user.controller;


import com.example.chaching.security.UserDetailsImpl;
import com.example.chaching.user.dto.UserLoginDto;
import com.example.chaching.user.dto.UserRegisterDto;
import com.example.chaching.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.chaching.security.jwt.JwtTokenUtil.AUTHORIZATION_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterDto.Response> registerUser(
            @RequestBody @Valid UserRegisterDto.Request request) {
        UserRegisterDto.Response response = userService.registerUser(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify/{id}")
    public ResponseEntity verifyUserEmail(@PathVariable Long id) {
        userService.verifyUserEmail(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid UserLoginDto.Request request) {
        UserLoginDto.Response response = userService.login(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER, response.getAccessToken());
        headers.add("refreshToken", response.getRefreshToken());

        return ResponseEntity.ok().headers(headers).body("login success");
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String accessToken,
                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUserId();
        userService.logout(accessToken, username);
    }
}
