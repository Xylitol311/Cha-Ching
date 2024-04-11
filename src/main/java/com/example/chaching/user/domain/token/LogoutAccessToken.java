package com.example.chaching.user.domain.token;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash("logoutAccessToken")
@AllArgsConstructor
@Builder
public class LogoutAccessToken {

  @Id
  private String id;

  private String username;

  @TimeToLive
  private Long expiration; // 설정한 시간만큼 데이터 저장

  public static LogoutAccessToken of(String accessToken, String username,
      Long remainingMilliSeconds) {
    return LogoutAccessToken.builder()
        .id(accessToken)
        .username(username)
        .expiration(remainingMilliSeconds / 1000)
        .build();
  }
}
