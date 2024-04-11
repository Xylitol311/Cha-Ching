package com.example.chaching.user.service;


import com.example.chaching.user.dto.UserLoginDto;
import com.example.chaching.user.dto.UserRegisterDto;

public interface UserService {
    UserRegisterDto.Response registerUser(UserRegisterDto.Request request);

    void verifyUserEmail(Long id);

    UserLoginDto.Response login(UserLoginDto.Request request);

    void logout(String accessToken, String username);

}
