package com.example.chaching.user.dto;

import com.example.chaching.user.domain.User;
import com.example.chaching.user.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserRegisterDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        @NotBlank(message = "아이디는 필수 입력값입니다.")
        private String userId;

        @NotBlank(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,12}$")
        private String password;

        @NotBlank(message = "이름은 필수 입력값입니다.")
        private String name;

        @NotBlank
        @Email(message = "이메일 형식이 아닙니다.")
        private String email;

        @Size(max = 11, message = "ex) 01012345678")
        @NotBlank(message = "전화번호는 필수 입력값입니다.")
        private String phone;

        @NotNull(message = "생일은 필수 입력값입니다.")
        @Past(message = "생일은 현재보다 이전이어야 합니다.")
        private LocalDate birth;

        @NotNull
        private UserRole role;

        public User toEntity(String encryptedPassword) {
            return User.builder()
                    .userId(userId)
                    .password(encryptedPassword)
                    .name(name)
                    .email(email)
                    .phone(phone)
                    .birth(birth)
                    .role(role)
                    .emailVerify(false)
                    .createdAt(new Date())
                    .build();
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        private String userId;
    }
}