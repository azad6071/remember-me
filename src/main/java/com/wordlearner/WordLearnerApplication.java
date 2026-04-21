package com.wordlearner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WordLearnerApplication {
    public static void main(String[] args) {
        SpringApplication.run(WordLearnerApplication.class, args);
    }
}
