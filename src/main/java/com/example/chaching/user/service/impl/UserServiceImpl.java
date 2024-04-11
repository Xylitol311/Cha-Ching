package com.example.chaching.user.service.impl;

import com.example.chaching.config.CacheConfig;
import com.example.chaching.exception.CustomException;
import com.example.chaching.exception.ErrorCode;
import com.example.chaching.security.jwt.JwtTokenUtil;
import com.example.chaching.user.domain.User;
import com.example.chaching.user.domain.UserRole;
import com.example.chaching.user.domain.token.LogoutAccessToken;
import com.example.chaching.user.domain.token.RefreshToken;
import com.example.chaching.user.dto.UserLoginDto;
import com.example.chaching.user.dto.UserRegisterDto;
import com.example.chaching.user.mail.MailComponent;
import com.example.chaching.user.repository.UserRepository;
import com.example.chaching.user.repository.token.LogoutAccessTokenRedisRepository;
import com.example.chaching.user.repository.token.RefreshTokenRepository;
import com.example.chaching.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.NoSuchElementException;

import static com.example.chaching.security.jwt.JwtTokenUtil.BEARER_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailComponent mailComponent;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
    private final JwtTokenUtil jwtTokenUtil;


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

    @Override
    public UserLoginDto.Response login(UserLoginDto.Request request) {
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new NoSuchElementException("회원이 없습니다."));
        checkPassword(request.getPassword(), user.getPassword());

        String username = user.getUserId();
        String accessToken = jwtTokenUtil.generateAccessToken(username, user.getRole());

        RefreshToken refreshToken = saveRefreshToken(username);
        return UserLoginDto.Response.of(accessToken, refreshToken.getRefreshToken());
    }

    private void checkPassword(String rawPassword, String findMemberPassword) {
        if (!passwordEncoder.matches(rawPassword, findMemberPassword)) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }
    }

    private RefreshToken saveRefreshToken(String username) {
        if (refreshTokenRepository.existsById(username)) {
            refreshTokenRepository.deleteById(username);
        }
        return refreshTokenRepository.save(RefreshToken.createRefreshToken(username,
                jwtTokenUtil.generateRefreshToken(username)));
    }

    @CacheEvict(value = CacheConfig.CacheKey.USER, key = "#username")
    public void logout(String accessToken, String username) {
        accessToken = resolveToken(accessToken);
        long remainMilliSeconds = jwtTokenUtil.getRemainMilliSeconds(accessToken);
        refreshTokenRepository.deleteById(username);
        logoutAccessTokenRedisRepository.save(
                LogoutAccessToken.of(accessToken, username, remainMilliSeconds));
    }

    private String resolveToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return token.replace(BEARER_PREFIX, "");
        }
        return null;
    }
}
