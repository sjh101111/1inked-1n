package com.example.oneinkedoneproject.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "follow")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {
    @Id
    @Column(name = "follow_id" , nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩
    @JoinColumn(name = "to_user", nullable = false)
    private User toUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user", nullable = false)
    private User fromUser;

    public Follow(String id, User toUser, User fromUser) {
        this.id = id;
        this.toUser = toUser;
        this.fromUser = fromUser;
    }
    public void changeFollow(User toUser) {
        this.toUser = toUser;
    }
}
