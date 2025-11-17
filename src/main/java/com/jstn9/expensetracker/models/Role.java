package com.jstn9.expensetracker.models;

import com.jstn9.expensetracker.models.enums.RoleNames;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "roles")
public class Role {

    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleNames name;
}
