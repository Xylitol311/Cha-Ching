package com.example.chaching.user.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Users")
@EntityListeners(AuditingEntityListener.class)
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String userId;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String phone;

  @Column
  private LocalDate birth;

  @Enumerated(value = EnumType.STRING)
  private UserRole role;

  @Column(nullable = false)
  private boolean emailVerify;

  @CreatedDate
  @Column(nullable = false)
  private Date createdAt;

  @Column
  @LastModifiedDate
  private Date updatedAt;

  public void verifyUserEmail() {
    emailVerify = true;
  }
}
