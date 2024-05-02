package com.example.oneinkedoneproject.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "password_question")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordQuestion {
    @Id
    @Column(name = "question_id", updatable = false, nullable = false)
    private String id;

    @Column(name = "question", nullable = false)
    private String question;

    public void updateQuestion(String question){
        this.question = question;
    }
}
