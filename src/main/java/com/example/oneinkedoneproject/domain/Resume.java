package com.example.oneinkedoneproject.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.w3c.dom.Text;

import java.time.LocalDateTime;
@Setter
@Table(name = "resume")
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resume {
    @Id
    @Column(name = "resume_id" , nullable = false)
    private String id;

    @Column(name = "contents",nullable = false, length = 65535)
    private String contents;

    @Column(name = "created_at", nullable = false)
    @CreatedDate
    private LocalDateTime createAt;

    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = true)
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
