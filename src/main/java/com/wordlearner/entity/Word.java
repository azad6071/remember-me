package com.wordlearner.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String word;

    @Column(columnDefinition = "TEXT")
    private String englishMeaning;

    @Column(columnDefinition = "TEXT")
    private String hindiMeaning;

    @Column(columnDefinition = "TEXT")
    private String exampleSentence;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Word() {
    }

    public Word(String word, String englishMeaning, String hindiMeaning, String exampleSentence) {
        this.word = word;
        this.englishMeaning = englishMeaning;
        this.hindiMeaning = hindiMeaning;
        this.exampleSentence = exampleSentence;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getEnglishMeaning() {
        return englishMeaning;
    }

    public void setEnglishMeaning(String englishMeaning) {
        this.englishMeaning = englishMeaning;
    }

    public String getHindiMeaning() {
        return hindiMeaning;
    }

    public void setHindiMeaning(String hindiMeaning) {
        this.hindiMeaning = hindiMeaning;
    }

    public String getExampleSentence() {
        return exampleSentence;
    }

    public void setExampleSentence(String exampleSentence) {
        this.exampleSentence = exampleSentence;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}