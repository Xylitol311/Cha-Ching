package com.example.chaching.user.service.impl;

import com.example.chaching.exception.CustomException;
import com.example.chaching.exception.ErrorCode;
import com.example.chaching.user.domain.User;
import com.example.chaching.user.domain.UserRole;
import com.example.chaching.user.dto.UserRegisterDto;
import com.example.chaching.user.mail.MailComponent;
import com.example.chaching.user.repository.UserRepository;
import com.example.chaching.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailComponent mailComponent;

    @Override
    public UserRegisterDto.Response registerUser(UserRegisterDto.Request request) {
        registerUserDuplicateCheck(request);

        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        User user = userRepository.save(request.toEntity(encryptedPassword));

//        // CUSTOMER인 경우 회원가입 쿠폰 발급
//        if (user.getRole().equals(UserRole.CUSTOMER)) {
//            CouponIssuanceDto.Request couponRequest = new CouponIssuanceDto.Request(user.getId(),
//                    CouponType.MEMBERSHIP_SIGNUP_COUPON, null);
//            couponService.issuanceCoupon(couponRequest);
//        }

        mailComponent.sendVerifyLink(user.getId(), user.getEmail(), user.getName());

        return new UserRegisterDto.Response(user.getUserId());
    }

    private void registerUserDuplicateCheck(UserRegisterDto.Request request) {
        if (userRepository.existsByUserId(request.getUserId())) {
            throw new CustomException(ErrorCode.USERID_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.existsByPhone(request.getPhone())) {
            throw new CustomException(ErrorCode.PHONE_ALREADY_EXISTS);
        }
    }

    @Override
    @Transactional
    public void verifyUserEmail(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.verifyUserEmail();
    }

}
