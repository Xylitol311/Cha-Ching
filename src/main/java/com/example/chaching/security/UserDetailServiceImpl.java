package com.example.chaching.security;

//import com.example.chaching.config.CacheConfig;
import com.example.chaching.exception.CustomException;
import com.example.chaching.exception.ErrorCode;
import com.example.chaching.user.domain.User;
import com.example.chaching.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
//  @Cacheable(value = CacheConfig.CacheKey.USER, key = "#username", unless = "#result == null")
  // 캐시에서 먼저 회원을 조회 후 없으면 DB에서 조회
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUserId(username)
        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    return new UserDetailsImpl(user);
  }
}
