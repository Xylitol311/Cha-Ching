package com.example.chaching.user.service;


import com.example.chaching.user.dto.UserRegisterDto;

public interface UserService {
    UserRegisterDto.Response registerUser(UserRegisterDto.Request request);

    void verifyUserEmail(Long id);

}
