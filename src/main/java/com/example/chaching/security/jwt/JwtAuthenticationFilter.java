package com.example.chaching.security.jwt;

import com.example.chaching.security.UserDetailServiceImpl;
//import com.example.chaching.user.repository.LogoutAccessTokenRedisRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenUtil jwtTokenUtil;
  private final UserDetailServiceImpl userDetailsService;
//  private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String accessToken = jwtTokenUtil.getToken(request);
    if (StringUtils.hasText(accessToken)) {
//      checkLogout(accessToken);
      if (!jwtTokenUtil.validateToken(accessToken)) { // JWT 토큰 검증
        log.error("Token Error");
        return;
      }

      try {
        setAuthentication(jwtTokenUtil.getUsername(accessToken)); // 인증 처리
      } catch (Exception e) {
        log.error(e.getMessage());
        return;
      }
    }

    filterChain.doFilter(request, response);
  }

//  private void checkLogout(String accessToken) {
//    if (logoutAccessTokenRedisRepository.existsById(accessToken)) {
//      throw new IllegalArgumentException("이미 로그아웃된 회원입니다.");
//    }
//  }

  public void setAuthentication(String username) {
    // SecurityContextHolder : authentication을 담고 있는 Holder
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    // 인증 객체 생성
    Authentication authentication = createAuthentication(username);
    context.setAuthentication(authentication);

    // SecurityContextHolder에 인증된 객체를 저장하면, 해당 인증 객체를 전역적으로 사용할 수 있음
    SecurityContextHolder.setContext(context);
  }

  // 인증 객체 생성
  private Authentication createAuthentication(String username) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    return new UsernamePasswordAuthenticationToken(userDetails, null,
        userDetails.getAuthorities()); // 인증된 사용자의 정보와 권한을 담은 UsernamePaswordAuthenticationToken 생성
  }
}
