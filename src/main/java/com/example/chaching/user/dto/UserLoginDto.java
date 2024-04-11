package com.example.chaching.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static com.example.chaching.security.jwt.JwtTokenUtil.BEARER_PREFIX;

public class UserLoginDto {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String userId;
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    private String accessToken;
    private String refreshToken;

    public static Response of(String accessToken, String refreshToken) {
      return Response.builder()
          .accessToken(BEARER_PREFIX + accessToken)
          .refreshToken(BEARER_PREFIX + refreshToken)
          .build();
    }
  }
}
