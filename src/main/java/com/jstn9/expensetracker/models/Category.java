package com.jstn9.expensetracker.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "category",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "user_id"})})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
