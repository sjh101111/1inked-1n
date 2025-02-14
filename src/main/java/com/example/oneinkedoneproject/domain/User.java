package com.example.oneinkedoneproject.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Table(name= "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Builder
public class User implements UserDetails {
    @Id
    @Column(name = "user_id", nullable = false)
    private String id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "password_question", nullable = false)
    private String passwordQuestion;

    @Column(name = "password_answer", nullable = true)
    private String passwordAnswer;

    @Column(name = "identity", nullable = true)
    private String identity;

    @Column(name = "location", nullable = true)
    private String location;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "withdraw", nullable = false)
    private Boolean withdraw;

    // 프로필 사진
    @Column(name = "image", nullable = true)
    private Byte image;

    @Enumerated(EnumType.STRING)
    private Grade grade;

//    @OneToMany(mappedBy = "user")
//    private List<Comment> commentList;

//    @OneToMany(mappedBy = "user")
//    private List<Article> articleList;


    public User(String id, String username, String email, String password, String passwordQuestion, String passwordAnswer, String identity, String location, String description, Boolean withdraw, Byte image, Grade grade) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.passwordQuestion = passwordQuestion;
        this.passwordAnswer = passwordAnswer;
        this.identity = identity;
        this.location = location;
        this.description = description;
        this.withdraw = withdraw;
        this.image = image;
        this.grade = grade;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void updateName(String username) {
        this.username = username;
    }

}
