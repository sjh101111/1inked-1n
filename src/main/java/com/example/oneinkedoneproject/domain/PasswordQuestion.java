package com.example.oneinkedoneproject.domain;

import com.example.oneinkedoneproject.utils.GenerateIdUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Table(name = "password_question")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordQuestion {
    @Id
    @Builder.Default
    @Column(name = "question_id", updatable = false, nullable = false)
    private String id = GenerateIdUtils.generateId();

    @Column(name = "question", nullable = false)
    private String question;

    public void updateQuestion(String question){
        this.question = question;
    }
}
