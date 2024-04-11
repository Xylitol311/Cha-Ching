package com.example.chaching.security;

import com.example.chaching.security.jwt.JwtAuthenticationFilter;
import com.example.chaching.security.jwt.JwtTokenUtil;
//import com.example.chaching.user.repository.LogoutAccessTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtTokenUtil jwtTokenUtil;
  private final UserDetailServiceImpl userDetailService;
//  private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;


  // 인증처리를 위한 AuthenticaitonManager
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

//  @Bean
//  public JwtAuthenticationFilter jwtAuthorizationFilter() {
//    return new JwtAuthenticationFilter(jwtTokenUtil, userDetailService,
//        logoutAccessTokenRedisRepository);
//  }

  // h2 console Spring Security 제외 설정
  @Bean
  @ConditionalOnProperty(name = "spring.h2.console.enabled", havingValue = "true")
  public WebSecurityCustomizer configureH2ConsoleEnable() {
    return web -> web.ignoring()
        .requestMatchers(PathRequest.toH2Console());
  }

  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .httpBasic((httpBasicConfig) ->
            httpBasicConfig.disable())
        .csrf((csrfConfig) ->
            csrfConfig.disable())
        .formLogin((formLoginConfig) ->
            formLoginConfig.disable())
        .authorizeHttpRequests((auth) ->
            auth.requestMatchers("/user/register").permitAll()
                .requestMatchers("/user/login").permitAll()
                .requestMatchers("/user/reissue").permitAll()
                .requestMatchers("/user/verify/{id}").permitAll()
                .requestMatchers("/user/address").hasRole("CUSTOMER")
                .requestMatchers("/user/find/userId").permitAll()
                .requestMatchers("/user/reset/**").permitAll()

                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/api-docs/**").permitAll()

                .requestMatchers("/coupon/admin/**").hasRole("ADMIN")
                .requestMatchers("/coupon/list").hasRole("CUSTOMER")

                .requestMatchers("/product").hasRole("SELLER")
                .requestMatchers("/product/**").hasRole("SELLER")
                .requestMatchers("/admin/product/**").hasRole("ADMIN")
                .requestMatchers("/search/**").permitAll()

                .requestMatchers("inquiry/admin/**").hasRole("ADMIN")

                .requestMatchers("/customer/order/**").hasRole("CUSTOMER")
                .requestMatchers("/seller/order/**").hasRole("SELLER")
                .anyRequest().authenticated())
        .logout((httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.disable()))
        .sessionManagement((sessionConfig) ->
            sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
