package com.example.chaching.user.controller;


import com.example.chaching.user.dto.UserRegisterDto;
import com.example.chaching.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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



}
