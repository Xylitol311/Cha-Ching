package com.example.chaching.security.jwt;

import com.example.chaching.exception.CustomException;
import com.example.chaching.exception.ErrorCode;
import com.example.chaching.user.domain.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static com.example.chaching.security.jwt.JwtExpirationEnums.ACCESS_TOKEN_EXPIRATION_TIME;
import static com.example.chaching.security.jwt.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;

@Slf4j
@Component
public class JwtTokenUtil {

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BEARER_PREFIX = "Bearer ";

  @Value("${jwt.secret}")
  private String SECRET_KEY;
  private static Key key;   // Secret key 를 담을 변수
  private static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  @PostConstruct      // 한 번만 받으면 값을 사용할 때마다, 매번 요청을 새로 호출하는 것을 방지
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(SECRET_KEY);
    key = Keys.hmacShaKeyFor(bytes);
  }

  public String generateAccessToken(String username, UserRole role) {
    Claims claims = Jwts.claims().setSubject(username);
    claims.put("auth", role);
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(
            new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME.getValue()))
        .signWith(key, signatureAlgorithm)
        .compact();
  }

  public String generateRefreshToken(String username) {
    Claims claims = Jwts.claims().setSubject(username);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(
            new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME.getValue()))
        .signWith(key, signatureAlgorithm)
        .compact();
  }

  public String getToken(HttpServletRequest request) {
    String headerAuth = request.getHeader(AUTHORIZATION_HEADER);
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(BEARER_PREFIX)) {
      return headerAuth.replace(BEARER_PREFIX, "");
    }
    return null;
  }

  public Boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException e) {
      log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
      throw new CustomException(ErrorCode.INVALID_JWT_SIGNATURE);
    } catch (ExpiredJwtException e) {
      log.error("Expired JWT token, 만료된 JWT token 입니다.");
      throw new CustomException(ErrorCode.EXPIRED_JWT_TOKEN);
    } catch (UnsupportedJwtException e) {
      log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
      throw new CustomException(ErrorCode.UNSUPPORTED_JWT_TOKEN);
    } catch (IllegalArgumentException e) {
      log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
      throw new CustomException(ErrorCode.JWT_CLAIMS_IS_EMPTY);
    }
  }

  public Claims extractAllClaims(String token) { // 토큰 추출
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public String getUsername(String token) {
    return extractAllClaims(token).getSubject();
  }

  public long getRemainMilliSeconds(String token) {
    Date expiration = extractAllClaims(token).getExpiration();
    Date now = new Date();
    return expiration.getTime() - now.getTime();
  }
}
