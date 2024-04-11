package com.example.chaching.user.repository.token;

import com.example.chaching.user.domain.token.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

  Optional<RefreshToken> findById(String username);

  Optional<RefreshToken> findByRefreshToken(String refreshToken);

  void deleteByRefreshToken(String refreshToken);

  boolean existsByRefreshToken(String refreshToken);
}
