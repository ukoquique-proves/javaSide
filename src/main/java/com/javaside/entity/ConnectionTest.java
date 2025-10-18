package com.javaside.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "connection_test")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public ConnectionTest(String message) {
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }
}
