package com.elsawaf.app.jwtAuth.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "myapp_user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false , updatable = false)
    private Long id;
    private String userId;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    @NotNull
    private String userName;
    private String password;
    private String email;
    private String profileImageUrl;
    private Date lastLoginDate;
    private Date lastLoginDateDisplay;
    private Date joinedDate;
    private String role;
    private String[] authorities;
    private Boolean isActive;
    private Boolean isNotLocked;

}
