package com.example.chaching.user.repository.token;

import com.example.chaching.user.domain.token.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutAccessTokenRedisRepository extends
    CrudRepository<LogoutAccessToken, String> {

}
