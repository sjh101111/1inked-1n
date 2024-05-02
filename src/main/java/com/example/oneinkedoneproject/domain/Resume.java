package com.example.oneinkedoneproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.w3c.dom.Text;

import java.time.LocalDateTime;

@Table(name = "resume")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resume {
    @Id
    @Column(name = "resume_id" , nullable = false)
    private String id;

    @Column(name = "contents",nullable = false)
    private String contents;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createAt;

    @Column(name = "updated_at", nullable = true)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Resume(String id, String contents, User user) {
        this.id = id;
        this.contents = contents;
        this.createAt = LocalDateTime.now();
        this.user = user;
    }

    public void updateContents(String contents){
        this.contents = contents;
    }
}
